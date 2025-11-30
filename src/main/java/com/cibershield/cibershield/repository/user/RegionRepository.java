package com.cibershield.cibershield.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.Region;


@Repository
public interface RegionRepository extends JpaRepository <Region, Long>{

    List<Region> findAllByOrderByNombreRegionAsc();

    Optional<Region> findByRegionName(String regionName);

    boolean existsByRegionName(String regionName);

    List<Region> findAllByOrderByRegionNameAsc();

    

    
}
