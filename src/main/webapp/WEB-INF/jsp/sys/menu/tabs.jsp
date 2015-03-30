<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title><spring:message code="menu"/><spring:message code="tabs"/></title>
	<script type="text/javascript">
	    var tabs2;
	    var height=Ext.getBody().getHeight();
		var width=Ext.getBody().getWidth();
		Ext.onReady(function(){
		 tabs2 = new Ext.TabPanel({
			 id:'tabs',
		     renderTo: document.body,
		     height:height,
		     activeTab: 0,
		      // plain:true,
		      border:false,
		     hideBorders:true ,
		     frame:false,
		     defaults:{autoScroll:false,
		     width:'auto'},
		     items:[
				   {
				      width:'auto',
				      layout:'fit',
		              title: '<spring:message code="menuInfo"/>',
		              html:"<iframe width='100%' height='100%' id='iframe1' src='${ctx}/sys/menu/edit.do?id=${id}' scrolling='no'  frameBorder='0'></iframe>"
		            }
		            ,{
			          layout:'fit',
		  		      width:'auto',
		              title: '<spring:message code="nextMenu"/>',
		              html:"<iframe id='iframe2' width='100%' height='100%' src='${ctx}/sys/menu/query.do?id=${id}'  scrolling='no' frameBorder='0'></iframe>"
		            }
		        ]
		      
		    });
		  
		 Ext.EventManager.onWindowResize(function(width,height){
			 Ext.getCmp("tabs").setWidth(width);
			 Ext.getCmp("tabs").setHeight(height);
			 if(Ext.get("iframe1")!=null)
			 {
				 Ext.get("iframe1").setWidth(width);
				 Ext.get("iframe1").setHeight(height);
				 
			 }
			 if( Ext.get("iframe2")!=null)
			 {
			    Ext.get("iframe2").setWidth(width);
			    Ext.get("iframe2").setHeight(height);
			 }
		 });
		});
	</script>
</head>
<body>
</body>
</html>