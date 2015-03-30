<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/style.css"/> 
<title>个人历史工作列表</title>
<script type="text/javascript">
var mv_grid;
Ext.onReady(function(){
	
	var toolbar = new Ext.Toolbar(
		{height: 30,hideBorders: true,
			buttons : [{
				text : '高级查询>>',
				handler : showAndHide,
				iconCls:'icon-zoom'
			} ]});

	var hs = [
			{header:'名称',dataIndex:'bName',width:30},			
			{header:'活动名称',dataIndex:'activityName',width:20},
			{header:'类型',dataIndex:'bType',width:20},
			{header:'开始时间',dataIndex:'createTime',width:20},
			{header:'结束时间',dataIndex:'endTime',width:20},
			{dataIndex:'id',width:0}
		];
	mv_grid = new FHD.ext.Grid('hisGridId',{},hs,null,null,document.body.offsetWidth,500,'${ctx}/jbpm/getHisTaskList.do',null,false,toolbar).grid;
});

	
	function queryTask(){
		 mv_grid.store.baseParams = {
		  			name: document.getElementById("name").value,
		    		type: document.getElementById("type").value	
		    		
				};
	    // 数据重新加载
	    mv_grid.store.load({
	    	params:{
		    		start:0,
		    		limit:20		    		
		    	}
			});
	}
	
	
	//打开或关闭高级查询
	function showAndHide(){
		var divShows = document.getElementById("hisEventDivId").style.display;
		if(divShows == 'none'){
			document.getElementById("hisEventDivId").style.display='';			
		}else{
			document.getElementById("hisEventDivId").style.display='none';
		}
	}
	
	function keypup(){
		if(event.keyCode==13){
			queryTask();
			return false;
		}
	}
    
    function reloadStore(){
    	hisDs.reload();
    }

    </script>

</head>
<body onload="showAndHide();">
	<div id="hisEventDivId" style="height:6px; background:#f9f9f9;">
		<form id="riskFormId" onkeypress="return keypup();" action="${ctx}/jbpm/getHisTaskList.do" method="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" />
				<spring:message code="fhd.common.querycondition"></spring:message>
				</td></tr>
			</table>
			<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0"
					class="fhd_query_table" style="display:block;" >
					<tr>
						<th>名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</th>
						<td><input type="text" id="name" name="name" value="${param.name}" style="width:150px"/></td>
						<th>代办任务类型：</th>
						<td>
							<select style="width:160;" id="type" name="type" >
					    		<option value="all"				>所有</option>
					    		<option	value="risk"			>风险</option>
					    		<option value="riskhistory"		>风险案例</option>
					    		<option	value="riskevent"		>风险事件</option>
					    		<option value="question"		>问卷</option>
					    		<option value="kpi_plan"		>监控计划</option>
					    		<option value="kpi_target"		>指标采集</option>
					    	</select>
						</td>
		
		            </tr>
		            
					<tr>
						<td colspan="4" align="center">
							<input type="button" onclick="queryTask();" value="<spring:message code="fhd.common.query" />" class="fhd_btn" />
						</td>
						
					</tr>
			</table>
		</form>
	</div>
	<div id="hisGridId" style="overflow:auto;width:100%;">
	</div>	
</body>
</html>

