/**
 * 我的数据
 * 我的风险
 * @author 邓广义
 */
Ext.define('FHD.view.icm.statics.IcmMyRiskInfo', {
    alias: 'widget.icmmyriskinfo',
 	extend: 'Ext.container.Container',
 	overflowX: 'hidden',
	overflowY: 'auto',
	requires: [
    	'FHD.view.icm.icsystem.form.RiskEditFormForView'
    ],
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
		 me.gird = Ext.create('FHD.ux.GridPanel', {
				cols: [
					{dataIndex:'id',hidden: true},
					{dataIndex:'parentId',hidden: true},
					{dataIndex:'orgId',hidden: true},
					{ header: '风险编号',  dataIndex: 'riskCode' ,flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
								return value; 
						}
					},
					{ header: '风险名称', dataIndex: 'riskName', flex: 2,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'"'; 
								return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showRiskView('" + record.data.id + "')\" >" + value + "</a>"; 
						}
					},
					{ header: '风险分类', dataIndex: 'parentName', flex: 2,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'"'; 
								return value; 
						}
					},
					{ header: '责任部门', dataIndex: 'orgName', flex: 2,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'"'; 
								return value; 
						}
					},
					{header:'更新日期',dataIndex:'updateDate',width:90}
					
				],
				url: __ctxPath+'/icm/statics/findriskbysome.f',
				extraParams:{orgId:me.orgId},
				tbarItems: [
					{iconCls : 'icon-ibm-action-export-to-excel',text:'导出到Excel',tooltip: '把当前列表导出到Excel',handler :me.exportChart,scope : this}
				],
				checked:false,
				searchable:true,
				pagable : true
			});			 
		
				 
	
        


        me.callParent(arguments);
        me.add(me.gird);
    },
    reloadData:function(orgid){
    	var me=this;

    },
    showRiskView:function(id){
		var me=this;
    	me.riskeditformforview=Ext.widget('riskeditformforview',{processRiskId:id});
		me.riskeditformforview.initParam({
			processRiskId : id
		});
		me.riskeditformforview.reloadData();
		me.riskeditformforview.getInitData();
		var win = Ext.create('FHD.ux.Window',{
			title:'详细查看',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.riskeditformforview]
    	}).show();
    },
    //导出grid列表
    exportChart:function(item, pressed){
    	var me=this;
    	if(me.gird.getStore().getCount()>0){
    		FHD.exportExcel(me.gird,'exportexcel','风险数据');
    	}else{
    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'没有要导出的数据!');
    	}
    }
});