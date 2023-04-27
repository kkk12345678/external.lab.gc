package org.example.gs.service;

import org.example.gs.dao.EntityDao;
import org.example.gs.dao.TagDao;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll("");
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
    public void remove(long id) {
        tagDao.delete(id);
    }

    @Override
    public Optional<Tag> getById(long id) {
        return tagDao.getById(id);
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return tagDao.getByName(name);
    }


}
