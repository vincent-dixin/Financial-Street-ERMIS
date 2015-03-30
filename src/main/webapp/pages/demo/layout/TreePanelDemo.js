Ext.define('FHD.demo.layout.TreePanelDemo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.treepaneldemo',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
 		
        var right = Ext.create("Ext.panel.Panel",{
        	region:'center',
        	title:'列表',
        	html:'右侧列表'
        });
        
        var data = '[{id:2,text:"父节点",cls:"org",leaf:true},{id:3,text:"父节点3",cls:"org",leaf:true}]';
        var queryUrl = __ctxPath + '/app/view/component/tree.json';	//暂时不留作参数
        var treetest = Ext.create('FHD.view.component.TreePanel',{//实例化一个grid列表
        	region:'west',
        	//mytype:'demo',
			url: queryUrl,///'getMyListFn',//调用后台url
			myfields:['id','text','leaf'],
			getMyListFn:function(params){	//params获取所有request的参数，以json格式存储
				//alert('搜索内容：'+params.query);
				return data;
			},
			onMyItemClick:function(tablepanel, record, item, index, e, options){
				alert(record.data.id);
			}
		});

        Ext.applyIf(me,{
        	layout:'border',
        	items:[treetest,right]
        });
        
        me.callParent(arguments);
      
    }
});