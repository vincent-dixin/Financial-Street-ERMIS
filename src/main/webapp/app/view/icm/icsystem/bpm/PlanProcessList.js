/**
 *    @description 用于在流程梳理阶段展示流程列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessList', {
	extend : 'FHD.ux.GridPanel',
	alias : 'widget.planprocesslist',
	searchable:false,
	pagable : false,
	border : false,
	autoHeight : true,
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
		me.callParent(arguments);
	},
	scollWindow:function(type,id){
//		var findpanel = function(comp,key){
//			if(typeof comp.defaultType != "undefined" && comp.defaultType == 'panel'){
//				return true;
//			}
//		}
		var planprocesstabpanel = this.up('panel').up('panel');
		var me = this;
		me.up('panel').items.items[1].initParam({
			processId : id
		});
//		Ext.each(me.up('panel').items.items,function(item){
//			console.info(item.alias);
//		});
//		var panels = me.up('panel').items.filterBy(findpanel);
//		panels.each(function(comp){
//			console.info(comp.id);
//		})
		me.up('panel').items.items[1].setVisible(true);
//		me.up('panel').items.items[1].setHeight(498);
		if(type == 'processEdit'){  //刷新流程基本信息
			me.up('panel').items.items[1].floweditpanelforworkflow.initParam({
				processId : id
			});
			me.up('panel').items.items[1].floweditpanelforworkflow.reloadData();
			me.up('panel').items.items[1].setActiveItem(me.up('panel').items.items[1].floweditpanelforworkflow);
		}else if(type == 'noteEdit'){
			me.up('panel').items.items[1].flownotemainpanel.initParam(
				{
					processId : id
				});
		    me.up('panel').items.items[1].flownotemainpanel.flownotelist.paramObj.processId = id;
			me.up('panel').items.items[1].flownotemainpanel.reloadData();
			me.up('panel').items.items[1].setActiveItem(me.up('panel').planprocessedittabpanel.flownotemainpanel);
		}else if(type == 'riskEdit'){
			me.up('panel').items.items[1].riskmeasuremainpanel.initParam(
				{
					processId : id
				});
		    me.up('panel').items.items[1].riskmeasuremainpanel.flowrisklist.paramObj.processId = id;
			me.up('panel').planprocessedittabpanel.riskmeasuremainpanel.reloadData();
			me.up('panel').planprocessedittabpanel.setActiveItem(me.up('panel').planprocessedittabpanel.riskmeasuremainpanel);
		}
		planprocesstabpanel.scrollBy(0,50 +planprocesstabpanel.items.items[1].items.items[0].getHeight(), true);
	},
	getChildElementHeight : function(obj){
		var height = 0;
		for(var i = 0;i < obj.items.length;i++){
			height += obj.items.items[i].getHeight();				
		}
		return height;
	},
	reloadData :function(obj){
		var me = this;
        me.store.proxy.url = __ctxPath + '/process/findprocesslistbypage.f',
        me.store.proxy.extraParams = me.paramObj;
		me.store.load();
	},
	reloadRepairData :function(){
		var me = this;
        me.store.proxy.url = __ctxPath + '/process/findprocesslistapprovebypage.f',
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