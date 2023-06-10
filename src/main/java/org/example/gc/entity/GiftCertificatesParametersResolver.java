package org.example.gc.entity;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

public class GiftCertificatesParametersResolver
        implements HandlerMethodArgumentResolver {
    private static final String DELIMITER = ",";
    private static final String PARAM_LIMIT = "limit";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SORT = "sort";
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_TAG_NAME = "tagName";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_DATE = "date";
    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";

    private static final String ERROR_SORT_PATTERN =
            "'sort' parameter '%s' does not match the pattern.";
    private static final String ERROR_SORT_CONTRADICTORY =
            "'sort' parameter contains contradictory sort order.";
    private static final String ERROR_SORT_OVER =
            "'sort' parameter has more values than allowed.";
    private static final String ERROR_LIMIT_NOT_SPECIFIED =
            "'limit' parameter is not specified, while 'page' is specified.";
    private static final String ERROR_LIMIT_NOT_POSITIVE =
            "'limit' parameter must be positive.";
    private static final String ERROR_LIMIT_NOT_INTEGER =
            "'limit' parameter must be integer.";
    private static final String ERROR_PAGE_NOT_POSITIVE =
            "'page' parameter must be positive.";
    private static final String ERROR_PAGE_NOT_INTEGER =
            "'page' parameter must be integer.";
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getNestedParameterType().equals(GiftCertificateParameters.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        GiftCertificateParameters giftCertificateParameters = new GiftCertificateParameters();
        String limit = webRequest.getParameter(PARAM_LIMIT);
        String page = webRequest.getParameter(PARAM_PAGE);
        String[] sort = webRequest.getParameterValues(PARAM_SORT);
        String[] search = webRequest.getParameterValues(PARAM_SEARCH);
        setPagination(giftCertificateParameters, limit, page);
        setSort(giftCertificateParameters, sort);
        setSearch(giftCertificateParameters, search);
        giftCertificateParameters.setTagName(webRequest.getParameter(PARAM_TAG_NAME));
        return giftCertificateParameters;
    }

    private static void setSort(GiftCertificateParameters giftCertificateParameters, String[] sort) {
        if (sort != null) {
            if (sort.length > 2) {
                throw new IllegalArgumentException(ERROR_SORT_OVER);
            }
            Arrays.stream(sort).forEach(s -> {
                String[] parts = s.split(DELIMITER);
                if (parts.length != 2 ||
                        !(parts[0].equals(FIELD_NAME) || parts[0].equals(FIELD_DATE)) ||
                        !(parts[1].equals(SORT_ASC) || parts[1].equals(SORT_DESC))) {
                    throw new IllegalArgumentException(
                            String.format(ERROR_SORT_PATTERN, s));
                }
            });
            if (sort.length == 2 &&
                    sort[0].split(DELIMITER)[0].equals(sort[1].split(DELIMITER)[0]) &&
                    !sort[0].split(DELIMITER)[1].equals(sort[1].split(DELIMITER)[1])) {
                throw new IllegalArgumentException(ERROR_SORT_CONTRADICTORY);
            }
        }
        giftCertificateParameters.setSort(sort);
    }

    private static void setPagination(GiftCertificateParameters giftCertificateParameters, String limit, String page) {
        if ((limit == null || limit.isBlank()) && (page == null || page.isBlank())) {
            giftCertificateParameters.setLimit(null);
            giftCertificateParameters.setPage(null);
        } else if (limit == null || limit.isBlank()) {
            throw new IllegalArgumentException(ERROR_LIMIT_NOT_SPECIFIED);
        } else {
            try {
                int l = Integer.parseInt(limit);
                if (l < 1) {
                    throw new IllegalArgumentException(ERROR_LIMIT_NOT_POSITIVE);
                }
                giftCertificateParameters.setLimit(l);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERROR_LIMIT_NOT_INTEGER);
            }
            if (page == null || page.isBlank()) {
                giftCertificateParameters.setPage(null);
            } else {
                try {
                    int p = Integer.parseInt(page);
                    if (p < 1) {
                        throw new IllegalArgumentException(ERROR_PAGE_NOT_POSITIVE);
                    }
                    giftCertificateParameters.setPage(p);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(ERROR_PAGE_NOT_INTEGER);
                }
            }
        }
    }

    private static void setSearch(GiftCertificateParameters giftCertificateParameters, String[] search) {
        if (search != null) {
            if (search.length > 2) {
                throw new IllegalArgumentException("'search' parameter has more values than allowed.");
            }
            Arrays.stream(search).forEach(s -> {
                String[] parts = s.split(",");
                if (parts.length != 2 ||
                        !(parts[0].equals("name") || parts[0].equals("description"))) {
                    throw new IllegalArgumentException(
                            String.format("'search' parameter '%s' does not match the pattern", s));
                }
            });
        }
        giftCertificateParameters.setSearch(search);
    }
}