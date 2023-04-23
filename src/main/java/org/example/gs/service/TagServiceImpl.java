package org.example.gs.service;

import org.example.gs.dao.TagJdbcTemplate;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagServiceImpl implements TagService {
    @Autowired
    private TagJdbcTemplate tagJdbcTemplate;

    @Override
    public List<Tag> getFromIds(List<Long> ids) {
        return ids.stream()
                .map((id) -> tagJdbcTemplate.getById(id).orElseThrow())
                .collect(Collectors.toList());
    }

    @Override
    public long add(Tag tag) {
        Optional<Tag> optional = tagJdbcTemplate.getByName(tag.getName());
        if (optional.isEmpty()) {
            tagJdbcTemplate.insert(tag);
        } else {
            throw new IllegalArgumentException();
        }
        return tagJdbcTemplate.getByName(tag.getName()).orElseThrow().getId();
    }

    @Override
    public void remove(Tag tag) {
        tagJdbcTemplate.delete(tag.getId());
    }

    @Override
    public List<Tag> getAll() {
        return tagJdbcTemplate.getAll();
    }

    @Override
    public Optional<Tag> getById(long id) {
        return tagJdbcTemplate.getById(id);
    }
}
