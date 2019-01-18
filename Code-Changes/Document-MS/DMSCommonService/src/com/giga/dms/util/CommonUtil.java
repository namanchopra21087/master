package com.giga.dms.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.giga.dms.exception.ServiceException;
import com.giga.dms.vo.DocumentInfoVO;

public class CommonUtil {
	
	/**
	 * check whether the given string is null or empty
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().length() == 0 || str.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrEmpty(Date date) {
		if (date == null) {
			return true;
		}
		return false;
	}
	/*
	public static DocumentInfoVO setHashMap(DocumentInfoVO dVO)
	{
		MyHashMap myHashMap=dVO.getMetadata();
		HashMap<String, String> hashMap=new HashMap<String, String>();
		List<String> keys=myHashMap.getKeys();
		List<String> values=myHashMap.getValues();
		for(int i=0,j=0;i<keys.size() && j<values.size();i++,j++)
		{
			hashMap.put(keys.get(i) , values.get(j));
		}
		dVO.setMapProperties(hashMap);
		return dVO;
	}
	
	public static DocumentInfoVO setMyHashMap(DocumentInfoVO dVO) throws ServiceException
	{
		if(dVO.getMapProperties()!=null)
		{
			MyHashMap myHashMap=new MyHashMap();
			HashMap<String, String> hashMap=new HashMap<String, String>();
			hashMap=dVO.getMapProperties();
			Iterator it=hashMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value=hashMap.get(key);
				if(key.equalsIgnoreCase(ServiceConstants.DATECREATED))
				{
					value=P8Util.convertToSimpleDateFormat(value);
				}
				myHashMap.put(key, value);
			}
			dVO.setMetadata(myHashMap);
		}
		return dVO;
	}*/
}
