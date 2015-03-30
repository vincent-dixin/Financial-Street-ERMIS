Ext.define('FHD.view.icm.assess.baseset.AssessGuidelinesMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.assessguidelinesmainpanel',
    requires: [
       'FHD.view.icm.assess.baseset.AssessGuidelinesEditGrid',
       'FHD.view.icm.assess.baseset.AssessGuidelinesPropertyEditGrid'
    ],
    // 初始化方法
    initComponent: function() {
        var me = this;

        me.assessguidelineseditgrid = Ext.widget('assessguidelineseditgrid');
        
        //me.assessguidelinespropertyeditgrid = Ext.widget('assessguidelinespropertyeditgrid');
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
            items: [
                me.assessguidelineseditgrid
               // me.assessguidelinespropertyeditgrid
            ]
        });
        
        me.callParent(arguments);
    },
    reloadData:function(){
    }
});