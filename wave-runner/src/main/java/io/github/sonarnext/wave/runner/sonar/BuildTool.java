package io.github.sonarnext.wave.runner.sonar;

import java.io.IOException;
import java.util.Map;

public interface BuildTool {

    String build(SonarProperties sonarBO, Map<String, String> properties) throws IOException, InterruptedException;

}
