package io.github.sonarnext.wave.runner.sonar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author magaofei
 */
public class FrontSonarRunner extends AbstractSonarRunnerStrategy{

    private static final Logger logger = LoggerFactory.getLogger(FrontSonarRunner.class);

    @Override
    public String scan() throws IOException, InterruptedException {
        // 如果 有 ESLint 的 BuildTools，则执行，并且导入

        String result = "";
        /*
         * 防止语言检测错误，兼容Java
         */
        Map<String, String> properties = this.getProperties();
        properties.put(SonarConstant.JAVA_BINARY, this.getSonarProperties().getSonarSource());

        List<String> runCommandList = new ArrayList<>();
        String dockerfile = this.dockerGenerator.generate(runCommandList, properties, getSonarProperties());
        this.getSonarProperties().setDockerfile(dockerfile);
        return result + this.buildTool.build(this.getSonarProperties(), properties);
    }

}
