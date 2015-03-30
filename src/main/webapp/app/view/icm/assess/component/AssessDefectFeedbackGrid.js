Ext.define('FHD.view.icm.assess.component.AssessDefectFeedbackGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.assessdefectfeedbackgrid',
	
	url:__ctxPath+'/icm/assess/findAssessRelaFeedbackDefectListByAssessPlanId.f',
	extraParams:{
		assessPlanId:'',
		executionId:'',
		isOwnerOrg:''
	},
	
	pagable:false,
	checked:false,
	defectsIsAvailable:true,
	feedbackIsAvailable:false,
	
	initComponent:function(){
		var me=this;
		
		me.extraParams.assessPlanId=me.businessId;
		me.extraParams.executionId=me.executionId;
		me.extraParams.isOwnerOrg=me.isOwnerOrg;
		
		me.levelStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath+'/sys/dic/findDefectDictEntryByTypeId.f?typeId=ca_defect_level'
			}
		});
		me.levelStore.load();
		me.typeStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath+'/sys/dic/findDefectDictEntryByTypeId.f?typeId=ca_defect_type'
			}
		});
		me.typeStore.load();
		me.isAgreeStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath+'/sys/dic/findDefectDictEntryByTypeId.f?typeId=0yn'
			}
		});
		me.isAgreeStore.load();
		me.orgStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath+'/sys/org/cmp/deptListByCompanyId.f'
			},
			reader : {
				type : 'json',
				root : 'deptList'
			}
		});
		me.orgStore.load();
		/*
		me.isStore=Ext.create('Ext.data.Store',{//myLocale的store
			fields : ['id', 'name'],
			data : [{'id' : 'Y','name' : '是'},{'id' : 'N','name' : '否'}]
		});
		*/
		me.cols=[
		    {header:'缺陷id',dataIndex:'defectId',hidden:true},
		    {header:'末级流程ID',dataIndex:'processId',hidden:true},
			{header:'缺陷关联Id',dataIndex:'assessRelaDefectId',hidden:true},
			{header:'流程分类', dataIndex: 'parentProcess', sortable: false,flex:1,hidden:true,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header:'末级流程', dataIndex: 'processName', sortable: false,flex:2,hidden:false,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header:'评价点', dataIndex: 'pointName',sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header:'评价人',dataIndex:'executeEmpName',sortable: false,flex:2},
			{header:'评价日期',dataIndex:'assessDate',sortable: false,flex:2},
			{header:'复核人',dataIndex:'reviewerEmpName',sortable: false,flex:2},
			{header:'复核日期',dataIndex:'reviewDate',sortable: false,flex:2}
		];
		
		if (me.defectsIsAvailable) {
        	var responsibilityOrg = {
    			header:'整改责任部门<font color=red>*</font>', 
    			dataIndex: 'orgId',
    			sortable: false,
    			flex:2,
    			editor:Ext.create('FHD.ux.org.DeptSelect',{
    				allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
		    	emptyCellText:'<font color="#808080">请选择</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = me.orgStore.find('id',value);
					var record = me.orgStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="整改责任部门是缺陷的责任部门，必填，评价完成后，缺陷要由整改责任部门进行整改" style="background-color:#FFFBE6"';
						}
					}
				}
        	};
        	var defectDesc = {
				header:'缺陷描述<font color=red>*</font>', 
				dataIndex: 'desc',
				sortable: false,
				flex:2,
				editor:{
					allowBlank : false
				},
				emptyCellText:'<font color="#808080">请填写</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="缺陷描述是对缺陷的详细描述，必填" style="background-color:#FFFBE6"';
					}
				}
			};
            var defectLevel = {
				header:'缺陷级别<font color=red>*</font>', dataIndex: 'level',sortable: false,flex:2,
				editor:Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		    		dictTypeId:'ca_defect_level',
		    		allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
		    	emptyCellText:'<font color="#808080">请选择</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = me.levelStore.find('id',value);
					var record = me.levelStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="缺陷级别是描述缺陷的等级情况，必填" style="background-color:#FFFBE6"';
						}
					}
		    	}
		    };
		    
		    var defectType = {
		    	header:'缺陷类型<font color=red>*</font>', dataIndex: 'type',sortable: false,flex:2,
		    	editor:Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		    		dictTypeId:'ca_defect_type',
		    		allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
		    	emptyCellText:'<font color="#808080">请选择</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = me.typeStore.find('id',value);
					var record = me.typeStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="缺陷类型是描述缺陷的类别，必填，穿行测试默认为设计缺陷，抽样测试默认为执行缺陷" style="background-color:#FFFBE6"';
						}
					}
				}
		    };
		    me.colInsert(10, responsibilityOrg);
		    me.colInsert(11, defectDesc);
            me.colInsert(12, defectLevel);
            me.colInsert(13, defectType);
        }else{
        	var defectDesc = {header:'缺陷描述', dataIndex: 'desc',sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
        	};
        	var responsibilityOrg = {header:'整改责任部门', dataIndex: 'orgId',sortable: false,flex:2,
    			renderer:function(value){
					var index = me.orgStore.find('id',value);
					var record = me.orgStore.getAt(index);
					if(record){
  						return record.data.name;
					}else{
  						return value;
					}
				}
        	};
        	var defectLevel = {
 				header:'缺陷级别', dataIndex: 'level',sortable: false,flex:2,
    			renderer:function(value){
					var index = me.levelStore.find('id',value);
					var record = me.levelStore.getAt(index);
					if(record){
  						return record.data.name;
					}else{
  						return value;
					}
				}
 		    };
 		    
 		    var defectType = {
 		    	header:'缺陷类型', dataIndex: 'type',sortable: false,flex:2,
    			renderer:function(value){
					var index = me.typeStore.find('id',value);
					var record = me.typeStore.getAt(index);
					if(record){
  						return record.data.name;
					}else{
  						return value;
					}
				}
 		    };
 		    me.colInsert(10, responsibilityOrg);
 		    me.colInsert(11, defectDesc);
 		    me.colInsert(12, defectLevel);
 		    me.colInsert(13, defectType);
        }
		
		if (me.feedbackIsAvailable) {
			var whetherOrNotPass = {
				header:'是否通过'+'<font color="red">*</font>', dataIndex: 'isAgree',sortable: false,flex:2,
				editor:Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		    		dictTypeId:'0yn',
		    		allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
		    	emptyCellText:'<font color="#808080">请选择</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = me.isAgreeStore.find('id',value);
					var record = me.isAgreeStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="“是否通过”指评价出来的缺陷是否存在，必填" style="background-color:#FFFBE6"';
						}
					}
				}
			};
			var feedback= {
				header:'反馈意见', dataIndex: 'feedback',sortable: false,flex:2,
				editor: {
					allowBlank: true
				},
				emptyCellText:'<font color="#808080">请填写</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'style="background-color:#FFFBE6"';
		    		if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="反馈意见指“是否通过”为否时，需要说明评价出来的缺陷不存在的理由或意见" style="background-color:#FFFBE6"';
					} 
				}
			};
            
            me.colInsert(14, whetherOrNotPass);
            me.colInsert(15, feedback);
        }else{
        	var whetherOrNotPass = {
				header:'是否通过', dataIndex: 'isAgree',sortable: false, flex:2,
				renderer:function(value){
					var index = me.isAgreeStore.find('id',value);
					var record = me.isAgreeStore.getAt(index);
					if(record!=null){
							return record.data.name;
					}else{
						return value;
					}
				}
			};
			var feedback= {
				header:'反馈意见', dataIndex: 'feedback',sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			};
            
            me.colInsert(14, whetherOrNotPass);
            me.colInsert(15, feedback);
        }
		 
		me.callParent(arguments);
		/*
		me.store.on('beforeload',function(){
			me.levelStore.load();
			me.typeStore.load();
			me.orgStore.load();
		});
		*/
	},
	colInsert: function (index, item) {
        if (index < 0) return;
        if (index > this.cols.length) return;
        for (var i = this.cols.length - 1; i >= index; i--) {
            this.cols[i + 1] = this.cols[i];
        }
        this.cols[index] = item;
    },
    reloadData:function(){
    	var me=this;
    	
    }
});