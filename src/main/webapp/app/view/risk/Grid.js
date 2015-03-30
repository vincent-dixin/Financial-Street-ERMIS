Ext.define('FHD.view.risk.Grid', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.mainpanel',
   
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        //alert(4);
        var url = 'app/view/risk/grid.json';
        var store = Ext.create('Ext.data.Store', {
        	//autoLoad:false,
		    fields:['name', 'email', 'phone'],
		    proxy: {
		        type: 'ajax',
		        url:url,
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
		});
		
		var grid = Ext.create('Ext.grid.Panel', {
		    title: 'SimpsonsMainPanel',
		    store: store,//Ext.data.StoreManager.lookup('simpsonsStore'),
		    columns: [
		        { header: 'Name',  dataIndex: 'name' },
		        { header: 'Email', dataIndex: 'email', flex: 1 },
		        { header: 'Phone', dataIndex: 'phone' }
		    ]
		});
		
        Ext.apply(me, {
            autoScroll:false,
		    items:[grid]
        });
        
        me.callParent(arguments);  
        
        //return Ext.create("pages.demo.Grid",{});
    }
});