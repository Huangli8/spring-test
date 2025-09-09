package org.example.spring.controller;

import org.example.spring.entity.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController {
    private final List<Company> companies = new ArrayList<>();

    @GetMapping("/companies")
    public List<Company> getCompanies (
            @RequestParam(required = false)Integer page,
            @RequestParam(required = false)Integer size){
        if(page == null || size == null){
            return companies;
        }
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int from = (page-1)*size;
        if(from>=companies.size())return List.of();
        int to = Math.min(from+size,companies.size());
        return companies.subList(from,to);
    }
    @GetMapping("/companies/{id}")
    public Company getCompanyById(@PathVariable long id){
        return companies.stream()
                .filter(company-> company.getId()==id)
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company company){
        company.setId(companies.size()+1);
        companies.add(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @PutMapping("companies/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable long id, @RequestBody Company company){
        Company companyToUpdate = companies.stream()
                .filter(c-> c.getId() == id)
                .findFirst()
                .orElse(null);
        if(companyToUpdate != null){
            companyToUpdate.setName(company.getName());
        }
        return ResponseEntity.ok(companyToUpdate);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id){
        boolean removed = companies.removeIf(company -> company.getId() == id);
        return removed? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
