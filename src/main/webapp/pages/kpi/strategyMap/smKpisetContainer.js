/**
 * 添加目标时,衡量指标面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.strategyMap.smKpisetContainer', {
    extend: 'Ext.container.Container',
    border: false,
    height: FHD.getCenterPanelHeight(),
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel, finishflag) {
        var me = this;
        var flag = false;
        var parameter = "";
        var storeItems = me.grid.store.data.items;
        Ext.Array.each(storeItems, function (object) {
            var item = object.data
            if (!(item.weight >= 1 && item.weight <= 100)) {
                flag = true;
            }
            parameter += item.id + "," + item.weight + ";";
        });
        if (flag) {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.strategymap.strategymapmgr.form.weighterror"));
            return;
        }
        /* 提交指标数据*/
        if (!flag) {
            FHD.ajax({
                params: {
                    "kpiParam": parameter,
                    "currentSmId": smid
                },
                url: __ctxPath + '/kpi/kpistrategymap/mergestrategyrelakpi.f',
                callback: function (ret) {
                    if (ret && ret.success) {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                        var layout = cardPanel.getLayout();
                        if (!finishflag) {
                        	cardPanel.pageMove("next");
                            smcontainer._navBtnState(cardPanel);
                            Ext.getCmp('sm_alarmset_btn' + sm_paramdc).setDisabled(false);
                            Ext.getCmp('sm_alarmset_btn_top' + sm_paramdc).setDisabled(false);
                        }else{
                        	smcontainer._gotopage();
                        }
                    }
                }
            });
        }
        me.grid.store.commitChanges();
    },
    /**
     * 查找衡量指标列表中的指标和权重信息
     */
    findKpiObjects: function () {
        var me = this;
        var i = 0;
        var selectedvalues = {
            data: {
                items: []
            }
        };
        var kpiWeight = {};
        var items = me.grid.store.data.items
        Ext.Array.each(items, function (object) {
            var item = object.data;
            var insertobj = {
                data: {
                    id: item.id,
                    name: item.name
                }
            }
            selectedvalues.data.items[i++] = insertobj;
            selectedvalues.data.length = i;
            kpiWeight[item.id] = item.weight;
        });
        return {
            'kpiWeight': kpiWeight,
            'kpiarr': selectedvalues
        };
    },
    /**
     * 指标选择弹出窗口函数
     */
    popKpiSelectorWindow: function () {
        var me = this;
        var kpiobj = me.findKpiObjects();
        var selectorWindow = Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow', {
            multiSelect: true,
            selectedvalues: kpiobj.kpiarr,
            onsubmit: function (store) {
                var list = [];
                var idArray = [];
                me.grid.store.removeAll();
                var items = store.data.items;
                Ext.Array.each(items, function (obj) {
                    var item = obj.data;
                    var kpim = new kpiModel({
                        id: item.id,
                        name: item.name,
                        weight: kpiobj.kpiWeight[item.id]
                    });
                    idArray.push(kpim.data.id);
                    list.push(kpim);
                });
                //需要查询所属部门
                FHD.ajax({
                    url: __ctxPath + '/kpi/kpistrategymap/findkpirelaorgempbyid.f',
                    params: {
                        kpiID: Ext.JSON.encode(idArray)
                    },
                    callback: function (data) {
                        if (data && data.kpiRelOrg) {
                            var vobj = Ext.JSON.decode(data.kpiRelOrg);
                            for (var i = 0; i < list.length; i++) {
                                list[i].set("dept", vobj[list[i].get("id")]);
                                me.grid.store.add(list[i]);
                            }
                        }
                    }
                });


            }
        }).show();

    },
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        Ext.define('kpiModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'code', 'text', 'name', 'dbid', 'type', 'sort', 'weight', 'oper', 'dept']
        });
        me.grid = Ext.create('FHD.ux.EditorGridPanel', {
            border: false,
            pagable: false,
            destoryflag:'true',
            /*tbar:{
            	height:0.1,
            	border:false
            },*/
            checked: false,
            url: __ctxPath + '/kpi/kpistrategymap/findsmrelakpibysome.f?currentSmId=' + smid,
            //height: FHD.getCenterPanelHeight() - 50,
            cols: [{
                header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.kpiname"),//指标名称
                flex: 1,
                dataIndex: 'name',
                sortable: false,
                flex: 1.5,
                renderer: function (value, metaData, record, rowIndex, colIndex, store) {
                    return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
                }
            }, {
                header: FHD.locale.get('fhd.strategymap.strategymapmgr.form.Dept'),//所属部门
                dataIndex: 'dept',
                sortable: false,
                flex: 3
            }, {
                header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.weight"),//权重
                renderer: function (value, metaData, record, rowIndex, colIndex, store) {
                    return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
                },
                dataIndex: 'weight',
                sortable: false,
                flex: 0.5,
                editor: {
                    allowBlank: false,
                    xtype: 'numberfield',
                    maxValue: 100,
                    minValue: 1,
                    allowDecimals: true,
                    nanText: FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.inputNum'),
                    step: 0.5
                }
            }, {
                header: FHD.locale.get('fhd.strategymap.strategymapmgr.form.oper'),//操作
                dataIndex: 'oper',
                xtype: 'templatecolumn',
                sortable: false,
                flex: 0.5,
                text: $locale('fhd.common.delete'),
                tpl: '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                listeners: {
                    click: {
                        fn: function (g, d, i) {
                            g.store.removeAt(i);
                        }
                    }
                }
            }],
            tbarItems: [{
                tooltip: FHD.locale.get('fhd.strategymap.strategymapmgr.form.set'),
                xtype: 'button',
                iconCls: 'icon-cog',
                columnWidth: 0.1,
                handler: function () {
                	//弹出指标选择按钮
                    me.popKpiSelectorWindow();
                }
            }]
        });


        Ext.applyIf(me, {
            items: [me.grid]



        });

        me.callParent(arguments);


    }



});