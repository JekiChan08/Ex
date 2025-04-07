package com.example.ex_testing.Service.Impl;

import com.example.ex_testing.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository repository;


    public MyUserDetailService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(user -> {
                    String[] roles = user.getRoles()
                            .stream()
                            .map(role -> role.getRoleName().replace("ROLE_", ""))
                            .toArray(String[]::new);
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .roles(roles)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
