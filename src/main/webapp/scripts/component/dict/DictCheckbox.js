/**
 * @author 胡迪新
 * @class FHD.ux.dict.DictRadio
 * @extends Ext.form.RadioGroup
 * 
 * 数据字典单选控件
 * 
 */
Ext.define('FHD.ux.dict.DictCheckbox', {
	
	    extend: 'Ext.form.CheckboxGroup',
	    alias: 'widget.dictcheckbox',
	
	    width: 400,
	    autoScroll: false,
	    fieldLabel:  FHD.locale.get('dictcheckbox.fieldLabel'),
		labelAlign : 'right',
		columns:3,
		blankText: '该输入项为必输项',
	    initComponent: function() {
	        var me = this;
	        var items = new Array();
	        
	        // 同步查询数据字典
	        Ext.Ajax.request({
			    url: __ctxPath + '/sys/dic/findDictEntryByTypeId.f?typeId=' + me.dictTypeId,
			    async :  false, // 是否同步
			    success: function(response){
			        var text = response.responseText;
			        Ext.each(Ext.JSON.decode(text).dictEnties,function(r,i){
		        		items.push({id:me.name+'_'+r.id,xtype:'checkbox',boxLabel:r.name,inputValue:r.id,name:me.name});
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
	    setValue:function(v)
	    { 
		var arr=v.split(","); 
		this.items.each(function(item,idx){
		    item.setValue(false);
		    for(i=0;i<arr.length;i++)
			{
			if(item.inputValue==arr[i])
			{
			item.setValue(true);
			}
			
			}
		    
	                
	        });
		
	    }
	});

