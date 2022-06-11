package io.github.sonarnext.wave.runner.sonar;

import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsConnector;

public class SonarClient {

    public static WsConnector getConnector() {
        return HttpConnector.newBuilder()
                .build();
    }
}
