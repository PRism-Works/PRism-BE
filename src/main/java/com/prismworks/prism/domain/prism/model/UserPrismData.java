package com.prismworks.prism.domain.prism.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserPrismData {
    @Id
    private String userId;
    private int communication;
    private int proactivity;
    private int problemSolving;
    private int responsibility;
    private int cooperation;
}