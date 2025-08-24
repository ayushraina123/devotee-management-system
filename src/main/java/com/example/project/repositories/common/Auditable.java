package com.example.project.repositories.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.Instant;

@Getter
@MappedSuperclass
public abstract class Auditable {
    @Column(name = "created_on", updatable = false)
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @PrePersist
    public void prePersist() {
        this.createdOn = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedOn = Instant.now();
    }
}
