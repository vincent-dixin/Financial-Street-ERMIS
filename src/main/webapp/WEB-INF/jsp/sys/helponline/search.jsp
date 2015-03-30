<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	font-size: 12px;
}

.mod-hd h2 {
	float: left;
	font: bold 14px/29px "\5FAE\8F6F\96C5\9ED1";
}
.mod a {
	font-size: 12px;
}

.mod-hd .link-more {
	float: right;
}

.path span {
	margin: 0 3px;
}

.mod-bd {
	padding: 5px 0;
	font-size: 12px;
}

#HelpClassList {
	padding: 10px;
	background-color: rgb(217, 232, 251);
	border: 1px solid #99bbe8
}
#Synopsis{padding:10px;line-height:18px; padding-bottom: 30px; }
#Synopsis p{float:left; padding: 10px 0;}
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
#queryDiv{
text-align: center;
}
-->
</style>
	<div id="queryDiv">
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;padding:5px;">
				<tr>
				<form action="${ctx}/sys/helponline/helpTopicSearch.do">
					<td style="text-align:center">
						<input type="text" name="query" class="i-text" style="width: 280px" placeholder="请输入查找关键字" value="${param.query}" />
						<input type="submit" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
					</form>
				</tr>
			</table>
	</div>