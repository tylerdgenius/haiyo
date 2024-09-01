package com.metrobuzz.dependencies.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.metrobuzz.dependencies.services.CountryService;
import com.metrobuzz.dependencies.utilities.Response;
import com.metrobuzz.dependencies.models.CountryModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/countries")
public class CountryController {
    private CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public Response<List<CountryModel>> getCountries() {
        List<CountryModel> countries = countryService.getAllCountries();

        return new Response<>("Success", HttpStatus.OK.value(), countries);
    }
}
