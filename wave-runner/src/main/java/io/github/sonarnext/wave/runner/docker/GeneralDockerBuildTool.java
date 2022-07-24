package io.github.sonarnext.wave.runner.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.*;
import com.google.common.collect.Lists;
import io.github.sonarnext.wave.runner.command.CommandExitException;
import io.github.sonarnext.wave.runner.command.SonarScannerCommand;
import io.github.sonarnext.wave.runner.config.ActionConfig;
import io.github.sonarnext.wave.runner.run.ContainerExecutor;
import io.github.sonarnext.wave.runner.run.DockerExecutor;
import io.github.sonarnext.wave.runner.sonar.DockerBuildTool;
import io.github.sonarnext.wave.runner.sonar.SonarConstant;
import io.github.sonarnext.wave.runner.sonar.SonarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public String build(SonarProperties sonarProperties, Map<String, String> properties) throws IOException, InterruptedException {

//        String sourcePath = this.getSourcePath(sonarProperties);

        this.runContainer(sonarProperties, properties);

        return "";
    }

    protected String runContainer(SonarProperties sonarProperties, Map<String, String> properties) throws InterruptedException {
        ContainerExecutor executor = new DockerExecutor();
        ActionConfig actionConfig = this.convertToActionConfig(sonarProperties, properties);
        executor.init(actionConfig);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(executor);
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.MINUTES);
        String imageId = executor.getImageId();
        logger.info("created imageId = {}", imageId);
        return imageId;
    }

    protected ActionConfig convertToActionConfig(SonarProperties sonarProperties, Map<String, String> properties) {
        ActionConfig actionConfig = new ActionConfig();
        actionConfig.setName(sonarProperties.getProjectKey() + "_" + sonarProperties.getBranch());
        Map<String, String> env = new HashMap<>();
        // volume
        actionConfig.setEnv(env);
        LinkedHashMap<String, ActionConfig.Job> jobs = new LinkedHashMap<>();
        ActionConfig.Job job = new ActionConfig.Job();
        env.put("workDir", sonarProperties.getSonarProjectBaseDir());
        // image name
        job.setRunsOn(this.getImageName(sonarProperties.getLanguage(), env));
        job.setEnvironment(env);
        job.setSteps(this.getSteps(sonarProperties, properties));
        job.setVolumes(this.initVolume());
        jobs.put("foo", job);
        actionConfig.setJobs(jobs);
        ActionConfig.On on = new ActionConfig.On();
        ActionConfig.Branch branch = new ActionConfig.Branch();
        branch.setBranches(Lists.newArrayList(sonarProperties.getBranch()));
        on.setPush(branch);
        actionConfig.setOn(on);
        return actionConfig;
    }

    protected String getImageName(String language, Map<String, String> env) {
        return "adoptopenjdk/maven-openjdk8";
    }

    protected List<ActionConfig.Step> getSteps(SonarProperties sonarProperties, Map<String, String> properties) {
        List<ActionConfig.Step> result = new ArrayList<>();
        ActionConfig.Step step = new ActionConfig.Step();
        step.setName("build");
        // 这里如果写根路径会报 index twice 错误

        this.initProperties(properties);
        List<String> commandString = SonarScannerCommand.propertiesToString(properties);

        String propertiesString = String.join(" ", commandString);
        List<String> runCommand = new ArrayList<>();

        this.getRunCommandForLanguage(runCommand);
        String command = String.join(" && ", runCommand);

        step.setRun(Lists.newArrayList(command, propertiesString));
        result.add(step);

        return result;
    }

    protected void getRunCommandForLanguage(List<String> runCommand) {
        runCommand.add("mvn -T 1C compile --settings /root/.m2/conf/settings.xml");
        runCommand.add("mvn sonar:sonar --settings /root/.m2/conf/settings.xml");
    }

    /**
     * init properties for each language
     * @param properties
     */
    protected void initProperties(Map<String, String> properties) {
        properties.put(SonarConstant.SOURCES, "\"pom.xml,src/main\"");
        properties.put(SonarConstant.TESTS, ".");
        properties.put(SonarConstant.PROJECT_BASE_DIR, ".");
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

            LogContainerTestCallback logContainerTestCallback = new LogContainerTestCallback(true);
            showLog(dockerClient, containerResponse.getId(), true, 1, logContainerTestCallback);

            logger.info("wait container cmd");
            try (WaitContainerResultCallback waitContainerResultCallback = new WaitContainerResultCallback()) {
                int exitCode = dockerClient.waitContainerCmd(containerResponse.getId()).exec(waitContainerResultCallback)
                        .awaitStatusCode(10, TimeUnit.MINUTES);
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

    protected void initDirectory(Path dockerfileDirectory) throws IOException {
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

    protected Set<String> getTags(SonarProperties sonarProperties) throws CommandExitException {
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

    protected String getSourcePath(SonarProperties sonarProperties) {
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
        hostConfig.withBinds(
                new Bind(M2_HOST, mavenRepositoryVolume), new Bind(DOWNLOAD_HOST, downloadVolume),
                new Bind(GO_HOST, goVolume), new Bind(SONAR_CACHE_HOST, sonarCacheVolume),
                new Bind(GO_CACHE_HOST, goCacheVolume), new Bind(NPM_HOST, npmCacheVolume),
                new Bind(PUB_CACHE_HOST, pubCacheVolume), new Bind(GRADLE_HOST, gradleVolume),
                new Bind(ANDROID_SDK_HOST, androidSdkVolume));
        return hostConfig;
    }

    private List<String> initVolume() {
        List<String> result = new ArrayList<>();
        result.add(M2_HOST + ":" + "/root/.m2");
        result.add(DOWNLOAD_HOST + ":" + "/download");
        result.add(GO_HOST + ":" + "/go");
        result.add(SONAR_CACHE_HOST + ":" + "/root/.sonar");
        result.add(GO_CACHE_HOST + ":" + "/root/.cache");
        result.add(NPM_HOST + ":" + "/root/.npm");
        result.add(PUB_CACHE_HOST + ":" + "/root/.pub-cache");
        result.add(GRADLE_HOST + ":" + "/root/.gradle");
        result.add(ANDROID_SDK_HOST + ":" + "/android-sdk");
        return result;
    }

    public static void showLog(DockerClient dockerClient, String containerId, boolean follow, int numberOfLines, ResultCallback<Frame> logCallback) {
        dockerClient.logContainerCmd(containerId).withStdOut(true).withStdErr(true).withFollowStream(follow).withTail(numberOfLines).exec(logCallback);
    }
}
