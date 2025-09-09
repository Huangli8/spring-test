package org.example.spring.controller;

import org.example.spring.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EmployeeController {
    private final List<Employee> employees = new ArrayList<>();

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
    public List<Employee> getEmployees(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        // 先过滤
        List<Employee> filteredEmployees = employees.stream()
                .filter(e -> gender == null || Objects.equals(e.getGender(), gender))
                .toList();

        if (page == null || size == null) {
            return filteredEmployees;
        }

        if (page < 1) page = 1;
        if (size < 1) size = 10;
        // 分页逻辑
        int from = (page - 1) * size;
        if (from >= filteredEmployees.size()) return List.of();
        int to = Math.min(from + size, filteredEmployees.size());
        return filteredEmployees.subList(from, to);
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

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id){
        boolean removed = employees.removeIf(employee -> employee.getId() == id);
        return removed? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
