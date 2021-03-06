/*
 * 评价产生的缺陷的可编辑列表 
 * */
 Ext.define('FHD.view.icm.assess.component.AssessDefectEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.assessdefecteditgrid',
	
	url: __ctxPath + '/icm/assess/findAssessRelaDefectListByPage.f',
	extraParams:{
		assessPlanId:'',
		executionId:'',
	},
	checked:false,
	isEditable:true,
	border:false,
	
	initComponent:function(){
		var me=this;
		
		me.extraParams.assessPlanId=me.businessId;
		me.extraParams.executionId=me.executionId;
		
		me.levelStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/sys/dic/findDefectDictEntryByTypeId.f?typeId=ca_defect_level'
			}
		});
		me.levelStore.load();
		me.typeStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/sys/dic/findDefectDictEntryByTypeId.f?typeId=ca_defect_type'
			}
		});
		me.typeStore.load();
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
		
		me.cols=[
		    {header:'缺陷Id',dataIndex:'defectId',hidden:true},
			{header:'末级流程ID',dataIndex:'processId',hidden:true},
		    {header:'参评人ID',dataIndex:'assessorId',hidden:true},
		    {header:'缺陷关联表Id',dataIndex:'assessRelaDefectId',hidden:true},
		    {header:'流程分类', dataIndex: 'parentProcess', sortable: false,flex:1,
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}
		    },
		    {header:'末级流程', dataIndex: 'processName', sortable: false,flex:1,
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}
		    },
		    {header:'评价点', dataIndex: 'pointName',sortable: false,flex:1,
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
		    },
		    {header:'控制措施', dataIndex: 'measureName',sortable: false,flex:1,
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
		    },
		    {header:'评价人', dataIndex: 'assessorName',sortable: false,flex:2}
		];
		
		if(me.isEditable){
			var defectDesc = {
				header:'缺陷描述<font color=red>*</font>', dataIndex: 'desc',sortable: false,flex:2,editor: {allowBlank: false},emptyCellText:'<font color="#808080">请填写</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="缺陷描述是对缺陷的详细描述，必填" style="background-color:#FFFBE6"';
					}
				}
		    }
		    var defectLevel = {
		    	header:'缺陷级别<font color=red>*</font>', dataIndex: 'level',sortable: false,flex:1,emptyCellText:'<font color="#808080">请选择</font>',
		    	editor:Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		    		dictTypeId:'ca_defect_level',
		    		allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
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
		    }
		    var defectType = {
		    	header:'缺陷类型<font color=red>*</font>', dataIndex: 'type',sortable: false,flex:1,emptyCellText:'<font color="#808080">请选择</font>',
		    	editor:Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		    		dictTypeId:'ca_defect_type',
		    		allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
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
		    }
		    var responsibilityOrg = {
		    	header:'整改责任部门<font color=red>*</font>', dataIndex: 'orgId',sortable: false,flex:2,emptyCellText:'<font color="#808080">请选择</font>',
		    	editor:Ext.create('FHD.ux.org.DeptSelect',{
    				allowBlank : false,
		    		editable : false,
		    		mode : "local",
					triggerAction : "all",
		    		fieldLabel:''
		    	}),
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
		    }
		    
			me.colInsert(9, responsibilityOrg);
		    me.colInsert(10, defectDesc);
            me.colInsert(11, defectLevel);
            me.colInsert(12, defectType);
		}else{
			var defectDesc = {
				header:'缺陷描述', dataIndex: 'desc',sortable: false,flex:2,
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
		    }
		    var defectLevel = {
		    	header:'缺陷级别', dataIndex: 'level',sortable: false,flex:1,
		    	renderer:function(value){
					var index = me.levelStore.find('id',value);
					var record = me.levelStore.getAt(index);
					if(record){
  						return record.data.name;
					}else{
  						return value;
					}
		    	}
		    }
		    var defectType = {
		    	header:'缺陷类型', dataIndex: 'type',sortable: false,flex:1,
				renderer:function(value){
					var index = me.typeStore.find('id',value);
					var record = me.typeStore.getAt(index);
					if(record){
  						return record.data.name;
					}else{
  						return value;
					}
				}
		    }
		    var responsibilityOrg = {
		    	header:'整改责任部门', dataIndex: 'orgId',sortable: false,flex:2,
		    	renderer:function(value){
					var index = me.orgStore.find('id',value);
					var record = me.orgStore.getAt(index);
					if(record){
  						return record.data.name;
					}else{
  						return value;
					}
				}
		    }
			
			me.colInsert(9, responsibilityOrg);
		    me.colInsert(10, defectDesc);
            me.colInsert(11, defectLevel);
            me.colInsert(12, defectType);
		}
		
		Ext.applyIf(me,{
			layout:{
				type:'fit'
			},
			cols: me.cols,
            checked: me.checked
		});
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
		var me = this;
		
		var rows = me.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
			url:__ctxPath + '/icm/defect/saveDefects.f',
            params: {
               jsonString:Ext.encode(jsonArray),
               assessPlanId:me.businessId
            },
            callback: function (data) {
            	if(data){
            		me.store.load();
            	}
            }
		});			
	},
	loadData:function(businessId,editflag){
		var me=this;
		
		me.businessId=businessId;
		me.editflag=editflag;
		me.extraParams.assessPlanId=me.businessId;
		me.store.load();
	},
	reloadData:function(){
		var me=this;
		me.store.load();
	}
});