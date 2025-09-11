package org.example.spring.repository;

import org.example.spring.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class EmployeeRepositoryInMemoryImpl {
    private final List<Employee> employees = new ArrayList<>();

    public Employee save(Employee employee) {
        employee.setId(1L);
        employee.setActiveStatus(1);
        employees.add(employee);
        return employee;
    }

    public Optional<Employee> findById(long id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst();
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream().filter(employee -> Objects.equals(employee.getGender(), gender)).toList();
    }

    public List<Employee> findAll() {
        return employees;
    }

    public Employee delete(Employee employee) {
        Optional<Employee> employeeToDelete = findById(employee.getId());
        employeeToDelete.ifPresent(value -> value.setActiveStatus(1));
        return employee;
    }


    public void deleteAll() {
        employees.clear();
    }

    public Employee update(Employee employee) {
        Optional<Employee> employeeToUpdate = findById(employee.getId());
        if (employeeToUpdate.isPresent()) {
            employeeToUpdate.get().setName(employee.getName());
            employeeToUpdate.get().setAge(employee.getAge());
            employeeToUpdate.get().setSalary(employee.getSalary());
            employeeToUpdate.get().setGender(employee.getGender());
        }
        return employeeToUpdate.get();
    }
}
