package com.meuprojeto.banco.sistemabancario.security;

import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository repository;


    //função usa o email para buscar no db a senha hash do cliente
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado."));

        //a comparação entre a senha hash e a de entrada no LoginDTO
        //é feita automaticamente pelo Spring Security

        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword()) //senha hash
                .roles("CLIENTE")
                .build();
    }
}
