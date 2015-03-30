Ext.define('FHD.view.monitor.sc.scMainContainer', {
    extend: 'Ext.container.Container',
    
    requires: [
    ],
    
    initComponent: function() {
        var me = this;
        
        me.paramObj = {};
        /**
         * 右侧上方导航
         */
        me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'sc',
        	id : '' 
        });
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[{
    			xtype:'box',
    			height:20,
    			style : 'border-left: 1px  #99bce8 solid;',
    			html:'<div id="'+me.navid+'" class="navigation"></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.sctab]
        });
        
        me.callParent(arguments);
    },
    /**
     * 导航重新渲染
     */
    navReRender:function(){
    	var me = this;
    	me.navigationBar.renderHtml(me.navid, me.paramObj.categoryid , '', 'sc');
    },
    
    // 重新加载数据
    reLoadData : function(record) {
    	var me = this;
		if (record.parentNode == null) {
			 me.navigationBar.renderHtml(me.navid, '' , '', 'sc');
			 return;//如果是根节点直接返回
		}
    	var id = record.data.id;
        var name = record.data.text;
    	var paramObj = {};
    	
    	if(record.parentNode.data&&record.parentNode.data.id == 'category_root'){//如果父级节点是根节点的情况
    		paramObj.categoryparentid = "";
            paramObj.categoryid = id;
            paramObj.categoryparentname =FHD.locale.get('fhd.kpi.categoryroot');
            paramObj.categoryname = name;
            paramObj.editflag = true;
            FHD.ajax({
            	async:false,
                url: __ctxPath + '/kpi/category/findcharttypebyid.f',
                params: {
                    id: id
                },
                callback: function (data) {
                	paramObj.chartIds = data.chartType==null?'':data.chartType;
                }
            });
		}else{
			//查找相关的参数信息
	    	 FHD.ajax({
	    		 async:false,
	             params: {
	                 "id": id
	             },
	             url: __ctxPath + '/kpi/category/findparentbyid.f',
	             callback: function (ret) {
	                 paramObj.categoryparentid = ret.parentid;
	                 paramObj.categoryid = id;
	                 paramObj.categoryparentname = ret.parentname;
	                 paramObj.categoryname = name;
	                 paramObj.editflag = true;
	                 paramObj.chartIds = ret.chartIds;
	             }
	         });
		}

    	
    	me.paramObj = paramObj;
    	me.sctab.initParam(paramObj);
    	me.sctab.reLoadData();
    	me.navigationBar.renderHtml(me.navid, me.paramObj.categoryid , '', 'sc');
    }
    
});