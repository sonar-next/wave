package io.github.sonarnext.wave.server.webhook;

import javax.servlet.http.HttpServletRequest;

public interface WebHookReceiver {

    void receive(HttpServletRequest request);

}
