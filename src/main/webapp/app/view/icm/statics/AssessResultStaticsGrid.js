Ext.define('FHD.view.icm.statics.AssessResultStaticsGrid', {
    alias: 'widget.assessresultstaticsgrid',
 	extend: 'Ext.container.Container',
 	overflowX: 'hidden',
	overflowY: 'auto',
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
    	 me.callParent(arguments);
		 me.gird = Ext.create('FHD.ux.GridPanel', {
				cols: [
					{dataIndex:'id',hidden:true},
					{ header: '评价计划', dataIndex: 'planName', flex: 2 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '评价人', dataIndex: 'empName', flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '流程', dataIndex: 'processName', flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '评价方式', dataIndex: 'assessMeasure', flex: 1,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '流程节点', dataIndex: 'processPointName', flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '控制措施', dataIndex: 'measureName', flex: 2,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '评价点', dataIndex: 'assessPointName', flex: 2,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '样本合格状态', dataIndex: 'isQualified', flex: 1,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '评价完成状态', dataIndex: 'isDone', flex: 1,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" '; 
							return value; 
						}
					},
					{ header: '样本数量', dataIndex: 'sampleCount', width: 65, sortable:false,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="点击数字查看样本列表" '; 
							return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showSampleGrid('" + record.data.id + "')\" >&nbsp;&nbsp;&nbsp;" + value + "&nbsp;&nbsp;&nbsp;</a>"; 
							return value; 
						}
					},
					{ header: '更新日期', dataIndex: 'updateDate', width: 90}
				],
				url: __ctxPath+'/icm/statics/findassessresultbysome.f',
				tbarItems: [
					{iconCls : 'icon-ibm-action-export-to-excel',text:'导出到Excel',tooltip: '把当前列表导出到Excel',handler :me.exportChart,scope : this}
				],
				extraParams:{orgId:me.orgId},
				checked:false,
				searchable:true,
				pagable : true
			});			 
        me.add(me.gird);
    },
    reloadData:function(orgid){
    	var me=this;
    },
    showSampleGrid:function(assessResultId){
    	var me=this;
    	me.samplePanel=Ext.create('FHD.view.icm.assess.component.AssessResultSampleViewGrid',{
    		extraParams:{
				assessResultId:assessResultId
			}
		});
		
		var win = Ext.create('FHD.ux.Window',{
			title:'评价样本信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.samplePanel]
    	}).show();
    },
    //导出grid列表
    exportChart:function(item, pressed){
    	var me=this;
    	if(me.gird.getStore().getCount()>0){
    		FHD.exportExcel(me.gird,'exportexcel','评价结果数据');
    	}else{
    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'没有要导出的数据!');
    	}
    }
});