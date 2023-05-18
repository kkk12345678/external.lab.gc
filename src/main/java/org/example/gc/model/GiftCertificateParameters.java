package org.example.gc.model;

import lombok.Data;

@Data
public class GiftCertificateParameters {
    private String[] sort;
    private String[] search;
    private String tagName;
    private Integer limit;
    private Integer page;
}