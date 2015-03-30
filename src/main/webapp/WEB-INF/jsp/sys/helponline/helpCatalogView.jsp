<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>

#Synopsis p{float:left; padding: 10px 0;}

</style>

<script type="text/javascript">
		var list;
		Ext.onReady(function(){
			
			
			<c:if test="${not empty helpCatalog.children}">
			var p = new Ext.Panel({
				title:'帮助分类',
				layout:'column',
				renderTo:'HelpCatalog',
				defaults:{
					columnWidth: 1/5,
					border:false,
					style:{
						margin: '5px'
					}
				},
				items:[
				<c:forEach items="${helpCatalog.children}" var="c" varStatus="s">
				<c:choose>
					<c:when test="${!s.last}">
					{
						html:'<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${c.id}">${c.catalogName}</a>'
					 },
					</c:when>
					<c:otherwise>
					{
						html:'<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${c.id}">${c.catalogName}</a>'
					 }
					</c:otherwise>
				</c:choose>
				 
				 </c:forEach>
				 ]
			});
			</c:if>
			var cols = [{hidden:true,dataIndex:"id"},
				        {    
							 width:100,
							 dataIndex:"topicName",
							 sortable:false,
				             renderer:function(value, metaData, record, rowIndex, colIndex, store){
		        	  		 	return "<a href=\"${ctx}/sys/helponline/helpTopicView.do?topicid=" + record.get("id") + "\"><div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div></a>";
							}
						}];
			list = new FHD.ext.Grid('HelpClassList', {catalogid:'${param.catalogid}'}, cols, null, "帮助主题", Ext.getBody().getWidth(), Ext.getBody().getHeight()-168,'queryHelpTopic.do', false,false, null);
		});
</script>
</head>
<body style="overflow-y: auto;">
<div class="mod">

	<div class="mod-hd">
		<a href="#">常见问题</a> 
		<c:if test="${not empty helpCatalog.parent}">
			<c:if test="${not empty helpCatalog.parent.parent}">
				&nbsp;&gt;&nbsp;<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${helpCatalog.parent.parent.id}">${helpCatalog.parent.parent.catalogName}</a>
			</c:if>
			&nbsp;&gt;&nbsp;<a href="${ctx}/sys/helponline/helpCatalogView.do?catalogid=${helpCatalog.parent.id}">${helpCatalog.parent.catalogName}</a>
		</c:if>
		&nbsp;&gt;&nbsp;${helpCatalog.catalogName}
	</div>
	<br />
	<!--查询条件开始-->
	<div id="queryDiv">
		<form action="${ctx}/sys/helponline/helpTopicSearch.do">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;查询条件</td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" >
				<tr>
					<th>
						帮助名称：
					</th>
					<td>
						<input type="text" name="query" class="i-text" style="width: 200px" placeholder="请输入查找关键字" value="${param.query}" />

					</td>
					<td>
						<input type="submit" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>

		</form>

	</div>
	<br />
	<!--查询条件结束-->
	<div class="mod-bd" >
		<div id="HelpCatalog">
		
		</div>
		<br />
		<div id="HelpClassList">
		</div>
	</div>
</div>
</body>
</html>