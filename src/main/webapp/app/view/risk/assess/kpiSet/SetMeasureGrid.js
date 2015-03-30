/**
 * @author denggy
 * @describe 评价标准模板动态列表
 */
 Ext.define('SetMeasureGridDataMapping', {
		    extend: 'Ext.data.Model',
		    fields:['id']
		});
 Ext.define('FHD.view.risk.assess.kpiSet.SetMeasureGrid',{
    	extend:'FHD.ux.EditorGridPanel',
    	alias: 'widget.setmeasuregrid',
    	findUrl: __ctxPath + "",//查询
    	region:'center',
    	objectType:{},
    	pagable : false,
    	searchable:false,
    	flex: 1,
    	layout: 'fit',
        margin: '2 0 0 0',
    	addGrid:function(){//新增方法
			var me = this;
			var count = me.store.data.length;
			//var maxSort = me.getStore().getAt(count-1) && me.getStore().getAt(count-1).get("sort") || 0;
			var r = Ext.create('SetMeasureGridDataMapping',{
				//新增时初始化参数
				id:''
				
			});
			me.store.insert(count, r);
			me.editingPlugin.startEditByPosition({row:count,column:0});
		},
		saveGrid:function(){//保存方法
			var me = this;
			var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
		//	if(!this.validate(jsonArray)) return ;
			
			FHD.ajax({
				url: __ctxPath + '',//保存
				params : {
					modifyRecords:Ext.encode(jsonArray)
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
							var delId = selection[i].get('id');
							if(delId){
									ids.push(delId)
							}else {
									return Ext.Msg.alert('提示','没有保存的记录无法删除!')
							       }
						}
						FHD.ajax({
							url : __ctxPath + '/icm/assess/baseset/delAssessGuidelinesPropertyById.f',//删除
							params : {
								ids : ids.join(',')
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
		setstatus: function(){
	    	var me = this;
	        var length = me.getSelectionModel().getSelection().length;
//	        var rows = me.store.getModifiedRecords();
//			var jsonArray=[];
//			Ext.each(rows,function(item){
//				jsonArray.push(item.data);
//			});
//			me.down('#icm_assessguidelinesprop_save').setDisabled(jsonArray.length === 0);
//	        me.down('#icm_assessguidelinesprop_del').setDisabled(length === 0);
	    },
    	initComponent:function(){//初始化
    		var me=this;
    		
	    	var assessStore = Ext.create('Ext.data.Store', {
	    	    fields : ['id', 'name'],
	    		proxy : {type : 'ajax',url : 'sys/dic/findDictEntryBySome.f?typeId=ca_defect_level',
	    		reader:{type:'json',root:'children'}},
	    		autoload:false
	    	});
	    	assessStore.load();
	    	me.tbarItems = [{
	    				text:'添加',
						iconCls : 'icon-add',
						id : 'risk_measure_add',
						handler : me.addGrid,
						disabled :false,
						scope : this
					}, '-', {
						text:'删除',
						iconCls : 'icon-del',
						id : 'risk_measure_del',
						handler : me.delGrid,
						disabled :false,
						scope : this
					}, '-', {
						text:'保存',
						iconCls : 'icon-save',
						id : 'risk_measure_save',
						handler : me.saveGrid,
						disabled :true,
						scope : this
					}, '-', {
						text:'关联',
						iconCls : 'icon-cog',
						id : 'risk_measure_rel',
						handler : me.relative,
						disabled :false,
						scope : this
					}];
	    	
	    	me.cols=[ {
				dataIndex : 'id',
				hidden : true
			}
			, {
				header : "衡量指标",
				dataIndex : 'assessKpi',
				sortable : true,
				flex : 1,
				editor:true
			},{
				header : "权重",
				dataIndex : 'weight',
				sortable : true,
				flex : 1,
				editor:true
			}, {
				header : "指标说明",
				dataIndex : 'kpiDesc',
				sortable : true,
				flex : 1,
				editor:true
			},{

				header : "指标责任人",
				dataIndex : 'kpiER',
				sortable : true,
				flex : 1,
				editor:true
			
			},
			{
				header : "操作",
				dataIndex : 'id',
				sortable : true,
				flex : 1,
				editor:true
			}
			];
	        me.on('selectionchange', function () {
	            me.setstatus()
	        });
	        me.on('edit', function () {
	            me.setstatus()
	        });
	    	me.on('resize',function(p){
	    			me.setHeight(FHD.getCenterPanelHeight()-270);
	    	});
	    	me.callParent(arguments);
	    },
	    reloadData :function(){//刷新grid
			var me = this;
		},
		relative:function(){
		}
});