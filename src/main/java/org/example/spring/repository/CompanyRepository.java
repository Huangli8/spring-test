package org.example.spring.repository;

import org.example.spring.entity.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();
    public void save(Company company) {
        companies.add(company);
    }

    public int getSize(){
        return companies.size();
    }

    public Company findById(long id) {
        return companies.stream()
                .filter(company-> company.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Company> findAll() {
        return companies;
    }

    public boolean delete(long id) {
        return companies.removeIf(company-> company.getId() == id);
    }

    public void deleteAll() {
        companies.clear();
    }
}
