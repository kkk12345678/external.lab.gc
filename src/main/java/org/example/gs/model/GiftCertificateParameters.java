package org.example.gs.model;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class GiftCertificateParameters implements Parameters {
    private static final String EOL = System.lineSeparator();
    private String[] sort;
    private String[] search;
    private Integer limit;
    private Integer page;
    private String tagName;

    public String limitClause() {
        List<String> conditions = new ArrayList<>();
        if (limit != null) {
            if (page == null) {
                conditions.add("count <= " + limit);
            } else {
                conditions.add("count <= " + (limit * page));
                conditions.add("count > " + (limit * (page - 1)));
            }
        }
        if (conditions.isEmpty()) {
            return "";
        }
        return "where " + String.join(" and ", conditions);
    }

    @Override
    public String whereClause() {
        if (search == null || search.length == 0) {
            return "";
        }
        return "where " + Arrays.stream(search).map(s -> {
            String[] parts = s.split(",");
            if (parts[0].equals("name")) {
                return "gift_certificate_name like '%" + parts[1] + "%'";
            }
            return "gift_certificate_description like '%" + parts[1] + "%'";
        }).collect(Collectors.joining(" and "));
    }

    @Override
    public String orderClause() {
        if (sort == null || sort.length == 0) {
            return "order by gift_certificate_name asc";
        }
        return "order by " + Arrays.stream(sort).map(s -> {
            String[] parts = s.split(",");
            if (parts[0].equals("name")) {
                return "gift_certificate_name " + parts[1];
            }
            return "create_date " + parts[1];
        }).collect(Collectors.joining(", "));
    }
}