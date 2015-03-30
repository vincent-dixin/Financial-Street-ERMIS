Ext.define('FHD.view.kpi.strategyobjective.StrategyObjectiveCardPanel',{
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.strategyobjectivecardpanel',
    
    title:FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
    
    activeItem: 0,
    
    requires: [
               'FHD.view.kpi.strategyobjective.StrategyObjectiveBasicForm',
               'FHD.view.kpi.strategyobjective.StrategyObjectiveKpiSet',
               'FHD.view.kpi.strategyobjective.StrategyObjectiveWarningSet'
              ],
    
    /**
     * 返回按钮事件
     */
    undo:function(){
    	var me = this;
    	me.gotopage();
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
        //me.lastSetBtnState(me, activePanel);
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
    	var scorecardtab = Ext.getCmp('strategyobjectivetab');
    	scorecardtab.setActiveTab(0);
    	this.setNavBtnEnable(false);
    },
    /**
     * 使导航按钮为enable状态
     */
    setNavBtnEnable:function(v){
    	Ext.getCmp('sm_details_btn_top').setDisabled(v);
        Ext.getCmp('sm_kpiset_btn_top').setDisabled(v);
        Ext.getCmp('sm_alarmset_btn_top').setDisabled(v);
        Ext.getCmp('sm_details_btn').setDisabled(v);
        Ext.getCmp('sm_kpiset_btn').setDisabled(v);
        Ext.getCmp('sm_alarmset_btn').setDisabled(v);
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
    	Ext.getCmp('sm_move-prev' ).setDisabled(!layout.getPrev());
        Ext.getCmp('sm_move-next' ).setDisabled(!layout.getNext());
        Ext.getCmp('sm_move-prev_top' ).setDisabled(!layout.getPrev());
        Ext.getCmp('sm_move-next_top' ).setDisabled(!layout.getNext());
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
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('sm_tbar');
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
        var bbar = Ext.getCmp('sm_bbar');
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
    	Ext.getCmp('sm_kpiset_btn').setDisabled(true);
    	Ext.getCmp('sm_alarmset_btn').setDisabled(true);
    	Ext.getCmp('sm_kpiset_btn_top').setDisabled(true);
        Ext.getCmp('sm_alarmset_btn_top').setDisabled(true);
    },
    tbar: {
        id: 'sm_tbar' ,
        items: [

        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'),//基本信息导航按钮
            iconCls: 'icon-001',
            id: 'sm_details_btn_top' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
            	strategyobjectivecardpanel.setBtnState(0);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset'),//衡量指标导航按钮
            iconCls: 'icon-002',
            id: 'sm_kpiset_btn_top',
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
            	strategyobjectivecardpanel.setBtnState(1);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'),//告警设置导航按钮
            iconCls: 'icon-003',
            id: 'sm_alarmset_btn_top' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
            	strategyobjectivecardpanel.setBtnState(2);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 2);
            }
        },
        '->', 
        {
            id: 'sm_move-undo_top' ,
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').undo();
            }
        },
        
        {
            id: 'sm_move-prev_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').back();
            }
        }, {
            id: 'sm_move-next_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'sm_finish_btn_top' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').finish();
            }
        }

        ]


    },
    bbar: {
        id: 'sm_bbar' ,
        items: [

        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'),//基本信息导航按钮
            iconCls: 'icon-001',
            id: 'sm_details_btn' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
            	strategyobjectivecardpanel.setBtnState(0);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset'),//衡量指标导航按钮
            iconCls: 'icon-002',
            id: 'sm_kpiset_btn' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
            	strategyobjectivecardpanel.setBtnState(1);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', 
        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'),//告警设置导航按钮
            iconCls: 'icon-003',
            id: 'sm_alarmset_btn' ,
            handler: function () {
            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
            	strategyobjectivecardpanel.setBtnState(2);
            	strategyobjectivecardpanel.navBtnHandler(this.up('panel'), 2);
            }
        },
        '->',
        {
            id: 'sm_move-undo' ,
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').undo();
            }
        },
        
        {
            id: 'sm_move-prev' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').back();
            }
        }, {
            id: 'sm_move-next' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'sm_finish_btn' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('strategyobjectivecardpanel').finish();
            }
        }

        ]


    },

    initComponent: function () {
        var me = this;
        
        me.strategyobjectivebasicform =  Ext.widget('strategyobjectivebasicform',{id:'strategyobjectivebasicform'});
        me.strategyobjectivekpiset =  Ext.widget('strategyobjectivekpiset',{id:'strategyobjectivekpiset'});
        me.strategyobjectivewarningset =  Ext.widget('strategyobjectivewarningset',{id:'strategyobjectivewarningset'});
        
        Ext.apply(me, {
            items: [
                    me.strategyobjectivebasicform,
                    me.strategyobjectivekpiset,
                    me.strategyobjectivewarningset
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
    	Ext.getCmp('sm_details_btn_top').setDisabled(status);
    	Ext.getCmp('sm_details_btn').setDisabled(status);
    	Ext.getCmp('sm_kpiset_btn').setDisabled(status);
    	Ext.getCmp('sm_alarmset_btn').setDisabled(status);
    	Ext.getCmp('sm_kpiset_btn_top').setDisabled(status);
        Ext.getCmp('sm_alarmset_btn_top').setDisabled(status);
        
        Ext.getCmp('sm_move-prev').setDisabled(status);
        Ext.getCmp('sm_move-next').setDisabled(status);
        Ext.getCmp('sm_finish_btn').setDisabled(status);
        Ext.getCmp('sm_move-undo').setDisabled(status);
        
        Ext.getCmp('sm_move-undo_top').setDisabled(status);
        Ext.getCmp('sm_finish_btn_top').setDisabled(status);
        Ext.getCmp('sm_move-next_top').setDisabled(status);
        Ext.getCmp('sm_move-prev_top').setDisabled(status);
        
    },
    reLoadData:function(){
    	var me = this;
    	var activeItem = me.getActiveItem();
    	var activeid = activeItem.id;
    	
    	if(activeid=='strategyobjectivebasicform'){
    		//设置基本信息按钮被选中
        	me.setBtnState(0);
    	}else if(activeid=='strategyobjectivekpiset'){
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
        	me.strategyobjectivebasicform.clearFormData();
        	//刷新基本信息form数据
        	me.strategyobjectivebasicform.initParam(me.paramObj);
        	me.strategyobjectivebasicform.reLoadData();
        	//衡量指标列表数据刷新
        	me.strategyobjectivekpiset.initParam(me.paramObj);
        	me.strategyobjectivekpiset.reLoadData();
        	
        	//刷新告警列表数据
        	me.strategyobjectivewarningset.initParam(me.paramObj);
        	me.strategyobjectivewarningset.reLoadData();
    	}
    	
    }



});