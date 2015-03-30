/**
 * @author 郑军祥 2013-4-24
 */
Ext.define('FHD.ux.treeselector.TreePanel',{
	extend: 'Ext.tree.Panel',
	alias: 'widget.treeselectortree',
	url:'',
	root:{},
	extraParams:{},
	checkModel:'single', // single还是cascade
	searchable:true,
	searchParamName:'query',
	myexpand:true,
	rootVisible : false,
	modelFields:null,	//treestore fields定义['text', 'id', 'code','leaf','iconCls','cls'];	//列表实体定义
	values:new Array(),
	initComponent:function(){
		var me = this;

		me.store = Ext.create('Ext.data.TreeStore', {
			fields : me.modelFields,
    	    proxy: {
    	    	url: me.url,
    	        type: 'ajax',
    	        reader: {
    	            type: 'json'
    	        },
    	        extraParams: me.extraParams
    	    },
    	    root: me.root
    	});
    	
		var toolbar = Ext.create('Ext.toolbar.Toolbar',{
			border:true
		});
		Ext.applyIf(me,{
			dockedItems:[toolbar]
		});
		me.callParent(arguments);
		
		//级联或单独设置子节点的状态
		me.addListener('checkchange',function(node,checked,eopts){
			if('cascade' == me.checkModel) {
	    		node.expand(false,function(){
	    			node.eachChild(function(child) {    
	                    child.set("checked",checked);   
	                    me.fireEvent('checkchange', child, checked, eopts);    
	                });  
	    		});  
			}else if('single' == me.checkModel) {
				var nodes = me.getChecked();
				Ext.each(nodes,function(n){
					n.set("checked",false); 
				});
				node.set("checked",checked); 
			}
			//留给子接口实现，处理额外动作
    		me.check(me,node,checked);
    	});
		
		//展开选中节点，设置子节点的选中状态
		me.addListener('afteritemexpand',function(node,checked,eopts){
    		var childNodes=node.childNodes;
			Ext.Array.each(childNodes,function(childNode){
				if(Ext.Array.contains(me.values,childNode.data.dbid)){
					childNode.set("checked",true); 
				}
			});
    	});
		
		//展开折叠按钮
    	var expandAndcollapseButton =Ext.create('Ext.Button',
				{
			listeners:{
				beforerender:function(thiz){
					if(me.myexpand){
						thiz.setIconCls('icon-expand-all');
					}else{
						thiz.setIconCls('icon-collapse-all');
					}
				}
			},
			handler:function(){
				if(me.myexpand){
					me.myexpand=false;
					expandAndcollapseButton.setIconCls('icon-collapse-all');
					me.getRootNode().collapseChildren();
				}else{
					me.myexpand=true;
					expandAndcollapseButton.setIconCls('icon-expand-all');
					me.expandAll();
				}
				  
			}
		});
		
    	//刷新按钮
		var refreshButton =Ext.create('Ext.Button',
				{
			iconCls:'icon-arrow-refresh-blue',
			handler:function(){
				  me.getStore().load();
			}
		});
		
		//搜索框
		var searchField = Ext.create('Ext.ux.form.SearchField', {
				width : 150,
				paramName:me.searchParamName,
				store:me.store,
				emptyText : FHD.locale.get('searchField.emptyText')
		});
		
		if(me.searchable){
			toolbar.add(searchField);
		}
		toolbar.add(expandAndcollapseButton);
		toolbar.add(refreshButton);
	}
});