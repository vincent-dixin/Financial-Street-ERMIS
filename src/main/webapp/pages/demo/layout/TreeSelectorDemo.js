Ext.define('FHD.demo.layout.TreeSelectorDemo', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        me.region = Ext.create('FHD.ux.treeselector.TreeSelector',{
        	title:'请您选择部门',
        	noteField:'orgcode',
    		columns: [{dataIndex: 'orgcode',header: '部门编号'},{dataIndex: 'orgname',header: '部门名称',isPrimaryName:true}],
    		entityName:'com.fhd.sys.entity.orgstructure.SysOrganization',
    		parentKey:'parentOrg.id',
    		relationKey:'orgseq',
    		value:'[{id:"XD00"},{id:"eda8ffeab0da4159be0ff924108e3883"}]',
        	fieldLabel : '部门',
        	labelAlign: 'left',
            multiSelect : false,
            checkable:true,
            //parameters:'{orgStatus:"1"}',
            name: 'deptFirst'
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
                title: "基础信息",
                items:[me.region]
            }]
            
        });
        
        me.callParent(arguments);
      
    }
});