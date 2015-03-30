Ext.define('FHD.ux.icm.assess.AssessPlanSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.assessplanselectorwindow',
	
    height: 500,
    width: 720,
    title: '评价计划列表',
    multiSelect:true,//是否多选
    showSubCompany: false,
    layout: {
		type:'vbox',
		padding:'5',
		align:'stretch'
    },
    
    initComponent: function() {
        var me = this,companyId;
        
		Ext.define('AssessPlan', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'companyName','code', 'name','createTime']
		});
		if(!me.showSubCompany){
			companyId = __user.companyId;
		}
		me.sourceGrid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            border: false,
            flex:1,
            checked:false,
            url: __ctxPath + '/icm/assess/findAssessPlanListByParams.f?companyId='+companyId+'&dealStatus=F,A', //调用后台url
            cols: [
            	{dataIndex:'id',hidden:true},
            	{dataIndex:'companyId',hidden:true},
            	{header : '所属公司',dataIndex : 'companyName',sortable : true, flex:1}, 
	    		{header : '编号',dataIndex : 'code',sortable : true, flex:1}, 
	    		{header : '名称',dataIndex : 'name',sortable : true, flex : 1, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showPlanViewList('" + record.data.id + "','" + record.data.status + "','" + record.data.dealStatus +"')\" >" + value + "</a>"; 
					}
				}, 
	 			{header : '创建日期',dataIndex :'createTime',sortable : true}
			]
        });
        me.selectedGrid = 	{
            xtype: 'gridpanel',
            flex: 1,
            store:Ext.create('Ext.data.Store', {
				idProperty: 'id',
				model:'AssessPlan'
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
                	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;&nbsp;</font>',
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
	        	var rec = new AssessPlan({
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
    showPlanViewList:function(id,status,dealStatus){
    	var me=this;
    	
    	me.assessPlanPanel=Ext.create('FHD.view.icm.assess.form.AssessPlanPreview',{
    		assessPlanId:id,
			dealStatus:dealStatus
		});
		
		var win = Ext.create('FHD.ux.Window',{
			title:'评价计划信息',
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.assessPlanPanel],
			buttonAlign: 'center',
			buttons: [
			    { 
			    	text: '关闭',
			    	//iconCls: 'icon-control-fastforward-blue',
			    	handler:function(){
				    	win.close();
			    	}
			    }
			]
    	}).show();
    },
    setValue:function(selecteds){
    	var me = this;
    	Ext.each(selecteds,function(selected){
        	me.selectedGrid.store.insert(0,selected);
        });
    },
    onSubmit:Ext.emptyFn()
});

