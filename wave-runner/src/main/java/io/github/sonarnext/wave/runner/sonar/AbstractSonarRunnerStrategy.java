package io.github.sonarnext.wave.runner.sonar;

import io.github.sonarnext.wave.runner.docker.DockerGenerator;
import org.sonar.api.CoreProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSonarRunnerStrategy {

    /**
     * sonar-scanner 执行参数
     */
    protected final Map<String, String> properties = new HashMap<>(64);

    protected final Map<String, String> configMap = new HashMap<>(32);

    private SonarProperties sonarProperties;

    BuildTool buildTool;

    /**
     * 生成 dockerfile
     */
    DockerGenerator dockerGenerator;

    abstract String scan() throws IOException, InterruptedException;

    public Map<String, String> getProperties() {
        properties.put(CoreProperties.PROJECT_KEY_PROPERTY, this.getSonarProperties().getProjectKey());
        properties.put(SonarConstant.SOURCES, this.getSonarProperties().getSonarSource());
        properties.put(SonarConstant.HOST_URL, this.getSonarProperties().getSonarHostUrl());
        properties.put(SonarConstant.PROJECT_BASE_DIR, this.getSonarProperties().getSonarProjectBaseDir());
        properties.put(CoreProperties.SCM_PROVIDER_KEY, SonarConstant.GIT);
        properties.put(CoreProperties.ENCODING_PROPERTY, SonarConstant.UTF8_ENCODING);
        properties.put(CoreProperties.LANGUAGE_SPECIFIC_PARAMETERS_LANGUAGE_KEY, this.getSonarProperties().getLanguage());
        properties.put(SonarConstant.ANALYSIS_AGENT_URL, this.getSonarProperties().getAgentUrl());
        properties.put(SonarConstant.ANALYSIS_COMMIT, this.getSonarProperties().getCommitId());
        properties.put(SonarConstant.ANALYSIS_START_TIME, String.valueOf(this.getSonarProperties().getStartTime()));
        properties.put(SonarConstant.ANALYSIS_COMPILING, String.valueOf(this.getSonarProperties().getCompiling()));
        properties.put(SonarConstant.COMMIT_EMAIL, String.valueOf(this.getSonarProperties().getCommitEmail()));
        properties.put(SonarConstant.ANALYSIS_TASK_ID, String.valueOf(this.getSonarProperties().getTaskId()));
        properties.put(SonarConstant.ANALYSIS_BRANCH, String.valueOf(this.getSonarProperties().getBranch()));
        return properties;
    }

    public SonarProperties getSonarProperties() {
        return sonarProperties;
    }

    public void setSonarProperties(SonarProperties sonarProperties) {
        this.sonarProperties = sonarProperties;
    }
}
