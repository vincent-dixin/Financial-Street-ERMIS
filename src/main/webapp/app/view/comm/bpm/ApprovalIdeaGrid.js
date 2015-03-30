/*
 * 评价流程的任务分配可编辑列表
 * */
Ext.define('FHD.view.comm.bpm.ApprovalIdeaGrid',{
	extend : 'FHD.ux.GridPanel',
	alias : 'widget.approvalideagrid',
	
	url:__ctxPath + "/jbpm/processInstance/findApprovalIdeaByExecutionId.f",
	extraParams:{
		processInstanceId:''
	},
	multiSelect:false,
	pagable:false,
	autoScroll:true,
	checked:false,
	
	initComponent : function() {
		var me = this;
		
		me.extraParams.processInstanceId=me.executionId;
		me.cols=[{
            header: '流程节点名称',
            dataIndex: 'processActivityName',
            sortable: true,
            flex: 2
        },
        {
            header: '审批人',
            dataIndex: 'approveEmp',
            sortable: false,
            flex: 1
        },
        {
            header: '审批操作',
            dataIndex: 'approveOperate',
            sortable: false,
            flex: 1,
            renderer:function(value){
            	if(value=='yes'){
            		return '同意';
            	}else if(value=='no'){
            		return '不同意';
            	}
            }
        },
        {
            header: '审批意见',
            dataIndex: 'approveIdeaContent',
            sortable: false,
            flex: 5
        },
        {
            header: '审批时间',
            dataIndex: 'approveDate',
            sortable: false,
            flex: 2
        },
        {
        	dataIndex:'id',
        	hidden:true
        }];
		
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		
		me.store.load();
	}
});