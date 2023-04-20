package org.example.gs.dao;

import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class GiftCertificateTagJdbcTemplate implements GiftCertificateTagDao {
    private static final String SQL_INSERT = "insert into certificate_tags (tag_id, gift_certificate_id) values (?, ?)";
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource dataSource) {
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Tag tag, GiftCertificate giftCertificate) {
        jdbcTemplateObject.update(SQL_INSERT, tag.getId(), giftCertificate.getId());
    }
}