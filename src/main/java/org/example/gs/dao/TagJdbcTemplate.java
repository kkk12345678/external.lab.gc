package org.example.gs.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;
import org.example.gs.service.GiftCertificateTagsService;
import org.example.gs.util.TagMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TagJdbcTemplate implements EntityDao<Tag> {
    private static final String SQL_INSERT = "insert into tags (id, name) values (default, ?)";
    private static final String SQL_DELETE = "delete from tags where id = ?";
    private static final String SQL_UPDATE = "update tags set name = ? where id = ?";
    private static final String SQL_SELECT = "select * from tags";
    private static final String SQL_SELECT_BY_ID = "select * from tags where id = ?";
    private static final String SQL_SELECT_BY_NAME = "select * from tags where name = ?";

    private static final Logger LOGGER = LogManager.getLogger("TAG_DAO");

    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public long insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        //jdbcTemplateObject.update(SQL_INSERT, tag.getName(), keyHolder);
        long generatedId = keyHolder.getKey().longValue();
        LOGGER.info("Generated id : " + generatedId);
        return generatedId;
    }

    @Override
    public void delete(long id) {
        jdbcTemplateObject.update(SQL_DELETE , id);
        LOGGER.info("Tag with id : " + id + " deleted");
    }

    @Override
    public void update(Tag tag) {
        jdbcTemplateObject.update(SQL_UPDATE, tag.getName(), tag.getId());
        LOGGER.info(tag + " updated");
    }

    @Override
    public Optional<Tag> getById(long id) {
        List<Tag> list = jdbcTemplateObject.query(SQL_SELECT_BY_ID, new TagMapper(), id);
        if (list.isEmpty()) {
            LOGGER.info("No tag found with id : " + id);
            return Optional.empty();
        }
        LOGGER.info(list.get(0) + " found");
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<Tag> getByName(String name) {
        List<Tag> list = jdbcTemplateObject.query(SQL_SELECT_BY_NAME, new TagMapper(), name);
        if (list.isEmpty()) {
            LOGGER.info("No tag found with name : '" + name + "'");
            return Optional.empty();
        }
        LOGGER.info(list.get(0) + " found");
        return Optional.of(list.get(0));
    }

    @Override
    public List<Tag> getAll() {
        List<Tag> tags = jdbcTemplateObject.query(SQL_SELECT, new TagMapper());
        LOGGER.info(tags.size() + " tags found");
        return tags;
    }
}