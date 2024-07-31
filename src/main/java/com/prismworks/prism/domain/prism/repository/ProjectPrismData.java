package com.prismworks.prism.domain.prism.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ProjectPrismData {
    @Id
    private Integer id;
    private String userId;
    private Integer projectId;
    private int communication;
    private int proactivity;
    private int problemSolving;
    private int responsibility;
    private int cooperation;
}