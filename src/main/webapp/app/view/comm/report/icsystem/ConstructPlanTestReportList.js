/**
 * 评价计划报告组件
 */
Ext.define('FHD.view.comm.report.icsystem.ConstructPlanTestReportList', {
	extend : 'FHD.view.comm.report.ReportBaseList',
	alias : 'widget.constructplantestreportlist',

	requires: [
		'FHD.view.comm.report.icsystem.ConstructTestReportForm'
	],
	
	extraParams:{
		reportType : 'test_construct_report'
	},
	
	initComponent: function(){
		var me = this;
		
		Ext.apply(me,{
			tbarItems : [{
				iconCls: 'icon-add',
				text: '添加',
				id:'construct_test_report_add',
				tooltip: '添加测试报告',
				handler : me.onSave
			},'-',{
				iconCls: 'icon-edit',
				text: '修改',
				id:'construct_test_report_edit',
				tooltip: '修改测试报告',
				handler : me.onEdit
			},'-',{
				iconCls: 'icon-del',
				text: '删除',
				id:'construct_test_report_del',
				tooltip: '删除测试报告',
				handler : me.onDel
			}]
		});
		
		me.callParent(arguments);
		
		me.store.on('load', function () {
            me.setstatus()
        });
        me.on('selectionchange', function () {
            me.setstatus()
        });
	},
	setstatus: function(){
    	var me = this;
    	
        var length = me.getSelectionModel().getSelection().length;
        me.down('#construct_test_report_del').setDisabled(length === 0);
        if(length != 1){
        	me.down('#construct_test_report_edit').setDisabled(true);
        }else{
        	me.down('#construct_test_report_edit').setDisabled(false);
        }
    },
	onSave : function(){
		var me = this;
		debugger;
		var constructplancenterpanel = this.up('constructplancenterpanel');
		constructplancenterpanel.removeAll(true);
		constructplancenterpanel.add(Ext.widget('constructtestreportform'));
	},
	onEdit : function(){
		var me = this.up('constructplantestreportlist');
		
		var selection = me.getSelectionModel().getSelection()[0];
		
		var constructplancenterpanel = this.up('constructplancenterpanel');
		constructplancenterpanel.removeAll(true);
		var constructtestreportform = Ext.widget('constructtestreportform',{reportId:selection.get('id')});
		constructplancenterpanel.add(constructtestreportform);
		constructtestreportform.reloadData();
	},
	onDel : function(){
		var me = this.up('constructplantestreportlist');
		
		var selections = me.getSelectionModel().getSelection();
    	if(0 == selections.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.msgDel'));
    	}else{
    		Ext.MessageBox.show({
                title: FHD.locale.get('fhd.common.delete'),
                width: 260,
                msg: FHD.locale.get('fhd.common.makeSureDelete'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn == 'yes') {
                    	var ids='';
                        
                        Ext.Array.each(selections, function (item) {
                            ids += item.get("id") + ",";
                        });

    					if(ids.length>0){
    						ids = ids.substring(0,ids.length-1);
    					}
                        FHD.ajax({
                            url: __ctxPath + '/comm/report/removereportinfomationbyidsforconstruct.f',
                            params: {
                                ids: ids
                            },
                            callback: function (data) {
                                if (data) {
                                    me.store.load();
                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                }else{
                                	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                                }
                            }
                        });
                    }
                }
            });
    	}
	}
});