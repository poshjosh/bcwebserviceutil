package com.bc.service.util;

import java.text.MessageFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Mirrors {@link io.github.jhipster.web.util.PaginationUtil}. Unlike that class
 * adds, a self link.
 * @author chinomso ikwuagwu
 * @see io.github.jhipster.web.util.PaginationUtil
 */
public final class PaginationUtil {

    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
    private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

    private PaginationUtil() { }

    /**
     * Generate pagination headers for a Spring Data {@link org.springframework.data.domain.Page} object.
     *
     * @param uriBuilder The URI builder.
     * @param <T> The type of object.
     * @param pageNumber The page number
     * @param pageSize The page size
     * @param totalPages The total number of pages
     * @param totalElements The total number of elements in all the pages of the result.
     * @return http header.
     */
    public static <T> MultiValueMap<String, String> generatePaginationHttpHeaders(
            UriComponentsBuilder uriBuilder, 
            int pageNumber, 
            int pageSize, 
            int totalPages, 
            long totalElements) {
        
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HEADER_X_TOTAL_COUNT, Long.toString(totalElements));
        
        StringBuilder link = new StringBuilder(100 * 5);

        appendSelfLink(uriBuilder, pageNumber, pageSize, link);

        appendPaginationHttpHeadersLink(uriBuilder, pageNumber, pageSize, totalPages, link);
        
        headers.add("Link", link.toString());
        
        return headers;
    } 

    public static void appendSelfLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, StringBuilder link) {
        link.append(prepareLink(uriBuilder, pageNumber, pageSize, "self"))
            .append(",");
    } 
    
    public static void appendPaginationHttpHeadersLink(
            UriComponentsBuilder uriBuilder, 
            int pageNumber, 
            int pageSize, 
            int totalPages,
            StringBuilder link) {
        if (pageNumber < totalPages - 1) {
            link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next"))
                .append(",");
        }
        if (pageNumber > 0) {
            link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev"))
                .append(",");
        }
        link.append(prepareLink(uriBuilder, totalPages - 1, pageSize, "last"))
            .append(",")
            .append(prepareLink(uriBuilder, 0, pageSize, "first"));
    }

    private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return MessageFormat.format(HEADER_LINK_FORMAT, preparePageUri(uriBuilder, pageNumber, pageSize), relType);
    }

    private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
        return uriBuilder.replaceQueryParam("page", Integer.toString(pageNumber))
            .replaceQueryParam("size", Integer.toString(pageSize))
            .toUriString()
            .replace(",", "%2C")
            .replace(";", "%3B");
    }
}
