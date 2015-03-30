/*
 * 评价流程的任务分配可编辑列表
 * 入参：extraParams:{assessPlanId:'评价计划Id'}
 * */
Ext.define('FHD.view.icm.assess.component.AssessPlanProcessRelaEmpEditGrid',{
	extend : 'FHD.ux.EditorGridPanel',
	alias: 'widget.assessplanprocessrelaempeditgrid',
	
	//parameter:{},
	//multiSelect:false,
	pagable:false,
	autoScroll:true,
	checked:false,
	url: __ctxPath + '/icm/assess/findAssessPlanRelaProcessListByPageForAllocation.f',
	extraParams:{
		assessPlanId:''
	},
	//empStore:null,
	
	initComponent : function() {
		var me = this;
		
		me.extraParams.assessPlanId=me.businessId;
		me.empStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/icm/assess/findEmpByAssessPlanId.f?assessPlanId='+me.businessId
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
		    {header:'评价人<font color=red>*</font>', dataIndex: 'handlerId', sortable: false, flex : 2,emptyCellText:'<font color="#808080">请选择</font>',
		    	editor:new Ext.form.ComboBox({
			    	store :me.empStore,
			    	valueField : 'id',
			    	displayField : 'name',
			    	allowBlank : false,
			    	editable : false
			    }),
			    renderer:function(value,metaData,record,colIndex,store,view) { 
			    	metaData.tdAttr = 'style="background-color:#FFFBE6"';
			    	var index = me.empStore.find('id',value);
			    	var record = me.empStore.getAt(index);
			    	if(record){
			    		return record.data.name;
			    	}else{
			    		if(value){
			    			metaData.tdAttr = 'data-qtip="评价人不能和复核人相同!" style="background-color:#FFFBE6"';
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="请选择流程评价人，评价人不能和复核人相同!" style="background-color:#FFFBE6"';
						}
			    	}
			    }
			 },
			 {header:'复核人<font color=red>*</font>', dataIndex: 'reviewerId', sortable: false, flex : 2,emptyCellText:'<font color="#808080">请选择</font>',
				 editor:new Ext.form.ComboBox({
			    	 store :me.empStore,
			    	 valueField : 'id',
			    	 displayField : 'name',
			    	 allowBlank : false,
			    	 editable : false
			     }),
			     renderer:function(value,metaData,record,colIndex,store,view) { 
			    	 metaData.tdAttr = 'style="background-color:#FFFBE6"';
			    	 var empIdHandlerValue=record.data.handlerId;
			    	 var index = me.empStore.find('id',value);
			    	 var record = me.empStore.getAt(index);
			    	 if(record!=null&&empIdHandlerValue==value){
			    		 value='';
			    		 Ext.MessageBox.alert('提示','复核人不能和评价人相同!');
			    	 }else{
			    		 if(record){
			    			 return record.data.name;
				    	 }else{
				    		 if(value){
				    			 metaData.tdAttr = 'data-qtip="复核人不能和评价人相同!" style="background-color:#FFFBE6"';
				    			 return value;
				    		 }else{
				    			 metaData.tdAttr = 'data-qtip="请选择流程复核人，复核人不能和评价人相同!" style="background-color:#FFFBE6"';
				    		 }
				    	 }
			    	 }
			     }
		      }
		];
		me.tbarItems=[
		    {
		    	text :'按部门批量',
		    	iconCls: 'icon-ibm-icon-scorecards',
		    	id:'saveByOrg${param._dc}',
		    	handler:function(){
		    		me.bathSet()
		    	},
		    	disabled: false,
		    	scope : this
		    }
		];
		me.callParent(arguments);
	},
	save:function(){
		var me=this;
		
		var validateFlag = false;
		var count = me.store.getCount();
		for(var i=0;i<count;i++){
			var item = me.store.data.get(i);
 			if(item.get('handlerId')!='' && item.get('reviewerId')!='' && item.get('handlerId')==item.get('reviewerId')){
				validateFlag = true;
			}
		}
		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '复核人不能和评价人相同!');
 			return false;
 		}
		
		var rows = me.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		
		FHD.ajax({
			url : __ctxPath + '/icm/assess/mergeAssessPlanProcessRelaOrgEmpBatch.f',
			params : {
				assessPlanId:me.businessId,
				jsonString:Ext.encode(jsonArray)
			},
			callback : function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				}
			}
		});
		me.store.commitChanges(); 
	},
	bathSet:function(){
		var me=this;
		
		var empStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/icm/assess/findEmpByAssessPlanId.f?assessPlanId='+me.businessId
			}
		});
		var editByOrgGrid=Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
			columnWidth : 1,
			pagable:false,
			checked:false,
			url: __ctxPath + '/icm/assess/findOrgListByPage.f?assessPlanId='+me.businessId,
			cols:[
			   {dataIndex:'assessPlanRelaProcessId',hidden:true},
			   {dataIndex:'orgId',hidden:true},
			   {header:'部门', dataIndex: 'orgName', sortable: false, flex : 1},
			   {header:'评价人', dataIndex: 'handlerId', sortable: false,flex:1,
				   editor:new Ext.form.ComboBox({
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
			   {header:'复核人', dataIndex: 'reviewerId', sortable: false, flex : 1,
				   editor:new Ext.form.ComboBox({
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
					   if(record!=null && empIdHandlerValue==value){
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
			   }
			]
		});
		var mergeAssessPlanRelaPoint=Ext.create('FHD.ux.Window',{
			title:'按部门批量修改',
			layout:'fit',
			modal:true,//是否模态窗口
			collapsible:false,
			height:380,
			width:380,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[editByOrgGrid],
			buttonAlign: 'center',
			buttons: [
				{ 
					text: '保存',
					//iconCls: 'icon-control-fastforward-blue',
					handler:function(){
						var rows = editByOrgGrid.store.getModifiedRecords();
			    		var jsonArray=[];
			    		Ext.each(rows,function(item){
			    			jsonArray.push(item.data);
			    		});
			    		FHD.ajax({
			    			url : __ctxPath + '/icm/assess/mergerAssessPlanProcessRelaOrgEmpBatch.f',
			    			params : {
			    				jsonString:Ext.encode(jsonArray),
			    				assessPlanId:me.businessId
			    			},
			    			callback : function(data){
			    				if(data){
			    					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
			    					mergeAssessPlanRelaPoint.close();
			    					me.store.load();
			    				}
			    			}
			    		});
					}
				},
			    { 
			    	text: '关闭',
			    	//iconCls: 'icon-control-fastforward-blue',
			    	handler:function(){
			    		mergeAssessPlanRelaPoint.close();
			    	}
			    }
			]
		}).show();
   },
   reloadData:function(){
	   var me=this;
	   
   }
});