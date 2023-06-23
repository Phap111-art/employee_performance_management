package com.example.springemployee.controller;

import com.example.springemployee.entity.Employee;
import com.example.springemployee.service.EmployeeService;
import com.example.springemployee.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Controller
@RequestMapping("/display/image/")
public class ImageUploadController {
    private final EmployeeService employeeService;
    private final StorageService storageService;
    public ImageUploadController(EmployeeService employeeService, StorageService storageService) {
        this.employeeService = employeeService;
        this.storageService = storageService;
    }

    @GetMapping("/storage/{photo}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String photo) {
        try {
            Path filename = storageService.load(photo);
            byte[] buffer = Files.readAllBytes(filename);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(byteArrayResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }
}
