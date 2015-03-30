/**
 * 告警设置面板
 * 
 * @author 陈晓哲
 */
Ext.define('FHD.view.kpi.kpi.KpiWarningSet', {
    extend: 'FHD.ux.EditorGridPanel',
    alias: 'widget.kpiwarningset',
    
    border: false,
    style : 'border-bottom: 0px  #99bce8 solid;',
    
    /**
     * 校验日期
     */
    _validateData: function (arr) {
        var hash = {};
        for (var i in arr) {
            if (hash[arr[i]]) return true;
            hash[arr[i]] = true;
        }
        return false;
    },
    
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel,finishflag) {
        var me = this;
        var rows = me.store.data.items;
        var jsonArray = [];
        var dataArray = [];
        Ext.Array.each(rows, function (item) {
            if (item.data.date instanceof Date) {
                dataArray.push(Ext.Date.format(new Date(item.data.date), 'Y-m-d'));
            } else {
                dataArray.push(item.data.date);
            }
            jsonArray.push(item.data);
        });
        if (me._validateContent(jsonArray)) {
            if (me._validateData(dataArray)) {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.dateRepeat'));
                return;
            }
            FHD.ajax({
                url: __ctxPath + '/kpi/kpi/mergekpirelaalarm.f',
                params: {
                    modifiedRecord: Ext.JSON.encode(jsonArray),
                    id: Ext.getCmp('kpibasicform').paramObj.kpiId
                },
                callback: function (data) {
                    if (data && data.success) {
                    	var kpicardpanel =  Ext.getCmp("kpicardpanel");
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                        if(!finishflag){//如果点击的不是完成按钮,需要移动到下一个面板
                        	cardPanel.pageMove("next");
                        	kpicardpanel.navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                            /*同时将权限按钮为可用状态*/
                            //Ext.getCmp('kpi_kpi_authority_btn').setDisabled(false);
                            //Ext.getCmp('kpi_kpi_authority_btn_top').setDisabled(false);
                        }else{
                        	kpicardpanel.gotopage();
                        }
                        
                    }
                }
            });

        }
        me.store.commitChanges();
    },
    
    /**
     * 校验是否为空函数
     */
    _validateContent: function (jsonArray) {
        for (var i = 0; i < jsonArray.length; i++) {
            if (jsonArray[i].date == "") {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.strategymap.strategymapmgr.prompt.dateIsNull"));
                return false;
            }
           if((jsonArray[i].alarm==null ||jsonArray[i].alarm == "" ) &&(jsonArray[i].warning==null||jsonArray[i].warning == "")){
            	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get("fhd.alarm.need.all"));
            	return false;
            }
        }
        return true;
    },
    
    /**
     * 添加预警时函数
     */
    addWarning: function () {
        var me = this;
        var r = Ext.create('warningModel');
        me.store.add(r);
        me.editingPlugin.startEditByPosition({
            row: 0,
            column: 0
        });
        me.doComponentLayout();

    },
    /**
     * 删除预警时函数
     */
    warningDel: function () {
        var me = this;
        var selection = me.getSelectionModel().getSelection();
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') {
                    var ids = [];
                    for (var i = 0; i < selection.length; i++) {
                        me.store.remove(selection[i]);
                    }
                }
            }
        });
    },
    
    
    /**
     * 告警编辑框渲染函数
     * @param value 编辑列表中的告警值
     */
    alarmRenderFun: function (value) {
        var me = this;
        var valuestr = "";
        Ext.Array.each(value, function (v) {
            var index = me.alarmStore.find('id', v);
            var record = me.alarmStore.getAt(index);
            if (record != null) {
                valuestr += record.data.name + ",";
            }
        });
        if (valuestr.length > 0) {
            valuestr = valuestr.substring(0, valuestr.length - 1);
        }
        return "<div data-qtitle='' data-qtip='" + valuestr + "'>" + valuestr + "</div>";
    },
    
    /**
     * 预警编辑框渲染函数
     * value 编辑列表中的预警值
     */
    warningRenderFun: function (value) {
        var me = this;
        var values = "";
        Ext.Array.each(value, function (v) {
            var index = me.warningStore.find('id', v);
            var record = me.warningStore.getAt(index);
            if (record != null) {
                values += record.data.name + ",";
            }
        });
        if (values.length > 0) {
            values = values.substring(0, values.length - 1);
        }
        return "<div data-qtitle='' data-qtip='" + values + "'>" + values + "</div>";
    },
    
    
    /**
     * 初始化组件
     */
    initComponent: function () {
        var me = this;
        Ext.define('warningModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'sort', 'date', 'alarm', 'warning']
        });

        Ext.define('alarmModels', {
            extend: 'Ext.data.Model',
            fields: [{
                name: 'id',
                type: 'string'
            }, {
                name: 'name',
                type: 'string'
            }]
        });
        /**
         * 定义预警的store
         */
        me.warningStore = Ext.create('Ext.data.Store', {
            model: 'alarmModels',
            proxy: {
                type: 'ajax',
                url: __ctxPath + '/kpi/kpistrategymap/findwarningbytype.f?type=warningtype',
                reader: {
                    type: 'json',
                    root: 'warninglist'
                }
            },
            autoLoad: true
        });
        /**
         * 定义告警的store
         */
        me.alarmStore = Ext.create('Ext.data.Store', {
            model: 'alarmModels',
            proxy: {
                type: 'ajax',
                url: __ctxPath + '/kpi/kpistrategymap/findwarningbytype.f?type=alarmtype',
                reader: {
                    type: 'json',
                    root: 'warninglist'
                }
            },
            autoLoad: true
        });




        Ext.apply(me, {
        	pagable: false,
            url : __ctxPath + "/kpi/kpi/findkpirelaalarmbysome.f",
            height: FHD.getCenterPanelHeight()-75,
            extraParams:{
            	id : undefined,
            	editflag:undefined
            },
            
            cols: [{
                dataIndex: 'id',
                id: 'id',
                width: 0
            }, {
                header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.date"),
                dataIndex: 'date',
                sortable: false,
                flex: 1,
                renderer: function (value) {
                    if (value instanceof Date) {
                        return Ext.Date.format(value, 'Y-m-d');
                    } else {
                        return value;
                    }
                },
                editor: new Ext.form.DateField({
                    //在编辑器里面显示的格式,这里为10/20/09的格式  
                    format: 'm/d/y'
                })
            }, {
                header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.alarm"),
                dataIndex: 'alarm',
                sortable: false,
                flex: 2,
                editor: Ext.create('Ext.form.field.ComboBox', {
                    multiSelect: false,
                    editable: false,
                    valueField: 'id',
                    displayField: 'name',
                    labelWidth: 40,
                    store: me.alarmStore,
                    queryMode: 'alarmModels',
                    name: 'alarm'
                }),
                renderer: function (value) {
                    return me.alarmRenderFun(value);
                }
            }, {
                header: FHD.locale.get("fhd.strategymap.strategymapmgr.form.warning"),
                dataIndex: 'warning',
                sortable: false,
                flex: 2,
                editor: Ext.create('Ext.form.field.ComboBox', {
                    multiSelect: false,
                    editable: false,
                    valueField: 'id',
                    displayField: 'name',
                    labelWidth: 40,
                    store: me.warningStore,
                    queryMode: 'alarmModels',
                    name: 'warning'
                }),
                renderer: function (value) {
                    return me.warningRenderFun(value);
                }
            }],
            tbarItems: [{
                id: 'kpiaddBtn',
                iconCls: 'icon-add',
                handler: function () {
                    me.addWarning();//添加记录
                },
                scope: this
            }, '-', {
                id: 'kpidelBtn',
                iconCls: 'icon-del',
                handler: function () {
                    me.warningDel()//删除记录
                },
                disabled: true,
                scope: this
            }]
        });
        
        me.on('selectionchange', function(){
        	//删除按钮
        	if(me.down('#kpidelBtn')){
        		me.down('#kpidelBtn').setDisabled(me.getSelectionModel().getSelection().length === 0);
        	}
        }); 
        

        me.callParent(arguments);
    },
    /**
     * 重新加载列表数据
     */
    reLoadGridById: function (id,editflag) {
    	var me = this;
    	//加载告警列表数据
    	me.store.proxy.extraParams.id = id;
    	me.store.proxy.extraParams.editflag = editflag;
		me.store.load();
    }

});