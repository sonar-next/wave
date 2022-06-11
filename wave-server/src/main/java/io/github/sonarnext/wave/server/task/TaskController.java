package io.github.sonarnext.wave.server.task;

import io.github.sonar.next.wave.EnumProto;
import io.github.sonarnext.wave.common.dto.TaskDTO;
import io.github.sonarnext.wave.common.vo.TaskVO;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import io.github.sonarnext.wave.common.vo.Pager;



@RequestMapping(value = "/wave/task")
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/pull")
    public Pager<TaskVO> pullAndGetTask() {
        return this.taskService.pullAndGetTask();
    }

    @PostMapping
    public String addTask(@Validated @RequestBody TaskDTO task) {
        return this.taskService.addTask(task);
    }


}
