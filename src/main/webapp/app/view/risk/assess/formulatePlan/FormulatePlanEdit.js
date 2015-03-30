/**
 * 
 * 计划制定表单
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulatePlanEdit', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formulatePlanEdit',
    
    loadData : function(id){
    	var me = this;
		me.load({
    	        url:'access/formulateplan/findriskassessplanById.f',
    	        params:{id:id||me.businessId},
    	        failure:function(form,action) {
    	            alert("err 155");
    	        },
    	        success:function(form,action){
    	        	var formValue = form.getValues();
    	        }
    	    });
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.id = 'formulatePlanEdit';
        var busid = me.businessId;
        var queryRangeTypeUrl = 'access/formulateplan/findrangetype.f';//评估范围模板下拉菜单Url
        var queryModelUrl = 'access/formulateplan/findtemplates.f';//评估模板
        //var queryModelTypeUrl = 'access/formulateplan/findmodeltype.f';//模板类型下拉菜单Url
        
        var planName = Ext.widget('textfield', {//计划名称
            fieldLabel: '计划名称'+'<font color=red>*</font>',
            allowBlank:false,//不允许为空
            margin: '7 10 0 30',
            name: 'planName',
            columnWidth: .5
        });
        var collectRate = Ext.widget('collectionSelector', {//采集频率
    	    name:'collectRate',
    	    labelText : '采集频率',
    	    single:false,
    	    value:'',
    	    margin: '7 10 0 30', 
    	    columnWidth: .5
    	});
        var workTage = Ext.widget('textareafield', {//工作目标
            xtype: 'textareafield',
            rows:3,
            fieldLabel: '工作目标',
            margin: '7 10 0 30',
            name: 'workTage',
            columnWidth: .7
        });
        var rangRequire = Ext.widget('textareafield', {//范围要求
            xtype: 'textareafield',
            rows: 3,
            fieldLabel: '范围要求',
            margin: '7 10 0 30',
            name: 'rangeReq',
            columnWidth: .7
        });
        //开始时间
		var assessPlanTimeStart = {
				xtype: 'datefield',
			    name: 'beginDataStr',
			    columnWidth:.5,
			    format: "Y-m-d"
			};
		//结束时间
		var assessPlanTimeEnd = {
				xtype: 'datefield',
			    name: 'endDataStr',
			    columnWidth:.5,
			    format: "Y-m-d"
			};
		var labelPlanDisplay={
			    xtype:'displayfield',
			    width:99,
			    value:'起止日期:'
			};
		var labelDisplay1={
				    xtype:'displayfield',
				    value:'&nbsp;至&nbsp;'
				};
		var assessPlanTime=Ext.create('Ext.container.Container',{//起止时间
	     	    layout:{
	     	    	type:'column'  
	     	    },
	     	    margin: '7 10 0 30',
	     	    columnWidth : .5,
	     	    items:[labelPlanDisplay,assessPlanTimeStart,labelDisplay1,assessPlanTimeEnd]
			});
        var states = Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            data : [
                {"abbr":"AL", "name":"因果分析"},
                {"abbr":"AK", "name":"层次分析"}
            ]
        });
        //评估范围类型下拉列表
        var rangeTypeStore = Ext.create('Ext.data.Store', {//性别store
    	    fields: ['type', 'name'],
    	    proxy: {
    	         type: 'ajax',
    	         url: queryRangeTypeUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});
        /*//模板类型下拉列表
        var modelypeStore = Ext.create('Ext.data.Store', {//性别store
    	    fields: ['type', 'name'],
    	    proxy: {
    	         type: 'ajax',
    	         url: queryModelTypeUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});*/
        //评估模板
   	 	var mbs = Ext.create('Ext.data.Store', {
            fields: ['type', 'name'],
    	    proxy: {
    	         type: 'ajax',
    	         url: queryModelUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
        });
        var workType = Ext.create('Ext.form.ComboBox', {
            fieldLabel: '工作类型',
            name : 'workType',
            store: states,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'abbr',
            margin: '7 10 0 30',
            columnWidth: .5
        });
        
        //计划编号
        var planCode = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '计划编号',
            margin: '7 10 0 30',
            name: 'planCode',
            columnWidth: .5
        });
        //有效性验证
        /*var effect = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '有效性验证',
            margin: '7 10 0 30',
            name: 'effective',
            columnWidth: .5
        });*/
        //评估模板
   	 	var mb = Ext.create('Ext.form.ComboBox', {
            fieldLabel: '评估模板'+'<font color=red>*</font>',
            allowBlank:false,//不允许为空
            name : 'templateName',
            store: mbs,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'type',
            margin: '7 10 0 30',
            columnWidth: .5
        });
   	 	//负责人
        var reponser = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel: '负责人',
        	name : 'responsName',
        	id : 'responsNameId',
            type:'emp',
            multiSelect:false,
            margin: '7 10 0 30',
            columnWidth: .5
        });
        //联系人
        var contactor = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel: '联系人',
        	name : 'contactName',
        	id : 'contactNameId',
            type:'emp',
            multiSelect:false,
            margin: '7 10 0 30',
            columnWidth: .5
        });
        
        /*var rangeType = Ext.create('Ext.form.ComboBox', {
            fieldLabel: '评估范围类型'+'<font color=red>*</font>',
            allowBlank:false,//不允许为空
            name : 'rangeType',
            store: rangeTypeStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'type',
            triggerAction :'all',
            margin: '7 10 0 30',
            columnWidth: .5
        });*/
        /*var mblx = Ext.create('Ext.form.ComboBox', {
            fieldLabel: '模板类型',
            name : 'templateType',
            store: modelypeStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'type',
            triggerAction :'all',
            margin: '7 10 0 30',
            columnWidth: .5
        });*/
        
        var fieldSet = {
            xtype:'fieldset',
            title: '基础信息',
            collapsible: true,
            defaultType: 'textfield',
            margin: '5 5 0 5',
            layout: {
     	        type: 'column'
     	    },
     	    items : [planName, planCode, workType, contactor, reponser, assessPlanTime, collectRate,mb]
        };
        var fieldSet2 = {
                xtype:'fieldset',
                title: '范围要求',
                collapsible: true,
                defaultType: 'textfield',
                margin: '5 5 0 5',
                layout: {
         	        type: 'column'
         	    },
         	    items : [ rangRequire]
            };
        var fieldSet3 = {
                xtype:'fieldset',
                title: '工作目标',
                collapsible: true,
                defaultType: 'textfield',
                margin: '5 5 0 5',
                layout: {
         	        type: 'column'
         	    },
         	    items : [workTage]
            };
        
        Ext.apply(me, {
        	autoScroll:true,
        	border:false,
            items : [fieldSet,fieldSet3,fieldSet2],
            tbar : {
//				items : []
			}
        });

       me.callParent(arguments);
       if(busid){
       		 me.loadData();
       } 
    }

});