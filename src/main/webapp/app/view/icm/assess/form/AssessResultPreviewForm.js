Ext.define('FHD.view.icm.assess.form.AssessResultPreviewForm',{
	extend : 'Ext.form.Panel',
	alias: 'widget.assessresultpreviewform',
	
	border:false,
	autoScroll:false,
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1 / 1
	},
	collapsed : false,
	
	initComponent : function() {
		var me = this;
		
		//评价点描述
		var assessPointDesc = {
			xtype : 'textareafield',
			labelAlign : 'left',
			fieldLabel : '评价点描述',
			margin: '7 10 5 30',
			name : 'assessPointDesc',
			readOnly:true,
			columnWidth : 1,
			labelWidth : 80,
			row : 1
		};
		//实施证据
		var assessPointComment = {
			xtype : 'textareafield',
			labelAlign : 'left',
			fieldLabel : '实施证据',
			margin: '7 10 5 30',
			name : 'assessPointComment',
			readOnly:true,
			columnWidth : 1,
			labelWidth : 80,
			row : 1
		};
		
		var fieldSetInfo={//基本信息
			xtype : 'fieldset',
			margin: '10 10 10 10',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			collapsed : true,
			collapsible : true,
			title : '评价点基本信息',
			items : [
			    assessPointDesc,assessPointComment
			]
		};
		
		me.items=[fieldSetInfo]
		me.callParent(arguments);
		
		me.loadData();
	},
	/*
	listeners:{
		afterrender:function(me){
			me.loadData();
		}
	},
	*/
	loadData:function(){
		var me=this;

		me.getForm().load({
            url: __ctxPath+'/icm/assess/findAssessPointViewByAssessResultId.f?assessResultId='+me.assessResultId,
            success: function (form, action) {
         	   return true;
            },
            failure: function (form, action) {
         	   return false;
            }
		});
	},
	reloadData:function(){
		var me=this;
		
	}
});
