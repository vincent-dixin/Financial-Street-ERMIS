Ext.define('FHD.view.risk.risk.RiskEditBasicFormView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.riskEditBasicFormView',
    autoScroll: true,
    requires:[
              
             ],
    border: false,
    isEdit:false,	   //标记是否是编辑，true是，false为添加状态
    isRiskClass:'rbs', //风险还是风险事件  rbs:风险  re：风险事件
    /**
     * 生成编码函数
     */
    createCode:function(){
    	var me = this;
    	var vform = me.getForm();

//        var vobj = vform.getValues();
//        var paraobj = {};
//        paraobj.currentSmId = me.paramObj.smid;
//        if (vobj.parentId != "") {
//            paraobj.parentid = vobj.parentId;
//            FHD.ajax({
//                url: __ctxPath + '/kpi/kpistrategymap/findcodebyparentid.f',
//                params: {
//                    param: Ext.JSON.encode(paraobj)
//                },
//                callback: function (data) {
//                    if (data && data.success) {
//                        vform.setValues({
//                            code: data.code
//                        });
//                    }
//                }
//            });
//        }
    },
    addBasicComponent: function () {
        var me = this;
        me.basicfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            defaults: {
                margin: '3 30 3 30',
                height:24,
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.common.baseInfo')
        });

        me.parentId = Ext.create('FHD.ux.treeselector.TreeSelector',{
        	title:'请您选择风险',
    		columns: [{dataIndex: 'code',header: '风险编号'},{dataIndex: 'name',header: '风险名称',isPrimaryName:true}],
//    		treeUrl: '/risk/riskTreeLoader?rbs=true&canChecked=true',
    		entityName:'com.fhd.risk.entity.Risk',
    		parentKey:'parent.id',
    		relationKey:'idSeq',
    		checkable:true,
    		parameters:'{"isRiskClass":"rbs","company.id":"eda8ffeab0da4159be0ff924108e3883","deleteStatus":"1"}',
    		//value:'[{id:"CW"}]',
        	fieldLabel : '上级风险' + '<font color=red>*</font>',
        	labelAlign: 'left',
            multiSelect : false,
            name: 'parentId',
            columnWidth: .5
        });
        me.basicfieldSet.add(me.parentId);

        //编码
        var code = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: '风险编号',
            margin: '3 3 3 30',
            name: 'code',
            maxLength: 255,
            columnWidth: .4
        });
        me.basicfieldSet.add(code);
        
        //自动生成编码按钮
        var codebtn = Ext.widget('button', {
            xtype: 'button',
            margin: '3 30 3 3',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'), //自动生成编码按钮
            handler: function () {
                me.createCode();
            },
            columnWidth: .1
        });
        me.basicfieldSet.add(codebtn);
        
        //风险名称
        var name = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 2,
            fieldLabel: '风险名称'+ '<font color=red>*</font>',
            margin: '7 30 3 30', 
            name: 'name',
            height:40,
            columnWidth: .5
        });
        me.basicfieldSet.add(name);
        
        //风险描述
        var desc = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 2,
            fieldLabel: '动因描述',
            margin: '7 30 3 30', 
            name: 'desc',
            height:40,
            columnWidth: .5
        });
        me.basicfieldSet.add(desc);
        
        //责任部门/人
        me.respDeptName = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel : '责任部门/人',
        	labelAlign: 'left',
        	type : 'dept_emp',
        	subCompany: true,
            multiSelect : true,
            margin: '7 30 3 30', 
            name: 'respDeptName',
            height:40,
            columnWidth: .5
        });
        me.basicfieldSet.add(me.respDeptName);
        
        //相关部门/人
        me.relaDeptName = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel : '相关部门/人',
        	labelAlign: 'left',
        	type : 'dept_emp',
        	subCompany: true,
            multiSelect : true,
            margin: '7 30 3 30', 
            name: 'relaDeptName',
            height:40,
            columnWidth: .5
        });
        me.basicfieldSet.add(me.relaDeptName);

        //影响指标
        me.influKpiName = Ext.widget('kpioptselector', {
		 	labelWidth: 100,
		 	gridHeight:25,
		 	btnHeight:25,
		 	btnWidth:22,
		 	multiSelect:true,
		 	labelAlign: 'left',
		 	labelText: '影响指标',
            margin: '7 30 3 30', 
            name: 'influKpiName',
            height:40,
            columnWidth: .5
        });
        me.basicfieldSet.add(me.influKpiName);
        
        //影响流程
        me.influProcessureName = Ext.create('FHD.ux.process.processSelector', {
		 	labelWidth: 95,
		 	gridHeight:25,
		 	btnHeight:25,
		 	btnWidth:25,
		 	single : false,
		 	fieldLabel: '影响流程',
            margin: '7 30 3 30', 
            name: 'influProcessureName',
            multiSelect:true,
            height:40,
            columnWidth: .5
        });
        me.basicfieldSet.add(me.influProcessureName);
        
        //风险类别  字典多选 综合类/流程类
        var riskKind = Ext.create('FHD.ux.dict.DictCheckbox',{
          	name:'riskKind',
          	dictTypeId:'rm_response_strategy',
          	labelAlign:'left',
          	fieldLabel : '风险类别',
          	margin: '7 30 3 30',
          	columnWidth: .5
          });
        me.basicfieldSet.add(riskKind);
          
        //是否定量
        var isFix =Ext.widget('dictradio',{
        	name:'isFix',
        	dictTypeId:'0yn',
        	defaultValue: '0yn_n',
        	labelAlign:'left',
        	fieldLabel : '定量分析',
        	multiSelect:false,
        	margin: '7 30 3 30',
        	columnWidth: .5
        });
        me.basicfieldSet.add(isFix);
        
        //是否启用
        var isUse =Ext.widget('dictradio',{
        	xtype: 'dictradio',
        	name:'isUse',
        	dictTypeId:'0yn',
        	defaultValue: '0yn_y',
        	labelAlign:'left',
        	fieldLabel : '是否启用',
        	multiSelect:false,
        	margin: '7 30 3 30',
        	columnWidth: .5
        });
        me.basicfieldSet.add(isUse);
        
        var sort = Ext.widget('numberfield', {
			xtype : 'numberfield',
			rows : 2,
			fieldLabel : '序号',
			margin : '7 30 3 30',
			name : 'sort',
			columnWidth : .5
		});
		me.basicfieldSet.add(sort);
		
        me.add(me.basicfieldSet);
    },
    addRelaComponent: function () {
        var me = this;
        me.relafieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: '相关信息'
        });
        
        //风险指标
        me.riskKpiName = Ext.widget('kpioptselector', {
		 	labelWidth: 100,
		 	gridHeight:25,
		 	btnHeight:25,
		 	btnWidth:22,
		 	multiSelect:true,
		 	labelText: '风险指标',
		 	labelAlign: 'left',
            margin: '7 30 3 30', 
            name: 'riskKpiName',
            height:40,
            columnWidth: .5
        });
        me.relafieldSet.add(me.riskKpiName);
 
        //控制流程
        me.controlProcessureName = Ext.create('FHD.ux.process.processSelector', {
		 	labelWidth: 95,
		 	columnWidth:.5,
		 	gridHeight:25,
		 	btnHeight:25,
		 	single : false,
		 	fieldLabel: '控制流程',
            margin: '7 30 3 30', 
            name: 'controlProcessureName',
            multiSelect:true,
            height:40,
            columnWidth: .5
        });

        me.relafieldSet.add(me.controlProcessureName);
 
      //告警方案
        var alarmScenarioStore = Ext.create('Ext.data.Store',{
			fields:['id','name'],
        	remoteSort:true,
        	proxy: {
		        type: 'ajax',
		        url:__ctxPath + "/kpi/alarm/findriskalarmplan.f",
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
		});
        alarmScenarioStore.load();
        var alarmPlanId = Ext.create('Ext.form.ComboBox', {
        	name:'alarmPlanId',
            store: alarmScenarioStore,
            displayField: 'name',
            valueField: 'id',
        	labelAlign:'left',
        	fieldLabel : '告警方案',
        	multiSelect:false,
        	margin: '7 30 3 30',
        	columnWidth: .5
        });
        me.relafieldSet.add(alarmPlanId);
        
        // 监控频率
		me.monitorFrequence = Ext.widget('collectionSelector', {
			name : 'monitorFrequence',
			xtype : 'collectionSelector',
			label : '监控频率',
			valueDictType : '',
			single : false,
			value : '',
			columnWidth : .5,
			margin: '7 30 3 30',
			allowBlank : true
		});
		me.relafieldSet.add(me.monitorFrequence);

        //继承上级模板
        var isInherit =Ext.widget('dictradio',{
        	name:'isInherit',
        	dictTypeId:'0yn',
        	defaultValue: '0yn_y',
        	labelAlign:'left',
        	fieldLabel : '是否继承',//上级模板
        	columnWidth: .5
        });
        me.relafieldSet.add(isInherit);
 
        //评估模板
        var templateNameStore = Ext.create('Ext.data.Store',{
        	
			fields:['id','name'],
        	remoteSort:true,
        	proxy: {
		        type: 'ajax',
		        url:__ctxPath + "/risk/findTemplateList.f",
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
		});
        templateNameStore.load();
        me.templateName = Ext.create('Ext.form.ComboBox', {
        	name : 'templateId',
            store: templateNameStore,
            displayField: 'name',
            valueField: 'id',
        	labelAlign:'left',
        	fieldLabel : '评估模板',
        	multiSelect:false,
        	margin: '7 30 3 30',	//emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示
        	triggerAction :'all',
        	columnWidth: .5
        });
        me.relafieldSet.add(me.templateName);
   
      // 计算公式
		me.formulaDefine = Ext.create('FHD.ux.kpi.FormulaTrigger', {
					fieldLabel : "计算公式",
					hideLabel : false,
					emptyText : '',
					labelAlign : 'left',
					flex : 1.5,
					labelWidth : 100,
					cols : 20,
					margin : '7 30 3 30',
					rows : 5,
					name : 'formulaDefine',
					type : 'kpi',
					showType : 'all',
					column : 'assessmentValueFormula',
					columnWidth : .5
				});
		me.relafieldSet.add(me.formulaDefine);

		// 涉及板块
		var relePlate = Ext.create('FHD.ux.dict.DictCheckbox', {
					name : 'relePlate',
					dictTypeId : 'rm_response_strategy',
					labelAlign : 'left',
					fieldLabel : '涉及板块',
					margin : '7 30 3 30',
					columnWidth : .5
				});
		me.relafieldSet.add(relePlate);
	      
        me.add(me.relafieldSet);
    },
    addExtendComponent: function () {
        var me = this;
        me.extendfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            collapsed:true,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: '扩展信息'
        });
        
      //风险动因
      var riskReason = Ext.create('FHD.ux.treeselector.TreeSelector',{
      	title:'请您选择风险动因',
  		columns: [{dataIndex: 'code',header: '风险编号'},{dataIndex: 'name',header: '风险名称',isPrimaryName:true}],
  		entityName:'com.fhd.risk.entity.Risk',
  		parentKey:'parent.id',
  		relationKey:'idSeq',
  		//value:'[{id:"XD00"},{id:"eda8ffeab0da4159be0ff924108e3883"}]',
      	fieldLabel : '风险动因',
      	labelAlign: 'left',
      	multiSelect : false,
      	//parameters:'{orgStatus:"1"}',
      	name: 'riskReason',
      	columnWidth: .5
      });
      me.extendfieldSet.add(riskReason);
      
      //风险结果
      var riskResult = Ext.create('FHD.ux.treeselector.TreeSelector',{
      	title:'请您选择风险结果',
  		columns: [{dataIndex: 'code',header: '风险编号'},{dataIndex: 'name',header: '风险名称',isPrimaryName:true}],
  		entityName:'com.fhd.risk.entity.Risk',
  		parentKey:'parent.id',
  		relationKey:'idSeq',
  		//value:'[{id:"XD00"},{id:"eda8ffeab0da4159be0ff924108e3883"}]',
      	fieldLabel : '风险结果',
      	labelAlign: 'left',
      	multiSelect : false,
      	//parameters:'{orgStatus:"1"}',
      	name: 'riskResult',
      	columnWidth: .5
      });
      me.extendfieldSet.add(riskResult);
      	
		// 趋势相对于
		var relativeTo = Ext.create('FHD.ux.dict.DictSelect', {
			name : 'relativeTo',
			dictTypeId : 'kpi_relative_to',
			labelAlign : 'left',
			fieldLabel : '趋势相对于',
			multiSelect : false,
			margin : '7 30 3 30',
			columnWidth : .5
		});
		me.extendfieldSet.add(relativeTo);

	      //扩展
	      var extend9 = Ext.create('FHD.ux.dict.DictCheckbox',{
	      	name:'extend9',
	      	dictTypeId:'rm_response_strategy',
	      	labelAlign:'left',
	      	fieldLabel : '扩展',
	      	margin: '7 30 3 30',
	      	columnWidth: .5
	      });
	      me.extendfieldSet.add(extend9);
	      
 		// 收集频率
		me.gatherFrequence = Ext.widget('collectionSelector', {
			name : 'gatherFrequence',
			xtype : 'collectionSelector',
			label : '收集频率',
			valueDictType : '',
			single : false,
			value : '',
			columnWidth : .5,
			margin: '7 30 3 30',
			allowBlank : true
		});
		me.extendfieldSet.add(me.gatherFrequence);

		// 数据收集延期天
		var resultCollectInterval = Ext.widget('numberfield', {
			xtype : 'numberfield',
			rows : 2,
			fieldLabel : '数据收集延期天',
			margin : '7 30 3 30',
			name : 'resultCollectInterval',
			columnWidth : .5
		});
		me.extendfieldSet.add(resultCollectInterval);

	      
	      //责任岗位
	      var respPositionName = Ext.widget('textfield', {
	          xtype: 'textfield',
	          fieldLabel: '责任岗位',
	          labelAlign: 'left',
	          margin: '7 30 3 30',
	          name: 'respPositionName',
	          columnWidth: .5
	      });

	      me.extendfieldSet.add(respPositionName);

	      //相关岗位
	      var relaPositionName = Ext.widget('textfield', {
	          xtype: 'textfield',
	          fieldLabel: '相关岗位',
	          labelAlign: 'left',
	          margin: '7 30 3 30',
	          name: 'relaPositionName',
	          columnWidth: .5
	      });
	      me.extendfieldSet.add(relaPositionName);
	      
		// 扩展5
		var extend5 = Ext.widget('textfield', {
			xtype : 'textfield',
			fieldLabel : '扩展5',
			labelAlign : 'left',
			margin : '7 30 3 30',
			name : 'extend5',
			columnWidth : .5
		});
		me.extendfieldSet.add(extend5);

		// 扩展6
		var extend6 = Ext.widget('textfield', {
			xtype : 'textfield',
			fieldLabel : '扩展6',
			labelAlign : 'left',
			margin : '7 30 3 30',
			name : 'extend6',
			columnWidth : .5
		});
		me.extendfieldSet.add(extend6);
		
		//应对策略
	      var responseStrategy = Ext.create('FHD.ux.dict.DictCheckbox',{
	      	name:'responseStrategy',
	      	dictTypeId:'rm_response_strategy',
	      	labelAlign:'left',
	      	fieldLabel : '应对策略',
	      	margin: '7 30 3 30',
	      	columnWidth: .5
	      });
	      me.extendfieldSet.add(responseStrategy);
	      
	      //风险类型
	      var riskType = Ext.create('FHD.ux.dict.DictCheckbox',{
	      	name:'riskType',
	      	dictTypeId:'rm_response_strategy',
	      	labelAlign:'left',
	      	fieldLabel : '风险类型',
	      	margin: '7 30 3 30',
	      	columnWidth: .5
	      });
	      me.extendfieldSet.add(riskType);
	      
	      //价值链
	      var valueChain = Ext.create('FHD.ux.dict.DictCheckbox',{
	      	name:'valueChain',
	      	dictTypeId:'rm_response_strategy',
	      	labelAlign:'left',
	      	fieldLabel : '价值链',
	      	margin: '7 30 3 30',
	      	columnWidth: .5
	      });
	      me.extendfieldSet.add(valueChain);
	      
	      //风险要素
	      var riskFactor = Ext.create('FHD.ux.dict.DictCheckbox',{
	      	name:'riskFactor',
	      	dictTypeId:'rm_response_strategy',//rm_response_strategy
	      	labelAlign:'left',
	      	fieldLabel : '风险要素',
	      	margin: '7 30 3 30',
	      	columnWidth: .5
	      });
	      me.extendfieldSet.add(riskFactor);
	      
		// 扩展1
		var extend1 = Ext.create('FHD.ux.dict.DictCheckbox', {
			name : 'extend1',
			dictTypeId : 'rm_response_strategy',
			labelAlign : 'left',
			fieldLabel : '扩展1',
			margin : '7 30 3 30',
			columnWidth : .5
		});
		me.extendfieldSet.add(extend1);

		// 扩展2
		var extend2 = Ext.create('FHD.ux.dict.DictCheckbox', {
			name : 'extend2',
			dictTypeId : 'rm_response_strategy',
			labelAlign : 'left',
			fieldLabel : '扩展2',
			margin : '7 30 3 30',
			columnWidth : .5
		});
		me.extendfieldSet.add(extend2);

		// 扩展3
		var extend3 = Ext.create('FHD.ux.dict.DictCheckbox', {
			name : 'extend3',
			dictTypeId : 'rm_response_strategy',
			labelAlign : 'left',
			fieldLabel : '扩展3',
			margin : '7 30 3 30',
			columnWidth : .5
		});
		me.extendfieldSet.add(extend3);

		// 扩展4
		var extend4 = Ext.create('FHD.ux.dict.DictCheckbox', {
			name : 'extend4',
			dictTypeId : 'rm_response_strategy',
			labelAlign : 'left',
			fieldLabel : '扩展4',
			margin : '7 30 3 30',
			columnWidth : .5
		});
		me.extendfieldSet.add(extend4);
      
	      //影响期间
	      var impactTime =Ext.create('FHD.ux.dict.DictCheckbox',{//DictCheckbox DictSelect
	    	name:'impactTime',
	    	dictTypeId:'influence_period',
	    	labelAlign:'left',
	    	fieldLabel : '影响期间',
	    	multiSelect:false,
	    	margin: '7 30 3 30',
	    	//height:80,
	    	columnWidth: .5
	      });
	      me.extendfieldSet.add(impactTime);
	      
	        
        //是否应对
        var isAnswer =Ext.widget('dictradio',{
        	xtype: 'dictradio',
        	name:'isAnswer',
        	dictTypeId:'0yn',
        	labelAlign:'left',
        	fieldLabel : '是否应对',
        	multiSelect:false,
        	margin: '7 30 3 30',
        	columnWidth: .5
        });
        me.extendfieldSet.add(isAnswer);
        // 亮灯依据
		// var alarmMeasure =Ext.create('FHD.ux.dict.DictSelect',{
		// name:'alarmMeasure',
		// dictTypeId:'kpi_alarm_measure',
		// labelAlign:'left',
		// fieldLabel : '亮灯依据',
		// multiSelect:false,
		// margin: '7 30 3 30',
		// columnWidth: .5
		// });
		// me.extendfieldSet.add(alarmMeasure);
      
      me.add(me.extendfieldSet);
    },
    
    editId:'',	// 编辑的Id
    // 保存
    last: function(){
    	var me = this;
    	
    	var saveUrl = '/risk/risk/saveRiskInfo.f';
		var form = me.getForm();
//		var tab = me.face.tabpanel;
//		var eventGrid = me.face.tabs[1];
//        tab.setActiveTab(eventGrid);
        //上级风险Id
        //var parentId = Ext.decode(me.parentId.getValue())[0].id;//组件得到的值是一个数组
		//风险指标
		var riskKpiName=me.riskKpiName.getFieldValue();
		//影响指标
		var influKpiName=me.influKpiName.getFieldValue();
		//控制流程
        var controlProcessureName=me.controlProcessureName.getValue();
        //影响流程
        var influProcessureName=me.influProcessureName.getValue();
		//公式定义
		var formulaDefine=me.formulaDefine.getValue();
		//监控频率
		var valueStr = me.monitorFrequence.getValue();
		var valueCron = me.monitorFrequence.valueCron;
		var valueDictType = me.monitorFrequence.valueDictType;
		var valueRadioType = me.monitorFrequence.valueRadioType;
		var monitorFrequence = valueStr+"@"+valueCron+"@"+valueDictType+"@"+valueRadioType;

		if(form.isValid()){
			if(me.isEdit){	//修改
				FHD.submit({
					form:form,
					params: {
						//isRiskClass:"rbs",//me.isRiskClass,	//风险还是风险事件
						riskKpiName:riskKpiName,
						influKpiName:influKpiName,
						controlProcessureName:controlProcessureName,
						influProcessureName:influProcessureName,
						monitorFrequence:monitorFrequence,
						formulaDefine:me.formulaDefine.getValue()
		            },
					url:__ctxPath + saveUrl + '?id=' + me.editId,
					callback:function(data){
						me.face.tree.treeArr[0].store.load();
					}
				});
			}else{	//添加
				FHD.submit({
					form:form,
					url:__ctxPath + saveUrl,
					params: {
						isRiskClass:me.isRiskClass,	//风险还是风险事件
						riskKpiName:riskKpiName,
						influKpiName:influKpiName,
						controlProcessureName:controlProcessureName,
						influProcessureName:influProcessureName,
						monitorFrequence:monitorFrequence,
						formulaDefine:me.formulaDefine.getValue()
		            },
					callback:function(data){
						me.face.tree.treeArr[0].store.load();
					}
				});	
			}
		}
	},
    initComponent: function () {
        var me = this;
        
        Ext.applyIf(me, {
            autoScroll: true,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3"
        });
        me.callParent(arguments);
        
        me.addBasicComponent();
        me.addRelaComponent();
        me.addExtendComponent();
    },
    reSetData: function (json) {
        var me = this;
        me.getForm().reset();
        //清空组件值
//        me.respDeptName.clearValues();
//        me.relaDeptName.clearValues();
//		me.controlProcessureName.clearValues();
//		me.influProcessureName.clearValues();
		//me.influKpiName

        //设置初始值
        me.getForm().setValues({
			parentId:json.parentId,//设置父ID和父名称
			isInherit:json.isInherit,
			templateId:json.templateId,
            relativeTo:json.relativeTo,
            formulaDefine:json.formulaDefine,
            alarmPlanId:json.alarmPlanId,
			isFix:'0yn_n',
			isUse:'0yn_y'
		});
		//上级风险
		me.parentId.initValue();
    },
    reLoadData: function (json) {
        var me = this;
		me.form.setValues({
					parentId : json.parentId,
					code : json.code,
					name : json.name,
					desc : json.desc,
					riskKind : json.riskKind,
					isFix : json.isFix,
					isUse : json.isUse,
					alarmPlanId : json.alarmPlanId,
					respDeptName : json.respDeptName,
					relaDeptName : json.relaDeptName,
					controlProcessureName : json.controlProcessureName,
					influProcessureName : json.influProcessureName,
					isInherit : json.isInherit,
					templateId : json.templateId,
					formulaDefine : json.formulaDefine, //计算公式
					relePlate : json.relePlate
				});

		//上级风险
		me.parentId.initValue();
		//监控频率
		var monitorFrequenceStr = json.monitorFrequence || '';
		var monitorFrequenceArr = monitorFrequenceStr.split('@');
		if (monitorFrequenceArr.length == 4) {
			me.monitorFrequence.setValue("每周,期间的最后一个星期二");
			me.monitorFrequence.valueCron = "0 0 0 ? * Tuesday";
			me.monitorFrequence.valueDictType = "0frequecy_week";
			me.monitorFrequence.valueRadioType = "6,星期二";
		} else {
			me.monitorFrequence.setValue(monitorFrequenceArr[0]);
		}

		//影响指标
		me.influKpiName.initGridStore(json.influKpiName);
		//风险指标
		me.riskKpiName.initGridStore(json.riskKpiName);
    }
});