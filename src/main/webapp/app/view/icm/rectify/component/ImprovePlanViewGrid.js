/*
 * 整改涉及的缺陷的整改方案列表
 * 入参：improveId  整改计划ID
 */
 Ext.define('FHD.view.icm.rectify.component.ImprovePlanViewGrid',{
 	extend: 'Ext.container.Container',
    alias: 'widget.improveplanviewgrid',
    layout:'fit',
    border: false,
    downloadImprovePlanRelaFile: function(){
    	var me = this,
    		selection,
			fileId;
    	selection = me.improvePlanGrid.getSelectionModel().getSelection()[0];//得到选中的记录
    	if(selection){
    		fileId = selection.get('fileId');
    	}
    	downloadFile(fileId);
    },
    initComponent: function(){
    	var me = this;
    	me.improvePlanGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
			autoScroll:true,
			searchable:false,
			pagable: false,
			checked:false,
			border: false,
			cols:[{header:'id',dataIndex:'id',hidden:true},
				{header:'companyId',dataIndex:'companyId',hidden:true},
				{header:'fileId',dataIndex:'fileId',hidden:true},
				{header:'empId',dataIndex:'empId',hidden:true},
				{header:'责任部门', dataIndex: 'orgName', sortable: false},
				{header:'缺陷描述', dataIndex: 'desc',sortable: false,flex:1, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						return value;
					}
				},
				{header:'方案内容', dataIndex: 'content', sortable: false,flex:2, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						return value;
					}
				},
				{header:'开始日期', dataIndex: 'planStartDate', sortable: false, renderer: function(value, metaData, record, colIndex, store, view) { 
	                    if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
	                    } else {
	                        return value;
	                    }
					}
				},
				{header:'结束日期', dataIndex: 'planEndDate', sortable: false, renderer: function(value, metaData, record, colIndex, store, view) { 
						if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
						} else {
	                        return value;
	                    }
					}
				},
				{header:'附件', dataIndex: 'file',sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').downloadImprovePlanRelaFile()\">"+value+"</a>";  
						//return value;
					}
				},
				
				{header:'上报人', dataIndex: 'lastModifyBy',width:160, sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						if (value) {
	                        return value;
						} else {
	                        return '';
	                    }
					}
				},
				{header:'复核人', dataIndex: 'reviewer',width:160, sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						if (value) {
	                        return value;
						} else {
	                        return '';
	                    }
					}
				},
				{header:'进度', dataIndex: 'finishRate',width:60, sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						if(value){
							return value+'%';
						}else{
							return '';
						}
						
					}
				},
				{header:'说明', dataIndex: 'improveResult', sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						return value;
					}
				}
			]
		}); 
		me.callParent(arguments);
		me.add(me.improvePlanGrid);
    },
    loadData: function(improveId){
    	var me = this;
    	me.improveId = improveId;
    	me.reloadData();
    },
    reloadData: function(){
    	var me = this;
    	if(me.improveId){
			me.improvePlanGrid.store.proxy.url=__ctxPath + '/icm/improve/findimproveplanlistandreviewerbyimproveid.f';
			me.improvePlanGrid.store.on('beforeload', function (store, options) {
				var new_params = {improveId: me.improveId };
		        Ext.apply(store.proxy.extraParams, new_params);
	    	});
			me.improvePlanGrid.store.load();
		}
    	
    }
});