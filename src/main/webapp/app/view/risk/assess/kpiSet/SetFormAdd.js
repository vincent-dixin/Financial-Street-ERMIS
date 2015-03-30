/**
 * 
 * 目标设定-表单添加
 */

Ext.define('FHD.view.risk.assess.kpiSet.SetFormAdd',{
 	extend: 'Ext.form.Panel',
	border:false,
	flex:1,
    initComponent: function () {
    		var me = this;
			   	 var tarname = Ext.create('Ext.form.TextField', {
				 	 disabled:false,
					 fieldLabel: '目标名称',
					 allowBlank:false,
				     name:'tarname'
				  });
			   	var tardesc = Ext.create('Ext.form.TextField', {
				 	 disabled:false,
					 fieldLabel: '目标描述',
					 allowBlank:false,
				     name:'tardesc'
				  });
			   	var or = Ext.create('Ext.form.TextField', {
				 	 disabled:false,
					 fieldLabel: '责任部门',
					 allowBlank:false,
				     name:'or'
				  });
			   	var er = Ext.create('Ext.form.TextField', {
				 	 disabled:false,
					 fieldLabel: '责任人',
					 allowBlank:false,
				     name:'er'
				  });
			  var basicInfo = Ext.create('Ext.form.FieldSet',{
					defaults: {
						columnWidth : 1/2,
						labelWidth: 95,
						margin: '7 30 3 30'
								 },
					layout: {
							type: 'column'
								},
					  title:'基本信息',
					  items:[tarname,tardesc,or,er]
		  	});
		  	
		  	var measureGrid = Ext.create('FHD.view.risk.assess.kpiSet.SetMeasureGrid');
		  	
		  	 var measureKPI = Ext.create('Ext.form.FieldSet',{
		  		 	flex:1,
					defaults: {
						columnWidth : 1,
						labelWidth: 95,
						margin: '7 30 3 30'
								 },
					layout: {
							type: 'column'
								},
					  title:'指标衡量',
					  items:[measureGrid]
		  	});
		  	
    	           
    	me.callParent(arguments);
    	me.add(basicInfo);
    	me.add(measureKPI);
    }

});