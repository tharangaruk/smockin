package com.smockin.utils;

import com.smockin.admin.dto.RestfulMockDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.utils.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mgallina.
 */
public final class GeneralUtils {

    private static final Logger logger = LoggerFactory.getLogger(GeneralUtils.class);

    public static final String ISO_DATE_FORMAT = "yyyy/MM/dd";
    public static final String ISO_DATETIME_FORMAT = "yyyy/MM/dd'T'HH:mm:ssZ";

    // Looks for values within the brace format ${}. So ${bob} would return the value 'bob'.
    static final String INBOUND_TOKEN_PATTERN = "\\$\\{(.*?)\\}";

    public final static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // Should be set to UTC from command line
    public final static Date getCurrentDate() {
        return new Date();
    }

    // NOTE It is important that this preserves any whitespaces around the token
    public static String findFirstInboundParamMatch(final String input) {

        if (input == null) {
            return null;
        }

        final Pattern pattern = Pattern.compile(INBOUND_TOKEN_PATTERN);
        final Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     *
     * Returns the header value for the given name.
     * Look up is case insensitive (as Java Spark handles header look ups with case sensitivity, which is wrong)
     *
     * @param request
     * @param headerName
     * @returns String
     *
     */
    public static String findHeaderIgnoreCase(final Request request, final String headerName) {

        for (String h : request.headers()) {
            if (h.equalsIgnoreCase(headerName)) {
                return request.headers(h);
            }
        }

        return null;
    }

    /**
     *
     * Returns the request parameter value for the given name.
     * Look up is case insensitive (as Java Spark handles request parameter look ups with case sensitivity. Unclear on what the standard is for this...)
     *
     * @param request
     * @param requestParamName
     * @returns String
     *
     */
    public static String findRequestParamIgnoreCase(final Request request, final String requestParamName) {

        for (String q : request.queryParams()) {
            if (q.equalsIgnoreCase(requestParamName)) {
                return request.queryParams(q);
            }
        }

        return null;
    }

    /**
     *
     * Returns the request parameter value for the given name.
     * Look up is case insensitive (as Java Spark handles request parameter look ups with case sensitivity. Unclear on what the standard is for this...)
     *
     * @param request
     * @param pathVarName
     * @returns String
     *
     */
    public static String findPathVarIgnoreCase(final Request request, final String pathVarName) {

        for (Map.Entry<String, String> pv : request.params().entrySet()) {
            if (pv.getKey().equalsIgnoreCase((pathVarName.startsWith(":"))?pathVarName:(":"+pathVarName))) {
                return pv.getValue();
            }
        }

        return null;
    }

    public static void checkForAndHandleSleep(final long sleepInMillis) {

        if (sleepInMillis > 0) {
            try {
                Thread.sleep(sleepInMillis);
            } catch (InterruptedException ex) {
                logger.error("Error pausing response for the specified period of " + sleepInMillis, ex);
            }
        }
    }

    public static String prefixPath(final String path) {

        if (StringUtils.isBlank(path)) {
            return null;
        }

        final String prefix = "/";

        if (!path.startsWith(prefix)) {
            return prefix + path;
        }

        return path;
    }

}
