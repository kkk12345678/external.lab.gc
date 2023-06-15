package org.example.gc.repository;

import org.example.gc.entity.Tag;
import org.example.gc.parameters.TagParameters;

import java.util.List;
import java.util.Set;

public interface TagRepository extends EntityRepository<Tag> {
    List<Tag> getByNames(Set<String> tags);
    List<Tag> getAll(TagParameters parameters);
}