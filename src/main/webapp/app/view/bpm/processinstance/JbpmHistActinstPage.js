
Ext.define('FHD.view.bpm.processinstance.JbpmHistActinstPage',{
    extend: 'FHD.ux.GroupGridPanel',
    alias: 'widget.JbpmHistActinstPage',
    
    jbpmHistProcinstId:"",
    model:"show",
    border:0,
    initComponent : function() {
    	var me = this;
    	var tbarItems=new Array();
    	var listeners={};
    	var checked=false;
    	if(me.model=="edit"){
	    	tbarItems.push({name:'update', text : "转办",iconCls: 'icon-edit',handler:me.update, disabled : true, scope : this});
	    	listeners={
				/*按钮控制监听*/
				selectionchange:function(selection,selecteds){
					me.down("[name='update']").disable();
				   	if(selecteds.length==1){
			   			if(selecteds[0].get('dbversion')==0){
				   			me.down("[name='update']").enable();
			   			}
				   	}
				}
			};
			checked=true;
    	}
    	Ext.apply(me,{
    		url : __ctxPath + '/jbpm/processInstance/jbpmHistActinstPage.f',
    		extraParams:{jbpmHistProcinstId:me.jbpmHistProcinstId},
    		checked:checked,
    		cols : [
				{dataIndex:'id',hidden:true},
				{dataIndex:'dbversion',hidden:true},
				{header: "任务名称",dataIndex: 'activityName',sortable: true,flex:1.5}, 
				{header: "状态",dataIndex: 'dbversionStr',sortable: true,flex:0.5},
				{header: "承办人编码",dataIndex: 'assigneeRealCode',sortable: false,flex:1},
				{header: "承办人",dataIndex: 'assigneeRealname',sortable: false,flex:0.5},
				{header: "承办人公司",dataIndex: 'assigneeCompanyName',sortable: false,flex:1},
				{header: "审批意见",dataIndex: 'ea_Content',sortable: false,flex:2},
				{header: "到达时间",dataIndex: 'startStr',sortable: true,flex:1.5},
				{header: "操作时间",dataIndex: 'endStr',sortable: true,flex:1.5}
			],
			/*默认排序方式*/
			sorter:[{property:'dbversionStr',direction:'desc'},{property:'startStr',direction:'desc'}],
			/*工具栏*/
			tbarItems:tbarItems,
			listeners:listeners
    	});
        me.callParent(arguments);
    },
   	update:function(){
    	var me=this;
    	var selecteds = me.getSelectionModel().getSelection();
    	var ids=new Array();
    	for(var i=0;i<selecteds.length;i++){
    		ids.push(selecteds[i].get('processInstanceId'));
		}
		jQuery.ajax({
			type: "POST",
			url: __ctxPath +"/jbpm/removePorcessInstance.f",
			data: "ids="+ids,
			success: function(msg){
				me.store.load();
				Ext.MessageBox.alert('提示', '操作成功!');
			},
			error: function(){
				Ext.MessageBox.alert('提示',"操作失败!");
			}
		});
    }
});
