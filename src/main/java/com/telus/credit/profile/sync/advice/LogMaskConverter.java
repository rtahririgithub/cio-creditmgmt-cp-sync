package com.telus.credit.profile.sync.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

public class LogMaskConverter<E extends ILoggingEvent> extends CompositeConverter<E> {

	private Map<Pattern, String> patternMap = new HashMap<>();

	private static final String MASK_4_CHARS = "****";
	private static final String MASK_6_CHARS = "******";
	private static final String BASE64_PDF_REMOVED = "XXXXXXXXXXXXXXXXXXXX Base64 PDF XXXXXXXXXXXXXXXXXXXX";

	private static final int maxLogMessageLen = 8000;

	@Override
	public void start() {
		patternMap.put(Pattern.compile("(postalCode\\\" {0,1}: {0,1}\\\")[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[- ]{0,1}[0-9]{1}[a-zA-Z]{1}[0-9]{1}"),"$1" + MASK_6_CHARS);
		patternMap.put(Pattern.compile("(birthDate\\\" {0,1}: {0,1}\\\")[0-9]{4}(-[0-9]{2}-[0-9]{2})"),"$1" + MASK_4_CHARS + "$2");
		//patternMap.put(Pattern.compile("(clientBirthDate\\\" {0,1}: {0,1}\\\")[0-9]{4}(-[0-9]{2}-[0-9]{2})"),"$1" + MASK_4_CHARS + "$2");
		patternMap.put(Pattern.compile("(accountNumber\\\" {0,1}: {0,1}\\\")[0-9]{3,4}([0-9]{4})"),"$1" + MASK_4_CHARS + "$2");
		//patternMap.put(Pattern.compile("(AccountNum\\\" {0,1}: {0,1}\\\")[0-9]{3,4}([0-9]{4})"),"$1" + MASK_4_CHARS + "$2");
		//patternMap.put(Pattern.compile("(employeeId\\\" {0,1}: {0,1}\\\")([0-9]{5})[0-9]{4}"), "$1" + "$2" + MASK_4_CHARS);
		patternMap.put(Pattern.compile("(pdf\\\" {0,1}: {0,1}\\\")(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)"), "$1" + BASE64_PDF_REMOVED);
		patternMap.put(Pattern.compile("(file\\\" {0,1}: {0,1}\\\")(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)"), "$1" + BASE64_PDF_REMOVED);
		//patternMap.put(Pattern.compile("(dataFile\\\" {0,1}: {0,1}\\\")(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)"), "$1" + BASE64_PDF_REMOVED);
		super.start();
	}

	@Override
	protected String transform(E event, String in) {
		if (!started) {
			return in;
		}
		String message = event.getFormattedMessage();
		Set<Pattern> patternSet = patternMap.keySet();

		if (message != null && !message.equals("")) {
			for (Pattern pattern : patternSet) {
				message = pattern.matcher(message).replaceAll(patternMap.get(pattern));
			}
		}

		// Truncate message if it's over X chars (to avoid flooding the log file)
		if (message != null && message.length() > maxLogMessageLen) {
			message = message.substring(0, maxLogMessageLen) + "...";
		}
		return message;
	}
}
