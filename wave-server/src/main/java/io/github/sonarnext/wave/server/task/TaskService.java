package io.github.sonarnext.wave.server.task;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonarnext.wave.common.dto.TaskDTO;

public interface TaskService {

    EnumProto.Task pullAndGetTask();

    String addTask(TaskDTO task);
}
