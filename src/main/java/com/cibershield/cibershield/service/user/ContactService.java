package com.cibershield.cibershield.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.ContactDTO;
import com.cibershield.cibershield.model.user.Address;
import com.cibershield.cibershield.model.user.Commune;
import com.cibershield.cibershield.model.user.Contact;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.AddressRepository;
import com.cibershield.cibershield.repository.user.CommuneRepository;
import com.cibershield.cibershield.repository.user.ContactRepository;
import com.cibershield.cibershield.repository.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContactService {

    @Autowired 
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private UserRepository userRepository;

    public ContactDTO.Response contactCreateWithAddress(ContactDTO.CreateContactWithAddress dto, Long userId) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        contactValidate(dto.name(), dto.lastName(), dto.phone());
            
        Address address = addressService.createAndSaveAddress(
            dto.street(),
            dto.number(),
            dto.communeId()
        );

        Contact contact = new Contact();
        contact.setName(dto.name().trim());
        contact.setLastName(dto.lastName().trim());
        contact.setPhone(dto.phone().trim());
        contact.setUser(currentUser);
        contact.setAddress(address);
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


    public ContactDTO.Response updateContactWithAddress(ContactDTO.UpdateContactWithAddress dto, Long userId) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        contactValidate(dto.name(), dto.lastName(), dto.phone());
        addressService.addressValidation(dto.street(), dto.number());

        Contact contact = contactRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));

        if (!contact.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para actualizar este contacto");
        }

        Commune commune = communeRepository.findById(dto.communeId())
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        Address address = contact.getAddress();
        if (address == null) {
            address = new Address();
        }

        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setCommune(commune);
        addressRepository.save(address);

        contact.setName(dto.name().trim());
        contact.setLastName(dto.lastName().trim());
        contact.setPhone(dto.phone().trim());
        contact.setUser(currentUser);
        contact.setAddress(address);

        contactRepository.save(contact);

        String addressInfo = address.getStreet() + " " + address.getNumber() +
                ", " + address.getCommune().getNameCommunity();

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
