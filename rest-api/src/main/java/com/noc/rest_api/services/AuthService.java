package com.noc.rest_api.services;

import com.noc.rest_api.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noc.rest_api.dto.security.AccountCredentialsDto;
import com.noc.rest_api.dto.security.TokenDto;
import com.noc.rest_api.exception.RequiredObjectIsNullException;
import com.noc.rest_api.model.User;
import com.noc.rest_api.repository.UserRepository;
import com.noc.rest_api.security.jwt.JwtTokenProvider;

@Service
public class AuthService {
    
    @Autowired
    private Mapper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository uRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    AuthService(Mapper mapper) {
        this.mapper = mapper;
    }

    public TokenDto signIn(AccountCredentialsDto credentialsDto){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credentialsDto.getUsername(), credentialsDto.getPassword())
        );

        var user = uRepository.findByUsername(credentialsDto.getUsername());

        if (user == null) throw new UsernameNotFoundException("Username " + credentialsDto.getUsername() + " not found!");

        var tokenResponse = tokenProvider.createAccesToken(credentialsDto.getUsername(), user.getRoles());

        return tokenResponse;
    }

    public TokenDto refresh(String username, String refreshToken){
        var user = uRepository.findByUsername(username);
        TokenDto token;

        if (user != null){
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

        return token;
    }

    public AccountCredentialsDto create(AccountCredentialsDto credentialsDto) {
        if (credentialsDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var entity = new User();
        entity.setFullName(credentialsDto.getFullname());
        entity.setUsername(credentialsDto.getUsername());
        entity.setPassword(passwordEncoder.encode(credentialsDto.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLoked(true);
        entity.setEnabled(true);
        entity.setCredentialsNonExpired(true);
        
        return mapper.parseObject(uRepository.save(entity), AccountCredentialsDto.class);
    }
}
