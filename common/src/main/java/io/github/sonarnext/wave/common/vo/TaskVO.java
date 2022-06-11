package io.github.sonarnext.wave.common.vo;

public class TaskVO {

    private String id; // task id
    private String name; // task name
    private String projectName; // project name
    private String branch; // task branch
    private Long startTime; // task start time
    private String language; // task language
    private String gitHost; // git url
    private String namespaceFullPath; // git namespace full path
    private String repositoryPath; // git repository path
    private String sonarHost; // sonar url
    private String commitId; // git commit id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGitHost() {
        return gitHost;
    }

    public void setGitHost(String gitHost) {
        this.gitHost = gitHost;
    }

    public String getNamespaceFullPath() {
        return namespaceFullPath;
    }

    public void setNamespaceFullPath(String namespaceFullPath) {
        this.namespaceFullPath = namespaceFullPath;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public String getSonarHost() {
        return sonarHost;
    }

    public void setSonarHost(String sonarHost) {
        this.sonarHost = sonarHost;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    @Override
    public String toString() {
        return "TaskVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", projectName='" + projectName + '\'' +
                ", branch='" + branch + '\'' +
                ", startTime=" + startTime +
                ", language='" + language + '\'' +
                ", gitHost='" + gitHost + '\'' +
                ", namespaceFullPath='" + namespaceFullPath + '\'' +
                ", repositoryPath='" + repositoryPath + '\'' +
                ", sonarHost='" + sonarHost + '\'' +
                ", commitId='" + commitId + '\'' +
                '}';
    }
}
