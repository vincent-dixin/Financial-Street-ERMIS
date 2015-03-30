/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.comm.report.assess.TestReportForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.testreportform',
	
    requires: [
    	'FHD.view.comm.report.assess.TestReportBaseForm'
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
        
        me.formPanel = Ext.widget('testreportbaseform',{
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
    onSave : function(){
    	var me = this.up('testreportform');
    	
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportDataText: me.formPanel.editor.html()
				},
				url : __ctxPath + '/comm/report/saveTestReport.f',
				callback: function (data) {
					var centerpanel = me.up('panel');
			    	centerpanel.removeAll();
					centerpanel.add(Ext.widget('testreportlist'));
				}
			});
		}
    },
    onSubmit : function() {
    	var me = this.up('testreportform');
    	
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportData: me.formPanel.editor.html()
				},
				url : __ctxPath + '/comm/report/savecompanyyearreportsubmit.f',
				callback: function (data) {
					var centerpanel = me.up('panel');
			    	centerpanel.removeAll();
					centerpanel.add(Ext.widget('testreportlist'));
				}
			});
		}  	
    },
    reloadData: function() {
    	var me = this;
    	
	    me.form.waitMsgTarget = true;
    	me.form.load({
            waitMsg: '加载中...',
            url: __ctxPath + '/comm/report/findAssessPlanTestReportbyId.f',
            params: {
            	reportId: me.reportId
            },
            // form加载数据成功后回调函数
            success: function (form, action) {
            	var responseJson = Ext.JSON.decode(action.response.responseText);
            	//加载报告内容
            	me.formPanel.editor.html(responseJson.data.reportData);
            	//加载评价计划控件
            	Ext.each(responseJson.data.reportRelaAssesslist,function(v){
            		me.down('assessplanselector').grid.store.insert(0,v);
            	});
            	//刷新评价范围列表
            	//me.formPanel.processGrid.store.proxy.extraParams.assessplanId = responseJson.data.assessplanId;
            	//me.formPanel.processGrid.store.load()
            	//刷新缺陷认定列表
    			//me.formPanel.defectGrid.store.proxy.extraParams.assessplanId = responseJson.data.assessplanId;
            	//me.formPanel.defectGrid.store.load();
            	
                return true;
            },
 	        failure: function (form, action) {
 	     	   return false;
 	        }
        });
    }
});