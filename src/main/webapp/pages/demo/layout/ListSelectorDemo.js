Ext.define('FHD.demo.layout.ListSelectorDemo', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        me.region = Ext.create('FHD.ux.listselector.ListSelector',{
           	title:'选择员工',
           	noteField:'userid',
    		columns: [{dataIndex: 'userid',header: '员工编号'},{dataIndex: 'realname',header: '姓名',isPrimaryName:true}],
    		entityName:'com.fhd.sys.entity.orgstructure.SysEmployee',
    		foreignKey:'sysOrganization.id',
    		queryKey:'realname',
           	fieldLabel : '员工',
           	labelAlign: 'left',
            multiSelect : true,
            value:'[{id:"chenjie"},{id:"hanwei"}]',
            name: 'emp'
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