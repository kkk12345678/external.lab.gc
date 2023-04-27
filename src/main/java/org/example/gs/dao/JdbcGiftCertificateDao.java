package org.example.gs.dao;

import org.apache.logging.log4j.Logger;
import org.example.gs.model.GiftCertificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class JdbcGiftCertificateDao implements GiftCertificateDao {
    @Autowired
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    private Logger logger;
    private static final String SQL_INSERT_GIFT_CERTIFICATE = "insert into gift_certificates " +
            "(gift_certificate_id, gift_certificate_name, description, price, duration, create_date, last_update_date) " +
            "values (default, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "delete from gift_certificates where gift_certificate_id = ?";
    private static final String SQL_UPDATE = """
            update gift_certificates " +
            "set gift_certificate_name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ?
            "where gift_certificate_id = ?
            """;
    private static final String SQL_SELECT_ALL = """
            select gift_certificates.*, tags.* from gift_certificates
            left join gift_certificate_tags on gift_certificate_tags.gift_certificate_id = gift_certificates.gift_certificate_id
            inner join tags on tags.tag_id = gift_certificate_tags.tag_id
            """;
    private static final String SQL_SELECT_BY_ID = """
            select gift_certificates.*, tags.* from gift_certificates
            left join gift_certificate_tags on gift_certificate_tags.gift_certificate_id = gift_certificates.gift_certificate_id
            inner join tags on tags.tag_id = gift_certificate_tags.tag_id
            where gift_certificates.gift_certificate_id = ?
            """;
    private static final String SQL_SELECT_BY_NAME = """
            select gift_certificates.*, tags.* from gift_certificates
            left join gift_certificate_tags on gift_certificate_tags.gift_certificate_id = gift_certificates.gift_certificate_id
            inner join tags on tags.tag_id = gift_certificate_tags.tag_id
            where gift_certificates.gift_certificate_id = ?
            """;
    private static final String SQL_INSERT_GIFT_CERTIFICATE_TAG =
            "insert into gift_certificate_tags (gift_certificate_id, tag_id) values (?, ?)";

    @Override
    public List<GiftCertificate> getAll(String parameters) {
        Map<Long, GiftCertificate> map = new HashMap<>();
        jdbcTemplateObject.query(SQL_SELECT_ALL + parameters, new GiftCertificateRowCallbackHandler(map));
        logger.info("Found " + map.keySet().size() + " certificates");
        return map.values().stream().toList();
    }

    @Override
    public long insert(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_INSERT_GIFT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            prepareStatement(ps, giftCertificate);
            return ps;
        }, keyHolder);
        logger.info(giftCertificate + " inserted");
        long generatedId = keyHolder.getKey().longValue();
        logger.info("Generated id : " + generatedId);
        return generatedId;
    }

    @Override
    public void delete(long id) {
        Optional<GiftCertificate> optional = getById(id);
        if (optional.isPresent()) {
            jdbcTemplateObject.update(SQL_DELETE , id);
            logger.info(optional.get() + "deleted");
        } else {
            logger.info("There is no row with id " + id);
        }
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplateObject.update(SQL_UPDATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration());
        logger.info(giftCertificate + " updated");
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        Map<Long, GiftCertificate> map = new HashMap<>();
        jdbcTemplateObject.query(SQL_SELECT_BY_ID, new GiftCertificateRowCallbackHandler(map), id);
        List<GiftCertificate> list = map.values().stream().toList();
        if (list.isEmpty()) {
            logger.info("No gift certificate found with id " + id);
            return Optional.empty();
        }
        logger.info(list.get(0) + " found");
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        Map<Long, GiftCertificate> map = new HashMap<>();
        jdbcTemplateObject.query(SQL_SELECT_BY_NAME, new GiftCertificateRowCallbackHandler(map), name);
        List<GiftCertificate> list = map.values().stream().toList();
        if (list.isEmpty()) {
            logger.info("No gift certificate found with name " + name);
            return Optional.empty();
        }
        logger.info(list.get(0) + " found");
        return Optional.of(list.get(0));
    }

    @Override
    public void insert(long giftCertificateId, long tagId) {
        jdbcTemplateObject.update(SQL_INSERT_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId);
    }

    private record GiftCertificateRowCallbackHandler(Map<Long, GiftCertificate> map) implements RowCallbackHandler {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            long id = rs.getLong("gift_certificate_id");
            if (!map.containsKey(id)) {
                GiftCertificate giftCertificate = new GiftCertificate();
                giftCertificate.setId(id);
                giftCertificate.setName(rs.getString("gift_certificate_name"));
                giftCertificate.setDescription(rs.getString("description"));
                giftCertificate.setPrice(rs.getDouble("price"));
                giftCertificate.setDuration(rs.getInt("duration"));
                giftCertificate.setCreateDate(rs.getString("create_date"));
                giftCertificate.setLastUpdateDate(rs.getString("last_update_date"));
                giftCertificate.setTags(new ArrayList<>());
                if (rs.getLong("tag_id") != 0L) {
                    giftCertificate.addTag(rs.getLong("tag_id"), rs.getString("tag_name"));
                }
                map.put(id, giftCertificate);
            } else {
                if (rs.getLong("tag_id") != 0L) {
                    map.get(id).addTag(rs.getLong("tag_id"), rs.getString("tag_name"));
                }
            }
        }
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