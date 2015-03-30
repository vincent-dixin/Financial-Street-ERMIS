/**
 * @author 胡迪新

 * @class FHD.ux.dict.DictRadio
 * @extends Ext.form.RadioGroup
 * 
 * 数据字典单选控件
 * 
 */


Ext.define('FHD.ux.dict.DictRadio', {
	    extend: 'Ext.form.RadioGroup',
	    alias: 'widget.dictradio',
	
	    width: 400,
	    autoScroll: false,
	    name:'_dictRadio',
	    fieldLabel: $locale('dictcheckbox.fieldLabel'),
		labelAlign : 'right',
		columns:3,
		
	    initComponent: function() {
	        var me = this;
	        var items = new Array();
	        Ext.Ajax.request({
			    url: __ctxPath + '/sys/dic/findDictEntryByTypeId.f?typeId=' + me.dictTypeId,
			    async :  false,
			    success: function(response){
			        var text = response.responseText;
			        
			        Ext.each(Ext.JSON.decode(text).dictEnties,function(r,i){
			            
		        		items.push({xtype:'radiofield',boxLabel:r.name,inputValue:r.id,name:me.name});
			        });
			    }
			});
			
			me.callParent(arguments);
			me.add(items);
			
			// 设置默认选中
			if(!me.defaultValue){
				me.items.items[0].setValue(true)
			}else {
				me.items.each(function(item, idx) {
					item.setValue(false);
					if (item.inputValue == me.defaultValue) {
						item.setValue(true);
					}
				});
			}
	    },
	    setValue:function(v) { 
	    	var me = this;
			me.items.each(function(item, idx) {
				item.setValue(false);
				if (item.inputValue == v) {
					item.setValue(true);
				}

			});
		
	    }
	});
