package com.devconnect.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class ProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;
    private List<String> skills;
    private Integer maxMembers;

    public ProjectRequest() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getSkills() { return skills; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }
}