package com.example.project.entities;

import com.example.project.repositories.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Donation")
@Entity
@org.hibernate.annotations.Cache(region = "default", usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Donation extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_seq_gen")
    @SequenceGenerator(name = "donation_seq_gen", sequenceName = "donation_seq", allocationSize = 1)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "donation_type", nullable = false)
    private Integer donationType;

    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;
}
