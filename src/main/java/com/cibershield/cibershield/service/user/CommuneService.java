package com.cibershield.cibershield.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.CommuneDTO;
import com.cibershield.cibershield.model.user.Commune;
import com.cibershield.cibershield.model.user.Region;
import com.cibershield.cibershield.repository.user.CommuneRepository;
import com.cibershield.cibershield.repository.user.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommuneService {
    
    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private RegionRepository regionRepository;


    public CommuneDTO.Response createCommune(CommuneDTO.Create dto, Long regionId){
        if(dto.nameCommunity() == null || dto.nameCommunity().trim().isBlank()){
            throw new RuntimeException("Debe ingresar el nombre de la comuna");
        }
        
        String name = dto.nameCommunity().trim().toUpperCase();

        if(communeRepository.existsByNameCommunity(name)){
            throw new RuntimeException("La comuna ya está registrada"); 
        }
        
        Region region = regionRepository.findById(regionId)
            .orElseThrow(()-> new RuntimeException("No se encontró la region."));

        if(communeRepository.existsByNameCommunityAndRegionId(name, regionId)){
            throw new RuntimeException("La comuna ya está registrada en esta región.");
        }
       
        Commune commune = new Commune();
        commune.setNameCommunity(name);
        commune.setRegion(region);
        commune = communeRepository.save(commune);
     
        return new CommuneDTO.Response(
            commune.getId(),
            commune.getNameCommunity(),
            region.getId(),
            region.getRegionName()
        );

    }   

    public List<CommuneDTO.Combo> ListCommunesByRegion(Long regionId){
        if(regionId == null){
            throw new RuntimeException("Debe seleccionar una región.");
        }
        regionRepository.findById(regionId)
            .orElseThrow(()-> new RuntimeException("Región no encontrada."));
        
        return communeRepository.findByRegionIdOrderByNameCommunityAsc(regionId).stream()
            .map(c -> new CommuneDTO.Combo(c.getId(), c.getNameCommunity(), c.getRegion().getRegionName()))
            .toList();
    }

  
    public void deleteCommune(Long communeId) {
        if(communeId == null){
            throw new RuntimeException("Para eliminar una comuna debe ingresar su id.");
        }
        Commune commune = communeRepository.findById(communeId)
            .orElseThrow(()-> new RuntimeException("No se encontró la comuna"));

        communeRepository.delete(commune);
    }

}
