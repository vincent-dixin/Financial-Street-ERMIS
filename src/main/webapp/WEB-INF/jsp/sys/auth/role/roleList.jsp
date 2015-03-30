<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.role.role"/></title>
	<script type="text/javascript">
		var mv_grid;
		var currentRoleId;
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
					{text: '<spring:message code="fhd.common.add" />',handler:saveRole,iconCls:'icon-add'},'-',
					{text: '<spring:message code="fhd.common.modify" />',id:'edit',handler:updateRole,iconCls:'icon-edit',disabled:true},'-',
					{text: '<spring:message code="fhd.common.delete" />',id:'del',handler:removeRole,iconCls:'icon-del',disabled:true},'-',
					{text: '<spring:message code="fhd.common.query" />',id:'adquery',handler:showAndHide,iconCls:'icon-zoom'}
				]
			});
		
			
			mv_grid = new FHD.ext.Grid('rolelist',{},
				[   
					{header: "<spring:message code='fhd.sys.auth.role.roleCode'/>", dataIndex: 'roleCode', sortable: true, width: 40,renderer:function(value, metaData, record, rowIndex, colIndex, store){
			        	   return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div>";
					  }}, 
					{header: "<spring:message code='fhd.sys.auth.role.roleName' />", dataIndex: 'roleName', sortable: true, width: 40,
							renderer:function(value,metaData,record,rowIndex,colIndex,store){return "<div ext:qtitle='' ext:qtip='"+value+"'>"+value+"</div>";}}, 
					{header: "<spring:message code='fhd.common.operate' />", dataIndex:'id',width:20,renderer:function(val){
						return "<a href='javascript:void(0)' onclick='javascript:roleAssignAuthority(\""+val+"\");'><spring:message code='fhd.sys.auth.authority.assignAuthority'/></a>"
						+"&nbsp;||&nbsp;"
						+"<a href='javascript:void(0)' onclick='javascript:roleAssignEmp(\""+val+"\");'>分配人员</a>"
						+"&nbsp;||&nbsp;"
						+"<a href='javascript:void(0)' onclick='javascript:roleAssignIndex(\""+val+"\");'>分配首页</a>";
					}},
					{header: "id", dataIndex: 'id', hidden:true}				   
				],
				null,false,widthBody, heightBody,'${ctx}/sys/auth/role/roleList.do'
				,false,true,
				toolbar
			);
			mv_grid.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==1){
		    		 Ext.getCmp("edit").enable();
		    	}else{
		    		Ext.getCmp("edit").disable();
		    	}
		        Ext.getCmp("del").enable();
			});
			mv_grid.grid.getSelectionModel().on('rowdeselect',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==0){
		    		 Ext.getCmp("edit").disable();
		    		 Ext.getCmp("del").disable();
		    	}else if(rows.length!=1){
		    		Ext.getCmp("edit").disable();
		    	}else if(rows.length==1){
		    		Ext.getCmp("edit").enable();
		    	}
			});
			//打开或关闭高级查询
			function showAndHide(){
				var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
				if(divShows == 'none'){
					document.getElementById("rmRiskTaxisDivId").style.display='';
					mv_grid.grid.getTopToolbar().get("adquery").setText('<spring:message code="fhd.common.hide" />');	
					var divHeight=Ext.get("rmRiskTaxisDivId").getHeight();
					var height=heightBody-divHeight;
					mv_grid.grid.setHeight(height);			
				}else{
					document.getElementById("rmRiskTaxisDivId").style.display='none';
					mv_grid.grid.getTopToolbar().get("adquery").setText('<spring:message code="fhd.common.query" />');	
					mv_grid.grid.setHeight(heightBody);
					}
			}
		});
		
		//保存角色
		function saveRole(){
			var ret = FHD.openWindow("<spring:message code='fhd.common.addRole' />", 500,106, "${ctx}/sys/auth/roleAdd.do",'no');
			
		}
		//更新角色
		function updateRole(){
			//var id = document.getElementsByName("id");
			
			var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />", '<spring:message code="fhd.common.updateSelect"/>'); 
				return;
			}
			if(rows.length>1){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />",'<spring:message code="fhd.common.updateTip"/>');
				return;
			}
			var id=rows[0].get("id");
			var ret = FHD.openWindow("<spring:message code='fhd.common.editRole' />", 510,106, "${ctx}/sys/auth/roleMod.do?id="+id,'no');
		}
		//删除角色
		function removeRole(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />", '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
			
			Ext.MessageBox.confirm("<spring:message code='fhd.common.prompt' />","<spring:message code='fhd.common.makeSureDelete' />",function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++){
						ids+=rows[i].get('id')+',';
					}
					FHD.ajaxReq('${ctx}/sys/auth/roleDel.do?orgId=${id}',{ids:ids},function(data){
						if(data!='true'){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />","<spring:message code='fhd.common.roleFaile' />");
						}else{ 
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />","<spring:message code='fhd.common.operateSuccess' />");
							mv_grid.grid.store.reload();
							Ext.getCmp("edit").disable();
				    		Ext.getCmp("del").disable();
						}
					});
				}else{
					return;
				}
			});
		}
		//分配权限
		function roleAssignAuthority(idValue){
			FHD.openWindow("<spring:message code='fhd.sys.auth.authority.assignAuthority' />", 800,466, "${ctx}/sys/auth/roleAssignAuthority.do?id="+idValue,'no');
			return true;
		}
		//选择人员弹出窗口
		function selectEmpWindow(callback,params){
			openWindow('${empselectorwindow.title}',606,400,contextPath+"/components/emp/jstreepage.do?callback="+callback+'&'+params);
		}
		//分配人员
		function roleAssignEmp(idValue){
			currentRoleId=idValue;
			var selects;
			
			//同步获取已经选择的人员IDs
			$.ajax({
				url: "${ctx}/sys/auth/roleAssignedUser.do?id="+idValue,
			  	cache: false,
			  	async:false,
			  	success: function(resultIds){
			  		selects=resultIds;
			  	}
			});
		
			selectEmpWindowEx('emphandler','selects='+selects);
			//selectEmpWindow('emphandler','selects='+selects);
			return true;
		}
		//后台操作分配人员
		function emphandler(selects){
			var selectIds='';
			var id=currentRoleId;
			for(var i=0;i<selects.length;i++){
				selectIds=selectIds+selects[i].id+',';
			}
			$.ajax({
				type: "GET",
				url: "${ctx}/sys/auth/roleAssignUserSubmit.do",
				data: "roleid="+id+"&selectIds="+selectIds,
				success: function(msg){
					if("true"==msg){
						window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />", '<spring:message code="fhd.common.operateSuccess"/>'); 
						closeWindow();
					}else{
						window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />", '<spring:message code="fhd.common.operateFailure"/>');
						closeWindow();
					}
				}
			});
		}
		//分配首页
		function roleAssignIndex(id){
			Ext.Ajax.request({
				url:'${ctx}/sys/auth/roleAssignIndex.do',
			    method:'post',
			    params:{
					roleId:id
			    },
			    success:function(response){
			      	if(response.responseText=='true'){
			      		FHD.openWindow('角色分配首页布局', 800, 500, '${ctx}/sys/portal/roleAssignPortalManage.do?roleId='+id,'no');
				    }else if(response.responseText=='false'){
				    	window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />","<spring:message code='fhd.common.operateFailure' />");
				    }else{
				    	window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt' />",response.responseText);
				    }
			    }
			});
		}
		//查询
		function queryRole(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
		    		roleCode: document.getElementById("roleCode").value,
		    		roleName: document.getElementById("roleName").value
			};
	   		mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
		//监听回车事件
		function keypup(event){
			if(event.keyCode==13){
				queryRole();
			}
		}
		Ext.EventManager.onWindowResize(function(width,height){
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
		<form id="sysRoleForm" name="sysRoleForm" onkeypress="keypup(event);" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" >
				<tr><td class="fhd_query_title" >&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide(this)" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0"
				class="fhd_query_table" style="display:block;">
				<tr>  
					<th><spring:message code="fhd.sys.auth.role.roleCode"/>：</th>
					<td><input type="text" name="roleCode" id="roleCode" value="${param.roleCode}"/></td>                                             
					<th><spring:message code="fhd.sys.auth.role.roleName"/>：</th>
					<td><input type="text" name="roleName" id="roleName" value="${param.roleName}"/></td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input type="button" onclick="queryRole();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<div id="rolelist" style="margin:0px;padding:0px;"></div>
</body>
</html>