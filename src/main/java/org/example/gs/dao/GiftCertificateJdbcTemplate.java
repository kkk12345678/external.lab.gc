package org.example.gs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.gs.model.GiftCertificate;
import org.example.gs.util.GiftCertificateMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public class GiftCertificateJdbcTemplate implements EntityDao<GiftCertificate> {
    private static final String SQL_INSERT = "insert into gift_certificates " +
            "(id, name, description, price, duration, create_date, last_update_date) " +
            "values (default, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "delete from gift_certificates where id = ?";
    private static final String SQL_UPDATE = "update gift_certificates " +
            "set name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? where id = ?";
    private static final String SQL_SELECT = "select * from gift_certificates";
    private static final String SQL_SELECT_BY_ID = "select * from gift_certificates where id = ?";
    private static final String SQL_SELECT_BY_NAME = "select * from gift_certificates where name = ?";
    private static final String SQL_SELECT_BY_DESCRIPTION = "select * from gift_certificates where description = ?";
    private static final Logger LOGGER = LogManager.getLogger("GS_DAO");
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource dataSource) {
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public long insert(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            prepareStatement(ps, giftCertificate);
            return ps;
        }, keyHolder);
        LOGGER.info(giftCertificate + " inserted");
        long generatedId = keyHolder.getKey().longValue();
        LOGGER.info("Generated id : " + generatedId);
        return generatedId;
    }

    @Override
    public void delete(long id) {
        Optional<GiftCertificate> optional = getById(id);
        if (optional.isPresent()) {
            jdbcTemplateObject.update(SQL_DELETE , id);
            LOGGER.info(optional.get() + "deleted");
        } else {
            LOGGER.info("There is no row with id " + id);
        }
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplateObject.update(SQL_UPDATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration());
        LOGGER.info(giftCertificate + "updated");
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        List<GiftCertificate> list = jdbcTemplateObject.query(SQL_SELECT_BY_ID, new GiftCertificateMapper(), id);
        if (list.isEmpty()) {
            LOGGER.info("No gift certificate found with id " + id);
            return Optional.empty();
        }
        LOGGER.info(list.get(0) + " found");
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        List<GiftCertificate> list = jdbcTemplateObject.query(SQL_SELECT_BY_NAME, new GiftCertificateMapper(), name);
        if (list.isEmpty()) {
            LOGGER.info("No gift certificate found with name '" + name + "'");
            return Optional.empty();
        }
        LOGGER.info(list.get(0) + "found");
        return Optional.of(list.get(0));
    }

    @Override
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> list = jdbcTemplateObject.query(SQL_SELECT, new GiftCertificateMapper());
        LOGGER.info("Found " + list.size() + " certificates");
        return list;
    }

    private static void prepareStatement(PreparedStatement preparedStatement, GiftCertificate giftCertificate)
            throws SQLException {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String date = df.format(new java.util.Date());
        preparedStatement.setString(1, giftCertificate.getName());
        preparedStatement.setString(2, giftCertificate.getDescription());
        preparedStatement.setDouble(3, giftCertificate.getPrice());
        preparedStatement.setInt(4, giftCertificate.getDuration());
        preparedStatement.setString(5, date);
        preparedStatement.setString(6, date);
    }
}