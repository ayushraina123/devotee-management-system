package com.example.project.service;

import com.example.project.dtos.AddressDto;
import com.example.project.dtos.DevoteeDto;
import com.example.project.entities.Address;
import com.example.project.entities.Devotee;
import com.example.project.enums.DonationType;
import com.example.project.exceptions.NotFoundException;
import com.example.project.repositories.AddressRepository;
import com.example.project.repositories.DevoteeRepository;
import com.example.project.specs.SpecificationsQueryBuilder;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DevoteeService {
    private final DevoteeRepository devoteeRepository;
    private final AddressRepository addressRepository;

    public DevoteeService(DevoteeRepository devoteeRepository, AddressRepository addressRepository) {
        this.devoteeRepository = devoteeRepository;
        this.addressRepository = addressRepository;
    }

    public List<DevoteeDto> getAllDevotees(int pageNumber, int pageSize, String firstName, String lastName, String city,
                                           String state, DonationType donationType) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("firstName", "lastName", "fatherName").ascending());
        Integer donationTypeId = null != donationType ? donationType.getId() : null;
        Specification<Devotee> spec = SpecificationsQueryBuilder.specificationForDevotee(firstName, lastName, city, state,
                donationTypeId);
        Page<Devotee> devotees = devoteeRepository.findAll(spec, pageable);
        return DevoteeDto.buildDevoteeDtos(devotees, donationTypeId).get().toList();
    }

    public void deleteDevotee(Long devoteeId) {
        Devotee devotee = findDevoteeById(devoteeId);
        devoteeRepository.delete(devotee);

        Address address = devotee.getAddress();
        devoteeRepository.delete(devotee);

        boolean addressInUse = devoteeRepository.existsByAddress_Id(address.getId());
        if (!addressInUse) {
            addressRepository.deleteById(address.getId());
        }
    }

    public Devotee findDevoteeById(Long devoteeId) {
        return devoteeRepository.findById(devoteeId).orElseThrow(() -> new NotFoundException(
                "No devotee found associated with id : " + devoteeId));
    }

    public Devotee findDevoteeByDonation(Long donationId) {
        return devoteeRepository.findByDonations_Id(donationId)
                .orElseThrow(() -> new NotFoundException("No devotee found for donation ID: " + donationId));
    }

    public void saveUpdatedDevotee(Devotee devotee) {
        devoteeRepository.save(devotee);
    }

    public void saveDevotees(List<DevoteeDto> devoteeDtos) {
        List<Devotee> existingDevotees = devoteeRepository.findAll();
        Map<String, Devotee> existingDevoteesMap = existingDevotees.stream().collect(Collectors.toMap(devotee ->
                buildDevoteeKey(devotee.getFirstName(), devotee.getLastName(), devotee.getFatherName()), devotee -> devotee));

        List<Address> existingAddresses = addressRepository.findAll();
        Map<String, Address> existingAddressMap = existingAddresses.stream().collect(Collectors.toMap(address ->
                        buildAddressKey(address.getHouseNumber(), address.getCity(), address.getState(), address.getCountry(), address.getPincode()),
                address -> address));

        List<Devotee> devoteesToSave = new ArrayList<>();
        Map<String, Address> dtosAddressMap = new HashMap<>();

        Map<String, DevoteeDto> mergedDevoteeDtoMap = getMergedDevoteeMap(devoteeDtos);
        List<DevoteeDto> mergedDevoteeDtos = new ArrayList<>(mergedDevoteeDtoMap.values());

        for (DevoteeDto devoteeDto : mergedDevoteeDtos) {
            String devoteeKey = buildDevoteeKey(devoteeDto.getFirstName(), devoteeDto.getLastName(), devoteeDto.getFatherName());

            if (existingDevoteesMap.containsKey(devoteeKey)) {
                Devotee devotee = existingDevoteesMap.get(devoteeKey);
                String addressKey = buildAddressKey(devotee.getAddress().getHouseNumber(), devotee.getAddress().getCity(), devotee.getAddress().getState(),
                        devotee.getAddress().getCountry(), devotee.getAddress().getPincode());
                String addressDtoKey = buildAddressKey(devoteeDto.getAddress().getHouseNumber(), devoteeDto.getAddress().getCity(), devoteeDto.getAddress().getState(),
                        devoteeDto.getAddress().getCountry(), devoteeDto.getAddress().getPincode());

                devoteesToSave.add(DevoteeDto.updateDevotee(devotee, devoteeDto, addressKey.equals(addressDtoKey)));
            } else {
                devoteesToSave.add(buildNewDevotee(devoteeDto, dtosAddressMap, existingAddressMap));
            }
        }
        devoteeRepository.saveAllAndFlush(devoteesToSave);
    }

    private Map<String, DevoteeDto> getMergedDevoteeMap(List<DevoteeDto> devoteeDtos) {
        return devoteeDtos.stream()
                .collect(Collectors.toMap(
                        dto -> buildDevoteeKey(dto.getFirstName(), dto.getLastName(), dto.getFatherName()),
                        dto -> dto,
                        (existing, duplicate) -> {
                            if (duplicate.getDonation() != null) {
                                existing.getDonation().addAll(duplicate.getDonation());
                            }
                            return existing;
                        }
                ));
    }

    private Devotee buildNewDevotee(DevoteeDto devoteeDto, Map<String, Address> dtosAddressMap, Map<String, Address> existingAddressMap) {
        String addressKey = buildAddressKey(
                devoteeDto.getAddress().getHouseNumber(),
                devoteeDto.getAddress().getCity(),
                devoteeDto.getAddress().getState(),
                devoteeDto.getAddress().getCountry(),
                devoteeDto.getAddress().getPincode()
        );

        Address address;
        if (dtosAddressMap.containsKey(addressKey)) {
            address = dtosAddressMap.get(addressKey);
        } else {
            Address existingAddress = existingAddressMap.get(addressKey);
            if (null != existingAddress) {
                address = existingAddress;
            } else {
                address = AddressDto.toEntity(devoteeDto.getAddress());
            }
            dtosAddressMap.put(addressKey, address);
        }

        Devotee newDevotee = DevoteeDto.buildDevotees(devoteeDto);
        newDevotee.setAddress(address);
        return newDevotee;
    }

    private String buildDevoteeKey(String firstName, String lastName, String fatherName) {
        return firstName.trim().replace(" ", "").toLowerCase() + lastName.trim().replace(" ", "").toLowerCase()
                + fatherName.trim().replace(" ", "").toLowerCase();
    }

    private String buildAddressKey(String houseNumber, String city, String state, String country, Integer pincode) {
        return houseNumber.trim().replace(" ", "").toLowerCase() + city.trim().replace(" ", "").toLowerCase()
                + state.trim().replace(" ", "").toLowerCase() + country.trim().replace(" ", "").toLowerCase()
                + String.valueOf(pincode).trim().replace(" ", "").toLowerCase();
    }

    public List<Devotee> getDevoteesBy(Specification<Devotee> spec) {
        return devoteeRepository.findAll(spec);
    }
}
