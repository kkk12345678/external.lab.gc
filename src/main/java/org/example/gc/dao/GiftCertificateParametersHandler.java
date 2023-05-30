package org.example.gc.dao;

import lombok.Setter;
import org.example.gc.model.GiftCertificateParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Setter
public class GiftCertificateParametersHandler implements ParametersHandler {
    private final GiftCertificateParameters giftCertificateParameters;

    public GiftCertificateParametersHandler(GiftCertificateParameters giftCertificateParameters) {
        this.giftCertificateParameters = giftCertificateParameters;
    }

    public String limitClause() {
        Integer limit = giftCertificateParameters.getLimit();
        Integer page = giftCertificateParameters.getPage();
        Collection<String> conditions = new ArrayList<>();
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
        String[] search = giftCertificateParameters.getSearch();
        String tagName = giftCertificateParameters.getTagName();
        if ((search == null || search.length == 0)
                && (tagName == null || tagName.isBlank())) {
            return "";
        }
        if (search == null || search.length == 0) {
            return "where " + getTagNameCondition(tagName);
        }
        if (tagName == null || tagName.isBlank()){
            return "where " + getSearchCondition(search);
        }
        return String.format("where %s and %s", getSearchCondition(search), getTagNameCondition(tagName));
    }

    @Override
    public String orderClause() {
        String[] sort = giftCertificateParameters.getSort();
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

    private String getTagNameCondition(String tagName) {
        return String.format("""
                    gc.gift_certificate_id in (select gct.gift_certificate_id
                    from gift_certificate_tags as gct
                    inner join tags as t on t.tag_id = gct.tag_id
                    where t.tag_name like '%%%s%%')
                    """, tagName);
    }

    private String getSearchCondition(String[] search) {
        return Arrays.stream(search).map(s -> {
            String[] parts = s.split(",");
            if (parts[0].equals("name")) {
                return "gift_certificate_name like '%" + parts[1] + "%'";
            }
            return "gift_certificate_description like '%" + parts[1] + "%'";
        }).collect(Collectors.joining(" and "));
    }
}