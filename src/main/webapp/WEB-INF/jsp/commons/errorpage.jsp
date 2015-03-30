<%@ page language="java" contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<html>
  <head>
    <title>内部服务器错误</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<script language="JavaScript" type="text/JavaScript">
	function showError() {
		var error = document.getElementById("exception");
		var s = error.style.display;
		error.style.display = (s == "none") ? "" : "none";
	}
	</script>
</head>

<body>
    <h2>内部服务器错误</h2>
	<font color="red" size="2">【<a href="javascript:history.back()"><font color="red" size="2">返回</font></a>】</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<font color="red" size="2">【<a href="javascript:showError()"><font color="red" size="2">详细错误</font></a>】</font>
	<hr>
	<div id="exception" style="color:blue; display:none">
      <pre>
<%
        if (exception != null) {
            exception.printStackTrace();
            exception.printStackTrace(new java.io.PrintWriter(out));
        }
        exception = (Throwable) request.getAttribute("exception");
        if (exception != null) {
            exception.printStackTrace();
            exception.printStackTrace(new java.io.PrintWriter(out));
        }
%>
      </pre>
	</div>
  </body>
</html>