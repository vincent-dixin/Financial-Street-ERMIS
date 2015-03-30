<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>


</head>
<body style="overflow:hidden;">
	<table width="100%" height="100%">
		<tr>
			<td width="20%">
				<div style="width: 100%;height: 100%" id="helpTree"></div>
			</td>
			<td>
				<iframe id="helpTopicFrame" name="helpTopicFrame" src="${ctx}/sys/helponline/helpTopicList.do?catalogid=1" frameborder="0" width="100%" height="100%"></iframe>
			</td>
		</tr>
	</table>
<script type="text/javascript">
	var helpTree;
	Ext.onReady(function() {
		helpTree = new FHD.ext.Tree('helpTree', '知识分类', '1',
				'${ctx}/sys/helponline/helpCatalogTreeLoader.do', true);
		helpTree.tree.on('contextmenu', onContextMenu);
		function onContextMenu(node, e) {
			var menu = new Ext.menu.Menu([ {
				text : '添加知识',
				iconCls : 'icon-add',
				handler : function() {
					openWindow('添加知识', 515,105, "${ctx}/sys/helponline/addHelpCatalog.do?id="+node.id+"&name="+node.text);
				}
			}, '-', {
				text : '修改知识',
				iconCls : 'icon-edit',
				handler : function() {
					openWindow('添加知识', 515,213, "${ctx}/sys/helponline/editHelpCatalog.do?id="+node.id);
					
				}
			}, '-', {
				text : '删除知识',
				iconCls : 'icon-del',
				handler : function() {
				Ext.MessageBox.confirm('提示框', '您确定要删除？',function(btn){ 
					if(btn=='yes'){
						$.ajax({
							type: "POST",
							url: "${ctx}/sys/helponline/delHelpCatalog.do?catalogid="+node.id,
							success: function(msg){
								if("true"==msg){
									window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
									reNode();
								}else{
									window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>,该条目下有其他内容");
								}
							}
						}); 
					}
				});
			}
			}, '-', {
				text : '刷新',
				iconCls : 'icon-arrow-refresh-blue',
				handler : function() {
					helpTree.tree.getRootNode().reload();
				}
			} ]);
			e.preventDefault();
			node.select();
			menu.showAt(e.getPoint());
		}
	});
	function beforeDeleteCheckCallBack(){
		window.top.Ext.ux.Toast.msg('提示', '操作成功!');
	}
	function reNode(){
		helpTree.tree.getRootNode().reload();
	}
	
</script>
	
</body>
</html>