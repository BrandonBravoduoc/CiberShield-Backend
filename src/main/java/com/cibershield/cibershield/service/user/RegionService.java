package com.cibershield.cibershield.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.RegionDTO;
import com.cibershield.cibershield.model.user.Region;
import com.cibershield.cibershield.repository.user.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {
    
    @Autowired
    private RegionRepository regionRepository;


    public List<Region>findAllRegions(){
        return regionRepository.findAllByOrderByNombreRegionAsc();
    }

    public Region findById(Long regionId){
        return regionRepository.findById(regionId)
            .orElseThrow(()-> new RuntimeException("Región no encontrada."));
    }

    public Region findByRegionName(String regionName){
        return regionRepository.findByRegionName(regionName)
            .orElseThrow(()-> new RuntimeException("Region no encontrada."));
    }
    
    public RegionDTO.Response createRegion(RegionDTO.Create dto){
        if(dto.regionName() == null || dto.regionName().trim().isEmpty()){
            throw new RuntimeException("Debe ingresar un nombre para la región.");
        }

        String name = dto.regionName().trim().toUpperCase();

        if(regionRepository.existsByRegionName(name)){
            throw new RuntimeException("La región ya está registrada.");
        }

        Region region = new Region();
        region.setRegionName(name);

        region = regionRepository.save(region);

        return new RegionDTO.Response(
            region.getId(),
            region.getRegionName(),
            region.getCommune() != null ? region.getCommune().size() : 0
        );

    }

    public List<RegionDTO.Combo> ListRegions(){
        return regionRepository.findAllByOrderByRegionNameAsc().stream()
            .map(r -> new RegionDTO.Combo(r.getId(), r.getRegionName()))
            .toList();
            
    }


}
