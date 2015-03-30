Ext.define('FHD.ux.treelistselector.TreeListSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.treelistselectorwindow',
    
    height: 500,
    width: 720,
    layout: {
        type: 'border'
    },
    title: '选择',
	modal: true,
    maximizable: true,
    
    //接口
    treeEntityName:'',	//后台反射使用
    treeQueryKey:'name',
	parentKey:'parent.id',
	relationKey:'idSeq',
	entityName:'',	//后台反射使用
    queryKey:'name',
    
    //多选员工
    multiSelect:true,
    iconCls:'icon-user',

    emptree:null,
    empgrid:null,
    selectedgrid:null,
    
    initComponent: function() {
        var me = this;

        me.candidateColumns = [];   //待选列表项
        me.gridColumns = [];		//列表项
        me.modelFields = ['id'];	//列表实体定义

        //1. 构建grid的columns
        me.candidateColumns.push(Ext.create('Ext.grid.RowNumberer',{width:25,align:'center'}));
        for(var i = 0; i < me.columns.length; i++) {
        	var obj = {};
        	obj['xtype'] = 'gridcolumn';
        	obj['dataIndex'] = me.columns[i]['dataIndex'];
        	obj['text'] = me.columns[i]['header'];
        	obj['flex'] = 2;
        	if(me.columns[i]['isPrimaryName']){
        		obj['flex'] = 3;
        		obj['renderer'] = function(value, metaData, record, rowIndex, colIndex, store){
					return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
			    }
        	}
        	me.candidateColumns.push(obj);
        	me.gridColumns.push(obj);

        	//构建modelField
        	me.modelFields.push(me.columns[i]['dataIndex']);
        }
        me.gridColumns.push({
        	xtype:'templatecolumn',
        	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
        	flex:1,
        	listeners:{
        		click:{
        			fn:function(g,d,i){
        				g.store.removeAt(i);
        			}
        		}
        	}
        });
        
        //2. 构建grid的model
        Ext.define('CandidateRecord', {
            extend: 'Ext.data.Model',
            fields: me.modelFields
        });
        
		var empgridStore = Ext.create('Ext.data.Store',{
        	pageSize: 100000,
        	idProperty: 'id',
        	fields:me.modelFields,
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
			fields:me.modelFields,
		});
        
        //3. 构建树
		var treeFileds = me.modelFields;
		treeFileds.push('text');
		treeFileds.push('leaf');
		treeFileds.push('text');
		treeFileds.push('cls');
		treeFileds.push('iconCls');
        me.emptree = Ext.create('FHD.ux.treeselector.SelectorTree',{
        	//title:me.title,
        	split : true,
        	region:'west',
        	flex: 0.7,
        	animate:false,
        	iconCls:'icon-door-open',
        	url:me.treeUrl,
        	entityName:me.treeEntityName,
        	queryKey:me.treeQueryKey,
    		parentKey:me.parentKey,
    		relationKey:me.relationKey,
        	checkable:false,
    		modelFields:['text', 'id', 'title','leaf','iconCls','cls','parentId','idSeq']
        });
        
        me.empgrid = Ext.create('Ext.grid.Panel',{
            flex: 1,
            loadMask: true,
            store:empgridStore,
            columns: me.candidateColumns,
            tbar : ['<b>' + FHD.locale.get('empselectorwindow.empgrid.title') + '</b>', '->',
			Ext.create('Ext.ux.form.SearchField', {
				width : 150,
				paramName:'query',
				store:empgridStore,
				emptyText : FHD.locale.get('searchField.emptyText')
			})]
        });
        
        me.selectedgrid = Ext.create('Ext.grid.Panel',{
            flex: 1,
            store:selectgridStore,
            columns: me.gridColumns,
            tbar:['<b>' + FHD.locale.get('empselectorwindow.selectedgrid.title') + '</b>']
        });
        Ext.applyIf(me, {
            items: [me.emptree,
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
        
        // 组织树选中事件,列表刷新
        me.emptree.on('select',function(t,r,i,o){
        	var id = r.data.id;
	       	empgridStore.proxy.extraParams = {id:id,foreignKey:me.foreignKey,entityName:me.entityName,queryKey:me.queryKey};
        	empgridStore.proxy.url = __ctxPath + me.listUrl;
        	empgridStore.load();
        });

        // 员工列表选择事件
        me.empgrid.on('select',function(t,node,i,o){
        	if(Ext.isEmpty(me.selectedgrid.store.getById(node.data.id))){
        		if(!me.multiSelect) {
        			me.selectedgrid.store.removeAll();
        		}
        		//3. 构建一条记录
	    	    var recordInstance = {};
        		for(var key in me.modelFields){
	    	    	recordInstance[me.modelFields[key]] = node.data[me.modelFields[key]];
	    	    }
        		var candidateRecord = new CandidateRecord(recordInstance);
	    		me.selectedgrid.store.insert(0,candidateRecord);
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
