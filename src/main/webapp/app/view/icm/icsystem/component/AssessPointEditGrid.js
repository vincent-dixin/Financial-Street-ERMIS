/*
 * 评价产生的缺陷的可编辑列表 
 * */
Ext.define('AssessPointPre', {
		    extend: 'Ext.data.Model',
		    fields:['id','pointId','processId','measureId','type','assessSampleName','desc','comment']
		});
Ext.define('FHD.view.icm.icsystem.component.AssessPointEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.assesspointeditgrid',
	url: '',
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	region:'center',
	objectType:{},
	pagable : false,
	searchable:false,
	sortableColumns : false,
	layout: 'fit',
	readOnly : false,
	border : false,
	addGrid:function(){//新增方法
		debugger;
		var me = this;
		var r = Ext.create('AssessPointPre',{
			id : '',
			pointId:me.paramObj.processPointId,
			processId : me.paramObj.processId,
			measureId : me.paramObj.measureId,
			type : me.type
		});
		me.store.insert(0, r);
		me.editingPlugin.startEditByPosition({row:0,column:0});
	},
	delGrid:function(){//删除方法
		var me = this;
		var selection = me.getSelectionModel().getSelection();
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					var ids = [];
					for(var i=0;i<selection.length;i++){
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({
						url : __ctxPath + '/assesspoint/removeassesspointbyid.f',
						params : {
							ids:ids.join(',')
						},callback: function (data) {
		                    if (data) {
		                    	me.reloadData();
		                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		                    }else{
		                    	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
		                    }
	                }
					});
				}
			} 
		});
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
		me.relaSubjectStore = Ext.create('Ext.data.Store',{//myLocale的store
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/assess/finddictentrybytypeid.f',
				extraParams:{
	    			typeId : 'ic_rela_subject'
	    		}
			}
		});
		me.controlPointStore = Ext.create('Ext.data.Store',{//myLocale的store
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/assess/findDictEntryByTypeId.f?typeId=ic_control_point'
			}
		});
		if(me.readOnly){
			me.tbarItems = [];
		}else{
			me.tbarItems = [{
					iconCls : 'icon-add',
					text: '添加',tooltip: '添加评价点',
					handler : me.addGrid,
					scope : this
				}, '-', {
					iconCls : 'icon-del',
					text: '删除',tooltip: '删除评价点',
					handler : me.delGrid,
					scope : this
				}];
		}
		me.cols=[ {header:'ID',dataIndex:'assessPointId',hidden:true},
		          {header:'pointId',dataIndex:'pointId',hidden:true},
		          {header:'processId',dataIndex:'processId',hidden:true},
		          {header:'measureId',dataIndex:'measureId',hidden:true},
		          {header:'type',dataIndex:'type',hidden:true},
				  {header:'评价样本名称',dataIndex:'assessSampleName',flex:1,editor:true},
				  {header:'描述',dataIndex:'desc',flex:1,editor:true},
			      {header:'实施证据', dataIndex: 'comment', sortable: false,flex:1,editor:true}
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