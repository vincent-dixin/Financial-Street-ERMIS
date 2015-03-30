/*
 * 样本测试的可编辑列表
 * */
 Ext.define('FHD.view.icm.assess.component.SampleTestEditGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.sampletesteditgrid',
	
	url: __ctxPath+'/icm/assess/findAssessSampleListBySome.f',
	extraParams:{
		assessResultId:''
	},
	
	cols:[],
	tbar:[],
	tbarItems:[],
	pagable:false,
	//region:'center',
	
	initComponent:function(){
    	var me=this;
    	
    	me.extraParams.assessResultId=me.assessResultId;
    	
    	me.isQualifiedStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
			    {'id' : 'Y','name' : '是'},
			    {'id' : 'N','name' : '否'},
			    {'id':'NAN','name':'不适用'}
			]
		});
    	//me.url= __ctxPath+'/icm/assess/findAssessSampleListBySome.f?assessResultId='+me.parameter.assessResultId,
    	me.cols=[
		    {header:'状态', dataIndex: 'type', sortable: false,flex:1},
		    {header:'样本编号<font color=red>*</font>', dataIndex: 'code', sortable: false,flex:1,editor: {allowBlank: false},
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="补充样本的样本编号需要手动填写" style="background-color:#FFFBE6"';
					}
				}
		    },
		    {header:'样本名称', dataIndex: 'name',sortable: false,flex:1,editor: {allowBlank: true},emptyCellText:'<font color="#808080">请填写</font>',
			    renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="样本名称为了方便查找样本，可以填写，不是必填" style="background-color:#FFFBE6"';
					}
				}
		    },
		    {header:'是否合格<font color=red>*</font>', dataIndex: 'isQualified', sortable: false,emptyCellText:'<font color="#808080">请选择</font>',
		    	editor:new Ext.form.ComboBox({
		    		store :me.isQualifiedStore,
		    		valueField : 'id',
		    		displayField : 'name',
		    		selectOnTab: false,
		    		allowBlank : false,
		    		editable : false,
			    	listeners:{
			    		change: function(t, newValue, oldValue, e){
			    			if('NAN' == newValue){
			    				Ext.getCmp('icm_assess_sample_add').setDisabled(false);
			    			}else{
			    				Ext.getCmp('icm_assess_sample_add').setDisabled(true);
			    			}
			    		}
			    	}
		    	}),
		    	renderer:function(value,metaData,record,colIndex,store,view) {
		    		metaData.tdAttr = 'style="background-color:#FFFBE6"';
		    		var index = me.isQualifiedStore.find('id',value);
		    		var record = me.isQualifiedStore.getAt(index);
		    		if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
							metaData.tdAttr = 'data-qtip="样本“是否合格”,必填" style="background-color:#FFFBE6"';
						}
					}
		    	}
		    },
		    {header:'说明', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: true},emptyCellText:'<font color="#808080">请填写</font>',
		    	renderer:function(value,metaData,record,colIndex,store,view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'" style="background-color:#FFFBE6"';
						return value;  
					}else{
						metaData.tdAttr = 'data-qtip="样本“是否合格”为“否”的样本，“说明”必填" style="background-color:#FFFBE6"';
					}
				}
		    },
		    {header:'补充样本的来源样本', dataIndex: 'sourceSample',sortable: false,flex:2},
		    {header:'附件', dataIndex: 'file',sortable: false,flex:3,
			    renderer : function(value, metaData, record, colIndex, store, view) { 
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						var valueNew = value.substring(0,value.length>10?10:value.length);
						if(value.length>10){
							valueNew +="...";
						}
			    		return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').downloadFile('" + record.data.fileId + "')\" >"+valueNew+" </a> "
			    			+" <a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').delFile('" + record.data.assessSampleId + "')\" ><font class='icon-close' style='cursor:pointer;'>&nbsp&nbsp&nbsp&nbsp</font></a>";
			    	}else{
			    		metaData.tdAttr = 'data-qtip="不合格的样本必须上传附件"'; 
			    		return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').uploadFile()\" >上传</a> ";
			    	}
			    }
			},
		    /*
			{
                header: FHD.locale.get('fhd.formula.operate'),
    		    xtype:'actioncolumn',
    		    dataIndex: 'fileId',
    		    flex: 1,
    		    items: [{
    		        icon: __ctxPath + '/images/icons/download.png',
    		        tooltip: '下载附件',
    		        handler: me.downloadFile
    		    }]
            },
            */
			{dataIndex:'fileId',hidden:true},
			{dataIndex:'isHasSupplement',hidden:true},
			{dataIndex:'assessSampleId',hidden:true}
		];
   
		me.tbarItems=[
			{iconCls: 'icon-add',id:'icm_assess_sample_add',text:'补充样本',tooltip: '针对"不适用"的样本添加补充样本',handler:me.addSample, disabled : true, scope : this},'-',
			{iconCls: 'icon-del',id:'icm_assess_sample_del',text:'删除补充样本',tooltip: '选择需要删除的补充样本进行删除',handler:me.delSample, disabled : true, scope : this},'-',
			//{iconCls: 'icon-folder-upload',id:'icm_assess_sample_upload',tooltip: '上传样本附件',handler:me.uploadFile, disabled : true, scope : this},'-',
			{iconCls: 'icon-ibm-icon-scorecards',id:'icm_assess_sample_batch',text:'批量设置',tooltip: '批量设置样本是否合格',handler:me.batchSet, disabled : true, scope : this}
		];
    	me.on('selectionchange',function(m) {
    		var selections = me.getSelectionModel().getSelection();
    		var length = selections.length; 
    		Ext.getCmp('icm_assess_sample_del').setDisabled(length === 0);
    		Ext.getCmp('icm_assess_sample_batch').setDisabled(length === 0);
            if(length != 1){
            	Ext.getCmp('icm_assess_sample_add').setDisabled(true); 
		  		//Ext.getCmp('icm_assess_sample_upload').setDisabled(true);
            }else{
            	if('NAN' == selections[0].data.isQualified){
            		Ext.getCmp('icm_assess_sample_add').setDisabled(false);
            	}
		  		//Ext.getCmp('icm_assess_sample_upload').setDisabled(false);
            }
	 	});
    	me.on('edit',function(editor,e){
	 		//获取参数值
	        var eid = e.record.data.assessSampleId;
	        var afterValue = e.value;
	        var originalValue = e.originalValue;
	       	var row = e.rowIdx;
	       	var column = e.colIdx;
	       	var gridStore = e.grid.store;
			//取出当前store中的数据条目数
	       	var storeCount = e.grid.store.getCount();
	       	
	       	if('code'==e.field && e.record.data.type=='自动'){
				Ext.Msg.alert('提示','自动生成的样本编号不可编辑!');
				e.record.data[e.field] = e.originalValue;
				e.record.commit();
				return false;
   			}
	       	
	  		var list = e.grid.store.data.items;
	       	for(var i=0;i<list.length;i++){
	       		if(eid != list[i].data.assessSampleId){
	       			if('code'==e.field){
	       				if(afterValue==gridStore.getAt(i).get('code')){
	       					Ext.Msg.alert('提示','样本编号已经存在!');
	       					e.record.data[e.field] = e.originalValue;
	       					e.record.commit();
	       					return false;
	       				}
	       			}
	       		}
	       	}
	    }); 
    	me.callParent(arguments);
    },
    colInsert: function (index, item) {
        if (index < 0) return;
        if (index > this.cols.length) return;
        for (var i = this.cols.length - 1; i >= index; i--) {
            this.cols[i + 1] = this.cols[i];
        }
        this.cols[index] = item;
    },
    addSample:function (){
    	var me=this;
    	
		var selection=me.getSelectionModel().getSelection();
		var assessSampleId=selection[0].get('assessSampleId');
		FHD.ajax({
			url:__ctxPath+'/icm/assess/mergeSample.f',
			params:{
				assessSampleId:assessSampleId
			},
			callback:function(data){
				if(data){
					me.store.commitChanges();
					//Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					//先保存样本列表数据
					var jsonArray=[];
			 		me.store.each(function(item){
			 			jsonArray.push(item.data);
			 		});
			 		
			 		FHD.ajax({
						url:__ctxPath +'/icm/assess/mergeAssessSampleBatch.f',
			            params: {
			            	jsonString:Ext.encode(jsonArray),
			            	assessResultId:me.assessResultId
			            },
			            callback: function (data) {
			            	if(data){
								//刷新样本列表
			            		me.store.load();
			            	}
			            }
					});
				}
			}
		});
	},
	delSample:function() {
    	var me = this;
    	
    	var selections = me.getSelectionModel().getSelection();
    	if(0 == selections.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.msgDel'));
    	}else{
    		Ext.MessageBox.show({
                title: FHD.locale.get('fhd.common.delete'),
                width: 260,
                msg: FHD.locale.get('fhd.common.makeSureDelete'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn == 'yes') {
                    	var ids='';
                        
                    	var flag = false;
                        Ext.Array.each(selections, function (item) {
                        	if('自动' == item.get('type')){
                        		flag = true;
                        	}else{
                        		ids += item.get('assessSampleId') + ",";
                        	}
                        });
                        
                        if(flag){
                        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '"自动"状态的样本不允许删除!');
                        	return;
                        }
                        
    					if(ids.length>0){
    						ids = ids.substring(0,ids.length-1);
    					}
                        FHD.ajax({
                            url: __ctxPath + '/icm/assess/removeSamplesByIds.f',
                            params: {
                                ids: ids
                            },
                            callback: function (data) {
                                if (data) {
                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                    me.store.load();
                                }else{
                                	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                                }
                            }
                        });
                    }
                }
            });
    	}
	},
    uploadFile:function(){
		var me=this;
		
		var selection=me.getSelectionModel().getSelection();
		var assessSampleId=selection[0].get('assessSampleId');
		
		Ext.create('FHD.ux.fileupload.FileUploadWindow',{
			multiSelect: false,
			callBack:function(value){
				if(value!=null && value.length>0){
					FHD.ajax({
						url:__ctxPath+'/icm/assess/mergeAssessSampleRelaFile.f',
						params:{
							fileId:value, 
							assessSampleId:assessSampleId
						},
						callback:function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								//me.store.load();
								//渲染附件列表：附件源文件名称
								selection[0].set("fileId",data.fileId);
								selection[0].set("file",data.fileName);
								//"<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').downloadFile('" + record.data.fileId + "')\" >"+value+"</a>"
								me.getStore().commitChanges();
							}
						}
					}); 
				}
			}
		}).show();
	},
	downloadFile:function(){
		var me=this;
		
		var selection=me.getSelectionModel().getSelection();
		var assessSampleId = selection[0].get('assessSampleId');
        var fileId = selection[0].get('fileId');
        
        if(fileId != ''){
        	window.location.href=__ctxPath+"/sys/file/download.do?id="+fileId;
        }else{
        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'该样本没有上传附件!');
        }
	},
	batchSet:function(){
		var me=this;
		
		var selections = me.getSelectionModel().getSelection();
    	if(0 == selections.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.msgDel'));
    	}else{
    		var isQualified = Ext.create('Ext.form.ComboBox', {
    		    fieldLabel: '是否合格',
    		    store: me.isQualifiedStore,
    		    queryMode: 'local',
    		    displayField: 'name',
    		    valueField: 'id',
    		    margin: '7 30 10 30',
    		    allowBlank : false,
    		    name:'isQualified'
    		});
    		
    		me.batchSetFormPanel = Ext.create('Ext.form.Panel', {
    		    //title: 'Simple Form',
    		    //bodyPadding: 5,
    			//margin: '10 10 10 10',
    		    layout: 'anchor',
    		    defaults: {
    		        anchor: '100%'
    		    },
    		    items: [isQualified]
    		});
    		
    		var viewWindow = Ext.create('Ext.window.Window',{
    			title:'批量设置样本是否合格',
    			modal:true,//是否模态窗口
    			height: 200,
    			width: 300,
    			layout : 'fit',
    			draggable : false,
    			collapsible:false,
    			//maximizable:true,//（是否增加最大化，默认没有）
    			items:[me.batchSetFormPanel],
    			buttonAlign:'center',
    			buttons: [
    			    { 
    			    	text: '保存',
    			    	//iconCls: 'icon-control-stop-blue',
    			    	handler:function(){
    			    		var isQualified;
    			    		var form = me.batchSetFormPanel.getForm();
    			    		var vobj = form.getValues();
        		            if (form.isValid()) {
        		            	isQualified = vobj.isQualified
        		            }
    			    		
    			    		var jsonArray=[];
    			    		Ext.Array.each(selections, function (item) {
    			    			jsonArray.push(item.data);
                            });
                            FHD.ajax({
                                url: __ctxPath + '/icm/assess/batchSaveSamplesIsQualified.f',
                                params: {
                                	jsonString:Ext.encode(jsonArray),
                                	isQualified: isQualified
                                },
                                callback: function (data) {
                                    if (data) {
                                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                        me.store.load();
                                        viewWindow.close();
                                    }else{
                                    	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                                    }
                                }
                            });
    			    	}
    			    }/*,
    			    { 
    			    	text: '关闭',
    			    	//iconCls: 'icon-control-fastforward-blue',
    			    	handler:function(){
    				    	viewWindow.close();
    			    	}
    			    }
    			    */
    			]
        	}).show();
    	}
	},
	delFile:function(){
		var me=this;
		
		var selection=me.getSelectionModel().getSelection();
		var assessSampleId=selection[0].get('assessSampleId');
		FHD.ajax({
			url:__ctxPath+'/icm/assess/removeAssessSampleRelaFile.f',
			params:{
				assessSampleId:assessSampleId
			},
			callback:function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					selection[0].set("file", "");
					selection[0].set("fileId", "");
					me.getStore().commitChanges();
				}
			}
		}); 
	},
	reloadData:function(){
		var me=this;
		
		me.store.load();
	}
});