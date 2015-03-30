<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="org.hibernate.stat.ConcurrentQueryStatisticsImpl"%>
<%
  for(int i=0; i<querys.length; i++) {
	ConcurrentQueryStatisticsImpl queryStatistics = (ConcurrentQueryStatisticsImpl)querys[i];
    long totalCount = (queryStatistics.getCacheHitCount() + queryStatistics.getCacheMissCount());
    if (totalCount == 0) {
        totalCount = queryStatistics.getExecutionCount(); //没有设置query cache情况        
    }
    double percent = (totalCount == 0)? 0: queryStatistics.getCacheHitCount() * 1.0 / totalCount;
    double aveRowCount = (queryStatistics.getExecutionCount() > 0)? (queryStatistics.getExecutionRowCount() * 1.0 / queryStatistics.getExecutionCount()): 0;
%>
  <tr bgcolor="#F5FBFE">
    <td colspan="2">缓存名称: <%=queryStatistics.getCategoryName()%></td>
  </tr>
  <tr bgcolor="white">
    <td>命中率:</td>
    <td>
    	
    	<%=percentFormat.format(percent)%>
      
       命中的次数:<%=numberFormat.format(queryStatistics.getCacheHitCount())%>
      Miss的次数: <%=numberFormat.format(queryStatistics.getCacheMissCount())%>
    </td>
  </tr>
  <tr bgcolor="white">
    <td>执行次数:</td>
    <td>
    	<%=numberFormat.format(queryStatistics.getExecutionCount())%>
    </td>
  </tr>
  <tr bgcolor="white">
    <td>执行的最长时间:</td>
    <td>
    	<%=numberFormat.format(queryStatistics.getExecutionMaxTime())%>
      毫秒
    </td>
  </tr>
  <tr bgcolor="white">
    <td>执行的平均时间:</td>
    <td>
    	<%=numberFormat.format(queryStatistics.getExecutionAvgTime())%>
      毫秒
    </td>
  </tr>
  <tr bgcolor="white">
    <td>加权执行的平均时间:</td>
    <td>
    	
    	<%=numberFormat.format(queryStatistics.getExecutionAvgTime() * (1 - percent))%>
      毫秒
    </td>
  </tr>
  
  <tr bgcolor="white">
    <td>执行的最短时间:</td>
    <td>
    	<%=numberFormat.format(queryStatistics.getExecutionMinTime())%>
      毫秒
    </td>
  </tr>
  <tr bgcolor="white">
    <td>执行返回的总行数:</td>
    <td>
    	<%=numberFormat.format(queryStatistics.getExecutionRowCount())%>
      
    </td>
  </tr>
  <tr bgcolor="white">
    <td>执行返回的平均行数:</td>
    <td>
    	<%=numberFormat.format(aveRowCount)%>
      
    </td>
  </tr>
  
<%
  }
%>
