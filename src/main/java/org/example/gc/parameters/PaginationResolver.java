package org.example.gc.parameters;

public interface PaginationResolver {
    String PARAM_LIMIT = "limit";
    String PARAM_PAGE = "page";
    String ERROR_LIMIT_NOT_SPECIFIED =
            "'limit' parameter is not specified, while 'page' is specified.";
    String ERROR_LIMIT_NOT_POSITIVE =
            "'limit' parameter must be positive.";
    String ERROR_LIMIT_NOT_INTEGER =
            "'limit' parameter must be integer.";
    String ERROR_PAGE_NOT_POSITIVE =
            "'page' parameter must be positive.";
    String ERROR_PAGE_NOT_INTEGER =
            "'page' parameter must be integer.";

    default void setPagination(Parameters parameters, String limit, String page) {
        if ((limit == null || limit.isBlank()) && (page == null || page.isBlank())) {
            parameters.setLimit(null);
            parameters.setPage(null);
        } else if (limit == null || limit.isBlank()) {
            throw new IllegalArgumentException(ERROR_LIMIT_NOT_SPECIFIED);
        } else {
            try {
                int l = Integer.parseInt(limit);
                if (l < 1) {
                    throw new IllegalArgumentException(ERROR_LIMIT_NOT_POSITIVE);
                }
                parameters.setLimit(l);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERROR_LIMIT_NOT_INTEGER);
            }
            if (page == null || page.isBlank()) {
                parameters.setPage(null);
            } else {
                try {
                    int p = Integer.parseInt(page);
                    if (p < 1) {
                        throw new IllegalArgumentException(ERROR_PAGE_NOT_POSITIVE);
                    }
                    parameters.setPage(p);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(ERROR_PAGE_NOT_INTEGER);
                }
            }
        }
    }
}
