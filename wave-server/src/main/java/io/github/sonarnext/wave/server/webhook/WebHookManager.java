package io.github.sonarnext.wave.server.webhook;

import io.github.sonarnext.wave.server.webhook.gitlab.GitLabWebHook;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class WebHookManager {

    private final GitLabWebHook gitLabWebHook = new GitLabWebHook();

    /**
     * 1. switch webhook
     * @param request
     * @param webHookTypeEnum
     */
    public void handle(HttpServletRequest request, WebHookTypeEnum webHookTypeEnum) {
        WebHookReceiver webHookReceiver = this.getWebHookReceiver(webHookTypeEnum);
        Optional.ofNullable(webHookReceiver).ifPresent(receiver -> receiver.receive(request));
    }

    private WebHookReceiver getWebHookReceiver(WebHookTypeEnum webHookTypeEnum) {
        switch (webHookTypeEnum) {
            case GITHUB:
                break;
            case GITLAB:
                return gitLabWebHook;
            default:
                break;
        }
        return null;
    }

}
