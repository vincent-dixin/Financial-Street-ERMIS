/*
 * File: app/view/KpiGrid.js
 *
 */

Ext.define('FHD.view.kpi.scorecard.ScorecardKpiGrid', {
    extend: 'FHD.ux.kpi.KpiGridPanel',
    alias: 'widget.scorecardkpigrid',

    title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),

    /**
     * 删除指标
     */
    kpiDelFun: function() {
        var me = this;
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function(btn) {
                if (btn == 'yes') { //确认删除
                    var paraobj = {};
                    paraobj.categoryid = Ext.getCmp('scorecardtab').paramObj.categoryid;
                    paraobj.kpiids = [];
                    var selections = me.getSelectionModel().getSelection();
                    Ext.Array.each(selections,
                    function(item) {
                        paraobj.kpiids.push(item.get("id"));
                    });

                    FHD.ajax({
                        url: __ctxPath + '/kpi/kpi/removekpibatch.f',
                        params: {
                            kpiItems: Ext.JSON.encode(paraobj)
                        },
                        callback: function(data) {
                            if (data && data.success) {
                                me.store.load();
                            }
                        }
                    });
                }
            }
        });
    },

    /**
     * 关联指标
     */
    kpiRelaFun: function() {
        var me = this;
        var selectorWindow = Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow', {
            multiSelect: true,
            onsubmit: function(store) {
                var idArray = [];
                var items = store.data.items;
                Ext.Array.each(items,
                function(item) {
                    idArray.push(item.data.id);
                });
                if (idArray.length > 0) {
                    var paraobj = {
                        categoryId: Ext.getCmp('scorecardtab').paramObj.categoryid,
                        kpiIds: idArray
                    };
                    FHD.ajax({
                        url: __ctxPath + '/kpi/category/mergecategoryrelakpi.f',
                        params: {
                            param: Ext.JSON.encode(paraobj)
                        },
                        callback: function(data) {
                            if (data && data.success) {
                                me.store.load();
                            }
                        }
                    });
                }

            }
        }).show();
    },
    /**
     * 添加指标
     */
    kpiaddFun: function() {
        var kpimainpanel = Ext.getCmp('kpimainpanel');
        var kpicardpanel = Ext.getCmp('kpicardpanel');
        var kpibasicform = Ext.getCmp('kpibasicform');
        var kpigatherform = Ext.getCmp('kpigatherform');
        var paramObj = {};
        paramObj.editflag = false;
        kpimainpanel.initParam(paramObj);
        kpicardpanel.setInitBtnState(); //设置导航按钮初始化状态,添加时,后两个按钮置灰;
        kpicardpanel.backType = "sc";
        Ext.getCmp('metriccentercardpanel').setActiveItem(kpimainpanel);
        //清空kpibasicform
        kpibasicform.clearFormData();
        //初始化kpibasicform默认值
        kpibasicform.initFormData();
        //清空kpigatherform数据
        kpigatherform.clearFormData();
        //初始化kpigatherform默认值
        kpigatherform.initFormData();
        //清除kpibasicform的参数信息
        kpibasicform.clearParamObj();
        //加载预警列表数据,如果不选择指标类型需要有默认值
        Ext.getCmp('kpiwarningset').reLoadGridById("", false);
        //恢复到第一个面板
        kpicardpanel.setFirstItemFoucs(true);
        //设置导航条
        Ext.getCmp('kpimainpanel').reLoadNav("sc");
    },
    /**
     * 编辑指标
     */
    kpiEditFun: function() {
        var me = this;
        var selections = me.getSelectionModel().getSelection();
        var length = selections.length;
        if (length > 0) {
            if (length >= 2) {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.kpi.kpi.prompt.editone'));
                return;
            } else {
                var selection = selections[0]; //得到选中的记录
                var kpiId = selection.get('id'); //获得指标ID
                var kpiname = selection.get('name');
                var kpibasicform = Ext.getCmp('kpibasicform');
                var kpimainpanel = Ext.getCmp('kpimainpanel');
                var kpicardpanel = Ext.getCmp('kpicardpanel');
                kpicardpanel.backType = "sc";
                //设置kpibasicform中的参数
                kpibasicform.paramObj.kpiId = kpiId;
                kpibasicform.paramObj.kpiname = kpiname;
                //将selecttypeflag设置为空,防止form表单加载指标类型数据
                kpibasicform.paramObj.selecttypeflag = '';
                //设置kpimainpanel中的参数
                kpimainpanel.paramObj.editflag = true;
                //设置kpimainpanel为当前激活面板
                Ext.getCmp('metriccentercardpanel').setActiveItem(kpimainpanel);
                //恢复到第一个面板
                kpicardpanel.setFirstItemFoucs(false);
                //加载form数据
                kpibasicform.formLoad();
                //给公式赋值name
                kpibasicform.valueToFormulaName();
                //设置导航条
                Ext.getCmp('kpimainpanel').reLoadNav("sc");
            }
        } else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
    },
    //指标列表监听事件
    addListerner: function() {
        var me = this;
        me.on('selectionchange',
        function() {
            if (me.down('#kpicategory_kpiedit')) {
                me.down('#kpicategory_kpiedit').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if (me.down('#kpicategory_kpidel')) {
                me.down('#kpicategory_kpidel').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if(me.down('#scorecardkpi_disable')){
            	me.down('#scorecardkpi_disable').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if(me.down('#scorecardkpi_enable')){
            	me.down('#scorecardkpi_enable').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }

        }); //选择记录发生改变时改变按钮可用状态
    },
    initComponent: function() {
        var me = this;
        Ext.apply(me, {
            url: __ctxPath + "/kpi/category/findcategoryrelakpiresult.f",
            extraParams: {
                id: me.categoryid,
                year: FHD.data.yearId,
                month: FHD.data.monthId,
                quarter: FHD.data.quarterId,
                week: FHD.data.weekId,
                eType: FHD.data.eType,
                isNewValue: FHD.data.isNewValue
            },
            tbarItems: [{
                tooltip: FHD.locale.get('fhd.kpi.kpi.op.addkpi'),
                iconCls: 'icon-add',
                id: 'kpicategory_kpiadd',
                handler: function() {
                    me.kpiaddFun();
                }
            },
            '-', {
                tooltip: FHD.locale.get('fhd.kpi.kpi.op.relakpi'),
                id: 'kpicategory_kpirela',
                iconCls: 'icon-plugin-add',
                handler: function() {
                    me.kpiRelaFun();
                }
            },
            '-', {
                tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.editkpi"),
                id: 'kpicategory_kpiedit',
                iconCls: 'icon-edit',
                disabled: true,
                handler: function() {
                    me.kpiEditFun();
                }
            },
            '-', {
                tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.delkpi"),
                id: 'kpicategory_kpidel',
                iconCls: 'icon-del',
                disabled: true,
                handler: function() {
                    me.kpiDelFun();
                }
            },
            '-', {
            	tooltip:  FHD.locale.get('fhd.sys.planMan.start'),
                iconCls: 'icon-plan-start',
                id: 'scorecardkpi_enable',
                handler: function() {
                    me.enables("0yn_y");
                },
                disabled: true
            },
            '-', {
            	tooltip:  FHD.locale.get('fhd.sys.planMan.stop'),
                iconCls: 'icon-plan-stop',
                id: 'scorecardkpi_disable',
                handler: function() {
                    me.enables("0yn_n");
                },
                disabled: true
            }

            ],
            type: 'scorecardkpigrid'
        });

        me.callParent(arguments);

        me.addListerner();

    },
    enables: function(enable) {
        var me = this;
        var paraobj = {};
        paraobj.enable = enable;
        paraobj.kpiids = [];
        var selections = me.getSelectionModel().getSelection();
        Ext.Array.each(selections,
        function(item) {
            paraobj.kpiids.push(item.get("id"));
        });

        FHD.ajax({
            url: __ctxPath + '/kpi/kpi/mergekpienable.f',
            params: {
                kpiItems: Ext.JSON.encode(paraobj)
            },
            callback: function(data) {
                if (data && data.success) {
                    me.store.load();
                }
            }
        });
    },
    gatherResultFun: function(v) {
    	var me = this;
    	var jsobj = {};
		jsobj.name = v;
		FHD.ajax({
			async:false,
            url: __ctxPath + '/kpi/kpi/findkpiinfobyname.f',
            params: {
                param: Ext.JSON.encode(jsobj)
            },
            callback: function (data) {
            	me.kpifrequence = data.frequence;
            	me.kpiId = data.kpiid;
            }
        });
    	if("0frequecy_relatime"==me.kpifrequence){//实时指标
    		 var realtimemain = Ext.widget('realtimemain',{
    			 id:Ext.getCmp('scorecardtab').paramObj.categoryid,
    			 name:v,
    			 kpiId:me.kpiId
    		 });
    		 Ext.getCmp('metriccentercardpanel').setActiveItem(realtimemain);
    	}else{
    		PARAM.category.categoryId = Ext.getCmp('scorecardtab').paramObj.categoryid;
            PARAM.category.categoryName = v;
            PARAM.kpiname = v;
            PARAM.type = 'category';

            if (Ext.getCmp('manPanel') == null) {
                Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.widget('manPanel').load(PARAM));
            } else {
                Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('manPanel').load(PARAM));
            }
    	}
        
    }

});