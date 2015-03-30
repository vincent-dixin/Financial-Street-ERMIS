<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评价计划编辑</title>
<script type="text/javascript">
function mergeAssessPlanRelaPointByOrg(){//保存方法
	var rows = editByOrgGrid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
			jsonArray.push(item.data);
	});
	FHD.ajax({
		url : 'icm/assess/mergerAssessPlanProcessRelaOrgEmpBatch.f',
		params : {
			jsonString:Ext.encode(jsonArray),
			assessPlanId:'${param.assessPlanId}',
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				mergeAssessPlanRelaPoint.close();
				fHD_icm_assess_assessPlanAllocation.gridPanel.store.load();
			}
		}
	})
}
var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"该操作需几分钟时间，请耐心等待..."});
var empStore=Ext.create('Ext.data.Store',{//myLocale的store
	fields : ['id', 'name'],
	proxy : {type : 'ajax',url : 'icm/assess/findEmpByAssessPlanId.f?assessPlanId=${param.assessPlanId}'},
});
var editByOrgGrid=Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
 	columnWidth : 1,
 	pagable:false,
	checked:false,
	url: 'icm/assess/findOrgListByPage.f?assessPlanId=${param.assessPlanId}',
	cols:[{dataIndex:'assessPlanRelaProcessId',hidden:true},
	      {dataIndex:'orgId',hidden:true},
			{header:'部门', dataIndex: 'orgName', sortable: false, flex : 3},
			{header:'评价日期', dataIndex: 'orgName', sortable: false, flex : 3},
			{header:'评价人', dataIndex: 'handlerId', sortable: false,flex:1,editor:new Ext.form.ComboBox({
				store :empStore,
				valueField : 'id',
				displayField : 'name',
				allowBlank : false,
				editable : false
			}),
			renderer:function(value){
				var index = empStore.find('id',value);
				var record = empStore.getAt(index);
				if(record!=null){
					return record.data.name;
				}else{
					return value;
				}
			}
		    },
		   {header:'复核人', dataIndex: 'reviewerId', sortable: false,editor:new Ext.form.ComboBox({
		    	  store :empStore,
		    	  valueField : 'id',
		    	  displayField : 'name',
		    	  selectOnTab: true,
		    	  lazyRender: true,
		    	  typeAhead: true,
		    	  allowBlank : false,
		    	  editable : false
		      }),
		      renderer:function(value,p,record){
		    	  var empIdHandlerValue=record.data.handlerId;
		    	  var index = empStore.find('id',value);
		    	  var record = empStore.getAt(index);
		    	  if(record!=null&&empIdHandlerValue==value){
		    		  value='';
		    		  Ext.MessageBox.alert('提示','复合人不能和评价人相同');
		    	  }else{
		    		  if(record!=null){
			    		  return record.data.name;
			    	  }else{
			    		  return value;
			    	  }
		    	  }
		      }
		      }
			 ],
		      tbarItems:[{iconCls: 'icon-save',id:'saveDimension${param._dc}',handler:mergeAssessPlanRelaPointByOrg, disabled : false, scope : this},'-',
				]
});
var mergeAssessPlanRelaPoint=Ext.create('Ext.Window',{
	title:'按部门批量修改',
	layout:'fit',
	modal:true,//是否模态窗口
	collapsible:true,
	width:750,
	height:600,
	maximizable:true,//（是否增加最大化，默认没有）
	items:[editByOrgGrid],
});
	Ext.define('FHD_icm_assess_assessPlanAllocation',{
						extend : 'Ext.form.Panel',
						renderTo:'FHD.icm.assess.assessPlanEdit${param._dc}',
						height : FHD.getCenterPanelHeight(),
						autoScroll:true,
						items : [],
						layout : {
						type : 'column'
						},
						defaults : {
						columnWidth : 1 / 1
						},
						collapsed : false,
						basicInfo:null,
						gridPanel:{},
						empStore:null,
						initComponent : function() {
							var me = this;
							me.empStore = Ext.create('Ext.data.Store',{//myLocale的store
								fields : ['id', 'name'],
								proxy : {type : 'ajax',url : 'icm/assess/findEmpByAssessPlanId.f?assessPlanId=${param.assessPlanId}'}
							});
							me.basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation');
							me.gridPanel = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
								multiSelect:false,
								pagable:false,
								autoScroll:true,
								height:180,
								columnWidth:1,
								checked:false,
								url: 'icm/assess/findAssessPlanRelaProcessListByPageForAllocation.f?assessPlanId=${param.assessPlanId}',
								cols:[{dataIndex:'assessPlanRelaProcessId',width:0},
								      {header:'末级流程', dataIndex: 'processName',sortable: false, flex : 1},
								      {header:'责任部门', dataIndex: 'orgName',sortable: false, flex : 1},
								      {header:'评价日期', dataIndex: 'planEndDate',sortable: false, flex : 1},
								      {header:'评价人', dataIndex: 'handlerId', sortable: false,editor:new Ext.form.ComboBox({
								    	  store :me.empStore,
								    	  valueField : 'id',
								    	  displayField : 'name',
								    	  selectOnTab: true,
								    	  lazyRender: true,
								    	  typeAhead: true,
								    	  allowBlank : false,
								    	  editable : false
								      }),
								      renderer:function(value,p,record){
								    	  var index = me.empStore.find('id',value);
								    	  var record = me.empStore.getAt(index);
								    	  if(record!=null){
								    		  return record.data.name;
								    	  }else{
								    		  return value;
								    	  }
								      }
								      },
								      {header:'复核人', dataIndex: 'reviewerId', sortable: false,editor:new Ext.form.ComboBox({
								    	  store :me.empStore,
								    	  valueField : 'id',
								    	  displayField : 'name',
								    	  selectOnTab: true,
								    	  lazyRender: true,
								    	  typeAhead: true,
								    	  allowBlank : false,
								    	  editable : false
								      }),
								      renderer:function(value,p,record){
								    	  var empIdHandlerValue=record.data.handlerId;
								    	  var index = me.empStore.find('id',value);
								    	  var record = me.empStore.getAt(index);
								    	  if(record!=null&&empIdHandlerValue==value){
								    		  value='';
								    		  Ext.MessageBox.alert('提示','复合人不能和评价人相同');
								    	  }else{
								    		  if(record!=null){
									    		  return record.data.name;
									    	  }else{
									    		  return value;
									    	  }
								    	  }
								    	  
								      }
								      }],
								      tbarItems:[{text :'按部门批量',iconCls: 'icon-save',id:'saveByOrg${param._dc}',handler:function(){mergeAssessPlanRelaPoint.show();} , disabled : false, scope : this},'-']
							});
							me.items = [me.basicInfo,{
										xtype : 'fieldset',
										margin: '7 10 0 30',
										layout : {
											type : 'column'
										},
										collapsed : false,
										collapsible : true,
										title : '指定评价人',
										items : [me.gridPanel]
									}
									
									
							];
							me.tbar={
									items: [{
										xtype: 'tbtext',
									}, '->',{
										text: '返回',
										iconCls: 'icon-arrow-redo',
										handler: function(){
											fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
                                       		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanList.jsp');
                                       		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
										}
									},{
										text: '保存',
										iconCls: 'icon-control-stop-blue',
										handler: function(){
											var rows = me.gridPanel.store.getModifiedRecords();
											var jsonArray=[];
											Ext.each(rows,function(item){
													jsonArray.push(item.data);
											});
											FHD.ajax({
												url : 'icm/assess/mergeAssessPlanProcessRelaOrgEmpBatch.f',
												params : {
													jsonString:Ext.encode(jsonArray)
												},
												callback : function(data){
													if(data){
														Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
													}
												}
											})
											me.gridPanel.store.commitChanges(); 
										}
									},{
										text: '提交',
										iconCls: 'icon-control-stop-blue',
										handler: function(){
											myMask.show();
											FHD.ajax({
													url : 'icm/assess/saveAssessorAndAssessResultBatch.f',
													params : {
														assessPlanId:'${param.assessPlanId}'
													},
													callback : function(data){
														if(data){
															myMask.hide();
															Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
														}
													}
												})
										}
									}]
							}
						
							me.bbar={
									items: [{
										xtype: 'tbtext',
									}, '->',{
										text: '返回',
										iconCls: 'icon-arrow-redo',
										handler: function(){
											fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
                                       		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanList.jsp');
                                       		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
										}
									},{
										text: '保存',
										iconCls: 'icon-control-stop-blue',
										handler: function(){
											var rows = me.gridPanel.store.getModifiedRecords();
											var jsonArray=[];
											Ext.each(rows,function(item){
													jsonArray.push(item.data);
											});
											FHD.ajax({
												url : 'icm/assess/mergeAssessPlanProcessRelaOrgEmpBatch.f',
												params : {
													jsonString:Ext.encode(jsonArray)
												},
												callback : function(data){
													if(data){
														Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
													}
												}
											})
											me.gridPanel.store.commitChanges(); 
										}
									},{
										text: '提交',
										iconCls: 'icon-control-stop-blue',
										handler: function(){
											myMask.show();
											 FHD.ajax({
													url : 'icm/assess/saveAssessorAndAssessResultBatch.f',
													params : {
														assessPlanId:'${param.assessPlanId}'
													},
													callback : function(data){
														if(data){
															myMask.hide();
															Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
														}
													}
												})
										}
									}]
							}
							
					    	
							 this.callParent(arguments);
	}
					});
	var fHD_icm_assess_assessPlanAllocation=null;
	
	Ext.onReady(function() {
		fHD_icm_assess_assessPlanAllocation=Ext.create('FHD_icm_assess_assessPlanAllocation');
		FHD.componentResize(fHD_icm_assess_assessPlanAllocation,0,0);
		fHD_icm_assess_assessPlanAllocation.empStore.load();
			     
		fHD_icm_assess_assessPlanAllocation.getForm().load({
			url:'assess/assessPlan/findAssessPlanForView.f?assessPlanId=${param.assessPlanId}',
					success: function (form, action) {
						return true;
					},
					failure: function (form, action) {
						return true;
					}
		});
		
	});
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessPlanEdit${param._dc}'></div>
</body>
</html>