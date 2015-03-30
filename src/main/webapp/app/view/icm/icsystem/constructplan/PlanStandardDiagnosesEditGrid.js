/*
 * 评价流程的可编辑列表
 * 入参：parameter:{assessPlanId:'评价计划Id',assessPlanType:'评价类型'}
 * */
Ext.define('FHD.view.icm.icsystem.constructplan.PlanStandardDiagnosesEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.planstandarddiagnoseseditgrid',
	requires: [
    ],
	url:__ctxPath+ '/icm/icsystem/findplanrelastandarddiagnoseslistbypage.f',
	checked :  false,
    searchable:false,
	cols:new Array(),
	
	initComponent:function(){
		var me=this;
    	//header
		//合规诊断store
		var isDiagnosisStore=Ext.create('Ext.data.Store', {
		    fields: ['value', 'text'],
		    data : [
		        {"value":true, "text":"合格"},
		        {"value":false, "text":"不合格"}
		    ]
		});
		me.cols=[
		    {dataIndex : 'id',hidden:true},
		    {dataIndex : 'planStandardId',hidden:true},
			{dataIndex : 'text',hidden:true},
			{dataIndex : 'standardId',hidden:true},
			{header : '标准名称',dataIndex : 'standardName',sortable : false,flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}
			},
			{header : '内控要求',dataIndex : 'controlRequirement',sortable : false,flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}
			}, 
			{header : '内控要素',dataIndex :'controlPoint',sortable : false, flex : 1},
			{header : '整改责任部门',dataIndex :'standardRelaOrg',sortable : false, flex : 1},
			{
				header : '诊断结果' + '<font color=red>*</font>',dataIndex :'diagnosis',emptyCellText:'<font color="#808080">请填写</font>',sortable : false, flex : 1,
				editor:Ext.create('Ext.form.ComboBox', {
				    store: isDiagnosisStore,
				    queryMode: 'local',
				    displayField:'text',
				    valueField: 'value'
				}),
				renderer:function(value,metaData,record,colIndex,store,view){
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = isDiagnosisStore.find('value',value);
					var record = isDiagnosisStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{header:'实施证据', dataIndex: 'proof', sortable: false,flex:2,editor:true,
				renderer:function(value,metaData,record,colIndex,store,view){
				    metaData.tdAttr = 'data-qtip="如果诊断结果为合格，请填写实施证据。" style="background-color:#FFFBE6"';
					if(value){
						return value;
					}else{
						return '';
					}
				}
			},
			{header:'缺陷描述', dataIndex: 'description', sortable: false,flex:2,editor:true,
				renderer:function(value,metaData,record,colIndex,store,view){
					metaData.tdAttr = 'data-qtip="如果诊断结果为不合格，请填写缺陷描述。" style="background-color:#FFFBE6"';
					if(value){
						return value;
					}else{
						return '';
					}
					}
			},
			{header:'控制描述', dataIndex: 'controldesc', sortable: false,flex:2,editor:true,
				renderer:function(value,metaData,record,colIndex,store,view){
					metaData.tdAttr = 'data-qtip="如果诊断结果为合格，请填写控制描述。" style="background-color:#FFFBE6"';
					if(value){
						return value;
					}else{
						return '';
					}
				}
			}
			];
		me.callParent(arguments);
	},
	saveData:function(){
		var me = this;
		var jsonArray=[];
		var rows = me.store.getModifiedRecords();
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		if(jsonArray.length>0){
//			var validateFlag = false;
//			var count = me.store.getCount();
//			for(var i=0;i<count;i++){
//				var item = me.store.data.get(i);
//	 			if(item.get('diagnosis') == null){
//	 				reason = '诊断结果不能为空!';
//	 				validateFlag = true;
//	 				break;
//	 			}else if(item.get('diagnosis') == true && (item.get('proof') == "" || item.get('proof') == null)){
//					reason = '诊断结果为合格的,实施证据不能为空!';
//					validateFlag = true;
//					break;
//				}else if(item.get('diagnosis') == false && (item.get('description') == ""|| item.get('description') == null)){
//					reason = '诊断结果为不合格的,缺陷描述不能为空!';
//					validateFlag = true;
//					break;
//				}
//			}
//			if(validateFlag){
//	 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), reason);
//	 			return false;
//	 		}
			 FHD.ajax({
    		     url : __ctxPath+ '/icm/icsystem/mergeconstructplanreladiagnosesbatch.f',
    		     params : {
    		    	 modifiedRecord:Ext.encode(jsonArray),
    		    	 type : 'save'
    			 },
    			 callback : function(data) {
    				 if (data) {
						 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
						 me.store.load();  
    				 }
    			 }
    		});
    		return true;
		}else{
			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),"无修改记录!");
			return false;
		}
	},
	saveSubmitData:function(){
		var me = this;
		var jsonArray=[];
		var rows = me.store.getModifiedRecords();
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		var validateFlag = false;
		var count = me.store.getCount();
		for(var i=0;i<count;i++){
			var item = me.store.data.get(i);
 			if(item.get('diagnosis') == null){
 				reason = '诊断结果不能为空!';
 				validateFlag = true;
 				break;
 			}else if(item.get('diagnosis') == true && (item.get('proof') == "" || item.get('proof') == null)){
				reason = '诊断结果为合格的,实施证据不能为空!';
				validateFlag = true;
				break;
			}else if(item.get('diagnosis') == true && (item.get('controldesc') == "" || item.get('controldesc') == null)){
				reason = '诊断结果为合格的,控制描述不能为空!';
				validateFlag = true;
				break;
			}else if(item.get('diagnosis') == false && (item.get('description') == ""|| item.get('description') == null)){
				reason = '诊断结果为不合格的,缺陷描述不能为空!';
				validateFlag = true;
				break;
			}
		}
		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), reason);
 			return false;
 		}
	 	FHD.ajax({
			url : __ctxPath+ '/icm/icsystem/mergeconstructplanreladiagnosesbatch.f',
		    params : {
		    	modifiedRecord:Ext.encode(jsonArray),
		    	type : 'save'
			},
    		callback : function(data) {
    			if (data) {
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
					me.store.load();  
    			}
    		}
    	});
    	return true;
	},
    reloadData:function(){
    	var me=this;
    	me.store.proxy.extraParams.constructPlanId = me.businessId;
    	me.store.proxy.extraParams.executionId = me.executionId;
    	me.store.load();
    }
});