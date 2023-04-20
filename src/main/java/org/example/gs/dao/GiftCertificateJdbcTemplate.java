package org.example.gs.dao;

import org.example.gs.model.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GiftCertificateJdbcTemplate implements EntityDao<GiftCertificate> {
    private static final String SQL_INSERT = "insert into gift_certificates (id, name) values (DEFAULT, ?)";
    private static final String SQL_DELETE = "delete from gift_certificates where id = ?)";
    private static final String SQL_UPDATE = "update gift_certificates set name = ? where id = ?";
    private static final String SQL_SELECT = "select * from gift_certificates";
    private static final String SQL_SELECT_BY_ID = "select * from gift_certificates where id = ?";
    private static final String SQL_SELECT_BY_NAME = "select * from gift_certificates where name = ?";

    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource dataSource) {
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(GiftCertificate giftCertificate) {
        jdbcTemplateObject.update(SQL_INSERT, giftCertificate.getId(), giftCertificate.getName());
    }

    @Override
    public void delete(long id) {
        jdbcTemplateObject.update(SQL_DELETE , id);
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplateObject.update(SQL_UPDATE, giftCertificate.getName(), giftCertificate.getId());
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        List<GiftCertificate> list = jdbcTemplateObject.query(SQL_SELECT_BY_ID, new GiftCertificateMapper(), id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        List<GiftCertificate> list = jdbcTemplateObject.query(SQL_SELECT_BY_NAME, new GiftCertificateMapper(), name);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplateObject.query(SQL_SELECT, new GiftCertificateMapper());
    }

    private static class GiftCertificateMapper implements RowMapper<GiftCertificate> {
        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId(rs.getLong("id"));
            giftCertificate.setName(rs.getString("name"));
            return giftCertificate;
        }
    }
}