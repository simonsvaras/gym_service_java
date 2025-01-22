package com.gym.gymmanagementsystem;

import org.springframework.core.io.Resource;

public class FileResourceData {
    private Resource resource;
    private String contentType;

    public FileResourceData(Resource resource, String contentType) {
        this.resource = resource;
        this.contentType = contentType;
    }
    // gettery
    public Resource getResource() {
        return resource;
    }
    public String getContentType() {
        return contentType;
    }
}
