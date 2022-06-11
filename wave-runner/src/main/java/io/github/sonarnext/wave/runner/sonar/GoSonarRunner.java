package io.github.sonarnext.wave.runner.sonar;

import io.github.sonarnext.wave.runner.docker.SonarScannerDockerGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author magaofei
 * @date 1/6/21
 */
public class GoSonarRunner extends AbstractSonarRunnerStrategy {
    @Override
    String scan() throws IOException, InterruptedException {
        Map<String, String> properties = this.getProperties();

        this.dockerGenerator = new SonarScannerDockerGenerator();
        List<String> runCommandList = new ArrayList<>();
        // TODO 使用 多步构建
        String dockerfile = this.dockerGenerator.generate(runCommandList, properties, getSonarProperties());
        this.getSonarProperties().setDockerfile(dockerfile);
        return this.buildTool.build(this.getSonarProperties(), properties);
    }
}
