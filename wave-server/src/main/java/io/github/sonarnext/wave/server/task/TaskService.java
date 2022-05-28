package io.github.sonarnext.wave.server.task;

import io.github.sonar.next.wave.EnumProto;

public interface TaskService {

    EnumProto.Task pullAndGetTask();

    String addTask(EnumProto.Task task);
}
