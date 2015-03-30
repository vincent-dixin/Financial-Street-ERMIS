Ext.define('FHD.view.risk.strategy.StrategyCardPanel',{
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.strategycardpanel',
    
    title:FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
    
    activeItem: 0,
    
    requires: [
               'FHD.view.risk.strategy.StrategyBasicForm',
               'FHD.view.risk.strategy.StrategyKpiSet',
               'FHD.view.risk.strategy.StrategyWarningSet'
              ],
    
    /**
     * 返回按钮事件
     */
    undo:function(){
    	var me = this;
    	me.gotopage();
    },
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
        me.pageMove("prev");
        var activePanel = me.getActiveItem();
        me.navBtnState();
        me.preSetBtnState(me, activePanel);
    },
    /**
     * 下一步按钮事件
     */
    last:function(){
    	var me = this;
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            activePanel.last(me);
        }
        me.lastSetBtnState(me, activePanel);
    },
    
    /**
     * 完成按钮事件
     */
    finish:function(){
    	var me = this;
    	me.setBtnState(0);
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            activePanel.last(me, true);
        }
    },
    
    gotopage:function(){
    	var scorecardtab = Ext.getCmp('strategytab');
    	scorecardtab.setActiveTab(0);
    	this.setNavBtnEnable(false);
    },
    /**
     * 使导航按钮为enable状态
     */
    setNavBtnEnable:function(v){
    	Ext.getCmp('risk_sm_details_btn_top').setDisabled(v);
        Ext.getCmp('risk_sm_kpiset_btn_top').setDisabled(v);
        Ext.getCmp('risk_sm_alarmset_btn_top').setDisabled(v);
        Ext.getCmp('risk_sm_details_btn').setDisabled(v);
        Ext.getCmp('risk_sm_kpiset_btn').setDisabled(v);
        Ext.getCmp('risk_sm_alarmset_btn').setDisabled(v);
    },
    /**
     * 点击上一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    preSetBtnState: function (cardpanel, activePanel) {
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel)
        this.setBtnState(index);
    },
    /**
     * 点击下一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    lastSetBtnState: function (cardpanel, activePanel) {
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel) + 1;
        this.setBtnState(index);
    },
    
    /**
    * 设置导航按钮的事件函数
    * @param {panel} cardPanel cardpanel面板
    * @param index 面板索引值
    */
    navBtnHandler: function (cardPanel, index) {
	   	var me = this;
	    cardPanel.getLayout().setActiveItem(index);
	    me.navBtnState();
       
    },
    
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	Ext.getCmp('risk_sm_move-prev' ).setDisabled(!layout.getPrev());
        Ext.getCmp('risk_sm_move-next' ).setDisabled(!layout.getNext());
        Ext.getCmp('risk_sm_move-prev_top' ).setDisabled(!layout.getPrev());
        Ext.getCmp('risk_sm_move-next_top' ).setDisabled(!layout.getNext());
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('risk_sm_tbar');
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
        k = 0;
        var bbar = Ext.getCmp('risk_sm_bbar');
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
    },
    /**
     * 设置首项为激活状态
     * 
     */
    setFirstItemFoucs:function(){
    	var me = this;
    	me.setBtnState(0);
    	me.navBtnHandler(me, 0);
    	Ext.getCmp('risk_sm_kpiset_btn').setDisabled(true);
    	Ext.getCmp('risk_sm_alarmset_btn').setDisabled(true);
    	Ext.getCmp('risk_sm_kpiset_btn_top').setDisabled(true);
        Ext.getCmp('risk_sm_alarmset_btn_top').setDisabled(true);
    },
    tbar: {
        id: 'risk_sm_tbar' ,
        items: [

        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'),//基本信息导航按钮
            iconCls: 'icon-001',
            id: 'risk_sm_details_btn_top' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategycardpanel');
            	strategyobjectivecardpanel.setBtnState(0);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset'),//衡量指标导航按钮
            iconCls: 'icon-002',
            id: 'risk_sm_kpiset_btn_top',
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategycardpanel');
            	strategyobjectivecardpanel.setBtnState(1);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'),//告警设置导航按钮
            iconCls: 'icon-003',
            id: 'risk_sm_alarmset_btn_top' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategycardpanel');
            	strategyobjectivecardpanel.setBtnState(2);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 2);
            }
        },
        '->', 
//        {
//            id: 'risk_sm_move-undo_top' ,
//            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
//            iconCls: 'icon-arrow-undo',
//            handler: function () {
//            	Ext.getCmp('strategycardpanel').undo();
//            }
//        },
//        
        {
            id: 'risk_sm_move-prev_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('strategycardpanel').back();
            }
        }, {
            id: 'risk_sm_move-next_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('strategycardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'risk_sm_finish_btn_top' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('strategycardpanel').finish();
            }
        }

        ]


    },
    bbar: {
        id: 'risk_sm_bbar' ,
        items: [

        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'),//基本信息导航按钮
            iconCls: 'icon-001',
            id: 'risk_sm_details_btn' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategycardpanel');
            	strategyobjectivecardpanel.setBtnState(0);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset'),//衡量指标导航按钮
            iconCls: 'icon-002',
            id: 'risk_sm_kpiset_btn' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategycardpanel');
            	strategyobjectivecardpanel.setBtnState(1);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'),//告警设置导航按钮
            iconCls: 'icon-003',
            id: 'risk_sm_alarmset_btn' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategycardpanel');
            	strategyobjectivecardpanel.setBtnState(2);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 2);
            }
        },
        '->',
//        {
//            id: 'risk_sm_move-undo' ,
//            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
//            iconCls: 'icon-arrow-undo',
//            handler: function () {
//            	Ext.getCmp('strategycardpanel').undo();
//            }
//        },
        
        {
            id: 'risk_sm_move-prev' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('strategycardpanel').back();
            }
        }, {
            id: 'risk_sm_move-next' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('strategycardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'risk_sm_finish_btn' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('strategycardpanel').finish();
            }
        }

        ]


    },

    initComponent: function () {
        var me = this;
 
        me.strategybasicform =  Ext.widget('strategybasicform',{id:'strategybasicform'});
        me.strategykpiset =  Ext.widget('strategykpiset',{id:'strategykpiset'});
        me.strategywarningset =  Ext.widget('strategywarningset',{id:'strategywarningset'});
        
        Ext.apply(me, {
            items: [
                    me.strategybasicform,
                    me.strategykpiset,
                    me.strategywarningset
                   ]
        });
    
        me.callParent(arguments);
    },
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    setAllBtnStatus:function(status){
    	var me = this;
    	Ext.getCmp('risk_sm_details_btn_top').setDisabled(status);
    	Ext.getCmp('risk_sm_details_btn').setDisabled(status);
    	Ext.getCmp('risk_sm_kpiset_btn').setDisabled(status);
    	Ext.getCmp('risk_sm_alarmset_btn').setDisabled(status);
    	Ext.getCmp('risk_sm_kpiset_btn_top').setDisabled(status);
        Ext.getCmp('risk_sm_alarmset_btn_top').setDisabled(status);
        
        Ext.getCmp('risk_sm_move-prev').setDisabled(status);
        Ext.getCmp('risk_sm_move-next').setDisabled(status);
        Ext.getCmp('risk_sm_finish_btn').setDisabled(status);
        //Ext.getCmp('risk_sm_move-undo').setDisabled(status);
        
        //Ext.getCmp('risk_sm_move-undo_top').setDisabled(status);
        Ext.getCmp('risk_sm_finish_btn_top').setDisabled(status);
        Ext.getCmp('risk_sm_move-next_top').setDisabled(status);
        Ext.getCmp('risk_sm_move-prev_top').setDisabled(status);
        
    },
    reLoadData:function(){
    	var me = this;
    	var activeItem = me.getActiveItem();
    	var activeid = activeItem.id;
    	
    	if(activeid=='strategybasicform'){
    		//设置基本信息按钮被选中
        	me.setBtnState(0);
    	}else if(activeid=='strategykpiset'){
    		//设置衡量指标列表被选中
    		me.setBtnState(1);
    	}else{
    		//告警列表被选中
    		me.setBtnState(2);
    	}
    	if(me.paramObj!=undefined){
    		if(me.paramObj.editflag){
        		//编辑状态时,导航按钮为可用状态
        		me.setNavBtnEnable(false);
            }
        	
        	//清除基本信息数据
        	me.strategybasicform.clearFormData();
        	//刷新基本信息form数据
        	me.strategybasicform.initParam(me.paramObj);
        	me.strategybasicform.reLoadData();
        	//衡量指标列表数据刷新
        	me.strategykpiset.initParam(me.paramObj);
        	me.strategykpiset.reLoadData();
        	
        	//刷新告警列表数据
        	me.strategywarningset.initParam(me.paramObj);
        	me.strategywarningset.reLoadData();
    	}
    	
    }



});