/**
 *    @description 展示列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.icm.icsystem.FlowNoteListForView', {
	extend : 'FHD.ux.GridPanel',
	requires: [
    	'FHD.view.icm.icsystem.form.NoteEditFormForView'
    ],
	alias : 'widget.flownotelistforview',
	url: __ctxPath + '/process/findprocesspointlistbypage.f',
	pagable :false,
	cols:new Array(),
	tbarItems:new Array(),
	bbarItems:new Array(),
	sortableColumns : false,
	checked : false,
	//可编辑列表为只读属性
	readOnly : false,
	searchable : false,
	paramObj : {},
	initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
	initComponent : function() {
		var me = this;
		me.on('selectionchange',me.onchange);//选择记录发生改变时改变按钮可用状态
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
						return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('flownotelistforview').showNoteEditView('" + record.data.id + "')\" >" + value + "</a>"; 
					}
				}, {
					header : '责任部门',
					dataIndex : 'orgName',
					sortable : true
				}, {
					header : '责任人',
					dataIndex : 'responsilePersionId',
					sortable : true
				}];
        Ext.apply(me,{ 
        			extraParams: {
               			id: undefined,
                		editflag: undefined
            		}});
		me.callParent(arguments);
	},
	reloadData :function(){
		var me = this;
        me.store.proxy.extraParams.processId = me.paramObj.processId;
		me.store.load();
	},
	onchange :function(){//设置你按钮可用状态
		var me = this;
	},
	showNoteEditView:function(id){
    	var me=this;
    	me.noteeditformforview=Ext.widget('noteeditformforview',{processPointId:id});
		me.noteeditformforview.initParam({
			processPointId : id
		});
		me.noteeditformforview.reloadData();
		var win = Ext.create('FHD.ux.Window',{
			title:'流程节点详细信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.noteeditformforview]
    	}).show();
    }
});