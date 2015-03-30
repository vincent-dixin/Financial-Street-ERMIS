Ext.define('FHD.ux.icm.defect.DefectSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.defectselectorwindow',
	
    height: 500,
    width: 720,
    title: '缺陷列表',
    multiSelect:true,
    layout: {
		type:'vbox',
		padding:'5',
		align:'stretch'
    },
    
    initComponent: function() {
        var me = this;
        
        Ext.define('Defect', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'code', 'desc','createTime']
		});
		me.sourceGrid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            border: false,
            flex:1,
            checked: false,
            url: __ctxPath + '/icm/defect/findDefectListBypage.f?companyId='+__user.companyId+'&status=P&dealStatus=N', //调用后台url
            cols: [
            	{dataIndex:'id',hidden:true},
                {header : '编号',hidden:true,dataIndex : 'code',sortable : false,flex : 1}, 
                {header : '缺陷描述',dataIndex : 'desc',sortable : false,flex : 1, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
                		//TODO缺陷弹出详细暂时没有
						//metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	//return "<a href=\"javascript:void(0);\" onclick=\"viewDefect('" + record.get('id') + "')\">"+value+"</a>";  
						return value;
					}
				},
                {header : '缺陷等级',dataIndex :'controlRequirement',sortable : false}, 
                {header : '缺陷类型',dataIndex : 'type',sortable : true}, 
	    		{header : '创建日期',dataIndex :'createTime',sortable : true}	
			]
        });
        me.selectedGrid = 	{
            xtype: 'gridpanel',
            flex: 1,
            store:Ext.create('Ext.data.Store', {
				idProperty: 'id',
				model:'Defect'
			}),
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'code',
                    hidden:true,
                    text: '编号'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'desc',
                    flex:1,
                    text: '缺陷描述',
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
	        	var rec = new Defect({
	    			id:r.data.id,
	    			code:r.data.code,
	    			desc:r.data.desc,
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
    onSubmit:Ext.emptyFn()
});