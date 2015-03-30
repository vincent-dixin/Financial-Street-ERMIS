<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><spring:message code="fhd.sys.orgstructure.org.orglist" /></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody;
		Ext.onReady(function(){
			heightBody=Ext.getBody().getHeight();
			width=Ext.getBody().getWidth(); 
			Ext.QuickTips.init();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: "<spring:message code='fhd.common.add'/>",handler:saveEmp,iconCls:'icon-add'},'-',
					{text: "<spring:message code='fhd.common.modify'/>",id:'edit',handler:updateEmp,iconCls:'icon-edit' ,disabled:true},'-',
					{text: "<spring:message code='fhd.common.delete'/>",id:'del',handler:removeEmp,iconCls:'icon-del',disabled:true},'-',
					{text: "<spring:message code='fhd.common.out'/>",handler:exportData,iconCls:'icon-folder-go'},'-',
					{text: "<spring:message code='fhd.common.query'/>",handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}
				]
			});
			
			mv_grid = new FHD.ext.Grid('orglist',{riskId:5},
				[   
					{header: "id", dataIndex: 'id', width: 0},
					{header: "<spring:message code='fhd.sys.orgstructure.org.orgcode'/>", dataIndex: 'orgcode', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.orgName'/>", dataIndex: 'orgname', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.orgType'/>", dataIndex: 'orgType', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.parentorgname'/>", dataIndex: 'parentOrg', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.orgaddress'/>", dataIndex: 'address', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.linkMan'/>", dataIndex: 'linkMan', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.linktel'/>", dataIndex: 'linkTel', sortable: true, width: 60}, 
					{header: "<spring:message code='fhd.sys.orgstructure.org.orgstatus'/>", dataIndex: 'orgStatus', sortable: true, width: 60,
						renderer:function(value, metaData, record, rowIndex, colIndex, store){
							if("1"==value){
								return "<spring:message code='fhd.common.normal'/>";
							}else{
								return "<spring:message code='fhd.common.cancellation'/>";
							}
						}
					}
				   
				],
				null,false,width,heightBody,'${ctx}/sys/orgstructure/org/querynextorg.do?id=${id}'
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
		//刷新
		function reloadGrid(){
			mv_grid.grid.store.reload({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});
		}
		//保存
		function saveEmp(){
			FHD.openWindow("<spring:message code='fhd.common.add'/>", 840,340, "${ctx}/sys/orgstructure/org/add.do?operation=page&id=${param.id}0000",'no');
		}
		//更新
		function updateEmp(){
			var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.updateSelect"/>'); 
				return;
			}
			if(rows.length>1){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.updateTip"/>');
				return;
			}
			var id=rows[0].get("id");
			var ret = FHD.openWindow("<spring:message code='fhd.common.modify'/>", 820,340, "${ctx}/sys/orgstructure/org/update.do?operation=page&id="+id,'no');
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
					for(var i=0;i<rows.length;i++){
						ids+=rows[i].get('id')+',';
					}
					FHD.ajaxReq('${ctx}/sys/orgstructure/org/delete.do?operation=page&parentid=${id}',{id:ids},function(data){
						if(data=='true'){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
							mv_grid.grid.store.reload();
							parent.parent.selectNodeReload();
						}else{ 
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",data);
						}
					});
				}else{
					return;
				}
			});
		}
		//导出数据
		function exportData(){
			var url= '${ctx}/sys/orgstructure/org/exportData.do?1=1';
			var orgid = document.getElementById("id").value;
			var orgcode = document.getElementById("orgcode").value;
			var orgname = document.getElementById("orgname").value;
			if(null != orgid && "" != orgid){
				url += "&orgid="+orgid;
			}
			if(null != orgcode && "" != orgcode){
				url += "&orgcode="+orgcode;
			}
			if(null != orgname && "" != orgname){
				url += "&orgname="+orgname;
			}
			window.location.href = url;
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
	    		orgcode: document.getElementById("orgcode").value,
	    		orgname: document.getElementById("orgname").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}

		function keypup(){
			if(event.keyCode==13){
				queryEmp();
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
<body style="overflow:hidden">
	<div id="rmRiskTaxisDivId" style="display:none;">
		<form id="orgForm" name="orgForm" method="post" action="" onkeypress="keypup();">
			<input type="hidden" name="id" id="id" value="${param.id}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0"
				cellspacing="0" class="fhd_query_table" style="display: block;">
				<tr>
					<th><spring:message code="fhd.sys.orgstructure.org.orgcode" />：</th>
					<td><input type="text" name="filter_LIKES_orgcode" id="orgcode"
						value="${param.filter_LIKES_orgcode}" /></td>
					<th><spring:message code="fhd.sys.orgstructure.org.orgName" />：</th>
					<td><input type="text" name="filter_LIKES_orgname" id="orgname"
						value="${param.filter_LIKES_orgname}" /></td>
				</tr>
				<tr>
					<td align="center" colspan="4"><input type="button" onclick="queryEmp();"
						value="<spring:message code='fhd.common.search' />" class="fhd_btn" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="orglist"></div>
	<select id="orgStatus" style="display:none">
		<option vaule="1"><spring:message code="fhd.common.normal"/></option>
		<option vaule="0"><spring:message code="fhd.common.cancellation"/></option>
	</select>
</body>
</html>