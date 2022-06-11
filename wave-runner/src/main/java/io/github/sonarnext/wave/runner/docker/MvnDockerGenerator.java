package io.github.sonarnext.wave.runner.docker;

import io.github.sonarnext.wave.runner.command.SonarScannerCommand;
import io.github.sonarnext.wave.runner.sonar.SonarConstant;
import io.github.sonarnext.wave.runner.sonar.SonarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author magaofei
 * @date 1/5/21
 */
public class MvnDockerGenerator implements DockerGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MvnDockerGenerator.class);

    @Override
    public String generate(List<String> runCommand, Map<String, String> properties, SonarProperties sonarProperties) {

        logger.info("MvnDockerGenerator");

        // 这里如果写根路径会报 index twice 错误
        properties.put(SonarConstant.SOURCES, "\"pom.xml,src/main\"");
        properties.put(SonarConstant.TESTS, ".");
        properties.put(SonarConstant.PROJECT_BASE_DIR, ".");
        String from = from();

        List<String> propertiesList = SonarScannerCommand.propertiesToString(properties);
        String propertiesString = String.join(" ", propertiesList);
        runCommand.add("mvn -T 1C compile --settings /root/.m2/conf/settings.xml");
        runCommand.add("mvn sonar:sonar --settings /root/.m2/conf/settings.xml");
        String command = String.join(" && ", runCommand);
        return String.format("FROM %s" +
                "\n" +
                "WORKDIR " + sonarProperties.getSonarProjectBaseDir() +
                "\n" +
                "CMD /bin/sh -c \" %s %s \"", from, command, propertiesString);
    }

    @Override
    public String from() {
        return "mvn";
    }


}
