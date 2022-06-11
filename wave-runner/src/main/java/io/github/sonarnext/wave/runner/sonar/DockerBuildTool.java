package io.github.sonarnext.wave.runner.sonar;

public interface DockerBuildTool extends BuildTool {

    String baseDockerfileName(String version);

}
