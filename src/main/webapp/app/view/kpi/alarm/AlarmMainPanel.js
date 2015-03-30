Ext.define('FHD.view.kpi.alarm.AlarmMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.alarmmainpanel',
    layout: 'border',
    
    requires: [
               'FHD.view.kpi.alarm.AlarmEditPanel'
              ],

    /*删除告警*/
    del: function () { //删除方法
        var me = this;
        var selection = me.alarm_mgr_grid.getSelectionModel().getSelection(); //得到选中的记录
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') { //确认删除
                    var ids = [];
                    for (var i = 0; i < selection.length; i++) {
                        ids.push(selection[i].get('id'));
                    }
                    FHD.ajax({ //ajax调用
                        url: __ctxPath + "/kpi/alarm/removealarmplan.f",
                        params: {
                            ids: ids.join(',')
                        },
                        callback: function (data) {
                            if (data && data.success) { //删除成功！
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                me.alarm_mgr_grid.store.load();
                            } else {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.alarmplan.prompt.delerror'));
                            }
                        }
                    });
                }
            }
        });
    },


    /*编辑告警*/
    edit: function (button) {
        var me = this;
        var selections = me.alarm_mgr_grid.getSelectionModel().getSelection();
        var length = selections.length;
        if (length >= 2) {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.alarmplan.prompt.editAlone'));
            return;
        }
        var selection = selections[0]; //得到选中的记录
        formwindow = new Ext.Window({
            constrain: true,
            layout: 'fit',
            iconCls: 'icon-edit', //标题前的图片
            modal: true, //是否模态窗口
            collapsible: true,
            scroll: 'auto',
            width: 800,
            height: 550,
            maximizable: true //（是否增加最大化，默认没有）
        });
        
        if (button.id == 'alarmadd') {
            formwindow.setTitle(FHD.locale.get('fhd.common.add'));
            isAdd = true;
        } else {
            formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
            formwindow.initialConfig.id = selection.get('id');
            isAdd = false;
        }
        
        var editpanel = Ext.create('FHD.view.kpi.alarm.AlarmEditPanel',{
        	isAdd:isAdd,
        	grid:me.alarm_mgr_grid
        });
        
        formwindow.add(editpanel);
        
        formwindow.show();
        
    },
    setstatus: function () { //设置你按钮可用状态
        var me = this;
        var length = me.alarm_mgr_grid.getSelectionModel().getSelection().length;
        me.alarm_mgr_grid.down('#del').setDisabled(length === 0);
        me.alarm_mgr_grid.down('#edit').setDisabled(length === 0);
    },
    getRegionsValues: function (id) {
        var me = this;
        FHD.ajax({ //ajax调用
            url: __ctxPath + "/kpi/alarm/findalarmplanregionstoviewbyid.f",
            params: {
                id: id
            },
            callback: function (data) {
                if (data && data.success) {
                	var rangevalue = [];
                	rangevalue.push(' <table align="center"  valign="middle" width="100%">');
                	rangevalue.push('<tr><td width="30%" style="text-align: center;font-size: 15;border: 2px #99bbe8 solid;">等级</td><td style="text-align: center;font-size: 15;border: 2px #99bbe8 solid;">区间</td></tr>');
                	var flag = false;
                	var high = '';
                	var mid = '';
                	var low = '';
                	var safe = '';
                	for(var k in data.regions){
                		if(k=='high'){
                			flag = true;
                			high = '<tr><td style="text-align: center;border: 2px #99bbe8 solid;" title="'+FHD.locale.get("fhd.alarmplan.form.hight")+'"><img src="' + __ctxPath + '/images/icons/symbol_jrj_r_sm.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;'+data.regions[k]+'</td></tr>';
                		}else if(k=='mid'){
                			flag = true;
                			mid = '<tr><td style="text-align: center;border: 2px #99bbe8 solid;" title="'+FHD.locale.get("fhd.alarmplan.form.min")+'"><img src="' + __ctxPath + '/images/icons/symbol_jrj_c_sm.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;'+data.regions[k]+'</td></tr>';
                		}else if(k=='low'){
                			flag = true;
                			low = '<tr><td style="text-align: center;border: 2px #99bbe8 solid;" title="'+FHD.locale.get("fhd.alarmplan.form.low")+'"><img src="' + __ctxPath + '/images/icons/symbol_jrj_y_sm.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;'+data.regions[k]+'</td></tr>' ;
                		}else if(k=='safe'){
                			flag = true;
                			safe = '<tr><td style="text-align: center;border: 2px #99bbe8 solid;" title="'+"安全"+'"><img src="' + __ctxPath + '/images/icons/symbol_jrj_g_sm.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;'+data.regions[k]+'</td></tr>' ;
                			
                		}
                	}
                	if(high.length>0){
                		rangevalue.push(high);
                	}
                	if(mid.length>0){
                		rangevalue.push(mid);
                	}
                	if(low.length>0){
                		rangevalue.push(low);
                	}
                	if(safe.length>0){
                		rangevalue.push(safe);
                	}
                	rangevalue.push('</table>');
                	if(flag){
                		var tpl = Ext.create('Ext.Template',rangevalue);
                	}else{
                		var tpl = Ext.create('Ext.Template',[]);
                	}
                	tpl.overwrite(me.center.body);
                    
                }
            }
        });
    },
    reloaddata:function(){
    	var me = this;
    },
    initComponent: function () {
        var me = this;

        var gridColums = [{
            header: FHD.locale.get('fhd.pages.test.field.name'),
            dataIndex: 'name',
            sortable: true,
            flex: 1.5,
            renderer: function (value, metaData, record, colIndex, store, view) {
                return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
            }
        }, {
            header: FHD.locale.get('fhd.common.type'),
            dataIndex: 'types',
            sortable: true,
            flex: 1
        }, {
            header: FHD.locale.get('fhd.alarmplan.form.desc'),
            dataIndex: 'descs',
            sortable: true,
            flex: 2
        }];

        var tbar = [ //菜单项
        {
            text: FHD.locale.get('fhd.common.add'),
            iconCls: 'icon-add',
            id: 'alarmadd',
            handler: function () {
                me.edit(this)
            }
        }, '-', {
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.edit'),
            id: 'edit',
            iconCls: 'icon-edit',
            disabled: true,
            handler: function () {
                me.edit(this)
            }
        }, '-', {
            text: FHD.locale.get('fhd.common.delete'),
            iconCls: 'icon-del',
            id: 'del',
            handler: function () {
                me.del()
            },
            disabled: true,
            scope: this
        }];

        me.center = Ext.create('Ext.panel.Panel', {
            region: 'center',
            width: 10,
            height: FHD.getCenterPanelHeight(),
            autoScroll: true,
            bodyPadding: '5 5 0',
            bodyStyle: {
                background: '#ffffff'
            },
            html: '',
            tbar: [{
                height: 21,
                xtype: 'tbtext',
                text: FHD.locale.get('fhd.alarmplan.form.alarmrange')
            }],
            bbar: [{
                height: 22,
                xtype: 'tbtext'
            }]
        });

        me.alarm_mgr_grid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            border: true,
            region: 'west',
            width: 1000,
            url: __ctxPath + "/kpi/alarm/findalarmplanbysome.f", //调用后台url
            height: FHD.getCenterPanelHeight(), //高度为：获取center-panel的高度
            cols: gridColums, //cols:为需要显示的列
            tbarItems: tbar

        });

        me.alarm_mgr_grid.store.on('load', function () {
            me.setstatus();
        });
        me.alarm_mgr_grid.on('selectionchange', function ( model,  selected,  eOpts ) {
            me.setstatus();
            if(selected.length==1){
            	var id = model.getSelection()[0].get('id');
                me.getRegionsValues(id);
            }
            if(selected.length==0){
            	var tpl = Ext.create('Ext.Template',[]);
            	tpl.overwrite(me.center.body);
        	}
            
        });
        me.alarm_mgr_grid.getSelectionModel().on('focuschange', function (model) {
            var selections = model.getSelection();
            if (selections.length > 0) {
                var id = model.getSelection()[0].get('id');
                me.getRegionsValues(id);
            }
        });

        me.callParent(arguments);
        me.add(me.alarm_mgr_grid);
        me.add(me.center);
        
        me.on('resize',function(p){
    		me.alarm_mgr_grid.setWidth(p.getWidth()*0.7);
    		me.center.setWidth(p.getWidth()*0.3);
    		me.alarm_mgr_grid.setHeight(p.getHeight());
    		me.center.setHeight(p.getHeight());
    	});
    	FHD.componentResize(me,0,0); 	

    }



});