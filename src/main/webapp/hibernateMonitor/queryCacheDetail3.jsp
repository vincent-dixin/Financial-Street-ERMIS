<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="head.jsp" %>
<html>
<head>
<title>Hibernate监控页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>
<body>
<div style="color: #0D5A7B; height: 15px; background-color: #DAF1FC; border: 1px solid #8DD3F5; font-size: 12px; padding: 3px">
  <b>Hibernate监控</b>
</div>
<p/>
<div align="center">
<table style="font-size: 12px" width="80%">
  <tr>
    <td><b style="color: #0B6693">计时间: 从 <%=messageFormat.format(new Object[]{startDate})%>  到 <%=messageFormat.format(new Object[]{nowDate})%></b></td>
  </tr>
</table>
<table cellpadding="5" cellspacing="1"  bgcolor="#26AEF0" style="font-size: 12px" width="80%">
  <tr bgcolor="#E1F4FD">
    <td colspan="2"><b>查询缓存统计</b></td>
  </tr>
  <tr bgcolor="white">
    <td>总命中率:</td>
    <td>
    	<%=percentFormat.format(statistics.getQueryCacheHitCount() * 1.0 / (statistics.getQueryCacheHitCount() + statistics.getQueryCacheMissCount()))%>
       命中的次数:<%=numberFormat.format(statistics.getQueryCacheHitCount())%>
      Miss的次数:<%=numberFormat.format(statistics.getQueryCacheMissCount())%> 
      Miss平均: <%=numberFormat.format(statistics.getQueryCacheMissCount() / lastSeconds)%> 次/秒
    </td>
  </tr>
  <tr bgcolor="white">
    <td>被缓存的查询个数:</td>
    <td> <%=numberFormat.format(statistics.getQueryCachePutCount())%></td>
  </tr>
  <tr bgcolor="white">
    <td>查询执行的次数:</td>
    <td> <%=numberFormat.format(statistics.getQueryExecutionCount())%></td>
  </tr>
  <tr bgcolor="white">
    <td>查询执行的最长时间:</td>
    <td> <%=numberFormat.format(statistics.getQueryExecutionMaxTime())%> 毫秒</td>
  </tr>
<%
  QueryStatistics [] querys = new QueryStatistics[statistics.getQueries().length];
  long []sortField = new long[querys.length];
  for(int i=0; i<statistics.getQueries().length; i++) {    String queryName = statistics.getQueries()[i];
    querys[i] = statistics.getQueryStatistics(queryName);
    QueryStatistics q = querys[i];
  	if (q.getCacheHitCount() + q.getCacheMissCount() == 0) {
  	  sortField[i] = q.getExecutionCount() > 0 ? 1000000000 + q.getExecutionCount():0;
	  } else {
	  	sortField[i] = 1000 * q.getExecutionCount() / (q.getCacheHitCount() + q.getCacheMissCount());
  	}
    
  }
  for (int i = 0; i < querys.length; i ++) {
  	for (int j = i + 1; j < querys.length; j++) {
  	    if (sortField[i] < sortField[j]) {
  	    	QueryStatistics s = querys[i]; querys[i] = querys[j]; querys[j] = s;
  	    	long temp = sortField[i]; sortField[i] = sortField[j]; sortField[j] = temp;
  	    }
  	}
  }
%>
<%@ include file="queryCacheDisplay.jsp" %>  
</table>
</div>
</body>
</html>
<%!
  private SessionFactory sessionFactory;
%>