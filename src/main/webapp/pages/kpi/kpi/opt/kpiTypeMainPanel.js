/**
 * 添加指标类型时主面板,在其中放入了一个card布局的panel,每一步的panel为基本信息面板-kpiTypeBasicPanel.js,采集计算面板-kpiTypeGatherPanel.js,告警设置面板-kpiTypeWarningPanel.js
 * 继承于Ext.tab.Panel
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.kpi.opt.kpiTypeMainPanel', {
    extend: 'Ext.tab.Panel',
    plain: true,
    border: true,
    /**
     * 返回首页
     */
    _gotopage:function(){
    	var me = this;
    	var kpitypeEditUrl = __ctxPath + "/pages/kpi/kpi/opt/kpitypeedit.jsp?";
    	fhd_kpi_kpiaccordion_view.initRightPanel(kpitypeEditUrl + "editflag=true" + "&id=" + currentId + "&name=" + encodeURIComponent(kpitypename));
    },
    _navBtnState:function(cardpanel){
    	var layout = cardpanel.getLayout();
    	Ext.getCmp('move-prev_top' + param_dc).setDisabled(!layout.getPrev());
        Ext.getCmp('move-next_top' + param_dc).setDisabled(!layout.getNext());
        Ext.getCmp('move-prev' + param_dc).setDisabled(!layout.getPrev());
        Ext.getCmp('move-next' + param_dc).setDisabled(!layout.getNext());
    },
    /**
     * 返回按钮事件
     */
    _undo:function(){
    	var me = this;
    	me._gotopage();
    },
    /**
     * 下一步按钮事件
     */
    _last:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
        var activePanel = cardPanel.getActiveItem();
        if (activePanel.last) {
            activePanel.last(cardPanel);
        }
        me._lastSetBtnState(cardPanel,activePanel);
    },
    /**
     * 上一步按钮事件
     */
    _back:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
    	cardPanel.pageMove("prev");
        var layout = cardPanel.getLayout();
        var activePanel = cardPanel.getActiveItem();
        me._navBtnState(cardPanel);
        me._preSetBtnState(cardPanel,activePanel);
    },
    /**
     * 完成按钮事件
     */
    _finish:function(){
    	var me = this;
    	me._setBtnState(0);
        var cardPanel = me.cardpanel;
        var activePanel = cardPanel.getActiveItem();
        if (activePanel.last) {
            activePanel.last(cardPanel, true);
        }
        //me._gotopage();
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
    _lastSetBtnState: function (cardpanel,activePanel) {
    	var items = cardpanel.items.items;
    	var index =  Ext.Array.indexOf(items, activePanel)+1;
        this._setBtnState(index);
    },
    /**
     * 点击上一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    _preSetBtnState: function (cardpanel,activePanel) {
    	var items = cardpanel.items.items;
    	var index =  Ext.Array.indexOf(items, activePanel)
        this._setBtnState(index);
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    _setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('kpitype_topbar' + param_dc);
        var btns = topbar.items.items;
        for (var i = 0; i < btns.length; i++) {
            var item = btns[i];
            if (item.pressed != undefined) {
                if (k == index) {
                    //item.toggle(!item.pressed);
                    item.toggle(true);
                } else {
                    item.toggle(false);
                }
                k++;
            }
        }
        k = 0;
        var bbar = Ext.getCmp('kpitype_bbar' + param_dc);
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
     * 初始化组件方法
     */
    initComponent: function () {
        var me = this;
        var arr = '<img src="'+__ctxPath+'/images/icons/show_right.gif">';
        
       
        me.grid = Ext.create('FHD.ux.kpi.KpiGridPanel', {
            title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
            url: me.kpiListUrl,
            checked: false,
            type: 'kpitype'
            
        });
        
        me.cardpanel = Ext.create('FHD.ux.CardPanel', {
            title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
            xtype: 'cardpanel',
            destoryflag:'true',
            activeItem: 0,
            tbar: {
            	style : 'border-bottom: 0px',
                id: 'kpitype_topbar' + param_dc,
                items: [

                {
                    text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
                    iconCls: 'icon-001',
                    id: 'details_btn_top' + param_dc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"),//采集计算报告导航按钮
                    iconCls: 'icon-002',
                    id: 'caculate_btn_top' + param_dc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr, {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置导航按钮
                    iconCls: 'icon-003',
                    id: 'alarmset_btn_top' + param_dc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                //arr,
               /* {
                    text: FHD.locale.get("fhd.sys.auth.authority.authority"),//权限导航按钮
                    iconCls: 'icon-004',
                    id: 'authority_btn_top' + param_dc,
                    handler: function () {
                        me._setBtnState(3);
                        me._navBtnHandler(this.up('panel'), 3);
                    }
                },*/
                    '->', 
                    
                    {
                        id: 'move-undo_top' + param_dc,
                        text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                        iconCls: 'icon-arrow-undo',
                        handler: function () {
                        	me._undo();
                        }
                    },
                    {
                    id: 'move-prev_top' + param_dc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                    	me._back();

                    }
                }, {
                    id: 'move-next_top' + param_dc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                        me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'finish_btn_top' + param_dc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me._finish();

                    }
                }]
            },
            bbar: {
                id: 'kpitype_bbar' + param_dc,
                items: [

                {
                    text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
                    iconCls: 'icon-001',
                    id: 'details_btn' + param_dc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"),//采集计算报告导航按
                    iconCls: 'icon-002',
                    id: 'caculate_btn' + param_dc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr, {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置导航按钮
                    iconCls: 'icon-003',
                    id: 'alarmset_btn' + param_dc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                //arr, 
                
                /*{
                    text: FHD.locale.get("fhd.sys.auth.authority.authority"),//权限导航按钮
                    iconCls: 'icon-004',
                    id: 'authority_btn' + param_dc,
                    handler: function () {
                        me._setBtnState(3);
                        me._navBtnHandler(this.up('panel'), 3);
                    }
                },*/
                    '->', 
                    {
                        id: 'move-undo' + param_dc,
                        text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                        iconCls: 'icon-arrow-undo',
                        handler: function () {
                        	me._undo();
                        }
                    },
                    {
                    id: 'move-prev' + param_dc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                    	me._back();
                    }
                }, {
                    id: 'move-next' + param_dc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                    	me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'finish_btn' + param_dc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me._finish();
                    }
                }]
            },
            items: me.cardItems
        });
        Ext.applyIf(me, {
            width: FHD.getCenterPanelWidth() - 270,
            height: FHD.getCenterPanelHeight() - 20,
            activeTab: 1,
            plain: me.plain,
            border: me.border,
            items: [
                    me.grid ,
                    me.cardpanel
            ],

            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "false" || me.editflag.length == 0) {
                        //添加时,使除了'基本信息'按钮可用外,其它按钮为不可用状态
                        Ext.getCmp('caculate_btn' + param_dc).setDisabled(true);
                        Ext.getCmp('alarmset_btn' + param_dc).setDisabled(true);
                        //Ext.getCmp('authority_btn' + param_dc).setDisabled(true);
                        Ext.getCmp('caculate_btn_top' + param_dc).setDisabled(true);
                        Ext.getCmp('alarmset_btn_top' + param_dc).setDisabled(true);
                        //Ext.getCmp('authority_btn_top' + param_dc).setDisabled(true);
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