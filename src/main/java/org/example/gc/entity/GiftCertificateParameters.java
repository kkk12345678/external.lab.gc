package org.example.gc.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GiftCertificateParameters extends Parameters {
    private String[] sort;
    private String[] search;
    private String tagName;
    private Integer limit;
    private Integer page;
}