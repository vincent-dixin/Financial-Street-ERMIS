Ext.define('FHD.view.assess.kpiSet.MainPanel', {
    extend: 'Ext.panel.Panel',
    //alias: 'widget.kpiSetPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        
        var cols = [ {
			dataIndex : 'id',
			hidden : true
		}, {
			header : "目标名称",
			dataIndex : 'name',
			sortable : true,
			flex : 1
		}, {
			header : "工作目标",
			dataIndex : 'workTarget',
			sortable : true,
			flex : 1
		}, {
			header : "操作",
			dataIndex : 'id',
			sortable : true,
			flex : 1,
			renderer:function(){
				return "操作";
			}
		}, {
			header : "衡量指标",
			dataIndex : 'assessKpi',
			sortable : true,
			flex : 1
		}, {
			header : "指标说明",
			dataIndex : 'kpiDesc',
			sortable : true,
			flex : 1
		}, {
			header : "风险定义",
			dataIndex : 'riskDefine',
			sortable : true,
			flex : 1,
			editor:{
					xtype:'textfield',
					editable:true
				}
		}, {
			header : "操作",
			dataIndex : 'id',
			sortable : true,
			flex : 1,
			renderer:function(){
				return "操作";
			}
		}];
		
        var grid = Ext.create("FHD.view.component.EditorGridPanel",{
        	region:'center',
        	url : __ctxPath + "/app/view/assess/kpiSet/targetSetList.json",
        	//url : __ctxPath + "/app/view/component/list.json",
            extraParams:{
            	
            },
        	cols:cols,
        	btns:[{
        			btype:'save',
        			handler:function(){
        				alert('save');
        			}
    		}],
        	title:'列表',
		    border: true,
		    checked: true
        });
        
        Ext.apply(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            layout:'border',
            items: [grid]
        });
        
        me.callParent(arguments);
      
    }
});