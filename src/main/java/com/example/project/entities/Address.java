package com.example.project.entities;

import com.example.project.repositories.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Address")
@Entity
@org.hibernate.annotations.Cache(region = "default", usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Address extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq_gen")
    @SequenceGenerator(name = "address_seq_gen", sequenceName = "address_seq", allocationSize = 1)
    private Long id;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state",nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "pincode", nullable = false)
    private int pincode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (pincode != address.pincode) return false;
        if (!houseNumber.equals(address.houseNumber)) return false;
        if (!city.equals(address.city)) return false;
        if (!state.equals(address.state)) return false;
        return country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseNumber, city, state, country, pincode);
    }
}
