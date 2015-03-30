Ext.define('FHD.view.comm.theme.ThemeBasicinfoPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.themebasicinfopanel',

    requires: [
        'FHD.view.comm.theme.ThemeBasicForm'
    ],

    activeItem: 0,
    
 
    //保存按钮
    tbar: {
        id: 'themebasicinfopanel_tbar',
        items: [
                '->',	//按钮居右
            {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'analysis_save_tbtn',
            iconCls: 'icon-save', 
            handler: function () {
            	Ext.getCmp('themebasicform').saveAnaly();
            }
        }]
    },
    //保存按钮
    bbar: {
        id: 'themebasicinfopanel_bbar',
        items: [
                '->',	//按钮居右
            {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'analysis_save_bbtn',
            iconCls: 'icon-save', 
            handler: function () {
            	Ext.getCmp('themebasicform').saveAnaly();
            }
        }]
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.themebasicform = Ext.widget('themebasicform',{id : 'themebasicform'});

        Ext.apply(me, {
            items: [
                    me.themebasicform
                   ]
        });

        me.callParent(arguments);
        
    },
    
    /**
     * 清除数据
     */
    clearDate:function(){
    	//清除form数据
    	Ext.getCmp('themebasicform').clearFormData();
    	//清除grid数据
    	Ext.getCmp('themewarningset').reLoadData();
    }
    
   

});