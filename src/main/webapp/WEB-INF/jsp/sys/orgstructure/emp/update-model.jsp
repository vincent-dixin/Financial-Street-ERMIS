<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.orgstructure.emp.editemp"/></title>
<base target="_self">
</head>
<body>
<form:form commandName="empForm" action="" method="post">
	<input type="hidden" id=userid name="userid" value="${empForm.userid}"/>
	<input type="hidden" id="id" name="id" value="${param.id}"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif" onclick="hideTable(this)" width="15" height="15" /><spring:message code="fhd.sys.orgstructure.emp.employee"/><spring:message code="fhd.common.modify"/></td></tr>
	</table>
	<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.empcode"/>：</th>
				<td>
					<form:input path="empcode" cssClass="required"/><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.empname"/>：</th>
				<td>
					<form:input path="empname" /><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.status"/>：</th>
				<td>
				<form:select path="empStatus" name="empStatus" cssClass="required">
					<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
					<option value="1"<c:if test="${empForm.empStatus eq '1' }">selected="selected"</c:if>><spring:message code="fhd.common.normal"/></option>
					<option value="0"<c:if test="${empForm.empStatus eq '0' }">selected="selected"</c:if>><spring:message code="fhd.common.cancellation"/></option>
				</form:select><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.gender"/>：</th>
				<td><d:radio dicTypeId="0gender" name="gender" property="${empForm.gender}" perrow="3"/></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.parentOrgname" />：</th>
				<td><form:input path="orgName" disabled="true"></form:input></td>
				<th><spring:message code="fhd.sys.orgstructure.emp.birthday"/>：</th>
				<td>
					<form:input path="birthdate" onclick="WdatePicker();" cssClass="Wdate dateISO"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.cardtype"/>：</th>
				<td>
					<div style="float:left"><d:select name="cardtype" style="width:100px" dicTypeId="0card_type" property="${emoForm.cardtype}"/></div><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.cardno"/>：</th>
				<td>
					<form:input path="cardno"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.otel"/>：</th>
				<td>
					<form:input path="otel" cssClass="isTel"/>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.oaddress"/>：</th>
				<td>
					<form:input path="oaddress" />
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.ozipcode"/>：</th>
				<td>
					<form:input path="ozipcode"/>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.oemail"/>：</th>
				<td>
					<form:input path="oemail" cssClass="email"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.faxno"/>：</th>
				<td>
					<form:input path="faxno"/>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.mobikeno"/>：</th>
				<td>
					<form:input path="mobikeno" cssClass="isMobile"/>
				</td>
			</tr>
			<tr>
				<th>MSN：</th>
				<td>
					<form:input path="msn"/>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.htel"/>：</th>
				<td>
					<form:input path="htel" cssClass="isTel"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.haddress"/>：</th>
				<td>
					<form:input path="haddress"/>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.hzipcode"/>：</th>
				<td>
					<form:input path="hzipcode"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.pemail"/>：</th>
				<td>
					<form:input path="pemail" cssClass="email"/>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.party"/>：</th>
				<td>
					<div style="float:left"><d:select name="party" style="width:100px" dicTypeId="0party" property="${empForm.party}"/></div><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.degree"/>：</th>
				<td>
					<div style="float:left"><d:select name="degree" style="width:100px" dicTypeId="0degree" property="${empForm.degree}"/></div><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.emp.major"/>：</th>
				<td>
					<form:input path="major"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.emp.specialty"/>：</th>
				<td>
					<form:input path="specialty"/>
				</td>
				<th><spring:message code="fhd.common.regdate"/>：</th>
				<td>
					<form:input path="regdate" onclick="WdatePicker();" cssClass="Wdate" />
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.remark"/>：</th>
				<td colspan="3">
					<form:textarea path="remark" cols="45" rows="5" />
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<td colspan="4">
					<input type="checkbox" name="checkOpr" id="checkOpr" onclick="isDisplay();" value="0"/><spring:message code="fhd.common.operatorInfo"/>
				</td>
			</tr>
		</table>
		<table id="showUserTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table" style="display:none;">
			<tr>
				<th><spring:message code="fhd.sys.auth.role.realName"/>：</th>
				<td><form:input path="realname" /><font color="red">*</font></td>
				<th><spring:message code="fhd.sys.auth.user.username"/>：</th>
				<td><form:input path="username" /><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.auth.user.password"/>：</th>
				<td><input id="resetpassword" name="resetpassword" type="password" style="width: 200"/>
				<form:input path="password" type="hidden" />
				</td>
				<th><spring:message code="fhd.common.status"/>：</th>
				<td>
					<fhd:dictEntrySelect value="${empForm.userStatus}" path="userStatus" dictName="userStatus"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.lockState"/>：</th>
				<td>
					<form:select path="lockstate" >
						<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
						<option value="1" <c:if test="${empForm.lockstate eq '1' }">selected="selected"</c:if>><spring:message code="fhd.common.lock"/></option>
						<option value="0" <c:if test="${empForm.lockstate eq '0' }">selected="selected"</c:if>><spring:message code="fhd.common.notLocked"/></option>
					</form:select>
					<font color="red">*</font>
				</td>
				<th><spring:message code="fhd.common.enable"/>：</th>
				<td>
					<form:select path="enable" >
						<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
						<option value="1" <c:if test="${empForm.enable eq 'true' }">selected="selected"</c:if>><spring:message code="fhd.common.true"/></option>
						<option value="0" <c:if test="${empForm.enable eq 'false' }">selected="selected"</c:if>><spring:message code="fhd.common.false"/></option>
					</form:select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.abatedate"/>：</th>
				<td><form:input path="expiryDate" onclick="WdatePicker();" cssClass="Wdate" /></td>
				<th><spring:message code="fhd.common.credentialsexpiryDate"/>：</th>
				<td><form:input path="credentialsexpiryDate" onclick="WdatePicker();" cssClass="Wdate" /></td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th colspan="4" align="center">
				    <input type="button" id="submits" name="submits" value="<spring:message code="fhd.common.save"/>" class="fhd_btn"/>&nbsp;&nbsp;
					<input id="reset" type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn" />
				</th>
			</tr>
		</table>
</form:form>

<script type="text/javascript">
	function isDisplay() {	
	   var c = document.getElementById("checkOpr");
	   if(c.checked){
		   document.getElementById("showUserTable").style.display = "block";
		   document.getElementById("checkOpr").value="1";
		   $("#username").addClass("required");
		   $("#realname").addClass("required");
		  // $("#password").addClass("required");
		   $("#userStatus").addClass("required");
		   $("#portal").addClass("required");
	   }else{
		   document.getElementById("showUserTable").style.display = "none";
		   document.getElementById("checkOpr").value="0";
		   $("#username").removeClass("required");
		   $("#realname").removeClass("required");
		  // $("#password").removeClass("required");
		   $("#userStatus").removeClass("required");
		   $("#portal").addClass("required");
	   }
	}
	
	$(document).ready(function(){
		var validator =$("#empForm").validate({
			errorElement: "em",
			//errorPlacement: function(error, element) {
	        //    if ( element.is(":radio") ){
	        //        error.appendTo( element.parent().next().next() );
	        //    }
	        //},
			success: function(label) {
				label.text("ok!").addClass("success");
			},
			rules: {
				empcode:{
					maxlength:32
				},
				empname:{
					required: true,
					maxlength:30,
				    remote: {
				        type:"POST",
				        url:"${ctx}/sys/orgstructure/emp/queryEmployeeByEmpname.do", 
				        data:{
				        	empname: function() {
					            return $("#empname").val();
					        },
					        operateType:2,
					        id: function() {
					            return $("#id").val();
					        }
				        }
					}
				},
				username:{
					maxlength:30,
				    remote: {
				        type:"POST",
				        url:"${ctx}/sys/orgstructure/emp/queryUserByUsername.do", 
				        data:{
				        	username: function() {
					            return $("#username").val();
					        },
					        operateType:2,
					        uid: function() {
						        return $("#userid").val();
					        }
				        }
					}
				}
			},
			submitHandler:function(form){
				form.submit();
				window.returnValue='refresh';
				window.close();
	        }
		});
		
		$("#reset").click(function() {
	        validator.resetForm();
	    });
	    
	      $('#submits').click(function(){
		if(true == validator.form()){
            var msgTip = FHD.opWait( {
				title : "<spring:message code='fhd.common.prompt'/>",
				width : 250,
				msg : "<spring:message code='fhd.common.opWait'/>",
				progress:true,
				wait:true
			});
			
			var options = {
				url:'${ctx}/sys/orgstructure/emp/edit.do',
				type:'POST',
				success:function(data) {
	     			msgTip.hide();
	         		if("true" == data){
	         			parentWindow().mv_grid.grid.store.reload();
	         			parentWindow().parent.parent.selectNodeReload();
	         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.operateSuccess" />');
	         			closeWindow();//关闭窗口
	         		}else
	         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.operateFailure" />');
	     		}
			};
			$('#empForm').ajaxSubmit(options);
			return false;
		}else{
			return false;
		}
    });
			
	});
	
<c:if test="${not empty success }">
	window.top.Ext.ux.Toast.msg('<spring:message code="fhd.common.operateSuccess" />', '${success}');
</c:if>

</script>
</body>
</html>