package org.example.gs.dao;

import org.example.gs.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TagJdbcTemplate implements EntityDao<Tag> {
    private static final String SQL_INSERT = "insert into tags (id, name) values (default, ?)";
    private static final String SQL_DELETE = "delete from tags where id = ?";
    private static final String SQL_UPDATE = "update tags set name = ? where id = ?";
    private static final String SQL_SELECT = "select * from tags";
    private static final String SQL_SELECT_BY_ID = "select * from tags where id = ?";
    private static final String SQL_SELECT_BY_NAME = "select * from tags where name = ?";

    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Tag tag) {
        jdbcTemplateObject.update(SQL_INSERT, tag.getName());
    }

    @Override
    public void delete(long id) {
        jdbcTemplateObject.update(SQL_DELETE , id);
    }

    @Override
    public void update(Tag tag) {
        jdbcTemplateObject.update(SQL_UPDATE, tag.getName(), tag.getId());
    }

    @Override
    public Optional<Tag> getById(long id) {
        List<Tag> list = jdbcTemplateObject.query(SQL_SELECT_BY_ID, new TagMapper(), id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public Optional<Tag> getByName(String name) {
        List<Tag> list = jdbcTemplateObject.query(SQL_SELECT_BY_NAME, new TagMapper(), name);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplateObject.query(SQL_SELECT, new TagMapper());
    }

    private static class TagMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getLong("id"));
            tag.setName(rs.getString("name"));
            return tag;
        }
    }
}