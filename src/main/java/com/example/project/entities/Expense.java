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
@Table(name = "Expense")
@Entity
@org.hibernate.annotations.Cache(region = "default", usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Expense extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq_gen")
    @SequenceGenerator(name = "expense_seq_gen", sequenceName = "expense_seq", allocationSize = 1)
    private Long id;

    @Column(name = "payee", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "expense_type", nullable = false)
    private Integer expenseType;
}
