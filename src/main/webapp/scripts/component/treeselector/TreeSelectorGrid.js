Ext.define('FHD.ux.treeselector.TreeSelectorGrid', {
    extend: 'FHD.ux.treeselector.GridPanel',
    alias: 'widget.treeselectorgrid',
    
    //接口
    entityName:'NoEntityName',	//后台反射使用
	queryKey:'name',
	parentKey:'parent.id',
	relationKey:'idSeq',
	multiSelect:true,
    mycolumns:null,
    mydata:null,
        
    title:'选择',
    border: true,		// 默认不显示border
    checked: true,		// 是否可以选中
    initComponent: function() {
        var me = this;
		
        //1. 构建grid的cols
        me.cols = [];
		me.cols.push({
			dataIndex:'id',
			hidden:true
		});
        for(var i = 0; i < me.mycolumns.length; i++) {
        	me.cols.push(me.mycolumns[i]);
        }
        me.cols.push({
        	header: '操作',
            dataIndex: 'id',
        	width:35,
        	align:'center',
        	readonly:true,
        	renderer:function(){
        		return '<font class="icon-close" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;&nbsp;</font>';
        	},
        	listeners:{
        		click:{
        			fn:function(grid,d,i){
        				var select=me.store.getAt(i);
        				me.store.remove(select);
        			}
        		}
        	}
    	});
        
        //查找按钮
        me.btnSearch = Ext.create('Ext.Button', {
		    tooltip: '查找',
		    text:'查找',
            iconCls: 'icon-add',
		    handler: function() {
		    	var choose = new Array();
            	var selects = new Array();
            	me.getGridStore().each(function(r){
            		choose.push(r.data.id);
            		selects.push(r);
            	});
		        me.window = Ext.create('FHD.ux.treeselector.TreeSelectorWindow',{
		    		title:me.title,
		    		columns:me.mycolumns,
		    		entityName:me.entityName,
		    		queryKey:me.queryKey,
		    		parentKey:me.parentKey,
		    		relationKey:me.relationKey,
					multiSelect:me.multiSelect,
					choose:choose.join(','),
					onSubmit:function(win){
						me.store.removeAll();
						me.setGridStore(win.selectedgrid.store);
					}
				}).show();
				me.window.setValue(selects);
		    }
		});
        me.tbarItems = [me.btnSearch];
        
        Ext.apply(me, {
        	mydata:me.mydata,
            searchable:false,
            pagable:false,
            extraParams:{
            	
            }
        });
        me.callParent(arguments);
    },
    /**
     * 销毁自己组件的垃圾
     */
    destroy: function() {
    	var me = this;
    	me.callParent(arguments);
    },
    /**
     * private 获得当前grid的store
     * @return 当前grid的store
     */
	getGridStore:function(){
    	var me = this;
		return me.store;
    },
    /**
	 * private 为隐藏域和显示的grid赋值
	 */
    setGridStore:function(values){
    	var me = this;
    	values.each(function(r){
    		me.store.insert(0,r);
    	});
    }
});