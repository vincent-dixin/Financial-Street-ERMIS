Ext.define('FHD.ux.timestamp.TimestampSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.timestampSelector',

    buttonHeight : 22,//按钮高度
	height: 22,//标示宽度
	labelText: FHD.locale.get('fhd.timestampSelector.labelText'),//标示名称
	labelAlign:'left',//标示对齐方式
	labelWidth:50,//标示宽度
	field:null,//文本框
	selectorWindow:null,//弹出窗口：默认未开启
	urlParam : '',//结果参数
	fun : null,//方法
	
	//初始化方法
    initComponent: function() {
        var me = this;
        
		me.field = Ext.widget('textfield',{
			hidden:true,
			//columnWidth: 0.9,
	        name:me.name,
	        value:me.value
        });
		
        Ext.applyIf(me, {
            items: [
            	{
            		xtype:'label',
            		width:me.labelWidth,
            		text:me.labelText + ':',
            		height: me.height,
            		style:{
            			marginTop: '3px',
            			marginRight: '5px',
            			textAlign: me.labelAlign
            		}
            	},
            	me.field,
            	{
                    xtype: 'button',
                    width : 22,
                    text : FHD.locale.get('fhd.collectionSelector.choose'),
                    iconCls:'icon-selectorButton',
                    //columnWidth: 0.1,
                    handler:function(){
						me.selectorWindow= Ext.create('FHD.ux.timestamp.TimestampWindow',{
							onSubmit:function(values){
								me.urlParam = values;
								Ext.callback(me.fun, this, [me.urlParam]);
							}
						}).show();
					}
                }
            ]
        });
		me.callParent(arguments);
    }
});