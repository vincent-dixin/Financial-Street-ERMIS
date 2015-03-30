
Ext.define('FHD.view.bpm.mywork.MyTask',{
    extend: 'FHD.ux.GroupGridPanel',
    alias: 'widget.mytask',
    
    url : __ctxPath + '/jbpm/processInstance/jbpmHistActinstPage.f',
    
	storeGroupField:'disName',
     
    initComponent : function() {
    	var me = this;
    	Ext.apply(me,{
    		extraParams:{assigneeId:__user.userId,endactivity:"execut1",dbversion:0},
    		checked:false,
    		cols : [
				{dataIndex: 'processInstanceId', hidden:true},
				{dataIndex: 'businessId', hidden:true},
				{dataIndex: 'form', hidden:true},
				{header: FHD.locale.get('fhd.pages.test.field.name'), dataIndex: 'businessName', sortable: false, flex :2},
				{header: "任务名称",dataIndex: 'activityName',sortable: true,flex:2},
				{header: FHD.locale.get('fhd.bpm.task.taskPage.businessType'), dataIndex: 'disName', sortable: true, flex : 2},
				{header: FHD.locale.get('fhd.bpm.task.taskPage.createTime'), dataIndex: 'startStr', sortable: true, flex : 2},
				{header: FHD.locale.get('fhd.common.operate'), dataIndex: 'operate', sortable: false, flex : 0.5,renderer: function(value, metaData, record, colIndex, store, view) { 
	                    return "<a href=\"javascript:void(0);\">执行<input name='url' type='hidden' value='"+record.get("form")+"'><input name='executionId' type='hidden' value='"+record.get("processInstanceId")+"'><input name='businessId' type='hidden' value='"+record.get("businessId")+"'></a>";
					},
					listeners:{
	            		click:{
	            			fn:me.execute
	            		}
	            	}
				}
			],
			sorter:[{property:'startStr',direction:'desc'}]
    	});
    	
        me.callParent(arguments);
        
    },
    
    execute : function (grid, ele, rowIndex){
    	var jEle=jQuery(ele);
    	var me = this.up('panel');
		var winId = "win" + Math.random()+"$ewin";
		
		var taskPanel = Ext.create(jEle.find("[name='url']").val(),{
			executionId : jEle.find("[name='executionId']").val(),
			businessId : jEle.find("[name='businessId']").val(),
			winId: winId
		});
		
		var window = Ext.create('FHD.ux.Window',{
			id:winId,
			title:FHD.locale.get('fhd.common.execute'),
			iconCls: 'icon-edit',//标题前的图片
			items: taskPanel, 
			maximizable: true,
			listeners:{
				close : function(){
					me.reloadData();
				}
			}
		});
		window.show();
		taskPanel.reloadData();
	},
    
    reloadData: function() {
    	var me = this;
    	me.store.load();
    }
    
});
