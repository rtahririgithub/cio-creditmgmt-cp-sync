package com.telus.credit.profile.sync.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;

public class DateTimeUtils {

    private DateTimeUtils() {
        // Utils
    }

    public static Timestamp toUtcTimestamp(String isoDatetime) {
        if (StringUtils.isBlank(isoDatetime)) {
            return null;
        }
        Instant instant = Instant.parse(isoDatetime);
        return new Timestamp(instant.minusSeconds(ZoneId.systemDefault().getRules().getOffset(instant).getTotalSeconds()).toEpochMilli());
    }

}
