package org.example.gs.service;

import org.example.gs.dao.EntityDao;
import org.example.gs.dao.TagDao;
import org.example.gs.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }


    @Override
    public long add(Tag tag) {
        Optional<Tag> optional = tagDao.getByName(tag.getName());
        if (optional.isEmpty()) {
            return tagDao.insert(tag);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void remove(Tag tag) {
        tagDao.delete(tag.getId());
    }



    @Override
    public Optional<Tag> getById(long id) {
        return tagDao.getById(id);
    }


}
