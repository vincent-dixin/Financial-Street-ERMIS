<%@page import="com.fhd.fdc.utils.UserContext"%>
<%@page pageEncoding="UTF-8" contentType="text/js; charset=UTF-8"%>
var __ctxPath="<%=request.getContextPath() %>";
var __fullPath="<%=request.getScheme() + "://" + request.getHeader("host") +  request.getContextPath()%>";
var __user = new Object();
__user.companyId = "<%=UserContext.getUser().getCompanyid()%>";
__user.companyName = "<%=UserContext.getUser().getCompanyName()%>";
__user.divisionManagerId = "<%=UserContext.getUser().getDivisionManagerId()%>";
__user.empId = "<%=UserContext.getUser().getEmpid()%>";
__user.majorDeptId = "<%=UserContext.getUser().getMajorDeptId()%>";
__user.majorDeptName = "<%=UserContext.getUser().getMajorDeptName()%>";
__user.realName = "<%=UserContext.getUser().getRealname()%>";
__user.userId = "<%=UserContext.getUser().getUserid()%>";
__user.userName = "<%=UserContext.getUser().getUsername()%>";
