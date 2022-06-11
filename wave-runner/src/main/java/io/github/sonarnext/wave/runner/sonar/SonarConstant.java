package io.github.sonarnext.wave.runner.sonar;

public class SonarConstant {
    public static final String SOURCES = ".";

    public static final String SONAR = "sonar";

    public static final String HOST_URL = "host.url";

    public static final String PROPERTIES_PREFIX = "-D";

    public static final String TESTS = "tests";
    public static final String PROJECT_BASE_DIR = "projectBaseDir";

    public static final String JAVA_BINARY = "java.binaries";

    public static final String JAVA_TEST_FILE = "src/test/*.java";

    public static final String JAVA_SOURCE = "java.source";

    public static final String ANALYSIS = "analysis";
    public static final String ANALYSIS_COMMIT = ANALYSIS + ".commit";
    public static final String ANALYSIS_COMMIT_TITLE = ANALYSIS_COMMIT + ".title";
    public static final String ANALYSIS_LANGUAGE = ANALYSIS + ".language";
    public static final String SONAR_ANALYSIS_LANGUAGE = String.join(".", SONAR, ANALYSIS_LANGUAGE);
    public static final String ANALYSIS_AGENT_URL = ANALYSIS + ".agent.url";
    public static final String SONAR_ANALYSIS_AGENT_URL = SONAR + "." + ANALYSIS_AGENT_URL;
    public static final String ANALYSIS_START_TIME = ANALYSIS + ".start.time";
    public static final String SONAR_ANALYSIS_START_TIME = SONAR + "." + ANALYSIS_START_TIME;
    public static final String ANALYSIS_COMPILING = ANALYSIS + ".compiling";
    public static final String SONAR_ANALYSIS_COMPILING = SONAR + "." + ANALYSIS_COMPILING;
    public static final String ANALYSIS_PACK = ANALYSIS + ".pack";
    public static final String SONAR_ANALYSIS_PACK = SONAR + "." + ANALYSIS_PACK;
    public static final String ANALYSIS_REPOSITORY_SIZE = ANALYSIS + ".repository.size";
    public static final String SONAR_ANALYSIS_REPOSITORY_SIZE = SONAR + "." + ANALYSIS_REPOSITORY_SIZE;

    public static final String COMMIT_EMAIL = ANALYSIS + ".commit.email";
    public static final String SONAR_ANALYSIS_COMMIT_EMAIL = SONAR + "." + COMMIT_EMAIL;

    public static final String ANALYSIS_TASK_ID = ANALYSIS + ".task.id";
    public static final String SONAR_ANALYSIS_TASK_ID = SONAR + "." + ANALYSIS_TASK_ID;

    public static final String ANALYSIS_PROJECT_ID = ANALYSIS + ".project.id";
    public static final String SONAR_ANALYSIS_PROJECT_ID = SONAR + "." + ANALYSIS_PROJECT_ID;

    public static final String ANALYSIS_BRANCH = ANALYSIS + ".branch";
    public static final String SONAR_ANALYSIS_BRANCH = SONAR + "." + ANALYSIS_BRANCH;

    public static final String SONAR_ANALYSIS_COMMIT = SONAR + "." + ANALYSIS_COMMIT;

    public static final String ANALYSIS_JAVA_VERSION = ANALYSIS + "." + "java.version";

    public static final String ANALYSIS_TARGET_BRANCH = ANALYSIS + "." + "targetBranch";
    public static final String SONAR_TARGET_BRANCH = SONAR + "." + ANALYSIS_TARGET_BRANCH;

    public static final String ANALYSIS_TARGET_BRANCH_COMMIT_ID = ANALYSIS + "." + "targetBranch.commitId";
    public static final String SONAR_TARGET_BRANCH_COMMIT_ID = SONAR + "." + ANALYSIS_TARGET_BRANCH_COMMIT_ID;

    public static final String ANALYSIS_BRANCH_KEY = ANALYSIS + "." + "branchKey";
    public static final String SONAR_BRANCH_KEY = SONAR + "." + ANALYSIS_BRANCH_KEY;
    public static final String GIT = "git";
    public static final String UTF8_ENCODING = "UTF-8";
}
