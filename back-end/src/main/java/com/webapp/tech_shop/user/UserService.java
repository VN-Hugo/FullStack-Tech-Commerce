package com.webapp.tech_shop.user;
import java.util.Optional;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public Optional<User> findByEmail(String email){
        return repository.findByEmail(email);
    }
}