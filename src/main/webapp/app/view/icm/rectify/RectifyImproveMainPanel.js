Ext.define('FHD.view.icm.rectify.RectifyImproveMainPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.rectifyimprovemainpanel',
    
    activeItem: 0,
    paramObj:{
    	editflag:false,
    	improveId:''
    },
    border: false,
    requires: [
       'FHD.view.icm.rectify.RectifyImproveList',
       'FHD.view.icm.rectify.RectifyImproveContainer'
    ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;

        me.rectifyimprovelist = Ext.widget('rectifyimprovelist');
        me.rectifyimprovecontainer = Ext.widget('rectifyimprovecontainer');
        
        Ext.apply(me, {
            items: [
                me.rectifyimprovelist,
                me.rectifyimprovecontainer
            ]
        });

        me.callParent(arguments);
    },
    //cardpanel切换
    navBtnHandler: function (index) {
    	var me = this;
        me.setActiveItem(index);
        if(0 == index){
        	//重新加载列表
        	me.rectifyimprovelist.reloadData();
        }else if(1 == index){
        	//重新加载表单
         	me.rectifyimprovecontainer.loadData(me.paramObj.improveId,null);
         	me.rectifyimprovecontainer.reloadData();
        }
    },
    initParam:function(paramObj){
	   	var me = this;
	   	me.paramObj = paramObj;
    },
    reloadData:function(){
    	var me=this;
    	//重新加载列表
    	me.rectifyimprovelist.reloadData();
    	//重新加载表单
     	me.rectifyimprovecontainer.reloadData();
    }
});