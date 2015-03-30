/*
 * 整改涉及的缺陷设置整改复核人列表
 * 入参：improveId  整改计划ID
 */
 Ext.define('FHD.view.icm.rectify.component.ImprovePlanSetReviewerEditGrid',{
 	extend: 'Ext.container.Container',
    alias: 'widget.improveplansetreviewereditgrid',
    layout:'fit',
    border: false,
    mergeImprovePlanSetReivewer: function(){
    	var me = this,window,
    		selection = me.improvePlanGrid.getSelectionModel().getSelection(),//得到选中的记录,
			improvePlanIds = new Array();
		for ( var i = 0; i < selection.length; i++) {
			improvePlanIds.push(selection[i].get('id'));
		}
    	if(improvePlanIds){
    		Ext.create('FHD.ux.org.EmpSelectorWindow',{
				multiSelect:false,
				onSubmit:function(win){
                	var empId = new Array();
                	win.selectedgrid.store.each(function(r){
                		empId.push(r.data.id);
                	});
                	FHD.ajax({//ajax调用
						url : __ctxPath+ '/icm/improve/mergeimproveplansetreviewerbatch.f',
						params : {
							improvePlanIds: improvePlanIds,
							empId: empId
						},
						callback: function (data){		                    			
							if (data) { //保存成功
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
								me.reloadData();
							} else {
							    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
							}
						}
	    			});
				}
			}).show();
    	}
    },
    deleteImprovePlanSetReivewer: function(){
    	var me = this,
    		selection,
			improvePlanId;
    	selection = me.improvePlanGrid.getSelectionModel().getSelection()[0];//得到选中的记录
    
    	Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {//确认删除
					if(selection){
			    		improvePlanId = selection.get('id');
			    	}
			    	
			    	FHD.ajax({//ajax调用
						url : __ctxPath+ '/icm/improve/removeImprovePlanReviewer.f',
						params : {
							improvePlanIds: improvePlanId
						},
						callback: function (data){		                    			
							if (data) { //保存成功
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
								me.reloadData();
							} else {
							    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
							}
						}
	    			});
				}
			}
		});
    },
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
    setstatus: function(){
    	var me = this;
        var length = me.improvePlanGrid.getSelectionModel().getSelection().length;
        if(me.improvePlanGrid.down('#mergeImprovePlanSetReivewer')){
        	me.improvePlanGrid.down('#mergeImprovePlanSetReivewer').setDisabled(length < 1);
        }
    },
    initComponent: function(){
    	var me = this;
    	
    	me.improvePlanGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
			multiSelect:false,
			autoScroll:true,
			searchable:false,
			pagable: false,
			border: me.border,
			tbarItems:[{iconCls : 'icon-group-add',id:'mergeImprovePlanSetReivewer',handler :me.mergeImprovePlanSetReivewer,tooltip:'先选中要指定复核人的记录，再点击该按钮',text:'批量设置复核人', scope : this}
			],
			checked:true,
			url: __ctxPath+'/icm/improve/findimproveplanlistandreviewerbyimproveid.f?improveId='+me.improveId,
			cols:[{dataIndex:'id',hidden:true},
				{dataIndex:'companyId',hidden:true},
				{dataIndex:'fileId',hidden:true},
				{dataIndex:'empId',hidden:true},
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
				{header:'方案内容', dataIndex: 'content', sortable: false,flex:1, renderer : function(value, metaData, record, colIndex, store, view) { 
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
				{header:'复核人<font color=red>*</font>', dataIndex: 'reviewer',sortable: false, width:200, renderer : function(value, metaData, record, colIndex, store, view) { 
						metaData.tdAttr = 'style="background-color:#FFFBE6"';
						if (value) {
							return value+"&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').deleteImprovePlanSetReivewer()\"><font class='icon-close' style='cursor:pointer;'>&nbsp&nbsp&nbsp&nbsp</font></a>";
						}else {
							return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').mergeImprovePlanSetReivewer()\">请选择</a>"; 
						}
					}
				}
			]
		}); 
		me.callParent(arguments);
		me.improvePlanGrid.store.on('load', function () {
            me.setstatus()
        });
        me.improvePlanGrid.on('selectionchange', function () {
            me.setstatus()
        });
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