/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.comm.report.assess.GroupYearReportCompileForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.groupyearreportcompileform',
	
    requires: [
    	'FHD.view.comm.report.assess.GroupYearReportBaseForm'
    ],
    
    
    frame: false,
    
    // 布局
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    
    border : false,
    
    flowImage : null,
    formPanel : null,
    
    
    
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       	
        me.flowImage = Ext.widget('image',{
        	src : __ctxPath + '/images/wp/psteps31.jpg',
            width: 450
        });
        
        me.formPanel = Ext.widget('companyyearreportbaseform',{
        	flex:1,
        	tbar:['->',{
        		iconCls : 'icon-zoom',
        		text:'预览',
        		handler: me.onView
        	},'-',{
        		iconCls : 'icon-control-play-blue',
        		text:'保存',
        		handler: me.onSave
        	},'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        	}],
        	bbar:['->',{
        		iconCls : 'icon-zoom',
        		text:'预览',
        		handler: me.onView
        	},'-',{
        		iconCls : 'icon-control-play-blue',
        		text:'保存',
        		handler: me.onSave
        	},'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        	}]
        });
        
        me.form = me.formPanel.getForm();
        
        
        Ext.applyIf(me, {
            items: [{
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
            },me.formPanel]
        });

        me.callParent(arguments);
        
        var approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
        	height : 200,
        	executionId : me.executionId
        });
        
        me.formPanel.add({
        	xtype:'fieldset',
        	layout: 'fit',
        	collapsed : true,
        	collapsible: true,
            autoHeight: true,
            autoWidth: true,
            title: '审批意见',
            items:approvalIdeaGrid
        });
        
    },
    
    
	/*
	 * 保存
	 */    
    onSave : function(){
    	
    	var me = this.up('groupyearreportcompileform');
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportData: me.formPanel.editor.html()
				},
				url : __ctxPath + '/comm/report/saveCompanyYearReport.f',
				callback: function (data) {
					Ext.getCmp(me.winId).close();
				}
			});
		}
    },
    
    onSubmit : function() {
    	
    	var me = this.up('groupyearreportcompileform');
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportData: me.formPanel.editor.html(),
					processInstanceId : me.executionId
				},
				url : __ctxPath + '/comm/report/savecompanyyearreportsubmit.f',
				callback: function (data) {
					Ext.getCmp(me.winId).close();
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
                reportId: me.businessId
            },
            // form加载数据成功后回调函数
            success: function (form, action) {
            	var responseJson = Ext.JSON.decode(action.response.responseText);
            	me.formPanel.editor.html(responseJson.data.reportDataText);
            	Ext.each(responseJson.data.reportRelaAssesslist,function(v){
            		me.down('assessplanselector').grid.store.insert(0,v);
            	});
            	
                return true;
            }
        });
    }
    
  

});