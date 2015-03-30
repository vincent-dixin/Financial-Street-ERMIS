/*
 * 评价流程的可编辑列表
 * 入参：parameter:{assessPlanId:'评价计划Id',assessPlanType:'评价类型'}
 * */
Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanRelaStandardViewGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.constructplanrelastandardviewgrid',
	url: '',
	extraParams:{
		businessId:''
	},
	pagable :false,
	checked : false,
	cols:new Array(),
	tbarItems:new Array(),
	bbarItems:new Array(),
	searchable : true,
	border : false,
	sortableColumns : false,
	initComponent:function(){
		var me=this;
		me.cols=[
			{header : '标准名称',dataIndex : 'standardName',flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}},
			{header : '内控要求',dataIndex : 'controlRequirement',flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}},
			{header : '内控要素',dataIndex :'controlPoint',flex : 2},
			{header : '末级流程',dataIndex :'processName',flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
		    		metaData.tdAttr = 'data-qtip="'+value+'"';
					return value; 
				}},
			{header : '责任部门',dataIndex :'standardRelaOrg',flex : 1},
			{header : '是否合规诊断',dataIndex :'isNormallyDiagnosis',flex : 1, hidden:false},
			{header : '是否流程梳理',dataIndex :'isProcessEdit',flex : 1, hidden:false},
			{header:'建设责任人',dataIndex:'constructPlanEmp'}
		];
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
    reloadData:function(){
    	var me=this;
    	me.store.proxy.url = __ctxPath+ '/icm/icsystem/findconstructplanrelastandardlistbypageforview.f'
    	me.store.proxy.extraParams.constructPlanId = me.extraParams.businessId;
    	me.store.load();
    }
});