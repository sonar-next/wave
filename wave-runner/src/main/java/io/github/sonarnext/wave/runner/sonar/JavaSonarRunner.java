package io.github.sonarnext.wave.runner.sonar;

import io.github.sonarnext.wave.runner.docker.SonarScannerDockerGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JavaSonarRunner extends AbstractSonarRunnerStrategy {

    private static final Logger logger = LoggerFactory.getLogger(JavaSonarRunner.class);

    private static final ThreadPoolExecutor MVN_BUILD_TOOL_THREAD_POOL =
            new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
    @Override
    String scan() throws IOException, InterruptedException {
        String result = "";
        List<String> runCommandList = new ArrayList<>();
        String dockerfile = this.dockerGenerator.generate(runCommandList, properties, getSonarProperties());
        this.getSonarProperties().setDockerfile(dockerfile);

        // mvn or gradle，增加超时机制
        try {
            result = MVN_BUILD_TOOL_THREAD_POOL.submit(() -> this.buildTool.build(this.getSonarProperties(), properties)).get(20, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error("e = ", e);
            // 失败后使用 SonarScanner 重试
            result += this.executeScanner();
        }
        return result;
    }

    protected String executeScanner() throws IOException, InterruptedException {
        logger.info("切换为SonarScanner Process 执行");
        String result = "";

        /*
         * 恢复 部分属性
         */
        Map<String, String> newProperties = this.getProperties();
        newProperties.put(SonarConstant.PROJECT_BASE_DIR, this.getSonarProperties().getSonarProjectBaseDir());
        newProperties.put(SonarConstant.ANALYSIS_COMPILING, String.valueOf(false));
        newProperties.put(SonarConstant.JAVA_BINARY, this.getSonarProperties().getSonarProjectBaseDir());

        this.dockerGenerator = new SonarScannerDockerGenerator();
        List<String> runCommandList = new ArrayList<>();
        String dockerfile = this.dockerGenerator.generate(runCommandList, newProperties, getSonarProperties());
        this.getSonarProperties().setDockerfile(dockerfile);
        try {
            result += this.buildTool.build(this.getSonarProperties(), newProperties);
        } catch (Exception ex) {
            logger.warn("SonarScannerProcessBuildTool error", ex);
            throw ex;
        }
        return result;
    }
}
