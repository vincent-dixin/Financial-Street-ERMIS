/**
 * 流程详细查看列表
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.icm.assess.AssessPlanDetailsPopGrid',{
	extend : 'FHD.ux.GridPanel',
	alias: 'widget.assessplandetailspopgrid',
	

	initComponent : function() {
		var me = this;
		me.queryUrl = __ctxPath + '/jbpm/processInstance/JbpmHistActinstList.f';
		me.cols=[{dataIndex:'processInstanceId',width:0,hidden:true},
				{header: "任务名称",dataIndex: 'activityName',sortable: false,flex:1,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";					
				}
		}, 
		{header: "状态",dataIndex: 'dbversion',sortable: true,width:60,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";					
				}
		}, 
		{header: "承办人编码",dataIndex: 'assigneeRealCode',sortable: true,flex:1,hidden:true}, 
		{header: "承办人",dataIndex: 'assigneeRealname',sortable: false,width:60,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";
				}
		},
		{header: "承办人单位",dataIndex: 'assigneeCompanyName',sortable: false,flex:1,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";
				}
		},
		{header: "审批操作",dataIndex: 'ea_Operates',sortable: false,flex:1,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){ 
					color="red";
				}
				value = value && ('yes'==value?'同意':'不同意')||''
				return "<font style='color:"+color+"'>" + value + "</font>";					
				}
		},
		{header: "审批意见",dataIndex: 'ea_Contents',sortable: false,flex:2,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";					
				}
		},
		{header: "到达时间",dataIndex: 'startStr',sortable: false,flex:1,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";					
				}
		},
		{header: "操作时间",dataIndex: 'endStr',sortable: false,flex:1,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var color="black";
				if(record.get("dbversion")=="未处理"){
					color="red";
				}
				return "<font style='color:"+color+"'>" + value + "</font>";					
				}
		}
		];
		me.tbarItems = [{
	    				text:'转办',
						iconCls : 'icon-edit',
						id : 'icm_assessplandetails_turn',
						handler : me.turnTodealwith,
						disabled :true,
						scope : this
		}];
		Ext.apply(me, {
			flex:1,
    		multiSelect: false,
    		border:true,
    		rowLines:true,//显示横向表格线
    		checked: false, //复选框
    		autoScroll:true,
    		title:"列表"
    	});
    	  me.on('selectionchange', function () {
		            me.setstatus()
		        });
		me.callParent(arguments);
	},
	reloadData : function(){
		var me = this;
		me.store.proxy.extraParams = { 
				jbpmHistProcinstId:me.reload_id
		};
		me.store.proxy.url = me.queryUrl;
		me.store.load();
	},
	turnTodealwith : function(){
		var me = this;
		var selection = me.getSelectionModel().getSelection();
		var recid = selection[0].get('id');
		var processInstanceId = selection[0].get('processInstanceId');
		var dbversion = selection[0].get('dbversion');
		me.assessplandetailsturntodealwith = Ext.create('FHD.view.icm.assess.AssessPlanDetailsTurnToDealwith',{recid:recid,processInstanceId:processInstanceId});
		var win = Ext.create('FHD.ux.Window',{
			height:280,
			title:'转办',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.assessplandetailsturntodealwith],
			buttonAlign: 'center',
			listeners:{
				close:function(){
					me.reloadData();
				}
			}
    	}).show();

	},
	    setstatus:function(){
    	
	    	var me = this;
	        var rec = me.getSelectionModel().getSelection();
	        
	        var flag = rec.length !== 0 && '未处理'==rec[0].get('dbversion');
			
	        me.down('#icm_assessplandetails_turn').setDisabled(!flag);
	    
    }
});