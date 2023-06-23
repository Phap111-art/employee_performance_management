package com.example.springemployee.api;

import com.example.springemployee.entity.Employee;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.EmployeeService;
import com.example.springemployee.service.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeControllerApi {
    private final EmployeeService employeeService;
    private final StorageService storageService;
    final Logger logger = LoggerFactory.getLogger(EmployeeControllerApi.class);

    public EmployeeControllerApi(EmployeeService employeeService, StorageService storageService) {
        this.employeeService = employeeService;
        this.storageService = storageService;
    }


    @PostMapping("/save")
    public ResponseEntity<ResponseObject> saveEmployee(@RequestParam("file") MultipartFile file,

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
            employee.setPhoto("no_avatar.jpg");
            logger.info("update file " + employee.getPhoto());
        }
        List<Employee> isId = employeeService.getAllEmployeeById(employee.getId());
        if (!isId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Id Employee already exist !", false, "")
            );
        }
        employeeService.updateStatusAccount(employee.getAccount_id(), 1);
        employeeService.updateStatusDepartment(employee.getDepartment_id(), 1);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("insert Employee successfully!", true, employeeService.saveEmployee(employee))
        );
    }

    @PutMapping("/uploadOrInsert")
    public ResponseEntity<ResponseObject> updateOrInsertEmployee(@RequestParam MultipartFile file,

                                                                 String jsonData) throws JsonProcessingException {
        Employee employee = new ObjectMapper().readValue(jsonData, Employee.class);
        employee.setFile(file);
        employee.setBirthday(DateUtils.getInstance().getStringToDate(employee.getDate()));
        if (employee.getFile() != null) {
            employee.setPhoto(storageService.storeAdd(employee.getFile()));
        }
        if (employee.getFile().isEmpty() || employee.getFile() == null) {
            Employee updateFile = employeeService.findByIdEmployee(employee.getId());
            employee.setPhoto(updateFile.getPhoto());
        }
        setDepartmentUpdateStatus(employee.getDepartment_id(), employee.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("insert Or Update Employee successfully!", true, employeeService.insertOrUpdateEmployee(employee))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findByIdEmployee(@PathVariable("id") String id) {
        try {
            Employee employee = employeeService.findByIdEmployee(id);
            if (Objects.nonNull(employee)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("found Employee id is " + id, true, employee)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Employee id not found " + id, false, e)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("account id not found " + id, false, "")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteByIdEmployee(@PathVariable("id") String id) {
        Employee employee = employeeService.findByIdEmployee(id);
        List<Employee> empForAccount = employeeService.getAllEmployeeByAccountId(employee.getAccount().getId());
        List<Employee> empForDepartment = employeeService.getAllEmployeeByDepartmentId(employee.getDepartment().getId());
        if (empForAccount.size() == 1) {
            employeeService.updateStatusAccount(employee.getAccount().getId(), 0);
        }
        if (empForDepartment.size() == 1) {
            employeeService.updateStatusDepartment(employee.getDepartment().getId(), 0);
        }

        boolean isDel = employeeService.deleteByIdEmployee(id);
        if (isDel) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new ResponseObject("Deleting Employee id " + id + " successfully", true, "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Employee id not found " + id, false, "")
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllEmployee() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("return all Employee successfully!", true, employeeService.getAllEmployee())
        );
    }

    @GetMapping("/detail_page")
    public ResponseEntity<ResponseObject> getEmployeePageDetail() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Employee detail page !", true, employeeService.getEmployeePageDetail())
        );
    }




    public void setDepartmentUpdateStatus(String idDepartNew, String idEmp) {
        Employee employee = employeeService.findByIdEmployee(idEmp);
        String idDepartOld = employee.getDepartment().getId();
        employee.setDepartment_id(idDepartNew);
        employee.setAccount_id(employee.getAccount().getId());
        List<Employee> empForDepartmentOld = employeeService.getAllEmployeeByDepartmentId(idDepartOld);
        if (empForDepartmentOld.size() == 1) {
            employeeService.updateStatusDepartment(idDepartOld, 0);
        }
        employeeService.insertOrUpdateEmployee(employee);
        List<Employee> empForDepartmentNew = employeeService.getAllEmployeeByDepartmentId(idDepartNew);
        if (empForDepartmentNew.size() == 1) {
            employeeService.updateStatusDepartment(idDepartNew, 1);
        }
    }


}
