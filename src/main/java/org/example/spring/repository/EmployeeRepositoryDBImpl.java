package org.example.spring.repository;

import org.example.spring.entity.Employee;
import org.example.spring.repository.dao.EmployeeJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryDBImpl implements EmployeeRepository {

    @Autowired
    EmployeeJPARepository employeeJPARepository;

    @Override
    public Employee save(Employee employee) {
        return employeeJPARepository.save(employee);
    }

    @Override
    public Optional<Employee> findById(long id) {
        return employeeJPARepository.findById(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeJPARepository.findAll();
    }

    @Override
    public List<Employee> findByGender(String gender) {
        return employeeJPARepository.findByGender(gender);
    }

    @Override
    public Employee delete(Employee employee) {
        employeeJPARepository.delete(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        return save(employee);
    }

    @Override
    public void deleteAll() {
        employeeJPARepository.deleteAll();
    }
}
