<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采集频率</title>
<script type="text/javascript">
/***attribute start***/
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	
	Ext.widget('collectionSelector', {
		renderTo: 'collectDemo',
	    name:'collections',
	    labelText : '采集频率',
	    single:false,
	    value:''
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='collectDemo'></div>
</body>
</html>