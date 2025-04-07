package com.example.ex_testing.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@Builder
@AllArgsConstructor
public class User{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @JsonBackReference
    @Column(name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public User() {

    }
    public User(String username, String password, List<Role>  role){
        this.username = username;
        this.password = password;
        this.roles = role;
    }
}
