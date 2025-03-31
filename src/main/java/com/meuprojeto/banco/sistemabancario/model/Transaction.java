package com.meuprojeto.banco.sistemabancario.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    public Transaction(Account accountOrigin, TransactionType type, BigDecimal value) {
        this.accountOrigin = accountOrigin;
        this.type = type;
        this.value = value;
    }

    public Transaction(Account accountOrigin, Account accountTarget, BigDecimal value) {
        this.accountOrigin = accountOrigin;
        this.accountTarget = accountTarget;
        this.type = TransactionType.TRANSFERENCIA;
        this.value = value;
    }

    @Id
    @Column (name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "valor", precision = 18, scale = 2, nullable = false)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_conta", nullable = false)
    private Account accountOrigin;

    @ManyToOne
    @JoinColumn(name = "id_conta_destino")
    private Account accountTarget;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private TransactionType type;

    @Column(name = "descricao", length = 50)
    private String description;

    @CreationTimestamp
    @Column(name = "data_transacao", nullable = false, updatable = false)
    private LocalDateTime transactionDate;
}
