/**
 * 系统菜单主面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.icm.statics.IcmMyDatasTreePanel', {
    extend: 'FHD.ux.org.DeptTree',
    alias: 'widget.icmmydatastreepanel',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	

    	Ext.apply(me, {
    		checkable:false,
    		subCompany:true,
    		rootVisible: true,
    		width:260,
    		split: true,
           	collapsible : true,
           	border:true,
           	region: 'west',
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
           	listeners : {
	   			'itemclick' : function(view,re){
	   				if(re.data&&re.data.id){
	   					me.up('icmmydatas').orgId=re.data.id;
	   					me.up('icmmydatas').reloadData();
	   					me.up('icmmydatas').searchorgid = re.data.id
	   				}
	   			}
           	}
        });
    	
        me.callParent(arguments);
    }
});