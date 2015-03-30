<%@ page errorPage="/WEB-INF/jsp/commons/errorpage.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" buffer="48kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<%@ taglib uri="fhd-tag-core" prefix="fhdcore" %>
<%@ taglib uri="fhd-dic-tag" prefix="d" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> 
<c:set var="req" value="${pageContext.request}" /> 

<script src="${ctx}/scripts/old-edition/calendar/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery.validate.method.js" type="text/javascript"></script>
<script src="${ctx}/scripts/messages_cn.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href="${ctx}/css/FHDstyle.css" />

<script>
	var contextPath = "${ctx}";	
	var contextHttp = "${req.scheme}://${req.serverName}:${req.serverPort}${ctx}/";
	$(document).ready(function(){
		$("#exportlinks").html($("#fhd_button").html() + $("#exportlinks").html());
	});
	
	
	//onclick checkbox tr change color
	function onClickColor(tag){
		var tab = document.getElementById("o");
		
	    var check=0;                             
	    if(tag.checked){      
	       tag.parentNode.parentNode.style.backgroundColor='#DEEFF7'; 
	       check=1;              
	    }                                    
	    if(check == 0){  
		    for(i=1;i<tab.rows.length;i++){
		    	if(i%2!=0){
		    		if(tab.rows[i].firstChild.firstChild.checked){
				    	tab.rows[i].style.backgroundColor='#DEEFF7';
				    	continue;
				    }else{
						tab.rows[i].style.backgroundColor='#FFFFFF';
				    }
				 }else{
					if(tab.rows[i].firstChild.firstChild.checked){
					   	tab.rows[i].style.backgroundColor='#DEEFF7';
					    continue;
					}else{
						tab.rows[i].style.backgroundColor='#F4F5F9';
					}
			     }   
		    }   
		}
	}

	//onclick radio tr change color
	function onClickRaidoColor(tag){
		var tab = document.getElementById("o");
	    var check=0;                             
	    if(tag.checked){
	       tag.parentNode.parentNode.style.backgroundColor='#DEEFF7';
	       for(i=1;i<tab.rows.length;i++){
		     if(i%2!=0){
		    	if(tab.rows[i].firstChild.firstChild.checked){
			    	tab.rows[i].style.backgroundColor='#DEEFF7';
			    	continue;
			    }else{
					tab.rows[i].style.backgroundColor='#FFFFFF';
			    }
			 }else{
				if(tab.rows[i].firstChild.firstChild.checked){
				   	tab.rows[i].style.backgroundColor='#DEEFF7';
				    continue;
				}else{
					tab.rows[i].style.backgroundColor='#F4F5F9';
				}
			 }   
		   }
	    }
	}
	
	//onclick img table hide
	function hideTable(img){
		var showtable = document.getElementById("showTable");
		if(showtable.style.display === "none"){
			showtable.style.display="block";
			img.src = "${ctx}/images/plus.gif";
		}else{
			showtable.style.display="none";
			img.src = "${ctx}/images/end.gif";
		}
		
	}
	
</script>