/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.GlobalPropertyListener;
import org.openmrs.api.context.Context;
import org.openmrs.order.RegimenSuggestion;

public class Format implements GlobalPropertyListener {
	
	private static Log log = LogFactory.getLog(Format.class);
	
	private static String defaultDateTimeCache = null;
	
	private static String defaultDateCache = null;
	
	private static String defaultTimeCache = null;
	
	public enum FORMAT_TYPE {
		DATE, TIME, TIMESTAMP
	};
	
	public static String formatPercentage(double pct) {
		return NumberFormat.getPercentInstance().format(pct);
	}
	
	public static String formatPercentage(Number pct) {
		if (pct == null) {
			return "";
		} else {
			return NumberFormat.getPercentInstance().format(pct.doubleValue());
		}
	}
	
	public static String format(double d) {
		return "" + ((d == (int) d) ? (int) d : d);
	}
	
	public static String format(Double d) {
		return d == null ? "" : format(d.doubleValue());
	}
	
	public static String formatTextBoxDate(Date date) {
		return format(date, Context.getLocale(), FORMAT_TYPE.DATE);
	}
	
	public static String format(Date date) {
		return format(date, Context.getLocale(), FORMAT_TYPE.DATE);
	}
	
	public static String format(Date date, FORMAT_TYPE type) {
		return format(date, Context.getLocale(), type);
	}
	
	public static String format(Date date, Locale locale, FORMAT_TYPE type) {
		log.debug("Formatting date: " + date + " with locale " + locale);
		
		DateFormat dateFormat = null;
		
		if (type == FORMAT_TYPE.TIMESTAMP) {
			String dateTimeFormat = Context.getAdministrationService().getGlobalPropertyValue(
			    OpenmrsConstants.GLOBAL_PROPERTY_DATEANDTIME_DISPLAY_FORMAT, null);
			if (StringUtils.isEmpty(dateTimeFormat))
				dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			else
				dateFormat = new OpenmrsDateFormat(new SimpleDateFormat(dateTimeFormat), locale);
		} else if (type == FORMAT_TYPE.TIME) {
			String timeFormat = Context.getAdministrationService().getGlobalPropertyValue(
			    OpenmrsConstants.GLOBAL_PROPERTY_TIME_DISPLAY_FORMAT, null);
			if (StringUtils.isEmpty(timeFormat))
				dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
			else
				dateFormat = new OpenmrsDateFormat(new SimpleDateFormat(timeFormat), locale);
		} else if (type == FORMAT_TYPE.DATE) {
			String formatValue = Context.getAdministrationService().getGlobalPropertyValue(
			    OpenmrsConstants.GLOBAL_POPERTY_DATE_DISPLAY_FORMAT, "");
			if (StringUtils.isEmpty(formatValue))
				dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
			else
				dateFormat = new OpenmrsDateFormat(new SimpleDateFormat(formatValue), locale);
		}
		
		return date == null ? "" : dateFormat.format(date);
	}
	
	public static String format(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return t + "\n" + sw.toString();
	}
	
	@Override
	public boolean supportsPropertyName(String propertyName) {
		return OpenmrsConstants.GLOBAL_PROPERTY_DATEANDTIME_DISPLAY_FORMAT.equals(propertyName)
		        || OpenmrsConstants.GLOBAL_POPERTY_DATE_DISPLAY_FORMAT.equals(propertyName)
		        || OpenmrsConstants.GLOBAL_PROPERTY_TIME_DISPLAY_FORMAT.equals(propertyName);
	}
	
	@Override
	public void globalPropertyChanged(GlobalProperty newValue) {
		// reset the value
		defaultDateTimeCache = null;
		defaultDateCache = null;
		defaultTimeCache = null;
	}
	
	@Override
	public void globalPropertyDeleted(String propertyName) {
		defaultDateTimeCache = null;
		defaultDateCache = null;
		defaultTimeCache = null;
	}
	
}
