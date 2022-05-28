package io.github.sonarnext.wave.server.task;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonarnext.wave.common.dto.TaskDTO;
import org.springframework.stereotype.Service;

/**
 * 任务获取，后续改为长连接的方式
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public EnumProto.Task pullAndGetTask() {
        try {
            TaskEntity taskEntity = TaskManager.getTask();
            return EnumProto.Task
                    .newBuilder()
                    .setId(taskEntity.getId())
                    .setName(taskEntity.getName())
                    .build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;

    }

    @Override
    public String addTask(TaskDTO task) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(task.getId());
        taskEntity.setName(task.getName());
        TaskManager.addTask(taskEntity);
        return "success";
    }
}
