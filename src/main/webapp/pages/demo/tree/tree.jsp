<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树grid列表</title>
<script type="text/javascript">
/***attribute start***/
var tree, queryUrl = 'test/loadTestMvcTree.f'; //查询url
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	treetest = Ext.create('FHD.ux.TreePanel',{//实例化一个grid列表
        useArrows: true,
        multiSelect: true,
        rowLines:false,
        singleExpand: false,
        checked: false,
		renderTo: 'treedemo', //渲染到id为treedemo的div里
		url: queryUrl,//调用后台url
		height:FHD.getCenterPanelHeight()-5//高度为：获取center-panel的高度
	});
	tree.on('collapse',function(p){
		treetest.setWidth(panel.getWidth()-26-5);
	});
	tree.on('expand',function(p){
		treetest.setWidth(panel.getWidth()-p.getWidth()-5);
	});
	panel.on('resize',function(p){
		treetest.setHeight(p.getHeight()-5);
		if(tree.collapsed){
			treetest.setWidth(p.getWidth()-26-5);
		}else{
			treetest.setWidth(p.getWidth()-tree.getWidth()-5);
		}
	});
	tree.on('resize',function(p){
		if(p.collapsed){
			treetest.setWidth(panel.getWidth()-26-5);
		}else{
			treetest.setWidth(panel.getWidth()-p.getWidth()-5);
		}
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='treedemo'></div>
</body>
</html>