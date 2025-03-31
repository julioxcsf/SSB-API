package com.meuprojeto.banco.sistemabancario.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "clients")
@Data
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome",length = 100, nullable = false)
    private String name;

    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(name = "nascimento", nullable = false)
    private LocalDate born;

    @Column(name = "email",length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String passwordHash;

    @Column(name = "ocupacao")
    private String occupation ;

    @CreatedDate
    @Column(name = "data_cadastro",updatable = false, nullable = false)
    private LocalDate createdAt;
}
