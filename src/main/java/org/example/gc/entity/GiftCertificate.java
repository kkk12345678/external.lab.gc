package org.example.gc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.gc.dto.GiftCertificateResponseDto;

import java.util.Set;

@Entity
@Table(
        name = "gift_certificates",
        uniqueConstraints = {@UniqueConstraint(columnNames = "gift_certificate_name")})
@Data
public class GiftCertificate {
    @Id
    @Column(name = "gift_certificate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gift_certificate_name", nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "create_date", nullable = false)
    private String createDate;

    @Column(name = "last_update_date", nullable = false)
    private String lastUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "gift_certificate_tags",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addTag(long tagId, String tagName) {
        addTag(new Tag(tagId, tagName));
    }

    public GiftCertificateResponseDto toResponseDto() {
        return new GiftCertificateResponseDto(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }
}
