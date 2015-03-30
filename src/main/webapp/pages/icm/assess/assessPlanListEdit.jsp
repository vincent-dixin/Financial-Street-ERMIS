<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内控评价列表</title>
<script type="text/javascript">
	/***attribute start***/
	Ext.define('FHD_icm_assess_assessPlanListEdit',{
    	tbar:[],
    	bbar:[],
    	idSeq:'',
    	upName:'',
    	pagable :false,
    	standardId:'',
    	cols:new Array(),
    	bathSetWindows:null,//批量设置弹出框
    	tbarItems:new Array(),
    	bbarItems:new Array(),
    	processSelectWindow:null,//流程选择弹出框
    	extend:'FHD.ux.EditorGridPanel',
    	initComponent:function(){
    	var me=this;
    	//评价方式
    	var getAssessMeasure='${param.assessMeasure}';
    	
    	var isPracticeTestStore=Ext.create('Ext.data.Store', {
    	    fields: ['value', 'text'],
    	    data : [
    	        {"value":true, "text":"是"},
    	        {"value":false, "text":"否"}
    	    ]
    	});
    	//穿行测试下拉
    	var isPracticeTestCombox=Ext.create('Ext.form.ComboBox', {
    	    store: isPracticeTestStore,
    	    queryMode: 'local',
    	    displayField:'text',
    	    valueField: 'value'
    	});
    	//穿行次数
    	var practiceNumField={
    	        xtype: 'numberfield',
    	        value: 99,
    	        margin:'0 0 0 0',
    	        maxValue: 99,
    	        minValue: 0
    	    };
    	
    	//抽样测试store
    	var isSampleTesttStore=Ext.create('Ext.data.Store', {
    	    fields: ['value', 'text'],
    	    data : [
    	        {"value":true, "text":"是"},
    	        {"value":false, "text":"否"}
    	    ]
    	});
    	//抽样测试下拉
    	var isSampleTestCombox=Ext.create('Ext.form.ComboBox', {
    	    store: isSampleTesttStore,
    	    queryMode: 'local',
    	    displayField:'text',
    	    valueField: 'value'
    	});
    	//抽样次数
    	var isSampleTestNumField={
    	        xtype: 'numberfield',
    	        maxValue: 99,
    	        padding:'0 0 0 0',
    	        margin:'0 0 0 0',
    	        minValue: 1
    	    };
    	//抽样覆盖率
    	var isSampleTestCorverRate={
    	        xtype: 'numberfield',
    	        maxValue:1,
    	        padding:'0 0 0 0',
    	        margin:'0 0 0 0',
    	        minValue: 0
    	    };
    	
        	//header
    	me.cols.push({dataIndex : 'dbid',hidden:true ,flex : 1});
    	me.cols.push({dataIndex : 'text',hidden:true ,flex : 1});
    	me.cols.push({dataIndex : 'processId',hidden:true ,flex : 1});
    	me.cols.push({header : '一级流程分类',dataIndex : 'firstProcessName',sortable : true,flex : 1});
 		me.cols.push({header : '二级流程分类',dataIndex : 'parentProcessName',sortable : true,flex : 1}); 
 		me.cols.push({header : '末级流程分类',dataIndex :'processName',sortable : true});
 		me.cols.push({header : '是否穿行测试',dataIndex :'isPracticeTest',sortable : true,editor:isPracticeTestCombox,renderer:function(value){
 			var index = isPracticeTestStore.find('value',value);
			var record = isPracticeTestStore.getAt(index);
			if(record!=null){
				return record.data.text;
			}else{
				return '';
			}
		}});
 		me.cols.push({header : '穿行次数',dataIndex :'practiceNum',sortable : true,editor:practiceNumField});
 		me.cols.push({header : '是否抽样测试',dataIndex :'isSampleTest',sortable : true,editor:isSampleTestCombox,renderer:function(value){
 			var index = isPracticeTestStore.find('value',value);
			var record = isPracticeTestStore.getAt(index);
			if(record!=null){
				return record.data.text;
			}else{
				return '';
			}
		}});
 		//me.cols.push({header : '抽样次数',dataIndex :'sampleNum',sortable : true,hidden:true,editor:isSampleTestNumField});
 		me.cols.push({header : '抽取样本比例(%)',dataIndex :'coverageRate',sortable : true,editor:isSampleTestCorverRate,renderer:function(value){
 			if(value!=''&&value!=null){
 				var newValue=value*100;//toFixed(0);
 	 			return newValue;
 			}else{
 				return value;
 			}
		}});
    	//tbar
    	me.tbarItems.push({iconCls : 'icon-save',id : 'assessPlanListEditAdd',handler :me.saveAssessRelaProcess,scope : this});
    	me.tbarItems.push({iconCls : 'icon-add',id : 'addAssessRelaProcess',handler :me.addAssessRelaProcess,scope : this});
    	me.tbarItems.push({iconCls : 'icon-del',id : 'assessPlanListEditDel',handler :me.delStandard,scope : this});
    	me.callParent(arguments);
 },
  //保存修改的数据
  saveAssessRelaProcess:function(){
    		var me=this;
    		var jsonArray=[];
    		var rows = me.store.getModifiedRecords();
    		Ext.each(rows,function(item){
    			jsonArray.push(item.data);
    		});
    		if(jsonArray.length>0){
    			 FHD.ajax({//ajax调用
        		     url : __ctxPath+ '/assess/assessPlan/mergeAssessPlanRelaProcessBatch.f',
        		     params : {
        		    	 assessPlanId:'${param.assessPlanId}',
        		    	 modifiedRecord:Ext.encode(jsonArray)
        			 },
        			 callback : function(data) {
        			 if (data) {//删除成功！
        			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
        			 me.store.load();
        		   }
        		   }
        		});
    		}
    	},
    	//新增流程范围
        addAssessRelaProcess:function(){
        	var me=this;
         	var title='流程选择';
         	var selectId='ca_scale_set_measure_business';
			var assessPlanScaleSetMeasure = Ext.create('FHD.ux.dict.DictSelect', {
						dictTypeId : 'ca_scale_set_measure',
						margin : '5 10 0 5',
						labelWidth : 80,
						columnWidth:1,
						name : 'scaleSetMeasure',
						multiSelect : false,
						fieldLabel : '范围选择方式'
					});
			assessPlanScaleSetMeasure.on('afterrender',function(){
				assessPlanScaleSetMeasure.setValue('ca_scale_set_measure_dept');
			});
			//1.按部门选择
			var assessPlanDepart = Ext.create('FHD.ux.process.processRelaOrgSelector', {
				  columnWidth:1,
                  single : false,	 
                  margin: '5 10 0 5',
                  value:'10,11',
                  height :100,
                  labelText: $locale('fhd.process.processselector.labeltext'),
                  labelAlign: 'left',
                  labelWidth: 80
					});
			//2.按流程选择
			var assessPlanRelaProcess = Ext.create('FHD.ux.process.processSelector',{
				   labelText : $locale('fhd.process.processselector.labeltext'),
				   fieldLabel:'流程选择',
				   single : false,
				   labelWidth : 60,
				   parent:false,
				   value:'',
				   margin: '5 0 0 0',
				   columnWidth:.85,
				   height:100
				});
			

        	var fieldSetTop={
					xtype : 'fieldset',
					layout: 'column',
					margin: '5 10 0 5',
					columnWidth:1,
					collapsed : false,
					collapsible : false,
					title : '范围选择',
					items : [assessPlanScaleSetMeasure]
				};
			
			//分析工具按钮
			var  feixiToolButton={
			        xtype:'button',
			        columnWidth:.15,
			        text:'分析工具',
			        height:100,
			        margin: '5 0 0 0',
			        handler:function(){
			        	var processAnalysisToolWindow=Ext.create('FHD.ux.process.processAnalysisToolWindow',{onSubmit : function(values) {
	        				assessPlanRelaProcess.setValue(values);
						}}
			        			);
			        	processAnalysisToolWindow.show();
			        }
			};
        	
        	var fieldSetBottom={
					xtype : 'fieldset',
					margin: '5 10 0 5',
					layout: 'column',
					columnWidth:1,
					height:230,
					collapsed : false,
					collapsible : false,
					title :title,
					items : [assessPlanRelaProcess,feixiToolButton]
				};
        	
        	var toolSetBottom={
					xtype : 'fieldset',
					margin: '5 10 0 5',
					layout: 'column',
					columnWidth:1,
					height:250,
					collapsed : false,
					collapsible : false,
					title :title,
					items : [assessPlanDepart]
				};
        	
        	
        	 //点击选择弹出框
        	  var windows=Ext.create('Ext.window.Window', {
	        	    title:'选择',
	        	    layout: 'column',
	        	    columnWidth:1,
	        	    collapsible:true,
	        	    modal : true,
	        	    maximizable:true,
	        	    width: 700,
	        	    height:300,
	        	    items:[fieldSetBottom,toolSetBottom],
	        	    dockedItems:{
			            xtype: 'toolbar',
			            dock: 'bottom',
			            ui: 'footer',
			            items: ['->',
			                    {text: '确定',handler: function(){
			                    	if(assessPlanRelaProcess.getValue()==''||assessPlanRelaProcess.getValue()==undefined){
			                    	  return;
			                    	}
		                		    FHD.ajax({//ajax调用
		        	        		     url : __ctxPath+ '/assess/assessPlan/saveAssessPlanRelaProcesses.f',
		        	        		     params : {
		        	        		    	 assessMeasureId:'${param.assessMeasureId}',
		        	        		    	 processIds:assessPlanRelaProcess.getValue(),
		        	        		    	 assessPlanDepartIds:'',
		        	        		    	 selectType:'ca_scale_set_measure_business',
		        	        		    	 assessPlanId:'${param.assessPlanId}'
		        	        			 },
		        	        			 callback : function(data) {
		        	        			 if (data) {
		        	        		     windows.close();
		        	        			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		        	        			 me.store.load();
		        	        		   }
		        	        		   }
		        	        		});
			                    } 
			                    },{text: '取消',handler: function(){
			                    	windows.close();
				                	}
					                }]
			             }
					
	        	}).show();
        	  //分析工具弹出框
        	  var toolWindows=Ext.create('Ext.window.Window', {
	        	    title:'选择',
	        	    layout: 'column',
	        	    columnWidth:1,
	        	    collapsible:true,
	        	    modal : true,
	        	    maximizable:true,
	        	    width: 700,
	        	    height:400,
	        	    items:[fieldSetTop,toolSetBottom],
	        	    dockedItems:{
			            xtype: 'toolbar',
			            dock: 'bottom',
			            ui: 'footer',
			            items: ['->',
			                    {text: '确定',handler: function(){
			                    	if(assessPlanScaleSetMeasure.getValue()=='ca_scale_set_measure_dept'){//按部门选择
			                    		if(assessPlanDepart.getValue()==''||assessPlanDepart.getValue()==undefined){
			                    			return;
			                    		}
			                    	}
		                		    FHD.ajax({//ajax调用
		        	        		     url : __ctxPath+ '/assess/assessPlan/saveAssessPlanRelaProcesses.f',
		        	        		     params : {
		        	        		    	 assessMeasureId:'${param.assessMeasureId}',
		        	        		    	 processIds:'',
		        	        		    	 assessPlanDepartIds:assessPlanDepart.getValue(),
		        	        		    	 selectType:assessPlanScaleSetMeasure.getValue(),
		        	        		    	 assessPlanId:'${param.assessPlanId}'
		        	        			 },
		        	        			 callback : function(data) {
		        	        			 if (data) {
		        	        			 windows.close();
		        	        		     toolWindows.close();
		        	        			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		        	        			 me.store.load();
		        	        		   }
		        	        		   }
		        	        		});
			                    } 
			                    },{text: '取消',handler: function(){
			                    	windows.close();
			                    	toolWindows.close();
				                	}
					            }]
			             }
	        	});
        },
        //删除流程范围
        delStandard:function(){
        	var me=this;
        	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    		Ext.MessageBox.show({
    			title : FHD.locale.get('fhd.common.delete'),
    			width : 260,
    			msg : FHD.locale.get('fhd.common.makeSureDelete'),
    			buttons : Ext.MessageBox.YESNO,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn) {
    		         if (btn == 'yes') {//确认删除
    			        var ids = [];
    		            var processIds=new Array();
    			        for ( var i = 0; i < selection.length; i++) {
    			            ids.push(selection[i].get('id'));
    			            processIds.push(selection[i].get('processId'));
    			        }
    			     FHD.ajax({//ajax调用
    		                 url : __ctxPath+ '/assess/assessList/removeAssessPlanRelaProcesses.f',
    		                 params : {
    		                          assessPlanRelaProcessIds : ids,
    		                          processIds:processIds,
    		                          assessPlanId:'${param.assessPlanId}'
    			                      },
    			             callback : function(data) {
    			                           if (data) {//删除成功！
    			                              Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
    			                              me.store.load();
    		                                }
    		                            }
    		        });
    		      }
    			}
    		});
        }
    });
	
	
	
	//定义包含的容器
	Ext.define('FHD_icm_assess_assessPlanListEditFormPanel',{
		extend:'Ext.form.Panel',
		renderTo:'FHD.icm.assess.assessPlanListEdit${param._dc}',
		height : FHD.getCenterPanelHeight(),
		width : FHD.getCenterPanelWidth(),
		autoScroll:true,
		layout : {
			type : 'column'
			},
		basicInfo:null,
	    items :new Array(),
		assessPlanRelaProcessGrid:{},
		initComponent:function(){
			var me=this;
		    //流程范围===============================
		    me.assessPlanRelaProcessGrid=Ext.create('FHD_icm_assess_assessPlanListEdit',{
		    	url:__ctxPath+ '/icm/assess/findAssessPlanRelaProcessListByPage.f?assessPlanId=${param.assessPlanId}',//调用后台url'
			    height : FHD.getCenterPanelHeight()-150,
			    width:FHD.getCenterPanelWidth()-80
			});
		    FHD.componentResize(me.assessPlanRelaProcessGrid,80,100);
		    me.assessPlanRelaProcessGrid.store.on("load",function(store){
		  	    Ext.getCmp('assessPlanListEditDel').setDisabled(true);//删除
			 });
		    me.assessPlanRelaProcessGrid.on('selectionchange',function(m) {
			        var len=me.assessPlanRelaProcessGrid.getSelectionModel().getSelection().length; 
			        if (len== 0) {
			           Ext.getCmp('assessPlanListEditDel').setDisabled(true);
			        } else if (len == 1||len > 1) {
				       Ext.getCmp('assessPlanListEditDel').setDisabled(false);//删除
			  	      if('${param.assessMeasureId}'=='ca_assessment_measure_0'){//穿行
			  	      }else if('${param.assessMeasureId}'=='ca_assessment_measure_1'){//抽样
			  	      }
			        }
			 });
		    me.basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation',{columnWidth:1/1});
		    me.items.push(me.basicInfo);
		    var childItems={xtype : 'fieldset',margin: '7 10 0 30',columnWidth:1/1,layout : {type : 'column'},collapsed : false,collapsible : true,title : '范围',items : [me.assessPlanRelaProcessGrid]};
		    me.items.push(childItems);
		    me.callParent(arguments);
		},  nextStep:function(){
			var assessMeasureId='${param.assessMeasureId}';
	    	var assessPlanId='${param.assessPlanId}';
	    	fhd_icm_assess_assessPlanPanelManager.remove(fhd_icm_assess_assessPlanPanelManager.centerContainer);
	    	fhd_icm_assess_assessPlanPanelManager.setCenterContainer('pages/icm/assess/assessPlanControlProcessManager.jsp?assessPlanId='+assessPlanId);
	    	fhd_icm_assess_assessPlanPanelManager.add(fhd_icm_assess_assessPlanPanelManager.centerContainer);
	  },
	  upSetp:function(){
		  var assessMeasureId='${param.assessMeasureId}';
	  	  var assessPlanId='${param.assessPlanId}';
	  	  fhd_icm_assess_assessPlanPanelManager.remove(fhd_icm_assess_assessPlanPanelManager.centerContainer);
	  	  fhd_icm_assess_assessPlanPanelManager.setCenterContainer('pages/icm/assess/assessPlanEdit.jsp?initForm=1&assessPlanId='+assessPlanId+'&assessMeasureId='+assessMeasureId);
	  	  fhd_icm_assess_assessPlanPanelManager.add(fhd_icm_assess_assessPlanPanelManager.centerContainer);
	  }
	})
    var fhd_icm_assess_assessPlanListEdit=null;
	Ext.onReady(function() {
		fhd_icm_assess_assessPlanListEdit=Ext.create('FHD_icm_assess_assessPlanListEditFormPanel');
		
		FHD.componentResize(fhd_icm_assess_assessPlanListEdit,0,0);
		
		fhd_icm_assess_assessPlanListEdit.getForm().load({
                              url:__ctxPath+'/assess/assessPlan/findAssessPlanForView.f?assessPlanId=${param.assessPlanId}',
                              success: function (form, action) {
                           	   return true;
                              },
                              failure: function (form, action) {
                           	   return true;
                              }
   });
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessPlanListEdit${param._dc}'></div>
</body>
</html>