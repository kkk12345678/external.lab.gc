package org.example.gs.service;

import org.example.gs.dao.GiftCertificateDao;
import org.example.gs.dao.TagDao;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateDao.getAll();
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
            giftCertificateDao.insert(giftCertificate.getId(), optional.get().getId());
        }
        return giftCertificate.getId();
    }

    @Override
    public void remove(GiftCertificate giftCertificate) {
        //TODO
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        //TODO
    }


}
