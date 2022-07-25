package io.github.sonarnext.wave.runner.sonar;

import io.github.sonarnext.wave.common.vo.TaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SonarProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SonarProcessor.class);

    private final SonarqubeChecker sonarqubeChecker = new SonarqubeChecker();
    /**
     * 1. 代码拉取
     * 2. 执行扫描
     *    - docker
     *    - shell
     *
     * @param task
     */
    public void process(TaskVO task) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        SonarProperties sonarProperties = this.initSonarProperties(task);
        sonarqubeChecker.checkEnvironment(sonarProperties);
        AbstractSonarRunnerStrategy sonarRunnerStrategy = SonarRunnerSimpleFactory.createSonarRunner(sonarProperties);
        String result = sonarRunnerStrategy.scan();
        logger.info("scan result: {}", result);

        logger.info("end process, cost {} ms", System.currentTimeMillis() - startTime);

    }

    private SonarProperties initSonarProperties(TaskVO task) {
        return new SonarProperties();
    }
}
