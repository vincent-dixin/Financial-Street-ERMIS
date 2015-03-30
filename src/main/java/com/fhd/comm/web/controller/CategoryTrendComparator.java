package com.fhd.comm.web.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public class CategoryTrendComparator implements Comparator<Map<String, Object>> {
	private String sortColumn;
	private String dir;

	public CategoryTrendComparator(String sortColumn, String dir) {
		this.dir = dir;
		this.sortColumn = sortColumn;
	}

	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		int v = 0;
		if("name".equals(sortColumn)){
			if("DESC".equals(dir)){
				v= ((String)o1.get("name")).compareTo((String)o2.get("name"));
			}else{
				v= ((String)o2.get("name")).compareTo((String)o1.get("name"));
			}
		}else if("assessmentStatus".equals(sortColumn)){
			if("DESC".equals(dir)){
				v= ((String)o1.get("assessmentStatus")).compareTo((String)o2.get("assessmentStatus"));
			}else{
				v= ((String)o2.get("assessmentStatus")).compareTo((String)o1.get("assessmentStatus"));
			}
		}else if("directionstr".equals(sortColumn)){
			if("DESC".equals(dir)){
				v= ((String)o1.get("directionstr")).compareTo((String)o2.get("directionstr"));
			}else{
				v= ((String)o2.get("directionstr")).compareTo((String)o1.get("directionstr"));
			}
		}else if("januaryValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("januaryValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("januaryValue");
			}
			if(o2.get("januaryValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("januaryValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("februaryValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("februaryValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("februaryValue");
			}
			if(o2.get("februaryValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("februaryValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("marchValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("marchValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("marchValue");
			}
			if(o2.get("marchValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("marchValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("aprilValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("aprilValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("aprilValue");
			}
			if(o2.get("aprilValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("aprilValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("mayValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("mayValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("mayValue");
			}
			if(o2.get("mayValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("mayValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("juneValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("juneValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("juneValue");
			}
			if(o2.get("juneValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("juneValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("julyValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("julyValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("julyValue");
			}
			if(o2.get("julyValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("julyValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("aguestValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("aguestValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("aguestValue");
			}
			if(o2.get("aguestValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("aguestValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("septemberValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("septemberValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("septemberValue");
			}
			if(o2.get("septemberValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("septemberValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("octoberValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("octoberValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("octoberValue");
			}
			if(o2.get("octoberValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("octoberValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("novemberValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("novemberValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("novemberValue");
			}
			if(o2.get("novemberValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("novemberValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}else if("decemberValue".equals(sortColumn)){
			BigDecimal b1 = BigDecimal.valueOf(0);
			BigDecimal b2 = BigDecimal.valueOf(0);
			if(o1.get("decemberValue") instanceof BigDecimal){
				b1 = (BigDecimal)o1.get("decemberValue");
			}
			if(o2.get("decemberValue") instanceof BigDecimal){
				b2 = (BigDecimal)o2.get("decemberValue");
			}
			if("DESC".equals(dir)){
				v= (b2).compareTo(b1);
			}else{
				v= (b1).compareTo(b2);
			}
		}
		return v;
	}

}
