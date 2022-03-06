package io.github.sonarnext.wave.server.webhook;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WebHookServiceImpl implements WebHookService {

    private WebHookManager webHookManager = new WebHookManager();


    @Override
    public String receive(HttpServletRequest request, WebHookTypeEnum webHookTypeEnum) {
        this.webHookManager.handle(request, webHookTypeEnum);
        return "ok";
    }
}
