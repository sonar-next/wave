package io.github.sonarnext.wave.server.task;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonarnext.wave.common.dto.TaskDTO;
import io.github.sonarnext.wave.common.vo.Pager;
import io.github.sonarnext.wave.common.vo.TaskVO;

public interface TaskService {

    Pager<TaskVO> pullAndGetTask();

    String addTask(TaskDTO task);
}
