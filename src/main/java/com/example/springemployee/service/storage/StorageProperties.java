package com.example.springemployee.service.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

@AllArgsConstructor
@NoArgsConstructor
@ComponentScan({"com.example.demo.service.storage"})
public class StorageProperties {
    /**
     * Folder location for storing files
     */
    private String location = "upload/";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
