<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.hibernate.SessionFactory" %>
<%@ page import="org.hibernate.stat.Statistics" %>
<%@ page import="org.hibernate.stat.SecondLevelCacheStatistics" %>
<%@ page import="org.hibernate.stat.QueryStatistics" %>
<%@ page import="org.hibernate.stat.EntityStatistics" %>
<%@ page import="org.hibernate.stat.CollectionStatistics" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%
  if(sessionFactory == null) {
    WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
    sessionFactory = (SessionFactory)applicationContext.getBean("sessionFactory"); 
  }
  Statistics statistics = sessionFactory.getStatistics();
  request.setAttribute("statistics", statistics);
  
  Date startDate = new Date(statistics.getStartTime());
  request.setAttribute("startDate", startDate);
  Date nowDate = new Date();
  long lastSeconds = (nowDate.getTime() - startDate.getTime())/1000;
  MessageFormat messageFormat = new MessageFormat("{0}");
  NumberFormat percentFormat = NumberFormat.getPercentInstance();
  NumberFormat numberFormat = NumberFormat.getInstance();
%>
