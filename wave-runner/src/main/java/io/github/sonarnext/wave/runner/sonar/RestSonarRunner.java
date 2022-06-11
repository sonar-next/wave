package io.github.sonarnext.wave.runner.sonar;

import io.github.sonarnext.wave.runner.docker.SonarScannerDockerGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * php python js
 *
 * @author magaofei
 */
public class RestSonarRunner extends AbstractSonarRunnerStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RestSonarRunner.class);

    @Override
    public String scan() throws IOException, InterruptedException {
        Map<String, String> properties = this.getProperties();
        properties.put(SonarConstant.JAVA_BINARY, this.getSonarProperties().getSonarSource());

        this.dockerGenerator = new SonarScannerDockerGenerator();
        List<String> runCommandList = new ArrayList<>();
        String dockerfile = this.dockerGenerator.generate(runCommandList, properties, getSonarProperties());
        this.getSonarProperties().setDockerfile(dockerfile);
        return this.buildTool.build(this.getSonarProperties(), properties);

    }

}
