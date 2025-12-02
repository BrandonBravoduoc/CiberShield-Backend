package com.cibershield.cibershield.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.user.CommuneDTO;
import com.cibershield.cibershield.dto.user.RegionDTO;
import com.cibershield.cibershield.service.user.CommuneService;
import com.cibershield.cibershield.service.user.RegionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    
    @Autowired
    private RegionService regionService;
    
    @Autowired
    private CommuneService communeService;

    @GetMapping("/regions")
    public List<RegionDTO.Combo> getRegions(){
        return regionService.ListRegions();
    }

    @GetMapping("/communes")
    public List<CommuneDTO.Combo>getCommunes(@RequestParam Long regionId) {
            return communeService.ListCommunesByRegion(regionId);
    }  

}
