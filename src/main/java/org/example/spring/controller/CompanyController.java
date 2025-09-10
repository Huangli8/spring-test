package org.example.spring.controller;

import org.example.spring.entity.Company;
import org.example.spring.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public List<Company> getCompanies (
            @RequestParam(required = false)Integer page,
            @RequestParam(required = false)Integer size){
        return companyService.getCompanies(page, size);
    }
    @GetMapping("/companies/{id}")
    public Company getCompanyById(@PathVariable long id){
        return companyService.getCompany(id);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company company){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(company));
    }

    @PutMapping("companies/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable long id, @RequestBody Company company){
        Company companyToUpdate = companyService.updateCompany(id, company);
        return ResponseEntity.ok(companyToUpdate);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id){
        return companyService.deleteCompany(id)? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
