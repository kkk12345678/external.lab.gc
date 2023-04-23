package org.example.gs.controller;

import org.example.gs.model.GiftCertificate;
import org.example.gs.service.GiftCertificateService;
import org.example.gs.service.GiftCertificateTagsService;
import org.example.gs.util.SortOrder;
import org.example.gs.util.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GiftCertificateController {
    @Autowired
    GiftCertificateService giftCertificateService;
    @Autowired
    GiftCertificateTagsService giftCertificateTagsService;

    @GetMapping("/gift-certificates")
    public String getAllGiftCertificates(@RequestParam(required = false) String sortType,
                                         @RequestParam(required = false) String sortOrder,
                                         Model model) {
        if (sortType == null || sortType.isEmpty()) {
            sortType = "NAME";
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "ASC";
        }
        model.addAttribute("gift_certificates",
                giftCertificateService.getAll(
                        SortType.valueOf(sortType.toUpperCase()),
                        SortOrder.valueOf(sortOrder.toUpperCase())));
        return "view_gift_certificates";
    }

    @PostMapping("/gift-certificate")
    public String addGiftCertificate(@RequestParam String name,
                                     @RequestParam(required = false) String description,
                                     @RequestParam double price,
                                     @RequestParam int duration,
                                     @RequestParam(required = false) String[] tags,
                                     Model model) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        try {
            giftCertificateService.add(giftCertificate, tags);
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", String.format("Gift certificate with name '%s' already exists.", name));
            return "400";
        }
        return "redirect:gift-certificates";
    }
}
