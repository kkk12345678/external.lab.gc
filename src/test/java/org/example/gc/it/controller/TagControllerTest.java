package org.example.gc.it.controller;

import com.google.gson.Gson;
import org.example.gc.Application;
import org.example.gc.parameters.TagParameters;
import org.example.gc.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
class TagControllerTest {
    private static final String URL = "http://gc-1.eu-central-1.elasticbeanstalk.com/tags";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagRepository tagRepository;

    @Test
    void getAllOrdersTest() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(tagRepository.getAll(new TagParameters()))));
    }
}
