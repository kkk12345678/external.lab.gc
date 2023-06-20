package org.example.gc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.gc.dto.UserDto;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = "user_name")})
@Data
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public UserDto toUserDto() {
        return new UserDto(name, role.getName(), password);
    }
}
