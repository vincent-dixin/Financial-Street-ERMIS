Ext.define('FHD.view.bpm.processDefinitionDeploy.ProcessImpShow', {
    extend: 'Ext.panel.Panel',
	alias: 'widget.ProcessImpShow',
	
	activeTab: 0,
	width:'auto',
	processDefinitionDeployId:null,
	autoScroll:true,
    initComponent: function() {
        var me = this;
        var items=new Array();
        var img=Ext.create('Ext.Img',{
        	src:__ctxPath +"/jbpm/processImpShow.do?processDefinitionDeployId="+me.processDefinitionDeployId
        });
        items.push(img);
        Ext.applyIf(me, {
            items: items
        });
        me.callParent(arguments);
    }
});