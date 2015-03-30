<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<style>
		iframe{width:100%;height:100%;margin:0px 0px}
	</style>
	<%-- 
<script type="text/javascript">
	function setSrc(){
		document.getElementById("ownerOrgShip").src="${ctx}/sys/orgstructure/emp/empAssignShip.do?id=${id}";
	}
</script>
	--%>

</head>
<body>


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
			        defaults:{autoScroll: false,
				    width:'auto'},
			        items:[

			        	{
			                title: '员工信息',
			                html:"<iframe src='${ctx}/sys/orgstructure/emp/edit.do?id=${id}' scrolling='no'  frameBorder=0></iframe>"
			            },
			        	{
			                title: '组织归属',
			                html:"<iframe src='${ctx}/sys/orgstructure/emp/empAssignShip.do?id=${id}' scrolling='no'  frameBorder=0></iframe>"
			            },
			        	{
			                title: '分配角色',
			                html:"<iframe src='${ctx}/sys/orgstructure/empAssignRole.do?id=${id}' scrolling='no'  frameBorder=0></iframe>"
			            },
			            {id:'frame2',
			                title: '分配权限',
			                html:"<iframe src='${ctx}/sys/orgstructure/empAssignAuthority.do?id=${id}'  scrolling='no' frameBorder=0></iframe>"
			            }
			        ]
			    });
			 Ext.EventManager.onWindowResize(function(width,height){
				 Ext.getCmp("tabs").setWidth(width);
				 Ext.getCmp("tabs").setHeight(height);
				
			 });
		});
		

</script>

<%--

<div id="TabbedPanels1" class="TabbedPanels">
	<ul class="TabbedPanelsTabGroup">
		<li class="TabbedPanelsTab" tabindex="0"><spring:message code="empInfo"/></li>
		<li class="TabbedPanelsTab" tabindex="0"><a href="javascript:void(0)" onclick="javascript:setSrc();"><spring:message code="ownerOrgShip"/></a></li>
		<c:if test="${flag eq 1}">
			<li class="TabbedPanelsTab" tabindex="0"><spring:message code="assignRole"/></li>
			<li class="TabbedPanelsTab" tabindex="0"><spring:message code="assignAuthority"/></li> 
		</c:if>
	</ul>
	<div class="TabbedPanelsContentGroup">
		<div class="TabbedPanelsContent">
			<iframe scrolling="auto" width="100%" height="555" noresize="noresize" src="${ctx}/sys/orgstructure/emp/edit.do?id=${id}" frameborder="0"></iframe>
		</div>
		<div class="TabbedPanelsContent">
			<iframe id="ownerOrgShip" scrolling="auto" width="100%" height="555" noresize="noresize" src="" frameborder="0"></iframe>
		</div>
		<c:if test="${flag eq 1}">
			<div class="TabbedPanelsContent">
				<iframe scrolling="auto" width="100%" height="555" noresize="noresize" src="${ctx}/sys/orgstructure/empAssignRole.do?id=${id}" frameborder="0"></iframe>
			</div>
			<div class="TabbedPanelsContent">
				<iframe scrolling="auto" width="100%" height="555" noresize="noresize" src="${ctx}/sys/orgstructure/empAssignAuthority.do?id=${id}" frameborder="0"></iframe>
			</div>
		</c:if>
	</div>
</div>

<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
</script>
 --%>
</body>
</html>