package com.cibershield.cibershield.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.AddressDTO;
import com.cibershield.cibershield.model.user.Address;
import com.cibershield.cibershield.model.user.Commune;
import com.cibershield.cibershield.repository.user.AddressRepository;
import com.cibershield.cibershield.repository.user.CommuneRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CommuneRepository communeRepository;

    public AddressDTO.Response createAddress(AddressDTO.Create dto, Long comunaId){
        addressValidation(dto.street(),dto.street());
        
       Commune commune = communeRepository.findById(dto.communeId())
        .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        Address address = new Address();
        address.setStreet(dto.street().trim());
        address.setNumber(dto.number().trim());
        address.setCommune(commune);

        address = addressRepository.save(address);

        return new AddressDTO.Response(
            address.getId(),
            address.getStreet(),
            address.getNumber(),
            commune.getId(),
            commune.getNameCommunity(),
            commune.getRegion().getId(),
            commune.getRegion().getRegionName()
        );
    }
    
    public Address createAndSaveAddress(String street, String number, Long communeId) {
        addressValidation(street, number);

        Commune commune = communeRepository.findById(communeId)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        Address address = new Address();
        address.setStreet(street.trim());
        address.setNumber(number.trim());
        address.setCommune(commune);

        return addressRepository.save(address);
    }


    public void addressValidation(String street, String number){
        if(street == null || street.trim().isEmpty()){
            throw new RuntimeException("Debe ingresar el nombre de la calle.");
        }
        if(number == null || number.trim().isEmpty()){
            throw new RuntimeException("Debe ingresar el número de la dirección.");
        }

    }
}
