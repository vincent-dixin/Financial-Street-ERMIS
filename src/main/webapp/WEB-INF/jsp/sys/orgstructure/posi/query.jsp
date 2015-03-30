<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.posi.posilist" /></title>
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
					{text: "<spring:message code='fhd.common.add'/>",handler:saveEmp,iconCls:'icon-add'},'-',
					{text: "<spring:message code='fhd.common.modify'/>",handler:updateEmp,iconCls:'icon-edit',id:'edit',disabled:true},'-',
					{text: "<spring:message code='fhd.common.delete'/>",handler:removeEmp,iconCls:'icon-del',id:'del',disabled:true},'-',
					//{text: '导出',handler:exportData,iconCls:'icon-folder-go '},'-',
					{text: "<spring:message code='fhd.common.query'/>",handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}
				]
			});
		
			mv_grid = new FHD.ext.Grid('poislist',{riskId:5},
				[   
					{header: "id", dataIndex: 'id', width: 0},
					{header: "<spring:message code='fhd.sys.orgstructure.posi.posicode'/>", dataIndex: 'posicode', sortable: true, width: 60},
					{header: "<spring:message code='fhd.sys.orgstructure.posi.posiname'/>", dataIndex: 'posiname', sortable: true, width: 60},
					{header: "<spring:message code='fhd.sys.orgstructure.emp.parentOrgname'/>", dataIndex: 'sysOrganization', sortable: true, width: 60},
					{header: "<spring:message code='fhd.common.effectdate'/>", dataIndex: 'startDate', sortable: true, width: 60},
					{header: "<spring:message code='fhd.common.abatedate'/>", dataIndex: 'endDate', sortable: true, width: 60},
					{header: "<spring:message code='fhd.common.status'/>", dataIndex: 'posiStatus', sortable: true, width: 60,
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
							if("1"==value){
								return "<spring:message code='fhd.common.normal'/>";
							}else{
								return "<spring:message code='fhd.common.cancellation'/>";
							}
						}},
					{header:"<spring:message code='fhd.common.operate'/>",dataIndex:'id',width:120,renderer:function(val){
						return "<a href='javascript:void(0)' onclick='javascript:posiAssignAuthority(\""+val+"\");'><spring:message code="fhd.sys.auth.authority.assignAuthority"/></a>"
								+"&nbsp;||&nbsp;"
								+"<a href='javascript:void(0)' onclick='javascript:posiAssignRole(\""+val+"\");'><spring:message code="fhd.sys.auth.user.assignRole"/></a>"
						}}
				],
				null,false,width,heightBody,'${ctx}/sys/orgstructure/posi/queryNextPosiList.do?id=${id}'
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

		//保存
		function saveEmp(){
			//匹配controler的方法
			FHD.openWindow("<spring:message code='fhd.sys.orgstructure.posi.addposi'/>", 800,215, "${ctx}/sys/orgstructure/posi/add.do?operation=page&id=${param.id}",'no');
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

			var ret = FHD.openWindow("<spring:message code='fhd.sys.orgstructure.posi.editposi'/>", 800,215, "${ctx}/sys/orgstructure/posi/update.do?id="+id,'no');
			
		}
		
		//删除数据
		function removeEmp(){
		
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
			
			Ext.MessageBox.confirm("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.makeSureDelete'/>",function(btn){
				if(btn=='yes')
				{
					var ids = '';
					for(var i=0;i<rows.length;i++)
						ids+=rows[i].get('id')+',';

					FHD.ajaxReq('${ctx}/sys/orgstructure/posi/delete.do?orgid=${id}',{id:ids},function(data){
						if(data!='true')
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
						else 
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
						    window.parent.parent.selectNodeReload();
						    mv_grid.grid.store.reload();
							
					});
				}
				else
				{
					return;
				}
			});
			
		}
		//导出
		function exportData(){

		}
		
		//打开或关闭高级查询
		function showAndHide(){
			var heightBody  = Ext.getBody().getHeight();
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
	    		posicode: document.getElementById("posicode").value,
	    		posiname: document.getElementById("posiname").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}

		function posiAssignAuthority(idValue){
			//alert(idValue);
			//清空选择的用户.
			$("[name=id]:checkbox").each(function() {
				$(this).attr("checked", false); 
			});
			var s = document.getElementsByName("id");
		    for(var i=0;i <s.length;i++){ 
				if(idValue == s[i].value){
					//选中用户
					s[i].checked=true;
				}
		    }
	
			openWindow('岗位<spring:message code="fhd.sys.auth.authority.assignAuthority"/>',800,500,"${ctx}/sys/orgstructure/posiAssignAuthority.do?id="+idValue);
		}

		function posiAssignRole(idValue){
			//alert(idValue);
			//清空选择的用户.
			$("[name=id]:checkbox").each(function() {
				$(this).attr("checked", false); 
			});
			var s = document.getElementsByName("id");
		    for(var i=0;i <s.length;i++){ 
				if(idValue == s[i].value){
					//选中用户
					s[i].checked=true;
				}
		    }
	
			openWindow('岗位<spring:message code="fhd.sys.auth.user.assignRole"/>',820,550,"${ctx}/sys/orgstructure/posiAssignRole.do?id="+idValue);
		}
		Ext.EventManager.onWindowResize(function(width ,height){
   			var height1 = heightBody-Ext.get("rmRiskTaxisDivId").getHeight();
   			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
   			if(divShows =='none'){
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
		<form id="posiForm" name="posiForm" method="post">
			<input type="hidden" name="id" value="${param.id}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="fhd_query_title">&nbsp;<img
						src="${ctx}/images/plus.gif" onclick="showAndHide();" width="15"
						height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
				</tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display: block;">
				<tr>
					<th><spring:message code="fhd.sys.orgstructure.posi.posicode" />：</th>
					<td><input type="text" name="posicode" id="posicode"
						value="${param.filter_LIKES_posicode}" /></td>
					<th><spring:message code="fhd.sys.orgstructure.posi.posiname" />：</th>
					<td><input type="text" name="posiname" id="posiname"
						value="${param.filter_LIKES_posiname}" /></td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" onclick="queryEmp();"
						class="fhd_btn" value="<spring:message code="fhd.common.search" />" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="poislist"></div>
	<select id="posiStatus" style="display:none">
		<option vaule="1"><spring:message code="fhd.common.normal"/></option>
		<option vaule="0"><spring:message code="fhd.common.cancellation"/></option>
	</select>
	<c:if test="${not empty success}">
		<script type="text/javascript">
			alert('${success}');
			parent.parent.selectNodeReload();
		</script>
	</c:if>
</body>
</html>