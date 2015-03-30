Ext.define('FHD.view.riskinput.workflow.ImplementationReportForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.implementationreportform',
	       
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
		me.listFieldset = Ext.widget('fieldset',{
			collapsed : false,
            collapsible : false,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
                columnWidth: 1
            },
            layout: {
                type: 'column'
            },
            title: '预案执行情况'
    	});
    	
    	
    	me.implementationGrid = Ext.create('FHD.ux.GridPanel',{
    		height:150,
    		url: __ctxPath + '',
    		extraParams:{
    			assessplanId:''
    		},
    		checked:false,
    		pagable:false,
    		searchable:false,
    		cols:[{
    			header:'控制措施',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'责任人',dataIndex:'processName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'工作内容',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'工作类别',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'计划完成时间',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		}]
    	});
		me.listFieldset.add(me.implementationGrid);
		
		
		var labelDisplay2={
		    xtype:'displayfield',
		    margin: '0 60 10 20',
		    value:'影响程度:'
		};
		
        
        var property ={
        	 xtype:'displayfield',
    		 columnWidth:.1,
    	     vertical: true,
    	     value:'市场方面'
        };
        
        var occurPossSel = Ext.create('Ext.form.RadioGroup',{
	        vertical: true,
	        layout:'column',
	        columnWidth:.3,
	        items: [
	            { boxLabel: "<span data-qtip='竞争对手增加'>高</span>", name: 'occurPossSel', inputValue: '1', columnWidth:.3},
	            { boxLabel: "<span data-qtip='价格升高'>中</span>", name: 'occurPossSel', inputValue: '2', columnWidth:.3},
	            { boxLabel: "<span data-qtip='供货量不足'>低</span>", name: 'occurPossSel', inputValue: '3', columnWidth:.3}
	        ]
    	});
        var propertyCon=Ext.create('Ext.container.Container',{
        	layout:{
     	    	type:'column'  
     	    },
     	    columnWidth : .7,
     	    items:[property,occurPossSel]
        });
        
        var property2 = {
    		 xtype:'displayfield',
    		 columnWidth:.1,
    	     vertical: true,
    	     value:'收入'
        };
        var occurPossSel2 =Ext.create('Ext.form.RadioGroup',{
	        vertical: true,
	        layout:'column',
	        columnWidth : .3,
	        items: [
	            { boxLabel: "<span data-qtip='竞争对手增加'>高</span>", name: 'everyWeeksCheck', inputValue: '1', columnWidth:.3},
	            { boxLabel: "<span data-qtip='价格升高'>中</span>", name: 'everyWeeksCheck', inputValue: '2', columnWidth:.3},
	            { boxLabel: "<span data-qtip='供货量不足'>低</span>", name: 'everyWeeksCheck', inputValue: '3', columnWidth:.3}
	        ]
    	});
        var propertyCon2=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    columnWidth : .7,
     	    items:[property2,occurPossSel2]
        });
        
//      --------------------------定量
        
        var percentage = {
    		 xtype:'displayfield',
    		 columnWidth:.1,
    	     vertical: true,
    	     value:'市场占有率'
        };
        var percentageData = Ext.widget('textfield', {
            xtype: 'textfield',
            value: '',
            margin: '0 20 10 20',
            maxLength: 255,
            columnWidth: .2
        });
        var occurPossSel = Ext.create('Ext.container.Container',{
	        vertical: true,
	        layout:'column',
	        columnWidth : .7,
	        items: [
	            { xtype:'displayfield',value:'红&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;70%~80%', columnWidth:.3},
	            { xtype:'displayfield',value:'黄&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;40%~70%', columnWidth:.3},
	            { xtype:'displayfield',value:'绿&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0~40%', columnWidth:.3}
	        ]
    	});
        var percentageCon=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '20 10 0 0',
     	    columnWidth : 1,
     	    items:[percentage,percentageData]
        });
        
        
        
        
        var effectDegreeArr=Ext.create('Ext.container.Container',{
     	    columnWidth : 1,
     	    items:[propertyCon,propertyCon2,percentageCon]
        });
        
         var effectDegreeCon=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    columnWidth : 1,
     	    items:[labelDisplay2,effectDegreeArr]
        });
        
        var rewards = {
            xtype : 'textareafield',
            fieldLabel:'责任认定及奖惩情况',
            labelAlign : 'left',
            margin: '7 10 0 20',
            row : 5,
            columnWidth : 1,
            name : 'requirement',
            labelWidth : 100
        };
        
        var differences = {
            xtype : 'textareafield',
            fieldLabel:'实际应对情况与预案差异',
            labelAlign : 'left',
            margin: '7 10 0 20',
            row : 5,
            columnWidth : 1,
            name : 'requirement',
            labelWidth : 100
        };
        
        var suggest = {
            xtype : 'textareafield',
            fieldLabel:'应对预案改进建议',
            labelAlign : 'left',
            margin: '7 10 0 20',
            row : 5,
            columnWidth : 1,
            name : 'requirement',
            labelWidth : 100
        };
		
		me.commentFieldset = Ext.widget('fieldset',{
			collapsed : false,
            collapsible : false,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
            	width: 1000
            },
            layout: {
                type: 'auto'
            },
            title: '评价信息'
    	});
		
		me.commentFieldset.add(effectDegreeCon);
		me.commentFieldset.add(rewards);
		me.commentFieldset.add(differences);
		me.commentFieldset.add(suggest);
    	
		Ext.apply(me,{
			items:[me.listFieldset,me.commentFieldset]
		},me.bbar)
		
		me.callParent(arguments);
		
	}
});