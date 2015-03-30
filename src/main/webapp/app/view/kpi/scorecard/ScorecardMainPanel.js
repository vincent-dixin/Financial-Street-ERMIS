Ext.define('FHD.view.kpi.scorecard.ScorecardMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.scorecardmainpanel',
    
    requires: [
        'FHD.view.kpi.scorecard.ScorecardTab'
    ],
    
    initComponent: function() {
        var me = this;
        me.paramObj = {};
        me.scorecardtab = Ext.widget('scorecardtab',{flex:1,id:'scorecardtab'});
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
    			html:'<div id="scorecardtabcontainer" class="navigation"></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.scorecardtab]
        });
        
        me.callParent(arguments);
    },
    
    // 重新加载数据
    reLoadData : function(record) {
    	var me = this;
		if (record.parentNode == null) {
			 me.navigationBar.renderHtml('scorecardtabcontainer', '' , '', 'sc');
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
    	me.scorecardtab.initParam(paramObj);
    	me.scorecardtab.reLoadData();
    	me.navigationBar.renderHtml('scorecardtabcontainer', me.paramObj.categoryid , '', 'sc');
    }
    ,
    gatherResultFun : function(v){
    	var me = this;
    	PARAM.category.categoryId = me.paramObj.categoryid;
    	PARAM.category.categoryName = v;
    	PARAM.kpiname = v;
    	PARAM.type = 'category';
    	
    	if(Ext.getCmp('manPanel') == null){
    		Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.widget('manPanel').load(PARAM));
    	}else{
    		Ext.getCmp('metriccentercardpanel').setActiveItem(Ext.getCmp('manPanel').load(PARAM));
    	}
    }
    
    
});