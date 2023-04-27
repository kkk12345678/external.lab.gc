package org.example.gs.service;

import org.example.gs.dao.GiftCertificateDao;
import org.example.gs.dao.TagDao;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private TagDao tagDao;


    @Override
    public List<GiftCertificate> getAll(String parameters) {
        return giftCertificateDao.getAll(parameters);
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        return giftCertificateDao.getById(id);
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        return giftCertificateDao.getByName(name);
    }

    @Override
    @Transactional
    public long add(GiftCertificate giftCertificate) {
        giftCertificate.setId(giftCertificateDao.insert(giftCertificate));
        for (Tag tag : giftCertificate.getTags()) {
            Optional<Tag> optional = tagDao.getByName(tag.getName());
            if (optional.isEmpty()) {
                tag.setId(tagDao.insert(tag));
                optional = Optional.of(tag);
            }
            tag.setId(optional.get().getId());
            giftCertificateDao.insert(giftCertificate.getId(), optional.get().getId());
        }
        return giftCertificate.getId();
    }

    @Override
    public void remove(long id) {
        giftCertificateDao.delete(id);
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        giftCertificateDao.update(giftCertificate);
    }
}
