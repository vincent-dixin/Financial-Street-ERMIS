
Ext.define('DeptAndEmp', {
    extend: 'Ext.data.Model',
    fields:['deptid', 'deptno', 'deptname','empid','empno','empname']
});
Ext.define('FHD.ux.org.DeptAndEmpSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.deptandempwindow',
    height: 500,
    width: 720,
    modal: true,
    maximizable: true,
    layout: {
        align: 'stretch',
        type: 'hbox'
    },
    title: $locale('deptandempwindow.title'),
    //多选部门
    multiSelect:true,

    checkModel:'multi',
    iconCls:'icon-root',
    depttree:null,
    selectedgrid:null,
    
    initComponent: function() {
        var me = this;
		me.id = Ext.id();
		var emps = Ext.create('Ext.data.Store', {
		        	idProperty: 'id',
		        	fields:['id', 'empno', 'empname'],
		        	proxy: {
		        		url: __ctxPath + '/sys/emp/findempsbyorgidwithonpage/'+__user.companyId,
				        type: 'ajax',
				        reader: {
				            type : 'json',
				            root : 'datas',
				            totalProperty :'totalCount'
				        },
				        autoLoad:false
				    }
			});
		emps.load();
		
		if(!me.multiSelect){
			me.checkModel = 'single';
		}
		me.depttree = Ext.create('FHD.ux.org.DeptTree',{
    		flex: 0.4,
    		title:'',
    		chooseId:me.choose,
        	checkModel:me.checkModel,
        	subCompany: me.subCompany,
			companyOnly: me.companyOnly,
			rootVisible: me.rootVisible,
	    	check:function(tree,node,checked){
	    		var dept = new DeptAndEmp({
	    			deptid:node.data.id,
	    			deptno:node.data.code,
	    			deptname:node.data.text
	    		});
	    		var selectedgrid = me.selectedgrid;
	    		var count = selectedgrid.getStore().getCount();// 获得总行数
	    		if(checked){
	    			if(!me.multiSelect){
	    				selectedgrid.store.removeAll();
	    			}
	    			selectedgrid.store.insert(count,dept);
	    		}else {
	    			selectedgrid.store.remove(selectedgrid.store.findRecord('deptid',node.data.id));
	    		}
	    	}
	    });
	    me.clicksToEdit =1;//单击编辑
		me.selectedgrid = Ext.create('Ext.grid.Panel',{
            flex: 1,
            store:Ext.create('Ext.data.Store', {
				idProperty: 'deptid',
				model:'DeptAndEmp'
			}),
			plugins:Ext.create('Ext.grid.plugin.CellEditing', {
		        clicksToEdit: me.clicksToEdit
		    }),
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'deptno',
                    header: $locale('deptselectorwindow.selectedgrid.deptno'),
                    hidden:true,
                    sortable: false,
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						return value;					
				    }
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'deptname',
                    flex:1,
                    sortable: false,
                    header: $locale('deptandempwindow.selectedgrid.deptname'),
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
						return value;					
				    }
                },
                {
                	dataIndex: 'empid',flex:1,
                	header: $locale('deptandempwindow.selectedgrid.empname'),
                	sortable: false,
                	editor:
	                	Ext.create('Ext.form.ComboBox',{
	                		valueField : 'id',
							displayField : 'empname',
							id:'comboBoxempselect',
							multiSelect:me.multiSelect,
							selectOnTab: false,
							editable : false,
		                	store : emps
	                	}),
	                renderer:function(value, metaData, record, rowIndex, colIndex, store){
	                	var displayName = new Array();
	                	if(value){
	                		Ext.Array.each(value,function(r,i){
	                			var rec = emps.findRecord('id',r);
	                			if(rec){
	                				displayName.push(rec.data.empname);
	                			}
	                		});
		                	if(displayName.length>0){
		                		metaData.tdAttr = 'data-qtip="'+displayName.join(',')+'"'; 
		                		return displayName.join(',');
		                	}else{
		                		if(record.data.empname){
			                		metaData.tdAttr = 'data-qtip="'+record.data.empname+'"'; 
	                				return record.data.empname;
			                	}else{
			                		return $locale('fhd.common.pleaseSelect');
			                	}
		                	}
	                	}else{
	                		return $locale('fhd.common.pleaseSelect');
	                	}
					}
                },
                {
                	xtype:'templatecolumn',
//                        	tpl:'<font color="red">'+ $locale('fhd.common.delete') +'</font>',
                	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                	flex:.3,
                	listeners:{
                		click:{
                			fn:function(g,d,i){
                				var nodes = me.depttree.getChecked();
								Ext.each(nodes,function(n){
									if(n.data.id == g.store.getAt(i).data.deptid){
										n.set("checked",false); 
									}
								});
                				g.store.removeAt(i);
                			}
                		}
                	}
                }
            ],
            listeners:{
            	select:function(me,record,index){
            		var comboBoxempselect = Ext.getCmp('comboBoxempselect');
            		comboBoxempselect.store.proxy.url = __ctxPath + '/sys/emp/findempsbyorgidwithonpage/' + record.data.deptid;
					comboBoxempselect.store.load();
					comboBoxempselect.disabled=true;
					comboBoxempselect.store.on('load',function(store){
						comboBoxempselect.disabled=false;
					});
            	}
            }
        });
        
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
