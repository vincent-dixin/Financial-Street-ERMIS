<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
	<title><spring:message code="fhd.sys.orgstructure.emp.emplist"/></title>
	<script type="text/javascript">
		var mv_grid;
		Ext.onReady(function(){
			Ext.QuickTips.init();
	
			mv_grid = new FHD.ext.Grid('emplist',null,
			[ 
				
			   {header: "id", dataIndex: 'id', width: 0},
			   {header: "员工编号", dataIndex: 'empcode', sortable: true, width: 60},
			   {header: "员工姓名", dataIndex: 'empname', sortable: true, width: 60},
			   {header: "所属机构", dataIndex: 'orgname', sortable: true, width: 60},
			   {header: "用户名", dataIndex: 'username', sortable: true, width: 60},
			   {header: "手机号", dataIndex: 'mobikeno', sortable: true, width: 60},
			   {header: "注册时间", dataIndex: 'regdate', sortable: true, width: 60},
			   {header: "状态", dataIndex: 'empStatus', sortable: true, width: 60},
			   {header: "userid", dataIndex: 'userid', width: 0}
			   
			],
			null,false,'100%',325,'${ctx}/sys/orgstructure/emp/queryEmps.do?parentType=${parentType}&id=${id}'
			,false, 
			<c:choose>
			<c:when test="${single eq 'single'}">false</c:when>
			<c:otherwise>true</c:otherwise>
			</c:choose>
			,
			false
			);
			
			//注册选择事件
			//rowselect : ( SelectionModel this, Number rowIndex, Ext.data.Record r ) 
			mv_grid.grid.getSelectionModel().on('rowselect',function(selModel,rowIndex,record){
				var pwind=window.parent.window;
				var chooseValues = pwind.document.getElementById("chooseValues");
				var tags = '${abName}';
				if('null' == tags || undefined == tags){
					tags = 'treeSelectId';
				}
				var nodeId = record.get("userid");
				//已经存在就返回
				var existobj = pwind.document.getElementById(nodeId);
				if(existobj){
					return;
				}
				
				var nodeText = record.get("empname");
				chooseValues.innerHTML +="<span id='"+ record.get("userid") +"'  style=\"width: 175px\"><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + tags + "\",\"" + nodeId + "\",\""+record.get("userid")+"\")' name='" + tags + "' value='"+ nodeId +"' text = '"+nodeText+"' />"+ nodeText + "</span>";

				parentWindow().$("#submits").removeAttr("disabled");  
			});
			
			//注册反选事件
			//rowdeselect : ( SelectionModel this, Number rowIndex, Record record ) 
			mv_grid.grid.getSelectionModel().on('rowdeselect',function(selModel,rowIndex,record){
				var pwind=window.parent.window;
				var chooseValues = pwind.document.getElementById("chooseValues");
				
				var tags = 'treeSelectId';
				var nodeId = record.get("userid");
				pwind.remove(nodeId);
			});
	
			//注册store加载数据后的事件  设置checkbox选中 
			//load : ( Store this, Ext.data.Record[] records, Object options ) 
			mv_grid.grid.store.on('load',function(store,records,options){
				var pwind=window.parent.window;
				var chooseValues = pwind.document.getElementById("chooseValues");
				var els = chooseValues.getElementsByTagName('span');
				
				var sm = mv_grid.grid.getSelectionModel();
				var store = mv_grid.grid.getStore();
				var view = mv_grid.grid.getView();
				for(var i = 0; i < view.getRows().length; i ++){
					var record = store.getAt(i);
					var userid = record.get("userid");
					for(var j=0;j<els.length;j++){
						if(els[j].id==userid){
							sm.selectRow(i,true);
						}
					}
				}
			});
		});

		//打开或关闭查询
		function showAndHide(){
			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
			if(divShows == 'none'){
				document.getElementById("rmRiskTaxisDivId").style.display='';	
				mv_grid.grid.setHeight(325-jQuery("#rmRiskTaxisDivId").height());
			}else{
				document.getElementById("rmRiskTaxisDivId").style.display='none';
				mv_grid.grid.setHeight(325);
			}
		}
		
		//查询
		function queryEmp(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
	    		empcode: document.getElementById("empcode").value,
	    		empname: document.getElementById("empname").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
		//取消选中的行
		function cancelSelect(nodeId){
			mv_grid.grid.getSelectionModel().clearSelections();
			var rows = mv_grid.grid.getId(nodeId);
			mv_grid.grid.getSelectionModel().selectRecords(rows,false);
		}
		Ext.EventManager.onWindowResize(function(width ,height){
   			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
   			if(divShows=='none'){
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(325-jQuery("#rmRiskTaxisDivId").height());
   			}else{
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(325);
   			}
		});
	</script>
</head>
<body style="margin: 0;border: 0;padding: 0;" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif" onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
		</tr>
	</table>
	<div id="rmRiskTaxisDivId" style="display:none">
		<form id="rmRiskForm" name="rmRiskForm" method="post">
			<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;" >
				<tr>
					<th><spring:message code="fhd.sys.orgstructure.emp.empcode" />：</th>
					<td><input type="text" name="filter_LIKES_empcode" id="empcode" value="${param.empcode}" /></td>
					<th><spring:message code="fhd.sys.orgstructure.emp.empname" />：</th>
					<td><input type="text" name="filter_LIKES_empname" id="empname" value="${param.filter_LIKES_empname}" /></td>
				</tr>
				<tr>
					<th colspan="4" align="center">
						<input type="button" onclick="queryEmp();" class="fhd_btn" value="<spring:message code="fhd.common.search" />" />
					</th>
				</tr>
			</table>	
		</form>
	</div>
	<div id="emplist" style="width:100%"></div>
</body>
</html>