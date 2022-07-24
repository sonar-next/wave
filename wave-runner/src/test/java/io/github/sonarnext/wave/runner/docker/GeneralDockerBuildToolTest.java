package io.github.sonarnext.wave.runner.docker;

import static org.junit.jupiter.api.Assertions.*;

import io.github.sonarnext.wave.common.test.BaseTestCase;
import io.github.sonarnext.wave.runner.sonar.SonarProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class GeneralDockerBuildToolTest extends BaseTestCase {

    @InjectMocks
    private GeneralDockerBuildTool self;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Build")
    void build() throws IOException, InterruptedException {
        SonarProperties sonarProperties = new SonarProperties();
        sonarProperties.setProjectPath("wave");
        Map<String, String> properties = new HashMap<>();
        self.build(sonarProperties, properties);
    }

    @Test
    @DisplayName("RunContainer")
    void RunContainer() {
    }

    @Test
    @DisplayName("TestRunContainer")
    void TestRunContainer() {
    }

    @Test
    @DisplayName("InitDirectory")
    void InitDirectory() {
    }

    @Test
    @DisplayName("GetTags")
    void GetTags() {
    }

    @Test
    @DisplayName("GetSourcePath")
    void GetSourcePath() {
    }

    @Test
    @DisplayName("InitAndImageId")
    void InitAndImageId() {
    }

    @Test
    @DisplayName("InitEnv")
    void InitEnv() {
    }

    @Test
    @DisplayName("ShowLog")
    void ShowLog() {
    }
}