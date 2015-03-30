

Ext.define('FHD.ux.org.EmpSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.empwindow',
    
    height: 500,
    width: 720,
    layout: {
        type: 'border'
    },
    title: FHD.locale.get('empselectorwindow.title'),
	modal: true,
    maximizable: true,
    //多选员工
    multiSelect:true,
    iconCls:'icon-user',
    
    
    emptree:null,
    roletree:null,
    empgrid:null,
    selectedgrid:null,
    
    
    initComponent: function() {
        var me = this;
        Ext.define('Emp', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'empno', 'empname']
		});
        
		var empgridStore = Ext.create('Ext.data.Store',{
        	storeId:'empgridstore',
        	pageSize: 100000,
        	idProperty: 'id',
        	fields:['id', 'empno', 'empname'],
        	proxy: {
		        type: 'ajax',
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
        	
        });
        
        var selectgridStore = Ext.create('Ext.data.Store', {
			idProperty: 'id',
			fields:['id', 'empno', 'empname']
		});
        me.emptree = Ext.create('FHD.ux.org.EmpTree',{
        	animate:false,
        	iconCls:'icon-door-open',
        	title: FHD.locale.get('empselectorwindow.emptree'),
        	subCompany: me.subCompany
        });
        me.roletree = Ext.create('FHD.ux.TreePanel',{
            animate:false,
            iconCls:'icon-tux',
            title: FHD.locale.get('empselectorwindow.roletree'),
            url:__ctxPath + '/sys/auth/role/treeloader',
            root: {
    	        text: __user.companyName,
    	        id: __user.companyId
    	    },
            /*store: Ext.create('Ext.data.TreeStore', {
        	    proxy: {
        	    	url: __ctxPath + '/sys/auth/role/treeloader',
        	    	//url:'sys/autho/findDictTypeBySome.f',
        	        type: 'ajax',
        	        reader: {
        	            type: 'json'
        	        }
        	    },
        	    root: {
        	        text: __user.companyName,
        	        id: __user.companyId
        	    }
        	}),*/
            viewConfig: {

            },
            listeners:{
            	expand:{
            		fn:function(p){
            			p.getRootNode().expand();
            		}
            	}
            }
           });
       /* me.roletree = Ext.create('FHD.ux.org.RoleTree',{
        	animate:false,
        	iconCls:'icon-door-open',
        	title: FHD.locale.get('empselectorwindow.roletree')
        });*/
        me.empgrid = Ext.create('Ext.grid.Panel',{
            flex: 1,
            loadMask: true,
            store:empgridStore,
            features: [{
            	ftype: 'filters',
				autoReload: false, //don't reload automatically
				local: true, //only filter locally
				// filters may be configured through the plugin,
				// or in the column definition within the headers configuration
				filters: [{
					dataIndex: 'empname'
				}, {
					dataIndex: 'empno'
				}]
            
            }],
            columns: [
            	Ext.create('Ext.grid.RowNumberer',{width:25,align:'center'}),
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'empno',
					filterable: true,
                    flex: 1,
                    text: FHD.locale.get('empselectorwindow.empgrid.empno')
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'empname',
					filterable: true,
                    flex: 1,
                    text: FHD.locale.get('empselectorwindow.empgrid.empname'),
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
				    }
                }
            ],
            tbar : ['<b>' + FHD.locale.get('empselectorwindow.empgrid.title') + '</b>', '->',
			Ext.create('Ext.ux.form.SearchField', {
				width : 150,
				paramName:'empname',
				store:empgridStore,
				emptyText : FHD.locale.get('searchField.emptyText')
			})],
            viewConfig: {

            }/*,
            bbar: Ext.create('Ext.PagingToolbar', {
	            pageSize: FHD.pageSize,
	            store: empgridStore,
	             displayInfo: true,
				//plugins: Ext.create('Ext.ux.ProgressBarPager', {}),
	            xtype: 'pagingtoolbar',
             	displayMsg: $locale('fhd.common.display')+'{0} - {1} '+$locale('fhd.common.count')+','+$locale('fhd.common.totalcount')+' {2}'+$locale('fhd.common.count'),
              	emptyMsg: $locale('fhd.common.norecord')
	        })*/
        });
        
        me.selectedgrid = Ext.create('Ext.grid.Panel',{
            flex: 1,
            store:selectgridStore,
            features: [{
            	ftype: 'filters',
				autoReload: false, //don't reload automatically
				local: true//only filter locally
				// filters may be configured through the plugin,
				// or in the column definition within the headers configuration
            }],
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'empno',
                    filterable: true,
                    flex:1,
                    text: FHD.locale.get('empselectorwindow.selectedgrid.empno')
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'empname',
                    filterable: true,
                    flex:1,
                    text: FHD.locale.get('empselectorwindow.selectedgrid.empname'),
                    renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value + "'>" + Ext.util.Format.ellipsis(value,15) + "</div>";					
				    }
                },
                {
                	xtype:'templatecolumn',
//                                        	tpl:'<font color="red">删除</font>',
                	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                	listeners:{
                		click:{
                			fn:function(t,d,i){
                				t.store.removeAt(i);
                			}
                		}
                	}
                }
            ],
            tbar:['<b>' + FHD.locale.get('empselectorwindow.selectedgrid.title') + '</b>'],
            viewConfig: {

            }
        });
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    width: 250,
                    layout: {
                        type: 'accordion'
                    },
					split : true,
                    region: 'west',
                    items: [me.emptree,me.roletree]
                },
                {
                    xtype: 'container',
                    activeItem: 0,
                    layout: {
                        type: 'border'
                    },
                    region: 'center',
                    items: [
                        {
                            xtype: 'container',
                            layout: {
                                align: 'stretch',
                                type: 'vbox'
                            },
                            region: 'center',
                            items: [me.empgrid,me.selectedgrid]
                        }
                    ]
                }
            ]
        });
        me.buttons = [
        	{
	            xtype: 'button',
	            text: $locale('fhd.common.confirm'),
	            width:70,
	            style: {
	            	marginRight: '10px'    	
	            },
	            handler:function(){
	            	me.onSubmit(me);
	            	me.close();
	            }
	        },
	        {
	            xtype: 'button',
	            text: $locale('fhd.common.close'),
	            width:70,
	            style: {
	            	marginLeft: '10px'    	
	            },
	            handler:function(){
	            	me.close();
	            }
	        }
	    ];
        me.callParent(arguments);       
        // 组织树选中事件
        me.emptree.on('select',function(t,r,i,o){
        	var id = r.data.id.split('_');
        	var cls = r.data.cls;
        	
	       	empgridStore.proxy.extraParams = {};
        	
        	if('org' === cls)empgridStore.proxy.url = __ctxPath + '/sys/emp/findempsbyorgid/' + id[0];
        	if('posi' === cls)empgridStore.proxy.url = __ctxPath + '/sys/emp/findempsbyposiid/' + id[0];
        	
        	empgridStore.load();
        });
        
        me.emptree.store.on("beforeload", function(store, o) {
			var cls = o.node.data.cls;
			if('org' === cls)store.proxy.url = __ctxPath + '/sys/emp/cmp/treeloader';
			if('posi' === cls)store.proxy.url = __ctxPath + '/sys/orgstructure/posi/loadPosiTree.do';
		});



        
        // 角色树选中事件
        me.roletree.on('select',function(t,r,i,o){
        	var id = r.data.id.split('_');
        	empgridStore.proxy.extraParams = {};
        	empgridStore.proxy.url = __ctxPath + '/sys/emp/findempsbyroleid/' + id[0];
        	empgridStore.load();
        });
        
        // 员工列表选择事件
        me.empgrid.on('select',function(t,r,i,o){
        	if(Ext.isEmpty(me.selectedgrid.store.getById(r.data.id))){
        		if(!me.multiSelect) {
        			me.selectedgrid.store.removeAll();
        		}
	        	var rec = new Emp({
	    			id:r.data.id,
	    			empno:r.data.empno,
	    			empname:r.data.empname
	    		}); 
	    		me.selectedgrid.store.insert(0,rec);
        	}
        });
        
       
        
    },
    setValue:function(selecteds){
    	var me = this;
    	Ext.each(selecteds,function(selected){
        	me.selectedgrid.store.insert(0,selected);
        });
    },
    onSubmit:Ext.emptyFn()
});
