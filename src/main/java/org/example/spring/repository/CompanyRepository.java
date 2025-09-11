package org.example.spring.repository;

import org.example.spring.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
