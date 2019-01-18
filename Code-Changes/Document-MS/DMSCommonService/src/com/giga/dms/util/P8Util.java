package com.giga.dms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.giga.dms.exception.ServiceException;

public class P8Util implements ServiceConstants {

	private static final Logger LOG = LoggerFactory.getLogger(P8Util.class);
	/**
	 * This method is used to convert the input date string format from client
	 * application to FileNet based date string format.
	 * @throws ParseException 
	 * 
	 * 
	 */
	public static String convertToFileNetDateFormat(String stringDate)
			throws ServiceException  {
		String fileNetDate = null;
		try{
		String tempDate = stringDate+EMPTYSTRING+TIME;
		DateFormat df = new SimpleDateFormat(CLIENTDATEFORMAT);
		Date dateFormat = df.parse(tempDate);
		DateFormat fileNetFormatter = new SimpleDateFormat(FILENETDATEFORMAT,
				Locale.ENGLISH);
		fileNetDate = fileNetFormatter.format(dateFormat);
		}catch (ParseException e) {
			LOG.error("Date Parse Exception::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		return fileNetDate;
	}

	/**
	 * Adding one day extra for to date search This method is used to convert
	 * the input date string format from client application to FileNet based
	 * date string format.
	 * 
	 * 
	 */
	public static String addOnedayPlusToFileNetDateFormat(String stringDate)
			throws ServiceException {
		String fileNetDate = null;
		try{
		String tempDate = stringDate+EMPTYSTRING+TIME;
		DateFormat df = new SimpleDateFormat(CLIENTDATEFORMAT);
		Date dateFormat = df.parse(tempDate);
		Date tempDateAdd = new Date(dateFormat.getTime() + (1000 * 60 * 60 * 24));
		DateFormat fileNetFormatter = new SimpleDateFormat(FILENETDATEFORMAT,
				Locale.ENGLISH);
		fileNetDate = fileNetFormatter.format(tempDateAdd);
		}catch (ParseException e) {
			LOG.error("Date Parse Exception::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		return fileNetDate;
	}

	
	/**
	 * This method is used to convert the FileNet Date format to String
	 * 
	 * 
	 */	
	public static String convertToSimpleDateFormat(String date) throws ServiceException
	{
		SimpleDateFormat  oldDateFormat = new SimpleDateFormat(FILENET_DATE_RECEIVED);
		SimpleDateFormat  newDateFormat=new SimpleDateFormat(CLIENTDATEFORMAT);
		String newDateString =null;
		try {
			Date oldDate = oldDateFormat.parse(date);
			newDateString = newDateFormat.format(oldDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("Date Parsing Exception::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		return newDateString;
	}
	
	
	/**
	 * This method is used to convert the FileNet Date format to String
	 * 
	 * 
	 */	
	public static String convertToSimpleDateFormatforDisplay(String date) throws ServiceException
	{
		SimpleDateFormat  oldDateFormat = new SimpleDateFormat(FILENET_DATE_RECEIVED);
		SimpleDateFormat  newDateFormat=new SimpleDateFormat(CLIENT_DISPLAY_DATEFORMAT);
		String newDateString =null;
		try {
			Date oldDate = oldDateFormat.parse(date);
			newDateString = newDateFormat.format(oldDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("Date Parse Exception::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		return newDateString;
	}
	
	
	/**
	 * This method is used to convert the input date string format to Date
	 * 
	 *  @throws ParseException 
	 * 
	 * 
	 */
	public static Date convertToDateFormat(String stringDate)
			throws ServiceException  {
		Date dateFormat = null; 
		try{
		String tempDate = stringDate+EMPTYSTRING+TIME;
		DateFormat df = new SimpleDateFormat(CLIENTDATEFORMAT);
		df.setLenient(false);
	    dateFormat = df.parse(tempDate);
		}catch (ParseException e) {
			LOG.error("Date Parse Exception::"+e);
			throw new ServiceException(DATE_ERROR,DATE_ERROR_MSG);
		}
		return dateFormat;
	}
}
