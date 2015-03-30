<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.emp.emplist"/></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody;
		var width;
		Ext.onReady(function(){
			Ext.QuickTips.init();
			heightBody=Ext.getBody().getHeight();
			width=Ext.getBody().getWidth();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: "<spring:message code='fhd.common.addEmp'/>",handler:saveEmp,iconCls:'icon-add'},'-',
					{text: "<spring:message code='fhd.common.modify'/>",handler:updateEmp,iconCls:'icon-edit',id:'edit',disabled:true},'-',
					{text: "<spring:message code='fhd.common.delete'/>",handler:removeEmp,iconCls:'icon-del',id:'del',disabled:true},'-',
					//{text: '导出',handler:exportData,iconCls:'icon-folder-go'},'-',
					{text: "<spring:message code='fhd.common.query'/>",handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}
				]
			});
			
			mv_grid = new FHD.ext.Grid('emplist',{riskId:5},
				[ 
				   {header: "id", dataIndex: 'id', width: 0},
				   {header: "<spring:message code='fhd.sys.orgstructure.emp.empcode'/>", dataIndex: 'empcode', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.sys.orgstructure.emp.empname'/>", dataIndex: 'empname', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.sys.orgstructure.org.parentComp'/>", dataIndex: 'orgname', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.sys.auth.user.username'/>", dataIndex: 'username', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.sys.orgstructure.emp.mobikeno'/>", dataIndex: 'mobikeno', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.common.regdate'/>", dataIndex: 'regdate', sortable: true, width: 60},
				   {header: "<spring:message code='fhd.common.status'/>", dataIndex: 'empStatus', sortable: true, width: 60,
						renderer:function(value, metaData, record, rowIndex, colIndex, store){
							if("1"==value){
								return "<spring:message code='fhd.common.normal'/>";
							}else{
								return "<spring:message code='fhd.common.cancellation'/>";
							}
						}
				   }
				   
				],
				null,false,width,heightBody,'${ctx}/sys/orgstructure/emp/queryNextonlyEmpList.do?id=${id}'
				,false,true,
				toolbar
			);
			mv_grid.grid.getSelectionModel().on('selectionchange',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==1){
		    		 Ext.getCmp("edit").enable();
		    	}else{
		    		Ext.getCmp("edit").disable();
		    	}
		        Ext.getCmp("del").enable();
		        if(rows.length==0){
		    		 Ext.getCmp("edit").disable();
		    		 Ext.getCmp("del").disable();
		        }
			});
		});

		//导出
		function exportData(){
	
		}
	
		//保存
		function saveEmp(){
			//为了匹配Controler中的g_add方法，在id后加上四位0
			var ret = FHD.openWindow("<spring:message code='fhd.sys.auth.user.addUser'/>", 800,467, "${ctx}/sys/orgstructure/emp/add.do?operation=page&id=${param.id}");
			
			/*var ret = window.showModalDialog("${ctx}/sys/orgstructure/emp/add.do?id=${param.id}",window,"dialogWidth:800px;dialogHeight:458px,center:yes,resizable:no,status:no");
			if(ret == "refresh"){
				window.location.reload();
			}
			parent.parent.selectNodeReload();
			*/
		}
	
		//更新
		function updateEmp(){
	
		    var rows =mv_grid.grid.getSelectionModel().getSelections(); 
				if(rows.length==0){
					window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.updateSelect'/>"); 
					return;
				}
				if(rows.length>1){
					window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.updateTip'/>");
					return;
				}
				var id=rows[0].get("id");
				FHD.openWindow("<spring:message code='fhd.sys.orgstructure.emp.editemp'/>", 850,485, "${ctx}/sys/orgstructure/emp/update.do?id="+id);
			
		}
	

		//删除数据
		function removeEmp(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
			
			Ext.MessageBox.confirm("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.makeSureDelete'/>",function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++)
						ids+=rows[i].get('id')+',';
						
					FHD.ajaxReq('${ctx}/sys/orgstructure/emp/delete.do?orgId=${id}',{id:ids},function(data){
						if(data!='true')
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
						else 
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
							mv_grid.grid.store.reload();
					});
				}else{
					return;
				}
			});
		}
	
		//打开或关闭高级查询
		function showAndHide(){
			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
			if(divShows == 'none'){
				document.getElementById("rmRiskTaxisDivId").style.display='';
				mv_grid.grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.hide'/>");
				var divHeight=Ext.get("rmRiskTaxisDivId").getHeight();
				var height=heightBody-divHeight;
				mv_grid.grid.setHeight(height);			
			}else{
				document.getElementById("rmRiskTaxisDivId").style.display='none';
				mv_grid.grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.query'/>");
				mv_grid.grid.setHeight(heightBody);
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
		Ext.EventManager.onWindowResize(function(width ,height){
   			var height1 = heightBody-Ext.get("rmRiskTaxisDivId").getHeight();
   			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
   			if(divShows=='none'){
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(height);
   			}else{
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(height1);
   			}
        });
	</script>
</head>
<body style="overflow:hidden;">
	<div id="rmRiskTaxisDivId" style="display:none;">
		<form id="rmRiskForm" name="rmRiskForm" method="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
				</tr>
			</table>
			<table id="showTable" border="0" width="100%" cellpadding="0" cellspacing="0"
					class="fhd_query_table" style="display:block;" >
				<tr>
					<th><spring:message code="fhd.sys.orgstructure.emp.empcode" />：</th>
					<td><input type="text" name="filter_LIKES_empcode" id="empcode"
						value="${param.empcode}" /></td>
					<th><spring:message code="fhd.sys.orgstructure.emp.empname" />：</th>
					<td><input type="text" name="filter_LIKES_empname" id="empname"
						value="${param.filter_LIKES_empname}" /></td>
				</tr>
					
				<tr>
					<td align="center" colspan="4" ><input type="button" onclick="queryEmp();"
						class="fhd_btn" value="<spring:message code="fhd.common.search" />" /></td>
				</tr>
			</table>	
		</form>
	</div>
	<div id="emplist"></div>
		<select id="empStatus" style="display:none">
		<option vaule="1"><spring:message code="fhd.common.normal"/></option>
		<option vaule="0"><spring:message code="fhd.common.cancellation"/></option>
	</select>
</body>
</html>