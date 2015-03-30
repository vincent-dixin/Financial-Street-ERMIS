Ext.define('FHD.ux.icm.rectify.ImproveSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.improveselectorwindow',
	
    height: 500,
    width: 720,
    title: '整改计划列表',
    multiSelect:true,
    showSubCompany: false,
    layout: {
		type:'vbox',
		padding:'5',
		align:'stretch'
    },
    
    initComponent: function() {
        var me = this,companyId;
        
        Ext.define('Improve', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'code', 'name','createTime']
		});
		 
		if(!me.showSubCompany){
			companyId = __user.companyId;
		}
		me.sourceGrid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            border: false,
            checked:false,
            flex:1,
            url: __ctxPath + '/icm/improve/findImproveListBypage.f?companyId='+companyId+'&dealStatus=H,F', //调用后台url
            cols: [
            	{dataIndex:'id',hidden:true},
            	{dataIndex:'companyId',hidden:true},
            	{header : '所属公司',dataIndex : 'companyName',sortable: false, flex:1}, 
	    		{header : '编号',dataIndex : 'code',sortable: false, flex:1}, 
	    		{header : '名称',dataIndex : 'name',sortable: false, flex : 1, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
		    			metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').viewObject('" + record.get('id') + "')\">"+value+"</a>";
					}
				}, 
	 			{header : '创建日期',dataIndex :'createTime',sortable : false}
			]
        });
        me.selectedGrid = 	{
            xtype: 'gridpanel',
            flex: 1,
            store:Ext.create('Ext.data.Store', {
				idProperty: 'id',
				model:'Improve'
			}),
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'companyName',
                    flex:1,
                    text: '所属公司'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'code',
                    flex:1,
                    text: '编号'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'name',
                    flex:1,
                    text: '名称',
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
                    	metaData.tdAttr = 'data-qtip="'+value+'"'; 
						return value;					
				    }
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'createTime',
                    text: '创建日期'
                },
                {
                	xtype:'templatecolumn',
                	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                	listeners:{
                		click:{
                			fn:function(g,d,i){
                				g.store.removeAt(i);
                			}
                		}
                	}
                }
            ]
        };
        Ext.applyIf(me, {
            items: [me.sourceGrid,me.selectedGrid]
        });
        
        me.buttons =[{
        	xtype:'button',
        	text:$locale('fhd.common.confirm'),
            handler:function(){
            	me.onSubmit(me);
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
        me.callParent(arguments);
        
        me.sourceGrid.on('select',function(t,r,i,o){
        	if(Ext.isEmpty(me.selectedGrid.store.getById(r.data.id))){
        		if(!me.multiSelect) {
        			me.selectedGrid.store.removeAll();
        		}
	        	var rec = new Improve({
	    			id:r.data.id,
	    			companyName:r.data.companyName,
	    			code:r.data.code,
	    			name:r.data.name,
	    			createTime:r.data.createTime
	    		}); 
	    		me.selectedGrid.store.insert(0,rec);
        	}
        });
    },
    setValue:function(selecteds){
    	var me = this;
    	Ext.each(selecteds,function(selected){
        	me.selectedGrid.store.insert(0,selected);
        });
    },
    onSubmit:Ext.emptyFn(),
    viewObject: function(id){
    	var me = this;
    	var rectifyView = Ext.widget('rectifyview');
    	var defectrelaimprovegrid = Ext.widget('defectrelaimprovegrid',{
			editable:false
		});
    	var improveplanviewgrid = Ext.widget('improveplanviewgrid');
    	rectifyView.loadData(id);
    	defectrelaimprovegrid.loadData(id);
    	improveplanviewgrid.loadData(id);
    	Ext.create('FHD.ux.Window',{
			title:'预览',
			iconCls: 'icon-view',//标题前的图片
			layout : {
				type : 'fit'
			},
			items: [Ext.widget('panel',{
				autoScroll: true,
				bodyPadding:'0 3 3 3',
				items:[
					rectifyView,{
						xtype : 'fieldset',
						layout : {
							type : 'column'
						},
						defaults:{
							columnWidth:1
						},
						collapsed : false,
						collapsible : true,
						title : '缺陷列表',
						items:[defectrelaimprovegrid]
					},
					{
						xtype : 'fieldset',
						layout : {
							type : 'column'
						},
						defaults:{
							columnWidth:1
						},
						collapsed : false,
						collapsible : true,
						title : '方案列表',
						items:[improveplanviewgrid]
					}
				]
			})], 
			maximizable: true,
			resizable: false,
			autoScroll: true,
			listeners:{
				close : function(){
					me.reloadData();
				}
			}
		}).show();
    }
});