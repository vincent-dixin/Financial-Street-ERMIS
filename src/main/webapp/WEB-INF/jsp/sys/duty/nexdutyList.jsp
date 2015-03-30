<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.duty.duty" /></title>
	<script type="text/javascript">
	
		var mv_grid;
		var heightBody;
		var widthBody;
		Ext.onReady(function(){
			heightBody=Ext.getBody().getHeight();
			widthBody=Ext.getBody().getWidth();
			Ext.QuickTips.init();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: '添加',handler:saveDuty,iconCls:'icon-add'},'-',
					{text: '修改',id:'edit',handler:updateDuty,iconCls:'icon-edit',disabled:true},'-',
					{text: '删除',id:'del',handler:removeDuty,iconCls:'icon-del',disabled:true},'-',
					{text: '查询',handler:showAndHide,id:'adquery',iconCls:'icon-zoom'}
				]
			});
			mv_grid = new FHD.ext.Grid('datalist',{riskId:5},
				[   
					{header: "id", dataIndex: 'id', width: 0},
					{header: '职务名称', dataIndex: 'dutyName', sortable: true, width: 60}, 
					{dataIndex:'id',width:0}
				   
				],
				null,false,widthBody,heightBody,'${ctx}/sys/duty/nexDuty.do?id=${param.id}'
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

		function saveDuty(){
		 	FHD.openWindow('新建', 700,192, "${ctx}/sys/duty/addPage.do?operation=page&node=${param.id}0000",'no');
		}
		
		function updateDuty(){
			var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg('信息提示', '<spring:message code="fhd.common.updateSelect"/>'); 
				return;
			}
			if(rows.length>1){
				window.top.Ext.ux.Toast.msg('信息提示','<spring:message code="fhd.common.updateTip"/>');
				return;
			}
			var id=rows[0].get("id");
			var ret = FHD.openWindow('更新', 700,192, "${ctx}/sys/duty/editPage.do?operation=page&id="+id,'no');
		/*
			if (grid.getSelectionModel().hasSelection() && grid.getSelectionModel().getSelections().length==1){
				var rows = grid.getSelectionModel().getSelections();
				window.showModalDialog("${ctx}/sys/duty/editPage.do?node="+rows[0].get('id'),window,"dialogWidth:800px;dialogHeight:248px,center:yes,resizable:no,status:no");
				window.location.reload();
			}else
				Ext.MessageBox.alert("","请选择一条要删除的记录");
		*/	
		}
		
		function removeDuty(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg('信息提示', '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
		
			Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++){
						ids+=rows[i].get('id')+',';
					}
					FHD.ajaxReq('${ctx}/sys/duty/delete.do',{ids:ids},function(data){
						if(data!='true'){
							window.top.Ext.ux.Toast.msg('提示','操作失败!');
						}else{ 
							window.top.Ext.ux.Toast.msg('提示','操作成功!');
							mv_grid.grid.store.reload();
							//window.parent.parent.selectNodeParentReoload();
						}
					});
				}else{
					return;
				}
			});
		}

		//打开或关闭高级查询
		function showAndHide(){
			var heightBody  = Ext.getBody().getHeight();
			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
			var height1 = heightBody-Ext.get("rmRiskTaxisDivId").getHeight();
			if(divShows == 'none'){
				document.getElementById("rmRiskTaxisDivId").style.display='';
				mv_grid.grid.getTopToolbar().get("adquery").setText("隐藏");	
				var divHeight=Ext.get("rmRiskTaxisDivId").getHeight();
				var height=heightBody-divHeight;
				mv_grid.grid.setHeight(height);		
			}else{
				document.getElementById("rmRiskTaxisDivId").style.display='none';
				mv_grid.grid.getTopToolbar().get("adquery").setText("查询");
				mv_grid.grid.setHeight(heightBody);
			}
		}
		//查询
		function queryData(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
		    	dutyName: document.getElementById("dutyName").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}

		//刷新
		function reloadGrid(){
			mv_grid.grid.store.reload({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
	//		
	function keypup(){
		if(event.keyCode==13){
			queryData();
			return;
		}
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
<body >
	<div id="rmRiskTaxisDivId" style="display:none;">
		<form id="dutyForm" name="dutyForm" onkeypress="keypup();">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide(this)" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0"
				class="fhd_query_table" style="display:block;">
				<tr>  
					<th>职务名称：</th>
					<td><input type="text" name="dutyName" id="dutyName" /></td>                                             
					<td><input type="text" name="data" id="data" style="width: 0" /></td>                                             
					
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input type="button" onclick="queryData();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<div id="datalist"></div>
</body>
</html>