/**
 * 待选列表和已选列表
 * @author 郑军祥
 */
Ext.define('FHD.ux.listselector.ListSelectorWindow', {
    extend: 'Ext.window.Window',
	alias: 'widget.listselectorwindow',
    height: 500,
    width: 720,
    layout: {
		type:'vbox',
		padding:'5',
		align:'stretch'
    },
    title: '标题',
    listUrl:'',//'/demo/findFirstlistBySome'
    queryKey:'',//查询的属性名称
    multiSelect:true,//是否多选
    iconCls:'',
    initComponent: function() {
        var me = this;
        
        me.candidateColumns = [];   //待选列表项
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
        
		me.sourceGrid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            border: false,
            flex:1,
            checked: false,
            extraParams:{entityName:me.entityName,queryKey:me.queryKey},
            url: __ctxPath + me.listUrl,
            cols: me.candidateColumns
        });
        me.selectedgrid = 	{
            xtype: 'gridpanel',
            flex: 1,
            store:Ext.create('Ext.data.Store', {
				idProperty: 'id',
				model:'CandidateRecord'
			}),
            columns: me.gridColumns
        };
        Ext.applyIf(me, {
            items: [me.sourceGrid,me.selectedgrid]
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
        
        me.sourceGrid.on('select',function(t,node,i,o){
        	if(Ext.isEmpty(me.selectedgrid.store.getById(node.data.id))){
        		if(!me.multiSelect) {
        			me.selectedgrid.store.removeAll();
        		}

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

