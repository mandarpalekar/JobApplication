package com.springsynergy.jobapp.Company.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springsynergy.jobapp.Company.model.CompanyDto;
import com.springsynergy.jobapp.Company.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsynergy.jobapp.Job.model.JobDto;
import com.springsynergy.jobapp.util.DtoToStringConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final ObjectMapper objectMapper;

    private final DtoToStringConverter dtoToStringConverter;

    @GetMapping("")
    public ResponseEntity<String> getAllCompanies() {
       Set<CompanyDto> companies = companyService.getAllCompanies();
        try {
            if (companies.isEmpty()) {
                return new ResponseEntity<>("No companies found", HttpStatus.NOT_FOUND);
            } else {
                String companiesJson = objectMapper.writeValueAsString(companies);
                return new ResponseEntity<>(companiesJson, HttpStatus.OK);
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error converting companies to JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/company/id/{companyId}")
    public ResponseEntity<String> getCompanyById(@PathVariable UUID companyId) {
       CompanyDto companyDto = companyService.getCompanyById(companyId);
       if(companyDto == null) {
           return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
       } else {
           return dtoToStringConverter.convertDtoToString(companyDto);
       }
    }

    @GetMapping("/company/name")
    public ResponseEntity<String> getCompanyByName(@RequestParam String companyName) {
        if(StringUtils.isEmpty(companyName)) {
            return new ResponseEntity<>("Company name is required", HttpStatus.BAD_REQUEST);
        }
        String decodedCompanyName = URLDecoder.decode(companyName, StandardCharsets.UTF_8);
        CompanyDto companyDto = companyService.getCompanyByName(decodedCompanyName);
        return dtoToStringConverter.convertDtoToString(companyDto);
    }

    @PostMapping("/company/createCompany")
    public ResponseEntity<String> createCompany(@RequestBody CompanyDto companyDto) {
        try{
            companyService.createCompany(companyDto);
            return new ResponseEntity<>("Company created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating company", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/company/deleteCompany/id/{companyId}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable String companyId) {
        if(StringUtils.isEmpty(companyId)) {
            return new ResponseEntity<>("Company id is required", HttpStatus.BAD_REQUEST);
        }
        boolean isDeleted = companyService.deleteCompanyById(companyId);
        if(isDeleted) {
            return new ResponseEntity<>("Company deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error deleting company", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/company/updateCompany/id/{companyId}")
    public ResponseEntity<String> updateCompanyById(@PathVariable String companyId, @RequestBody CompanyDto companyDto) {
        boolean isUpdated = companyService.updateCompanyById(companyId, companyDto);
        if(isUpdated) {
            return new ResponseEntity<>("Company updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error updating company", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
