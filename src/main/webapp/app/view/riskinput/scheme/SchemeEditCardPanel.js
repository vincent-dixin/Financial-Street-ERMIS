Ext.define('FHD.view.riskinput.scheme.SchemeEditCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.schemeeditcardpanel',
    border:true,
    activeItem: 0,
    paramObj:{
    	editflag:false,
    	businessId:''
    },
    
    requires: [
       'FHD.view.riskinput.scheme.SchemeList',
       'FHD.view.riskinput.scheme.SchemeEditPanel'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
    // 初始化方法
    initComponent: function() {
        var me = this;
        //评价计划列表
        me.schemelist = Ext.widget('schemelist');
        //评价计划第一步container
        me.schemeeditpanel = Ext.widget('schemeeditpanel');
        
        Ext.apply(me, {
            items: [
                me.schemelist,
                me.schemeeditpanel
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
    	me.schemelist.reloadData();
    	//重新加载mainpanel
     	me.schemeeditpanel.initParam(me.paramObj);
     	me.schemeeditpanel.reloadData();
    }
});