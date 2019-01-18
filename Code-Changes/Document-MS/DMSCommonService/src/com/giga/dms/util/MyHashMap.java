package com.giga.dms.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Metadata")
public class MyHashMap {
	
	@XmlElement(name="key")
	public List<String> key;
	
	@XmlElement(name="value")
	public List<String> value;
	
	public MyHashMap() {
		// TODO Auto-generated constructor stub
		this.key=new ArrayList<String>();
		this.value=new ArrayList<String>();
	}
	
	public void put(String k,String v)
	{
		this.key.add(k);
		this.value.add(v);
	}
	
	public String get(String v)
	{
		int i=-1;
		Iterator it=key.iterator();
		for(int j=0;it.hasNext();j++) {
			String object = (String) it.next();
			if(object.equals(v))
			{
				i=j;
			}
		}
		if(i==-1)
		{
			return null;
		}
		return this.value.get(i);
	}
	
	public List<String> getKeys()
	{
		return this.key;
	}
	
	public List<String> getValues()
	{
		return this.value;
	}
	
	public Set<MyHashMap.Entry> entrySet()
	{
		Set<MyHashMap.Entry> s=new HashSet<MyHashMap.Entry>();
		if(this.key!=null && this.value!=null)
		{
			for(int i=0;i<this.key.size() && i<this.value.size(); i++)
			{
				MyHashMap.Entry e=new MyHashMap.Entry();
				e.setKey(key.get(i));
				e.setValue(value.get(i));
				s.add(e);
			}
		}
		return s;
	}
	
	public boolean containsKey(String key_str)
	{
		if(key_str!=null)
		{
			for(int i=0;i<this.key.size();i++)
			{
				if(key_str.equals(this.key.get(i)))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public class Entry
	{
		String key;
		String value;
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
