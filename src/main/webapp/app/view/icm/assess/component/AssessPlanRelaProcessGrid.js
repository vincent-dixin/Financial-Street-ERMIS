/*
 * 评价流程的列表
 * parameter:{assessPlanId:'评价计划Id'}
 * */
Ext.define('FHD.view.icm.assess.component.AssessPlanRelaProcessGrid',{
	extend:'FHD.ux.GridPanel',
	alias: 'widget.assessplanrelaprocessgrid',
	
	pagable :false,
	checked:false,
	url: __ctxPath+ '/icm/assess/findAssessPlanRelaProcessListByPage.f',
	extraParams:{
		assessPlanId:''
	},
	
	initComponent:function(){
		var me=this;
		
		var isPracticeTestStore=Ext.create('Ext.data.Store', {
		    fields: ['value', 'text'],
		    data : [
	            {"value":true, "text":"是"},
	            {"value":false, "text":"否"}
		    ]
		});
		//抽样测试store
		var isSampleTesttStore=Ext.create('Ext.data.Store', {
		    fields: ['value', 'text'],
		    data : [
	            {"value":true, "text":"是"},
	            {"value":false, "text":"否"}
		    ]
		});
    	//header
		me.cols=[
		    {dataIndex : 'dbid',hidden:true ,flex : 1},
			{dataIndex : 'text',hidden:true ,flex : 1},
			{dataIndex : 'processId',hidden:true ,flex : 1},
			{header : '一级流程分类',dataIndex : 'firstProcessName',sortable : true,flex : 1, hidden:false},
			{header : '二级流程分类',dataIndex : 'parentProcessName',sortable : true,flex : 1, hidden:false}, 
			{header : '末级流程',dataIndex :'processName',sortable : true, flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header : '是否穿行测试',dataIndex :'isPracticeTest',sortable : true, flex : 2,
				renderer:function(value){
					var index = isPracticeTestStore.find('value',value);
					var record = isPracticeTestStore.getAt(index);
					if(record!=null){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{header : '穿行次数',dataIndex :'practiceNum',sortable : true,flex : 2},
			{header : '是否抽样测试',dataIndex :'isSampleTest',sortable : true,flex : 2,
				renderer:function(value){
					var index = isPracticeTestStore.find('value',value);
					var record = isPracticeTestStore.getAt(index);
					if(record!=null){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{header : '抽样比例(%)',dataIndex :'coverageRate',sortable : true,flex : 2,
				renderer:function(value){
					if(value!=''&&value!=null){
						var newValue=value*100;//toFixed(0);
			 			return newValue;
					}else{
						return value;
					}
				}
			},
			{header : '评价人',dataIndex :'executeEmpName',sortable : false,flex : 1, hidden:true,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header : '评价日期',dataIndex :'assessDate',sortable : false,flex : 2, hidden:true},
			{header : '复核人',dataIndex :'reviewerEmpName',sortable : false,flex : 1, hidden:true,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header : '复核日期',dataIndex :'reviewDate',sortable : false,flex : 2, hidden:true},
			{header : '结果分析',dataIndex :'resultAnalysis',sortable : false,flex : 3, hidden:true,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			}
		];
		
		me.callParent(arguments);
		
		if(me.isShow){
			me.down('[dataIndex=firstProcessName]').hide();
			me.down('[dataIndex=parentProcessName]').hide();
			me.down('[dataIndex=executeEmpName]').show();
			me.down('[dataIndex=assessDate]').show();
			me.down('[dataIndex=reviewerEmpName]').show();
			me.down('[dataIndex=reviewDate]').show();
			me.down('[dataIndex=resultAnalysis]').show();
		}
	},
	reloadData:function(){
    	var me=this;
    	
    	me.store.load();
    }
});