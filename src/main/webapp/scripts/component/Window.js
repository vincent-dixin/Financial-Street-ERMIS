/**
 * 卡片布局的面板
 * 允许定义多个组件，但每次只显示一个组件
 * 
 * @author 胡迪新
 */
Ext.define('FHD.ux.Window',{
	extend: 'Ext.window.Window',
	alias: 'widget.fhdwindow',
	
	
	
	layout : 'fit',
	collapsible : true,
	modal : true,
	draggable : false,
	
	initComponent : function(){
		var me = this;
	
		Ext.applyIf(me,{
			height: Ext.getBody().getHeight() * 0.8,
			width : Ext.getBody().getWidth() * 0.8
		}),
        me.callParent(arguments);
	}
	
});