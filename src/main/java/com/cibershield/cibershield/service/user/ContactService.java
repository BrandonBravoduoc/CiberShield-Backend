package com.cibershield.cibershield.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.ContactDTO;
import com.cibershield.cibershield.model.user.Address;
import com.cibershield.cibershield.model.user.Contact;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.AddressRepository;
import com.cibershield.cibershield.repository.user.ContactRepository;

@Service
public class ContactService {

    @Autowired 
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;


    public ContactDTO.Response contactCreate(ContactDTO.CreateContact dto, User currentUser) {

        contactValidate(dto.name(), dto.lastName(), dto.phone());

        Contact contact = new Contact();
        contact.setName(dto.name().trim());
        contact.setLastName(dto.lastName().trim());
        contact.setPhone(dto.phone().trim());
        contact.setUser(currentUser);

        if (dto.addressId() != null) {
            Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
            contact.setAddress(address);
        }

        contact = contactRepository.save(contact);

       String addressInfo = contact.getAddress() != null
        ? contact.getAddress().getStreet() + " " + contact.getAddress().getNumber() +
        ", " + contact.getAddress().getCommune().getNameCommunity()
        : null;

        return new ContactDTO.Response(
            contact.getId(),
            contact.getName(),
            contact.getLastName(),
            contact.getPhone(),
            addressInfo,
            currentUser.getUserName()
        );
    }
    
    
    public void contactValidate(String name, String lastName, String phone){
        if(name == null || name.trim().isBlank()){
            throw new RuntimeException("El nombre es obligatorio.");
        }
        if(lastName == null || lastName.trim().isBlank()){
            throw new RuntimeException("El apellido es obligatorio.");
        }
        if(phone == null || phone.trim().isBlank()){
            throw new RuntimeException("Debe ingresar un número de teléfono.");
        }
        if(contactRepository.existsByPhone(phone)){
            throw new RuntimeException("El número de teléfono ya está en uso.");
        }
        if(!phone.matches("\\d+")){
            throw new RuntimeException("El teléfono solo puede contener números.");
        }
        if(phone.length() != 9){
            throw new RuntimeException("Debe ingresar solo 9 dígitos.");
        }
    }




    
}
