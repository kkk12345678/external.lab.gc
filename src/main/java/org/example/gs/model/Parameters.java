package org.example.gs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Parameters {

    private Pagination pagination;
    private Sort sort;
    private Search search;




    public String getClause() {
        /* where gift_certificate_name like '%gs_name%'
         * and gift_certificate_description like '%description%'
         * and tag_name = 'tagName'
         * order by gift_certificate_name asc
         * limit 1 offset 1;
         */
        String whereClause = search == null ? "" : search.getClause();
        String sortClause = sort == null ? "" : sort.getClause();
        String paginationClause = pagination == null ? "" : pagination.getClause();
        return whereClause + sortClause + paginationClause;
    }

    @AllArgsConstructor
    @Data
    public static class Pagination {
        private int limit;
        private int offset;

        public String getClause() {
            if (limit <= 0 || offset < 0) {
                return "";
            }
            return String.format(" limit %d offset %d", limit, offset);
        }
    }

    @AllArgsConstructor
    @Data
    public static class Sort {
        private String[] fields;
        private String[] sort;

        public String getClause() {
            if (fields == null || fields.length == 0 || sort == null || sort.length == 0) {
                return "";
            }
            assert(fields.length == sort.length);
            return " order by " + IntStream.range(0, fields.length)
                    .mapToObj(i -> String.format("gift_certificates.%s %s", fields[i], sort[i]))
                    .collect(Collectors.joining(", "));
        }
    }

    @AllArgsConstructor
    @Data
    public static class Search {
        private String[] fields;
        private String[] search;

        public String getClause() {
            if (fields == null || fields.length == 0 || search == null || search.length == 0) {
                return "";
            }
            assert(fields.length == search.length);
            return " where " + IntStream.range(0, fields.length)
                    .mapToObj(i -> String.format("%s like '%%%s%%'", fields[i], search[i]))
                    .collect(Collectors.joining(" and "));
        }
    }
}
