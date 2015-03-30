/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.comm.report.icsystem.ConstructTestReportForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructtestreportform',
	
    requires: [
    	'FHD.view.comm.report.icsystem.ConstrcutTestReportBaseForm'
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
        
        me.formPanel = Ext.widget('constrcuttestreportbaseform',{
        	flex:1,
        	bbar:['->',{   
	            	   text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),
	                   iconCls: 'icon-operator-home',
	                   handler: me.cancel
	               },{
        		iconCls : 'icon-control-stop-blue',
        		text : FHD.locale.get("fhd.common.save"),
        		handler: me.onSave
        	},'-',{
        		iconCls : 'icon-operator-submit',
        		text : FHD.locale.get("fhd.common.submit"),
        		handler : me.onSubmit
        	}]
        });
        
        me.form = me.formPanel.getForm();
        
        Ext.applyIf(me, {
            items: [
            	Ext.widget('flowtaskbar',{
    				jsonArray:[
			    		{index: 1, context:'1.测试报告制定',status:'current'}
	    			]
    			}),me.formPanel]
        });

        me.callParent(arguments);
    },
    onSave : function(){
    	var me = this.up('constructtestreportform');
    	if(me.form.isValid()) {
    		FHD.submit({
				form : me.form,
				params : {
					reportDataText: me.formPanel.editor.html()
				},
				url : __ctxPath + '/comm/report/saveconstructplantestreport.f',
				callback: function (data) {
					var centerpanel = me.up('panel');
			    	centerpanel.removeAll();
					centerpanel.add(Ext.widget('constructplantestreportlist'));
				}
			});
		}
    },
    cancel : function(){
    	var me = this.up('constructtestreportform');
   	    var centerpanel = me.up('panel');
    	centerpanel.removeAll();
		centerpanel.add(Ext.widget('constructplantestreportlist'));
    },
    onSubmit : function() {
    	var me = this.up('constructtestreportform');
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
            url: __ctxPath + '/comm/report/findconstructionplantestreportbyid.f',
            params: {
            	reportId: me.reportId
            },
            // form加载数据成功后回调函数
            success: function (form, action) {
            	var responseJson = Ext.JSON.decode(action.response.responseText);
            	//加载报告内容
            	me.formPanel.editor.html(responseJson.data.reportData);
            	//加载评价计划控件
            	Ext.each(responseJson.data.reportRelaConstructlist,function(v){
            		me.down('constructplanselector').grid.store.insert(0,v);
            	});
            	//刷新建设范围
            	me.formPanel.constructPlanGrid.extraParams.businessId = responseJson.data.constructplanId;
            	me.formPanel.constructPlanGrid.reloadData();
                return true;
            },
 	        failure: function (form, action) {
 	     	   return false;
 	        }
        });
    }
});