package com.meuprojeto.banco.sistemabancario.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "numero_conta", unique = true, nullable = false)
    private Long number;

    @ManyToOne //preciso alterar a coluna para cliente
    @JoinColumn(name = "id_cliente", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private AccountType type;

    @Column(name = "data_criacao", nullable = false)
    @CreatedDate
    private LocalDate createdAt;

    //fazer depois - data_ultima_ação..

    @Column(name = "saldo", precision = 18, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "ativa", nullable = false)
    private boolean active;
}
