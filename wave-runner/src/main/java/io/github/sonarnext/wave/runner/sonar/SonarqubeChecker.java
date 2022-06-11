package io.github.sonarnext.wave.runner.sonar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonarqube.ws.Webhooks;
import org.sonarqube.ws.client.webhooks.CreateRequest;
import org.sonarqube.ws.client.webhooks.ListRequest;
import org.sonarqube.ws.client.webhooks.WebhooksService;

/**
 * 检查 Sonarqube 的配置
 */
public class SonarqubeChecker {

    private static final Logger logger = LoggerFactory.getLogger(SonarqubeChecker.class);

    public void checkEnvironment(SonarProperties sonarProperties) {
        try {
            this.checkSonarWebHook();
        } catch (Exception e) {
            // 这里可能会发生请求超时等问题, 可以忽略
            logger.warn("checkSonarWebHook e ", e);
        }

        try {
            this.checkQualityProfiles(sonarProperties);
        } catch (Exception e) {
            // 这里可能会发生请求超时等问题, 可以忽略
            logger.warn("checkQualityProfiles e ", e);
        }
    }

    protected void checkQualityProfiles(SonarProperties sonarProperties) {
        // un impl
    }

    protected void checkSonarWebHook() {

        WebhooksService webhooksService = new WebhooksService(SonarClient.getConnector());
        ListRequest listRequest = new ListRequest();
        Webhooks.ListResponse webhookResponse = webhooksService.list(listRequest);
        if (!haveWebHook(webhookResponse)) {
            logger.info("exist web hook");
            return;
        }
        logger.info("not exist web hook, create one");
        CreateRequest createRequest = new CreateRequest();
        createRequest.setName("WAVE");
        createRequest.setUrl("");
        Webhooks.CreateWsResponse wsResponse = webhooksService.create(createRequest);
        logger.info("create web hook, {}", wsResponse);

    }

    private boolean haveWebHook(Webhooks.ListResponse webhookResponse) {
        if (webhookResponse.getWebhooksList().isEmpty()) {
            return false;
        }
        for (Webhooks.ListResponseElement listResponseElement : webhookResponse.getWebhooksList()) {
            // TODO URL
            if (listResponseElement.getUrl().equals("")) {
                return true;
            }
        }
        return false;
    }
}
