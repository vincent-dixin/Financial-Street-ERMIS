<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帮助内容</title>

<style>
.btn-b {
	width: 102px;
	height: 30px;
	line-height: 28px;
	color: #FFF;
	font-size: 14px;
	font-weight: bold;
	vertical-align: middle;
	overflow: visible;
	border: none;
	background: url(${ctx}/images/help/btn.png) no-repeat 0 -48px;
	overflow: hidden;
}

.btn-b:hover,.btn-b.hover {
	background-position: 0 -78px;
}



.mod {
	padding: 0 10px;
	border: 1px solid #D8D8D8;
}

.mod-hd {
	height: 29px;
	line-height: 29px;
	border-bottom: 1px solid #D8D8D8;
}

.mod-hd h2 {
	float: left;
	font: bold 14px/29px "\5FAE\8F6F\96C5\9ED1";
}

.mod-hd .link-more {
	float: right;
}

.path span {
	margin: 0 3px;
}

.mod-bd {
	padding: 5px 0;
	width: 100%;
}

#HelpClassList {
	padding: 10px;
	background-color: rgb(217, 232, 251);
	border: 1px solid #99bbe8
}
#Synopsis{padding:10px;line-height:18px}
.question-cat {
	line-height: 26px;
	margin: 5px 0
}

.question-cat dt {
	font-weight: 700
}

.question-cat dd {
	display: inline;
	margin-right: 5px;
	width: 200px;
}

.search {
	height: 88px;
	padding: 15px 35px 15px 115px;
	text-align: center;
	background:
		url(http://img03.taobaocdn.com/tps/i3/T1TMVMXapAXXXXXXXX-708-118.jpg)
		no-repeat 0 0;
	border: 1px solid #D8D8D8;
}

.search-ss {
	height: 80px;
	padding: 0 75px 0 20px;
	background-image:
		url(http://img04.taobaocdn.com/tps/i4/T1s3pMXcNAXXXXXXXX-708-81.jpg);
}
</style>
<script type="text/javascript">

function printdiv(printpage) {
	var headstr = "<html><head><title></title></head><body>";
	var footstr = "</body>";
	var newstr = document.all.item(printpage).innerHTML;
	var oldstr = document.body.innerHTML;
	document.body.innerHTML = headstr+newstr+footstr;
	window.print(); 
	document.body.innerHTML = oldstr;
	return false;
}

</script>

</head>
<body style="overflow-x: hidden;">
<%@include file="search.jsp" %>
<br />
<div class="mod">
	<div class="mod-hd">
		<a href="#">常见问题</a> 
		<c:if test="${not empty helpTopic.helpCatalog.parent}">
			<c:if test="${not empty helpTopic.helpCatalog.parent.parent}">
				&nbsp;&gt;&nbsp;<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${helpTopic.helpCatalog.parent.parent.id}">${helpTopic.helpCatalog.parent.parent.catalogName}</a>
			</c:if>
			&nbsp;&gt;&nbsp;<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${helpTopic.helpCatalog.parent.id}">${helpTopic.helpCatalog.parent.catalogName}</a>
		</c:if>
		&nbsp;&gt;&nbsp;<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${helpTopic.helpCatalog.id}">${helpTopic.helpCatalog.catalogName}</a>
		&nbsp;&gt;&nbsp;帮助内容
	</div>
	<div id="printdiv" class="mod-bd" >
		<br />
		<br />
		<center><font size="6"><h1>${helpTopic.topicName}</h1></font></center>
		<br />
		<br />
		<br />
		${helpTopic.content}
		<br />
		<br />
		<div style="float: right;padding-top:10px;">
		<a href="#" onclick="javascript:printdiv('printdiv');"><img src="${ctx}/images/icons/printer_add.png" alt="打印本页面"  />打印本页面</a>
		</div>
	</div>
</div>

</body>
</html>