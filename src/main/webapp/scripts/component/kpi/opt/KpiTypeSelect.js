Ext.define('FHD.ux.kpi.opt.KpiTypeSelect', {
    extend: 'Ext.container.Container',
    alias: 'widget.kpitypeselect',
    requires: [
               'FHD.ux.kpi.opt.KpiTypeSelectWindow'
              ],
    layout: {
        type: 'column'
    },

    /**
     * 标示名称
     */
    labelText: '指标类型',

    /**
     * 标示对齐方式
     */
    labelAlign: 'left',

    /**
     * 标示宽度
     */
    labelWidth: 50,

    /**
     * 指标类型文本框
     */
    textfield: null,


    /**
     * label高度
     */
    labelHeight: 22,

    /**
     * 按钮的高度
     */
    btnHeight: 30,
    
    /**
     * 按钮的宽度
     */
    btnWidth:30,
    
    /**
     * field 值
     */
    fieldValue:'',
    
    getName:function(){
    	var me = this;
    	return me.textfield.getValue();
    },
    
    getValue:function(){
    	var me = this;
    	return me.field.getValue();
    },
    

    initComponent: function () {
        var me = this;
        
        me.field = Ext.create('Ext.form.field.Hidden',{
        	name:me.name,
        	value:me.fieldValue
        });

        me.label = Ext.create('Ext.form.Label', {
            xtype: 'label',
            width: me.labelWidth,
            text: me.labelText + ':',
            height: me.labelHeight,
            style: {
                marginTop: '3px',
                marginRight: '5px',
                textAlign: me.labelAlign
            }

        });
        
        me.textfield = Ext.create('Ext.form.field.Text',{
        	width: me.textWidth,
        });


        Ext.applyIf(me, {

            items: [
            me.label,
            me.textfield, {
                xtype: 'button',
                width:me.btnWidth,
                height: me.btnHeight,
                //iconCls: Ext.baseCSSPrefix + 'form-search-trigger',
                iconCls:'icon-magnifier',
                handler: function () {
                	me.kpiTypeSelectWindow = Ext.create('FHD.ux.kpi.opt.KpiTypeSelectWindow',{
                		onsubmit:function(id,name){
                			me.onsubmit(id,name);
                			me.textfield.setValue(name);
                			me.field.setValue(id);
                		}
                	});
                	me.kpiTypeSelectWindow.show();
                    
                }
            }]

        });


        me.callParent(arguments);
    }

});