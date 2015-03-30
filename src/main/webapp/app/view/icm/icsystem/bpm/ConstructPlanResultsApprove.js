

Ext.define('FHD.view.icm.icsystem.bpm.ConstructPlanResultsApprove', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructplanresultsapprove',
   requires: [
        'FHD.ux.icm.common.FlowTaskBar',
        'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm'
    ],

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
        	 //审批意见
		me.ideaApproval=Ext.create('FHD.view.comm.bpm.ApprovalIdea',{
			columnWidth:1/1,
			executionId:me.executionId
		});
        //基本信息
		me.basicInfo=Ext.widget('constructplanpreviewform',{
			columnWidth:1/1,
			businessId:me.businessId,
			border:false
		});
        //流程列表
        me.planprocesseditapprove = Ext.create('FHD.view.icm.icsystem.bpm.PlanProcessEditApprove',{id:'planprocesseditapprove',executionId : me.executionId,border : false,plain:false});
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
	    		{index: 1, context:'1.流程和矩阵编写',status:'done'},
	    		{index: 2, context:'2.成果审批',status:'current'}
	    	]
    		}),me.basicInfo,me.planprocesseditapprove,me.ideaApproval]
        });
        me.bbar=[
		    '->',
		    {
				text:'提交',
				iconCls: 'icon-operator-submit',
				handler: function () {
					//提交工作流
					me.submit(me.ideaApproval.isPass,me.ideaApproval.getValue());
					this.setDisabled(true);
	            }
			}
		];
        me.callParent(arguments);
    },
    submit:function(isPass,examineApproveIdea){
		var me=this;
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/icsystem/planprocesseditapproval.f',
		    params : {
		    	businessId:me.businessId,
	   			executionId:me.executionId,
		    	isPass : isPass,
		    	examineApproveIdea : examineApproveIdea
			},
			callback : function(data) {
				if(me.winId){
					Ext.getCmp(me.winId).close();
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
    	me.planprocesseditapprove.initParam({
    		constructPlanId : me.businessId,
    		executionId 	: me.executionId
    	});
    	me.planprocesseditapprove.reloadData();
    }
});