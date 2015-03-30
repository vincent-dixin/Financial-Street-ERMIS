Ext.define('FHD.view.kpi.kpitype.KpiTypeCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.kpitypecardpanel',

    title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),

    activeItem: 0,

    requires: [
        'FHD.view.kpi.kpitype.KpiTypeBasicForm',
        'FHD.view.kpi.kpitype.KpiTypeGatherForm',
        'FHD.view.kpi.kpitype.KpiTypeWarningSet'
        ],

    /**
     * 返回按钮事件
     */
    undo: function () {
        var me = this;
        me.gotopage();
    },

    /**
     * 下一步按钮事件
     */
    last: function () {
        var me = this;
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
        	activePanel.last(me);
        }
       // me.lastSetBtnState(me, activePanel);
    },

    /**
     * 完成按钮事件
     */
    finish: function () {
        var me = this;
        me.setBtnState(0);
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            activePanel.last(me, true);
        }
    },

    gotopage: function () {
        var kpitypetab = Ext.getCmp('kpitypetab');
        kpitypetab.setActiveTab(0);
        this.setNavBtnEnable(false);
    },
    /**
     * 使导航按钮为enable状态
     */
    setNavBtnEnable: function (v) {
        Ext.getCmp('kpitype_details_btn_top').setDisabled(v);
        Ext.getCmp('kpitype_kpiset_btn_top').setDisabled(v);
        Ext.getCmp('kpitype_alarmset_btn_top').setDisabled(v);
        Ext.getCmp('kpitype_details_btn').setDisabled(v);
        Ext.getCmp('kpitype_kpiset_btn').setDisabled(v);
        Ext.getCmp('kpitype_alarmset_btn').setDisabled(v);
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
    navBtnState: function () {
        var me = this;
        var layout = me.getLayout();
        Ext.getCmp('kpitype_move-prev').setDisabled(!layout.getPrev());
        Ext.getCmp('kpitype_move-next').setDisabled(!layout.getNext());
        Ext.getCmp('kpitype_move-prev_top').setDisabled(!layout.getPrev());
        Ext.getCmp('kpitype_move-next_top').setDisabled(!layout.getNext());
    },

    /**
     * 上一步按钮事件
     */
    back: function () {
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
        var topbar = Ext.getCmp('kpitype_tbar');
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
        var bbar = Ext.getCmp('kpitype_bbar');
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
    setFirstItemFoucs: function (v) {
        var me = this;
        me.setBtnState(0);
        me.navBtnHandler(me, 0);
        Ext.getCmp('kpitype_kpiset_btn').setDisabled(v);
        Ext.getCmp('kpitype_alarmset_btn').setDisabled(v);
        Ext.getCmp('kpitype_kpiset_btn_top').setDisabled(v);
        Ext.getCmp('kpitype_alarmset_btn_top').setDisabled(v);
    },
    tbar: {
        id: 'kpitype_tbar',
        items: [

        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'), //基本信息导航按钮
            iconCls: 'icon-001',
            id: 'kpitype_details_btn_top',
            handler: function () {
                var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
                kpitypecardpanel.setBtnState(0);
                kpitypecardpanel.navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', {
        	 text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"), //采集计算报告导航按钮
            iconCls: 'icon-002',
            id: 'kpitype_kpiset_btn_top',
            handler: function () {
                var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
                kpitypecardpanel.setBtnState(1);
                kpitypecardpanel.navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'), //告警设置导航按钮
            iconCls: 'icon-003',
            id: 'kpitype_alarmset_btn_top',
            handler: function () {
                var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
                kpitypecardpanel.setBtnState(2);
                kpitypecardpanel.navBtnHandler(this.up('panel'), 2);
            }
        },
            '->', {
            id: 'kpitype_move-undo_top',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'), //返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').undo();
            }
        },

        {
            id: 'kpitype_move-prev_top',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"), //上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').back();
            }
        }, {
            id: 'kpitype_move-next_top',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"), //下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"), //保存按钮
            id: 'kpitype_finish_btn_top',
            iconCls: 'icon-control-stop-blue',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').finish();
            }
        }

        ]


    },
    bbar: {
        id: 'kpitype_bbar',
        items: [

        {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'), //基本信息导航按钮
            iconCls: 'icon-001',
            id: 'kpitype_details_btn',
            handler: function () {
                var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
                kpitypecardpanel.setBtnState(0);
                kpitypecardpanel.navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"), //采集计算报告导航按钮
            iconCls: 'icon-002',
            id: 'kpitype_kpiset_btn',
            handler: function () {
                var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
                kpitypecardpanel.setBtnState(1);
                kpitypecardpanel.navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">', {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'), //告警设置导航按钮
            iconCls: 'icon-003',
            id: 'kpitype_alarmset_btn',
            handler: function () {
                var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
                kpitypecardpanel.setBtnState(2);
                kpitypecardpanel.navBtnHandler(this.up('panel'), 2);
            }
        },
            '->', {
            id: 'kpitype_move-undo',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'), //返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').undo();
            }
        },

        {
            id: 'kpitype_move-prev',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"), //上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').back();
            }
        }, {
            id: 'kpitype_move-next',
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"), //下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"), //保存按钮
            id: 'kpitype_finish_btn',
            iconCls: 'icon-control-stop-blue',
            handler: function () {
                Ext.getCmp('kpitypecardpanel').finish();
            }
        }

        ]


    },


    // 初始化方法
    initComponent: function () {
        var me = this;
        me.kpitypebasicform = Ext.widget('kpitypebasicform', {
            id: 'kpitypebasicform'
        });
        me.kpitypegatherform = Ext.widget('kpitypegatherform', {
        	id: 'kpitypegatherform'
        });
        me.kpitypewarningset = Ext.widget('kpitypewarningset', {
        	id: 'kpitypewarningset'
        });
        

        Ext.apply(me, {
            items: [
                    me.kpitypebasicform,
                    me.kpitypegatherform,
                    me.kpitypewarningset
            ]
        });
        me.callParent(arguments);
    },
    
    reLoadData:function(){
    	var me = this;
    	var kpitypetab = Ext.getCmp('kpitypetab');
    	var activeItem = me.getActiveItem();
    	var activeid = activeItem.id;
    	if(activeid=='kpitypebasicform'){
    		//设置基本信息按钮被选中
        	me.setBtnState(0);
    	}else if(activeid=='kpitypegatherform'){
    		//设置采集结果面板被选中
    		me.setBtnState(1);
    	}else{
    		//告警列表被选中
    		me.setBtnState(2);
    	}
    	if(kpitypetab.paramObj!=undefined){
    		if(kpitypetab.paramObj.editflag){
        		//编辑状态时,导航按钮为可用状态
        		me.setNavBtnEnable(false);
            }
        	//清除数据
        	//基本信息
        	me.kpitypebasicform.clearFormData();
        	//采集结果面板
        	me.kpitypegatherform.clearFormData();
        	//刷新基本信息form数据
        	me.kpitypebasicform.reLoadData();
        	//采集结果面板数据刷新
        	me.kpitypegatherform.reLoadData();
        	//刷新告警列表数据
        	me.kpitypewarningset.reLoadData();
    	}
    }

});