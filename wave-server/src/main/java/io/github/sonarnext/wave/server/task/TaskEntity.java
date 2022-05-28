package io.github.sonarnext.wave.server.task;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Table(name = "task")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TaskEntity {

    @Id
    private String id;

    @Column(name = "name")
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
