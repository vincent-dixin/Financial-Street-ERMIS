/**
 * 评价计划报告组件
 */
Ext.define('FHD.view.comm.report.assess.CompanyYearReportList', {
	extend : 'FHD.view.comm.report.ReportBaseList',
	alias : 'widget.companyyearreportlist',

	requires: [
		'FHD.view.comm.report.assess.CompanyYearReportForm'
	],
	
	extraParams:{
		reportType : 'company_year_assessment_report'		
	},
	
	initComponent: function(){
		var me = this;
		
		Ext.apply(me,{
			tbarItems : [{
				iconCls: 'icon-add',
				text: '添加',
				id:'company_year_assessment_report_add',
				tooltip: '添加公司年度评价报告',
				handler : me.onSave
			},{
				iconCls: 'icon-edit',
				text: '修改',
				id:'company_year_assessment_report_edit',
				tooltip: '修改公司年度评价报告',
				handler : me.onEdit
			},{
				iconCls: 'icon-del',
				text: '删除',
				id:'company_year_assessment_report_del',
				tooltip: '删除公司年度评价报告',
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
        me.down('#company_year_assessment_report_del').setDisabled(length === 0);
        if(length != 1){
        	me.down('#company_year_assessment_report_edit').setDisabled(true);
        }else{
        	me.down('#company_year_assessment_report_edit').setDisabled(false);
        }
    },
    
	onSave : function(){
		var me = this;
		
		var assessplancenterpanel = this.up('assessplancenterpanel');
		assessplancenterpanel.removeAll(true);
		assessplancenterpanel.add(Ext.widget('companyyearreportform'));
	},
	
	onEdit : function() {
		var me = this.up('companyyearreportlist');
		
		var selection = me.getSelectionModel().getSelection()[0];
		
		var assessplancenterpanel = this.up('assessplancenterpanel');
		assessplancenterpanel.removeAll(true);
		var companyyearreportform = Ext.widget('companyyearreportform',{reportId:selection.get('id')});
		assessplancenterpanel.add(companyyearreportform);
		companyyearreportform.reloadData();
	},
	
	onDel : function(){
		var me = this.up('companyyearreportlist');
		
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
                            url: __ctxPath + '/comm/report/removeReportInfomationByIds.f',
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