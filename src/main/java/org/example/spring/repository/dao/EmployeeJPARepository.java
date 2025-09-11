package org.example.spring.repository.dao;

import org.example.spring.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeJPARepository extends JpaRepository<Employee, Long> {
    public List<Employee> findByName(String name);
    public List<Employee> findBySalary(double salary);
    public List<Employee> findByGender(String gender);

}
