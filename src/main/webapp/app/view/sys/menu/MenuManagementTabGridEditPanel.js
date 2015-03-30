/**
 * @author denggy
 * @describe 页签动态表格面板
 */
 Ext.define('tabEditGridDataMapping', {
		    extend: 'Ext.data.Model',
		    fields:['id','parentId','parentName','authorityCode', 'authorityName', 'url','isLeafs','sn','rank','icon']
		});
 Ext.define('FHD.view.sys.menu.MenuManagementTabGridEditPanel',{
    	extend:'FHD.ux.EditorGridPanel',
    	alias: 'widget.menumanagementtabgrideditpanel',
    	url: __ctxPath + '/menu/menuManagement/findMenuInfoByIdAndType.f',//查询
    	region:'center',
    	objectType:{},
    	pagable : false,
    	searchable:false,
    	layout: 'fit',
    	height:150,
    	addGrid:function(){//新增方法
			var me = this;
			var count = me.store.data.length;
			var maxSort = me.getStore().getAt(count-1) && me.getStore().getAt(count-1).get("sn") || 0;
			var r = Ext.create('tabEditGridDataMapping',{
				id : '',
				parentId : me.currentId,
				parentName : me.currentName,
				sn: maxSort+1,
				rank:1,
				authorityCode:'0001',
				authorityName:'输入名称',
				isLeafs:'0yn_n'
			});
			me.store.insert(count, r);
			me.editingPlugin.startEditByPosition({row:count,column:0});
		},
		saveGrid:function(){//保存方法
			var me = this;
			var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			var menuCodes = []; //获得菜单编号
			var currentIds = []; //获得记录id
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
				menuCodes.push(item.data.authorityCode);
				currentIds.push(item.data.id);
			});
			if(!jsonArray.length)return
				var obj = {};
			for(var k in menuCodes){	//判断列表中的编号是否重复
				if(obj[menuCodes[k]]){
					return Ext.Msg.alert('提示','菜单编号不能重复');
				}
				obj[menuCodes[k]] = 1;
			}
			FHD.ajax({
				url: __ctxPath + '/menu/menuManagement/findfindMenuInfoByCodeIfExist.f',//判断菜单编号是否存在
				params : {
					menuCodes:menuCodes.toString(),
					currentIds:currentIds.toString()
				},
				callback : function(data){
					if(data.success){
						return Ext.Msg.alert('提示','菜单编号不能重复');
					}
					FHD.ajax({
							url: __ctxPath + '/menu/menuManagement/saveMenuInfoForGrid.f',//保存
							params : {
								modifyRecords:Ext.encode(jsonArray),
								saveType:'T'
							},
							callback : function(data){
								if(data){
									Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
									me.store.load();
								}
							}
						})
				}
			})
			
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
							url : __ctxPath + '/menu/menuManagement/delMenuInfoById.f',//删除
							params : {
								ids : ids.join(',')
							},callback: function (data) {
                            if (data) {
                            	me.reloadData();
                            	var btnpanel = me.up('menumanagementformaddpanel').btnEditGrid;
                            	btnpanel.reloadData();
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
    	initComponent:function(){//初始化
    		
    		var me=this;
	    	Ext.apply(me,{
	    		extraParams:{
	    			id : me.currentId,
	    			type:'T'
	    		}
	    	});
	    	var yesNoStore = new FHD.ux.dict.DictSelectForEditGrid({
	            dictTypeId: '0yn',
	            allowBlank:false,
	            fieldLabel: ''
	        });
	    	
	    	me.tbarItems = [{
	    				text:'添加',
						iconCls : 'icon-add',
						id : 'sys_menumanagement_tab_add',
						disabled:true,
						handler : me.addGrid,
						scope : this
					}, '-', {
						text:'删除',
						iconCls : 'icon-del',
						id : 'sys_menumanagement_tab_del',
						disabled:true,
						handler : me.delGrid,
						scope : this
					}, '-', {
						text:'保存',
						iconCls : 'icon-save',
						id : 'sys_menumanagement_tab_save',
						disabled:true,
						handler : me.saveGrid,
						scope : this
					}];
	    	
	    	me.cols=[ {header:'ID',dataIndex:'id',hidden:true,editor:false},
	    	          {header:'父ID',dataIndex:'parentId',hidden:true,editor:false},
	    	          {header:'菜单编号',dataIndex:'authorityCode',hidden:false,editor: {allowBlank: false},flex:1},
	    			  {header:'菜单名称',dataIndex:'authorityName',hidden:false,editor: {allowBlank: false},flex:1},
				      {header:'上级菜单名称',dataIndex:'parentName',hidden:false,editor:false},
				      {header:'是否子叶',dataIndex:'isLeafs',hidden:false
				      ,editor:yesNoStore,allowBlank:false,
				      renderer:function(value){ 
				    	  var datastore = yesNoStore.store.data.items;
				    	  for(var i=0;i<datastore.length;i++){
				    		   if(datastore[i].data['id']==value){
				    			   return datastore[i].data['name']
				    		   }
				    	  }
				    	  
					  }},
					  {header:'菜单层次',dataIndex:'rank',hidden:false,editor:{
							xtype:'numberfield',
							allowBlank:false,
							minValue: 1,  
							allowDecimals: false, // 允许小数点 
							nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
							//hideTrigger: true,  //隐藏上下递增箭头
							keyNavEnabled: true,  //键盘导航
							mouseWheelEnabled: true,  //鼠标滚轮
							step:1
			      },width:80},
				      {header:'图标',dataIndex:'icon',hidden:false,editor:true},
				      {header:'链接地址',dataIndex:'url',hidden:false,editor:true},
				      {header:'排序',dataIndex:'sn',hidden:false,editor:{
								xtype:'numberfield',
								allowBlank:false,
								minValue: 1,  
								allowDecimals: false, // 允许小数点 
								nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
								//hideTrigger: true,  //隐藏上下递增箭头
								keyNavEnabled: true,  //键盘导航
								mouseWheelEnabled: true,  //鼠标滚轮
								step:1
				      },width:80},
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
			                    //console.log(rec.data.parentName);
			                    var btnpanel = me.up('menumanagementformaddpanel').btnEditGrid;
			                    btnpanel.down('#sys_menumanagement_btn_add').setDisabled(!rec.data.id);
			                    if(! rec.data.id) {
			                    	return Ext.Msg.alert('提示','请先保存此条记录！');
			                    }
			                    btnpanel.store.proxy.extraParams.id = rec.data.id;
			                    btnpanel.parentId = rec.data.id;
			                    btnpanel.parentName = rec.data.authorityName;
			                    btnpanel.reloadData();
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
		setstatus: function(){
	    	var me = this;
	        var length = me.getSelectionModel().getSelection().length;
	        var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
			me.down('#sys_menumanagement_tab_save').setDisabled(jsonArray.length === 0);
	        me.down('#sys_menumanagement_tab_del').setDisabled(length === 0);
	    },
	    reloadData :function(){//刷新grid
			var me = this;
			me.store.load();
			me.down('#sys_menumanagement_tab_save').setDisabled(true);
		}
});