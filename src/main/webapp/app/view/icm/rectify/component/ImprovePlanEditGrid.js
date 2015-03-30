/*
 * 整改方案编辑列表，可上传附件，删除附件，下载附件。
 * */
Ext.define('FHD.view.icm.rectify.component.ImprovePlanEditGrid',{
 	extend: 'Ext.container.Container',
    alias: 'widget.improveplaneditgrid',
    layout:'fit',
    improveId:[],
    border: false,
    mergeImprovePlan: function(){
    	var me = this,
    		jsonArray= new Array();
		var rows = me.improvePlanGrid.store.getModifiedRecords();
		Ext.each(rows,function(item){
			if(item.data.content && item.data.content!=''){
				jsonArray.push(item.data);
			}
		});
		if(jsonArray.length==0){
			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'请先编辑“方案内容”');
		}
		if(jsonArray.length>0){
			 FHD.ajax({//ajax调用
    		     url : __ctxPath+ '/icm/improve/mergeimproveplanbatch.f',
    		     params : {
    		    	 improveId: me.improveId,
    		    	 modifiedRecord:Ext.encode(jsonArray)
    			 },
    			 callback: function (data){		                    			
					if (data) { //保存成功
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
						me.improvePlanGrid.store.commitChanges();
					} else {
					    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
					}
				}
    		});
		}
    },
    uploadImprovePlanRelaFile: function(){
    	var me = this,
    		selection,
			improvePlanId;
    	selection = me.improvePlanGrid.getSelectionModel().getSelection()[0];//得到选中的记录
    	if(selection){
    		improvePlanId = selection.get('id');
    	}
    	var jsonArray= new Array();
		var rows = me.improvePlanGrid.store.getModifiedRecords();
		Ext.each(rows,function(item){
			if(item.data.content && item.data.content!=''){
				jsonArray.push(item.data);
			}
		});
    	Ext.create('FHD.ux.fileupload.FileUploadWindow',{
			multiSelect: false,
			callBack:function(value){
				if(value!=null&&value.length>0){
					if(jsonArray.length>0){
						 FHD.ajax({//ajax调用
			    		     url : __ctxPath+ '/icm/improve/mergeimproveplanbatch.f',
			    		     params : {
			    		    	 improveId: me.improveId,
			    		    	 modifiedRecord:Ext.encode(jsonArray)
			    			 },
			    			 callback: function (data){		                    			
								if (data) { //保存成功
									FHD.ajax({
										url:__ctxPath+'/icm/rectify/mergeimprovePlanFile.f',
										params:{
											fileId:value, 
											improvePlanId:improvePlanId
											},
										callback:function(data){
											if(data){
												Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
												me.improvePlanGrid.store.load();
											}else {
											    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
											}
										}
									}); 
								} else {
								    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
								}
							}
			    		});
					}else{
						FHD.ajax({
							url:__ctxPath+'/icm/rectify/mergeimprovePlanFile.f',
							params:{
								fileId:value, 
								improvePlanId:improvePlanId
								},
							callback:function(data){
								if(data){
									Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
									me.improvePlanGrid.store.load();
								}else {
								    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
								}
							}
						}); 
					}
				}
			}
		}).show();
    },
    deleteImprovePlanRelaFile: function(){
    	var me = this,
    		selection,
			improvePlanId;
    	selection = me.improvePlanGrid.getSelectionModel().getSelection()[0];//得到选中的记录
    
    	var jsonArray= new Array();
		var rows = me.improvePlanGrid.store.getModifiedRecords();
		Ext.each(rows,function(item){
			if(item.data.content && item.data.content!=''){
				jsonArray.push(item.data);
			}
		});
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
			    	
			    	if(jsonArray.length>0){
						 FHD.ajax({//ajax调用
			    		     url : __ctxPath+ '/icm/improve/mergeimproveplanbatch.f',
			    		     params : {
			    		    	 improveId: me.improveId,
			    		    	 modifiedRecord:Ext.encode(jsonArray)
			    			 },
			    			 callback: function (data){		                    			
								if (data) { //保存成功
									FHD.ajax({
										url:__ctxPath+'/icm/rectify/removeimprovePlanFile.f',
										params:{
											improvePlanId:improvePlanId 
											},
										callback:function(data){
											if(data){
												Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
												me.improvePlanGrid.store.load();
											}else {
											    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
											}
										}
									}); 
								} else {
								    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
								}
							}
			    		});
					}else{
						FHD.ajax({
							url:__ctxPath+'/icm/rectify/removeimprovePlanFile.f',
							params:{
								improvePlanId:improvePlanId 
								},
							callback:function(data){
								if(data){
									Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
									me.improvePlanGrid.store.load();
								}else {
								    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
								}
							}
						}); 
					}
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
        //var length = me.improvePlanGrid.getSelectionModel().getSelection().length;
    },
    initComponent: function(){
    	var me = this;
    	
    	me.improvePlanGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
			multiSelect:false,
			autoScroll:true,
			searchable:false,
			pagable: false,
			checked:false,
			border: me.border,
			//url: __ctxPath+'/icm/improve/findimproveplanlistbyimproveid.f?improveId='+me.improveId,
			cols:[{dataIndex:'id',hidden:true},
				{header:'fileId',dataIndex:'fileId',hidden:true},
				{header:'责任部门', dataIndex: 'orgName',  sortable: false},
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
				{header:'方案内容<font color=red>*</font>', dataIndex: 'content', sortable: false, flex:2, editor:{xtype: 'textfield'} , renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						if(value){
							metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
							return value;  
						}else{
							metaData.tdAttr = 'data-qtip="请填写“方案内容”" style="background-color:#FFFBE6"';
						}
					}
				},
				{header:'开始日期', dataIndex: 'planStartDate', sortable: false, editor:new Ext.form.DateField({format:'Y-m-d'}), renderer: function(value, metaData, record, colIndex, store, view) { 
	                    metaData.tdAttr = 'style="background-color:#FFFBE6"';
	                    if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
	                    } else {
	                        return value;
	                    }
					}
				},
				{header:'结束日期', dataIndex: 'planEndDate', sortable: false, editor:new Ext.form.DateField({format:'Y-m-d'}), renderer: function(value, metaData, record, colIndex, store, view) { 
					  metaData.tdAttr = 'style="background-color:#FFFBE6"';
					  if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
	                    } else {
	                        return value;
	                    }
					}
				},
				{header:'附件', dataIndex: 'file',sortable: false,width:125, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						if(value){
							var valueNew = value.substring(0,value.length>5?5:value.length);
							if(value.length>5){
								valueNew +="...";
							}
							return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').downloadImprovePlanRelaFile()\">"+valueNew+"</a>&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').deleteImprovePlanRelaFile()\"><font class='icon-close' style='cursor:pointer;'>&nbsp&nbsp&nbsp&nbsp</font></a>";  
						}else{
							return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').uploadImprovePlanRelaFile()\">上传</a>";  
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
			me.improvePlanGrid.store.proxy.url=__ctxPath + '/icm/improve/findimproveplanlistbyimproveid.f';
			me.improvePlanGrid.store.on('beforeload', function (store, options) {
				var improveId = {improveId: me.improveId };
		        Ext.apply(store.proxy.extraParams, improveId);
	    	});
			me.improvePlanGrid.store.load();
		}
    }
});