package org.example.gc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "tags", uniqueConstraints = {@UniqueConstraint(columnNames = "tag_name")})
@Data
@NoArgsConstructor
public class Tag implements Serializable {
    @Id
    @Column(name = "tag_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
