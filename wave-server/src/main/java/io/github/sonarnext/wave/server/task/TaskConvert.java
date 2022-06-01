package io.github.sonarnext.wave.server.task;

import cn.hutool.core.lang.UUID;
import io.github.sonar.next.wave.EnumProto;
import org.gitlab4j.api.webhook.PushEvent;

public class TaskConvert {

    public static EnumProto.Task eventToTask(PushEvent pushEvent) {
        return EnumProto.Task.newBuilder()
                .setId(UUID.fastUUID().toString())
                .setName(pushEvent.getProject().getName())
                .build();
    }
}
