package com.cibershield.cibershield.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.model.user.Region;
import com.cibershield.cibershield.repository.user.RegionRepository;

@Service
public class RegionService {
    
    @Autowired
    private RegionRepository regionRepository;


    public List<Region>findAllRegions(){
        return regionRepository.findAllByOrderByNombreRegionAsc();
    }
}
