/**
 * 我的数据
 * 我的缺陷
 * @author 吴德福
 */
Ext.define('FHD.view.icm.statics.IcmMyDefectInfo', {
 	extend: 'Ext.container.Container',
 	alias: 'widget.icmmydefectinfo',
 	overflowX: 'hidden',
	overflowY: 'auto',
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
    	 
		 me.gird = Ext.create('FHD.ux.GridPanel', {
			cols: [
				{dataIndex:'id',hidden:true},
				{ header: '缺陷描述',  dataIndex: 'defectDesc' ,flex: 3 ,
					renderer:function(value,metaData,record,colIndex,store,view) {
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showDefectView('" + record.data.id + "','defect')\" >" + value + "</a>"; 
					}
				},
				{ header: '缺陷等级', dataIndex: 'defectLevel', flex: 1},
				{ header: '缺陷类型', dataIndex: 'defectType', flex: 1},
				{ header: '整改状态', dataIndex: 'dealStatus', flex: 1}, 
				{ header: '整改责任部门', dataIndex: 'orgName', flex: 1},
				{ header: '更新日期', dataIndex: 'updateDate', width:90}
			],
			url: __ctxPath+'/icm/statics/finddefectbysome.f',
			extraParams:{pagable:true,orgId:me.orgid},
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
    showDefectView:function(defectId){
    	var me = this;
    	
    	var grid = Ext.create('FHD.view.icm.defect.form.DefectFormForView',{defectId:defectId,readOnly:true});;
    	grid.reloadData();
    	me.win=Ext.create('FHD.ux.Window',{
			title : '详细查看',
			flex:1,
			autoHeight:true,
			collapsible : true,
			modal : true,
			maximizable : true,
			listeners:{
				close : function(){
				}
			},
			items:[grid]
		}).show();
    },
    //导出grid列表
    exportChart:function(item, pressed){
    	var me=this;
    	if(me.gird.getStore().getCount()>0){
    		FHD.exportExcel(me.gird,'exportexcel','缺陷数据');
    	}else{
    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'没有要导出的数据!');
    	}
    }
});