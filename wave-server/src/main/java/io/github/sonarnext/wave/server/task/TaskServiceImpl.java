package io.github.sonarnext.wave.server.task;

import com.google.common.collect.Lists;
import io.github.sonar.next.wave.EnumProto;
import io.github.sonarnext.wave.common.dto.TaskDTO;
import io.github.sonarnext.wave.common.vo.TaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import io.github.sonarnext.wave.common.vo.Pager;

import java.util.Collections;

/**
 * 任务获取，后续改为长连接的方式
 */
@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public Pager<TaskVO> pullAndGetTask() {
        try {
            EnumProto.Task taskEntity = TaskManager.getTask();
            if (taskEntity == null) {
                return Pager.create(0, 0, Collections.emptyList());
            }

            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(taskEntity, taskVO);

            return Pager.create(1, 1, Lists.newArrayList(taskVO));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Pager.create(0, 0, Collections.emptyList());
    }

    @Override
    public String addTask(TaskDTO task) {
        logger.info("add task {}", task);
        EnumProto.Task taskBO = EnumProto.Task.newBuilder()
                .setId(task.getId())
                .setName(task.getName())
                .setBranch("")
                .setGitHost("")
                .setLanguage("")
                .setName("")
                .setNamespaceFullPath("")
                .setLanguage("")
                .setRepositoryPath("")
                .setProjectName("")
                .setSonarHost("")
                .setStartTime(System.currentTimeMillis())
                .setCommitId("")
                .build();
        TaskManager.addTask(taskBO);
        return "success";
    }
}
