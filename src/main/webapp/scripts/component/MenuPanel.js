/**
 * @author 胡迪新
 */
Ext.define('FHD.ux.MenuPanel',{
	extend: 'Ext.panel.Panel',
	alias: 'widget.menupanel',
	
	
    defaultType: 'button',
	layout:{
		type:'vbox',
        align:'stretch'
	},
	defaults:{
		cls:'menu-btn',
		style:'border-bottom: 1px  #99bce8 solid !important;padding-bottom: 5px !important;padding-top: 5px !important;'
	},
	
	initComponent: function() {
        var me = this;
        
        Ext.each(me.items,function(item){
        	item.listeners = {
        		click:function(btn){
        			Ext.each(me.items.items,function(b){
        				if(btn.id == b.id) {
        					btn.addCls('menu-selected-btn');
        				}else{
        					b.removeCls('menu-selected-btn');
        				}
					});
        		}
        	};
        	//btn.addListener('click',me.onBtnClick);
		});
        
        
        Ext.apply(me,{
        	
        })
        
        
        me.callParent(arguments);	
        
        
        
	},
	
	onBtnClick:function(btn){
		
	}
	
});