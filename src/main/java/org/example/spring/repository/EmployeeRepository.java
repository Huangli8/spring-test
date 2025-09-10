package org.example.spring.repository;

import org.example.spring.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public int getSize(){
        return employees.size();
    }

    public Employee getEmployeeById(long id) {
        return employees.stream()
                .filter(employee-> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Employee> getEmployeesByGender(String gender) {
        return employees.stream().filter(employee -> Objects.equals(employee.getGender(),gender)).toList();
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public boolean deleteEmployeeById(long id) {
        return employees.removeIf(employee-> employee.getId() == id);
    }

    public void clearEmployees() {
        employees.clear();
    }

}
