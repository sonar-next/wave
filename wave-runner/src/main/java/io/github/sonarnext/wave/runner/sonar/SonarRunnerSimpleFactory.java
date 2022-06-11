package io.github.sonarnext.wave.runner.sonar;

import io.github.sonarnext.wave.runner.docker.SonarScannerDockerGenerator;

public class SonarRunnerSimpleFactory {

    public static AbstractSonarRunnerStrategy createSonarRunner(SonarProperties sonarProperties) {
        String language = sonarProperties.getLanguage();
        AbstractSonarRunnerStrategy sonarRunner;
        LanguageEnum languageEnum = LanguageEnum.getLanguageFromName(language);
        switch (languageEnum) {
            case JAVA:
                sonarRunner = new JavaSonarRunner();
                break;
            case C:
            case CPP:
                sonarRunner = new CppSonarRunner();
                break;

            // iOS 仅支持按照 shell 的方式去执行
            case OBJECTIVE_C:
            case SWIFT:
//                sonarRunner = new IosSonarRunner2();
//                sonarRunner.buildTool = new SonarScannerProcessBuildTool();
                throw new UnsupportedOperationException();

            case CSS:
            case HTML:
            case VUE:
            case JAVA_SCRIPT:
            case REACT:
                sonarRunner = new FrontSonarRunner();
                break;
            case DART:
//                sonarRunner = new FlutterSonarRunner();
                throw new UnsupportedOperationException();
            case GO:
                sonarRunner = new GoSonarRunner();
                break;
            default:
                sonarRunner = new RestSonarRunner();

        }

        sonarRunner.buildTool = new GeneralDockerBuildTool();
        // 设置默认的 docker generator
        sonarRunner.dockerGenerator = new SonarScannerDockerGenerator();
        sonarRunner.setSonarProperties(sonarProperties);
        return sonarRunner;
    }

}
