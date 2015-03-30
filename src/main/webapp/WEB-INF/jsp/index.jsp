<%@ page language="java" pageEncoding="utf-8"%> 
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<html>
<head>
<base href="${req.scheme}://${req.serverName}:${req.serverPort}${ctx}/">
		<title>第一会达风险管理平台</title>
		<!-- 主界面CSS -->
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/FHDstyle.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/selectable.css'/>"/>
		<!-- ext4默认样式CSS-->
		<link rel="stylesheet" type="text/css" href="<c:url value='/scripts/ext-4.1/resources/css/ext-all.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/icon.css'/>" />
		<!-- ext4核心JS -->
		<script type="text/javascript" src="<c:url value='/scripts/ext-4.1/ext-all-debug.js'/>"></script>
		<!-- ext4中文支持JS -->
		<script type="text/javascript" src="<c:url value='/scripts/ext-4.1/locale/ext-lang-zh_CN.js'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/scripts/fhd.js'/>"></script>
		
		<!-- 本地 国际化资源 -->
		<script type="text/javascript" src="<c:url value='/i18n'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/locale.js'/>"></script>
		
		<!-- 公用JS -->
		<script type="text/javascript" src="<c:url value='/App.js'/>" defer="defer"></script>
		<script type="text/javascript" src="<c:url value='/scripts/commons/dynamic.jsp'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/commons/selectable.js'/>"></script>
		<style type="text/css">
			#loading {
				position: absolute;
				left: 42%;
				top: 45%;
				padding: 2px;
				z-index: 20001;
				height: 50px;
				border: 2px solid #ccc;
			}
			
			#loading a {
				color: #225588;
			}
			
			#loading .loading-indicator {
				background: white;
				color: #444;
				font: bold 13px tahoma, arial, helvetica;
				padding: 10px;
				margin: 0;
				height: auto;
			}
			
			#loading-msg {
				font: normal 12px arial, tahoma, sans-serif;
				color: #444;
			}
			.x-panel-ghost {
			    z-index: 1;
			}
			.x-border-layout-ct {
			    background: #DFE8F6;
			}
			.x-portal-body {
			    padding: 0 0 0 8px;
			}
			.x-portal .x-portal-column {
			    padding: 8px 8px 0 0;
			}
			.x-portal .x-panel-dd-spacer {
			    border: 2px dashed #99bbe8;
			    background: #f6f6f6;
			    border-radius: 4px;
			    -moz-border-radius: 4px;
			    margin-bottom: 10px;
			}
			.x-portlet {
			    margin-bottom:10px;
			    padding: 1px;
			}
			.x-portlet .x-panel-body {
			    background: #fff;
			}
			.portlet-content {
			    padding: 10px;
			    font-size: 12px;
			}
			.x-tab-default-top .x-tab-inner {
			height: 14px !important;
			line-height: 14px !important;
			}
		</style>
		<script type="text/javascript">
			String.prototype.endWith = function(s) {
				if (s == null || s == "" || this.length == 0 || s.length > this.length)
					return false;
				if (this.substring(this.length - s.length) == s)
					return true;
				else
					return false;
				return true;
			}
			String.prototype.startWith = function(s) {
				if (s == null || s == "" || this.length == 0 || s.length > this.length)
					return false;
				if (this.substr(0, s.length) == s)
					return true;
				else
					return false;
				return true;
			}
			/*
			 * 
			 * string:原始字符串 substr:子字符串 isIgnoreCase:忽略大小写
			 */
		
			function contains(string, substr, isIgnoreCase) {
				if (isIgnoreCase) {
					string = string.toLowerCase();
					substr = substr.toLowerCase();
				}
				var startChar = substr.substring(0, 1);
				var strLen = substr.length;
				for (var j = 0; j < string.length - strLen + 1; j++) {
					if (string.charAt(j) == startChar)// 如果匹配起始字符,开始查找
					{
						if (string.substring(j, j + strLen) == substr)// 如果从j开始的字符与str匹配，那ok
						{
							return true;
						}
					}
				}
				return false;
			}
			/**
			 * 修复Grid的loadMask的BUG
			 */
			Ext.override(Ext.view.AbstractView, {
			    onRender: function(){
			        var me = this;
			        
			        me.callOverridden();
			        
			        if (me.mask && Ext.isObject(me.store)) {
			            me.setMaskBind(me.store);
			        }
			    }
			});

		
		   	Ext.onReady(function(){
		   		
		   		Ext.QuickTips.init();
			   	
				var storeTheme=FHD.getCookie('theme');
				if(storeTheme==null || storeTheme==''){
				 	storeTheme='ext-all';
				}
				Ext.util.CSS.swapStyleSheet("theme", __ctxPath+"/scripts/ext-4.1/resources/css/"+storeTheme+".css");
		    });
		   function helponline(){
				var btn = new Object();
				btn.url = "/sys/helponline/helpOnlineView.do";
				btn.text = "在线帮助";
				onButtonClick(btn);
				return false;
			}
		   function onButtonClick(btn){
				var url = '${ctx}' + btn.url;
				var centerPanel = Ext.getCmp('center-panel');
		    	var tab = centerPanel.getComponent(url);
		    	if(tab){
		    		centerPanel.setActiveTab(tab);
		    	}else{
		    		if(centerPanel.items.length >= 6)
		    			centerPanel.remove(1);
		        	    var p = centerPanel.add({
		        	    	id:url,
		        	        title: btn.text,
		                	tabTip:btn.text,
		        	        layout:'fit',
		        	        autoWidth:true,
		        	        iconCls: 'tabs',
		        	        html:"<iframe width='100%' height='100%' scrolling='auto' stype='overflow-y:hidden;' noresize='noresize' src='" + url + "' frameborder='0'></iframe>",
		        	        closable:true
		        	    });
		        	    centerPanel.setActiveTab(p);
		    	}
		    }
	    </script>
	</head>
	<body>
	<script type="text/javascript" src="<c:url value='/scripts/commons/Vtypes.js'/>"></script>
	<div id="loading">
             <div class="loading-indicator">
             	 <img
					src="${pageContext.request.contextPath}/scripts/ext-3.4.0/examples/shared/extjs/images/extanim32.gif"
					width="32" height="32"
					style="margin-right: 8px; float: left; vertical-align: top;" />
				<span id="loading-msg">验证用户身份...</span>

             </div>
        </div> 
        <script type="text/javascript">
		setTimeout(function(){
			document.getElementById('loading-msg').innerHTML = '正在装载页面...';
		}, 100);
		</script>
        <div id="loading-mask"></div> 
		<div id="app-header">
			<div id="header-left">
				<img id ="CompanyLogo" src="${ctx}/images/logo.gif" height="50" style="max-width:230px;"/>
			</div>
			<div id="header-main">
				<div id="header-info">
					<a href="http://www.firsthuida.com" onclick="" style="text-indent:25px;padding-left: 28px;" class="icon-company" target="_black">公司主页</a>
					&nbsp;
					欢迎您，${user.realname}(${user.username}),[<a href="<c:url value="/j_spring_security_logout"/>" onclick = "">注销</a>]
				</div>
				<div id="header-nav"></div>
			</div>
			<div id="header-right">
				<div id="setting">
				    <a href="javascript:void(0)" onclick="helponline()">帮助</a>
					|&nbsp;<a href="#" onclick="">设置</a>
					|&nbsp;<a href="http://www.firsthuida.com:2012/forum.php" onclick="" target="_blank">论坛</a>
				</div>
			</div>
		</div>
	</body>
</html>
