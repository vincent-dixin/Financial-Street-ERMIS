

Ext.define('FHD.ux.org.DeptTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.depttreepanel',
   
    //title: $locale('depttreepanel.title'), //zjx修改，去掉title
    
    subCompany:false, // 是否显示子公司
    companyOnly:false,
    checkable:true,	// 是否有复选框
    chooseId:'',
    checked: false,
    myexpand:true,	//设置树是否展开  郑军祥使用
    rootVisible:false,
    initComponent: function() {
        var me = this;
        if(__user.companyId == 'null') {
        	__user.companyId = 'root';
        	__user.companyName = $locale('depttreepanel.__user.companyName');
        }
        if(me.checkable){
            Ext.apply(me, {
            	autoScroll:true,
            	url: __ctxPath + '/sys/org/cmp/depttreeloader.f',
        	    extraParams: {
    	        	checkable: me.checkable,
    	        	subCompany: me.subCompany,
    	        	companyOnly: me.companyOnly,
    	        	chooseId:me.chooseId
    	        },
        	    root: {
        	        text: __user.companyName,
        	        id: __user.companyId,
        	        checked: me.checked,
        	        expanded: me.myexpand
        	    }
            });
        }else{	//不需要复选框时，根节点也不需要复选框。（郑军祥修改 2013.5.28）
            Ext.apply(me, {
            	autoScroll:true,
            	url: __ctxPath + '/sys/org/cmp/depttreeloader.f',
        	    extraParams: {
    	        	checkable: me.checkable,
    	        	subCompany: me.subCompany,
    	        	companyOnly: me.companyOnly,
    	        	chooseId:me.chooseId
    	        },
        	    root: {
        	        text: __user.companyName,
        	        id: __user.companyId,
        	        expanded: me.myexpand
        	    }
            });
        }

        me.callParent(arguments);
    }
    
    

});