Ext.define('FHD.view.component.TreePanel',{
	extend: 'Ext.tree.Panel',
	alias: 'widget.fhdtreepanel',
	
	/**
	 * public
	 * 接口属性
	 */
	url:'',	//tree请求地址
	extraParams:{},
	mysearchable:true,
	mysearchParamName:'query',
	mytype:'remote',
	myfields:['id','text'],	//treeStore的字段
	
	/**
	 * public
	 * 接口方法
	 */
	
	/**
	 * private
	 * 自定义的属性和方法
	 */
	
	/**
	 * public
	 * ext属性
	 */
	root: {},
	autoScroll:true,
	rootVisible : false,
	useArrows: true,
    rowLines:false,
    singleExpand: false,
	initComponent:function(){
		var me = this;
        
		var proxy;
		if(me.mytype=='demo'){
			proxy = Ext.create("Ext.ux.data.ServerToClientProxy",{
		    	    	url: __ctxPath + '/app/view/component/tree.json',//随便的一个url,
		    	        type: 'ajax',
		    	        reader: {
		    	            type: 'json'
		    	        },
		    	        mycallback:me.url,
		    	        myparent:me,
		    	        extraParams: me.extraParams
		    	    });
		}else{
			proxy = {
		    	    	url: me.url,
		    	        type: 'ajax',
		    	        reader: {
		    	            type: 'json'
		    	        },
		    	        extraParams: me.extraParams
		    	    };
		}
		var store = Ext.create('Ext.data.TreeStore', {
			fields : me.modelFields,
    	    proxy: proxy,
    	    root: me.root
    	});
		var toolbar = Ext.create('Ext.toolbar.Toolbar',{
			border:true
		});
		
		Ext.apply(me,{
			store:store,
			dockedItems:[toolbar],
			listeners: {
   				//树单击事件
                itemclick: me.onMyItemClick,
                /**
                 * 右键监听事件
                 */
                itemcontextmenu: function (view, rec, node, index, e) {
                    e.stopEvent();
//                    var menu = me.onMyContext(view, rec, node, index, e);
//                    if (menu) {
//                        menu.showAt(e.getPoint());
//                    }
                    return false;
                }
            }
		});
		me.callParent(arguments);

		//打开关闭按钮
    	var expandAndcollapseButton =Ext.create('Ext.Button',{
			listeners:{
				beforerender:function(thiz){
					if(me.mysearchable){
						thiz.setIconCls('icon-expand-all');
					}else{
						thiz.setIconCls('icon-collapse-all');
					}
				}
			},
			handler:function(){
				if(me.mysearchable){
					me.mysearchable=false;
					expandAndcollapseButton.setIconCls('icon-collapse-all');
					me.getRootNode().collapseChildren();
				}else{
					me.mysearchable=true;
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
		
		//查询按钮
		var searchField = Ext.create('Ext.ux.form.SearchField', {
				width : 150,
				paramName:me.mysearchParamName,
				store:me.store,
				emptyText : FHD.locale.get('searchField.emptyText')
		});
		
		if(me.mysearchable){
			toolbar.add(searchField);
		}
		toolbar.add(expandAndcollapseButton);
		toolbar.add(refreshButton);
	}
});