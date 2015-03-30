Ext.define('FHD.ux.fileupload.FileUploadWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.FileUploadWindow',
	
    height: 300,
    width: 500,
    modal: true,
    collapsible:true,
    maximizable: true,
    multiSelect: true,
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    border:false,
    title: $locale('fileuploadwindow.labeltext'),
    values:null,
    fileArray:new Array(),
    fileTypeArray:[ "jpg", "jpeg", "gif", "txt" , "doc","docx", "xls","xlsx", "mp3", "wma", "m4a", "rar", "zip" ],
    
	fileStatus:{
		noUpload:$locale('fileuploadwindow.filestatus.noupload'),
		isBreak:$locale('fileuploadwindow.filestatus.isbreak'),
		uploading:$locale('fileuploadwindow.filestatus.uploading'),
		failure:$locale('fileuploadwindow.filestatus.failure'),
		isUpload:$locale('fileuploadwindow.filestatus.isupload')
	},
	fileComment:{
		noUpload:'',
		isBreak:'<span class="icon-exclamation-min">&nbsp&nbsp&nbsp&nbsp</span>',
		uploading:'<span class="icon-file-uploading">&nbsp&nbsp&nbsp&nbsp</span>',
		failure:'<span class="icon-error-min">&nbsp&nbsp&nbsp&nbsp</span>',
		isUpload:'<span class="icon-accept-min">&nbsp&nbsp&nbsp&nbsp</span>'
	},
    
	chooseWay:[
		{value:'dataBase',name:$locale('fileuploadwindow.chooseway.database')},
		{value:'fileDir',name:$locale('fileuploadwindow.chooseway.filedir')}
	],
	
    progressBar:null,
    fileGrid:null,
    fileGridBbar:null,
    form:null,
    
    getValues:function(){
    	var me=this;
		var ids=new Array();
		me.fileGrid.store.each(function(r){
			if(r.data.status==me.fileStatus.isUpload){
				ids.push(r.data.id);
			}
		});
		return ids;
    },
    
    createAddBotton:function(){
    	var me=this;
    	var addBotton={
    		id:Ext.id(),
        	xtype:'filefield',
        	name:'file',
        	main:me,
        	buttonOnly: true,
        	buttonText:$locale('fhd.common.add'),
        	buttonConfig: {
	        	iconCls: 'icon-add'
            },
        	listeners: {
	            change: me.addFile
	        }
		};
		
    	return addBotton;
    },
    
    addFile:function(fb, value){
    	var me=this.main;
    	var prefix=value.substring(value.lastIndexOf(".")+1);
    	if(Ext.Array.indexOf(me.fileTypeArray,prefix.toUpperCase())!=-1){
	    	me.updateProgress(me.fileArray.length,0);
			var newAddBotton=me.createAddBotton();
			me.fileGridBbar.insert(0,newAddBotton);
			fb.hide();
			me.fileArray.push(fb);
			var file= new File({
				id:fb.id,
				status:me.fileStatus.noUpload,
				oldFileName:value,
				chooseWay:me.chooseWay[0].value,
				comment:me.fileComment.noUpload
			});
			me.fileGrid.store.insert(me.fileGrid.store.count(),file);
    	}else if(value!=null&&value!=""){
    		Ext.Msg.alert($locale('fileuploadwindow.filetypeerror.title'),$locale('fileuploadwindow.filetypeerror.text')+me.fileTypeArray.join(","));
    	}
    },
    setLoadIcon:function(loading){
    	var me=this;
    	if(loading){
    		me.fileGrid.fileGridBbarLoadIconButton.setIconCls("icon-loading");
    	}else{
    		me.fileGrid.fileGridBbarLoadIconButton.setIconCls("icon-done");
    	}
    },
    
    updateProgress:function(sum,finishNum){
    	var me=this;
    	var text=null;
    	if(finishNum==0){
    		text=$locale('fileuploadwindow.updateprogress.null');
    	}else if(finishNum==sum){
    		text=$locale('fileuploadwindow.updateprogress.finish');
    	}else{
    		text=$locale('fileuploadwindow.updateprogress.updataing');
    	}
    	me.progressBar.updateProgress(finishNum/sum,text,true);
    },
    
    uploadFile :function(sum,finishNum,failureNum){
    	var me=this;
    	me.setLoadIcon(true);
    	me.updateProgress(sum,finishNum)
    	if(me.fileArray.length>failureNum){
	    	var file=me.fileArray[failureNum];
	    	var record=me.fileGrid.store.getById(file.id);
			record.set("status",me.fileStatus.uploading);
			record.set("comment",me.fileComment.uploading);
			me.fileGrid.getStore().commitChanges();
			var chooseWay=Ext.create('Ext.form.field.Text',{name:'chooseWay',value:record.data.chooseWay});
			me.form.insert(0,chooseWay);
			me.form.insert(0,file);
			me.form.submit({
				url:__ctxPath+'/fileupload/upload',
				method:'POST',
				success:function(form, action){
					var value=Ext.JSON.decode(action.response.responseText).id;
					if(value!=""&&value!=null){
						record.set("id",value);
						record.set("status",me.fileStatus.isUpload);
						record.set("comment",me.fileComment.isUpload);
						Ext.Array.remove(me.fileArray,file);
					}else{
						record.set("status",me.fileStatus.failure);
						record.set("comment",me.fileComment.failure);
						failureNum=failureNum+1;
					}
					me.fileGrid.getStore().commitChanges();
					me.form.remove(file);
					me.form.remove(chooseWay);
					finishNum=finishNum+1;
					me.uploadFile(sum,finishNum,failureNum);
				},
				failure:function(form, action){
					record.set("status",me.fileStatus.failure);
					record.set("comment",me.fileComment.failure);
					me.fileGrid.getStore().commitChanges();
					me.form.remove(file);
					me.form.remove(chooseWay);
					failureNum=failureNum+1;
					finishNum=finishNum+1;
					me.uploadFile(sum,finishNum,failureNum);
				}
			});
    	}else{
        	me.fileGridBbar.remove(me.fileGridBbar.down("[name='file']"));
        	var newAddBotton=me.createAddBotton();
			me.fileGridBbar.insert(0,newAddBotton);
        	me.fileGrid.fileGridBbarRefreshButton.setDisabled(false);
        	me.fileGrid.fileGridBbarStartUpdateButton.setDisabled(false);
        	me.fileGrid.fileGridBbarAcceptButton.setDisabled(false);
    		me.setLoadIcon(false);
    		var selects = me.fileGrid.getSelectionModel().getSelection();
    		if(selects.length>0){
	        	me.fileGrid.fileGridBbarRemoveButton.setDisabled(false);
    		}
    		if(!me.multiSelect){
    			if(me.fileGrid.store.count()>0){
    				me.fileGrid.down("[name='file']").setDisabled(true);
    			}else{
    				me.fileGrid.down("[name='file']").setDisabled(false);
    			}
    		}
    	}
    },
    
    callBack:function(values){
    },
    
    initComponent: function() {
        var me = this;
        
        me.fileArray=new Array();
        me.fileTypeArray=new Array();
        FHD.ajax({
        	url: __ctxPath + '/sys/dic/listMap.f',
        	params:{parentId:null,dictTypeId:'0file_type',isLeaf:true},
        	callback:function(fileTypes){
        		Ext.Array.each(fileTypes,function(fileType){
        			if(fileType.value!=null){
		        		me.fileTypeArray.push(fileType.value.toUpperCase());
        			}
        		});
        	}
        });
    	me.form = Ext.widget('form',{
			hidden:true,
    		fileUpload:true
		});
		Ext.define('File', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'status', 'oldFileName','chooseWay','comment']
		});
		
		me.progressBar=Ext.create('Ext.ProgressBar',{
			x : 0,
			y : 0,
			anchor : "0",
			value : 0,
			text : $locale('fileuploadwindow.updateprogress.null')
		});
		/*定义底部工具栏*/
		fileGridBbarRemoveButton=Ext.create('Ext.Button',{
			text:$locale("fhd.common.del"),
        	iconCls : "icon-del",
        	disabled:true,
            handler:function(){
            	var selects = me.fileGrid.getSelectionModel().getSelection();
            	Ext.Array.each(selects,function(select){
                	Ext.Array.each(me.fileArray,function(file){
                		if(file.id==select.data.id){
		                	Ext.Array.remove(me.fileArray,file);
		                	return true;
                		}
                	});
            		me.fileGrid.store.remove(select);
            	});
            	if(!me.multiSelect){
            		me.fileGridBbar.remove(me.fileGridBbar.down("[name='file']"));
                	var newAddBotton=me.createAddBotton();
					me.fileGridBbar.insert(0,newAddBotton);
            	}
            }
		});
		fileGridBbarRefreshButton=Ext.create('Ext.Button',{
			text:$locale("fhd.common.reset"),
        	iconCls : "icon-arrow-refresh",
            handler:function(){
            	me.fileArray=new Array();
            	me.fileGrid.store.removeAll();
            	if(!me.multiSelect){
            		me.fileGridBbar.remove(me.fileGridBbar.down("[name='file']"));
                	var newAddBotton=me.createAddBotton();
					me.fileGridBbar.insert(0,newAddBotton);
            	}
            	me.fileGrid.fileGridBbarStartUpdateButton.setDisabled(true);
            }
		});
		fileGridBbarStartUpdateButton=Ext.create('Ext.Button',{
			text:$locale("fileuploadwindow.startupdate"),
        	iconCls : "icon-folder-upload",
        	disabled:true,
            handler:function(){
            	me.fileGrid.down("[name='file']").setDisabled(true);
            	me.fileGrid.fileGridBbarRemoveButton.setDisabled(true);
            	me.fileGrid.fileGridBbarRefreshButton.setDisabled(true);
            	me.fileGrid.fileGridBbarStartUpdateButton.setDisabled(true);
            	me.fileGrid.fileGridBbarAcceptButton.setDisabled(true);
            	var sum=me.fileArray.length;
            	var finishNum=0;
            	var failureNum=0;
            	me.uploadFile(sum,finishNum,failureNum);
            }
		});
		fileGridBbarLoadIconButton=Ext.create('Ext.Button',{
        	iconCls : "icon-done",
        	disabled:true
		});
		fileGridBbarAcceptButton=Ext.create('Ext.Button',{
			align:'',
        	text:$locale('fhd.common.confirm'),
        	iconCls:"icon-accept",
            handler:function(){
            	if(me.fileArray.length!=0){
                	Ext.MessageBox.confirm('警告', '还有文件未上传完毕，确认退出？', function showResult(btn){
				        if("yes"==btn){
		                	me.callBack(me.getValues());
		                	me.fileArray=new Array();
		                	me.fileGrid.store.removeAll();
		                	me.close();
				        }
				    });
            	}else{
                	me.callBack(me.getValues());
                	me.fileArray=new Array();
                	me.fileGrid.store.removeAll();
                	me.close();
            	}
            }
		});
		me.fileGridBbar=new Ext.Toolbar({
			items : [
				me.createAddBotton(),
	            fileGridBbarRemoveButton,
	            fileGridBbarRefreshButton,
	            fileGridBbarStartUpdateButton,
	            fileGridBbarLoadIconButton,
	            "->",
	            fileGridBbarAcceptButton
        	]
        });
		
		me.fileGrid=Ext.widget('grid',{
			fileGridBbarRemoveButton:fileGridBbarRemoveButton,
            fileGridBbarRefreshButton:fileGridBbarRefreshButton,
            fileGridBbarStartUpdateButton:fileGridBbarStartUpdateButton,
            fileGridBbarLoadIconButton:fileGridBbarLoadIconButton,
            fileGridBbarAcceptButton:fileGridBbarAcceptButton,
	        flex: 1,
	        plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
		        clicksToEdit: 1
		    })],
	        selModel:Ext.create('Ext.selection.CheckboxModel'),
	        store:Ext.create('Ext.data.Store',{
        		idProperty: 'id',
			    fields:['id','status', 'oldFileName','chooseWay', 'comment'],
			    listeners:{
			    	datachanged:function(store){
			    		if(store.count()>0){
			    			me.fileGrid.fileGridBbarStartUpdateButton.setDisabled(false);
			    		}else{
			    			me.fileGrid.fileGridBbarStartUpdateButton.setDisabled(true);
			    		}
			    		if(!me.multiSelect){
			    			if(store.count()>0){
			    				me.fileGrid.down("[name='file']").setDisabled(true);
			    			}else{
			    				me.fileGrid.down("[name='file']").setDisabled(false);
			    			}
			    		}
			    	}
			    }
        	}),
	        columns: [
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'id',
	                hidden:true
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'comment',
	                width:50,
	                align:"center"
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'oldFileName',
	                flex:1,
	                text: $locale('fileupdate.filename'),
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
				    }
	            },
	            {
	                dataIndex: 'chooseWay',
	                flex:1,
	                text: $locale('fileupdate.chooseway'),
                    editor: Ext.create('Ext.form.ComboBox',{
					    displayField: 'name',
					    valueField: 'value',
                    	store: Ext.create('Ext.data.Store', {
						    fields: ['value', 'name'],
						    data : me.chooseWay
					    })
		            }),renderer:function(value, metaData, record, rowIndex, colIndex, store){
		            	var name='';
		            	Ext.Array.each(me.chooseWay,function(data){
		            		if(value==data.value){
		            			name=data.name;
		            		}
		            	});
						return name;
					}
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'status',
	                text: $locale('fileuploadwindow.filestatus')
            	}
	        ],
	        tbar:me.progressBar,
	        bbar:me.fileGridBbar,
            listeners: {
	            selectionchange: function(model, selects){
	            	if(selects.length>0){
	            		me.fileGrid.fileGridBbarRemoveButton.setDisabled(false);
	            	}else{
	            		me.fileGrid.fileGridBbarRemoveButton.setDisabled(true);
	            	}
	            }
	        }
	    });
        
        Ext.applyIf(me, {
            items: [
	            me.fileGrid,
	            me.form
            ]
        });
        if(me.values!=null&&me.values!=""){
	        me.values.each(function(value){
				var file= new File({
					id:value.data.id,
					status:me.fileStatus.isUpload,
					oldFileName:value.data.oldFileName,
					chooseWay:me.chooseWay[0].value,
					comment:me.fileComment.isUpload
				});
				me.fileGrid.store.insert(me.fileGrid.store.count(),file);
			})
        }
        me.callParent(arguments);
    }

});