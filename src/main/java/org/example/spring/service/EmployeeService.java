package org.example.spring.service;

import org.example.spring.Exception.*;
import org.example.spring.controller.UpdateEmployeeReq;
import org.example.spring.entity.Employee;
import org.example.spring.repository.CompanyRepository;
import org.example.spring.repository.EmployeeRepository;
import org.example.spring.repository.EmployeeRepositoryDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
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
        //employee.setActiveStatus(1);
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty())
            throw new EmployeeNotFoundException("Employee with id%d not found".formatted(id));
        return employee.get();
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        // 1. 按性别查，不分页
        if (gender != null) {
            return employeeRepository.findByGender(gender);
        }

        // 2. 没有分页参数，返回所有数据
        if (page == null || size == null) {
            return employeeRepository.findAll(); // 返回 List<Employee>
        }

        // 3. 分页查询，再转成 List
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Employee> pageResult = employeeRepository.findAllPagination(pageable);

        return pageResult.getContent(); // 只取分页后的数据列表
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
