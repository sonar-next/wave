package io.github.sonarnext.wave.runner.sonar;

public class SonarProperties {

    private String projectKey;
    private String sonarSource;
    private String sonarHostUrl;
    private String sonarProjectBaseDir;
    private String language;
    private String agentUrl;
    private String commitId;
    private Long startTime;
    private Boolean compiling;
    private String commitEmail;
    private String taskId;
    private String branch;

    private String dockerfile;

    private String httpRepoToUrl;

    private String namespaceFullPath;
    private String projectPath;

    private String dockerfilePath;

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getSonarSource() {
        return sonarSource;
    }

    public void setSonarSource(String sonarSource) {
        this.sonarSource = sonarSource;
    }

    public String getSonarHostUrl() {
        return sonarHostUrl;
    }

    public void setSonarHostUrl(String sonarHostUrl) {
        this.sonarHostUrl = sonarHostUrl;
    }

    public String getSonarProjectBaseDir() {
        return sonarProjectBaseDir;
    }

    public void setSonarProjectBaseDir(String sonarProjectBaseDir) {
        this.sonarProjectBaseDir = sonarProjectBaseDir;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAgentUrl() {
        return agentUrl;
    }

    public void setAgentUrl(String agentUrl) {
        this.agentUrl = agentUrl;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Boolean getCompiling() {
        return compiling;
    }

    public void setCompiling(Boolean compiling) {
        this.compiling = compiling;
    }

    public String getCommitEmail() {
        return commitEmail;
    }

    public void setCommitEmail(String commitEmail) {
        this.commitEmail = commitEmail;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDockerfile() {
        return dockerfile;
    }

    public void setDockerfile(String dockerfile) {
        this.dockerfile = dockerfile;
    }

    public String getHttpRepoToUrl() {
        return httpRepoToUrl;
    }

    public void setHttpRepoToUrl(String httpRepoToUrl) {
        this.httpRepoToUrl = httpRepoToUrl;
    }

    public String getNamespaceFullPath() {
        return namespaceFullPath;
    }

    public void setNamespaceFullPath(String namespaceFullPath) {
        this.namespaceFullPath = namespaceFullPath;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public void setDockerfilePath(String dockerfilePath) {
        this.dockerfilePath = dockerfilePath;
    }

    public String getDockerfilePath() {
        return dockerfilePath;
    }
}
