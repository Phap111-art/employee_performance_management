package com.example.springemployee.api;

import com.example.springemployee.dto.DepartmentDTO;
import com.example.springemployee.entity.Department;
import com.example.springemployee.exception.FieldErrorResultMsg;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.DepartmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentControllerApi {
    private final DepartmentService departmentService;
    final Logger logger = LoggerFactory.getLogger(DepartmentControllerApi.class);

    public DepartmentControllerApi(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseObject> saveDepartment(@Valid @RequestBody DepartmentDTO dto,
                                                         BindingResult bindingResult) {

        List<Department> isName = departmentService.getAllDepartmentByName(dto.getName().trim());
        List<Department> isId = departmentService.getAllDepartmentById(dto.getId());
        String errorField = FieldErrorResultMsg.getMsgError(bindingResult);
        if (!errorField.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Error valid", false, errorField)
            );
        }
        if (!isName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Name department already exist !", false, "")
            );
        }
        if (!isId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Id department already exist !", false, "")
            );
        }
        String validId = dto.validId(dto.getId());
        if (!validId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject(validId, false, validId)
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("insert Department successfully!", true, departmentService.saveDepartmentDTO(dto))
        );

    }


    @PutMapping("/uploadOrInsert")
    public ResponseEntity<ResponseObject> updateOrInsertDepartment(@Valid @RequestBody DepartmentDTO dto, BindingResult result) {
        String errorField = FieldErrorResultMsg.getMsgError(result);
        if (!errorField.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Error valid", false, errorField)
            );
        }
        String validId = dto.validId(dto.getId());
        if (!validId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Error Id Department !", false, validId)
            );
        }
        dto.setId(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("insert Or Update Department successfully!", true, departmentService.insertOrUpdateDepartmentDTO(dto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findByIdDepartment(@PathVariable("id") String id) {
        try {
            Department department = departmentService.findByIdDepartment(id);
            if (Objects.nonNull(department)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("found department id is " + id, true, department)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("department id not found " + id, false, e)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("account id not found " + id, false, "")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteByIdDepartment(@PathVariable("id") String id) {
        boolean isDel = departmentService.deleteByIdDepartment(id);
        if (isDel) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new ResponseObject("Deleting department id " + id + " successfully", true, "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("department id not found " + id, false, "")
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllDepartment() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("return all department successfully!", true, departmentService.getAllDepartment())
        );
    }

    @GetMapping("/detail_page")
    public ResponseEntity<ResponseObject> getDepartmentPageDetail() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Department detail page !", true, departmentService.getDepartmentPageDetailByName(1, 5, ""))
        );
    }


}
