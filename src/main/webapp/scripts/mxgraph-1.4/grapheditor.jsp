<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!--
  $Id: grapheditor-gears.html,v 1.3 2010-01-02 09:45:15 gaudenz Exp $
  Copyright (c) 2006-2010, JGraph Ltd
  
  Graph Editor example for mxGraph. This example demonstrates using
  mxGraph inside an ExtJs panel, and integrating tooltips, popupmenus,
  toolbars and dialogs into mxGraph.
-->
<html>
<head>
    <title>Graph Editor with Gears</title>
 
 	
	<link rel="stylesheet" type="text/css" href="${ctx}/scripts/mxgraph-1.4/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/scripts/mxgraph-1.4/css/grapheditor.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/scripts/mxgraph-1.4/css/common.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/scripts/mxgraph-1.4/css/explorer.css" />
	
	<!--mxclinet-->
	<script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Base/IEJS/mxclient.js"></script> 
	<%-- <script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Base/firefoxJS/mxclient.js"></script>  --%>
	<script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Base/ext-base.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Base/ext-all.js"></script>
	<script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Base/jquery-1.5.js"></script>
	<!-- Sets the basepath for the library if not in same directory -->
	<script type="text/javascript">
		mxBasePath = 'src';
	</script>

    
    
	<script type="text/javascript">

		// Replaces the built-in alert dialog with ExtJs message dialog (can't
		// replace confirm and prompt as they are callback-based and we require
		// a synchronous call with a return value).
		// Ext.MessageBox.prompt('Prompt', message, null, null, null, defaultValue);
		// Ext.MessageBox.confirm('Confirm', message);
		var url = 'http://${header["host"]}${pageContext.request.contextPath}/scripts/mxgraph/';
		//加载XML文档以显示新的绘图
		function LoadXmlDocument(xml) 
		{
		    if (xml != null && xml.length > 0)
			{
				var doc = mxUtils.parseXml(xml); 
				var dec = new mxCodec(doc); 
				dec.decode(doc.documentElement, graph.getModel()); 
			}
		}
		//上传XML文档到服务器
		function UploadXmlHtmlDocument(xml,html)
		{
		    //str 变量中包含当前文档相应的XML文档字符串
		    alert(xml);
		    //html 变量中包含当前文档相应的HTML文档字符串
		    alert(html);
		}
		function CustomImageLibrary()
	    {
	        insertImageTemplate(library, graph, '铃', url+'images/bell.png', false);
	        insertImageTemplate(library, graph, '盒子', url+'images/box.png', false);
	        insertImageTemplate(library, graph, '立方体', url+'images/cube_green.png', false);
	        //insertImageTemplate(library, graph, '用户', 'images/dude3.png', true);
	        insertImageTemplate(library, graph, '地球', url+'images/earth.png', true);
	        insertImageTemplate(library, graph, '齿轮', url+'images/gear.png', true);
	        insertImageTemplate(library, graph, '家', url+'images/house.png', false);
	        insertImageTemplate(library, graph, '包', url+'images/package.png', false);
	        insertImageTemplate(library, graph, '打印机', url+'images/printer.png', false);
	        insertImageTemplate(library, graph, '服务器', url+'images/server.png', false);
	        insertImageTemplate(library, graph, '工作站', url+'images/workplace.png', false);
	        insertImageTemplate(library, graph, '扳手', url+'images/wrench.png', true);
	
	    }
	    function CustomSymbolLibrary()
	    {
	        insertSymbolTemplate(library, graph, '取消', url+'images/symbols/cancel_end.png', false);
	        insertSymbolTemplate(library, graph, '错误', url+'images/symbols/error.png', false);
	        insertSymbolTemplate(library, graph, '事件', url+'images/symbols/event.png', false);
	        insertSymbolTemplate(library, graph, '交叉', url+'images/symbols/fork.png', true);
	        insertSymbolTemplate(library, graph, '包含', url+'images/symbols/inclusive.png', true);
	        insertSymbolTemplate(library, graph, '连接', url+'images/symbols/link.png', false);
	        insertSymbolTemplate(library, graph, '合并', url+'images/symbols/merge.png', true);
	        insertSymbolTemplate(library, graph, '消息', url+'images/symbols/message.png', false);
	        insertSymbolTemplate(library, graph, '多体', url+'images/symbols/multiple.png', false);
	        insertSymbolTemplate(library, graph, '规则', url+'images/symbols/rule.png', false);
	        insertSymbolTemplate(library, graph, '终止', url+'images/symbols/terminate.png', false);
	        insertSymbolTemplate(library, graph, '计时器', url+'images/symbols/timer.png', false);
	    }
		mxUtils.alert = function(message)
		{
			Ext.example.msg(message, '', '');
		};
		jQuery(function(){
			main();
		});
	</script>
	<script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Panel/GraphEditor.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Panel/MainPanel.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Panel/LibraryPanel.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Panel/DiagramStore.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/mxgraph-1.4/js/Panel/DiagramPanel.js"></script>
</head>
<body>
</body>
</html>
