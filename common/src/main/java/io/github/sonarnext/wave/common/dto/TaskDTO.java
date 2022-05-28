package io.github.sonarnext.wave.common.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TaskDTO {
    @NotNull
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
