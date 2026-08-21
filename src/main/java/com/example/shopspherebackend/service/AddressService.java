package com.example.shopspherebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopspherebackend.dto.AddressRequestDTO;
import com.example.shopspherebackend.dto.AddressResponseDTO;
import com.example.shopspherebackend.entity.Address;
import com.example.shopspherebackend.entity.User;
import com.example.shopspherebackend.repository.AddressRepository;
import com.example.shopspherebackend.repository.UserRepository;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressResponseDTO> getAddressByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AddressResponseDTO getAddressByUserAndId(Long userId, Long id) {
        return toResponseDTO(getAddressForUser(userId, id));
    }

    @Transactional
    public AddressResponseDTO createAddress(Long userId, AddressRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Address> existingAddresses = addressRepository.findByUserIdAndIsDeletedFalse(userId);
        boolean shouldBeDefault = existingAddresses.isEmpty() || Boolean.TRUE.equals(request.isDefault());

        if (shouldBeDefault) {
            clearDefaultForUser(existingAddresses);
        }

        Address address = new Address();
        address.setUser(user);
        applyRequest(address, request);
        address.setDefault(shouldBeDefault);
        address.setIsDeleted(false);

        return toResponseDTO(addressRepository.save(address));
    }

    @Transactional
    public AddressResponseDTO updateAddress(Long userId, Long id, AddressRequestDTO request) {
        Address address = getAddressForUser(userId, id);

        if (Boolean.TRUE.equals(request.isDefault())) {
            clearDefaultForUser(addressRepository.findByUserIdAndIsDeletedFalse(userId), id);
            address.setDefault(true);
        } else {
            address.setDefault(false);
        }

        applyRequest(address, request);

        return toResponseDTO(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(Long userId, Long id) {
        Address address = getAddressForUser(userId, id);

        address.setIsDeleted(true);
        address.setDefault(false);
        addressRepository.save(address);
    }

    private Address getAddressForUser(Long userId, Long addressId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findByIdAndIsDeletedFalse(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to the user");
        }

        return address;
    }

    private void applyRequest(Address address, AddressRequestDTO request) {
        address.setFullName(request.fullName());
        address.setPhone(request.phone());
        address.setAddress_line1(request.address_line1());
        address.setAddress_line2(request.address_line2());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPostal_code(request.postal_code());
        address.setCountry(request.country());
        address.setType(request.type());
    }

    private void clearDefaultForUser(List<Address> addresses) {
        clearDefaultForUser(addresses, null);
    }

    private void clearDefaultForUser(List<Address> addresses, Long excludeAddressId) {
        for (Address address : addresses) {
            if (excludeAddressId != null && excludeAddressId.equals(address.getId())) {
                continue;
            }

            if (address.isDefault()) {
                address.setDefault(false);
                addressRepository.save(address);
            }
        }
    }

    private AddressResponseDTO toResponseDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getFullName(),
                address.getPhone(),
                address.getAddress_line1(),
                address.getAddress_line2(),
                address.getCity(),
                address.getState(),
                address.getPostal_code(),
                address.getCountry(),
                address.getType(),
                address.isDefault(),
                address.getCreatedAt(),
                address.getUpdatedAt());
    }
}
