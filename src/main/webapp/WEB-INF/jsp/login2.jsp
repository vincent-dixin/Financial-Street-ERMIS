<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<script src="${ctx}/scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx }/scripts/CFInstall.min.js"></script>
	<div id="chromeFramePrompt"  style="display: none;">
		<div>
			<a href="${ctx}/tmp/attached/GoogleChromeframeStandaloneEnterprise.msi" target="_blank">如果想让系统运行更顺畅请下载安装 Google Chrome Frame</a>
		</div>
	</div>

	<script type="text/javascript">
	
		function getRootWin() {
			var win = window;
			while (win != win.parent) {
				win = win.parent;
			}
			return win;
		}
		
		if (getRootWin().location != self.location) {
			getRootWin().location = self.location;
		}
		function valid() {
			var username = document.getElementById("userid").value;
			var password = document.getElementById("password").value;
			if (username == null || username == "") {
				alert("请输入用户名!");
				return false;
			}
			if (password == null || password == "") {
				alert("请输入密码!");
				return false;
			}
			return true;
		}
	</script>
	<style>
		body{
			background-color:#789fc8;
			margin:0px;
			padding:0px;
		}
		.background{
			width:100%;
			height:335px;
			background:url(${ctx}/images/logon/004.jpg) repeat-x;
			margin-top:150px;
		}
		.main{
			width:831px;
			height:335px;
			position:absolute;  
			left:50%;  
			margin-left:-415px;
			background:url(${ctx}/images/logon/0001.jpg) no-repeat;
		}
		.title{
			width:378px;
			height:156px;
			background:url(${ctx}/images/logon/00021.gif) ;
		}
		.top{
			width:1024px;
			height:93px;
		}
		.main2{
			width:1024px;
			height:233px;
		}
		.center{
			width:347px;
			height:207px;
			float:left;
			margin-left:189px !important;
			margin-left:378px !important;
			background:url(${ctx}/images/logon/0003.gif) no-repeat;
			margin-top:0px;
		}
		.foot{  
			width:1024px;
			height:53px;
			clear: both;
			padding:0px;
			margin:0px;
		}
		.font{
			font-size:14px;
			color:#FFFFFF;
			font-weight:bold;
		}
		.ToolBar{
			height:24px;
			width:1024px;
			font-size:12px;
		}
		.button_reset{
	background-image: url(${ctx}/images/logon/button_reset.gif);
	background-repeat: no-repeat;
	border: 0px none #000;
	height: 27px;
	width: 82px;
	color: #FFF;
}
.buttondown_reset{
	background-image: url(${ctx}/images/logon/buttondown_reset.gif);
	background-repeat: no-repeat;
	border: 0px none #000;
	height: 27px;
	width: 82px;
	color: #FFF;
}
.button_sub{
	background-image: url(${ctx}/images/logon/button_sub.gif);
	background-repeat: no-repeat;
	border: 0px none #000;
	height: 27px;
	width: 82px;
	color: #FFF;

}
.buttondown_sub{
	background-image: url(${ctx}/images/logon/buttondown_sub.gif);
	background-repeat: no-repeat;
	border: 0px none #000;
	height: 27px;
	width: 82px;
	color: #FFF;
}
	</style>
</head>
<body>
	<%
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
	%>
	<form action="<c:url value='j_spring_security_check'/>" method="POST" onsubmit="return valid();">
		<input type="hidden" name="j_ip" id="ip" value="<%=ip%>" />
		<div class="background">
			<div class="main">
				<div class="title"></div>
				<div class="main2">
					<div class="center">
						<div style="margin-top: 25px;">
							<table border="0" cellpadding="0" cellspacing="0" style="margin-left: 50px;">
								<tr>
									<td height="40" class="font">用户:</td><!-- username -->
									<td colspan="2">&nbsp;&nbsp;<input type="text" value="${param.j_username}" id="userid" name="j_username" validateAttr="allowNull=false"
										style="width: 160px; height: 20px; background-color: #FFFFFF; border: solid 1px #153966; font-size: 12px; color: #283439;"
										size="20"/>
									</td>
								</tr>
								<tr>
									<td height="40" class="font">密码:</td><!-- password -->
									<td colspan="2">&nbsp;&nbsp;<input type="password" id="password" name="j_password" validateAttr="allowNull=false"
										style="width: 160px; height: 20px; background-color: #FFFFFF; border: solid 1px #153966; font-size: 12px; color: #283439;"
										size="20">
									</td>
								</tr>
								<tr>
									<td>
									&nbsp;
									</td>
									<td colspan="3" height="40">
									
										<input type="submit" name="button" value="&nbsp;&nbsp;登&nbsp;录" class="button_sub"  onmouseout="this.className='button_sub'"
 onMouseDown="this.className='buttondown_sub'" onMouseUp="this.className='button_sub'" >
										
										<input type="reset" name="button" value="&nbsp;&nbsp;重&nbsp;置" class="button_reset" onclick="" onMouseOut="this.className='button_reset'"
 onMouseDown="this.className='buttondown_reset'" onMouseUp="this.className='button_reset'">&nbsp;
										
									</td>
								</tr>
							</table>
							<div align="center" >
								<div height="60">&nbsp;</div>
								<c:if test="${not empty param.login_error}">
									<div id="status" class="errors" >
										<font color="red"> 登陆失败:用户名或者密码错误</font>
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>

<script type="text/javascript">
<!--

// The conditional ensures that this code will only execute in IE,
// Therefore we can use the IE-specific attachEvent without worry
window.attachEvent("onload", function() {
	
	CFInstall.check({
		destination: document.URL,
		mode: "overlay", // the default
		node: "chromeFramePrompt",
		oninstall: function() {
			alert('Google Chrome Frame was successfully installed');
			window.location.reload(true);
			
		},
		onmissing: function() {
			var promptPane = document.getElementById('chromeFramePrompt');
			promptPane.style.display = 'block';
		},
		preventPrompt: true
	});
});

//-->
</script>
</html>
