<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程树</title>
<script type="text/javascript">
Ext.onReady(function(){
	var panel = Ext.create('FHD.ux.standard.StandardTree',{
		id:'formdemo',
		width:300,
		height:500,
		renderTo:'standard${param._dc}',
		autoScroll:true,
		single:false, 
		extraParams:{canChecked : false},
		/* 树节点点击事件 */
		clickFunction:function(node){
			
		},
		onCheckchange:function(item, check){
			//alert('Checkchange');
		}
	});
	FHD.componentResize(panel,200,0); 
});
</script>
</head>
<body>
	<div id='standard${param._dc}'><div id="l1"></div></div>
</body>
</html>