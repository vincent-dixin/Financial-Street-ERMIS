
/*
 * 评价流程的任务分配可编辑列表
 * 入参：parameter:{assessPlanId:'评价计划Id'}
 * */
Ext.define('FHD.view.icm.assess.component.AssessPlanProcessRelaEmpGrid',{
	extend : 'FHD.ux.GridPanel',
	alias: 'widget.assessplanprocessrelaempgrid',
	
	url:__ctxPath + '/icm/assess/findAssessPlanRelaProcessListByPageForAllocation.f',
	extraParams:{
		assessPlanId:''
	},
	multiSelect:false,
	pagable:false,
	autoScroll:true,
	checked:false,
	
	initComponent : function() {
		var me = this;
		
		me.extraParams.assessPlanId=me.businessId;
		me.empStore = Ext.create('Ext.data.Store',{//myLocale的store
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/icm/assess/findEmpByAssessPlanId.f?assessPlanId='+me.extraParams.assessPlanId
			}
		});
		me.empStore.load();
		me.cols=[
		    {dataIndex:'assessPlanRelaProcessId',hidden:true},
		    {dataIndex:'processId',hidden:true},
		    {header : '一级流程分类',dataIndex : 'firstProcessName',sortable : true,flex : 2},
			{header : '二级流程分类',dataIndex : 'parentProcessName',sortable : true,flex : 1}, 
		    {header:'末级流程', dataIndex: 'processName',sortable: false, flex : 1},
		    {header:'责任部门', dataIndex: 'orgName',sortable: false, flex : 1},
			{header:'评价人', dataIndex: 'handlerId', sortable: false,flex : 2,
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
			 {header:'复核人', dataIndex: 'reviewerId', sortable: false,flex : 2,
			     renderer:function(value,p,record){
			    	var empIdHandlerValue=record.data.handlerId;
			    	var index = me.empStore.find('id',value);
			    	var record = me.empStore.getAt(index);
			    	if(record!=null&&empIdHandlerValue==value){
			    		value='';
			    		Ext.MessageBox.alert('提示','复核人不能和评价人相同!');
			    	}else{
			    		if(record!=null){
			    			return record.data.name;
				    	}else{
				    		return value;
				    	}
			    	}
			  }
		}];
		me.callParent(arguments);
	}
});