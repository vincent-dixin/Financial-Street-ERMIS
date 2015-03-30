/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.wp.WorkPlanForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workplanform',
	
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
        
    },
    
    reloadData: function() {
    	var me = this,
    		workPlanId = me.workPlanId;
    	
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
            	// 责任人赋值
                return true;
            }
        });
    	
    },
    
	/*
	 * 保存
	 */    
    onSave : function(){
    	
    	var me = this.up('workplanform');
    	var jsonArray = me.formPanel.buildMilestoneStore();
    	if(me.form.isValid() && me.formPanel.validateMilestone()) {
    		FHD.submit({
				form : me.form,
				params : {
					milestoneData: Ext.JSON.encode(jsonArray)
				},
				url : __ctxPath + '/wp/saveworkplan.f',
				callback: function (data) {
					var workplancenterpanel = me.up('panel');
			    	workplancenterpanel.removeAll();
					workplancenterpanel.add(Ext.widget('workplanlist'));
				}
			});
		}
    },
    
    onSubmit : function() {
    	
    	var me = this.up('workplanform');
    	var jsonArray = me.formPanel.buildMilestoneStore();
    	if(me.form.isValid() && me.formPanel.validateMilestone(jsonArray)) {
    		FHD.submit({
				form : me.form,
				params : {
					milestoneData: Ext.JSON.encode(jsonArray),
					transition : 'y'
				},
				url : __ctxPath + '/wp/saveworkplansubmit.f',
				callback: function (data) {
					var workplancenterpanel = me.up('panel');
			    	workplancenterpanel.removeAll();
					workplancenterpanel.add(Ext.widget('workplanlist'));
				}
			});
		}    	
    },
    
    /*
     * 预览
     */
    onView : function(){
    	
    	var me = this.up('workplanform');
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