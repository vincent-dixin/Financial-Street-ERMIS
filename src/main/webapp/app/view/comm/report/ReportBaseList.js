/**
 * 评价计划报告组件
 */
Ext.define('FHD.view.comm.report.ReportBaseList', {
	extend : 'FHD.ux.GridPanel',
	alias : 'widget.reportbaselist',

	url : __ctxPath + "/comm/report/findReportInfomationList.f",
	iconCls : 'icon-ibm-icon-reports',
	border : false,
	autoScroll : true,

	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			cols : [{
				header : '标题',
				dataIndex : 'name',
				sortable : true,
				flex : 3
			}, {
				header : '所属公司',
				dataIndex : 'companyName',
				sortable : true,
				flex : 2
			}, {
				header : '版本号',
				dataIndex : 'code',
				sortable : true,
				flex : 1
			}, {
				header : '状态',
				dataIndex : 'status',
				sortable : true,
				flex : 1,
				renderer:function(value){
					if(value=='S'){
						return '已保存';
					}else if(value=='P'){
						return '已提交';
					}
					return value;
				}
			}, {
				header : '执行状态',dataIndex :'dealStatus',sortable : true,flex:1,
				renderer:function(value){
					if(value=='N'){
						return '未开始';
					}else if(value=='H'){
						return '处理中';
					}else if(value=='F'){
						return '已完成';
					}else if(value=='A'){
						return '逾期';
					}
					return value;
				}
 			},{
				header : '创建者',
				dataIndex : 'createEmp',
				sortable : true,
				flex : 1
			}, {
				header : '创建时间',
				dataIndex : 'createDate',
				sortable : true,
				flex : 2
			}, {
				header : FHD.locale
						.get('fhd.formula.operate'),
				xtype : 'actioncolumn',
				dataIndex : 'id',
				flex : 1,
				items : [{
					icon : __ctxPath
							+ '/images/icons/download.png',
					tooltip : '下载附件',
					handler : me.downloadFun
				}]
			}]
		});

		me.callParent(arguments);
	},

	downloadFun : function(grid, rowIndex, colIndex) {
		var me = this;

		var rec = grid.getStore().getAt(rowIndex);
		var id = rec.get('id');
		window.location.href = __ctxPath + '/comm/report/downloadReport.f?id=' + id;
	},

	reloadData : function() {
		var me = this;
		me.grid.store.load();
	}
});