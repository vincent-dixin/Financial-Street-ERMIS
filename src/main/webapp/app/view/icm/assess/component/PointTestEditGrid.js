/*
 * 评价点的测试的可编辑列表
 * */
Ext.define('FHD.view.icm.assess.component.PointTestEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.pointtesteditgrid',
	
	url: __ctxPath + '/icm/assess/findAssessResultPageBySome.f',
	extraParams:{
		assessPlanId:'',
		processId:'',
		assessorId:'',
		testType:'ca_assessment_measure_0',
		isAll:''
	},
	cols:[],
	tbar:[],
	tbarItems:[],
	pagable:false,
	checked:false,
	
	initComponent:function(){
		var me=this;
		
		me.extraParams.assessPlanId=me.businessId;
		me.extraParams.processId=me.processId;
		me.extraParams.testType=me.testType;
		me.extraParams.assessorId = me.assessorId;
		me.extraParams.isAll = me.isAll;

		me.isGoodStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
			    {'id' : true,'name' : '是'},
			    {'id' : false,'name' : '否'}
			]
		});
		me.defectStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax', 
				url : __ctxPath + '/icm/defect/findAllDefect.f'
			}
		});
		me.defectStore.load();
		
		me.cols=[
			{header:'操作', dataIndex: '',sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="请点击此处"';
					return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').showSampleWindow('" + record.data.assessResultId + "','through','"+me.processId+"')\" >样本测试</a>"  
				}
			},
			{header:'流程节点', dataIndex: 'processPointName', sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header:'评价点', dataIndex: 'assessPointDesc',sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header:'实施证据', dataIndex: 'assessSampleName',sortable: false,flex:2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';
					return value;  
				}
			},
			{header:'评价结果(Y|N|N/A)', dataIndex: 'assessSampleName',sortable: false,flex:3,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					return  record.data.qualifiedNumber+"|"+record.data.notQualifiedNumber+"|"+record.data.notApplyNumber;
				}
			},
			{header:'自动(合格)', dataIndex: 'hasDefect',sortable: false,width:100,  
				renderer:function(value,metaData,record,colIndex,store,view) { 
					var index = me.isGoodStore.find('id',value);
					var record = me.isGoodStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						return value;
					}
				}
			},
			{header:'调整(合格)', dataIndex: 'hasDefectAdjust',sortable: false,width:100, 
				editor:new Ext.form.ComboBox({
					store :me.isGoodStore,
					valueField : 'id',
					displayField : 'name',
					selectOnTab: true
				}),
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'style="background-color:#FFFBE6"';
					var index = me.isGoodStore.find('id',value);
					var record = me.isGoodStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="如需调整评价结果，请点击这里，否则，无需选择" style="background-color:#FFFBE6"';
						}
					}
				}
			},
			{header:'补充说明', dataIndex: 'adjustDesc',sortable: false,flex:3,editor: {},
				renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="如调整评价结果，请填写“补充说明”，否则，无需填写" style="background-color:#FFFBE6"';
					}
				}
			},
			{header:'缺陷描述', dataIndex: 'comment',sortable: false,flex:3,editor: {},
				renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="如不合格，请填写“缺陷描述”，否则，无需填写" style="background-color:#FFFBE6"';
					}
				}
			},
			
			{dataIndex:'assessResultId',hidden:true},
			{dataIndex:'effectiveNumber',hidden:true},
			{dataIndex:'defectNumber',hidden:true},
			{dataIndex:'qualifiedNumber',hidden:true},
			{dataIndex:'notQualifiedNumber',hidden:true},
			{dataIndex:'notApplyNumber',hidden:true},
			{dataIndex:'testType',hidden:true},
			{dataIndex:'assessPointId',hidden:true},
			{header:'流程节点ID', dataIndex: 'processPointId',hidden:true}
		];
		/*
	    me.on('select',function(){
			var selectionDate=me.getSelectionModel().getSelection();
			if(null!=selectionDate[0].get('assessResultId')){
				self3.assessResultId=selectionDate[0].get('assessResultId');	
			}
		});
		*/
		me.callParent(arguments);
	},
	/*
	listeners:{
		afterrender:function(me){
			me.isGoodStore.load({
				callback:function(){
					//me.loadData(me.businessId,me.processId);
					me.store.load();
				}
			});
		}
	},
	*/
	loadData:function(businessId,processId){
		var me=this;
		
		me.extraParams.assessPlanId = businessId;
		me.extraParams.processId = processId;
		me.store.load();
	},
	reloadData:function(){
    	var me=this;
    	
    	me.store.load();
    }
});