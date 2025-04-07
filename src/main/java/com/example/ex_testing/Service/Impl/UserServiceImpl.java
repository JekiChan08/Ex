package com.example.ex_testing.Service.Impl;

import com.example.ex_testing.Model.User;
import com.example.ex_testing.Repositories.RoleRepository;
import com.example.ex_testing.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> {
    private final Long USER_ROLE_ID = 1L;
    private final Long ADMIN_ROLE_ID = 2L;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User findByLogin(String login) {
        return userRepository.findByUsername(login).orElse(null);
    }
    @Override
    public User save(User myUser) {
        if (!userRepository.findByUsername(myUser.getUsername()).isPresent()) {
            myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            myUser.setRoles(List.of(roleRepository.findById(USER_ROLE_ID).get()));
            return userRepository.save(myUser);
        }else {
            return null;
        }
    }


}
