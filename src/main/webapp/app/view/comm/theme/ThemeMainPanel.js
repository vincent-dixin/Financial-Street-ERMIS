Ext.define('FHD.view.comm.theme.ThemeMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.thememainpanel',
    
    requires: [
        'FHD.view.comm.theme.ThemeTab'
    ],
    
    initComponent: function() {
        var me = this;
        me.paramObj = {};
        me.themetab = Ext.widget('themetab',{flex:1,id:'themetab'});
//        me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
//        	type: 'sc',
//        	id : '' 
//        });
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[{
    			xtype:'box',
    			height:20,
    			style : 'border-left: 1px  #99bce8 solid;',
    			html:'<div id="scorecardtabcontainer" class="navigation"></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.themetab]
        });
        
        me.callParent(arguments);
    },
    
    // 重新加载数据
    reLoadData : function(record) {
    	var me = this;
//		if (record.parentNode == null) {
//			 me.navigationBar.renderHtml('scorecardtabcontainer', '' , '', 'sc');
//			 return;//如果是根节点直接返回
//		}
    	var id = record.data.id;
        var name = record.data.text;
    	var paramObj = {};
    	
    	if(record.parentNode.data.id == 'category_root'){//如果父级节点是根节点的情况
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
    	me.themetab.initParam(paramObj);
    	me.themetab.reLoadData();
//    	me.navigationBar.renderHtml('scorecardtabcontainer', me.paramObj.categoryid , '', 'sc');
    }
    
});