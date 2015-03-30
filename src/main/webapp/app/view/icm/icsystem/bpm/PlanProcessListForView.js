/**
 *    @description 展示列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessListForView', {
	extend : 'FHD.ux.GridPanel',
	alias : 'widget.planprocesslistforview',
	pagable :false,
	cols:new Array(),
	tbarItems:new Array(),
	bbarItems:new Array(),
	sortableColumns : false,
	checked : false,
	//可编辑列表为只读属性
	readOnly : false,
	searchable : false,
	initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
	initComponent : function() {
		var me = this;
		me.cols = [{
					header : '标准名称',
					dataIndex : 'standardName',
					sortable : false,
					flex : 1,
					renderer:function(value,metaData,record,colIndex,store,view) { 
			    		metaData.tdAttr = 'data-qtip="'+value+'"';
						return value; 
					}
				},{
					header : '内控要求',
					dataIndex : 'standardRequir',
					sortable : false,
					flex : 1,
					renderer:function(value,metaData,record,colIndex,store,view) { 
			    		metaData.tdAttr = 'data-qtip="'+value+'"';
						return value; 
					}
				},{
					header : '流程编号',
					dataIndex : 'processCode',
					sortable : false,
					flex : 1
				}, {
					header : '流程名称',
					dataIndex : 'processName',
					sortable : false,
					flex : 1
				}, {header:'操作',
					dataIndex:'operate', 
					sortable: false,
					flex : 2}];
        Ext.apply(me);
        Ext.getDom()
		me.callParent(arguments);
	},
	scollWindow:function(type,id){
		var planprocesstabpanel = this.up('panel').up('panel');
		planprocesstabpanel.scrollBy(0, 350, true);
		var me = this;
		var planprocesseditapprove = me.up('panel').items.items[1];
		planprocesseditapprove.initParam({
			processId : id
		});
		planprocesseditapprove.setVisible(true);
		planprocesseditapprove.floweditpanelforview.initParam({
			processId : id
		});
		planprocesseditapprove.floweditpanelforview.reloadData();
		planprocesseditapprove.flownotemainpanelforview.initParam(
			{
				processId : id
			});
		planprocesseditapprove.flownotemainpanelforview.reloadData();
		planprocesseditapprove.riskmeasuremainpanelforview.initParam(
			{
				processId : id
			});
		planprocesseditapprove.riskmeasuremainpanelforview.reloadData();
	},
	reloadData :function(){
		var me = this;
        me.store.proxy.url = __ctxPath + '/process/findprocesslistapprovebypage.f';
        me.store.proxy.extraParams = me.paramObj;
		me.store.load();
	},
    loadApproveData :function(){
		var me = this;
		me.store.proxy.url = __ctxPath + '/process/findprocesslistapprovebypage.f';
        me.store.proxy.extraParams = me.paramObj;
		me.store.load();
	}
});