package io.github.sonarnext.wave.runner.command;

import io.github.sonarnext.wave.runner.sonar.SonarConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author magaofei
 */
public class SonarScannerCommand {

    private static final Logger logger = LoggerFactory.getLogger(SonarScannerCommand.class);

    /**
     *
     * @return
     */
    public static List<String> propertiesToString(final Map<String, String> properties) {
        List<String> commands = new ArrayList<>(properties.size() + 10);
        for (Map.Entry<String, String> map : properties.entrySet()) {
            String command = SonarConstant.PROPERTIES_PREFIX + SonarConstant.SONAR + "." + map.getKey() + "=" + map.getValue();
            commands.add(command);
        }
        return commands;
    }

    /**
     * add sonar prefix
     * sonar.source   /path/source
     * sonar.projectKey
     * @return
     */
    public static Map<String, String> getSonarProperties(final Map<String, String> properties) {
        Map<String, String> result = new HashMap<>(properties.size() + 10);
        for (Map.Entry<String, String> map : properties.entrySet()) {
            String key = SonarConstant.SONAR + "." + map.getKey();
            result.put(key, map.getValue());
        }
        return result;
    }
}
