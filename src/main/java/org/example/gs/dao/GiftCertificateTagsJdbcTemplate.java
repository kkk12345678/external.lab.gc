package org.example.gs.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class GiftCertificateTagsJdbcTemplate implements GiftCertificateTagsDao {
    private static final String SQL_SELECT_TAG_ID_BY_CERTIFICATE_ID =
            "select tag_id from gift_certificate_tags where gift_certificate_id = ?";
    private static final String SQL_INSERT =
            "insert into gift_certificate_tags (gift_certificate_id, tag_id) values (?, ?)";

    private JdbcTemplate jdbcTemplateObject;


    @Override
    public void setDataSource(DataSource dataSource) {
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Long> getTagIdByCertificateId(long id) {
        return jdbcTemplateObject
                .query(SQL_SELECT_TAG_ID_BY_CERTIFICATE_ID, (rs, rowNum) -> rs.getLong("tag_id"), id);
    }

    @Override
    public void insert(long giftCertificateId, long tagId) {
        jdbcTemplateObject.update(SQL_INSERT, giftCertificateId, tagId);
    }
}
