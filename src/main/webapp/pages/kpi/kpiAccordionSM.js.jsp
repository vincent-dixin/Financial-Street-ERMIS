<%@ page language="java" contentType="text/javascript; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
Ext.define('pages.kpi.kpiAccordionSM',{
	extend : 'Ext.tree.Panel',
	alias : 'widget.kpiaccordionsm',
	
    border:true,
    title: '战略地图',
    iconCls:'icon-ibm-icon-reports',
    autoScroll: true,
    root : {
    	text : '战略地图',
        iconCls : 'icon-ibm-icon-report',
        expanded : true,
        children : [{
        	text: '第一会达',
        	expanded : true,
        	iconCls : 'icon-ibm-icon-report',
        	leaf : true
        }]
    }   
});