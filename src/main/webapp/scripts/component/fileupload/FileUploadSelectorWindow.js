Ext.define('FHD.ux.fileupload.FileUploadSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.FileUploadSelectorWindow',
	requires:['FHD.ux.fileupload.FileTypeTree','FHD.ux.fileupload.FileUploadWindow'],
	
    height: 500,
    width: 720,
    modal: true,
    collapsible:true,
    maximizable: true,
	layout: {
       type: 'border'
    },
    
    values:null,
    tree:null,
    showGrid:null,
    selectGrid:null,
    buttons:null,
    
    initComponent: function() {
        var me = this;
    	
		if(me.single){
			me.checkModel = 'single';
		}
		
		var ids=new Array();
		me.values.each(function(value){
			ids.push(value.data.id);
		})
		
		me.tree=Ext.widget('FileTypeTree',{
			region: 'west',
			title: $locale('filetypetree.root'),
			width:200,
			listeners: {
				select:function(rowModel,record,index){
					me.showGrid.store.proxy.extraParams={fileTypeId:record.data.id};
					me.showGrid.store.load();
				}
			}
	    });
		me.showGrid = Ext.create('FHD.ux.GridPanel',{
			flex: 1,
			url: __ctxPath + '/fileupload/page',
			checked:false,
			extraParams:{
				
			},
			cols:[
				{header: $locale('fileupdate.filename'), dataIndex: 'oldFileName', sortable: true, flex: 1,renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
				    }
				},
				{header: $locale('fileupdate.uploadtime'),dataIndex: 'uploadTime', width:140, sortable: true},
				{header: $locale('fileupdate.filetype'),dataIndex: 'fileTypeDictEntryName', width:60, sortable: true},
				{header: $locale('fileupdate.countnum'),dataIndex: 'countNum', width:60,align:'right', sortable: true},
				{
	        		xtype:'templatecolumn',
	        		header:$locale('fhd.common.operate'),
	        		dataIndex:"id",
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
	            				var selectGridRecord=me.selectGrid.getStore().getById(record.get("id"));
	            				if(null!=selectGridRecord){
	            					selectGridRecord.set("countNum",record.get("countNum"));
		            				me.selectGrid.getStore().commitChanges();
	            				}
	            			}
	            		}
	            	}
	        	}
			],
			tbarItems:[//菜单项
				{
					xtype : "tbtext",
					text : $locale('fileuploadselectorwindow.showgrid')
				}, '-',{
					text : $locale('fhd.common.add'),iconCls: 'icon-add',scope : this,handler:function(){
						Ext.create('FHD.ux.fileupload.FileUploadWindow',{
							callBack:function(values){
								me.showGrid.store.load();
								if(values!=null&&values.length>0){
									FHD.ajax({
										url:__ctxPath+'/fileupload/listMap',
										params:{ids:values},
										callback:function(files){
											Ext.Array.each(files,function(file){
												me.selectGrid.store.insert(me.selectGrid.store.count(),file);
											});
										}
									});
								}
							}
						}).show();
					}
				},{
					xtype : 'tbspacer'
				}
			],
			listeners: {
				select:function( rowModel ,record,index){
					var flag=true;
					me.selectGrid.store.each(function(record1){
						if(record.data.id==record1.data.id){
							flag=false;
							return false;
						}
					});
					if(flag){
						me.selectGrid.store.insert(me.selectGrid.store.count(),record);
					}
				}
	        }
		});
		
		me.selectGrid=Ext.widget('grid',{
	        flex: 1,
	        tbar:new Ext.Toolbar({
				height:25,
				items : [{
					xtype : "tbtext",
					text : $locale('fileuploadselectorwindow.selectgrid')
				},'-']
			}),
	        store:Ext.create('Ext.data.Store',{
        		idProperty: 'id',
			    fields:['id', 'oldFileName', 'uploadTime','fileType','countNum']
        	}),
	        columns: [
	            {
	                xtype: 'gridcolumn',
	                hidden:true,
	                dataIndex: 'id'
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'oldFileName',
	                text: $locale('fileupdate.filename'),
	                flex: 1,
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
				    }
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'uploadTime',
	                text: $locale('fileupdate.uploadtime'),
	                width:140
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'fileType',
	                text: $locale('fileupdate.filetype'),
	                width:60
	            },
	            {
	                xtype: 'gridcolumn',
	                dataIndex: 'countNum',
	                text: $locale('fileupdate.countnum'),
	                width:60,
	                align:'right'
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
	            				var showGridRecord=me.showGrid.getStore().getById(record.get("id"));
	            				if(null!=showGridRecord){
	            					showGridRecord.set("countNum",record.get("countNum"));
		            				me.showGrid.getStore().commitChanges();
	            				}
	            			}
	            		}
	            	}
	        	},
	            {
	            	xtype:'templatecolumn',
	            	text:$locale('fhd.common.delete'),
	            	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
	            	width:35,
	            	align:'center',
	            	listeners:{
	            		click:{
	            			fn:function(grid,d,i){
	            				grid.store.removeAt(i);
	            			}
	            		}
	            	}
	            }
	        ]
	    });
		
		me.buttons=[{
        	xtype:'button',
        	text:$locale('fhd.common.confirm'),
            handler:function(){
            	me.onSubmit(me.selectGrid.store);
            	me.close();
            }
        },{
        	xtype:'button',
        	text:$locale('fhd.common.close'),
        	style: {
            	marginLeft: '10px'
            },
            handler:function(){
            	me.close();
            }
        }];
        
        Ext.applyIf(me, {
		    items: [
		    	me.tree,
		    	{
	            	xtype:'container',
	            	region: 'center',
	            	layout: {
				        align: 'stretch',
				        type: 'vbox'
				    },
				    items: [
		                me.showGrid,
		                me.selectGrid
				    ]
	            }
		    ]
        });
        
        me.callParent(arguments);
        me.values.each(function(value){
	        me.selectGrid.store.insert(me.selectGrid.store.count(),value);
        });
        
    }

});