package com.cibershield.cibershield.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cibershield.cibershield.model.user.Commune;
import com.cibershield.cibershield.model.user.Region;




@Repository
public interface CommuneRepository extends JpaRepository <Commune, Long>{

    boolean existsByNameCommunity(String nameCommunity);

    boolean findByNameCommunityAndRegionId(String nameCommunity, Long regionId);

    boolean existsByNameCommunityAndRegionId(String nameCommunity, Long regionId);

    List<Commune> findByRegionIdOrderByNameCommunityAsc(Long regionId);

    Optional<Region> findByNameCommunity(String nameCommunity);

    

    
}
