package org.example.spring.service;

import org.example.spring.entity.Company;
import org.example.spring.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public List<Company> getCompanies(Integer page, Integer size){
        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 10;

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Company> companyPage = companyRepository.findAll(pageable);

        return companyPage.getContent();
    }

    public Optional<Company> getCompany(long id){
        return companyRepository.findById(id);
    }

    public Company createCompany(Company company){
        return  companyRepository.save(company);
    }
    public Optional<Company> updateCompany(long id, Company company){
        Optional<Company> companyToUpdate = companyRepository.findById(id);
        companyToUpdate.ifPresent(value -> value.setName(company.getName()));
        return companyToUpdate;
    }

    public boolean deleteCompany(long id){
        if(companyRepository.existsById(id)){
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
