package com.telus.credit.profile.sync.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class VersionUtils {

	static Properties gitProp ;
	
    private VersionUtils() {
        // Utils
    }

    public static Timestamp toUtcTimestamp(String isoDatetime) {
        if (StringUtils.isBlank(isoDatetime)) {
            return null;
        }
        Instant instant = Instant.parse(isoDatetime);
        return new Timestamp(instant.minusSeconds(ZoneId.systemDefault().getRules().getOffset(instant).getTotalSeconds()).toEpochMilli());
    }

    public static  String getBuildDetails() {
    	String buildDetails="[";
    	
    	try {
    		if(gitProp==null) {
    			gitProp = new Properties();
    		}    		
 			gitProp.load(VersionUtils.class.getClassLoader().getResourceAsStream("git.properties"));
			buildDetails = buildDetails + "[git.properties=" + gitProp +"]";
		} catch (Throwable e) {
		}
    	
    	buildDetails = buildDetails + "]";
		return buildDetails;
	}
}
