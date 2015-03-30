/*
 * 评价流程的可编辑列表
 * 入参：parameter:{assessPlanId:'评价计划Id',assessPlanType:'评价类型'}
 * */
Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanRelaStandardEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.constructplanrelastandardeditgrid',
	requires: [
       'FHD.ux.standard.StandardSelectorWindow',
       'FHD.view.icm.icsystem.constructplan.SelectorConstructEmpWindow'
    ],
	url:__ctxPath+ '/icm/icsystem/findconstructplanrelastandardlistbypage.f',
	extraParams:{
		businessId:''
	},
	pagable :false,
	cols:new Array(),
	tbarItems:new Array(),
	bbarItems:new Array(),
	sortableColumns : false,
	//可编辑列表为只读属性
	readOnly : false,
	searchable : false,
	
	initComponent:function(){
		var me=this;
		me.on('selectionchange',me.onchange);//选择记录发生改变时改变按钮可用状态
		//合规诊断store
		var isNormallyDiagnosisStore=Ext.create('Ext.data.Store', {
		    fields: ['value', 'text'],
		    data : [
		        {"value":true, "text":"是"},
		        {"value":false, "text":"否"}
		    ]
		});
		//流程梳理store
		var isProcessEditStore=Ext.create('Ext.data.Store', {
		    fields: ['value', 'text'],
		    data : [
		        {"value":true, "text":"是"},
		        {"value":false, "text":"否"}
		    ]
		});
		me.empStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/icm/icsystem/findallempbyconstructplanid.f'
			}
		});
		
		me.cols=[
		    {dataIndex : 'dbid',hidden:true},
			{dataIndex : 'text',hidden:true},
			{dataIndex : 'standardId',hidden:true},
			{
				header : '标准名称',dataIndex : 'standardName',flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}
			},
			{header : '内控要求',dataIndex : 'controlRequirement',flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}
			}, 
			{header : '内控要素',dataIndex :'controlPoint',flex : 2},
			{header : '末级流程',dataIndex :'processName',flex : 1,
			 	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}},
			{header : '责任部门',dataIndex :'standardRelaOrg',flex : 1},
			{header : '合规诊断',dataIndex :'normallyDiagnosis',hidden : true},
			{header : '流程梳理',dataIndex :'processEdit',hidden : true},
			{
				header : '是否合规诊断',dataIndex :'isNormallyDiagnosis',flex : 1, scope:this,
				editor:Ext.create('Ext.form.ComboBox', {
				    store: isNormallyDiagnosisStore,
				    queryMode: 'local',
				    displayField:'text',
				    valueField: 'value'	
				}),
				renderer:function(value, metaData, record, colIndex, store, view){
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var me = this;
					if(me.up('constructplancardpanel').constructplanform.items.items[0].items.items[6].lastValue == 'process'){
						me.cols[10].hidden=true;	
					}
					var index = isNormallyDiagnosisStore.find('value',value);
					var record = isNormallyDiagnosisStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{
				header : '是否流程梳理',dataIndex :'isProcessEdit',sortable : false, flex : 1, 
				editor:Ext.create('Ext.form.ComboBox', {
				    store: isNormallyDiagnosisStore,
				    queryMode: 'local',
				    displayField:'text',
				    valueField: 'value',
				    listeners : {
				    	beforedeselect : function(obj,record,index,eOpts){
				    		var selection = me.getSelectionModel().getSelection();
				    		if(selection[0].data.processName == ''||selection[0].data.processName == null){
				    			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '流程为空不能进行流程梳理!');
				    			return false;
				    		}
				    	}
				    }
				}),
				renderer : function(value, metaData, record, colIndex, store, view){
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var me = this;
					var index = isNormallyDiagnosisStore.find('value',value);
					var record = isNormallyDiagnosisStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{dataIndex : 'constructPlanRelaOrgEmpId',hidden:true},
			{
				header:'建设责任人<font color=red>*</font>',dataIndex:'constructPlanEmp',flex:1,emptyCellText:'<font color="#808080">请选择</font>',
				editor:Ext.create('Ext.form.ComboBox',{
					store :me.empStore,
					valueField : 'id',
					displayField : 'name',
					allowBlank : false,
					editable : false}),
					renderer:function(value,metaData,record,colIndex,store,view){
						metaData.tdAttr = 'style="background-color:#FFFBE6"';
						var index = me.empStore.find('id',value);
						var record = me.empStore.getAt(index);
						if(record){
							return record.data.name;
						}else{
							return '';
						}
					}
			}
		];
		if(me.readOnly){
			me.searchable = false;
		}else{
		//tbar
			me.tbarItems=[
			    {
			    	iconCls : 'icon-add',
			    	id : 'addConstructRelaStandard',
			    	text: '选择标准',
			    	tooltip : '选择已有标准',
			    	handler :me.addConstructRelaStandard,
			    	scope : this
			    },
//			     '-',
//			    {
//			    	iconCls : 'icon-add',
//			    	id : 'addConstructRelaDefect',
//			    	tooltip: '选择已有缺陷',
//			    	text: '选择缺陷',
//			    	handler :me.addConstructRelaDefect,
//			    	scope : this
//			    },
			     '-',
			    {
			    	iconCls : 'icon-group-add',
			    	id : 'addConstructRelaEmp',
			    	tooltip: '批量设置建设责任人',
			    	text: '批量设置建设责任人',
			    	handler :me.addConstructRelaDefect,
			    	scope : this,
			    	disabled : true
			    },
			    '-',
			    {
			    	iconCls : 'icon-del',
			    	id : 'constructPlanListEditDel',
			    	text: '删除已选',
			    	tooltip: '删除已选',
			    	handler :me.delStandard,
			    	scope : this,
			    	disabled : true
			    }
			]
		}
		me.callParent(arguments);
	},
	colInsert: function (index, item) {
        if (index < 0) return;
        if (index > this.cols.length) return;
        for (var i = this.cols.length - 1; i >= index; i--) {
            this.cols[i + 1] = this.cols[i];
        }
        this.cols[index] = item;
    },
	saveData:function(){
		var me=this;
		var validateFlag = false;
		var count = me.store.getCount();
		for(var i=0;i<count;i++){
			var item = me.store.data.get(i);
 			if(item.get('constructPlanEmp') == ''){
				validateFlag = true;
			}
		}
		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '体系建设责任人不允许为空!');
 			return false;
 		}
		var jsonArray=[];
		var rows = me.store.getModifiedRecords();
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		if(jsonArray.length>0){
			 FHD.ajax({
    		     url : __ctxPath+ '/icm/icsystem/mergeconstructplanrelastandardbatch.f',
    		     params : {
    		    	 modifiedRecord:Ext.encode(jsonArray)
    			 },
    			 callback : function(data) {
    				 if (data) {
						 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
						 me.store.load();  
    				 }
    			 }
    		});
		}
		return true;
	},
	saveDataUndoVailidate:function(){
		var me=this;
		var jsonArray=[];
		var rows = me.store.getModifiedRecords();
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		if(jsonArray.length>0){
			 FHD.ajax({
    		     url : __ctxPath+ '/icm/icsystem/mergeconstructplanrelastandardbatch.f',
    		     params : {
    		    	 modifiedRecord:Ext.encode(jsonArray)
    			 },
    			 callback : function(data) {
    				 if (data) {
						 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
						 me.store.load();  
    				 }
    			 }
    		});
		}
		return true;
	},
	saveEmp:function(id){
		var me=this;
		debugger;
		var jsonArray=[];
//		var rows =  me.store.data;
		var selection = me.getSelectionModel().getSelection();
		for ( var i = 0; i < selection.length; i++) {
			Ext.each(selection[i],function(item){
				jsonArray.push(item.data);
			});
		}
		FHD.ajax({
		    url : __ctxPath+ '/icm/icsystem/mergeconstructplanrelastandardbatch.f',
		params : {
			modifiedRecord : Ext.encode(jsonArray),
			defaultValue : id
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
	//新增标准选择
    addConstructRelaStandard : function(){
    	var me=this;
		//1.按标准选择
		var constructPlanStandard = Ext.widget('standardselectorwindow', {
			labelText : '',
			fieldLabel:'选择标准',
			single : false,
			labelWidth : 60,
			parent:false,
			value:'',
			myType : 'required',//required//standard
			margin: '5 5 5 5',
			columnWidth:.85,
			onSubmit : function(store) {
				var standardIdArray = new Array();
				store.each(function(r){
            		standardIdArray.push(r.data.id);
            	});
				FHD.ajax({
					url : __ctxPath + '/icm/icsystem/saveconstructplanrelastandard.f',
					params : {
						standardIds:standardIdArray.join(','),
						constructPlanId : me.extraParams.businessId
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							me.store.load();
						}
					}
				});
			}
		}).show();
    },
	// 新增  缺陷  选择
    addConstructRelaDefect : function(){
    	//2.按缺陷选择
    	var me=this;
		var constructPlanRelaDefect = Ext.widget('selectorconstructempwindow',{
		   labelText : '',
		   fieldLabel:'缺陷选择',
		   single : false,
		   labelWidth : 60,
		   constructPlanId : me.extraParams.businessId,
		   parent:false,
		   value:'',
		   margin: '5 5 5 5',
		   columnWidth:.85,
		   onSubmit : function(id) {
				me.saveEmp(id);
		   	    me.reloadData();
		   }
		}).show();
    },
    //删除流程范围
    delStandard:function(){
    	var me=this;
    	
    	var selection = me.getSelectionModel().getSelection();
    	if(0 == selection.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.msgDel'));
    	}else{
			Ext.MessageBox.show({
				title : FHD.locale.get('fhd.common.delete'),
				width : 260,
				msg : FHD.locale.get('fhd.common.makeSureDelete'),
				buttons : Ext.MessageBox.YESNO,
				icon : Ext.MessageBox.QUESTION,
				fn : function(btn) {
			         if (btn == 'yes') {
				        var ids = [];
				        for ( var i = 0; i < selection.length; i++) {
				            ids.push(selection[i].get('dbid'));
				        }
				        FHD.ajax({
			                 url : __ctxPath+ '/icm/icsystem/removeconstructplanrelastandard.f',
			                 params : {
		                          constructPlanRelaStandardIds: ids
				             },
				             callback : function(data) {
				                  if (data) {
				                	  Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
				                      me.store.load();
			                      }
			                 }
				        });
			         }
				}
			});
    	}
    },
    reloadData:function(){
    	var me=this;
    	me.empStore.proxy.extraParams.constructPlanId = me.extraParams.businessId;
    	me.empStore.load();
    	me.store.proxy.extraParams.constructPlanId = me.extraParams.businessId;
    	me.store.load();
    },
    
    onchange :function(){//设置你按钮可用状态
		var me = this;
		me.down('#addConstructRelaEmp').setDisabled(me.getSelectionModel().getSelection().length === 0);
		me.down('#constructPlanListEditDel').setDisabled(me.getSelectionModel().getSelection().length === 0);
	}
});