Ext.define('Ext.kpi.strategyMap.smTabPanel', {
    extend: 'Ext.tab.Panel',
    plain: true,
    border: true,
    
    _gotopage:function(){
    	var me = this;
    	var editUrl = __ctxPath + "/pages/kpi/strategyMap/smedit.jsp?";
    	fhd_kpi_kpiaccordion_view.initRightPanel(editUrl + "parentid=" + smparentid + "&editflag=true" + "&id=" + smid + "&parentname=" + encodeURIComponent(smparentname) + "&smname=" + encodeURIComponent(smname));
    },
    
    /**
     * 返回按钮事件
     */
    _undo:function(){
    	var me = this;
    	me._gotopage();
    },
    
    /**
     * 完成按钮事件
     */
    _finish:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
        var activePanel = cardPanel.getActiveItem();
        if (activePanel.last) {
            activePanel.last(cardPanel,true);
        }
        //me._gotopage();
    },
    /**
     * 上一步按钮事件
     */
    _back:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
    	cardPanel.pageMove("prev");
        var activePanel = cardPanel.getActiveItem();
        me._navBtnState(cardPanel);
        me._preSetBtnState(cardPanel,activePanel);
    },
    /**
     * 下一步按钮事件
     */
    _last:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
        var activePanel = cardPanel.getActiveItem();
        if (activePanel.last) {
            activePanel.last(cardPanel,false);
        }
        me._lastSetBtnState(cardPanel,activePanel);
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    _navBtnState:function(cardPanel){
    	var layout = cardPanel.getLayout();
        Ext.getCmp('sm_move-prev_top' + sm_paramdc).setDisabled(!layout.getPrev());
        Ext.getCmp('sm_move-next_top' + sm_paramdc).setDisabled(!layout.getNext());
        Ext.getCmp('sm_move-prev' + sm_paramdc).setDisabled(!layout.getPrev());
        Ext.getCmp('sm_move-next' + sm_paramdc).setDisabled(!layout.getNext());
    },
    /**
     * 设置导航按钮的事件函数
     * @param {panel} cardPanel cardpanel面板
     * @param index 面板索引值
     */
    _navBtnHandler: function (cardPanel, index) {
    	var me = this;
        cardPanel.getLayout().setActiveItem(index);
        me._navBtnState(cardPanel);
    },
    /**
     * 点击下一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    _lastSetBtnState: function (cardpanel, activePanel) {
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel) + 1;
        this._setBtnState(index);
    },
    /**
     * 点击上一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    _preSetBtnState: function (cardpanel, activePanel) {
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel)
        this._setBtnState(index);
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    _setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('sm_tbar' + sm_paramdc);
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
        var bbar = Ext.getCmp('sm_bbar' + sm_paramdc);
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
     * 初始化导航按钮的状态
     */
    _initBtnState:function(){
    	//添加时,使除了'基本信息'按钮可用外,其它按钮为不可用状态
        Ext.getCmp('sm_kpiset_btn' + sm_paramdc).setDisabled(true);
        Ext.getCmp('sm_alarmset_btn' + sm_paramdc).setDisabled(true);
        Ext.getCmp('sm_kpiset_btn_top' + sm_paramdc).setDisabled(true);
        Ext.getCmp('sm_alarmset_btn_top' + sm_paramdc).setDisabled(true);
    },


    
    initComponent: function () {
    	var me = this;
    	
    	var arr = '<img src="'+__ctxPath+'/images/icons/show_right.gif">';
        me.cardpanel = Ext.create('FHD.ux.CardPanel', {
        	title:FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
        	destoryflag:'true',
            height: FHD.getCenterPanelHeight(),
            xtype: 'cardpanel',
            activeItem: 0,
            border: me.border,
            tbar: {
                id: 'sm_tbar' + sm_paramdc,
                items: [

                {
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'),//基本信息导航按钮
                    iconCls: 'icon-001',
                    id: 'sm_details_btn_top' + sm_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset'),//衡量指标导航按钮
                    iconCls: 'icon-002',
                    id: 'sm_kpiset_btn_top' + sm_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr, {
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'),//告警设置导航按钮
                    iconCls: 'icon-003',
                    id: 'sm_alarmset_btn_top' + sm_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                '->', 
                {
                    id: 'sm_move-undo_top' + sm_paramdc,
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                    iconCls: 'icon-arrow-undo',
                    handler: function () {
                    	me._undo();
                    }
                },
                
                {
                    id: 'sm_move-prev_top' + sm_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                        me._back();
                    }
                }, {
                    id: 'sm_move-next_top' + sm_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                        me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'sm_finish_btn_top' + sm_paramdc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me._finish();
                    }
                }

                ]


            },
            bbar: {
                id: 'sm_bbar' + sm_paramdc,
                items: [

                {
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo'),//基本信息导航按钮
                    iconCls: 'icon-001',
                    id: 'sm_details_btn' + sm_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset'),//衡量指标导航按钮
                    iconCls: 'icon-002',
                    id: 'sm_kpiset_btn' + sm_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr, {
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset'),//告警设置导航按钮
                    iconCls: 'icon-003',
                    id: 'sm_alarmset_btn' + sm_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                '->',
                {
                    id: 'sm_move-undo' + sm_paramdc,
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                    iconCls: 'icon-arrow-undo',
                    handler: function () {
                    	me._undo();
                    }
                },
                
                {
                    id: 'sm_move-prev' + sm_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                        me._back();
                    }
                }, {
                    id: 'sm_move-next' + sm_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                        me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'sm_finish_btn' + sm_paramdc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me._finish();
                    }
                }

                ]


            },
            items: me.cardItems


        });
        
        me.grid = Ext.create('FHD.ux.kpi.KpiGridPanel', {
            title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
            url: me.kpiListUrl,
            checked: false,
            type: 'sm'
        });
        
    	Ext.applyIf(me, {
            width: FHD.getCenterPanelWidth() - 270,
            height: FHD.getCenterPanelHeight() - 20,
            activeTab: 1,
            plain: me.plain,
            border: me.border,
            items: [
                    me.grid,
                    me.analysisChartMainPanel,
                    me.cardpanel
            ],

            listeners: {
                afterrender: function (t, e) {
                	if (me.editflag == "undefined" || me.editflag == "false" || me.editflag.length == 0) {
                        //添加时,使除了'基本信息'按钮可用外,其它按钮为不可用状态
                    	me._initBtnState();
                    }
                    //设置基本信息按钮为选中状态
                    me._setBtnState(0);
                    //设置首面板为激活面板
                    me._navBtnHandler(me.cardpanel, 0);
        			
                }
            }

        });

        me.callParent(arguments);
    }



});