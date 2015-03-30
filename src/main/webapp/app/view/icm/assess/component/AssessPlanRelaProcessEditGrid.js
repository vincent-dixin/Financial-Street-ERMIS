/*
 * 评价流程的可编辑列表
 * 入参：parameter:{assessPlanId:'评价计划Id',assessPlanType:'评价类型'}
 * */
Ext.define('FHD.view.icm.assess.component.AssessPlanRelaProcessEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.assessplanrelaprocesseditgrid',
	
	url:__ctxPath+ '/icm/assess/findAssessPlanRelaProcessListByPage.f',
	extraParams:{
		businessId:''
	},
	pagable :false,
	cols:new Array(),
	tbarItems:new Array(),
	bbarItems:new Array(),
	
	initComponent:function(){
		var me=this;
		
		me.extraParams.businessId=me.businessId;
		//穿行测试store
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
		    {dataIndex : 'dbid',hidden:true},
			{dataIndex : 'text',hidden:true},
			{dataIndex : 'processId',hidden:true},
			{header : '一级流程分类',dataIndex : 'firstProcessName',sortable : true,flex : 1,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
			},
			{header : '二级流程分类',dataIndex : 'parentProcessName',sortable : true,flex : 1,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
			}, 
			{header : '末级流程',dataIndex :'processName',sortable : true, flex : 2,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
			},
			{
				header : '是否穿行测试',dataIndex :'isPracticeTest',sortable : true, flex : 1, hidden:false,
				editor:Ext.create('Ext.form.ComboBox', {
				    store: isPracticeTestStore,
				    queryMode: 'local',
				    displayField:'text',
				    valueField: 'value'
				}),
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = isPracticeTestStore.find('value',value);
					var record = isPracticeTestStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{
				header : '是否穿行测试',dataIndex :'isPracticeTestShow',sortable : true, flex : 1, hidden:false,
				renderer:function(value){
					var index = isPracticeTestStore.find('value',value);
					var record = isPracticeTestStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{
				header : '穿行次数',dataIndex :'practiceNum',sortable : true, flex : 1, hidden:false,
				editor:{
			        xtype: 'numberfield',
			        value: 1,
			        margin:'0 0 0 0',
			        allowBlank : false,
			        minValue: 1,
			        listeners: {
			        	change: function(field, value) {  
			        		value = parseInt(value,10);  
			        		field.setValue(value);  
			        	}
			        }
			    },
			    renderer:function(value,metaData,record,colIndex,store,view) { 
			    	metaData.tdAttr = 'style="background-color:#FFFBE6"';
			    	return value;
			    }
			},
			{
				header : '是否抽样测试',dataIndex :'isSampleTest',sortable : true,flex : 1, hidden:false,
				editor:Ext.create('Ext.form.ComboBox', {
				    store: isSampleTesttStore,
				    queryMode: 'local',
				    displayField:'text',
				    valueField: 'value'
				}),
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = isPracticeTestStore.find('value',value);
					var record = isPracticeTestStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{
				header : '是否抽样测试',dataIndex :'isSampleTestShow',sortable : true,flex : 1, hidden:false,
				renderer:function(value){
					var index = isPracticeTestStore.find('value',value);
					var record = isPracticeTestStore.getAt(index);
					if(record){
						return record.data.text;
					}else{
						return '';
					}
				}
			},
			{
				header : '抽取样本比例(%)',dataIndex :'coverageRate',sortable : true,flex : 1, hidden:false,
				renderer : function(value, metaData, record, colIndex, store, view) {
					if(value!='' && value!=null){
						metaData.tdAttr = 'data-qtip="可在基础设置的流程发生频率对应抽样比例的表格中调整"'; 
						var newValue=value*100;//toFixed(0);
			 			return newValue;
					}else{
						return value;
					}
				}
			}
		];
		
		//tbar
		me.tbarItems=[
		    {
		    	iconCls : 'icon-add',
		    	id : 'addAssessRelaProcess',
		    	text:'添加',
		    	tooltip: '添加评价范围流程',
		    	handler :me.addAssessRelaProcess,
		    	scope : this
		    },
		    '-',
		    {
		    	iconCls : 'icon-del',
		    	id : 'assessPlanListEditDel',
		    	text:'删除',
		    	tooltip: '删除评价范围流程',
		    	handler :me.delStandard,
		    	scope : this
		    }
		]
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
			if(typeof(item.get('isPracticeTest')) != "string" && typeof(item.get('isSampleTest')) != "string" && !item.get('isPracticeTest') && !item.get('isSampleTest') && item.get('isPracticeTest') == item.get('isSampleTest')){
				validateFlag = true;
			}
		}
		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '穿行测试和抽样测试不能全部为否!');
 			return false;
 		}
		
		var jsonArray=[];
		var rows = me.store.getModifiedRecords();
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
		    url : __ctxPath+ '/icm/assess/mergeAssessPlanRelaProcessBatch.f',
		    params : {
		    	assessPlanId:me.extraParams.businessId,
		    	modifiedRecord:Ext.encode(jsonArray)
			},
			callback : function(data) {
				if (data) {
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
					me.store.load();  
				}
			}
		});
	},
	//新增流程范围
    addAssessRelaProcess:function(){
    	var me=this;
    	
     	var title='流程选择';
     	var selectId='ca_scale_set_measure_business';
		var assessPlanScaleSetMeasure = Ext.create('FHD.ux.dict.DictSelect', {
			dictTypeId : 'ca_scale_set_measure',
			margin : '5 5 5 5',
			labelWidth : 80,
			columnWidth:1,
			name : 'scaleSetMeasure',
			multiSelect : false,
			fieldLabel : '范围选择方式'
		});
		assessPlanScaleSetMeasure.on('afterrender',function(){
			assessPlanScaleSetMeasure.setValue('ca_scale_set_measure_dept');
		});
		//1.按部门选择
		var assessPlanDepart = Ext.create('FHD.ux.process.processRelaOrgSelector', {
			columnWidth:1,
			single : false,	 
			margin: '5 5 5 5',
			value:'10,11',
			height :100,
			labelText: $locale('fhd.process.processselector.labeltext'),
			labelAlign: 'left',
			labelWidth: 80
		});
		//2.按流程选择
		var assessPlanRelaProcess = Ext.create('FHD.ux.process.processSelector',{
		   labelText : '',
		   fieldLabel:'选择流程',
		   single : false,
		   labelWidth : 60,
		   parent:false,
		   value:'',
		   margin: '5 5 5 5',
		   columnWidth:.80
		});

    	var fieldSetTop={
			xtype : 'fieldset',
			layout: 'column',
			margin: '5 5 5 5',
			columnWidth:1,
			collapsed : false,
			collapsible : false,
			title : '范围选择',
			items : [assessPlanScaleSetMeasure]
		};
		
		//分析工具按钮
		var feixiToolButton={
	        xtype:'button',
	        width: 100,
	        text:'按部门选择流程',
	        columnWidth:.20,
	        height: 22,
	        margin: '5 5 5 5',
	        handler:function(){
	        	var processAnalysisToolWindow=Ext.create('FHD.ux.process.processAnalysisToolWindow',{
	        		onSubmit : function(values) {
	        			var resultValue = new Array();
	        			//console.log(assessPlanRelaProcess.getValue());
	        			var val = assessPlanRelaProcess.getValue();
	        			if('' != val){
	            			var valArray = val.split(",");
	            			for(var i=0;i<valArray.length;i++){
	            				resultValue.push(valArray[i]);
	            			}
	        			}
	        			//console.log(values);
	        			if(values.length>0){
	        				for(var j=0;j<values.length;j++){
	        					resultValue.push(values[j]);
	        				}
	        			}
	        			assessPlanRelaProcess.setValue(resultValue);
					}
	        	});
	        	processAnalysisToolWindow.show();
	        }
		};
    	
    	var fieldSetBottom={
			xtype : 'fieldset',
			margin: '5 5 5 5',
			layout: 'column',
			columnWidth:1,
			height:230,
			collapsed : false,
			collapsible : false,
			title :title,
			items : [assessPlanRelaProcess,feixiToolButton]
		};
    	
    	var toolSetBottom={
			xtype : 'fieldset',
			margin: '5 5 5 5',
			layout: 'column',
			columnWidth:1,
			height:250,
			collapsed : false,
			collapsible : false,
			title :title,
			items : [assessPlanDepart]
		};
    	
    	//点击选择弹出框
    	var windows=Ext.create('Ext.window.Window', {
    	    title:'选择流程',
    	    //layout: 'column',
    	    //columnWidth:1,
    	    layout:'fit',
    	    collapsible:true,
    	    modal : true,
    	    maximizable:true,
    	    width: 700,
    	    height:300,
    	    items:[fieldSetBottom,toolSetBottom],
    	    dockedItems:{
	            xtype: 'toolbar',
	            dock: 'bottom',
	            ui: 'footer',
	            items: ['->',
                    {
	            		text: '确定',
	            		handler: function(){
	                    	if(assessPlanRelaProcess.getValue()==''||assessPlanRelaProcess.getValue()==undefined){
		                    	return;
		                    }
	            		    FHD.ajax({
	    	        		     url : __ctxPath+ '/icm/assess/saveAssessPlanRelaProcesses.f',
	    	        		     params : {
	    	        		    	 processIds:assessPlanRelaProcess.getValue(),
	    	        		    	 assessPlanDepartIds:'',
	    	        		    	 selectType:'ca_scale_set_measure_business',
	    	        		    	 assessPlanId:me.extraParams.businessId
	    	        		    	 //assessPlanId:me.businessId
	    	        			 },
	    	        			 callback : function(data) {
	    	        				 if (data) {
		        	        		     windows.close();
		        	        			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		        	        			 me.store.load();
	    	        				 }
	        	        		 }
	        	        	});
		                } 
		           },
		           {
		            	text: '关闭',
		            	handler: function(){
		                    windows.close();
		            	}
				   }
		       ]
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
			            var processIds=new Array();
				        for ( var i = 0; i < selection.length; i++) {
				            ids.push(selection[i].get('id'));
				            processIds.push(selection[i].get('processId'));
				        }
				        FHD.ajax({
			                 url : __ctxPath+ '/icm/assess/removeAssessPlanRelaProcesses.f',
			                 params : {
		                          assessPlanRelaProcessIds: ids,
		                          processIds:processIds,
		                          assessPlanId:me.extraParams.businessId
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
    	
    	me.store.load();
    }
});