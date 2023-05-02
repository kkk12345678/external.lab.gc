package org.example.gs.model;

import lombok.Setter;

@Setter
public class Parameters {
    private String[] sort;
    private String[] search;
    private Integer limit;
    private Integer page;
    private String tagName;

    public String clause() {
        /* where gift_certificate_name like '%gs_name%'
         * and gift_certificate_description like '%description%'
         * and tag_name = 'tagName'
         * order by gift_certificate_name asc
         * limit 1 offset 1;
         */
        return "";
    }
}
