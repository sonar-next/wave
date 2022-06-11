package io.github.sonarnext.wave.runner.docker;

import io.github.sonarnext.wave.runner.command.SonarScannerCommand;
import io.github.sonarnext.wave.runner.sonar.SonarConstant;
import io.github.sonarnext.wave.runner.sonar.SonarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;

import java.util.List;
import java.util.Map;

/**
 * @author magaofei
 * @date 1/5/21
 */
public class GradleDockerGenerator implements DockerGenerator {

    private static final Logger logger = LoggerFactory.getLogger(GradleDockerGenerator.class);

    /**
     * @param runCommand
     * @param properties
     * @return
     */
    @Override
    public String generate(List<String> runCommand, Map<String, String> properties, SonarProperties sonarProperties) {

        logger.info("GradleDockerGenerator");
        properties.put(SonarConstant.SOURCES, "src");
        properties.put(SonarConstant.TESTS, "src/test");

        String from = from();

        List<String> propertiesList = SonarScannerCommand.propertiesToString(properties);
        String propertiesString = String.join(" ", propertiesList);
        runCommand.add("JAVA_OPTS= ./gradlew sonarqube");
        String command = String.join(" && ", runCommand);
        return String.format("FROM %s" +
                "\n" +
                "WORKDIR " + sonarProperties.getSonarProjectBaseDir() +
                "\n" +
                "RUN " + command +
                "\n" +
                "CMD /bin/sh -c \" %s %s \"", from, command, propertiesString);
    }

    @Override
    public String from() {
        return "gradle-jdk";
    }
}
