<%@ page errorPage="/WEB-INF/jsp/commons/errorpage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" buffer="48kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> 
<c:set var="req" value="${pageContext.request}" /> 
