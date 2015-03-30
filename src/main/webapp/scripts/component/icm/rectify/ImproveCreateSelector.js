Ext.define('FHD.ux.icm.rectify.ImproveCreateSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.improvecreateselector',
	
	layout: {
        type: 'column'
    },
	autoWidth: true,
	allowBlank: true,
	labelWidth:95,
	height:22,
	
	initComponent : function() {
		var me = this;
		
    	me.field=Ext.widget('textfield',{
	        name:me.name,
	        columnWidth: 1,
	        value: me.value,
	        allowBlank:me.allowBlank
	        /*,
	        listeners:{
				change:function (field,newValue,oldValue,eOpts ){
					me.initValue(newValue);
				}
		    }
		    */
        });
        me.label=Ext.widget('label',{
    		width: me.labelWidth,
    		html: '<span style="float:'+me.labelAlign+'">'+me.fieldLabel + ':</span>',
    		height: 22,
    		style: {
    			marginRight: '10px'
    		}
    	});
    	
        me.button=Ext.widget('button',{
            iconCls:'icon-magnifier',
            height: 22,
            width: 22,
            border:false,
            handler:function(){
	    		me.window = Ext.create('FHD.ux.Window',{
					title:'整改计划制定',
					collapsible:false,
					maximizable:true,
					items:Ext.create('FHD.ux.icm.rectify.ImproveSelectorPanel',{
						onSubmit:function(paramObj){
							me.hiddenValue = paramObj.businessId;
							me.field.setValue(paramObj.name);
							me.window.close();
						}
					})
	    		}).show();
		    }
    	});
    	
        Ext.applyIf(me, {
            items: [
            	me.label,
                me.field,
                me.button
            ]
        });
        
        me.callParent(arguments);
        
        me.initValue(me.value);
	},
	initValue: function(value){
		var me=this;
		
		if(value){
			var arr=value.split(",");
			me.hiddenValue=arr[0];
			me.field.setValue(arr[1]);
		}else{
			me.clearValues();
		}
	},
    /**
     * 获得当前值
     * @return {当前值}
     */
    getValue:function(){
    	var me = this;
    	
    	return me.field.getValue();
    },
    getHiddenValue:function(){
    	var me=this;
    	
    	return me.hiddenValue;
    },
    /**
     * 清空值
     */
    clearValues : function(){
        var me = this;
        
        me.hiddenValue = '';
        me.field.setValue(null);
    }
});