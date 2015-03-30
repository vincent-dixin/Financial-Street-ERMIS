Ext.define('FHD.view.riskinput.workflow.EventCorrectiveForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.eventcorrectiveform',
	       
	border : false,
	autoScroll:true,
    layout: {
                type : 'column'
            },
            defaults : {
                columnWidth : 1 / 1
            },
    bodyPadding:'0 60 3 60',
	
	initComponent: function(){
		var me = this;
		
		me.bbar=['->',{
        		iconCls : 'icon-control-play-blue',
        		text:'保存',
        		handler: function(){
//               	var riskeventeditcardpanel = me.up('riskeventeditcardpanel');
//		    		riskeventeditcardpanel.reloadData();
//    				riskeventeditcardpanel.navBtnHandler(0);
//    				me.close();
        			var riskeventeditcardpanel = me.up('riskeventeditcardpanel');
        			riskeventeditcardpanel.reloadData();
        			var schemeform = Ext.widget('schemeform');
        			riskeventeditcardpanel.setActiveItem(schemeform);
        			
                }
        	},'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        }]
		me.eventListFieldset = Ext.widget('fieldset',{
			collapsed : false,
            collapsible : false,
            defaults: {
                margin: '7 30 3 30',
            	labelAlign: 'left'
            },
            layout: {
                type: 'column'
            },
            columnWidth: .5,
            title: '已发生事件'
    	});
    	
    	
    	me.eventGrid = Ext.create('FHD.ux.GridPanel',{
    		height:150,
    		url: __ctxPath + '',
    		extraParams:{
    			assessplanId:''
    		},
    		columnWidth: 1,
    		checked:false,
    		pagable:false,
    		searchable:false,
    		cols:[{
    			header:'事件',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		}]
    	});
		me.eventListFieldset.add(me.eventGrid);
		
		
		
	    me.planListFieldset = Ext.widget('fieldset',{
			collapsed : false,
            collapsible : false,
            margin: '20 0 0 20',
            defaults: {
                margin: '7 30 3 30',
            	labelAlign: 'left'
            },
            layout: {
                type: 'column'
            },
            columnWidth: .5,
            title: '现有管控措施'
    	});
    	
    	
    	me.planGrid = Ext.create('FHD.ux.GridPanel',{
    		height:150,
    		url: __ctxPath + '',
    		extraParams:{
    			assessplanId:''
    		},
    		checked:false,
    		pagable:false,
    		columnWidth: 1,
    		searchable:false,
    		cols:[{
    			header:'措施',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		}]
    	});
		me.planListFieldset.add(me.planGrid);
		
       var suggest = {
            xtype : 'textareafield',
            fieldLabel:'管理改进建议',
            labelAlign : 'left',
            margin: '20 0 0 0',
            row : 10,
            columnWidth:1,
            name : 'requirement',
            height:200,
            labelWidth : 100
        };
        
		Ext.apply(me,{
			items:[me.eventListFieldset,
				 	me.planListFieldset,
					suggest]
		},me.bbar)
		
		me.callParent(arguments);
		
	}
});