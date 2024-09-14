package com.metrobuzz.dependencies.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metrobuzz.dependencies.models.CityModel;
import com.metrobuzz.dependencies.repositories.CityRepository;

@Service
public class CityService {

    private CityRepository cityRepository;
    
    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public CityModel getCityById(String id) {
       return cityRepository.findById(id).orElse(null);
    }

    public List<CityModel> getAllCities() {
        return cityRepository.findAll();
    }

    public List<CityModel> getCitiesByStateCode(String stateCode) {
        return cityRepository.findByStateCode(stateCode);
    }

    public List<CityModel> getCitiesByCountryCode(String countryCode) {
        return cityRepository.findByCountryCode(countryCode);
    }

    public CityModel getCityByName(String name) {
        return cityRepository.findByName(name);
    }
}