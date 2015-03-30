Ext.define('FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm',{
	extend : 'Ext.form.Panel',
	alias: 'widget.constructplanpreviewform',
	
	border:false,
	autoScroll:false,
	isShowGroupPers:true,
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1 / 1
	},
	collapsed : false,
	initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	initComponent : function() {
		var me = this;
		//编号
		var constructPlanCode = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'right',
			fieldLabel : '计划编号',
			value : '',
			name : 'codeView',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//名称
		var constructPlanName = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '计划名称',
			name : 'nameView',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		  //样本期间
		var constructPlanStart = {
	        xtype: 'displayfield',
	        name: 'planStartDateView',
	        format: 'Y-m-d'
		};
		var constructPlanEnd = {
	        xtype: 'displayfield',
	        name: 'planEndDateView',
	        format: 'Y-m-d'
		};
		var labelDisplay={
		    xtype:'displayfield',
		    width:82,
		    value:'计划期间:'
		};
		var labelDisplay1={
		    xtype:'displayfield',
		    value:'&nbsp;至'
		};
		var constructPlanDateAmongCont=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '7 10 0 30',
     	    columnWidth : .5,
     	    items:[labelDisplay,constructPlanStart,labelDisplay1,constructPlanEnd]
        });
		me.constructTypeHid = Ext.widget('textfield',{ 
            name : 'typeViewHid',
            hidden : true
        });
		//计划类型
		var constructType = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '工作内容',
			name : 'typeView',
			margin : '7 10 0 30',
			value : '',
			columnWidth : .5
		};
		//组长
		var constructPlanGroup = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'right',
			fieldLabel : '组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长',
			value : '',
			name : 'responsibilityEmp',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//组成员
		var constructPlanGroupPers = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'right',
			fieldLabel : '组&nbsp;&nbsp;成&nbsp;员',
			value : '',
			name : 'handlerEmp',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//工作目标
		var workTarget = {
			xtype : 'textareafield',
			labelAlign : 'left',
			name : 'workTargetView',
			columnWidth : 1,
			labelWidth : 80,
//			margin: '7 10 0 30',
			row : 5
		};
		//基本要求
		var constructPlanStandard = {
			xtype : 'textareafield',
			labelAlign : 'left',
			name : 'requirementView',
			columnWidth : 1,
			labelWidth : 80,
//			margin: '7 10 0 30',
			row : 5
		};
		
		var fieldSetInfo={//基本信息
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			collapsed : false,
			collapsible : false,
			title : '基本信息',
			items : [
			    constructPlanName,
			    constructPlanDateAmongCont,
    			me.constructTypeHid,constructType,constructPlanGroup,constructPlanGroupPers
			]
		};
		
		var fieldSetChildTarger={//评价目标
			xtype : 'fieldset',
//			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			collapsed : false,
			collapsible : false,
			title : '评价目标',
			items : [workTarget]
		};
		var fieldSetChildRange={//范围要求
			xtype : 'fieldset',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			title : '范围要求',
			items : [constructPlanStandard]
		};
		var fieldSetXiangXiInfo={//详细信息
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			defaults: {
                columnWidth : 1 / 2,
                margin: '7 30 3 30',
                labelWidth: 95
            },
			collapsed : true,
			collapsible : true,
			columnWidth:1,
			title : '更多信息',
			items : [
			    fieldSetChildTarger,fieldSetChildRange
			]
		};
		
		me.items=[fieldSetInfo,fieldSetXiangXiInfo]
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		debugger;
		me.getForm().load({
            url: __ctxPath+'/icm/icsystem/findconstructplanforview.f',
            params: {
            	constructPlanId : me.paramObj.businessId
	        },
            success: function (form, action) {
         	    if(me.up('panel').constructPlanGrid){
         	    	var vobj = form.getValues();
         	    	me.operateColumn(vobj);
         	    }
         	   return true;
            },
            failure: function (form, action) {
         	   return false;
            }
		});
	},
	operateColumn : function(vobj){
		var me = this;
		if(vobj.typeViewHid == 'diagnoses'){
			me.up('panel').constructPlanGrid.down('[dataIndex=isProcessEdit]').hide();
			me.up('panel').constructPlanGrid.down('[dataIndex=isNormallyDiagnosis]').show();
		}else if(vobj.typeViewHid == 'process'){
			me.up('panel').constructPlanGrid.down('[dataIndex=isNormallyDiagnosis]').hide();
			me.up('panel').constructPlanGrid.down('[dataIndex=isProcessEdit]').show();
		}else{
			me.up('panel').constructPlanGrid.down('[dataIndex=isNormallyDiagnosis]').show();
			me.up('panel').constructPlanGrid.down('[dataIndex=isProcessEdit]').show();
		}
	}
});
