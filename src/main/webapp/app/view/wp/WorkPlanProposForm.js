/**
 * 
 * 工作计划申请
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.wp.WorkPlanProposForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workplanproposform',
	
    requires: [
    	'FHD.view.wp.WorkPlanView',
    	'FHD.view.wp.WorkPlanBaseForm'
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
        
        me.formPanel = Ext.widget('workplanbaseform',{
        	autoScroll:true,
        	flex:1,
        	border:false,
        	bodyPadding: '0 3 3 3',
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
        me.milestoneGrid = me.formPanel.milestoneGrid;
        me.hiddenId = me.formPanel.hiddenId;
        
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
    
    reloadData: function() {
    	var me = this,
    		workPlanId = me.businessId;
    	
    	me.milestoneGrid.store.proxy.extraParams.workPlanId = workPlanId;	
    	me.milestoneGrid.store.load();
    	
	    me.form.waitMsgTarget = true;
    	me.form.load({
            waitMsg: '加载中...',
            url: __ctxPath + '/wp/findworkplanbyid.f',
            params: {
                workPlanId: workPlanId
            },
            // form加载数据成功后回调函数
            success: function (form, action) {
            	me.hiddenId.setValue(workPlanId);
            	// 实施部门赋值
            	
            	// 责任人赋值
            	
                return true;
            }
        });
    	
    },
    /*
     * 整理里程碑列表数据
     */
    buildMilestoneStore: function(){
    	var me = this;
 		var rows = me.milestoneGrid.store.data.items;
 		var jsonArray = new Array();
		Ext.Array.each(rows, function (item) {
		    jsonArray.push(item.data);
		});
    	return jsonArray;
    },
	/*
	 * 保存
	 */    
    onSave : function(){
    	var me = this.up('workplanproposform');
    	var jsonArray = me.formPanel.buildMilestoneStore();
    	if(me.form.isValid() && me.formPanel.validateMilestone()) {
    		FHD.submit({
				form : me.form,
				params : {
					milestoneData: Ext.JSON.encode(jsonArray)
				},
				url : __ctxPath + '/wp/saveworkplan.f',
				callback: function (data) {
					Ext.getCmp(me.winId).close();
				}
			});
		}
    },
    
    /**
     * 提交
     */
    onSubmit : function() {
    	var me = this.up('workplanproposform');
    	var jsonArray = me.formPanel.buildMilestoneStore();
    	if(me.form.isValid() && me.formPanel.validateMilestone()) {
    		FHD.submit({
				form : me.form,
				params : {
					milestoneData: Ext.JSON.encode(jsonArray),
					processInstanceId : me.executionId
				},
				url : __ctxPath + '/wp/saveworkplansubmit.f',
				callback: function (data) {
					Ext.getCmp(me.winId).close();
				}
			});
		}    	
    },
    
    /*
     * 预览
     */
    onView : function(){
    	var me = this.up('workplanproposform');
    	if(me.hiddenId.getValue() != '') {
    		var workplanview = Ext.widget('workplanview',{
    			workPlanId : me.hiddenId.getValue()
    		});
    		
    		Ext.widget('fhdwindow',{
    			title:'预览',
    			items:[workplanview]
    		}).show();
    		workplanview.reloadData();
    	} else {
    	 	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '<font color="red">保存后才能预览</font>');
    	}
    }
    
    
    

});