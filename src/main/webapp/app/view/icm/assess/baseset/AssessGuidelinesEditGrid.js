/**
 * @author denggy
 * @describe 评价标准模板动态列表
 */
 Ext.define('AssessGuidelinesDataMapping', {
		    extend: 'Ext.data.Model',
		    fields:['id','name','comment','sort', 'dictype']
		});
 Ext.define('FHD.view.icm.assess.baseset.AssessGuidelinesEditGrid',{
    	extend:'FHD.ux.EditorGridPanel',
    	alias: 'widget.assessguidelineseditgrid',
    	requires: [
    	           'FHD.view.icm.assess.baseset.AssessGuidelinesPropertyEditGrid'
    	           ],
    	url: __ctxPath + '/icm/assess/baseset/findAssessGuidelinesBySome.f',//查询
    	region:'center',
    	objectType:{},
    	pagable : false,
    	searchable:false,
    	layout: 'fit',
    	flex: 1,
    	addGrid:function(){//新增方法
			var me = this;
			var count = me.store.data.length;
			var maxSort = me.getStore().getAt(count-1) && me.getStore().getAt(count-1).get("sort") || 0;
			var r = Ext.create('AssessGuidelinesDataMapping',{
				//新增时初始化参数
				id : '',
				sort: maxSort+1,
				name:'模板名称',
				dictype:'nonfinancial_defect'
			});
			me.store.insert(count, r);
			//alert(me.store.data.length);
			me.editingPlugin.startEditByPosition({row:count,column:0});
			//me.store.commitChanges();
		},
		saveGrid:function(){//保存方法
			var me = this;
			var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
			//if(!this.validate(jsonArray)) return ;
			
			FHD.ajax({
				url: __ctxPath + '/icm/assess/baseset/saveAssessGuidelines.f',//保存
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
			if(!selection.length)return
			Ext.MessageBox.show({
				title : FHD.locale.get('fhd.common.delete'),
				width : 260,
				msg : '评价模板删除，相对应的模板项也需要删除，你确定删除吗？',
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
							url : __ctxPath + '/icm/assess/baseset/delAssessGuidelinesById.f',//删除
							params : {
								id : ids.join(',')
							},callback: function (data) {
                            if (data) {
                            	me.reloadData();
                            	me.assessguidelinespropertyeditgrid && me.assessguidelinespropertyeditgrid.reloadData(' ');
                            	me.assessguidelinespropertyeditgrid && me.up('assessguidelinesmainpanel').remove(me.assessguidelinespropertyeditgrid)
                            	
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
	    setstatus:function(){
	    	var me = this;
	        var length = me.getSelectionModel().getSelection().length;
	        var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
			me.down('#icm_assessguidelines_save').setDisabled(jsonArray.length === 0);
	        me.down('#icm_assessguidelines_del').setDisabled(length === 0);
	    },
    	initComponent:function(){//初始化
    		var me=this;
    		
	    	var assessStore = Ext.create('Ext.data.Store', {//dictTreeCombox的store
	    	    fields : ['id', 'name'],
	    		proxy : {type : 'ajax',url : 'sys/dic/findDictEntryBySome.f?typeId=assessment_temp_type',
	    		reader:{type:'json',root:'children'}},
	    		autoload:false
	    	});
	    	assessStore.load();
	    	me.tbarItems = ['评价模板列表','-',{
	    				text:'添加',
						iconCls : 'icon-add',
						id : 'icm_assessguidelines_add',
						handler : function(){
							me.addGrid();
						}
					}, '-', {
						text:'删除',
						iconCls : 'icon-del',
						id : 'icm_assessguidelines_del',
						handler : me.delGrid,
						disabled :true,
						scope : this
					}, '-', {
						text:'保存',
						iconCls : 'icon-save',
						id : 'icm_assessguidelines_save',
						handler : me.saveGrid,
						disabled :true,
						scope : this
					}];
	    	
	    	me.cols=[ {header:'评价标准模板类型'+'<font color=red>*</font>',dataIndex:'dictype',hidden:false,width:140
				      		,editor:new FHD.ux.dict.DictSelectForEditGrid({
						    dictTypeId:'assessment_temp_type',fieldLabel:'',editable:false
						}),allowBlank:false,
					      renderer:function(value){ 
							    var curModel = assessStore.findRecord("id",value);
							    if(curModel!=null){
							    	return curModel.raw.name;
								}
						  }
	    			   },
	    			  {header:'评价标准名称'+'<font color=red>*</font>',dataIndex:'name',hidden:false,editor: {allowBlank: false},flex:1},
				      {header:'说明',dataIndex:'comment',hidden:false,editor:true,flex:1},
				      {header:'排序',dataIndex:'sort',hidden:false,editor:{
								xtype:'numberfield',
								allowBlank:false,
								minValue: 1,  
								allowDecimals: false, // 允许小数点 
								nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
								//hideTrigger: true,  //隐藏上下递增箭头
								keyNavEnabled: true,  //键盘导航
								mouseWheelEnabled: true,  //鼠标滚轮
								step:1
				      }},
				      {header:'操作',dataIndex:'',hidden:false,editor:false,align:'center',
				       xtype:'actioncolumn',
				       items: [{
			                icon: __ctxPath+'/images/icons/edit.gif',  // Use a URL in the icon config
			                tooltip: FHD.locale.get('fhd.common.edit'),
			                handler: function(grid, rowIndex, colIndex) {
			                	//点击编辑按钮时，自动选中行
		                    	grid.getSelectionModel().deselectAll();
		    					var rows=[grid.getStore().getAt(rowIndex)];
		    	    			grid.getSelectionModel().select(rows,true);
		    	    			
			                   var rec = grid.getStore().getAt(rowIndex);
			                   var  assessguidelinesmainpanel = me.up('assessguidelinesmainpanel');
			                  // assessguidelinesmainpanel.assessguidelinespropertyeditgrid && assessguidelinesmainpanel.assessguidelinespropertyeditgrid.remove(true);
			                	   
			                   me.assessguidelinespropertyeditgrid && assessguidelinesmainpanel.remove(me.assessguidelinespropertyeditgrid);
		                	   
			                   me.assessguidelinespropertyeditgrid = Ext.widget('assessguidelinespropertyeditgrid',{
			                	   parentId:rec.data.id,
			                	   parentName:rec.data.name,
			                	   as_tmlt_type:rec.data.dictype
			                   });
			                   assessguidelinesmainpanel.add(me.assessguidelinespropertyeditgrid);
			                   
			                   me.assessguidelinespropertyeditgrid.parentId = rec.data.id;
			                   me.assessguidelinespropertyeditgrid.parentName = rec.data.name;
			                   me.assessguidelinespropertyeditgrid.as_tmlt_type = rec.data.dictype;//赋值模板类型
			                   me.assessguidelinespropertyeditgrid.down('#icm_assessguidelinesprop_add').setDisabled(false);
			                   me.assessguidelinespropertyeditgrid.reloadData(rec.data.id);
			                   
			                   
			                }
			            }]
				    	  }
				      ];
	    	 me.on('selectionchange', function () {
		            me.setstatus()
		        });
	    	 me.on('edit', function () {
		            me.setstatus()
		        });
	    	me.callParent(arguments);
	    },
	    reloadData :function(){//刷新grid
			var me = this;
			me.store.load();
		}

});