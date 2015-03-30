<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
	<title><spring:message code="fhd.sys.orgstructure.emp.ownerOrgShip"/></title>
</head>
<body>
	<form id="empForm" name="empForm" action="${ctx}/sys/orgstructure/emp/empAssignShipSubmit.do" method="post">
		<input type="hidden" id="id" name="id" value="${param.id}"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F5F5" class="fhd_border_table">
			<tr>
				<td>
					<select name="isMainOrg" id="isMainOrg" style="display: none;"> 
					      <option value="是">"<spring:message code='fhd.common.true'/>"</option>
					      <option value="否">"<spring:message code='fhd.common.false'/>"</option>
					</select> 
					<div id="mainOrg-grid"></div>
				</td>
			</tr>
		</table>
		
		<script type="text/javascript">
			var orgGrid;
			var heightBody=Ext.getBody().getHeight();
			Ext.onReady(function(){ 
			    Ext.QuickTips.init();

			    // 弄个缩写的别名 
			    var fm = Ext.form; 
	
			    // 列模型定义了表格所有列的信息, 
			    // dataIndex 将特定的列映射到数据源(Data Store)中的数据列
			    var cm = new Ext.grid.ColumnModel([ 
			    	new Ext.grid.RowNumberer({header:'${grid.seq}',width:20}),
			        { 
			           id:'id', 
			           header: "id", 
			           dataIndex: 'id',
			           sortable: true, 
			           width: 0
			        },
			        { 
			           id:'orgName', 
			           header: "<spring:message code='fhd.sys.orgstructure.org.orgName'/>", 
			           dataIndex: 'orgName',
			           sortable: true,
			           width: 150//, 
			           //editor: new fm.TextField({ 
			           //    allowBlank: false
			           //}) 
			        }, 
			        { 
			           header: "<spring:message code='fhd.sys.orgstructure.org.mainOrg'/>",
			           dataIndex: 'isMainOrg',
		        	   sortable: true,
			           width: 100, 
			           editor: new Ext.form.ComboBox({
			               typeAhead: true, 
			               triggerAction: 'all', 
			               transform:'isMainOrg',// 对应的选择框的ID 
			               lazyRender:true,
			               listClass: 'x-combo-list-small'
			            })
			        }, 
			        { 
			        	id:'orgType', 
				        header: "<spring:message code='fhd.sys.orgstructure.org.orgType'/>", 
				        dataIndex: 'orgType',
				        sortable: true,
				        width: 150
				    }
			    ]); 
	
			    // 默认情况下列是可排序的 
			    cm.defaultSortable = true; 
	
			    // 定义一个员工岗位对象,这样便于我们动态的添加记录,虽然也可以设置成匿名内置对象 
			    var EmpOrg = Ext.data.Record.create([ 
			           // 下面的 "name" 匹配读到的标签名称
			           {name: 'id', type: 'String' }, 
			           {name: 'orgName', type: 'string'}, 
			           {name: 'isMainOrg', type: 'String'},
			           {name: 'orgType', type: 'String'} 
			      ]); 

			    var proxy = new Ext.data.HttpProxy({
			    	method:'POST',
			    	url:'${ctx}/sys/orgstructure/emp/queryEmpOrg.do?empid=${param.id}'
			    });

			    var reader = new Ext.data.JsonReader({
			    	idProperty: 'id', //json数据中，记录中主键所对应的列的属性名
			        root: 'datas', //json数据中，保存记录集的属性的属性名
			        totalProperty: 'totalCount', //json数据中，保存总记录数的属性
			        fields: ['id', 'orgName', 'isMainOrg', 'orgType']
			    });
			    
			    // 创建 Data Store 
			    var store = new Ext.data.Store({
			    	autoDestroy: true,
			    	proxy: proxy,
			        reader: reader,
			        //data: Ext.grid.dummyData,
			        sortInfo:{field:'orgName', direction:'ASC'}// 排序信息 
			    }); 

			    // 创建编辑器表格 
			    var grid = new Ext.grid.EditorGridPanel({ 
			        store: store,
			        cm: cm, 
			        renderTo: 'mainOrg-grid', 
			        autoWidth:true, 
			        height:heightBody/2, 
			        

			        
			        title:"<spring:message code='fhd.sys.orgstructure.org.mainOrgMan'/>",//标题 
			        tbar:new Ext.Toolbar({
	                      items:[
	                             {
		                    	   text:"<spring:message code='fhd.common.save'/>",
		  			        	   icon: '${ctx}/images/icons/save.gif',    //'${ctx}/images/icons/select.gif',
		  				           handler:orgsave
	                             },{
	                                text:"<spring:message code='fhd.common.del'/>",
	                                iconCls:'icon-del',
	                                handler:orgdel


	                              }]

					     }),
	                      
			        frame:true, 
			        clicksToEdit:1,//设置点击几次才可编辑 
			        selModel: new Ext.grid.RowSelectionModel({
				        singleSelect:false //设置单行选中模式, 否则将无法删除数据
				    }),
		            listeners :{'validateedit' : function(e){
		            	var eid = e.record.get("id");
			        	var list = e.grid.store.data.items;
						for(var i=0;i<list.length;i++){
							if(eid != list[i].data.id && e.record.get("isMainOrg")){
								if(list[i].data.isMainOrg == "<spring:message code='fhd.common.true'/>"){
									alert("<spring:message code='fhd.common.orgExist'/>");
									return false;
								}
							}
						}
		            }}
			        //tbar: false
			        
			    }); 
				//引用机构表
				orgGrid=grid;
				
			    // 单元格编辑后事件处理 
			    //grid.on("afteredit", afterEdit, grid); 
			    // 事件处理函数 
			    function afterEdit(e) {
			    	// 被编辑的记录
			        var record = e.record;
			        //alert(record.get("isMainOrg"));
			        // 显示等待对话框 
			        Ext.Msg.wait("<spring:message code='fhd.common.wait'/>", "<spring:message code='fhd.common.editing'/>", "<spring:message code='fhd.common.operating'/>"); 
			        // 修改${fhd.common.prompt}
			        Ext.Msg.alert('修改主机构', "被修改的机构名称是:" + e.record.get("orgName") + "\n 修改的字段是:"+ e.field);
			    }; 
	
			    // 数据重新加载
			    store.load(); 
			}); 
			
	
			//机构删除
			function orgdel(){
				var rows = orgGrid.getSelectionModel().getSelections();// 返回值为 Record 数组
				if(rows.length==0){ 
					Ext.MessageBox.alert("<spring:message code='fhd.common.alert'/>", "<spring:message code='fhd.common.msgDel'/>"); 
				}else{ 
					Ext.MessageBox.confirm("<spring:message code='fhd.common.alertBox'/>", "<spring:message code='fhd.common.makeSureDelete'/>",function(btn){ 
						if(btn=='yes'){ 
							if(rows){
								var empOrgIds = '';
								for (var i = 0; i < rows.length; i++) {
									//alert(rows[i].get("id"));
									empOrgIds += rows[i].get("id")+",";
									orgGrid.store.remove(rows[i]);
									//store.removeAll(); //删除所有数据 
								}
								// 后台操作更新数据
								Ext.Ajax.request({
									url:'${ctx}/sys/orgstructure/emp/delEmpOrg.do',
								    method:'post',
								    params:{
										empOrgIds: empOrgIds
								    },
								    success:function(response){
								      	if(response.responseText=='true'){
									      	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
									    }else{
									     	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
									    }
								    }
								});
							} 
						} 
					}); 
				}
			}
			
			
				//机构保存
			function orgsave(){
				var empOrgIds = '';
		        var rec = '';
		   	  	var records = orgGrid.store.getModifiedRecords();
		   	  	Ext.each(records,function(m){ 
		   	  		//alert(m.get("id"));
		   	  		//alert(m.get("isMainOrg"));
		   	  		rec = m.get("id")+";"+m.get("isMainOrg");
		   	  		empOrgIds += rec+",";
		   	  	});
		   	 	// 后台操作更新数据
				Ext.Ajax.request({
					url:'${ctx}/sys/orgstructure/emp/updateEmpOrg.do',
				    method:'post',
				    params:{
						empOrgIds: empOrgIds
				    },
				    success:function(response){
				      	if(response.responseText=='true'){
					      	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
					    }else{
					     	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
					    }
				    }
				});
			}
		    Ext.EventManager.onWindowResize(function(width ,height){
		    	orgGrid.setWidth(width);
		    	orgGrid.setHeight(height/2);
		   });
		</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F5F5" class="fhd_border_table">
			<tr>
				<td>
					<select name="isMainPosition" id="isMainPosition" style="display: none;"> 
					      <option value="是">是</option>
					      <option value="否">否</option>
					</select> 
					<div id="mainPosition-grid"></div>
				</td>
			</tr>
		</table>
		<script type="text/javascript">
			var posiGrid;
			Ext.onReady(function(){ 
			    Ext.QuickTips.init();

			    // 弄个缩写的别名 
			    var fm = Ext.form; 
	
			    // 列模型定义了表格所有列的信息, 
			    // dataIndex 将特定的列映射到数据源(Data Store)中的数据列
			    var cm = new Ext.grid.ColumnModel([ 
			    new Ext.grid.RowNumberer({header:'序',width:20}),
			        { 
			           id:'id', 
			           header: "id", 
			           dataIndex: 'id',
			           sortable: true, 
			           width: 0
			        },
			        { 
			           id:'positionName', 
			           header: "岗位名称", 
			           dataIndex: 'positionName',
			           sortable: true,
			           width: 150//, 
			           //editor: new fm.TextField({ 
			           //    allowBlank: false
			           //}) 
			        }, 
			        { 
			           header: "主岗位",
			           dataIndex: 'isMainPosition',
		        	   sortable: true,
			           width: 100, 
			           editor: new Ext.form.ComboBox({
			               typeAhead: true, 
			               triggerAction: 'all', 
			               transform:'isMainPosition',// 对应的选择框的ID 
			               lazyRender:true,
			               listClass: 'x-combo-list-small'
			            })
			        }
			    ]); 
	
			    // 默认情况下列是可排序的 
			    cm.defaultSortable = true; 
	
			    // 定义一个员工岗位对象,这样便于我们动态的添加记录,虽然也可以设置成匿名内置对象 
			    var EmpPosi = Ext.data.Record.create([ 
			           // 下面的 "name" 匹配读到的标签名称
			           {name: 'id', type: 'String' }, 
			           {name: 'positionName', type: 'string'}, 
			           {name: 'isMainPosition', type: 'String'}
			           //{name: 'birthDay', mapping: 'birth', type: 'date', dateFormat: 'Y/m/d'}, 
			      ]); 

			    var proxy = new Ext.data.HttpProxy({
			    	method:'POST',
			    	url:'${ctx}/sys/orgstructure/emp/queryEmpPosiJson.do?empid=${param.id}'
			    });

			    var reader = new Ext.data.JsonReader({
			    	idProperty: 'id', //json数据中，记录中主键所对应的列的属性名
			        root: 'datas', //json数据中，保存记录集的属性的属性名
			        totalProperty: 'totalCount', //json数据中，保存总记录数的属性
			        fields: ['id', 'positionName', 'isMainPosition']
			    });
			    
			    // 创建 Data Store 
			    var store = new Ext.data.Store({
			    	autoDestroy: true,
			    	proxy: proxy,
			        reader: reader,
			        //data: Ext.grid.dummyData,
			        sortInfo:{field:'positionName', direction:'ASC'}// 排序信息 
			    }); 

			    // 创建编辑器表格 
			    var grid = new Ext.grid.EditorGridPanel({ 
			        store: store,
			        cm: cm, 
			        renderTo: 'mainPosition-grid', 
			        autoWidth:true, 
			        height:heightBody/2, 
			        title:'主岗位管理',//标题 
			        tbar:new Ext.Toolbar({
	                      items:[
	                             {
		                    	   text:"<spring:message code='fhd.common.save'/>",
		  			        	   icon: '${ctx}/images/icons/save.gif',    //'${ctx}/images/icons/select.gif',
		  				           handler:opsisave
	                             },{
	                                text:"<spring:message code='fhd.common.delete'/>",
	                                iconCls:'icon-del',
	                                handler:opsidel


	                              }]

					     }),
			        frame:true, 
			        clicksToEdit:1,//设置点击几次才可编辑 
			        selModel: new Ext.grid.RowSelectionModel({
				        singleSelect:false //设置单行选中模式, 否则将无法删除数据
				    }),
		            listeners :{'validateedit' : function(e){
		            	var eid = e.record.get("id");
			        	var list = e.grid.store.data.items;
						for(var i=0;i<list.length;i++){
							if(eid != list[i].data.id && e.record.get("isMainPosition")){
								if(list[i].data.isMainPosition == "是"){
									alert("<spring:message code='fhd.common.posiExit'/>");
									return false;
								}
							}
						}
		            }}
			        //tbar: false 
			    }); 
			    
			    //引用岗位表
				posiGrid=grid;
				
			    // 单元格编辑后事件处理 
			    //grid.on("afteredit", afterEdit, grid); 
			    // 事件处理函数 
			    function afterEdit(e) {
			    	// 被编辑的记录
			        var record = e.record;
			        //alert(record.get("isMainPosition"));
			        // 显示等待对话框 
			        Ext.Msg.wait("<spring:message code='fhd.common.wait'/>", "<spring:message code='fhd.common.editing'/>", "<spring:message code='fhd.common.operating'/>"); 
			        // 修改${fhd.common.prompt}
			        Ext.Msg.alert('修改主岗位', "被修改的岗位名称是:" + e.record.get("positionName") + "\n 修改的字段是:"+ e.field);
			    }; 
	
			    // 数据重新加载
			    store.load(); 
			}); 
			
			//岗位删除
			function opsidel(){
				var rows = posiGrid.getSelectionModel().getSelections();// 返回值为 Record 数组
				if(rows.length==0){ 
					Ext.MessageBox.alert("<spring:message code='fhd.common.alert'/>", "<spring:message code='fhd.common.msgDel'/>"); 
				}else{ 
					Ext.MessageBox.confirm("<spring:message code='fhd.common.prompt'/>框", "<spring:message code='fhd.common.makeSureDelete'/>",function(btn){ 
						if(btn=='yes'){ 
							if(rows){
								var empPosiIds = '';
								for (var i = 0; i < rows.length; i++) {
									//alert(rows[i].get("id"));
									empPosiIds += rows[i].get("id")+",";
									posiGrid.store.remove(rows[i]);
									//store.removeAll(); //删除所有数据 
								}
								// 后台操作更新数据
								Ext.Ajax.request({
									url:'${ctx}/sys/orgstructure/emp/delEmpPosi.do',
								    method:'post',
								    params:{
								    	empPosiIds: empPosiIds
								    },
								    success:function(response){
								      	if(response.responseText=='true'){
									      	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
									    }else{
									     	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>",'${fhd.common.operateFailure}');
									    }
								    }
								});
							} 
						} 
					}); 
				}
			}
			//岗位保存
			function opsisave(){
				 var empPosiIds = '';
		         var rec = '';
		    	  	var records = posiGrid.store.getModifiedRecords();
		    	  	Ext.each(records,function(m){ 
		    	  		//alert(m.get("id"));
		    	  		//alert(m.get("isMainPosition"));
		    	  		rec = m.get("id")+";"+m.get("isMainPosition");
		    	  		empPosiIds += rec+",";
		    	  	});
		    	 	// 后台操作更新数据
				Ext.Ajax.request({
					url:'${ctx}/sys/orgstructure/emp/updateEmpPosi.do',
				    method:'post',
				    params:{
				    	empPosiIds: empPosiIds
				    },
				    success:function(response){
				      	if(response.responseText=='true'){
					      	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
					    }else{
					     	Ext.Msg.alert("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
					    }
				    }
				});
			}
			Ext.EventManager.onWindowResize(function(width ,height){
				posiGrid.setWidth(width);
				posiGrid.setHeight(height/2);
   });
		</script>
	</form>
</body>
</html>