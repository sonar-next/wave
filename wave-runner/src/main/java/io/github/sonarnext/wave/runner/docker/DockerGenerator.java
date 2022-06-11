package io.github.sonarnext.wave.runner.docker;

import io.github.sonarnext.wave.runner.sonar.SonarProperties;

import java.util.List;
import java.util.Map;

public interface DockerGenerator {

    String generate(List<String> runCommand, Map<String, String> properties, SonarProperties sonarProperties);

    String from();
}
