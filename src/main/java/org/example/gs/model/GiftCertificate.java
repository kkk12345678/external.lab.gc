package org.example.gs.model;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GiftCertificate {
    private @Getter @Setter long id;
    private @Getter @Setter String name;
    private @Getter @Setter String description;
    private @Getter @Setter long duration;
    private @Getter @Setter Date createDate;
    private @Getter @Setter Date lastUpdateDate;
}