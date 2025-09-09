package org.example.spring.controller;

import org.example.spring.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EmployeeController {
    private List<Employee> employees = new ArrayList<Employee>();

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        employee.setId(employees.size()+1);
        employees.add(employee);
        return employee;
    }

    @PostMapping("/employees1")
    public ResponseEntity<Employee> createEmployee1(@RequestBody Employee employee) {
        employee.setId(employees.size()+1);
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable long id){
        return employees.stream()
                .filter(employee-> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/employees")
    public List<Employee> getEmployeesByGender (@RequestParam String gender){
        return employees.stream().filter(employee -> Objects.equals(employee.getGender(), gender)).toList();
    }
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody Employee employee){
        Employee employeeToUpdate = employees.stream()
                .filter(e-> e.getId() == id)
                .findFirst()
                .orElse(null);
        if(employeeToUpdate != null){
            employeeToUpdate.setName(employee.getName());
            employeeToUpdate.setAge(employee.getAge());
            employeeToUpdate.setSalary(employee.getSalary());
            employeeToUpdate.setGender(employee.getGender());
        }
        return ResponseEntity.ok(employeeToUpdate);
    }
}
