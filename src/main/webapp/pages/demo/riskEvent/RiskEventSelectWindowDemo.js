Ext.define('FHD.demo.riskEvent.RiskEventSelectWindowDemo', {
    extend: 'Ext.form.Panel',
    alias: 'widget.riskeventselectwindow',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        me.button = Ext.create('Ext.Button', {
            text: '选择事件弹窗',
            renderTo: Ext.getBody(),
            handler: function() {
            	var win = Ext.create('FHD.ux.riskEvent.RiskEventSelectorWindow',{
    				multiSelect:true,
    				modal: true,
    				onSubmit:function(win){
    					var values = new Array();
    					var store = win.selectedgrid.store;
    					store.each(function(r){
    			    		values.push(r.data.id);
    			    	});
    					alert(values.length);
    					alert(values.join(','));
    				}
    			}).show();
            }
        });

        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                defaults: {
                	margin: '7 30 3 30',
                	columnWidth:.5
                },
                layout: {
                    type: 'column'
                },
                title: "风险事件选择",
                items:[me.button]
            }]
            
        });
        
        me.callParent(arguments);
      
    }
});