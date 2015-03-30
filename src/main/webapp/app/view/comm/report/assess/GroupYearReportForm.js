/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.comm.report.assess.GroupYearReportForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.groupyearreportform',
	
    requires: [
    	'FHD.view.comm.report.assess.GroupYearReportBaseForm'
    ],
    
    frame: false,
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    border : false,
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       	
        me.flowImage = Ext.widget('image',{
        	src : __ctxPath + '/images/wp/psteps31.jpg',
            width: 450
        });
        
        me.formPanel = Ext.widget('companyyearreportbaseform',{
        	flex:1,
        	bbar:['->',{
        		iconCls : 'icon-control-play-blue',
        		text:'保存',
        		handler: me.onSave
        	}/*,'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        	}*/]
        });
        
        me.form = me.formPanel.getForm();
        
        
        Ext.applyIf(me, {
            items: [/*{
            	xtype:'container',
            	height: 50,
            	style:'border-bottom: 1px  #99bce8 solid !important;',
            	layout:{
            		align: 'stretch',
        			type: 'hbox'
            	},
            	items:[{
            		xtype:'image',
            		src : __ctxPath + '/images/wp/zuo.jpg',
            		flex:1
            	},me.flowImage,{
            		xtype:'image',
            		src : __ctxPath + '/images/wp/you.jpg',
            		flex:1
            	}]
            },*/me.formPanel]
        });

        me.callParent(arguments);
        
    },
	/*
	 * 保存
	 */    
    onSave : function(){
    	
    	var me = this.up('groupyearreportform');
    	
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportData: me.formPanel.editor.html()
				},
				url : __ctxPath + '/comm/report/saveCompanyYearReport.f?reportType=group_year_assessment_report',
				callback: function (data) {
					var centerpanel = me.up('panel');
			    	centerpanel.removeAll();
					centerpanel.add(Ext.widget('groupyearreportlist'));
				}
			});
		}
    },
    /*
     * 提交
     */
    onSubmit : function() {
    	
    	var me = this.up('groupyearreportform');
    	
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportData: me.formPanel.editor.html()
				},
				url : __ctxPath + '/comm/report/savecompanyyearreportsubmit.f?reportType=group_year_assessment_report',
				callback: function (data) {
					var centerpanel = me.up('panel');
			    	centerpanel.removeAll();
					centerpanel.add(Ext.widget('groupyearreportlist'));
				}
			});
		}
    	
    },
    reloadData : function() {
    	var me = this;
    	
    	me.form.waitMsgTarget = true;
    	me.form.load({
            waitMsg: '加载中...',
            url: __ctxPath + '/comm/report/findCompanyYearReport.f',
            params: {
                reportId: me.reportId
            },
            // form加载数据成功后回调函数
            success: function (form, action) {
            	var responseJson = Ext.JSON.decode(action.response.responseText);
            	//加载报告内容
            	me.formPanel.editor.html(responseJson.data.reportDataText);
            	//加载评价计划控件
            	Ext.each(responseJson.data.reportRelaAssesslist,function(v){
            		me.down('assessplanselector').grid.store.insert(0,v);
            	});
            	
                return true;
            }
        });
    }
});