package io.github.sonarnext.wave.runner.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.github.sonarnext.wave.runner.command.CommandExitException;
import io.github.sonarnext.wave.runner.sonar.DockerBuildTool;
import io.github.sonarnext.wave.runner.sonar.SonarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GeneralDockerBuildTool implements DockerBuildTool {

    private static final String PATH_PRE = System.getProperty("wave", System.getProperty("user.home"));

    private static final String DOWNLOAD_HOST = PATH_PRE + "/wave/download";
    private static final String GO_CACHE_HOST = PATH_PRE + "/wave/.cache";
    private static final String GO_HOST = PATH_PRE + "/wave/go";
    private static final String SONAR_CACHE_HOST = PATH_PRE + "/wave/root/.sonar";
    private static final String M2_HOST = PATH_PRE + "/wave/.m2";
    private static final String NPM_HOST = PATH_PRE + "/wave/.npm";
    private static final String PUB_CACHE_HOST = PATH_PRE + "/wave/.pub-cache";
    private static final String GRADLE_HOST = PATH_PRE + "/wave/.gradle";
    private static final String ANDROID_SDK_HOST = PATH_PRE + "/wave/android-sdk";


    private static final Logger logger = LoggerFactory.getLogger(GeneralDockerBuildTool.class);

    @Override
    public String baseDockerfileName(String version) {
        return null;
    }

    @Override
    public String build(SonarProperties sonarProperties, Map<String, String> properties) throws IOException, InterruptedException {

        DockerClientConfig standard = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(standard.getDockerHost()).sslConfig(standard.getSSLConfig()).build();
        DockerClient dockerClient = DockerClientImpl.getInstance(standard, httpClient);

        logger.info("start create container ");

        StringBuilder result = new StringBuilder();

        String sourcePath = getSourcePath(sonarProperties);

        logger.info("source path = {}", sourcePath);

        Set<String> tags = getTags(sonarProperties);

        logger.info("tags = {}", tags);
        Path dockerfileDirectory = Paths.get("build", sonarProperties.getProjectKey());
        Path dockerfilePath = Paths.get("build", sonarProperties.getProjectKey(), "Dockerfile");
        initDirectory(dockerfileDirectory);

        File dockerfile = dockerfilePath.toFile();
        if (dockerfile.exists()) {
            Files.delete(dockerfile.toPath());
        }
        boolean createFileSuccess = dockerfile.createNewFile();

        if (!createFileSuccess) {
            logger.warn("create docker file fail");
            throw new IOException("create dockerfile fail");
        }

        try (OutputStream outputStream = Files.newOutputStream(dockerfile.toPath())){
            outputStream.write(sonarProperties.getDockerfile().getBytes());
        }
        sonarProperties.setDockerfilePath(dockerfilePath.toAbsolutePath().toString());

        String imageId = initAndImageId(dockerClient, result, tags, dockerfileDirectory, dockerfile);

        logger.info("created imageId = {}", imageId);

        HostConfig hostConfig = getHostConfig();

        runContainer(dockerClient, result, imageId, hostConfig);

        return result.toString();
    }

    protected static void runContainer(DockerClient dockerClient, StringBuilder result, String imageId, HostConfig hostConfig) throws CommandExitException {
        CreateContainerResponse containerResponse = null;
        try {
            try (CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageId)) {
                containerResponse = createContainerCmd
                        .withHostConfig(hostConfig)
                        .withTty(true)
                        .exec();
            }

            logger.info("start container cmd");
            // start
            dockerClient.startContainerCmd(containerResponse.getId()).exec();

            showLog(dockerClient, containerResponse.getId(), true, 1, new LogContainerTestCallback() {
                @Override
                public void onNext(Frame frame) {
                    String message = new String(frame.getPayload());
                    logger.info("{}", message);
                    result.append(message);
                    super.onNext(frame);
                }
            });

            logger.info("wait container cmd");
            try (WaitContainerResultCallback waitContainerResultCallback = new WaitContainerResultCallback()) {
                int exitCode = dockerClient.waitContainerCmd(containerResponse.getId()).exec(waitContainerResultCallback)
                        .awaitStatusCode(30, TimeUnit.MINUTES);
                logger.info("exitCode = {}", exitCode);
                if (exitCode != 0) {
                    throw new CommandExitException("exit code = " + exitCode, result.toString());
                }
            }
        } catch (CommandExitException e) {
            throw e;
        } catch (Exception e) {
            logger.warn("run image error ", e);
            throw new CommandExitException(e.getMessage(), result.toString());
        } finally {
            try {
                // 防止因报错导致没有
                if (containerResponse != null) {
                    logger.info("start stop container");
                    try {
                        dockerClient.stopContainerCmd(containerResponse.getId()).exec();
                    } catch (NotModifiedException e) {
                        logger.warn("Container already stopped");
                    }

                    logger.info("end stop container");

                    logger.info("start remove container id = {}", containerResponse.getId());
                    dockerClient.removeContainerCmd(containerResponse.getId()).exec();
                }
                logger.info("start remove image id = {}", imageId);
                dockerClient.removeImageCmd(imageId).exec();
            } catch (Exception e) {
                logger.warn("e", e);
            }

        }
    }

    private static void initDirectory(Path dockerfileDirectory) throws IOException {
        try {
            Files.createDirectory(dockerfileDirectory.getParent());
        } catch (FileAlreadyExistsException e) {
            logger.info("already build directory");
        }

        try {
            Files.createDirectory(dockerfileDirectory);
        } catch (FileAlreadyExistsException e) {
            logger.info("already build directory");
        }
    }

    private static Set<String> getTags(SonarProperties sonarProperties) throws CommandExitException {
        // Docker must use lowercase
        Set<String> tags = new HashSet<>();
        // 这里使用 http repo 是为了防止数据库中有 path 为空的问题
        if (sonarProperties.getHttpRepoToUrl().isEmpty()) {
            throw new CommandExitException("http url repo is empty");
        }

        String tag = sonarProperties.getNamespaceFullPath().toLowerCase() + "_" + sonarProperties.getProjectPath().toLowerCase() + ":" + sonarProperties.getCommitId().substring(0, 8);
        tag = tag.replace("-", "");
        if (tag.endsWith("_") || tag.startsWith("_")) {
            tag = tag.replace("_", "");
        }
        tags.add(tag);
        return tags;
    }

    private static String getSourcePath(SonarProperties sonarProperties) {
        String sourcePath = sonarProperties.getSonarSource();
        // 如果为空, 就使用 根目录
        if (StringUtils.hasLength(sourcePath)) {
            sourcePath = Paths.get(EnvironmentUtil.getDownloadFileAbsolutePath(), sonarProperties.getProjectPath()).toAbsolutePath().toString();
        } else {
            sourcePath = Paths.get(EnvironmentUtil.getDownloadFileAbsolutePath(), sonarProperties.getProjectPath(), sourcePath).toAbsolutePath().toString();
        }
        return sourcePath;
    }

    protected static String initAndImageId(DockerClient dockerClient, StringBuilder result, Set<String> tags, Path dockerfileDirectory, File dockerfile) throws CommandExitException {
        String imageId;
        try {
            imageId = dockerClient.buildImageCmd()
                    .withTags(tags)
                    .withPull(true)
                    .withBaseDirectory(dockerfileDirectory.toFile())
                    .withDockerfile(dockerfile)
                    .exec(new BuildImageResultCallback() {
                        @Override
                        public void onNext(BuildResponseItem item) {
                            if (item.getStream() != null) {
                                logger.info("{}", item.getStream());
                                result.append(item.getStream());
                            }
                            super.onNext(item);
                        }

                        @Override
                        public void onComplete() {
                            logger.info("build image finish");
                            super.onComplete();
                        }
                    })
                    .awaitImageId();
        } catch (Exception e) {
            logger.warn("build image error ", e);
            throw new CommandExitException(e.getMessage(), result.toString());
        }
        return imageId;
    }

    private static HostConfig getHostConfig() {
        Volume mavenRepositoryVolume = new Volume("/root/.m2");
        Volume downloadVolume = new Volume("/download");
        Volume goCacheVolume = new Volume("/root/.cache");
        Volume goVolume = new Volume("/go");
        Volume sonarCacheVolume = new Volume("/root/.sonar");
        Volume npmCacheVolume = new Volume("/root/.npm");
        Volume pubCacheVolume = new Volume("/root/.pub-cache");
        Volume gradleVolume = new Volume("/root/.gradle");
        Volume androidSdkVolume = new Volume("/android-sdk");
        HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(new Bind(M2_HOST, mavenRepositoryVolume), new Bind(DOWNLOAD_HOST, downloadVolume),
                new Bind(GO_HOST, goVolume), new Bind(SONAR_CACHE_HOST, sonarCacheVolume),
                new Bind(GO_CACHE_HOST, goCacheVolume), new Bind(NPM_HOST, npmCacheVolume),
                new Bind(PUB_CACHE_HOST, pubCacheVolume), new Bind(GRADLE_HOST, gradleVolume),
                new Bind(ANDROID_SDK_HOST, androidSdkVolume));
        return hostConfig;
    }

    public static void showLog(DockerClient dockerClient, String containerId, boolean follow, int numberOfLines, ResultCallback<Frame> logCallback) {
        dockerClient.logContainerCmd(containerId).withStdOut(true).withStdErr(true).withFollowStream(follow).withTail(numberOfLines).exec(logCallback);
    }
}
