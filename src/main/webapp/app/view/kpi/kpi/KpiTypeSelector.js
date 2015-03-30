Ext.define('FHD.view.kpi.kpi.KpiTypeSelector', {
    extend: 'Ext.form.Panel',
    alias: 'widget.kpitypeselector',
    reload:function(){
    	var me = this;
    	me.kpitypeGrid.store.load();
    },
    save: function () {
    	var me = this;
    	var selections = me.kpitypeGrid.getSelectionModel().getSelection();
        var length = selections.length;
        if (length > 0) {
        	debugger;
        	var selection = selections[0]; //得到选中的记录
            var kpibasicform = Ext.getCmp('kpibasicform');
            kpibasicform.paramObj.selecttypeflag = true;
            kpibasicform.paramObj.kpitypeid = selection.get('id');
            kpibasicform.paramObj.kpitypename = selection.get('name');
//            kpibasicform.formwindow.close();
            kpibasicform.typeWindowClose();
            kpibasicform.formLoad();
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
                    Ext.getCmp('kpibasicform').typeWindowClose();
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