/**
 * 系统图标
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.icon.IconMan', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.iconMan',
    
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	var icongrid;
    	var iconqueryUrl = 'sys/icon/iconList.f'; //查询url

    	var icongridColums =[
    		{header: FHD.locale.get('fhd.sys.icon.preview'), dataIndex: 'css', sortable: true, width: 34,renderer:function(value,metaData,record,colIndex,store,view) { 
    			metaData.tdAttr = 'data-qtip="'+value+'"';  
    			var CSS=record.get('css');
    		    return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='"+CSS+"'></div>";  
    		}},
    		{header: FHD.locale.get('fhd.sys.icon.name'), dataIndex: 'css', sortable: true, width: 60,flex : 1},
    	 	{header: FHD.locale.get('fhd.sys.icon.filename'), dataIndex: 'fileName', sortable: true, width: 60,flex : 1},
    	 	{header: FHD.locale.get('fhd.sys.icon.path'), dataIndex: 'path', sortable: true, width: 60,flex : 1}
    	];
    	
    	Ext.apply(me, {
    		url: iconqueryUrl,//调用后台url
    		checked:false,
    		height:FHD.getCenterPanelHeight(),//高度为：获取center-panel的高度
    		cols:icongridColums//cols:为需要显示的列
        });
    	
        me.callParent(arguments);
    }
});