<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.duty.duty"/><spring:message code="fhd.common.tabs"/></title>
	 
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
	
</head>
<body >


<script type="text/javascript">

// var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
</script>

<script type="text/javascript">
        var height=Ext.getBody().getHeight();
		Ext.onReady(function(){
			 var tabs2 = new Ext.TabPanel({
			        renderTo: document.body,
			        activeTab: 0,
			        id:'tabs',
			        //width:document.body.getHeight(),
			        height:height,
			        // plain:true, 
			        defaults:{autoScroll: false},
			        items:[
			        <c:if test="${param.nexduty != null }">
			        	{
			                title: '下级职务',
			                html:"<iframe src='${ctx}/sys/duty/nexdutyList.do?id=${param.id}' scrolling='no'  frameBorder=0></iframe>"
			            }
			         </c:if>
			         <c:if test="${param.nexpeople != null }">
			        	{
			                title: '修改职务',
			                html:"<iframe src='${ctx}/sys/duty/editPage.do?id=${param.id}' scrolling='no'  frameBorder=0></iframe>"
			            },
			            { id:'frame2',
			                title: '下级人员',
			                html:"<iframe src='${ctx}/sys/duty/nexpeopleList.do?id=${param.id}' height='100%' scrolling='no' frameBorder=0></iframe>"
			            }
			         </c:if>   
			        ]
			    });
			 Ext.EventManager.onWindowResize(function(width,height){
				 Ext.getCmp("tabs").setWidth(width);
				 Ext.getCmp("tabs").setHeight(height);
				
			 });
		});
		

</script>


</body>
</html>