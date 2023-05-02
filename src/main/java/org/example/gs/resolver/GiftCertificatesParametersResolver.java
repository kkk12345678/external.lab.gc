package org.example.gs.resolver;

import org.example.gs.model.GiftCertificateParameters;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

public class GiftCertificatesParametersResolver
        implements HandlerMethodArgumentResolver {
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
        String limit = webRequest.getParameter("limit");
        String page = webRequest.getParameter("page");
        String[] sort = webRequest.getParameterValues("sort");
        String[] search = webRequest.getParameterValues("search");
        setPagination(giftCertificateParameters, limit, page);
        setSort(giftCertificateParameters, sort);
        setSearch(giftCertificateParameters, search);
        giftCertificateParameters.setTagName(webRequest.getParameter("tagName"));
        return giftCertificateParameters;
    }

    private static void setSort(GiftCertificateParameters giftCertificateParameters, String[] sort) {
        if (sort != null) {
            if (sort.length > 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "'sort' parameter has more values than allowed.");
            }
            Arrays.stream(sort).forEach(s -> {
                String[] parts = s.split(",");
                if (parts.length != 2 ||
                        !(parts[0].equals("name") || parts[0].equals("date")) ||
                        !(parts[1].equals("asc") || parts[1].equals("desc"))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            String.format("'sort' parameter '%s' does not match the pattern", s));
                }
            });
            if (sort.length == 2 &&
                    sort[0].split(",")[0].equals(sort[1].split(",")[0]) &&
                    !sort[0].split(",")[1].equals(sort[1].split(",")[1])) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "'sort' parameter contains contradictory sort order.");
            }
        }
        giftCertificateParameters.setSort(sort);
    }

    private static void setPagination(GiftCertificateParameters giftCertificateParameters, String limit, String page) {
        if (limit == null) {
            giftCertificateParameters.setLimit(null);
            giftCertificateParameters.setPage(null);
        } else {
            try {
                int l = Integer.parseInt(limit);
                if (l < 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "'limit' parameter must be positive.");
                }
                giftCertificateParameters.setLimit(l);
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "'limit' parameter must be integer.");
            }
            if (page == null) {
                giftCertificateParameters.setPage(null);
            } else {
                try {
                    int p = Integer.parseInt(page);
                    if (p < 1) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "'page' parameter must be positive.");
                    }
                    giftCertificateParameters.setPage(p);
                } catch (NumberFormatException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "'page' parameter must be integer.");
                }
            }
        }
    }

    private static void setSearch(GiftCertificateParameters giftCertificateParameters, String[] search) {
        if (search != null) {
            if (search.length > 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "'search' parameter has more values than allowed.");
            }
            Arrays.stream(search).forEach(s -> {
                String[] parts = s.split(",");
                if (parts.length != 2 ||
                        !(parts[0].equals("name") || parts[0].equals("description"))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            String.format("'search' parameter '%s' does not match the pattern", s));
                }
            });
        }
        giftCertificateParameters.setSearch(search);
    }
}