Ext.define('FHD.ux.icm.assess.AssessPlanSelectorCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.assessplanselectorcardpanel',
    
    activeItem: 0,
    
    requires: [
       'FHD.view.icm.assess.form.AssessPlanForm',
       'FHD.view.icm.assess.form.AssessPlanRangeForm'
    ],
    paramObj:{
    	businessId:'',
    	name:''
    },
    
    tbar: {
        id: 'icm_assessplan_selector_card_topbar',
        items: [
	        {
	            text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
	            iconCls: 'icon-001',
	            id: 'icm_assessplan_selector_card_details_btn_top',
	            handler: function () {
	            	var assessplanselectorcardpanel = this.up('assessplanselectorcardpanel');
	            	if(assessplanselectorcardpanel){
	            		assessplanselectorcardpanel.setBtnState(0);
	            		assessplanselectorcardpanel.navBtnHandler(this.up('panel'), 0);
	            	}
	            }
	        },
	        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
	        {
	            text: '范围选择',//范围选择导航按钮
	            iconCls: 'icon-002',
	            id: 'icm_assessplan_selector_card_range_btn_top',
	            handler: function () {
	            	var assessplanselectorcardpanel = this.up('assessplanselectorcardpanel');
	            	if(assessplanselectorcardpanel){
	            		assessplanselectorcardpanel.setBtnState(1);
	            		assessplanselectorcardpanel.navBtnHandler(this.up('panel'), 1);
	            	}
	            }
	        }
	    ]
    },
    bbar: {
        id: 'icm_assessplan_selector_card_bbar',
        items: [
	        '->', 
	        {
	            id: 'icm_assessplan_selector_card_pre_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
	            iconCls: 'icon-operator-back',
	            handler: function () {
	            	var assessplanselectorcardpanel = this.up('assessplanselectorcardpanel');
	            	if(assessplanselectorcardpanel){
	            		assessplanselectorcardpanel.back();
	            	}
	            }
	        }, 
	        {
	            id: 'icm_assessplan_selector_card_next_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
	            iconCls: 'icon-operator-next',
	            handler: function () {
	            	var assessplanselectorcardpanel = this.up('assessplanselectorcardpanel');
	            	if(assessplanselectorcardpanel){
	            		assessplanselectorcardpanel.last();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.save"),//保存按钮
	            id: 'icm_assessplan_selector_card_finish_btn' ,
	            iconCls: 'icon-control-stop-blue',
	            handler: function () {
	            	var assessplanselectorcardpanel = this.up('assessplanselectorcardpanel');
	            	if(assessplanselectorcardpanel){
	            		assessplanselectorcardpanel.finish();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.submit"),//提交按钮
	            id: 'icm_assessplan_selector_card_submit_btn' ,
	            iconCls: 'icon-operator-submit',
	            handler: function () {
	            	var assessplanselectorcardpanel = this.up('assessplanselectorcardpanel');
	            	if(assessplanselectorcardpanel){
	            		assessplanselectorcardpanel.submit();
	            	}
	            }
	        }
	    ]
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.assessplanform = Ext.widget('assessplanform');
        
        me.assessplanrangeform = Ext.widget('assessplanrangeform');
        
        Ext.applyIf(me, {
            items: [
                me.assessplanform,
                me.assessplanrangeform
            ]
        });

        me.callParent(arguments);
        
        //初始化tbar和bbar状态
        me.setNavBtnEnable(true,false);
    },
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;

    	//保存流程信息
    	me.assessplanrangeform.assessPlanGrid.saveData();
    	me.setBtnState(0);
    	if(me.businessId){
    		me.editflag=true;
    		me.navBtnHandler(me,0);
    	}else{
    		me.setActiveItem(0);
			me.navBtnState();
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
    		Ext.getCmp('icm_assessplan_selector_card_range_btn_top').setDisabled(false);
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
					//所有按钮不可用，提交成功后跳转到列表
			    	Ext.getCmp('icm_assessplan_selector_card_pre_btn').setDisabled(true);
			        Ext.getCmp('icm_assessplan_selector_card_next_btn').setDisabled(true);
			        Ext.getCmp('icm_assessplan_selector_card_finish_btn').setDisabled(true);
			        Ext.getCmp('icm_assessplan_selector_card_submit_btn').setDisabled(true);
			        	
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
				        		me.paramObj.businessId = me.businessId;
				        		me.paramObj.name = me.assessplanform.getForm().getValues().name;
				        		
				        		me.onSubmit(me.paramObj);
				        		
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
    	Ext.getCmp('icm_assessplan_selector_card_pre_btn' ).setDisabled(!layout.getPrev());
        Ext.getCmp('icm_assessplan_selector_card_next_btn' ).setDisabled(!layout.getNext());
        Ext.getCmp('icm_assessplan_selector_card_submit_btn' ).setDisabled(!layout.getPrev());
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('icm_assessplan_selector_card_topbar');
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
    		Ext.getCmp('icm_assessplan_selector_card_details_btn_top').setDisabled(v);
    		Ext.getCmp('icm_assessplan_selector_card_range_btn_top').setDisabled(v);
    	}else{
    		//新增
    		Ext.getCmp('icm_assessplan_selector_card_details_btn_top').setDisabled(first);
    		Ext.getCmp('icm_assessplan_selector_card_range_btn_top').setDisabled(v);
    	}
    	me.setBtnState(0);
    	me.navBtnHandler(me,0);
    },
    loadData:function(businessId,editflag){
    	var me=this;
    	
    	me.businessId = businessId;
    	me.editflag = editflag;
    	
    	if(me.editflag){
			//编辑评价计划
        	me.setNavBtnEnable(false,true);
        }else{
        	//添加评价计划
        	me.setNavBtnEnable(true,false);
        }
    },
    onSubmit:Ext.emptyFn(),
    reloadData:function(){
    	var me=this;
    	
    }
});