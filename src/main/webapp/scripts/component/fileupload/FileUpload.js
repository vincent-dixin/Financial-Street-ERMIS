function downloadFile(id){
	window.location.href=__ctxPath+"/sys/file/download.do?id="+id;
}
Ext.define('FHD.ux.fileupload.FileUpload', {
	extend : 'Ext.container.Container',
	alias : 'widget.FileUpload',
	requires:['FHD.ux.fileupload.FileUploadSelectorWindow'],
	
	layout: {
        type: 'column'
    },
    /*属性*/
    /**
     * 是否为只读
     * @type Boolean
     */
    readonly:false,
    /**
     * 显示模式：base,grid
     */
    showModel:'grid',
    /**
     * 调用窗口模式：upload,selector
     */
	windowModel:'upload',
	/**
	 * 是否多选:预留需求，暂不可用
	 * @type Boolean
	 */
	multiSelect:true,
	/**
	 * 高度
	 * @type Number
	 */
	height: 150,
	/**
	 * 当前值
	 * @type String
	 */
	value:'',
	/**
	 * 标示名称
	 * @type String
	 */
	labelText:'',
	/**
	 * 标示对齐方式
	 * @type String
	 */
	labelAlign:'right',
	/**
	 * 标示宽度
	 * @type Number
	 */
	labelWidth:80,
	/**
	 * 滚动条属性
	 * @type Boolean
	 */
	scroll:true,
	/*成员*/
	label:null,
	topbar:null,
	topbarDelButton:null,
	showEle:null,
	botton:null,
	field:null,
	/**
	 * 弹出窗口：默认未开启
	 * @type 
	 */
	selectorWindow:null,
	allowBlank:true,
	/*方法*/
	initValue:function(value){
		var me=this;
		if(value==null||value==""){
			value=me.field.getValue();
		}
		var ids=value.split(",");
		FHD.ajax({
        	url: __ctxPath + '/fileupload/listMap',
        	params:{ids:ids},
        	callback:function(fileUploadEntitys){
        		var ids=new Array();
        		me.showEle.store.removeAll();
        		Ext.Array.each(fileUploadEntitys,function(fileUploadEntity){
		    		ids.push(fileUploadEntity.id);
	        		me.showEle.store.insert(me.showEle.store.count(),fileUploadEntity);
        		})
				var value=ids.join(",");
				me.value = value;
				me.field.setValue(value);
        	}
        });
	},
    /**
     * 设定当前值
     * @param {} value设定值
     */
    setValue:function(value){
    	var me = this;
    	me.value = value;
		me.field.setValue(value);
    },
    /**
     * 设定当前值
     * @param {} values设定值Store格式
     */
	setStoreValue:function(values){
    	var me = this;
    	var ids=new Array();
		me.showEle.store.removeAll();
    	values.each(function(value){
    		ids.push(value.data.id);
    		me.showEle.store.insert(0,value);
    	});
    	var value=ids.join(",");
		me.value = value;
		me.field.setValue(value);
    },
    /**
     * 获得当前值
     * @return {当前值}
     */
    getValue:function(){
    	var me = this;
		me.value = me.field.getValue();
    	return me.value;
    },
	getStoreValue:function(){
    	var me = this;
		return me.showEle.store;
    },
    
    storesRemove:function(values){
    	var me = this;
    	Ext.Array.each(values,function(value){
	    	me.showEle.store.remove(value);
		});
    	var values=me.getStoreValue();
    	var ids=new Array();
    	values.each(function(value){
    		ids.push(value.data.id);
    	});
    	var value=ids.join(",");
		me.value = value;
		me.field.setValue(me.value);
    },
    
	storeRemove:function(value){
    	var me = this;
    	me.showEle.store.remove(value);
    	var values=me.getStoreValue();
    	var ids=new Array();
    	values.each(function(value){
    		ids.push(value.data.id);
    	});
    	var value=ids.join(",");
		me.value = value;
		me.field.setValue(value);
    },
    
	/**
	 * 初始化方法
	 */
    initComponent: function() {
        var me = this;
		
        Ext.define('File', {
		    extend: 'Ext.data.Model',
		    fields:['id','oldFileName', 'uploadTime', 'fileType','countNum']
		});
        
        /*初始化分组件*/
        
		me.field=Ext.widget('textfield',{
			hidden:true,
	        name:me.name,
	        allowBlank:me.allowBlank,
	        listeners:{
				change:function (field,newValue,oldValue,eOpts ){
					if(newValue!=oldValue){
						var ids=newValue.split(",");
						FHD.ajax({
				        	url: __ctxPath + '/fileupload/listMap',
				        	params:{ids:ids},
				        	callback:function(fileUploadEntitys){
				        		var ids=new Array();
				        		me.showEle.store.removeAll();
				        		Ext.Array.each(fileUploadEntitys,function(fileUploadEntity){
						    		ids.push(fileUploadEntity.id);
					        		me.showEle.store.insert(me.showEle.store.count(),fileUploadEntity);
				        		})
								me.showEle.store.sort('oldFileName', 'ABS');
								var value=ids.join(",");
								me.value = value;
				        	}
				        });
					}
				}
		    }
        });
        
        
		if(me.showModel=='grid'){
			if(!me.multiSelect){
	        	me.height=72;
	        }
        	me.topbarDelButton=Ext.create('Ext.button.Button',{
				iconCls : "btn-del",
            	text:$locale("fhd.common.del"),
            	iconCls : "icon-del",
				disabled:true,
				handler : function(){
					var selects=me.showEle.getSelectionModel().getSelection();
					me.storesRemove(selects);
				}
			});
			var topbarItems=new Array();
			if(me.labelText!=null&&me.labelText!=""){
				topbarItems.push({
					xtype : "tbtext",
					html : me.labelText
				});
				topbarItems.push('-');
			}
			if(!me.readonly){
				topbarItems.push({
					text:$locale('fhd.common.add'),
	    			iconCls: 'icon-add',
					xtype : "button",
					handler : function(){
		            	if(me.windowModel=="upload"){
		            		Ext.create('FHD.ux.fileupload.FileUploadWindow',{
								values:me.getStoreValue(),
								callBack:function(values){
									me.setValue(values);
								}
							}).show();
		            	}else if(me.windowModel=="selector"){
							me.selectorWindow= Ext.create('FHD.ux.fileupload.FileUploadSelectorWindow',{
								values:me.getStoreValue(),
								onSubmit:function(setStoreValue){
									me.setStoreValue(setStoreValue);
								}
							}).show();
		            	}
				    }
				});
				topbarItems.push(me.topbarDelButton);
			}
			if(topbarItems.length>0){
	        	me.topbar = new Ext.Toolbar({
	        		height:25,
					items : topbarItems
				});
			}
			
			me.showEle=Ext.widget('grid',{
				/*title:'附件',*/
	        	tbar:me.topbar,
	            height: me.height,
	            columnWidth: 1,
	            selModel:Ext.create('Ext.selection.CheckboxModel'),
	        	columns:[
	        	{
	        		xtype: 'gridcolumn',
	                dataIndex: 'id',
	                hidden:true
	        	},{
	        		xtype: 'gridcolumn',
	        		text:$locale('fileupdate.filename'),
	                dataIndex: 'oldFileName',
	                flex:1,
	                renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
				    }
	        	},{
	        		xtype: 'gridcolumn',
	        		text:$locale('fileupdate.countnum'),
	        		align:'right',
	        		width:70,
	                dataIndex: 'countNum'
	        	},{
	                xtype:'templatecolumn',
	        		text:$locale('fhd.common.operate'),
	            	tpl:'<span class="icon-download-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</span>',
	            	width:35,
	            	align:'center',
	            	listeners:{
	            		click:{
	            			fn:function(grid,d,i){
	            				var record=grid.getStore().getAt(i);
	            				downloadFile(record.get("id"));
	            				record.set("countNum",record.get("countNum")+1);
	            				grid.getStore().commitChanges();
	            			}
	            		}
	            	}
	        	}],
	        	store:Ext.create('Ext.data.Store',{
	        		idProperty: 'id',
				    fields:['id', 'oldFileName', 'countNum']
	        	}),
	        	listeners: {
		            selectionchange: function(model, selects){
		            	if(selects.length>0){
		            		me.topbarDelButton.setDisabled(false);
		            	}else{
		            		me.topbarDelButton.setDisabled(true);
		            	}
		            }
		        }
	        });
	        
	        Ext.applyIf(me, {
	            items: [
	            	me.showEle,
	                me.field
	            ]
	        });
        }else if(me.showModel=='base'){
        	if(!me.multiSelect){
	        	me.height=22;
	        	me.scroll='none';
	        }
	        var meItems=new Array();
        	if(me.labelText){
				me.label=Ext.widget('label',{
		    		width:me.labelWidth,
		    		html:me.labelText + ':',
		    		height: me.height,
		    		style:{
		    			marginRight: '5px',
		    			textAlign: me.labelAlign
		    		}
		    	});
        	}
	        
        	var showEleColumns=new Array();
        	showEleColumns.push({
        		xtype: 'gridcolumn',
				width : 5,
				dataIndex : "sn",
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					record.data["sn"] = rowIndex + 1;
					store.commitChanges();
					return record.data["sn"];
				}
			});
        	showEleColumns.push({
        		xtype: 'gridcolumn',
                dataIndex: 'id',
                hidden:true
        	});
			showEleColumns.push({
        		xtype: 'gridcolumn',
                dataIndex: 'oldFileName',
                flex:1,
                renderer:function(value, metaData, record, rowIndex, colIndex, store){
					return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
			    }
        	});
			showEleColumns.push({
        		xtype:'templatecolumn',
            	tpl:'<span class="icon-download-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</span>',
            	width:35,
            	align:'center',
            	listeners:{
            		click:{
            			fn:function(grid,d,i){
            				downloadFile(grid.getStore().getAt(i).getData().id);
            			}
            		}
            	}
        	});
        	if(!me.readonly){
		        me.botton=Ext.widget('button',{
		            iconCls:'icon-page',
		            width: 22,
		            height: 22,
		            handler:function(){
		            	if(me.windowModel=="upload"){
		            		Ext.create('FHD.ux.fileupload.FileUploadWindow',{
								values:me.getStoreValue(),
								callBack:function(values){
									me.setValue(values);
								}
							}).show();
		            	}else if(me.windowModel=="selector"){
							me.selectorWindow= Ext.create('FHD.ux.fileupload.FileUploadSelectorWindow',{
								values:me.getStoreValue(),
								onSubmit:function(setStoreValue){
									me.setStoreValue(setStoreValue);
								}
							}).show();
		            	}
				    }
	        	});
				showEleColumns.push({
	            	xtype:'templatecolumn',
	            	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
	            	width:35,
	            	align:'center',
	            	listeners:{
	            		click:{
	            			fn:function(grid,d,i){
	            				var select=grid.store.getAt(i);
	            				me.storeRemove(select);
	            			}
	            		}
	            	}
	            });
        	}
			me.scroll='none',
			me.showEle=Ext.widget('grid',{
				scroll:me.scroll,
				disableSelection:true,
	        	hideHeaders:true,
	            height: me.height,
	            columnWidth: 1,
	        	columns:showEleColumns,
	        	store:Ext.create('Ext.data.Store',{
	        		idProperty: 'id',
				    fields:['id', 'oldFileName', 'countNum']
	        	})
	        });
	
	        meItems.push(me.field);
	        meItems.push(me.label);
	        meItems.push(me.showEle);
	        meItems.push(me.botton);
	        Ext.applyIf(me, {
	            items: meItems
	        });
        }
		me.callParent(arguments);
		me.initValue(me.value);
    }

});
	