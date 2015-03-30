/**
 * 带提示信息的列表列
 * 
 * 胡迪新
 */
Ext.define('FHD.ux.TipColumn',{
	
	extend:'Ext.grid.column.Column',
	alias:'widget.tipcolumn',
	
	requires: ['Ext.tip.ToolTip'],
	
	// @private [Ext.tip.ToolTip] tooltip 
	 
	
	// @private 初始化方法
	initComponent:function(){
		var me = this;
		
		me.initTip();
		
		me.callParent(arguments);
	},
	
	// @private 初始化提示信息
	initTip:function() {
		var me = this;
		
		// 创建提示信息的默认方法
		me.tipConfig = Ext.apply({}, me.tips, {
			// 默认渲染方法为空，当改变值时重新渲染
            renderer: Ext.emptyFn,
            // 设置背景色为白色
			bodyStyle:{
	     		background: '#FFF'
	     	},
            constrainPosition: true,
            autoHide: false
        });
		
		me.tooltip = new Ext.tip.ToolTip(me.tipConfig);

		// 添加鼠标经过事件
		me.on('mouseover', me.onMouseMove, me);
        me.on('mouseout', function() {
        	me.tooltip.hide();
        });
		
	},
	
	// @private 鼠标经过 
	onMouseMove: function(t,htmlelement,cellIndex,rowIndex,event) {
		var me = this;
		me.tooltip.targetXY = event.getXY();
		me.tooltip.show();
		me.tooltip.trackMouse = true;
		// 调用渲染方法
		me.tipConfig.renderer.call(me,rowIndex,cellIndex,me.tooltip)
	}
	
	
});