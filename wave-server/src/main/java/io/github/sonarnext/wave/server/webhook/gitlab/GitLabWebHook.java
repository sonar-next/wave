package io.github.sonarnext.wave.server.webhook.gitlab;

import io.github.sonarnext.wave.server.webhook.WebHookReceiver;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.ProjectHook;
import org.gitlab4j.api.systemhooks.SystemHookManager;
import org.gitlab4j.api.utils.HttpRequestUtils;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.webhook.Event;
import org.gitlab4j.api.webhook.WebHookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GitLabWebHook implements WebHookReceiver {

    private static final Logger logger = LoggerFactory.getLogger(GitLabWebHook.class);

    public static final WebHookManager HOOK_MANAGER = new WebHookManager();
    public static final SystemHookManager SYSTEM_HOOK_MANAGER = new SystemHookManager();
    public static final ProjectHook PROJECT_HOOK = new ProjectHook();

    private static final JacksonJson JACKSON_JSON = new JacksonJson();

    private static final GitLabWebHookListener gitLabWebHookListener = new GitLabWebHookListener();

    static {
        PROJECT_HOOK.setPushEvents(true);
        PROJECT_HOOK.setBuildEvents(true);
        PROJECT_HOOK.setEnableSslVerification(true);
        PROJECT_HOOK.setIssuesEvents(true);
        PROJECT_HOOK.setJobEvents(true);
        PROJECT_HOOK.setMergeRequestsEvents(true);
        PROJECT_HOOK.setWikiPageEvents(true);
        PROJECT_HOOK.setTagPushEvents(true);
        PROJECT_HOOK.setNoteEvents(true);
        PROJECT_HOOK.setPipelineEvents(true);

        HOOK_MANAGER.addListener(gitLabWebHookListener);
    }

    @Override
    public void receive(HttpServletRequest request) {

        String postData = "";
        Event event;
        try {
            postData = HttpRequestUtils.getPostDataAsString(request);
            event = JACKSON_JSON.unmarshal(Event.class, postData);
        } catch (IOException e) {
            logger.warn("get post data error e = ", e);
            return;
        }

        try {
            HOOK_MANAGER.handleEvent(event);
        } catch (GitLabApiException e) {
            logger.warn("receive webhook gitlab ex = ", e);
        } catch (Exception e) {
            logger.warn("receive webhook  e = ", e);
        }

    }
}
