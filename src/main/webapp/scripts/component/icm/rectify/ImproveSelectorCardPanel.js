Ext.define('FHD.ux.icm.rectify.ImproveSelectorCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.improveselectorcardpanel',
  
    activeItem: 0,
    requires: [
       'FHD.view.icm.rectify.form.RectifyImproveForm',
       'FHD.view.icm.rectify.form.RectifyDefectSelectForm'
    ],
    paramObj:{
    	businessId:'',
    	name:''
    },
    tbar: {
        id: 'icm_rectifyimprove_card_selector_topbar',
        items: [
	        {
	            text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
	            iconCls: 'icon-001',
	            id: 'icm_rectifyimprove_details_selector_btn_top',
	            handler: function () {
	            	var improveselectorcardpanel = this.up('improveselectorcardpanel');
	            	if(improveselectorcardpanel){
	            		improveselectorcardpanel.setBtnState(0);
	            		improveselectorcardpanel.navBtnHandler(this.up('panel'), 0);
	            	}
	            }
	        },
	        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
	        {
	            text: '范围选择',//范围选择导航按钮
	            iconCls: 'icon-002',
	            id: 'icm_rectifyimprove_range_selector_btn_top',
	            handler: function () {
	            	var improveselectorcardpanel = this.up('improveselectorcardpanel');
	            	if(improveselectorcardpanel){
	            		improveselectorcardpanel.setBtnState(1);
	            		improveselectorcardpanel.navBtnHandler(this.up('panel'), 1);
	            	}
	            }
	        }
	    ]
    },
    bbar: {
        id: 'icm_rectifyimprove_card_selector_bbar',
        items: [
	        '->', 
	        {
	            id: 'icm_rectifyimprove_card_selector_pre_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
	            iconCls: 'icon-operator-back',
	            handler: function () {
	            	var improveselectorcardpanel = this.up('improveselectorcardpanel');
	            	if(improveselectorcardpanel){
	            		improveselectorcardpanel.back();
	            	}
	            }
	        }, 
	        {
	            id: 'icm_rectifyimprove_card_selector_next_btn' ,
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
	            iconCls: 'icon-operator-next',
	            handler: function () {
	            	var improveselectorcardpanel = this.up('improveselectorcardpanel');
	            	if(improveselectorcardpanel){
	            		improveselectorcardpanel.last();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.save"),//保存按钮
	            id: 'icm_rectifyimprove_card_selector_finish_btn' ,
	            iconCls: 'icon-control-stop-blue',
	            handler: function () {
	            	var improveselectorcardpanel = this.up('improveselectorcardpanel');
	            	if(improveselectorcardpanel){
	            		improveselectorcardpanel.finish();
	            	}
	            }
	        }, 
	        {
	            text: FHD.locale.get("fhd.common.submit"),//提交按钮
	            id: 'icm_rectifyimprove_card_selector_submit_btn' ,
	            iconCls: 'icon-operator-submit',
	            handler: function () {
	            	var improveselectorcardpanel = this.up('improveselectorcardpanel');
	            	if(improveselectorcardpanel){
	            		improveselectorcardpanel.submit();
	            	}
	            }
	        }
	    ]
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.rectifyimproveform = Ext.widget('rectifyimproveform',{autoScroll: true, border:false});
        me.rectifydefectselectform = Ext.widget('rectifydefectselectform',{autoScroll: true, border:false});
        
        Ext.applyIf(me, {
            items: [
                me.rectifyimproveform,
                me.rectifydefectselectform
            ]
        });

        me.callParent(arguments);
         //初始化tbar和bbar状态
        me.setNavBtnEnable(true,false);
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
           me.rectifyimproveform.loadData(me.improveId,me.executionId);
 	    }else if(1 == index){
 	    	//范围选择刷新
            me.rectifydefectselectform.loadData(me.improveId);
 	    }
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	Ext.getCmp('icm_rectifyimprove_card_selector_pre_btn' ).setDisabled(!layout.getPrev());
        Ext.getCmp('icm_rectifyimprove_card_selector_next_btn' ).setDisabled(!layout.getNext());
    },

    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
        me.pageMove("prev");
        Ext.getCmp('icm_rectifyimprove_card_selector_pre_btn').setDisabled(true);
        var activePanel = me.getActiveItem();
        me.navBtnState();
        me.preSetBtnState(me, activePanel);
    },
    /**
     * 下一步按钮事件
     */
    last:function(){
    	var me = this;
    	
    	if(me.rectifyimproveform.saveFunc(me)){
    		Ext.getCmp('icm_rectifyimprove_card_selector_next_btn').setDisabled(true);
    		me.setBtnState(1);
            //me.navBtnHandler(me,1);
    		me.setActiveItem(1);
 	   		me.navBtnState();
    	}
    },
    /**
     * 完成按钮事件
     */
    finish:function(){
    	var me = this;
    	if(me.rectifyimproveform.saveFunc()){
    		me.setBtnState(0);
            var activePanel = me.getActiveItem();
            if (activePanel.last) {
                activePanel.last(me, true);
            }
    	}
    },
    /**
     * 完成按钮事件
     */
    submit:function(){
    	var me = this;
    	if(me.rectifydefectselectform.defectrelaimprovegrid.defectRelaImproveGrid.getStore().getCount()==0){
			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '请选择待整改的缺陷信息');
			return false;
		}
    	//所有按钮不可用，提交成功后跳转到列表
    	Ext.getCmp('icm_rectifyimprove_card_selector_pre_btn').setDisabled(true);
        Ext.getCmp('icm_rectifyimprove_card_selector_next_btn').setDisabled(true);
        Ext.getCmp('icm_rectifyimprove_card_selector_finish_btn').setDisabled(true);
        Ext.getCmp('icm_rectifyimprove_card_selector_submit_btn').setDisabled(true);
        if(me.improveId){
				FHD.ajax({//ajax调用
					url : __ctxPath+ '/icm/rectify/rectifyDraft.f',
				    params : {
				    	businessId : me.improveId,
				    	executionId:me.executionId
					},
					callback : function(data) {
						me.paramObj.businessId = me.improveId;
		        		me.paramObj.name = me.rectifyimproveform.getForm().getValues().name;
		        		me.onSubmit(me.paramObj);
					}
				});
		}
		
    },
    /**
     * 点击下一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    lastSetBtnState: function (cardpanel, activePanel) {
    	var me=this;
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel) + 1;
        me.setBtnState(index);
    },
    /**
     * 点击上一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    preSetBtnState: function (cardpanel, activePanel) {
    	var me=this;
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel)
        me.setBtnState(index);
    },
	/**
     * 设置tbar导航按钮状态:false可用，true不可用
     */
    setNavBtnEnable:function(v,first){
    	var me=this;
    	if(first){
    		//修改
    		Ext.getCmp('icm_rectifyimprove_details_selector_btn_top').setDisabled(v);
    		Ext.getCmp('icm_rectifyimprove_range_selector_btn_top').setDisabled(v);
    	}else{
    		//新增
    		Ext.getCmp('icm_rectifyimprove_details_selector_btn_top').setDisabled(first);
    		Ext.getCmp('icm_rectifyimprove_range_selector_btn_top').setDisabled(v);
    	}
    	me.setBtnState(0);
    	me.navBtnHandler(me, 0);
        //Ext.getCmp('icm_assessplan_preview_btn_top').setDisabled(v);
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('icm_rectifyimprove_card_selector_topbar');
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
        /*
        k = 0;
        var bbar = Ext.getCmp('icm_assessplan_card_bbar');
        btns = bbar.items.items;
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
        */
    },
    /**
     * 设置bbar按钮状态
     */
    setFirstItemFoucs:function(disable){
    	var me=this;
    	me.setBtnState(0);
        //设置bbar按钮的状态：上一步、提交
        Ext.getCmp('icm_rectifyimprove_card_selector_pre_btn').setDisabled(disable);
        Ext.getCmp('icm_rectifyimprove_card_selector_submit_btn').setDisabled(disable);
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	Ext.getCmp('icm_rectifyimprove_card_selector_pre_btn' ).setDisabled(!layout.getPrev());
        Ext.getCmp('icm_rectifyimprove_card_selector_next_btn' ).setDisabled(!layout.getNext());
        Ext.getCmp('icm_rectifyimprove_card_selector_finish_btn' ).setDisabled(!layout.getNext());
    	if(me.improveId){
        	//修改
        	Ext.getCmp('icm_rectifyimprove_card_selector_submit_btn' ).setDisabled(!layout.getPrev());
        }else{
        	//添加
        	//alert('添加时提交按钮状态='+!layout.getPrev()+"\t"+!layout.getNext());
        	Ext.getCmp('icm_rectifyimprove_card_selector_submit_btn' ).setDisabled(!layout.getPrev());
        }
        
    },
    loadData: function(improveId, executionId){
    	var me = this;
    	me.improveId = improveId;
    	me.executionId = executionId;
    	me.reloadData();
    },
    onSubmit:Ext.emptyFn(),
	/**
	 * 重新加载数据
	 */
    reloadData:function(){
    	var me=this;
    	//初始化tbar和bbar按钮状态
    	me.setInitBtnState();
    	var activeItem = me.getActiveItem();
    	var activeid = activeItem.id;
    	if(activeid==me.rectifyimproveform.id){
    		//设置基本信息按钮被选中
    		me.setBtnState(0);
    		//基本信息form刷新
        	me.rectifyimproveform.loadData(me.improveId,me.executionId);
    	}else if(activeid==me.rectifydefectselectform.id){
    		//设置范围选择按钮被选中
    		me.setBtnState(1);
    		//范围选择刷新
            me.rectifydefectselectform.reloadData();
    		
    	}
    }
});