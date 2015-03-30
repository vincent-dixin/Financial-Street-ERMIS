Ext.define('FHD.view.riskinput.EventEditCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.eventeditcardpanel',
    
    activeItem: 0,
    paramObj:{
    	editflag:false,
    	businessId:''
    },
    
    requires: [
       'FHD.view.riskinput.EventList',
       'FHD.view.riskinput.EventEditPanel'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
    // 初始化方法
    initComponent: function() {
        var me = this;
        //评价计划列表
        me.eventlist = Ext.widget('eventlist');
        //评价计划第一步container
        me.eventeditpanel = Ext.widget('eventeditpanel');
        
        Ext.apply(me, {
            items: [
                me.eventlist,
                me.eventeditpanel
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
    	me.eventlist.reloadData();
    	//重新加载mainpanel
     	me.eventeditpanel.initParam(me.paramObj);
     	me.eventeditpanel.reloadData();
    }
});