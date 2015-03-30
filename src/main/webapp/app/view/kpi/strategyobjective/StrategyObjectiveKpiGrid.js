Ext.define('FHD.view.kpi.strategyobjective.StrategyObjectiveKpiGrid',{
    extend: 'FHD.ux.kpi.KpiGridPanel',
    alias: 'widget.strategyobjectivekpigrid',
    
    title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
    
    /**
     * 初始化该类所用到的参数
     */
    
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
    },
    /**
     * 删除指标
     */
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
                    var kpiids = [];
                    var selections = me.getSelectionModel().getSelection();
                    Ext.Array.each(selections, function (item) {
                        kpiids.push(item.get("id"));
                    });

                    FHD.ajax({
                        url: __ctxPath + '/kpi/kpi/removecommonkpibatch.f',
                        params: {
                            kpiItems: Ext.JSON.encode(kpiids)
                        },
                        callback: function (data) {
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
     * 编辑指标
     */
    kpiEditFun:function(){
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
                kpicardpanel.backType = "sm";
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
            	Ext.getCmp('kpimainpanel').reLoadNav("sm");
            }
        } else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
    },
    addListerner: function () {
        var me = this;
        me.on('selectionchange', function () {
            if (me.down('#sm_kpiedit')) {
                me.down('#sm_kpiedit').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if (me.down('#sm_kpidel' )) {
                me.down('#sm_kpidel' ).setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if(me.down('#sm_disable')){
            	me.down('#sm_disable').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if(me.down('#sm_enable')){
            	me.down('#sm_enable').setDisabled(me.getSelectionModel().getSelection().length === 0);
            }

        }); //选择记录发生改变时改变按钮可用状态

    },
    initComponent: function () {
        var me = this;
        Ext.apply(me, {
        	url : __ctxPath + "/kpi/kpistrategymap/findsmrelakpiresult.f",
            extraParams:{
            	id : me.smid,
            	year : FHD.data.yearId,
            	month : FHD.data.monthId,
            	quarter : FHD.data.quarterId,
            	week : FHD.data.weekId,
            	eType : FHD.data.eType,
            	isNewValue : FHD.data.isNewValue
            },
            checked: true,
            type: 'strategyobjectivekpigrid',
            tbarItems: [
                        {
                         tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.editkpi"),
                         id: 'sm_kpiedit',
                         iconCls: 'icon-edit',
                         disabled: true,
                         handler: function () {
                            me.kpiEditFun();
                         }
                       },
                       '-',
                       {
                           tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.delkpi"),
                           id: 'sm_kpidel',
                           iconCls: 'icon-del',
                           disabled: true,
                           handler: function () {
                             me.kpiDelFun();
                           }
                       },
                       '-', {
                          	tooltip:  FHD.locale.get('fhd.sys.planMan.start'),
                              iconCls: 'icon-plan-start',
                              id: 'sm_enable',
                              handler: function() {
                                  me.enables("0yn_y");
                              },
                              disabled: true
                          },
                          '-', {
                          	tooltip:  FHD.locale.get('fhd.sys.planMan.stop'),
                              iconCls: 'icon-plan-stop',
                              id: 'sm_disable',
                              handler: function() {
                                  me.enables("0yn_n");
                              },
                              disabled: true
                          }
            ]
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
    /**
     * 重新加载列表数据
     */
    reLoadData:function(){
    	var me = this;
    	if(me.paramObj!=undefined){
    		me.store.proxy.extraParams.id = me.paramObj.smid;
    		me.store.load();
    	}
    },
    gatherResultFun : function(v){
    	PARAM.sm.smId = Ext.getCmp('strategyobjectivetab').paramObj.smid;
    	PARAM.sm.smName = v;
    	PARAM.kpiname = v;
    	PARAM.type = 'sm';
    	
    	if(Ext.getCmp('manPanel') == null){
    		Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.widget('manPanel').load(PARAM));
    	}else{
    		Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('manPanel').load(PARAM));
    	}
    }


});