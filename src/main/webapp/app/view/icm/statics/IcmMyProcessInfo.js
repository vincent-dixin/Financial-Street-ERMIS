/**
 * 我的数据
 * 我的流程
 * @author 邓广义
 */
Ext.define('FHD.view.icm.statics.IcmMyProcessInfo', {
    alias: 'widget.icmmyprocessinfo',
 	extend: 'Ext.container.Container',
 	overflowX: 'hidden',
	overflowY: 'auto',
	flex:1,
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
    	 me.callParent(arguments);
		 me.gird = Ext.create('FHD.ux.GridPanel', {
				cols: [
					{dataIndex:'id',hidden:true},
					{dataIndex:'orgId',hidden:true},
					{dataIndex:'empId',hidden:true},
					{
					 	header: '统计',
					    xtype:'actioncolumn',
					    dataIndex: '',
					    align: 'center',
					    width: 70,
					    items: [{
					        iconCls: 'icon-table',
					        tooltip: '评价统计',
					        handler: function(grid, rowIndex, colIndex) {
			                    var rec = grid.getStore().getAt(rowIndex);
			                    me.showAssessResultStatics(rec.get('processName'),rec.get('id'));
			                }
					    },'-',{
					    	iconCls: 'icon-chart-bar',
					        tooltip: '缺陷统计',
					        handler: function(grid, rowIndex, colIndex) {
			                    var rec = grid.getStore().getAt(rowIndex);
			                    me.showDefectStatics(rec.get('processName'),rec.get('id'));
			                }
					    }/*,'-',{
					    	icon: __ctxPath + '/images/icons/icon_browse_dis.gif',
					    	tooltip: '',
					        handler: me.showSampleTestList
					    },'-',{
					        icon: __ctxPath + '/images/icons/icon_browse_r.gif',
					        tooltip: '',
					        handler: me.showSampleTestAllList
					    }*/]
					},
					{ header: '流程编号',  dataIndex: 'processCode' ,flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
								metaData.tdAttr = 'data-qtip="'+value+'" '; 
								return value; 
						}
					},
					{ header: '流程名称', dataIndex: 'processName', flex: 2 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
								metaData.tdAttr = 'data-qtip="'+value+'" '; 
								return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showProcessView('" + record.data.id + "')\" >" + value + "</a>"; 
						}
					},
					{ header: '流程分类', dataIndex: 'parentName', flex: 1,
						renderer:function(value,metaData,record,colIndex,store,view) {
								metaData.tdAttr = 'data-qtip="'+value+'" '; 
								return value; 
						}
					},
					{ header: '发生频率', dataIndex: 'frequency', flex: .3},
					{ header: '责任部门', dataIndex: 'orgName', flex: .6},
					{ header: '责任人', dataIndex: 'empName', flex: .5},
					{ header: '更新日期', dataIndex: 'updateDate', width: 90}
					/*{ header: '操作', dataIndex: '', width: 50 , hideable: false,
						renderer:function(value,metaData,record,colIndex,store,view) {
								return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').showDefectStatics('" + record.get("id") + "')\"><div class='TipDiv'>aaaaa</div></a>"; 
						}
					},*/
					
				],
				url: __ctxPath+'/icm/statics/findprocessbysome.f',
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
    showDefectStatics: function(processName,processId){
    	var me=this;
		var centerPanel = Ext.getCmp('center-panel');
		var tab = centerPanel.getComponent('FHD.view.comm.chart.ChartDashboardPanel'+processId);
		if(tab){
			centerPanel.setActiveTab(tab);
		}else{
			var p = centerPanel.add(Ext.create('Ext.panel.Panel',{
				id:'FHD.view.icm.statics.DefectCountChart'+processId,
				title:processName+'-缺陷统计',
				flex:1,
				closable:true,
				layout:'fit',
				items:[
					Ext.create('FHD.view.icm.statics.DefectCountChart',{
			    		//grid列表url参数--可选
			    		extraParams:{processId: processId}
			    	})
				]
			}));
			centerPanel.setActiveTab(p);
		}
    },
    showAssessResultStatics: function(processName,processId){
    	var me=this;
		var centerPanel = Ext.getCmp('center-panel');
		var tab = centerPanel.getComponent('FHD.view.comm.chart.ChartDashboardPanel'+processId);
		if(tab){
			centerPanel.setActiveTab(tab);
		}else{
			
			var p = centerPanel.add(Ext.create('Ext.panel.Panel',{
				id:'FHD.view.icm.statics.AssessResultCountChart'+processId,
				title:processName+'-评价统计',
				flex:1,
				closable:true,
				layout:'fit',
				items:[
					Ext.create('FHD.view.icm.statics.AssessResultCountChart',{
			    		//grid列表url参数--可选
			    		extraParams:{processId: processId}
			    	})
				]
			}));
			centerPanel.setActiveTab(p);
		}
    },
    showProcessView:function(processid){
    	var me = this;
    	var grid = Ext.create('FHD.view.icm.icsystem.bpm.PlanProcessEditTabPanelForView',{paramObj:{processId:processid},readOnly:true});;
    	grid.reloadData();
    	me.win=Ext.create('FHD.ux.Window',{
			title : '详细查看',
			flex:1,
			autoHeight:true,
			autoScroll:true,
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
    		FHD.exportExcel(me.gird,'exportexcel','流程数据');
    	}else{
    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'没有要导出的数据!');
    	}
    }
});