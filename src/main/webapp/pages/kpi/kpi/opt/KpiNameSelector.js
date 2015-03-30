Ext.define('Ext.kpi.kpi.opt.KpiNameSelector', {
    extend: 'Ext.form.FieldContainer',
    alias : 'widget.kpinameselector',

    labelWidth: 105,
    
    labelAlign:'left',
    
    defaultValue:'0yn_y',
    
    height:75,

    /**
     * field字段名称
     */
    fieldLabel: '',

    /**
     * 名称控件name属性
     */
    textfieldname: '',
    /**
     * 是否使用默认名称数据字典name属性
     */
    is_default: '',

    initComponent: function () {
        var me = this;
        
        me.textfield = Ext.create('Ext.form.field.TextArea',{
        	id:'kpiname',
        	rows: 3,
            labelAlign: me.labelAlign,
            name: me.textfieldname,
            maxLength: 255,
            allowBlank: false
            ,disabled: true
        });
        
        me.isDefaultDictRadio = Ext.create('FHD.ux.dict.DictRadio',{
        	labelWidth: 105,
            name: me.is_default,
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: '',
            defaultValue: me.defaultValue,
            labelAlign: me.labelAlign,
            listeners: {
            	
            	change: function(t, newValue, oldValue, options ){
		  			if(!newValue[me.is_default]){
		  				me.textfield.setDisabled(true);
		  			}else if(newValue[me.is_default]=="0yn_y"){
		  				me.textfield.setDisabled(true);
		  			}else if(newValue[me.is_default]=="0yn_n"){
		  				me.textfield.setDisabled(false);
		  			}
		  		}
            }
        	
        });
        
        Ext.applyIf(me, {

            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            labelWidth: me.labelWidth,
            fieldLabel: me.fieldLabel,
            height:me.height,
            items: [me.isDefaultDictRadio,me.textfield]


        });
        me.callParent(arguments);
    }


});