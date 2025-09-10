package org.example.spring.service;

import org.example.spring.entity.Company;
import org.example.spring.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public List<Company> getCompanies(Integer page, Integer size){
        List<Company> companies = companyRepository.getAllCompanies();
        if(page == null && size == null){ return companies; }

        if (page < 1) page = 1;
        if (size < 1) size = 10;
        // 分页逻辑
        int from = (page - 1) * size;
        if (from >= companies.size()) return List.of();
        int to = Math.min(from + size, companies.size());
        return companies.subList(from, to);
    }

    public Company getCompany(long id){
        return companyRepository.getCompanyById(id);
    }

    public Company createCompany(Company company){
        company.setId(companyRepository.getSize()+1);
        companyRepository.addCompany(company);
        return company;
    }
    public Company updateCompany(long id, Company company){
        Company companyToUpdate = companyRepository.getCompanyById(id);
        if(companyToUpdate!=null){
            companyToUpdate.setName(company.getName());
        }
        return companyToUpdate;
    }

    public boolean deleteCompany(long id){
        return companyRepository.deleteCompanyById(id);
    }
}
