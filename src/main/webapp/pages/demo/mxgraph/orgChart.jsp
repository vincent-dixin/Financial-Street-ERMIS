<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>Orgchart example for mxGraph</title>
		<script type="text/javascript">
			Ext.onReady(function(){
				Ext.create('FHD.ux.treeChar.TreeCharExt',{
					renderTo: 'div', 
					path:"${ctx}",
					treeCharModel:"edit",
					root:{
						id:"root",
						value:"根节点",
						image:'${ctx}/pages/demo/mxgraph/images/symbol_4_med.gif'
					},open:function(node){
						var nodes=new Array();
						var node={
							value:"节点1",
							image:'${ctx}/pages/demo/mxgraph/images/symbol_6_med.gif'
						}
						nodes.push(node);
						var node={
							value:"节点2",
							image:'${ctx}/pages/demo/mxgraph/images/symbol_5_med.gif'
						}
						nodes.push(node);
						return nodes;
					},add:function(parent){
			        	var node={
		        			value:"新建节点",
							image:"${ctx}/scripts/component/treeChar/images/open.png"	
			        	}
			        	return node;
					},del:function(node){
						return true;
					}
				});
			})
		</script>
	</head>
<body>
	<div id="div" style="height:100%;"></div>
</body>
</html>