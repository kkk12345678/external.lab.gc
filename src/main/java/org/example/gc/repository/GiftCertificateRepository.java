package org.example.gc.repository;

import org.example.gc.entity.GiftCertificate;
import org.example.gc.parameters.GiftCertificateParameters;

import java.util.List;

public interface GiftCertificateRepository extends EntityRepository<GiftCertificate> {
    List<GiftCertificate> getAll(GiftCertificateParameters parameters);
}