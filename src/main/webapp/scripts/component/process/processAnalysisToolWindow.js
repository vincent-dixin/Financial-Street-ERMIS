Ext.define('FHD.ux.process.processAnalysisToolWindow', {
	extend : 'FHD.ux.Window',
	alias : 'widget.processanalysistoolwindow',
	
	//requires : ['Ext.window.Window'],
	constrain : true,
	modal : true,
	collapsible : true,
	maximizable : true,
	//items:new Array(),
	/*
	layout : {
		type : 'column'
	},
	*/
	title : $locale('fhd.process.processselector.title'),
	//values : new Array(),
	
	initComponent : function() {
		var me = this;

		//按部门选择流程
		me.assessPlanDepart = Ext.create('FHD.ux.process.processRelaOrgSelectorWindow', {
			columnWidth:1,
			//autoDestroy:true,
			single:false,
			height:450
		});
		
		//下部分fieldSet,用于展现相应的选择框
		var fieldSetBottom={
			xtype : 'fieldset',
			margin: '5 5 5 5',
			layout: 'fit',
			//columnWidth:1,
			//autoDestroy:true,
			//height:400,
			collapsed : false,
			collapsible : false,
			title :'选择',
			items : [me.assessPlanDepart]
		};
		me.buttons = [
		    {xtype : 'button',text : $locale('fhd.common.confirm'),
		    	handler : function() {
		            me.onSubmit(me.getValues());
					me.close();
				}
		    }, 
		    {xtype : 'button',text : $locale('fhd.common.close'),style : {marginLeft : '10px'},
				handler : function() {
					me.close();
				}
		    }
		];
		me.items=[fieldSetBottom];
		me.callParent(arguments);
	},
	getValues:function(){
		var me=this;
		
		return me.assessPlanDepart.getValue();
	},
	onSubmit:function(){
		var me=this;
		
	},
	reloadData:function(){
		var me=this;
		
	}
});