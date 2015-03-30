Ext.define('FHD.view.icm.assess.AssessPlanCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.assessplancardpanel',
    
    activeItem: 0,
    border:false,
    requires: [
       'FHD.view.icm.assess.form.AssessPlanForm',
       'FHD.view.icm.assess.form.AssessPlanRangeForm'
    ],
    
    tbar: {
        id: 'icm_assessplan_card_topbar',
        items: [
	        {
	            text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
	            iconCls: 'icon-001',
	            id: 'icm_assessplan_card_details_btn_top',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.setBtnState(0);
	            		assessplancardpanel.navBtnHandler(this.up('panel'), 0);
	            	}
	            }
	        },
	        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
	        {
	            text: '范围选择',//范围选择导航按钮
	            iconCls: 'icon-002',
	            id: 'icm_assessplan_card_range_btn_top',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.setBtnState(1);
	            		assessplancardpanel.navBtnHandler(this.up('panel'), 1);
	            	}
	            }
	        }
	    ]
    },
    bbar: {
        id: 'icm_assessplan_card_bbar',
        items: [
	        '->', 
	        {
	            id: 'icm_assessplan_card_undo_btn' ,
	            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
	            iconCls: 'icon-operator-home',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.undo();
	            	}
	            }
	        },
	        /*
	        {
	            id: 'icm_assessplan_card_close_btn' ,
	            text: '关闭',
	            iconCls: 'icon-arrow-undo',
	            handler: function () {
	            	var assessplanbpmone = this.up('assessplanbpmone');
	            	if(assessplanbpmone.winId){
	            		Ext.getCmp(assessplanbpmone.winId).close();
	            	}
	            }
	        },
	        */
	        {
	            id: 'icm_assessplan_card_pre_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
	            iconCls: 'icon-operator-back',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.back();
	            	}
	            }
	        }, 
	        {
	            id: 'icm_assessplan_card_next_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
	            iconCls: 'icon-operator-next',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.last();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.save"),//保存按钮
	            id: 'icm_assessplan_card_finish_btn' ,
	            iconCls: 'icon-control-stop-blue',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.finish();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.submit"),//提交按钮
	            id: 'icm_assessplan_card_submit_btn' ,
	            iconCls: 'icon-operator-submit',
	            handler: function () {
	            	var assessplancardpanel = this.up('assessplancardpanel');
	            	if(assessplancardpanel){
	            		assessplancardpanel.submit();
	            	}
	            }
	        }
	    ]
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.assessplanform = Ext.widget('assessplanform',{
        	businessId:me.businessId,
        	executionId:me.executionId,
        	editflag:me.editflag
        });
        me.assessplanrangeform = Ext.widget('assessplanrangeform',{
        	businessId:me.businessId,
        	editflag:me.editflag
        });
        
        Ext.applyIf(me, {
            items: [
                me.assessplanform,
                me.assessplanrangeform
            ]
        });

        me.callParent(arguments);
        
        if(me.executionId){
    		Ext.getCmp('icm_assessplan_card_undo_btn').hidden=true;
    		//Ext.getCmp('icm_assessplan_card_close_btn').hidden=false;
    		me.setNavBtnEnable(false,true);
        }else{
    		//Ext.getCmp('icm_assessplan_card_close_btn').hidden=true;
    		Ext.getCmp('icm_assessplan_card_undo_btn').hidden=false;
    	}
    },
    /**
     * 返回按钮事件
     */
    undo:function(){
    	var me = this;
    	var assessplanmainpanel = this.up('assessplanmainpanel');
    	if(assessplanmainpanel){
    		assessplanmainpanel.navBtnHandler(0);
    	}
    },
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
    	/*
    	if(me.assessplanrangeform.saveFunc()){
    		me.setBtnState(0);
    		me.navBtnHandler(me,0);
    	}
    	*/
    	//保存流程信息
    	me.assessplanrangeform.assessPlanGrid.saveData();
    	me.setBtnState(0);
    	if(me.businessId){
    		me.navBtnHandler(me,0);
    	}else{
    		me.setActiveItem(0);
			me.navBtnState();
			var assessplanmainpanel = this.up('assessplanmainpanel');
			if(assessplanmainpanel){
    			me.businessId = assessplanmainpanel.paramObj.businessId;
			}
			me.assessplanform.loadData(me.businessId,true);
    	}
    },
    /**
     * 下一步按钮事件
     */
    last:function(){
    	var me = this;
    	
    	if(me.assessplanform.saveFunc(me)){
    		Ext.getCmp('icm_assessplan_card_range_btn_top').setDisabled(false);
    		me.editflag=true;
    		me.setBtnState(1);
    		if(me.businessId){
    			me.navBtnHandler(me,1);
    		}else{
    			me.setActiveItem(1);
    			me.navBtnState();
    			//评价范围列显示控制
    			me.assessplanrangeform.operateColumn();
    		}
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
        //me.setBtnState(index);
        
        if(0 == index){
 	    	//基本信息刷新
        	me.assessplanform.saveFunc();
 	    }else if(1 == index){
 	    	//范围选择刷新
            me.assessplanrangeform.assessPlanGrid.saveData();
 	    }
    },
    /**
     * 提交按钮事件
     */
    submit:function(){
    	var me = this;
    	
    	//判断流程列表不能为空
    	var count = me.assessplanrangeform.assessPlanGrid.store.getCount();
    	if(count == 0){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'评价计划未选择评价范围!');
    		return;
    	}
    	
    	var validateFlag = false;
		var count = me.assessplanrangeform.assessPlanGrid.store.getCount();
		for(var i=0;i<count;i++){
			var item = me.assessplanrangeform.assessPlanGrid.store.data.get(i);
			if(typeof(item.get('isPracticeTest')) != "string" && typeof(item.get('isSampleTest')) != "string" && !item.get('isPracticeTest') && !item.get('isSampleTest') && item.get('isPracticeTest') == item.get('isSampleTest')){
				validateFlag = true;
			}
		}
		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '穿行测试和抽样测试不能全部为否!');
 			return false;
 		}
    	
    	//保存流程列表
    	var jsonArray=[];
		var rows = me.assessplanrangeform.assessPlanGrid.store.getModifiedRecords();
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
		    url : __ctxPath+ '/icm/assess/mergeAssessPlanRelaProcessBatch.f',
		    params : {
		    	assessPlanId:me.businessId,
		    	modifiedRecord:Ext.encode(jsonArray)
			},
			callback : function(data) {
				if (data) {
					Ext.MessageBox.show({
			            title: '提示',
			            width: 260,
			            msg: '提交后将不能修改，您确定要提交么?',
			            buttons: Ext.MessageBox.YESNO,
			            icon: Ext.MessageBox.QUESTION,
			            fn: function (btn) {
			                if (btn == 'yes') {
            					//所有按钮不可用，提交成功后跳转到列表
            			    	Ext.getCmp('icm_assessplan_card_pre_btn').setDisabled(true);
            			        Ext.getCmp('icm_assessplan_card_next_btn').setDisabled(true);
            			        Ext.getCmp('icm_assessplan_card_finish_btn').setDisabled(true);
            			        Ext.getCmp('icm_assessplan_card_submit_btn').setDisabled(true);
            			        	
            					//提交工作流
            			    	FHD.ajax({
            				        url: __ctxPath + '/icm/assess/assessPlanDraft.f',
            				        async:false,
            				        params: {
            				        	businessId: me.businessId,
            				        	executionId: me.executionId
            				        },
            				        callback: function (data) {
            				        	if(data){
            				        		var assessplanmainpanel = me.up('assessplanmainpanel');
            				            	if(assessplanmainpanel){
            				            		assessplanmainpanel.navBtnHandler(0);
            				            	}else{
            				            		var assessplanbpmone = me.up('assessplanbpmone');
            					            	if(assessplanbpmone.winId){
            					            		Ext.getCmp(assessplanbpmone.winId).close();
            					            	}
            				            	}
            				        	}
            				        }
            			    	});
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
    navBtnHandler: function (cardPanel, index) {
 	   	var me = this;
 	   	
 	    cardPanel.setActiveItem(index);
 	    me.navBtnState();
 	    if(0 == index){
 	    	//基本信息刷新
            me.assessplanform.loadData(me.businessId,me.editflag);
 	    }else if(1 == index){
 	    	//评价范围列显示控制
    		me.assessplanrangeform.operateColumn();
 	    	//评价范围刷新
            me.assessplanrangeform.loadData(me.businessId,me.editflag);
 	    }
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	Ext.getCmp('icm_assessplan_card_pre_btn' ).setDisabled(!layout.getPrev());
        Ext.getCmp('icm_assessplan_card_next_btn' ).setDisabled(!layout.getNext());
        //Ext.getCmp('icm_assessplan_card_finish_btn').setDisabled(!layout.getNext());
        Ext.getCmp('icm_assessplan_card_submit_btn' ).setDisabled(!layout.getPrev());
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('icm_assessplan_card_topbar');
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
    		Ext.getCmp('icm_assessplan_card_details_btn_top').setDisabled(v);
    		Ext.getCmp('icm_assessplan_card_range_btn_top').setDisabled(v);
    	}else{
    		//新增
    		Ext.getCmp('icm_assessplan_card_details_btn_top').setDisabled(first);
    		Ext.getCmp('icm_assessplan_card_range_btn_top').setDisabled(v);
    	}
    	me.setBtnState(0);
    	me.navBtnHandler(me,0);
    },
    /**
     * 初始化tbar和bbar按钮状态
     */
    setInitBtnState:function(editflag){
    	var me = this;
    	
		if(editflag){
			//编辑评价计划
        	me.setNavBtnEnable(false,true);
        }else{
        	//添加评价计划
        	me.setNavBtnEnable(true,false);
        }
    },
    loadData:function(businessId,editflag){
    	var me=this;
    	
    	me.businessId = businessId;
    	me.editflag = editflag;
    	me.setInitBtnState(editflag);
    },
    reloadData:function(){
    	var me=this;
    	
    }
});