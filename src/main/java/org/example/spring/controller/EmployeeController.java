package org.example.spring.controller;

import org.example.spring.entity.Employee;
import org.example.spring.repository.EmployeeRepository;
import org.example.spring.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PostMapping("/employees1")
    public ResponseEntity<Employee> createEmployee1(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id){
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return employeeService.getEmployees(gender, page, size);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody UpdateEmployeeReq updateEmployeeReq){
        return ResponseEntity.ok(employeeService.updateEmployee(id, updateEmployeeReq));

    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable long id){
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

}
