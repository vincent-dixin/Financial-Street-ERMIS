/**
 * 添加记分卡时主面板,在其中放入了一个card布局的panel,每一步的panel为基本信息面板-kpicategoryBasicPanel.js,告警设置面板-kpicategoryWarningPanel.js
 * 继承于Ext.container.Container
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.kpi.opt.kpicategoryMainPanel', {
    extend: 'Ext.tab.Panel',
    border: true,
    
    /**
     * 返回按钮事件
     */
    _undo:function(){
    	var me = this;
    	me._gotopage();
    },
    /**
     * 返回首页
     */
    _gotopage:function(){
    	var me = this;
    	var kpitypeEditUrl = __ctxPath + "/pages/kpi/kpi/opt/kpitypeedit.jsp?";
    	var rightUrl = category_editUrl + "categoryparentid=" + categoryparentid + "&editflag=true" + "&categoryid=" + categoryid + "&categoryparentname=" + encodeURIComponent(categoryparentname) + "&categoryname=" + encodeURIComponent(categoryname);
    	fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
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
        me._lastSetBtnState(cardPanel, activePanel);
    },
    /**
     * 上一步按钮事件
     */
    _back:function(){
    	var me = this;
    	var cardPanel = me.cardpanel;
        cardPanel.pageMove("prev");
        var activePanel = cardPanel.getActiveItem();
        me._navBtnState();
        me._preSetBtnState(cardPanel, activePanel);
    },
    /**
     * 设置上一步和下一步按钮的状态
     */
    _navBtnState:function(){
    	var me = this;
    	var layout = me.cardpanel.getLayout();
    	Ext.getCmp('kpicategory_move-prev' + kpicategory_paramdc).setDisabled(!layout.getPrev());
        Ext.getCmp('kpicategory_move-next' + kpicategory_paramdc).setDisabled(!layout.getNext());
        Ext.getCmp('kpicategory_move-prev_top' + kpicategory_paramdc).setDisabled(!layout.getPrev());
        Ext.getCmp('kpicategory_move-next_top' + kpicategory_paramdc).setDisabled(!layout.getNext());
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
    //添加指标
    kpiaddFun: function () {
        var me = this;
        var url = __ctxPath + "/pages/kpi/kpi/opt/kpiedit.jsp?editflag=false" + "&categoryparentid=" + me.categoryparentid + "&categoryid=" + me.categoryid + "&categoryparentname=" + encodeURIComponent(me.categoryparentname) + "&categoryname=" + encodeURIComponent(me.categoryname);
        fhd_kpi_kpiaccordion_view.initRightPanel(url);
    },
    //修改指标
    kpiEditFun: function () {
        var me = this;
        var selections = me.grid.getSelectionModel().getSelection();
        var length = selections.length;
        if (length > 0) {
            if (length >= 2) {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.kpi.kpi.prompt.editone'));
                return;
            } else {
                var selection = selections[0]; //得到选中的记录
                var kpiId = selection.get('id'); //获得指标ID
                var kpiname = selection.get('name');
                var url = __ctxPath + "/pages/kpi/kpi/opt/kpiedit.jsp?editflag=true" + "&categoryparentid=" + me.categoryparentid + "&categoryid=" + me.categoryid + "&categoryparentname=" + encodeURIComponent(me.categoryparentname) + "&categoryname=" + encodeURIComponent(me.categoryname) + "&kpiId=" + kpiId + "&kpiname=" + encodeURIComponent(kpiname);
                fhd_kpi_kpiaccordion_view.initRightPanel(url);
            }
        } else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
    },
    //指标列表监听事件
    _addListerner: function () {
        var me = this;
        me.grid.on('selectionchange', function () {
            if (me.grid.down('#kpicategory_kpiedit' + kpicategory_paramdc)) {
                me.grid.down('#kpicategory_kpiedit' + kpicategory_paramdc).setDisabled(me.grid.getSelectionModel().getSelection().length === 0);
            }
            if (me.grid.down('#kpicategory_kpidel' + kpicategory_paramdc)) {
                me.grid.down('#kpicategory_kpidel' + kpicategory_paramdc).setDisabled(me.grid.getSelectionModel().getSelection().length === 0);
            }

        }); //选择记录发生改变时改变按钮可用状态

    },
    //删除指标
    kpiDelFun: function () {
        var me = this;
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') { //确认删除
                    var paraobj = {};
                    paraobj.categoryid = categoryid;
                    paraobj.kpiids = [];
                    var selections = me.grid.getSelectionModel().getSelection();
                    Ext.Array.each(selections, function (item) {
                        paraobj.kpiids.push(item.get("id"));
                    });

                    FHD.ajax({
                        url: __ctxPath + '/kpi/kpi/removekpibatch.f',
                        params: {
                            kpiItems: Ext.JSON.encode(paraobj)
                        },
                        callback: function (data) {
                            if (data && data.success) {
                                me.grid.store.load();
                            }
                        }
                    });
                }
            }
        });
    },
    //关联指标
    kpiRelaFun: function () {
        var me = this;
        var selectorWindow = Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow', {
        	multiSelect: true,
        	onsubmit:function(store){
        		var idArray = [];
        		var items = store.data.items;
        		Ext.Array.each(items,function(item){
            		idArray.push(item.data.id);
            	});
        		if(idArray.length>0){
        			var paraobj = {
                            categoryId: categoryid,
                            kpiIds: idArray
                        };
                    FHD.ajax({
                    url: __ctxPath + '/kpi/category/mergecategoryrelakpi.f',
                    params: {
                        param: Ext.JSON.encode(paraobj)
                    },
                    callback: function (data) {
                        if (data && data.success) {
                            me.grid.store.load();
                        }
                    }
                	});
        		}
        		
        	}
        }).show();

    },
    /**
     * 设置导航按钮的事件函数
     * @param {panel} cardPanel cardpanel面板
     * @param index 面板索引值
     */
    _navBtnHandler: function (cardPanel, index) {
    	var me = this;
        cardPanel.getLayout().setActiveItem(index);
        me._navBtnState();
        
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
        var topbar = Ext.getCmp('kpicategory_topbar' + kpicategory_paramdc);
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
        var bbar = Ext.getCmp('kpicategory_bbar' + kpicategory_paramdc);
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
     * 初始化组件
     */
    initComponent: function () {
        var arr = '<img src="'+__ctxPath+'/images/icons/show_right.gif">';
        var me = this;
    	
        me.cardpanel = Ext.create('FHD.ux.CardPanel', {
            title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
            xtype: 'cardpanel',
            destoryflag:'true',
            activeItem: 0,
            tbar: {
                id: 'kpicategory_topbar' + kpicategory_paramdc,
                items: [

                {
                    text: FHD.locale.get('fhd.common.details'),//基本信息按钮
                    iconCls: 'icon-001',
                    id: 'kpicategory_details_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置按钮
                    iconCls: 'icon-002',
                    id: 'kpicategory_alarmset_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);
                    }
                },
                //arr, 
                /*{
                    text: FHD.locale.get("fhd.sys.auth.authority.authority"),//权限按钮
                    iconCls: 'icon-003',
                    id: 'kpicategory_authority_btn_top' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },*/
                    '->',
                    {
                        id: 'kpicategory_move-undo_top' + kpicategory_paramdc,
                        text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                        iconCls: 'icon-arrow-undo',
                        handler: function () {
                        	me._undo();
                        }
                    },
                    {
                    id: 'kpicategory_move-prev_top' + kpicategory_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                    	me._back();
                    }
                }, {
                    id: 'kpicategory_move-next_top' + kpicategory_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                        me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'kpicategory_finish_btn_top' + kpicategory_paramdc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me._finish();
                    }
                }]
            },
            bbar: {
                id: 'kpicategory_bbar' + kpicategory_paramdc,
                items: [

                {
                    text: FHD.locale.get('fhd.common.details'),//基本信息按钮
                    iconCls: 'icon-001',
                    id: 'kpicategory_details_btn' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(0);
                        me._navBtnHandler(this.up('panel'), 0);
                    }
                },
                arr, {
                    text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置按钮
                    iconCls: 'icon-002',
                    id: 'kpicategory_alarmset_btn' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(1);
                        me._navBtnHandler(this.up('panel'), 1);

                    }
                },
                //arr,
                /*{
                    text: FHD.locale.get("fhd.sys.auth.authority.authority"),//权限按钮
                    iconCls: 'icon-003',
                    id: 'kpicategory_authority_btn' + kpicategory_paramdc,
                    handler: function () {
                        me._setBtnState(2);
                        me._navBtnHandler(this.up('panel'), 2);
                    }
                },*/
                    '->', 
                    {
                        id: 'kpicategory_move-undo' + kpicategory_paramdc,
                        text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                        iconCls: 'icon-arrow-undo',
                        handler: function () {
                        	me._undo();
                        }
                    },
                    {
                    id: 'kpicategory_move-prev' + kpicategory_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
                    iconCls: 'icon-control-rewind-blue',
                    handler: function () {
                        me._back();
                    }
                }, {
                    id: 'kpicategory_move-next' + kpicategory_paramdc,
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
                    iconCls: 'icon-control-fastforward-blue',
                    handler: function () {
                    	 me._last();
                    }
                }, {
                    text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
                    id: 'kpicategory_finish_btn' + kpicategory_paramdc,
                    iconCls: 'icon-control-stop-blue',
                    handler: function () {
                        me._finish();
                    }
                }]
            },
            items: me.cardItems
        });
        /**
         * 指标列表grid
         */
        me.grid = Ext.create('FHD.ux.kpi.KpiGridPanel', {
            title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
            url: me.kpiListUrl,
        	//cls:'rowspan-grid',
            tbarItems: [{
                tooltip: FHD.locale.get('fhd.kpi.kpi.op.addkpi'),
                iconCls: 'icon-add',
                id: 'kpicategory_kpiadd' + kpicategory_paramdc,
                handler: function () {
                    me.kpiaddFun();
                }
            }, '-', {
                tooltip: FHD.locale.get('fhd.kpi.kpi.op.relakpi'),
                id: 'kpicategory_kpirela' + kpicategory_paramdc,
                iconCls: 'icon-plugin-add',
                handler: function () {
                    me.kpiRelaFun();
                }
            }, '-', {
                tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.editkpi"),
                id: 'kpicategory_kpiedit' + kpicategory_paramdc,
                iconCls: 'icon-edit',
                disabled: true,
                handler: function () {
                    me.kpiEditFun();
                }
            }, '-', {
                tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.delkpi"),
                id: 'kpicategory_kpidel' + kpicategory_paramdc,
                iconCls: 'icon-del',
                disabled: true,
                handler: function () {
                    me.kpiDelFun();
                }
            }

            ],
            type: 'category'
        });
        
      

        Ext.apply(me, {
        	plain: true,
        	deferredRender:false,
        	height: FHD.getCenterPanelHeight() - 20,
        	renderTo: me.renderTo,
        	//activeTab: 1,
        	tabBar:{
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: [me.grid,me.analysisChartMainPanel,me.cardpanel],
            listeners: {
                afterrender: function (t, e) {
                    //默认进来如果没有选择记分卡节点,则使工具栏按钮不可用
                    var toolbarflag = true;
                    if (categoryid != "undefined"&&categoryid !="") {
                        toolbarflag = false;
                    }
                    Ext.getCmp('kpicategory_kpiadd' + kpicategory_paramdc).setDisabled(toolbarflag);
                    Ext.getCmp('kpicategory_kpirela' + kpicategory_paramdc).setDisabled(toolbarflag);

                    if (me.editflag == "undefined" || me.editflag == "false" || me.editflag.length == 0) {
                        //添加时,使除了'基本信息'按钮可用外,其它按钮为不可用状态
                        Ext.getCmp('kpicategory_alarmset_btn' + kpicategory_paramdc).setDisabled(true);
                        //Ext.getCmp('kpicategory_authority_btn' + kpicategory_paramdc).setDisabled(true);
                        Ext.getCmp('kpicategory_alarmset_btn_top' + kpicategory_paramdc).setDisabled(true);
                        //Ext.getCmp('kpicategory_authority_btn_top' + kpicategory_paramdc).setDisabled(true);
                    }
                    //设置基本信息按钮为选中状态
                    me._setBtnState(0);
                    //设置首面板为激活面板
                    me._navBtnHandler(me.cardpanel, 0);

                    me._addListerner();
                }
            }
        });
        me.callParent(arguments);
        
    }

});