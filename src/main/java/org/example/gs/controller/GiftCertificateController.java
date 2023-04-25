package org.example.gs.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.gs.config.JdbcConfig;
import org.example.gs.dao.JdbcGiftCertificateDao;
import org.example.gs.dao.JdbcTagDao;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.GsonResponse;
import org.example.gs.model.Tag;
import org.example.gs.service.GiftCertificateService;
import org.example.gs.service.GiftCertificateServiceImpl;
import org.example.gs.service.TagService;
import org.example.gs.service.TagServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GiftCertificateController {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    private final GiftCertificateService giftCertificateService =
            new GiftCertificateServiceImpl(
                    new JdbcGiftCertificateDao(JdbcConfig.getDataSource()),
                    new JdbcTagDao(JdbcConfig.getDataSource())
            );
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @RequestMapping(value = "/gift-certificates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAllGiftCertificates(@RequestParam(required = false) String[] sort,
                                         @RequestParam(required = false) String[] search,
                                         @RequestParam(required = false) Integer limit,
                                         @RequestParam(required = false) Integer page) {
        try {
            GsonResponse<List<GiftCertificate>> response = new GsonResponse<>();
            response.setResult(OK);
            response.setResponse(giftCertificateService.getAll());
            return gson.toJson(response);
        } catch (Exception e) {
            GsonResponse<String> response = new GsonResponse<>();
            response.setResult(ERROR);
            response.setResponse(e.getMessage());
            return gson.toJson(response);
        }
    }


    @RequestMapping(value = "/gift-certificate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addGiftCertificate(@RequestParam String name,
                                     @RequestParam(required = false) String description,
                                     @RequestParam double price,
                                     @RequestParam int duration,
                                     @RequestParam(required = false) String[] tags) {

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        giftCertificate.setTags(Arrays.stream(tags).map(Tag::create).collect(Collectors.toList()));
        try {
            giftCertificate.setId(giftCertificateService.add(giftCertificate));
            GsonResponse<GiftCertificate> response = new GsonResponse<>();
            response.setResult(OK);
            response.setResponse(giftCertificate);
            return gson.toJson(response);
        } catch (IllegalArgumentException e) {
            GsonResponse<String> response = new GsonResponse<>();
            response.setResult(ERROR);
            response.setResponse("Gift certificate with name '" + name + "' already exists.");
        } catch (Exception e) {
            GsonResponse<String> response = new GsonResponse<>();
            response.setResult(ERROR);
            response.setResponse(e.getMessage());
            return gson.toJson(response);
        }
        return null;
    }

}
