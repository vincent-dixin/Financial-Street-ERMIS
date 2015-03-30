/**
 *    @description 展示列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.icm.icsystem.FlowRiskListForView', {
	extend : 'FHD.ux.GridPanel',
	requires: [
    	'FHD.view.icm.icsystem.form.RiskEditFormForView'
    ],
	alias : 'widget.flowrisklistforview',
    pagable :false,
	cols:new Array(),
	tbarItems:new Array(),
	bbarItems:new Array(),
	sortableColumns : false,
	checked : false,
	//可编辑列表为只读属性
	readOnly : false,
	searchable : false,
	url: __ctxPath + '/processrisk/findProcessRiskListByPage.f',
	paramObj : {},
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	initComponent : function() {
		var me = this;
		me.cols = [{
					header : '编号',
					dataIndex : 'code',
					sortable : true,
					flex : 1
				}, {
					header : '名称',
					dataIndex : 'name',
					sortable : true,
					flex : 1,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('flowrisklistforview').showRiskEditView('" + record.data.id + "')\" >" + value + "</a>"; 
					}
				},{
					header : '风险描述',
					dataIndex : 'desc',
					sortable : true
				},{
					header : '控制措施数量',
					dataIndex : 'measureNum',
					sortable : true
				}];
        Ext.apply(me,{ 
        			extraParams: {
               			id: undefined,
                		editflag: undefined
            		}})
		me.callParent(arguments);
	},
	reloadData :function(){
		var me = this;
        me.store.proxy.extraParams.processId = me.paramObj.processId;
		me.store.load();
	},
	showRiskEditView:function(id){
    	var me=this;
    	me.riskeditformforview=Ext.widget('riskeditformforview',{processRiskId:id});
		me.riskeditformforview.initParam({
			processRiskId : id
		});
		me.riskeditformforview.reloadData();
		me.riskeditformforview.getInitData();
		var win = Ext.create('FHD.ux.Window',{
			title:'风险详细信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.riskeditformforview]
    	}).show();
    }
});