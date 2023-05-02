package org.example.gs.dao;

import org.apache.logging.log4j.Logger;
import org.example.gs.model.Parameters;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class JdbcTagDao implements TagDao {
    private static final String SQL_INSERT = "insert into tags (tag_id, tag_name) values (default, ?)";
    private static final String SQL_DELETE = "delete from tags where tag_id = ?";
    private static final String SQL_UPDATE = "update tags set tag_name = ? where tag_id = ?";
    private static final String SQL_SELECT = "select * from tags";
    private static final String SQL_SELECT_BY_ID = "select * from tags where tag_id = ?";
    private static final String SQL_SELECT_BY_NAME = "select * from tags where tag_name = ?";

    @Autowired
    private Logger logger;
    @Autowired
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public Collection<Tag> getAll(Parameters parameters) {
        Collection<Tag> tags = jdbcTemplateObject.query(SQL_SELECT + parameters.whereClause(), new TagMapper());
        logger.info(tags.size() + " tags found");
        return tags;
    }

    @Override
    public long insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        long generatedId = keyHolder.getKey().longValue();
        logger.info(String.format("Tag successfully inserted. Generated id : %s.", generatedId));
        return generatedId;
    }

    @Override
    public void delete(long id) {
        jdbcTemplateObject.update(SQL_DELETE , id);
        logger.info(String.format("Tag with id : %d successfully deleted.", id));
    }

    @Override
    public Tag getById(long id) {
        List<Tag> list = jdbcTemplateObject.query(SQL_SELECT_BY_ID, new TagMapper(), id);
        if (list.isEmpty()) {
            logger.info("No tag found with id : " + id);
            return null;
        }
        logger.info(list.get(0) + " found");
        return list.get(0);
    }

    @Override
    public Tag getByName(String name) {
        List<Tag> list = jdbcTemplateObject.query(SQL_SELECT_BY_NAME, new TagMapper(), name);
        if (list.isEmpty()) {
            logger.info("No tag found with name : '" + name + "'");
            return null;
        }
        logger.info(list.get(0) + " found");
        return list.get(0);
    }

    private static class TagMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getLong("tag_id"));
            tag.setName(rs.getString("tag_name"));
            return tag;
        }
    }
}