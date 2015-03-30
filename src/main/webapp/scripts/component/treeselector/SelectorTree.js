Ext.define('FHD.ux.treeselector.SelectorTree', {
    extend: 'FHD.ux.treeselector.TreePanel',	//FHD.ux.TreePanel
    alias: 'widget.selectortreepanel',
   
    title: '', 
    url:'',
    entityName:'',	//后台反射使用
    queryKey:'name',
	parentKey:'parent.id',
	relationKey:'idSeq',
    checkable:false,	// 是否有复选框
    chooseId:'',
    checked: false,
    myexpand:false,
    parameters:'',	//树查询传递的参数 {orgStatus:"1"}
    initComponent: function() {
        var me = this;

		var extraParams = {};
		
		//构造参数
		Ext.apply(extraParams,{
			parameters:me.parameters,
			entityName:me.entityName,
	    	queryKey:me.queryKey,
    		parentKey:me.parentKey,
    		relationKey:me.relationKey,
        	checkable: me.checkable,
        	chooseId:me.chooseId
		});
        Ext.apply(me, {
        	autoScroll:true,
        	url: __ctxPath + me.url,// '/demo/findFristtreeTree.f'
    	    extraParams:extraParams,
        	root: {
    	        text: "first部门树",
    	        id: "root",
    	        expanded: true
    	    }
        });
        me.callParent(arguments);
    }
});