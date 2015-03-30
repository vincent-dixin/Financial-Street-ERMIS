<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>

     <%--taskName: ${taskName} --bType: ${bType}--taskType: ${taskType}--userId: ${userId}--userChecks：${userChecks}
  --showIdeaFlag：${showIdeaFlag}--processInstanceId:${processInstanceId}  --%> 
		<c:if test="${processInstanceId !=null}">
			<tr>
				<th>处理意见：</th>
				<td valign="top">
					<div style="float: left;">
						<textarea cols="35" rows="12" id="examineApproveIdea" name="examineApproveIdea"></textarea>
					</div>
					<div id='examineApproveIdeaRadioTd' style="float: left;"></div>
				</td>
			</tr>
			<script type="text/javascript">
				jQuery(function(){
					var examineApproveIdeaRadioList=new Array();
					examineApproveIdeaRadioList.push("同意");
					examineApproveIdeaRadioList.push("不同意");
					jQuery.each(examineApproveIdeaRadioList,function(i,value){
						jQuery("#examineApproveIdeaRadioTd").append('<input type="radio" name="examineApproveIdeaRadio" value="'+value+'"/>'+value+'</br>');
					});
					jQuery("[name='examineApproveIdeaRadio']").change(function(){
						jQuery("[name='examineApproveIdea']").val(jQuery("[name='examineApproveIdeaRadio']:checked").val());
					});
				});
			</script>
		</c:if>
		<!-- 工作流未启动时，添加修改页面要加入审批人这一行 ---------------------------------------------------------------------------------  -->
		<c:if test="${ param.bType eq  '' }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm checkModel="cascade" multiple="false" require="true" radioType="12" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<!-- 工作流未启动时，添加修改页面要加入审批人这一行 ---------------------------------------------------------------------------------  -->

<!--   风险评估-------------------------------------------------------------------------------------------------------------              -->	
	<c:if test="${ (bType eq 'quest')}">
		<c:if test="${(taskName eq '创建评估计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="43" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '部门人员审核评估计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="46" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批评估计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="45" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '发布评估计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="44" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '文书接收评估计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="30" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、处长  -->
					<div id="empsDiv">
						<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" style="width:300px;" defaultOrg="true"></fhd:empSelector><font style="color:red">*</font>
					</div>
				</td>
			</tr>
			<script type="text/javascript">
				function empSelect(){
					var userCheck=jQuery("[name='userCheck']:checked").val();
					var divemp2=jQuery("#divemp2").parent();
					var empsDiv=jQuery("#empsDiv");
					if(userCheck=="zz"){
						divemp2.hide();	
						empsDiv.show();
					}else if(userCheck=="zr"){
						divemp2.show();
						empsDiv.hide();
					}
				}
				jQuery(function(){
					empSelect();
					jQuery("[name='userCheck']").change(function(){
						empSelect();
					});
				});
			</script>
		</c:if>
		<c:if test="${(taskName eq '领导分配任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="31" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- ,path  -->
					<div id="empsDiv">
						<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" style="width:300px;" defaultOrg="true"></fhd:empSelector><font style="color:red">*</font>
					</div>
				</td>
			</tr>
			<script type="text/javascript">
				function empSelect(){
					
					var userCheck=jQuery("[name='userCheck']:checked").val();
					var divemp2=jQuery("#divemp2").parent();
					var empsDiv=jQuery("#empsDiv");
					if(userCheck=="zz"){
						divemp2.hide();	
						empsDiv.show();
					}else if(userCheck=="zb"){
						divemp2.show();
						empsDiv.hide();
					}
				}
				jQuery(function(){
					empSelect();
					jQuery("[name='userCheck']").change(function(){
						empSelect();
					});
				});
			</script>
		</c:if>
		
		<c:if test="${(taskName eq '确认评估计划填写评估问卷') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="32" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书、退回  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批评估计划和问卷') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="49" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>	
		<c:if test="${(taskName eq '专责下发评估计划') }">
		    <tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="44" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员、退回 -->
			    </td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '部门领导分配任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="31" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- ,path  -->
					<div id="empsDiv">
						<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" style="width:300px;" defaultOrg="true"></fhd:empSelector><font style="color:red">*</font>
					</div>
				</td>
			</tr>
			<script type="text/javascript">
				function empSelect(){
					var userCheck=jQuery("[name='userCheck']:checked").val();
					var divemp2=jQuery("#divemp2").parent();
					var empsDiv=jQuery("#empsDiv");
					if(userCheck=="zz"){
						divemp2.hide();	
						empsDiv.show();
					}else if(userCheck=="zb"){
						divemp2.show();
						empsDiv.hide();
					}
				}
				jQuery(function(){
					empSelect();
					jQuery("[name='userCheck']").change(function(){
						empSelect();
					});
				});
			</script>
		</c:if>
		<c:if test="${(taskName eq '专责填写评估问卷') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="32" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书、退回  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '部门领导审批评估问卷') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="49" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>	
		<c:if test="${(taskName eq '省公司审批评估问卷') }">
		</c:if>					 
		
		
		
	</c:if>
<!--   风险辨识-------------------------------------------------------------------------------------------------------------              -->	
	<c:if test="${ (bType eq 'riskDifferent')}">
		<c:if test="${(taskName eq '创建风险辨识计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="43" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '部门人员审核风险辨识计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="46" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批风险辨识计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="45" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '发布风险辨识计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="44" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '文书接受风险辨识计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="30" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、处长  -->
					<div id="empsDiv">
						<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" style="width:300px;" defaultOrg="true"></fhd:empSelector><font style="color:red">*</font>
					</div>
				</td>
			</tr>
			<script type="text/javascript">
				function empSelect(){
					var userCheck=jQuery("[name='userCheck']:checked").val();
					var divemp2=jQuery("#divemp2").parent();
					var empsDiv=jQuery("#empsDiv");
					if(userCheck=="zz"){
						divemp2.hide();	
						empsDiv.show();
					}else if(userCheck=="zr"){
						divemp2.show();
						empsDiv.hide();
					}
				}
				jQuery(function(){
					empSelect();
					jQuery("[name='userCheck']").change(function(){
						empSelect();
					});
				});
			</script>
		</c:if>
		<c:if test="${(taskName eq '领导分配风险辨识任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="20" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- ,path  -->
					<div id="empsDiv">
						<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" style="width:300px;" defaultOrg="true"></fhd:empSelector><font style="color:red">*</font>
					</div>
				</td>
			</tr>
			<script type="text/javascript">
				function empSelect(){
					var userCheck=jQuery("[name='userCheck']:checked").val();
					var divemp2=jQuery("#divemp2").parent();
					var empsDiv=jQuery("#empsDiv");
					if(userCheck=="mr"){
						divemp2.hide();	
						empsDiv.show();
					}else if(userCheck=="zb"){
						divemp2.show();
						empsDiv.hide();
					}
				}
				jQuery(function(){
					empSelect();
					jQuery("[name='userCheck']").change(function(){
						empSelect();
					});
				});
			</script>
		</c:if>
		<c:if test="${(taskName eq '专责填写风险辨识任务') }">
				<tr>
					<th>处理人：</th>
					<td>
						<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="22" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书  -->
					</td>
				</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批风险辨识任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="39" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 文书  -->
				</td>
			</tr>
		</c:if>
			<c:if test="${(taskName eq '风险管理员审批风险辨识任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="7" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员汇总风险辨识计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="4" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任 、处长 -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部处长确认风险辨识结果') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="12" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部领导确认风险辨识结果') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="41" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员归档风险信息') }"></c:if>		
	</c:if>
	
	<!--控制措施执行记录--------------------------------------------------------------------------------------------------------------->	
	<c:if test="${(bType eq 'controlExecution')}">
	  <c:if test="${(taskName eq '填写控制措施执行评价')}">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="43" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  ，处长-->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '审批控制措施执行评价') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="46" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批控制措施执行评价') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="21" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员审批控制措施执行评价') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="28" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导确认控制措施执行评价') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="29" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员归档控制措施执行评价') }"></c:if>		
	</c:if>	
	
	<!--风险执行记录子流程-修改控制措施--------------------------------------------------------------------------------------------------------------->	
	<c:if test="${(bType eq 'controlExecutionSub')}">
	   <c:if test="${(taskName eq '新增/修改风险控制措施')}">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="43" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '审批风险控制措施')}">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="46" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批风险控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="39" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员审批风险控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="7" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部领导确认风险控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="10" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		
		<c:if test="${(taskName eq '风险管理员归档风险控制措施')}">
		</c:if>										
	</c:if>	
	
		<!--   福建电力指标流程------------------------------------------------------------------------------------------------------------              -->	
	<c:if test="${ (bType eq 'kpi')}">
		<c:if test="${(taskName eq '创建指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm checkModel="cascade" multiple="false" require="true" radioType="12" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部领导审批指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="26" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '文书接受指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="34" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、处长  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导分配指标任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="35" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- ,path  -->
				</td>
			</tr>
		</c:if>
		
		<c:if test="${(taskName eq '专责填写指标信息') }">
				<tr>
					<th>处理人：</th>
					<td>
						<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="32" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书  -->
					</td>
				</tr>
		</c:if>
		
		<c:if test="${(taskName eq '领导审批指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="42" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 下一个节点：管理员审批  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员审批指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="7" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部处长确认指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="12" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部领导确认指标信息') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="36" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员归档指标信息') }"></c:if>		
	</c:if>	
		
	
<!--   风险控制------------------------------------------------------------------------------------------------------------              -->	
	<c:if test="${ (bType eq 'scenario')}">
		<c:if test="${(taskName eq '创建控制计划') }">
				<tr>
					<th>处理人：</th>
					<td>
						<fhd:empSelectorBpm multiple="false" require="true" radioType="43" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 处长、部门领导 -->
					</td>
				</tr>
		</c:if>
		<c:if test="${(taskName eq '部门人员审核控制计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="46" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批控制计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="45" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员、退回 -->
			    </td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '专责下发控制计划') }">
		    <tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="44" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员、退回 -->
			    </td>
			</tr>
		</c:if>
		
		<c:if test="${(taskName eq '文书接收控制计划') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="48" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、处长  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '确认控制计划制定管控方案') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="32" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书、退回  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批管控方案') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="49" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>	
		<c:if test="${(taskName eq '专责下发管控方案') }">
		    <tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="44" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员、退回 -->
			    </td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '部门领导分配任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  radioType="50"  defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '专责填写控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="32" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书、退回  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '部门领导审批控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="49" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>	
			<c:if test="${(taskName eq '省公司审批控制措施') }">
			<tr style="display: none;">
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="21" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>					 
		<c:if test="${(taskName eq '风险管理员审批控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="7" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 回退、风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部领导确认风险控制措施') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="21" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员归档风险控制措施') }">
			<tr style="display: none;">
				<th>提醒领导：</th>
				<td>
					<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" defaultOrg="false" style="width:300px;" empfilter="role/ZHUREN"></fhd:empSelector>
				</td>
			</tr>
		</c:if>
		
	</c:if>
	
	
	
	<!--   预警通知单------------------------------------------------------------------------------------------------------------              -->	
	<c:if test="${ (bType eq 'alarm')}">
		<c:if test="${(taskName eq '创建预警通知单') }">
				<tr>
					<th>处理人：</th>
					<td>
						<fhd:empSelectorBpm multiple="false" require="true" radioType="43" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 处长、部门领导 -->
					</td>
				</tr>
		</c:if>
		<c:if test="${(taskName eq '部门人员审核预警通知单') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="46" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm>
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批预警通知单') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="45" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员、退回 -->
			    </td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '发布预警通知单') }">
		    <tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="44" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员、退回 -->
			    </td>
			</tr>
		</c:if>
		
		<c:if test="${(taskName eq '文书接收预警通知单') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" radioType="48" defaultOrg="true" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、处长  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '专责填写预警通知单任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="32" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任、文书、退回  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '领导审批预警通知单任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true"  defaultOrg="true" radioType="21" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 主任  -->
				</td>
			</tr>
		</c:if>	
		<c:if test="${(taskName eq '风险管理员审批预警通知单任务') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" require="true" defaultOrg="true" radioType="7" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 回退、风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '经法部领导确认预警通知单') }">
			<tr>
				<th>处理人：</th>
				<td>
					<fhd:empSelectorBpm multiple="false" defaultOrg="true" require="true" radioType="21" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelectorBpm><!-- 风险管理员  -->
				</td>
			</tr>
		</c:if>
		<c:if test="${(taskName eq '风险管理员归档预警通知单') }">
			<tr style="display: none;">
				<th>提醒领导：</th>
				<td>
					<fhd:empSelector multiple="true" attributeName="emps" title="选择员工" checkNode="true" onlyLeafCheckable="true" checkModel="childCascade" showType="textFiledType" defaultOrg="false" style="width:300px;" empfilter="role/ZHUREN"></fhd:empSelector>
				</td>
			</tr>
		</c:if>
		
	</c:if>
	
	
	
