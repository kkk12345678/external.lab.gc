package org.example.gc.dao;

import org.apache.logging.log4j.Logger;
import org.example.gc.model.GiftCertificate;

import org.example.gc.model.Parameters;
import org.example.gc.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
    private static final String SQL_INSERT = """
            insert into gift_certificates
            (gift_certificate_id, gift_certificate_name, description, price, duration, create_date, last_update_date)
            values (default, ?, ?, ?, ?, ?, ?)
            """;
    private static final String SQL_DELETE = "delete from gift_certificates where gift_certificate_id = ?";
    private static final String SQL_UPDATE = """
            update gift_certificates
            set gift_certificate_name = ?, description = ?, price = ?, duration = ?, last_update_date = ?
            where gift_certificate_id = ?
            """;
    private static final String SQL_SELECT_ALL = """
            select * from
                (
                select gc.*, t.*, dense_rank() over (%s) count
            	from gift_certificates as gc
                left join gift_certificate_tags as gct on gct.gift_certificate_id = gc.gift_certificate_id
            	left join tags as t on t.tag_id = gct.tag_id
            	%s
            	) result
            	%s
            """;
    private static final String SQL_SELECT_BY_ID = """
            select gc.*, t.* from gift_certificates as gc
            left join gift_certificate_tags  as gct on gct.gift_certificate_id = gc.gift_certificate_id
            left join tags as t on t.tag_id = gct.tag_id
            where gc.gift_certificate_id = ?
            """;
    private static final String SQL_SELECT_BY_NAME = """
            select gc.*, t.* from gift_certificates as gc
            left join gift_certificate_tags  as gct on gct.gift_certificate_id = gc.gift_certificate_id
            left join tags as t on t.tag_id = gct.tag_id
            where gc.gift_certificate_name = ?
            """;
    private static final String SQL_INSERT_GIFT_CERTIFICATE_TAG =
            "insert into gift_certificate_tags (gift_certificate_id, tag_id) values (?, ?)";
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TAGS =
            "delete from gift_certificate_tags where gift_certificate_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private Logger logger;

    @Override
    public Collection<GiftCertificate> getAll(Parameters parameters) {
        Collection<GiftCertificate> giftCertificates = jdbcTemplateObject.query(
                String.format(SQL_SELECT_ALL,
                        parameters.orderClause(), parameters.whereClause(), parameters.limitClause()),
                new GiftCertificateResultSetExtractor());
        if (giftCertificates == null || giftCertificates.isEmpty()) {
            logger.info("No gift certificates found.");
        } else {
            logger.info("Found " + giftCertificates.size() + " gift certificates.");
        }
        return giftCertificates;
    }

    @Override
    public long insert(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            String date = getDate();
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setDouble(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setString(5, date);
            preparedStatement.setString(6, date);
            return preparedStatement;
        }, keyHolder);
        logger.info(giftCertificate + " inserted");
        long generatedId = keyHolder.getKey().longValue();
        logger.info("Generated id : " + generatedId);
        return generatedId;
    }

    @Override
    public void delete(long id) {
        if (getById(id) != null) {
            jdbcTemplateObject.update(SQL_DELETE , id);
            logger.info("Successfully deleted tag with id " + id);
        } else {
            logger.info("There is no gift certificate with id " + id);
        }
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplateObject.update(SQL_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                getDate(),
                giftCertificate.getId());
        logger.info(giftCertificate + " updated");
    }

    @Override
    public void deleteTags(long id) {
        int n = jdbcTemplateObject.update(SQL_DELETE_GIFT_CERTIFICATE_TAGS, id);
        logger.info(n + " tags were successfully deleted for gift certificate with id " + id);
    }

    @Override
    public GiftCertificate getById(long id) {
        List<GiftCertificate> list = jdbcTemplateObject.query(SQL_SELECT_BY_ID, new GiftCertificateResultSetExtractor(), id);
        if (list == null || list.isEmpty()) {
            logger.info("No gift certificate found with id " + id);
            return null;
        }
        logger.info(list.get(0) + " found");
        return list.get(0);
    }

    @Override
    public GiftCertificate getByName(String name) {
        List<GiftCertificate> list = jdbcTemplateObject.query(
                SQL_SELECT_BY_NAME,
                new GiftCertificateResultSetExtractor(), name);
        if (list == null || list.isEmpty()) {
            logger.info("No gift certificate found with name " + name);
            return null;
        }
        logger.info(list.get(0) + " found");
        return list.get(0);
    }

    @Override
    public void insert(long giftCertificateId, long tagId) {
        jdbcTemplateObject.update(SQL_INSERT_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId);
    }

    private static class GiftCertificateResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {
        @Override
        public List<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Set<Long> ids = new HashSet<>();
            List<GiftCertificate> list = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("gift_certificate_id");
                if (!ids.contains(id)) {
                    GiftCertificate giftCertificate = new GiftCertificate();
                    giftCertificate.setId(id);
                    giftCertificate.setName(rs.getString("gift_certificate_name"));
                    giftCertificate.setDescription(rs.getString("description"));
                    giftCertificate.setPrice(rs.getDouble("price"));
                    giftCertificate.setDuration(rs.getInt("duration"));
                    giftCertificate.setCreateDate(rs.getString("create_date"));
                    giftCertificate.setLastUpdateDate(rs.getString("last_update_date"));
                    giftCertificate.setTags(new ArrayList<>());
                    list.add(giftCertificate);
                    ids.add(id);
                }
                long tagId = rs.getLong("tag_id");
                if (tagId != 0) {
                    Tag tag = new Tag();
                    tag.setId(tagId);
                    tag.setName(rs.getString("tag_name"));
                    list.get(list.size() - 1).addTag(tag);
                }
            }
            return list;
        }
    }
    private static String getDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }
}