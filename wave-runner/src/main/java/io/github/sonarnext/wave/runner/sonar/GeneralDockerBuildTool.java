package io.github.sonarnext.wave.runner.sonar;

import java.io.IOException;
import java.util.Map;

public class GeneralDockerBuildTool implements DockerBuildTool {
    @Override
    public String baseDockerfileName(String version) {
        return null;
    }

    @Override
    public String build(SonarProperties sonarBO, Map<String, String> properties) throws IOException, InterruptedException {
        // un impl
        return null;
    }
}
