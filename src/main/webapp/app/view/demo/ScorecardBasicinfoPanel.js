Ext.define('FHD.view.kpi.scorecard.ScorecardBasicinfoPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.scorecardbasicinfopanel',

    requires: [
        'FHD.view.kpi.scorecard.ScorecardBasicForm',
        'FHD.view.kpi.scorecard.ScorecardWarningSet'
    ],

    activeItem: 0,
    
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
    	var scorecardtab = Ext.getCmp('scorecardtab');
    	scorecardtab.setActiveTab(0);
    	this.setNavBtnEnable(false);
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
    	Ext.getCmp('kpicategory_move-prev' ).setDisabled(!layout.getPrev());
        Ext.getCmp('kpicategory_move-next' ).setDisabled(!layout.getNext());
        Ext.getCmp('kpicategory_move-prev_top' ).setDisabled(!layout.getPrev());
        Ext.getCmp('kpicategory_move-next_top' ).setDisabled(!layout.getNext());
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
        var topbar = Ext.getCmp('kpicategory_topbar');
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
        var bbar = Ext.getCmp('kpicategory_bbar');
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
    
    setAllBtnStatus:function(status){
    	Ext.getCmp('kpicategory_details_btn_top' ).setDisabled(status);
    	Ext.getCmp('kpicategory_alarmset_btn_top' ).setDisabled(status);
    	Ext.getCmp('kpicategory_details_btn' ).setDisabled(status);
    	Ext.getCmp('kpicategory_alarmset_btn' ).setDisabled(status);
    	
    	Ext.getCmp('kpicategory_move-undo_top' ).setDisabled(status);
    	Ext.getCmp('kpicategory_move-prev_top' ).setDisabled(status);
    	Ext.getCmp('kpicategory_move-next_top' ).setDisabled(status);
    	Ext.getCmp('kpicategory_finish_btn_top' ).setDisabled(status);
    	
    	Ext.getCmp('kpicategory_move-undo' ).setDisabled(status);
    	Ext.getCmp('kpicategory_move-prev' ).setDisabled(status);
    	Ext.getCmp('kpicategory_move-next' ).setDisabled(status);
    	Ext.getCmp('kpicategory_finish_btn' ).setDisabled(status);
    },
    
    
    tbar: {
        id: 'kpicategory_topbar',
        items: [

        {
            text: FHD.locale.get('fhd.common.details'),//基本信息按钮
            iconCls: 'icon-001',
            id: 'kpicategory_details_btn_top',
            handler: function () {
                Ext.getCmp('scorecardbasicinfopanel').setBtnState(0);
                Ext.getCmp('scorecardbasicinfopanel').navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置按钮
            iconCls: 'icon-002',
            id: 'kpicategory_alarmset_btn_top',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').setBtnState(1);
            	Ext.getCmp('scorecardbasicinfopanel').navBtnHandler(this.up('panel'), 1);
            }
        },
            '->',
            {
                id: 'kpicategory_move-undo_top',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                iconCls: 'icon-arrow-undo',
                handler: function () {
                	Ext.getCmp('scorecardbasicinfopanel').undo();
                }
            },
            {
            id: 'kpicategory_move-prev_top',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').back();
            }
        }, {
            id: 'kpicategory_move-next_top',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'kpicategory_finish_btn_top',
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').finish();
            }
        }]
    },
    bbar: {
        id: 'kpicategory_bbar',
        items: [

        {
            text: FHD.locale.get('fhd.common.details'),//基本信息按钮
            iconCls: 'icon-001',
            id: 'kpicategory_details_btn',
            handler: function () {
            	var scorecardbasicinfopanel = Ext.getCmp('scorecardbasicinfopanel');
            	scorecardbasicinfopanel.setBtnState(0);
            	scorecardbasicinfopanel.navBtnHandler(this.up('panel'), 0);
            	//scorecardbasicinfopanel.scorecardbasicform.reLoadData();
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置按钮
            iconCls: 'icon-002',
            id: 'kpicategory_alarmset_btn' ,
            handler: function () {
            	var scorecardbasicinfopanel = Ext.getCmp('scorecardbasicinfopanel');
            	scorecardbasicinfopanel.setBtnState(1);
            	scorecardbasicinfopanel.navBtnHandler(this.up('panel'), 1);
            	//scorecardbasicinfopanel.scorecardwarningset.reLoadData();
            	

            }
        },
            '->', 
            {
                id: 'kpicategory_move-undo',
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                iconCls: 'icon-arrow-undo',
                handler: function () {
                	Ext.getCmp('scorecardbasicinfopanel').undo();
                }
            },
            {
            id: 'kpicategory_move-prev',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').back();
            }
        }, {
            id: 'kpicategory_move-next',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'kpicategory_finish_btn',
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('scorecardbasicinfopanel').finish();
            }
        }]
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.scorecardbasicform = Ext.widget('scorecardbasicform',{id : 'scorecardbasicform'});
        me.scorecardwarningset = Ext.widget('scorecardwarningset',{id : 'scorecardwarningset'});

        Ext.apply(me, {
            items: [
                    me.scorecardbasicform,me.scorecardwarningset
                   ]
        });

        me.callParent(arguments);
        
    },
    
    //加载表单数据
    reLoadData : function() {
    	var me = this;
    	var scorecardtab = Ext.getCmp('scorecardtab');
    	//清除基本信息数据
    	me.scorecardbasicform.clearFormData();
    	//重新加载基本信息form数据
    	me.scorecardbasicform.reLoadData();
    	//重新加载告警设置列表
    	me.scorecardwarningset.reLoadData();
    	
    	var activeItem = me.getActiveItem();
    	var activeid = activeItem.id;
    	if(activeid=='scorecardbasicform'){
    		//设置基本信息按钮被选中
        	me.setBtnState(0);
    	}else{
    		//设置告警按钮被选中
    		me.setBtnState(1);
    	}
    	if(scorecardtab.paramObj.editflag){
    		//编辑状态时,导航按钮为可用状态
    		me.setNavBtnEnable(false);
        }
    },
    /**
     * 清除数据
     */
    clearDate:function(){
    	//清除form数据
    	Ext.getCmp('scorecardbasicform').clearFormData();
    	//清除grid数据
    	Ext.getCmp('scorecardwarningset').reLoadData();
    },
    
    /**
     * 使导航按钮为enable状态
     */
    setNavBtnEnable:function(v){
    	Ext.getCmp('kpicategory_alarmset_btn').setDisabled(v);
        Ext.getCmp('kpicategory_alarmset_btn_top').setDisabled(v);
        Ext.getCmp('kpicategory_details_btn_top').setDisabled(v);
        Ext.getCmp('kpicategory_details_btn').setDisabled(v);
    },
    
    /**
     * 设置首项为激活状态
     * 
     */
    setFirstItemFoucs:function(){
    	var scorecardbasicinfopanel = Ext.getCmp('scorecardbasicinfopanel');
    	scorecardbasicinfopanel.setBtnState(0);
    	scorecardbasicinfopanel.navBtnHandler(scorecardbasicinfopanel, 0);
    	Ext.getCmp('kpicategory_alarmset_btn').setDisabled(true);
        Ext.getCmp('kpicategory_alarmset_btn_top').setDisabled(true);
    }
    

});