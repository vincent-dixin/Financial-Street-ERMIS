<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.orgstructure.posi.position"/><spring:message code="fhd.common.tabs"/></title>
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
</head>
<body>



<script type="text/javascript">
        var height=Ext.getBody().getHeight();
		Ext.onReady(function(){
			 var tabs2 = new Ext.TabPanel({
				    id:'tabs',
			        renderTo: document.body,
			        activeTab: 0,
			        //width:document.body.getHeight(),
			        height:height,
			        // plain:true, 
			        defaults:{autoScroll: true},
			        items:[{
			                title: '<spring:message code="fhd.sys.orgstructure.posi.posiInfo"/>',
			                html:"<iframe src='${ctx}/sys/orgstructure/posi/edit.do?id=${id}' scrolling='no'  frameBorder=0></iframe>"
			            },{id:'frame2',
			                title: '<spring:message code="fhd.sys.orgstructure.org.nextemp"/>',
			                html:"<iframe src='${ctx}/sys/orgstructure/emp/queryEmpsByPosi.do?id=${id}'  scrolling='no' frameBorder=0></iframe>"
			            },{
			                title: '<spring:message code="fhd.sys.auth.user.assignRole"/>',
			                html:"<iframe src='${ctx}/sys/orgstructure/posiAssignRole.do?id=${id}'  scrolling='no'  frameBorder=0></iframe>"
			            },{
			                title: '<spring:message code="fhd.sys.auth.authority.assignAuthority"/>',
			                html:"<iframe src='${ctx}/sys/orgstructure/posiAssignAuthority.do?id=${id}'  scrolling='no' frameBorder=0></iframe>"
			            }
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