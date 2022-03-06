package io.github.sonarnext.wave.server.webhook;

import javax.servlet.http.HttpServletRequest;

public interface WebHookService {
    String receive(HttpServletRequest payload, WebHookTypeEnum webHookTypeEnum);
}
