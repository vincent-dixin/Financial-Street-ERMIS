/*
 * 评价产生的缺陷的可编辑列表 
 * */
 Ext.define('ProcessPointPre', {
		    extend: 'Ext.data.Model',
		    fields:['id','processId','pointId', 'pointName', 'pointPreId','contition']
		});
Ext.define('FHD.view.icm.icsystem.component.ParentNoteEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.parentnoteeditgrid',
	url: '',
	region:'center',
	objectType:{},
	pagable : false,
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	searchable:false,
	layout: 'fit',
	addGrid:function(){//新增方法
		var me = this;
//			var upMe = me.up('noteeditform');
//			var values = upMe.getForm().getValues();
		var r = Ext.create('ProcessPointPre',{
			id : '',
			pointId:me.paramObj.processPointId,
			processId : me.paramObj.processId
//    			pointName:values.name
		});
		me.store.insert(0, r);
		me.editingPlugin.startEditByPosition({row:0,column:0});
	},
	saveGrid:function(){//保存方法
		var me = this;
		var rows = me.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
			url: __ctxPath + '/ProcessPoint/ProcessPoint/saveParentPointEditGrid.f',
			params : {
				modifiedRecord:Ext.encode(jsonArray)
			},
			callback : function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					me.store.load();
				}
			}
		})
		me.store.commitChanges();
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
						url : __ctxPath + '/processpoint/removeparentpointbyid.f',
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
    			processId : me.processId
    		}
    	});
    	//me.extraParams.processPointId = Ext.getCmp('flownotelist').selectId;
    	me.processStore = Ext.create('Ext.data.Store',{//myLocale的store
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : ''  //__ctxPath + '/processpoint/findallprocesspointbyprocessid.f'
			}
		});
		me.processStore.load();
    	me.tbarItems = [{
					iconCls : 'icon-add',
					id : 'parentPointAdd',
					text: '添加',tooltip: '上级节点',
					handler : me.addGrid,
					scope : this
				}, '-', {
					iconCls : 'icon-del',
					id : 'parentPointDel',
					text: '删除',tooltip: '下级节点',
					handler : me.delGrid,
					scope : this
				}];
    	me.cols=[ {header:'节点ID',dataIndex:'pointId',hidden:true},
    			  {header:'流程Id',dataIndex:'processId',hidden:true},
			      {header:'节点名称',dataIndex:'pointName',hidden:true,flex:1},
			      {header:'父节点',dataIndex:'pointPreId',hidden:false
			      ,editor:new Ext.form.ComboBox({
					   store :me.processStore,
					   valueField : 'id',
					   displayField : 'name',
					   allowBlank : false,
					   editable : false
			      }),
			      renderer:function(value){
			      		debugger;
						var index = me.processStore.find('id',value);
						var record = me.processStore.getAt(index);
						if(record!=null){
							return record.data.name;
						}else{
							return value;
						}
				  }},
			      {header:'入口条件', dataIndex: 'contition', sortable: false,flex:1,editor:true}
			      ];
    	me.callParent(arguments);
    },
    reloadData :function(){
		var me = this;
		me.store.proxy.url = __ctxPath + '/process/findParentListByPointId.f',
        me.store.proxy.extraParams = me.paramObj;
		me.store.load();
		me.processStore.proxy.url = __ctxPath + '/processpoint/findallprocesspointbyprocessid.f';
		me.processStore.proxy.extraParams = me.paramObj;
		me.processStore.load();
	}
});