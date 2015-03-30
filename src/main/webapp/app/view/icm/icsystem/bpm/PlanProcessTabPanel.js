

Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessTabPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.planprocesstabpanel',
    requires: [
        'FHD.ux.icm.common.FlowTaskBar',
        'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm'
    ],
	autoHeight : true,
    plain: true,
    autoScroll:true,
    //传递的参数对象
    paramObj:{},
    /**
     * 设置激活的tab页签
     */
    setActiveItem:function(index){
    	me = this;
    	me.setActiveTab(index);
    },
    
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    
    initComponent: function() {
        var me = this;
        debugger;
        //基本信息
		me.basicInfo=Ext.widget('constructplanpreviewform',{
			columnWidth:1/1,
			border:false
		});
        //流程列表
        me.planprocesseditdraft = Ext.create('FHD.view.icm.icsystem.bpm.PlanProcessEditDraft',{id:'planprocesseditdraft',executionId : me.executionId});
        me.bbar=[
		    '->',
		    {
	            text: '提交', //保存按钮
	            iconCls: 'icon-operator-submit',
	            handler: function () {
               		me.submit();
	            }
	        }
		];
        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type:  'vbox'
        	},
        	tabBar:{
        		style : 'border-right: 1px  #99bce8 solid;'
        	},
            items: [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.流程和矩阵编写',status:'current'},
	    		{index: 2, context:'2.成果审批',status:'undo'}
	    	]
    		}),me.basicInfo,me.planprocesseditdraft]
        });
        me.callParent(arguments);
    },
    submit : function(){
		var me=this;
		Ext.MessageBox.show({
            title: '提示',
            width: 260,
            msg: '提交后，系统会将该计划提交给下一个处理人审批，您确定计划已经完成并提交吗？',
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') {
					FHD.ajax({//ajax调用
						url : __ctxPath+ '/icm/icsystem/constructplanrelaprocesssubmit.f',
					    params : {
					    	businessId : me.businessId,
					    	executionId : me.executionId
						},
						callback : function(data) {
							if(me.winId){
								Ext.getCmp(me.winId).close();
							}
						}
					});
                }
            }
		});
	},
    reloadData : function() {
    	var me = this;
    	me.basicInfo.initParam({
    		businessId : me.businessId
    	});
    	me.basicInfo.reloadData();
    	me.planprocesseditdraft.initParam({
    		constructPlanId : me.businessId,
    		executionId 	: me.executionId
    	});
    	me.planprocesseditdraft.reloadData();
    }
});