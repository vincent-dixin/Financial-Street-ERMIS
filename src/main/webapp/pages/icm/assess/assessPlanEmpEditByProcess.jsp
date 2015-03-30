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
function mergeAssessPlanRelaPoint(){//保存方法
	var rows = assessPlanRelaPointGrid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
			jsonArray.push(item.data);
	});
	FHD.ajax({
		url : 'icm/assess/mergerAssessPlanPointRelaOrgEmpBatch.f',
		params : {
			jsonString:Ext.encode(jsonArray),
			assessPlanId:'${param.assessPlanId}',
			batchType:'process'
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				returnAssessPlanRelaPoint();
			}
		}
	})
	assessPlanRelaPointGrid.store.commitChanges(); 
}
function returnAssessPlanRelaPoint(){//返回
	assessPlanPointRelaOrgAndEmpEditView.remove(assessPlanPointRelaOrgAndEmpEditView.gridPanel);
	assessPlanPointRelaOrgAndEmpEditView.gridPanel=Ext.create('Ext.container.Container',{
		autoLoad : {
			url : 'pages/icm/assess/assessPlanPointRelaOrgEmpEditList.jsp?assessPlanId=${param.assessPlanId}',
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
	   	fields: [{name: 'processId', type: 'string'},
		        {name: 'processName', type:'string'},
		        {name: 'empIdHandler', type:'string'}
		        ]
	});
	assessPlanRelaPointGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		renderTo: 'assessPlanRelaPoint${param._dc}', 
		multiSelect:false,
		checked:false,
		url: 'icm/assess/findProcessListByPage.f?assessPlanId=${param.assessPlanId}',
		height:FHD.getCenterPanelHeight()-30,
		cols:[{dataIndex:'processId',hidden:true},
			{header:'流程', dataIndex: 'processName', sortable: false, flex : 3},
			{header:'评价人', dataIndex: 'handlerId', sortable: false,flex:1,editor:new Ext.form.ComboBox({
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
		tbarItems:[{iconCls: 'icon-save',id:'saveDimension${param._dc}',handler:mergeAssessPlanRelaPoint, disabled : false, scope : this},'-',
		           {iconCls: 'icon-arrow-redo',id:'return${param._dc}',handler:returnAssessPlanRelaPoint, disabled : false, scope : this},'-'
			  
		]
	});
	
});
/***Ext.onReady end***/


</script>
</head>
<body>
	<div id='assessPlanRelaPoint${param._dc}'></div>
</body>
</html>