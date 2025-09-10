package org.example.spring.repository;

import org.example.spring.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public void save(Employee employee) {
        employee.setId(employees.size() + 1);
        employee.setActiveStatus(true);
        employees.add(employee);
    }

    public int getSize() {
        return employees.size();
    }

    public Employee findById(long id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream().filter(employee -> Objects.equals(employee.getGender(), gender)).toList();
    }

    public List<Employee> findAll() {
        return employees;
    }

    public Employee delete(long id) {
        Employee employee = findById(id);
        if (employee != null) {
            employee.setActiveStatus(false);
        }
        return employee;
    }

    public void deleteAll() {
        employees.clear();
    }

    public Employee update(long id, Employee employee) {
        Employee employeeToUpdate = findById(id);
        if (employeeToUpdate != null) {
            employeeToUpdate.setName(employee.getName());
            employeeToUpdate.setAge(employee.getAge());
            employeeToUpdate.setSalary(employee.getSalary());
            employeeToUpdate.setGender(employee.getGender());
        }
        return employeeToUpdate;
    }
}
