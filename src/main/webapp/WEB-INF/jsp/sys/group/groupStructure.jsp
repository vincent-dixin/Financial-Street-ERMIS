<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.group.group" /></title>
</head>
<body>
<table style="height:100%;width:100%">
	<tr>
    	<td width="200">
    		<div id="treediv"></div>
        </td>
        <td>
        	<iframe src="" name="mainframe" id="mainframe" width="100%" scrolling="no" height="0" frameborder="0"></iframe>
        </td>
    </tr>
</table>
<fhdcore:tree rootText="${root.groupName}" 
rootHref="${ctx}/sys/group/tabs.do?id=${root.id}" 
el="treediv" 
url="${ctx}/sys/group/loadTree.do" 
height="document.body.offsetHeight - 5" width="200" rootId="${root.id}" rootIconCls="sysgroup-tree-icon" enableDD="true">
	<fhdcore:treeNode nodeType="sysGroup" url="${ctx}/sys/group/loadTree.do"></fhdcore:treeNode>
</fhdcore:tree>
	<script type="text/javascript">
		Ext.onReady(function(){
			document.getElementById('mainframe').src='${ctx}/sys/group/tabs.do?id=1';
			FHD.util.UI.expand('mainframe');
			tree.on('click',function(node){
				document.getElementById('mainframe').src='${ctx}/sys/group/tabs.do?id='+node.id;
				FHD.util.UI.expand('mainframe');
			});
			
			tree.on('contextmenu', onContextMenu);
			function onContextMenu(node, e) {
				var menu;
				if ("root" === node.attributes.cls) {
					if(!menu){
						menu = new Ext.menu.Menu(
							{ 
								id:'root'+ Math.random(),
								items:[
									{
										id : node.id + 'refresh' + Math.random(),
										text : '<spring:message code="fhd.common.refresh"/>',
										iconCls : 'icon-arrow-refresh-blue',
										handler : function() {
											 node.reload();
										}
									}
								]
							}
						);
					}
				}else{
					if(false == node.attributes.leaf){
						if(!menu){
							menu = new Ext.menu.Menu(
								{ 
									id:'root'+ Math.random(),
									items:[
					
										{
											id : node.id + 'refresh' + Math.random(),
											text : '<spring:message code="fhd.common.refresh"/>',
											iconCls : 'icon-arrow-refresh-blue',
											handler : function() {
												 node.reload();
											}
										}
									]
								}
							);
						}
					}
				}
				e.preventDefault();
				node.select();
				menu.showAt(e.getPoint());
			}
		});

		// 刷新
		function nodeReload(){
			tree.getSelectionModel().getSelectedNode().reload();
		}
	</script>
</body>
</html>