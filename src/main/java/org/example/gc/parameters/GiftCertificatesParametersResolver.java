package org.example.gc.parameters;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

public class GiftCertificatesParametersResolver
        implements HandlerMethodArgumentResolver, PaginationResolver {
    private static final String DELIMITER = ",";

    private static final String PARAM_SORT = "sort";
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_TAG_NAME = "tagName";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_DATE = "date";
    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";

    private static final String ERROR_SORT_PATTERN =
            "'sort' parameter '%s' does not match the pattern.";
    private static final String ERROR_SORT_CONTRADICTORY =
            "'sort' parameter contains contradictory sort order.";
    private static final String ERROR_SORT_OVER =
            "'sort' parameter has more values than allowed.";
    private static final String ERROR_SEARCH_OVER =
            "'search' parameter has more values than allowed.";
    private static final String ERROR_SEARCH_PATTERN =
            "'search' parameter '%s' does not match the pattern";

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
        setPagination(giftCertificateParameters,
                webRequest.getParameter(PARAM_LIMIT),
                webRequest.getParameter(PARAM_PAGE));
        setSort(giftCertificateParameters, webRequest.getParameterValues(PARAM_SORT));
        setSearch(giftCertificateParameters, webRequest.getParameterValues(PARAM_SEARCH));
        giftCertificateParameters.setTagNames(webRequest.getParameterValues(PARAM_TAG_NAME));
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

    private static void setSearch(GiftCertificateParameters giftCertificateParameters, String[] search) {
        if (search != null) {
            if (search.length > 2) {
                throw new IllegalArgumentException(ERROR_SEARCH_OVER);
            }
            Arrays.stream(search).forEach(s -> {
                String[] parts = s.split(DELIMITER);
                if (parts.length != 2 ||
                        !(parts[0].equals(FIELD_NAME) || parts[0].equals(FIELD_DESCRIPTION))) {
                    throw new IllegalArgumentException(
                            String.format(ERROR_SEARCH_PATTERN, s));
                }
            });
        }
        giftCertificateParameters.setSearch(search);
    }
}