Ext.define('FHD.view.riskinput.RiskEventEditCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.riskeventeditcardpanel',
    
    activeItem: 0,
    paramObj:{
    	editflag:false,
    	businessId:''
    },
    
    requires: [
       'FHD.view.riskinput.RiskEventList',
       'FHD.view.riskinput.RiskEventEditPanel'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
    // 初始化方法
    initComponent: function() {
        var me = this;
        //评价计划列表
        me.riskeventlist = Ext.widget('riskeventlist');
        //评价计划第一步container
        me.riskeventeditpanel = Ext.widget('riskeventeditpanel');
        
        Ext.apply(me, {
            items: [
                me.riskeventlist,
                me.riskeventeditpanel
            ]
        });

        me.callParent(arguments);
    },
    //cardpanel切换
    navBtnHandler: function (index) {
    	var me = this;
        me.setActiveItem(index);
    },
    reloadData:function(){
    	var me=this;
    	//重新加载报告列表
    	me.riskeventlist.reloadData();
    	//重新加载mainpanel
     	me.riskeventeditpanel.initParam(me.paramObj);
     	me.riskeventeditpanel.reloadData();
    }
});