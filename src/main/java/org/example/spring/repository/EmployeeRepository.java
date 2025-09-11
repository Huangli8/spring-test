package org.example.spring.repository;

import org.example.spring.entity.Employee;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    public Employee save(Employee employee);
    public Optional<Employee> findById(long id);
    Page<Employee> findAllPagination(Pageable pageable);
    List<Employee> findAll();
    List<Employee> findByGender(String gender);
    public Employee delete(Employee employee);
    public Employee update(Employee employee);
    public void deleteAll();
}
