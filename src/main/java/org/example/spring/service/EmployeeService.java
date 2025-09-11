package org.example.spring.service;

import org.example.spring.Exception.*;
import org.example.spring.controller.UpdateEmployeeReq;
import org.example.spring.entity.Employee;
import org.example.spring.repository.CompanyRepository;
import org.example.spring.repository.EmployeeRepository;
import org.example.spring.repository.EmployeeRepositoryDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepositoryDBImpl employeeRepository;
    @Autowired
    CompanyRepository companyRepository;


    public Employee createEmployee(@RequestBody Employee employee) {
        if (employee.getAge() < 18 || employee.getAge() > 65) throw new EmployeeNotAmongLegalAgeException();
        if (employee.getAge() > 30 && employee.getSalary() < 20000) throw new EmployeeSalaryToLowException();

        List<Employee> employees = employeeRepository.findAll();
        boolean exists = (employees.stream().anyMatch(e -> e.getName().equals(employee.getName()) && e.getAge() == employee.getAge()));
        if (exists) {
            throw new EmployeeAlreadyExistsException();
        }
        employee.setActiveStatus(1);
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty())
            throw new EmployeeNotFoundException("Employee with id%d not found".formatted(id));
        return employee.get();
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        List<Employee> employees = (gender == null) ? employeeRepository.findAll() : employeeRepository.findByGender(gender);
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

    public Employee updateEmployee(Long id, UpdateEmployeeReq updateEmployeeReq) {
        Optional<Employee> employeeToUpdate = employeeRepository.findById(id);
        if (employeeToUpdate.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with id=%d is not found".formatted(id));
        }
        if (employeeToUpdate.get().getActiveStatus() == 0) throw new InactiveEmployeeUpdateException();
        employeeToUpdate.get().setName(updateEmployeeReq.getName());
        employeeToUpdate.get().setAge(updateEmployeeReq.getAge());
        employeeToUpdate.get().setSalary(updateEmployeeReq.getSalary());
        return employeeRepository.save(employeeToUpdate.get());
    }

    public Employee deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with id=%d is not found".formatted(id));
        }
        if (employee.get().getActiveStatus() == 0)
            throw new EmployeeAlreadyDeletedException();
        return employeeRepository.delete(employee.get());
    }

}
