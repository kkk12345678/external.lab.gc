package org.example.gc.dao;

import org.example.gc.model.TagParameters;

public class TagParametersHandler implements ParametersHandler {
    private final TagParameters tagParameters;
    public TagParametersHandler(TagParameters tagParameters) {
        this.tagParameters = tagParameters;
    }
}