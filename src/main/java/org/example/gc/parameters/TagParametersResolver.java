package org.example.gc.parameters;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TagParametersResolver
        implements HandlerMethodArgumentResolver, PaginationResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getNestedParameterType().equals(TagParameters.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory)
            throws Exception {

        TagParameters tagParameters = new TagParameters();
        String limit = webRequest.getParameter(PARAM_LIMIT);
        String page = webRequest.getParameter(PARAM_PAGE);
        setPagination(tagParameters, limit, page);
        return tagParameters;
    }
}
