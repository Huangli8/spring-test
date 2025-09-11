package org.example.spring.repository;

import org.example.spring.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    public Employee save(Employee employee);
    public Optional<Employee> findById(long id);
    public List<Employee> findAll();
    public List<Employee> findByGender(String gender);
    public Employee delete(Employee employee);
    public Employee update(Employee employee);
    public void deleteAll();
}
