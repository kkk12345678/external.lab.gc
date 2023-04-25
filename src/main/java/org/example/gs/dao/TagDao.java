package org.example.gs.dao;

import org.example.gs.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends EntityDao<Tag> {
    List<Tag> getByGiftCertificateId(long id);
    Optional<Tag> getByName(String name);
}
