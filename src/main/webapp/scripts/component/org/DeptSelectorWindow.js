
Ext.define('Dept', {
    extend: 'Ext.data.Model',
    fields:['id', 'deptno', 'deptname']
});

Ext.define('FHD.ux.org.DeptSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.deptwindow',
    height: 500,
    width: 720,
    modal: true,
    maximizable: true,
    layout: {
        align: 'stretch',
        type: 'hbox'
    },
    title: $locale('deptselectorwindow.title'),
    //多选部门
    multiSelect:true,

    checkModel:'multi',
    iconCls:'icon-root',
    depttree:null,
    selectedgrid:null,
    
    initComponent: function() {
        var me = this;
		me.id = Ext.id();
		
		
		if(!me.multiSelect){
			me.checkModel = 'single';
		}
		me.depttree = Ext.create('FHD.ux.org.DeptTree',{
    		flex: 0.7,
    		//title:$locale('depttreepanel.title'),
    		chooseId:me.choose,
        	checkModel:me.checkModel,
        	subCompany: me.subCompany,
        	companyOnly: me.companyOnly,
        	rootVisible: me.rootVisible,
	    	check:function(tree,node,checked){
	    		var dept = new Dept({
	    			id:node.data.id,
	    			deptno:node.data.code,
	    			deptname:node.data.text
	    		});
	    		var selectedgrid = me.selectedgrid;
	    		if(checked){
	    			if(!me.multiSelect){
	    				selectedgrid.store.removeAll();
	    			}
	    			selectedgrid.store.insert(0,dept);
	    		}else {
	    			selectedgrid.store.remove(selectedgrid.store.getById(node.data.id));
	    		}
	    	}
	    });
		me.selectedgrid = 	{
                    xtype: 'gridpanel',
                    flex: 1,
                    store:Ext.create('Ext.data.Store', {
						idProperty: 'id',
						model:'Dept'
					}),
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'deptno',
                            flex:2,
                            text: $locale('deptselectorwindow.selectedgrid.deptno')
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'deptname',
                            flex:3,
                            text: $locale('deptselectorwindow.selectedgrid.deptname'),
                            renderer:function(value, metaData, record, rowIndex, colIndex, store){
								return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
						    }
                        },
                        {
                        	xtype:'templatecolumn',
//                        	tpl:'<font color="red">'+ $locale('fhd.common.delete') +'</font>',
                        	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                        	flex:1,
                        	listeners:{
                        		click:{
                        			fn:function(g,d,i){
                        				
                        				var nodes = me.depttree.getChecked();
										Ext.each(nodes,function(n){
											if(n.data.id == g.store.getAt(i).data.id){
												n.set("checked",false); 
											}
										});
                        				
                        				g.store.removeAt(i);
                        			}
                        		}
                        	}
                        }
                    ]
                };
        Ext.applyIf(me, {
            items: [me.depttree,me.selectedgrid]
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
    },
    setValue:function(selecteds){
    	var me = this;
    	Ext.each(selecteds,function(selected){
        	me.selectedgrid.store.insert(0,selected);
        });
    },
    onSubmit:Ext.emptyFn()
});

