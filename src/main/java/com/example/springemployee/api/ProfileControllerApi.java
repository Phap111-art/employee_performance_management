package com.example.springemployee.api;

import com.example.springemployee.entity.Employee;
import com.example.springemployee.service.UpdateProfileService;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileControllerApi {
    private final UpdateProfileService updateProfileService;
    private final StorageService storageService;
    final Logger logger = LoggerFactory.getLogger(ProfileControllerApi.class);

    public ProfileControllerApi(UpdateProfileService updateProfileService, StorageService storageService) {
        this.updateProfileService = updateProfileService;
        this.storageService = storageService;
    }


    @GetMapping("/account/{email}")
    public ResponseEntity<ResponseObject> findByIdEmployeeByAccountEmail(@PathVariable("email") String email) {
        try {
            Employee employee = updateProfileService.findByEmployeeByAccountEmail(email);
            if (Objects.nonNull(employee)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("found email for Emp id is " + email, true, employee)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("email  not found " + email, false, e)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("email id not found " + email, false, "")
        );
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ResponseObject> updateOrInsertEmployee(@RequestParam MultipartFile file,
                                                                 String jsonData) throws JsonProcessingException {
        Employee employee = new ObjectMapper().readValue(jsonData, Employee.class);
        employee.setFile(file);
        employee.setBirthday(DateUtils.getInstance().getStringToDate(employee.getDate()));
        logger.info("date : " + employee.getDate());
        if (employee.getFile() != null) {
            employee.setPhoto(storageService.storeAdd(employee.getFile()));
            logger.info("data " + employee.getPhoto());
        }
        if (employee.getFile().isEmpty() || employee.getFile() == null) {
            Employee updateFile = updateProfileService.findByIdEmployee(employee.getId());
            employee.setPhoto(updateFile.getPhoto());
            logger.info("update file " + employee.getPhoto());
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Update Employee successfully!", true, updateProfileService.updateProfileEmployee(employee))
        );
    }

}
