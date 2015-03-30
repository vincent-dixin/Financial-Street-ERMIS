<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>可编辑的assessPlanPointRelaOrgEmpGrid列表</title>
<script type="text/javascript">
/***attribute start***/
var assessPlanRelaPointGrid,
	empStore = Ext.create('Ext.data.Store',{//myLocale的store
		fields : ['id', 'name'],
		proxy : {type : 'ajax',url : 'icm/assess/findEmpByAssessPlanId.f?assessPlanId=${param.assessPlanId}'}
	});
/***attribute end***/
/***function start***/
   function submitGrid(){//提交

	   FHD.ajax({
			url : 'icm/assess/saveAssessorAndAssessResultBatch.f',
			params : {
				assessPlanId:'${param.assessPlanId}'
			},
			callback : function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					assessPlanRelaPointGrid.store.load();
				}
			}
		})
	
	}
 
 function returnPage(){//返回
	 fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
	 fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanList.jsp?assessPlanId=${param.assessPlanId}');
	 fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);

	}
function mergeAssessPlanRelaPoint(){//保存方法
	var rows = assessPlanRelaPointGrid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
			jsonArray.push(item.data);
	});
	FHD.ajax({
		url : 'icm/assess/mergeAssessPlanPointRelaOrgEmpBatch.f',
		params : {
			jsonString:Ext.encode(jsonArray),
			assessPlanId:'${param.assessPlanId}'
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				assessPlanRelaPointGrid.store.load();
			}
		}
	})
	assessPlanRelaPointGrid.store.commitChanges(); 
}
function mergeAssessPlanRelaPointByProcess(){//按流程批量修改方法
	assessPlanPointRelaOrgAndEmpEditView.remove(assessPlanPointRelaOrgAndEmpEditView.gridPanel);
	assessPlanPointRelaOrgAndEmpEditView.gridPanel=Ext.create('Ext.container.Container',{
		autoLoad : {
			url : 'pages/icm/assess/assessPlanEmpEditByProcess.jsp?assessPlanId=${param.assessPlanId}',
			scripts : true
		}
	});
	assessPlanPointRelaOrgAndEmpEditView.add(assessPlanPointRelaOrgAndEmpEditView.gridPanel);
}
function mergeAssessPlanRelaPointByOrg(){//按部门批量修改方法
	assessPlanPointRelaOrgAndEmpEditView.remove(assessPlanPointRelaOrgAndEmpEditView.gridPanel);
	assessPlanPointRelaOrgAndEmpEditView.gridPanel=Ext.create('Ext.container.Container',{
		autoLoad : {
			url : 'pages/icm/assess/assessPlanEmpEditByOrg.jsp?assessPlanId=${param.assessPlanId}',
			scripts : true
		}
	});
	assessPlanPointRelaOrgAndEmpEditView.add(assessPlanPointRelaOrgAndEmpEditView.gridPanel);

}
/***function end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	/***assessPlanRelaPointGrid start***/
	Ext.define('Dimension', {//定义model
	    extend: 'Ext.data.Model',
	   	fields: [{name: 'assessPlanRelaPointId', type: 'string'},
		        {name: 'processName', type:'string'},
		        {name: 'assessPointName', type:'string'},
		        {name: 'orgName', type:'string'},
		        {name: 'empIdHandler', type:'string'}
		        ]
	});
	assessPlanRelaPointGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		//renderTo: 'assessPlanRelaPoint${param._dc}', 
		multiSelect:false,
		columnWidth: 1,
		checked:false,
		url: 'icm/assess/findAssessPlanRelaPointListByPage.f?assessPlanId=${param.assessPlanId}',
		height:200,
		autoScroll:true,
		cols:[{dataIndex:'assessPlanRelaPointId',hidden:true},
		      {header:'部门', dataIndex: 'orgName',sortable: false, flex : 1},
		      {header:'流程', dataIndex: 'processName', sortable: false, flex : 1},
		      {header:'评价点', dataIndex: 'assessPointName',sortable: false, flex : 1},
		      {header:'评价人', dataIndex: 'handlerId', sortable: false,editor:new Ext.form.ComboBox({
		    	  store :empStore,
		    	  valueField : 'id',
		    	  displayField : 'name',
		    	  selectOnTab: true,
		    	  lazyRender: true,
		    	  typeAhead: true,
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
		      }],
		      tbarItems:[{text : FHD.locale.get('fhd.common.save'),iconCls: 'icon-save',id:'saveDimension${param._dc}',handler:mergeAssessPlanRelaPoint, disabled : false, scope : this},'-',
		                 {text :'按流程批量',iconCls: 'icon-save',id:'saveByProcess${param._dc}',handler:mergeAssessPlanRelaPointByProcess, disabled : false, scope : this},'-',
		                 {text :'按部门批量',iconCls: 'icon-save',id:'saveByOrg${param._dc}',handler:mergeAssessPlanRelaPointByOrg, disabled : false, scope : this},'-',
		                 {text :'返回',iconCls: 'icon-arrow-redo',id:'return${param._dc}',handler:returnPage, disabled : false, scope : this},'-',
		                 {text :'提交',iconCls: 'icon-save',id:'submit${param._dc}',handler:submitGrid, disabled : false, scope : this},'-'
		                 ]
	});
	var basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation');
	var basePanel=new Ext.FormPanel({
		height : FHD.getCenterPanelHeight(),
		autoScroll:true,
		layout : {
		type : 'column'
		},
		defaults : {
		columnWidth : 1 / 1
		},
		collapsed : false,
		renderTo: 'assessPlanRelaPoint${param._dc}',
		items:[basicInfo,
		       {
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : true,
			title : '指定评价人',
			items : [assessPlanRelaPointGrid]
		       }],
		       tbar:{
						items: [{
							xtype: 'tbtext',
						}, '->',{
							text: '返回',
							iconCls: 'icon-arrow-redo',
							handler: returnPage
						},{
							text: '保存',
							iconCls: 'icon-control-stop-blue',
							handler: mergeAssessPlanRelaPoint
						},{
							text: '提交',
							iconCls: 'icon-control-stop-blue',
							handler: submitGrid
						}]
				},
				bbar:{
					items: [{
						xtype: 'tbtext',
					}, '->',{
						text: '返回',
						iconCls: 'icon-arrow-redo',
						handler: returnPage
					},{
						text: '保存',
						iconCls: 'icon-control-stop-blue',
						handler: mergeAssessPlanRelaPoint
					},{
						text: '提交',
						iconCls: 'icon-control-stop-blue',
						handler: submitGrid
					}]
			}
	});
	basePanel.getForm().load({
		url:'assess/assessPlan/findAssessPlanForView.f?assessPlanId=${param.assessPlanId}',
				success: function (form, action) {
					return true;
				},
				failure: function (form, action) {
					return true;
				}
	});
});
/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='assessPlanRelaPoint${param._dc}'></div>
</body>
</html>