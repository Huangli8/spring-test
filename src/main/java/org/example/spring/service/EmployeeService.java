package org.example.spring.service;

import org.example.spring.entity.Employee;
import org.example.spring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(@RequestBody Employee employee) {
        employee.setId(employeeRepository.getSize()+1);
        employeeRepository.save(employee);
        return employee;
    }

    public Employee getEmployee(long id) {
        return employeeRepository.findById(id);
    }
    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        List<Employee> employees = (gender==null)? employeeRepository.findAll() : employeeRepository.findByGender(gender);
        if (page == null || size == null) {
            return employees;
        }
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        // 分页逻辑
        int from = (page - 1) * size;
        if (from >= employees.size()) return List.of();
        int to = Math.min(from + size, employees.size());
        return employees.subList(from, to);

    }
    public Employee updateEmployee(long id ,Employee employee) {
        Employee employeeToUpdate = employeeRepository.findById(id);
        if(employeeToUpdate != null){
            employeeToUpdate.setName(employee.getName());
            employeeToUpdate.setAge(employee.getAge());
            employeeToUpdate.setSalary(employee.getSalary());
            employeeToUpdate.setGender(employee.getGender());
        }
        return employeeToUpdate;
    }
    public boolean deleteEmployee(long id) {
        return employeeRepository.delete(id);
    }
}
