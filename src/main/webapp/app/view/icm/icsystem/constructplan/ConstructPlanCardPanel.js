Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.constructplancardpanel',
    
    activeItem: 0,
    
    requires: [
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanForm',
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanRangeForm'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
    tbar: {
        id: 'icm_constructplan_card_topbar',
        items: [
	        {
	            text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
	            iconCls: 'icon-001',
	            id: 'icm_constructplan_card_details_btn_top',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.setBtnState(0);
	            		constructplancardpanel.navBtnHandler(0);
	            	}
	            }
	        },
	        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
	        {
	            text: '范围选择',//范围选择导航按钮
	            iconCls: 'icon-002',
	            id: 'icm_constructplan_card_range_btn_top',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.setBtnState(1);
	            		constructplancardpanel.navBtnHandler(1);
	            	}
	            }
	        }
	    ]
    },
    bbar: {
        id: 'icm_constructplan_card_bbar',
        items: [
	        '->', 
	        {	
	            id: 'icm_constructplan_card_undo_btn' ,
	            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
	            iconCls: 'icon-operator-home',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.undo();
	            	}
	            }
	        },
//	        {
//	            id: 'icm_constructplan_card_close_btn' ,
//	            text: '关闭',
//	            iconCls: 'icon-operator-back',
//	            handler: function () {
//	            	var constructplandraft = this.up('constructplandraft');
//	            	if(constructplandraft.winId){
//	            		Ext.getCmp(constructplandraft.winId).close();
//	            	}
//	            }
//	        },
	        {
	            id: 'icm_constructplan_card_pre_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
	            iconCls: 'icon-operator-back',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.back();
	            	}
	            }
	        }, 
	        {
	            id: 'icm_constructplan_card_next_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
	            iconCls: 'icon-operator-next',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.last();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.save"),//保存按钮
	            id: 'icm_constructplan_card_finish_btn' ,
	            iconCls: 'icon-control-stop-blue',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.finish();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.submit"),//提交按钮
	            id: 'icm_constructplan_card_submit_btn' ,
	            iconCls: 'icon-operator-submit',
	            handler: function () {
	            	var constructplancardpanel = this.up('constructplancardpanel');
	            	if(constructplancardpanel){
	            		constructplancardpanel.submit();
	            	}
	            }
	        }
	    ]
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.constructplanform = Ext.widget('constructplanform',{
        	businessId:me.businessId,
        	executionId:me.executionId,
        	editflag:me.editflag,
        	border:false
        });
        me.constructplanrangeform = Ext.widget('constructplanrangeform',{
        	businessId:me.businessId,
        	editflag:me.editflag,
        	border:false
        });
        
        Ext.applyIf(me, {
            items: [
                me.constructplanform,
                me.constructplanrangeform
            ]
        });

        me.callParent(arguments);
        
        if(me.executionId){
    		Ext.getCmp('icm_constructplan_card_undo_btn').hidden=true;
//    		Ext.getCmp('icm_constructplan_card_close_btn').hidden=false;
    		me.setNavBtnEnable(false,true);
        }else{
//    		Ext.getCmp('icm_constructplan_card_close_btn').hidden=true;
    		Ext.getCmp('icm_constructplan_card_undo_btn').hidden=false;
    	}
    },
    /**
     * 返回按钮事件
     */
    undo:function(){
    	var me = this;
    	var constructplanmainpanel = this.up('constructplaneditpanel');
    	if(constructplanmainpanel){
    		constructplanmainpanel.navBtnHandler(0);
    		constructplanmainpanel.constructplanlist.reloadData();
    	}
    },
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
    	/*
    	if(me.constructplanrangeform.saveFunc()){
    		me.setBtnState(0);
    		me.navBtnHandler(me,0);
    	}
    	*/
    	me.constructplanrangeform.constructPlanGrid.saveDataUndoVailidate();
    	me.setBtnState(0);
		me.setActiveItem(0);
		me.navBtnState();
    },
    /**
     * 下一步按钮事件
     */
    last:function(){
    	var me = this;
    	if(me.constructplanform.saveFunc(me)){
    		Ext.getCmp('icm_constructplan_card_range_btn_top').setDisabled(false);
    		me.setBtnState(1);
    		me.navBtnHandler(1);
    	}
    },
    /**
     * 完成按钮事件
     */
    finish:function(){
    	var me = this;
        var activePanel = me.getActiveItem();
        var items = me.items.items;
        var index = Ext.Array.indexOf(items, activePanel);
        if(0 == index){
 	    	//基本信息刷新
        	me.constructplanform.saveFunc(me);
 	    }else if(1 == index){
 	    	//范围选择刷新
            me.constructplanrangeform.constructPlanGrid.saveData();
 	    }
    },
    /**
     * 提交按钮事件
     */
    submit:function(){
		var me = this;
		Ext.MessageBox.show({
            title: '提示',
            width: 260,
            msg: '提交后，系统会将该计划提交给下一个处理人审批，您确定计划已经完成并提交吗？',
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') {
			    	//判断流程列表不能为空
			    	var count = me.constructplanrangeform.constructPlanGrid.store.getCount();
			    	if(count == 0){
			    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'建设计划未选择建设范围!');
			    		return;
			    	}
			    	if(!me.constructplanrangeform.constructPlanGrid.saveData()){
			    		return false;
			    	}
			        if(me.businessId){
			        	me.initParam({
			        		businessId : me.businessId
			        	});
			        }
					//提交工作流
			    	FHD.ajax({
				        url: __ctxPath + '/icm/icsystem/constructplandraft.f',
				        async:false,
				        params: {
				        	businessId: me.paramObj.businessId,
				        	executionId: me.executionId
				        },
				        callback: function (data) {
				        	if(data){
			                	//所有按钮不可用，提交成功后跳转到列表
						    	Ext.getCmp('icm_constructplan_card_pre_btn').setDisabled(true);
						        Ext.getCmp('icm_constructplan_card_next_btn').setDisabled(true);
						       // Ext.getCmp('icm_constructplan_card_finish_btn').setDisabled(true);
						        Ext.getCmp('icm_constructplan_card_submit_btn').setDisabled(true);
				        		var constructplaneditpanel = me.up('constructplaneditpanel');
				            	if(constructplaneditpanel){
				            		constructplaneditpanel.navBtnHandler(0);
				            		constructplaneditpanel.constructplanlist.reloadData();
				            	}else{
				            		var constructplandraft = me.up('constructplandraft');
					            	if(constructplandraft.winId){
					            		Ext.getCmp(constructplandraft.winId).close();
					            	}
				            	}
				        	}
				        }
			    	});
                }
            }
		});
    },
    /**
     * 设置导航按钮的事件函数
     * @param {panel} cardPanel cardpanel面板
     * @param index 面板索引值
     */
    navBtnHandler: function (index) {
 	   	var me = this;
 	    me.setActiveItem(index);
 	    me.navBtnState();
 	    if(0 == index){
 	    	//基本信息刷新
 	    	me.constructplanform.initParam(me.paramObj);
            me.constructplanform.reloadData();
 	    }else if(1 == index){
 	    	//范围选择刷新
 	    	debugger;
            me.constructplanrangeform.initParam(me.paramObj);
            me.constructplanrangeform.reloadData();
 	    }
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	Ext.getCmp('icm_constructplan_card_pre_btn' ).setDisabled(!layout.getPrev());
        Ext.getCmp('icm_constructplan_card_next_btn' ).setDisabled(!layout.getNext());
//        Ext.getCmp('icm_constructplan_card_finish_btn').setDisabled(!layout.getNext());
        Ext.getCmp('icm_constructplan_card_submit_btn' ).setDisabled(!layout.getPrev());
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('icm_constructplan_card_topbar');
        var btns = topbar.items.items;
        for (var i = 0; i < btns.length; i++) {
            var item = btns[i];
            if (item.pressed != undefined) {
                if (k == index) {
                    item.toggle(true);
                } else {
                    item.toggle(false);
                }
                k++;
            }
        }
    },
    /**
     * 设置tbar导航按钮状态:false可用，true不可用
     */
    setNavBtnEnable:function(v,first){
    	var me=this;
    	if(first){
    		//修改
    		Ext.getCmp('icm_constructplan_card_details_btn_top').setDisabled(v);
    		Ext.getCmp('icm_constructplan_card_range_btn_top').setDisabled(v);
    	}else{
    		//新增
    		Ext.getCmp('icm_constructplan_card_details_btn_top').setDisabled(first);
    		Ext.getCmp('icm_constructplan_card_range_btn_top').setDisabled(v);
    	}
    	me.setBtnState(0);
    	me.navBtnHandler(0);
    },
    /**
     * 初始化tbar和bbar按钮状态
     */
    setInitBtnState:function(){
    	var me = this;
    	
		if(me.paramObj.editFlag){
			//编辑评价计划
        	me.setNavBtnEnable(false,true);
        }else{
        	//添加评价计划
        	me.removeAll(true);
	        me.constructplanform = Ext.widget('constructplanform',{
	        	businessId:me.businessId,
	        	executionId:me.executionId,
	        	editflag:me.editflag
        	});
        	me.add(me.constructplanform);
        	me.constructplanrangeform = Ext.widget('constructplanrangeform',{
	        	businessId:me.businessId,
	        	editflag:me.editflag
        	});
        	me.add(me.constructplanrangeform);	
        	me.setNavBtnEnable(true,false);
        }
    },
    reloadData:function(){
    	var me=this;
    	me.setInitBtnState();
    }
});