Ext.define('FHD.ux.treeselector.TreeSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.treeselectorwindow',
    height: 500,
    width: 720,
    modal: true,
    maximizable: true,
    layout: {	//布局
        type: 'border'
    },
    /**外部接口开始*/
    title: '标题',
    columns:null,//[{dataIndex: 'title',text: '编号'},{dataIndex: 'name',text: '名称',isPrimaryName:true}]
    treeUrl:'/components/treeLoader',
    entityName:'',	//后台反射使用
    queryKey:'name',
	parentKey:'parent.id',
	relationKey:'idSeq',
    multiSelect:true,	//是否多选
    checkable:false,
    parameters:'',	//树查询传递的参数 {orgStatus:"1"}
    /**外部接口结束*/

    iconCls:'icon-dept',
    depttree:null,
    selectedgrid:null,
    
    initComponent: function() {
        var me = this;

        me.gridColumns = [];		//列表项
        me.modelFields = ['id'];	//列表实体定义

        //1. 构建grid的columns
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
        });
        
        //2. 构建grid的model
        Ext.define('CandidateRecord', {
            extend: 'Ext.data.Model',
            fields: me.modelFields
        });
        
//		if(!me.multiSelect){
//			me.checkModel = 'single';
//		}
		
		//3. 构建树
		var treeFileds = me.modelFields;
		treeFileds.push('text');
		treeFileds.push('leaf');
		treeFileds.push('text');
		treeFileds.push('cls');
		treeFileds.push('iconCls');
		me.depttree = Ext.create('FHD.ux.treeselector.SelectorTree',{
    		//title:me.title,
			split : true,
			region:'west',
    		url:me.treeUrl,
    		entityName:me.entityName,
    		queryKey:me.queryKey,
    		parentKey:me.parentKey,
    		relationKey:me.relationKey,
    		modelFields:treeFileds,	//treeStore Fields
    		parameters:me.parameters,//动态参数
    		flex: 0.7,
    		chooseId:me.choose,
        	checkable:me.checkable,
	    	check:function(tree,node,checked){
	    	    //3. 构建一条记录
	    	    var recordInstance = {};
		    	
	    	    for(var key in me.modelFields){
	    	    	recordInstance[me.modelFields[key]] = node.data[me.modelFields[key]];
	    	    }
	    	    
		    	var candidateRecord = new CandidateRecord(recordInstance);
	    		var selectedgrid = me.selectedgrid;
	    		if(checked){
	    			if(!me.multiSelect){
	    				selectedgrid.store.removeAll();
	    			}
	    			selectedgrid.store.insert(0,candidateRecord);
	    		}else {
	    			selectedgrid.store.remove(selectedgrid.store.getById(node.data.id));
	    		}
	    	}
	    });
		me.selectedgrid = 	{
                    xtype: 'gridpanel',
                    region:'center',
                    flex: 1,
                    store:Ext.create('Ext.data.Store', {
						idProperty: 'id',
						model:'CandidateRecord'
					}),
                    columns: me.gridColumns
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

