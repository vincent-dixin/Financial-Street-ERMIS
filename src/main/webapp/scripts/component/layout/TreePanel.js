
Ext.define('FHD.ux.layout.TreePanel',{
	extend: 'Ext.tree.Panel',
	alias: 'widget.fhdtreepanel',
	url:'',
	root:{},
	extraParams:{},
	checkModel:'single', // single还是ascade
	searchable:true,
	searchParamName:'query',
	myexpand:true,
	rootVisible : false,
	values:new Array(), //保存所有选中节点dbid
	//获取整棵树被选中的节点、设置根据参数checked，节点dbid=id的节点的状态
	setNodeChecked:function(id,checked){
		var me=this;
    	var nodes = me.getChecked();
		Ext.Array.remove(me.values,id);
    	Ext.Array.each(nodes,function(node){
    		if(node.data.dbid==id){
				node.set("checked",checked); 
    		}
		})
    },
    canSelect:'',//设置可以选择的节点类型
	initComponent:function(){
		var me = this;
		me.store = Ext.create('Ext.data.TreeStore', {
			fields : ['text', 'id', 'dbid','code','leaf','iconCls','cls','type','idSeq'],
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
			dockedItems:[toolbar],
			canSelect:me.canSelect,
			//遍历整棵树，判断设置所有节点的状态
			setChecked:function(node,checked){
				function recursion(node,checked,str){
					var childNodes=node.childNodes;
					Ext.Array.each(childNodes,function(childNode){
						if(Ext.Array.contains(str,childNode.data.dbid)){
							if(childNode.data.type==me.canSelect){
								childNode.set("checked",true);
							}
						}else{
							if(childNode.data.type==me.canSelect){
								childNode.set("checked",false);
							}
						}
						if(!childNode.isLeaf()){
							recursion(childNode,checked,str);
						}
					});
				}
				recursion(me.getRootNode(),checked,me.values);
			}
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
    	var expandAndcollapseButton =Ext.create('Ext.Button',
				{
			//iconCls:'icon-collapse-all',
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
		
		var refreshButton =Ext.create('Ext.Button',
				{
			iconCls:'icon-arrow-refresh-blue',
			handler:function(){
				  me.getStore().load();
			}
		});
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

		//添加全部按钮
		var allButton = Ext.create("Ext.form.field.Checkbox", {
			boxLabel : '全部',
			name : 'all',
			inputValue : '2',
			checked : false,
		});
		toolbar.add(allButton);
	}
});