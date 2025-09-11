package org.example.spring.repository.dao;

import org.example.spring.entity.Employee;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeJPARepository extends JpaRepository<Employee, Long> {
    public List<Employee> findByName(String name);
    public List<Employee> findBySalary(double salary);
    // 分页查询所有
    Page<Employee> findAll(Pageable pageable);
    // 按性别查询，不分页
    List<Employee> findByGender(String gender);
}
