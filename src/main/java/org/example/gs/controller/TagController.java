package org.example.gs.controller;

import org.example.gs.config.JdbcConfig;
import org.example.gs.dao.JdbcTagDao;
import org.example.gs.model.Tag;
import org.example.gs.service.TagService;
import org.example.gs.service.TagServiceImpl;
import org.example.gs.model.GsonResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import com.google.gson.*;

@Controller
public class TagController {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    private final TagService tagService = new TagServiceImpl(new JdbcTagDao(JdbcConfig.getDataSource()));
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @RequestMapping(value = "/tags", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAllTags() {
        try {
            GsonResponse<List<Tag>> response = new GsonResponse<>();
            response.setResult(OK);
            response.setResponse(tagService.getAll());
            return gson.toJson(response);
        } catch (Exception e) {
            GsonResponse<String> response = new GsonResponse<>();
            response.setResult(ERROR);
            response.setResponse(e.getMessage());
            return gson.toJson(response);
        }
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addTag(@RequestParam String name) {
        Tag tag = new Tag();
        tag.setName(name);
        try {
            tag.setId(tagService.add(tag));
            GsonResponse<Tag> response = new GsonResponse<>();
            response.setResult(OK);
            response.setResponse(tag);
            return gson.toJson(response);
        } catch (IllegalArgumentException e) {
            GsonResponse<String> response = new GsonResponse<>();
            response.setResult(ERROR);
            response.setResponse("Tag with name '" + name + "' already exists.");
            return gson.toJson(response);
        }
    }

    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getTagById(@PathVariable("tagId") long id) {
        try {
            Optional<Tag> optional = tagService.getById(id);
            if (optional.isEmpty()) {
                GsonResponse<String> response = new GsonResponse<>();
                response.setResult(OK);
                response.setResponse("No tag with id '" + id + "' is present.");
                return gson.toJson(response);
            }
            GsonResponse<Tag> response = new GsonResponse<>();
            response.setResult(OK);
            response.setResponse(optional.get());
            return gson.toJson(response);
        } catch (Exception e) {
            GsonResponse<String> response = new GsonResponse<>();
            response.setResult(ERROR);
            response.setResponse(e.getMessage());
            return gson.toJson(response);
        }
    }

    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteTag(@PathVariable("tagId") long id) {
        GsonResponse<String> response = new GsonResponse<>();
        try {
            Optional<Tag> optional = tagService.getById(id);
            if (optional.isEmpty()) {
                response.setResult(OK);
                response.setResponse("No tag with id '" + id + "' is present.");

            } else {
                tagService.remove(optional.get());
                response.setResult(OK);
                response.setResponse("Tag with id '" + id + "' successfully deleted.");
            }
        } catch (Exception e) {
            response.setResult(ERROR);
            response.setResponse(e.getMessage());
        }
        return gson.toJson(response);
    }
}
