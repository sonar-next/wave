package io.github.sonarnext.wave.runner.sonar;

import com.google.common.base.Splitter;
import io.github.sonarnext.wave.runner.docker.SonarScannerDockerGenerator;
import io.github.sonarnext.wave.runner.exclude.CppcheckExcludePatternConvert;
import io.github.sonarnext.wave.runner.exclude.WildcardPatternConvert;
import org.sonar.api.CoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CppSonarRunner extends AbstractSonarRunnerStrategy {

    private static final String CPPCHECK = "cppcheck";

    private static final String CPPCHECK_REPORT_XML = "cppcheck-report.xml";

    private static final String SONAR_CPPCHECK_REPORTPATH = "cxx.cppcheck.reportPath";

    private static final WildcardPatternConvert EXCLUDE_PATTERN_CONVERT = new CppcheckExcludePatternConvert();


    @Override
    String scan() throws IOException, InterruptedException {

        Map<String, String> properties = this.getProperties();
        String exclusions = properties.get(CoreProperties.CATEGORY_EXCLUSIONS);

        String[] cppcheckCommand = this.cppcheckCommand(exclusions);

        String result = "";
        List<String> runCommandList = new ArrayList<>();
        List<String> cppcheckList = Arrays.stream(cppcheckCommand).collect(Collectors.toList());
        cppcheckList.add(this.getSonarProperties().getSonarProjectBaseDir());
        runCommandList.add(String.join(" ", cppcheckList));

//        String result = ProcessBuilderCommandLine.runCommand(cppcheckCommand, this.getSonarBO().getProjectBaseDir());
//        Path reportFilePath = Paths.get(this.getSonarBO().getProjectBaseDir(), CPPCHECK_REPORT_XML);
        //        Files.write(reportFilePath, result.getBytes());

        exclusions = exclusions + "," + CPPCHECK_REPORT_XML;
        properties.put(CoreProperties.CATEGORY_EXCLUSIONS, exclusions);
        properties.put(SONAR_CPPCHECK_REPORTPATH, CPPCHECK_REPORT_XML);
        // CPP 项目 删除 Java
        properties.remove(SonarConstant.JAVA_BINARY);
        properties.remove(SonarConstant.JAVA_TEST_FILE);
        properties.remove(SonarConstant.JAVA_SOURCE);

        this.dockerGenerator = new SonarScannerDockerGenerator();
        String dockerfile = this.dockerGenerator.generate(runCommandList, properties, getSonarProperties());
        this.getSonarProperties().setDockerfile(dockerfile);

        result += this.buildTool.build(this.getSonarProperties(), properties);
        return result;
    }

    private String[] cppcheckCommand(String exclusions) {
        List<String> command = new ArrayList<>(16);
        String[] exclude = EXCLUDE_PATTERN_CONVERT.convert(exclusions.split(","));
        command.add(CPPCHECK);
        command.add("--enable=all");
        command.add("--xml-version=2");
        for (String excludePath : exclude) {
            command.add("-i");
            command.add(excludePath);
        }
        command.add("./");

        command.add("2>");
        command.add(CPPCHECK_REPORT_XML);
        return command.toArray(new String[0]);
    }
}
