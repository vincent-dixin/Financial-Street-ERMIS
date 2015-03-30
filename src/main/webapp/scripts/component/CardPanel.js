/**
 * 卡片布局的面板
 * 允许定义多个组件，但每次只显示一个组件
 * 
 * @author 胡迪新
 */
Ext.define('FHD.ux.CardPanel',{
	extend: 'Ext.panel.Panel',
	alias: 'widget.cardpanel',
	
	layout:{
		type:'card',
		deferredRender:true
	},
	
	/**
	 * 移动面板
	 * 参数 dir prev：上一页，next：下一页
	 */
	pageMove:function(dir){
		var me = this;
		var layout = me.getLayout();
		layout[dir]();
	},
	/**
	 * 返回当前活动的子组件的上一子组件
	 */
	getPrev:function(){
		var me = this;
		return me.getLayout().getPrev();
	},
	/**
	 * 返回当前活动的子组件的下一子组件
	 */
	getNext:function(){
		var me = this;
		return me.getLayout().getNext();
	},
	/**
	 * 将当前活动的子组件的上一个子组件设置为活动（可见）状态
	 */
	prev:function(){
		var me = this;
		me.getLayout().prev();
	},
	/**
	 * 将当前活动的子组件的下一个子组件设置为活动（可见）状态
	 */
	next:function(){
		var me = this;
		me.getLayout().next();
	},
	/**
	 * 返回布局中当前活动的子组件
	 */
	getActiveItem:function(){
		var me = this;
		return me.getLayout().getActiveItem();
	},
	/**
	 * 设置当前活动的子组件
	 */
	setActiveItem:function(p){
		var me = this;
		return me.getLayout().setActiveItem(p);
	}
	
	
	
});