package org.example.gs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gs.model.Tag;
import org.example.gs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public String getAllTags(Model model) {
        model.addAttribute("tags", tagService.getAll());
        return "view_tags";
    }

    @PostMapping("/tag")
    public String addTag(@RequestParam String name, Model model) {
        Tag tag = new Tag();
        tag.setName(name);
        try {
            tagService.add(tag);
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", String.format("Tag with name '%s' already exists.", name));
            return "400";
        }
        return "redirect:tags";
    }

    @GetMapping("/tag/{tagId}")
    public String getTagById(@PathVariable("tagId") long id, Model model) {
        Optional<Tag> optional = tagService.getById(id);
        if (optional.isPresent()) {
            model.addAttribute("tags", List.of(optional.get()));
            return "view_tags";
        }
        model.addAttribute("message", "Invalid 'tagId' parameter.");
        return "400";
    }

    @DeleteMapping("/tag/{tagId}")
    public RedirectView deleteTag(@PathVariable("tagId") long id, RedirectAttributes attributes) {
        Optional<Tag> optional = tagService.getById(id);
        if (optional.isPresent()) {
            tagService.remove(optional.get());
            return new RedirectView("/tags");
        }
        attributes.addFlashAttribute("message", "Invalid 'tagId' parameter.");
        return new RedirectView("/400");
    }
}
