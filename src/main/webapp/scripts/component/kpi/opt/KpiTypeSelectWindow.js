Ext.define('FHD.ux.kpi.opt.KpiTypeSelectWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.kpitypeselectwindow',
    layout: 'fit',
    reload:function(){
    	var me = this;
    	me.kpitypeGrid.store.load();
    },
    save: function () {
    	var me = this;
    	var selections = me.kpitypeGrid.getSelectionModel().getSelection();
        var length = selections.length;
        if (length > 0) {
        	var selection = selections[0]; //得到选中的记录
        	me.onsubmit(selection.get('id'),selection.get('name'));
        	me.close();
        }else{
        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.kpitypeone"));
        	return ;
        }
    },

    // 初始化方法
    initComponent: function () {
        var me = this;
        me.kpitypeGrid = Ext.create('FHD.ux.EditorGridPanel', {
            pagable: false,
            checked: false,
            border: false,
            url: __ctxPath + "/kpi/kpi/findkpitypeall.f",
            height: 560,
            width: 560,
            cols: [{
                    dataIndex: 'id',
                    id: 'id',
                    width: 0
                }, {
                    header: FHD.locale.get('fhd.kpi.kpi.form.name'),
                    dataIndex: 'name',
                    sortable: true,
                    flex: 1
                }
            ]
        });
        var formButtons = [ //表单按钮
            {
                text: FHD.locale.get('fhd.common.confirm'),
                handler: function () {
                    me.save();
                }
            }, {
                text: FHD.locale.get('fhd.common.cancel'),
                handler: function () {
                	me.close();
                }
            }
        ];

        Ext.applyIf(me, {
            buttons: formButtons
        });
        me.callParent(arguments);
        me.add(me.kpitypeGrid);
    }

})