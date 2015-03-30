/*
 * 评价产生的缺陷的可编辑列表 
 * */
Ext.define('AssessPointPre', {
		    extend: 'Ext.data.Model',
		    fields:['id','pointId','processId','measureId','type','assessSampleName','desc','comment']
		});
Ext.define('FHD.view.icm.icsystem.component.AssessPointEditGridForView',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.assesspointeditgridforview',
	url: __ctxPath + '/assess/findassesspointlistbysome.f',
	region:'center',
	objectType:{},
	pagable : false,
	searchable:false,
	border : false,
	sortableColumns : false,
	layout: 'fit',
	readOnly : false,
	checked : false,
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	initComponent:function(){
		var me=this;
		Ext.apply(me,{
			extraParams:{
				processPointId : me.processPointId,
				measureId : me.measureId,
				processId : me.processId,
				type : me.type
			}
		});
		me.cols=[ 
				  {header:'评价样本名称',dataIndex:'assessSampleName',flex:1},
				  {header:'描述',dataIndex:'desc',flex:1},
			      {header:'实施证据', dataIndex: 'comment',flex:1}
			      ];
		me.callParent(arguments);
	},
	reloadData :function(){
		var me = this;
		me.store.proxy.url = __ctxPath + '/assess/findassesspointlistbysome.f',
        me.store.proxy.extraParams = me.paramObj;
		me.store.load();
	}
});