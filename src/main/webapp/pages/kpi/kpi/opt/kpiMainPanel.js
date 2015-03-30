/**
 * 添加指标时主面板,在其中放入了一个card布局的panel,每一步的panel为基本信息面板-kpiBasicPanel.js,采集计算面板-kpiGatherPanel.js,告警设置面板-kpiWarningPanel.js
 * 继承于Ext.container.Container
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.kpi.opt.kpiMainPanel', {
    extend: 'Ext.container.Container',
    border: true,
    layout: "fit",
    /**
     * 返回首页
     */
    _gotopage:function(){
    	var me = this;
    	kpiActivePanelflag = 'kpicategory';
    	var url = __ctxPath + "/pages/kpi/kpi/opt/kpicategoryedit.jsp?editflag=true&categoryid="+me.categoryid+"&categoryparentname="+encodeURIComponent(me.categoryparentname)
    	+"&categoryparentid="+me.categoryparentid+"&categoryname="+encodeURIComponent(me.categoryname);
    	fhd_kpi_kpiaccordion_view.initRightPanel(url);
    },
    /**
     * 完成按钮事件
     */
    _finish:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
    	//获得当前激活面板
        var activePanel = cardPanel.getActiveItem();
        //如果激活面板定义了last函数则提交数据
        if (activePanel.last) {
            activePanel.last(cardPanel,true);
        }
        //me._gotopage();
    	
    },
    /**
     * 返回按钮事件
     */
    _undo:function(){
    	var me = this;
    	me._gotopage();
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
        Ext.getCmp('kpi_kpi_move-prev_top' + kpi_kpi_paramdc).setDisabled(!layout.getPrev());
        Ext.getCmp('kpi_kpi_move-next_top' + kpi_kpi_paramdc).setDisabled(!layout.getNext());
        Ext.getCmp('kpi_kpi_move-prev' + kpi_kpi_paramdc).setDisabled(!layout.getPrev());
        Ext.getCmp('kpi_kpi_move-next' + kpi_kpi_paramdc).setDisabled(!layout.getNext());
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
        var topbar = Ext.getCmp('kpi_kpi_topbar' + kpi_kpi_paramdc);
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
        var bbar = Ext.getCmp('kpi_kpi_bbar' + kpi_kpi_paramdc);
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
        Ext.getCmp('kpi_kpi_caculate_btn' + kpi_kpi_paramdc).setDisabled(true);
        Ext.getCmp('kpi_kpi_alarmset_btn' + kpi_kpi_paramdc).setDisabled(true);
        //Ext.getCmp('kpi_kpi_authority_btn' + kpi_kpi_paramdc).setDisabled(true);
        Ext.getCmp('kpi_kpi_caculate_btn_top' + kpi_kpi_paramdc).setDisabled(true);
        Ext.getCmp('kpi_kpi_alarmset_btn_top' + kpi_kpi_paramdc).setDisabled(true);
        //Ext.getCmp('kpi_kpi_authority_btn_top' + kpi_kpi_paramdc).setDisabled(true);
    },
    /**
     * 初始化组件方法
     */
    initComponent: function () {
    	kpiActivePanelflag = "kpi";
        var me = this;
        var arr = '<img src="'+__ctxPath+'/images/icons/show_right.gif">';
        me.cardpanel = Ext.create('FHD.ux.CardPanel', {
            height: FHD.getCenterPanelHeight()-35,
            destoryflag:'true',
            xtype: 'cardpanel',
            activeItem: 0,
            border: me.border,
            tbar: {

                id: 'kpi_kpi_topbar' + kpi_kpi_paramdc,
                items: [

                {
                    text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
                    iconCls: 'icon-001',
                    id: 'kpi_kpi_details_btn_top' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr,
                 {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"),//采集计算报告导航按钮
                    iconCls: 'icon-002',
                    id: 'kpi_kpi_caculate_btn_top' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr,
                 {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置导航按钮
                    iconCls: 'icon-003',
                    id: 'kpi_kpi_alarmset_btn_top' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                //arr,
                 /*{
                    text: FHD.locale.get("fhd.sys.auth.authority.authority"),//权限导航按钮
                    iconCls: 'icon-004',
                    id: 'kpi_kpi_authority_btn_top' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(3);
                        me._navBtnHandler(this.up('panel'), 3);
                    }
                },*/
                    '->', 
                    {
                        id: 'kpi_kpi_move-undo_top' + kpi_kpi_paramdc,
                        text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                        iconCls: 'icon-arrow-undo',
                        handler: function () {
                        	me._undo();
                        }
                    },
                    {
                    id: 'kpi_kpi_move-prev_top' + kpi_kpi_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                    	me._back();
                    }
                }, {
                    id: 'kpi_kpi_move-next_top' + kpi_kpi_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                        me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'kpi_kpi_finish_btn_top' + kpi_kpi_paramdc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                    	me._finish();
                    }
                }]
            },
            bbar: {
                id: 'kpi_kpi_bbar' + kpi_kpi_paramdc,
                items: [


                {
                    text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
                    iconCls: 'icon-001',
                    id: 'kpi_kpi_details_btn' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr,
                 {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"),//采集计算报告导航按钮
                    iconCls: 'icon-002',
                    id: 'kpi_kpi_caculate_btn' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                arr,
                 {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置导航按钮
                    iconCls: 'icon-003',
                    id: 'kpi_kpi_alarmset_btn' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },
                //arr,
                 /*{
                    text: FHD.locale.get("fhd.sys.auth.authority.authority"),//权限导航按钮
                    iconCls: 'icon-004',
                    id: 'kpi_kpi_authority_btn' + kpi_kpi_paramdc,
                    handler: function () {
                        me._setBtnState(3);
                        me._navBtnHandler(this.up('panel'), 3);
                    }
                },*/
                    '->', 
                {
                    id: 'kpi_kpi_move-undo' + kpi_kpi_paramdc,
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                    iconCls: 'icon-arrow-undo',
                    handler: function () {
                    	me._undo();
                    }
                },
                    
                {
                    id: 'kpi_kpi_move-prev' + kpi_kpi_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                    	me._back();

                    }
                }, {
                    id: 'kpi_kpi_move-next' + kpi_kpi_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                        me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'kpi_kpi_finish_btn' + kpi_kpi_paramdc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                    	me._finish();
                        
                    }
                }]
            },
            items: me.cardItems
        });





        Ext.applyIf(me, {
        	renderTo: me.renderTo,
            items: [me.cardpanel]
            ,listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "false" || me.editflag.length == 0) {
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