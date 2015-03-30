

Ext.define('pages.icm.defect.defectTree1', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.depttreepanel',
   
    title:'缺陷', 
    
    subCompany:false, // 是否显示子公司
    canChecked:true,	// 是否有复选框
    chooseId:'',
    checked: false,
    check:function(){},
    myexpand:false,
    initComponent: function() {
        var me = this;
        if(__user.companyId == 'null') {
        	__user.companyId = 'root';
        	__user.companyName = $locale('depttreepanel.__user.companyName');
        }
        
        Ext.apply(me, {
        	autoScroll:true,
        	
        	url: __ctxPath + '/defect/defectTree/findrootDefectTreeLoader.f',
    	    extraParams: {
	        	canChecked: me.canChecked,
	        	subCompany: me.subCompany,
	        	chooseId:me.chooseId
	        },
    	    root: {
    	        text: __user.companyName,
    	        id: __user.companyId,
    	        expanded: true
    	    }
        });
        me.callParent(arguments);
    }
    
    

});