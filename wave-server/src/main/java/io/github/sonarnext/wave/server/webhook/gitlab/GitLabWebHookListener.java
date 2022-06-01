package io.github.sonarnext.wave.server.webhook.gitlab;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonarnext.wave.server.task.TaskConvert;
import io.github.sonarnext.wave.server.task.TaskManager;
import org.gitlab4j.api.webhook.MergeRequestEvent;
import org.gitlab4j.api.webhook.PushEvent;
import org.gitlab4j.api.webhook.WebHookListener;
import org.slf4j.Logger;

public class GitLabWebHookListener implements WebHookListener {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GitLabWebHookListener.class);

    @Override
    public void onMergeRequestEvent(MergeRequestEvent event) {
        logger.info("Merge request event: {}", event);
    }

    @Override
    public void onPushEvent(PushEvent pushEvent) {
        logger.info("Push event: {}", pushEvent);

        EnumProto.Task task = TaskConvert.eventToTask(pushEvent);
        TaskManager.addTask(task);
    }
}
