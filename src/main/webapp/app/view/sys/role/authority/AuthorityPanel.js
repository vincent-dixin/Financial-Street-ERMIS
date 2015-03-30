/**
 * 为角色添加人员,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.authority.AuthorityPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.authorityPanel',
    requires: [
               'FHD.view.sys.role.authority.AuthorityTree',
               'FHD.view.sys.role.authority.AuthorityShowTree'
    ],
    initComponent: function () {
        var me = this;
        me.authorityTree = Ext.widget('authorityTree');  
        me.authorityShowTree = Ext.widget('authorityShowTree');  
        me.id = 'authorityPanel';
        Ext.applyIf(me, {
        	border:false,
        	title:"角色授权",
            layout: {
                type: 'table',
                columns: 2
            },
            defaults: {width:200, height: FHD.getCenterPanelHeight() - 53},
            //items:[],
           items:[me.authorityTree,me.authorityShowTree]
           // items:[me.authorityTree]
        });
        me.callParent(arguments);
    }
});