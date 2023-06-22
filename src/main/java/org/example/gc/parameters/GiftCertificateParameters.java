package org.example.gc.parameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiftCertificateParameters extends Parameters {
    private String[] sort;
    private String[] search;
    private String[] tagNames;
}