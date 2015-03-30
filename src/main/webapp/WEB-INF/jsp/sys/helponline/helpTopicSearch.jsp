<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

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

#Synopsis {
	padding: 10px;
	line-height: 18px
}

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


</style>
</head>
<body style="overflow-x: hidden;overflow-y: auto;">
	<%@include file="search.jsp" %>
	<br />
	<div class="mod">
		<div class="mod-hd">
			<h1>
				搜到官方答案 “${param.query}” <font color="red">${searchResults.totalHits}</font> 条
			</h1>
		</div>
		<div class="mod-bd">
			<c:if test="${! empty searchResults}">
				<c:forEach var="hit" items="${searchResults.hits}">
					<div style="width:100%;">
						<dl>
							<dt style="font-size: 18px;padding-top: 10px;">
								<a href="${ctx}/sys/helponline/helpTopicView.do?topicid=${hit.data.id}">
								<c:choose>
									<c:when test="${not empty hit.highlightedText['name']}">
					  					${hit.highlightedText['name']}
					  				</c:when>
									<c:otherwise>
					  					${hit.data.topicName}
					  				</c:otherwise>
								</c:choose>
								</a>
							</dt>
							<dd style="border-bottom: 1px dashed #ddd;font-size: 12px;padding-bottom: 5px;padding-top: 5px;">
								<c:choose>
									<c:when test="${not empty hit.highlightedText['content']}">
					  					${hit.highlightedText['content']}
					  				</c:when>
									<c:otherwise>
					  					${hit.data.hitContent}
					  				</c:otherwise>
								</c:choose>
							</dd>
						</dl>
					</div>
				</c:forEach>
				<c:if test="${not empty searchResults.pages}">
					<table>
						<tr>
							<td>第${param.page + 1} 页，共 ${fn:length(searchResults.pages)} 页</td>
							<c:if test="${(param.page + 1) != 1}">
								<td>
									<form method="GET">
										<input type="hidden" name="query" value="<c:out value="${param.query}"/>" /> 
										<input type="hidden" name="page" value="<c:out value="0"/>" /> 
										<input type="submit" value="首页" />
									</form>
								</td>
								<td>
									<form method="GET">
										<input type="hidden" name="query" value="<c:out value="${param.query}"/>" /> 
										<input type="hidden" name="page" value="<c:out value="${param.page-1}"/>" /> 
										<input type="submit" value="上一页" />
									</form>
								</td>
							</c:if>
							<c:if test="${(param.page +1) != fn:length(searchResults.pages)}">
								<td>
									<form method="GET">
										<input type="hidden" name="query" value="<c:out value="${param.query}"/>" /> 
										<input type="hidden" name="page" value="<c:out value="${param.page+1}"/>" /> 
										<input type="submit" value="下一页" />
									</form>
								</td>
								<td>
									<form method="GET">
										<input type="hidden" name="query" value="<c:out value="${param.query}"/>" /> 
										<input type="hidden" name="page" value="<c:out value="${fn:length(searchResults.pages)-1}"/>" />
										<input type="submit" value="尾页" />
									</form>
								</td>
							</c:if>
						</tr>
					</table>
				</c:if>
			</c:if>
		</div>
	</div>
</body>
</html>