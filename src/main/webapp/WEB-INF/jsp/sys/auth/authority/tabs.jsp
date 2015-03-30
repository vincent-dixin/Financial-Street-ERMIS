<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.authority.authority"/><spring:message code="fhd.common.tabs"/></title>
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
</head>
<body>
	
	
	
	<script type="text/javascript">
	    var height=Ext.getBody().getHeight();
	    var width=Ext.getBody().getWidth();
		Ext.onReady(function(){
			 var tabs = new Ext.TabPanel({
				    id:'tabs',
			        renderTo: document.body,
			        activeTab: 0,
			        height:height,
			        border:false,
			        // plain:true, 
			        defaults:{autoScroll: false,
				    width:'auto'},
			        items:[{
			        	 width:'auto',
			                title: '<spring:message code="fhd.sys.auth.authority.authorityInfo"/>',
			                html:"<iframe id='iframe1' src='${ctx}/sys/auth/authority/edit.do?id=${id}' scrolling='no'  frameBorder=0></iframe>"
			            },{
			            	width:'auto',
			                title: '<spring:message code="fhd.sys.auth.authority.nextAuthority"/>',
			                html:"<iframe id='iframe2' src='${ctx}/sys/auth/authority/query.do?id=${id}'  scrolling='no' frameBorder=0></iframe>"
			            }
			        ]
			    });
			 Ext.EventManager.onWindowResize(function(width,height){
				 Ext.getCmp("tabs").setWidth(width);
				 Ext.getCmp("tabs").setHeight(height);
				 if(Ext.get("iframe1")!==null)
				 {
					 Ext.get("iframe1").setWidth(width);
					 Ext.get("iframe1").setHeight(height);
					 
				 }
				 if( Ext.get("iframe2")!==null)
				 {
				    Ext.get("iframe2").setWidth(width);
				    Ext.get("iframe2").setHeight(height);
				 }
			 });
		});
		
	</script>
	
</body>
</html>