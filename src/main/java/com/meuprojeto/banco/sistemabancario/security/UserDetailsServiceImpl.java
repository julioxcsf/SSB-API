package com.meuprojeto.banco.sistemabancario.security;

import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.repository.ClientRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepository repository;

    public UserDetailsServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado"));

        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword()) // precisa estar criptografada!
                .roles("CLIENTE") // ou "ADMIN" se quiser diferenciar
                .build();
    }
}
