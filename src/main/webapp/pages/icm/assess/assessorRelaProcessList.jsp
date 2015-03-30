<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
/***function start***/
var basePanel;
var processId;
var testGrid;
var assessResultId;
var isQualified;

var isAccordStore = Ext.create('Ext.data.Store',{//myLocale的store
	fields : ['id', 'name'],
	data : [{'id' : 'Y','name' : '完全符合'},{'id' : 'YORN','name' : '部分符合'},
	{'id' : 'N','name' : '不符合'},{'id' : 'NAN','name' : '不适用'}]
}); 
var isGoodStore = Ext.create('Ext.data.Store',{//myLocale的store
	fields : ['id', 'name'],
	data : [{'id' : 'true','name' : '是'},{'id' : 'false','name' : '否'}]
});


var calculate={
	xtype:'displayfield',
	id:'calculatefield',
	fieldLabel: '机算(是否符合)',
	margin: '7 10 0 30',
	columnWidth : 1 / 1
};


	var combo= Ext.create('Ext.form.ComboBox',{
	fieldLabel: '调整(是否符合)',
	store :isAccordStore,
	valueField : 'id',
	margin: '7 10 0 30',
	columnWidth : 1 / 2,
	name:'combo',
	displayField : 'name',
	selectOnTab: true,
	lazyRender: true,
	typeAhead: true,
	allowBlank : false,
	blankText:'--请选择--',
	editable : false
});

var testDesc={
       xtype: "textarea",
       margin: '7 10 0 30',
       columnWidth : 1 / 2,
       fieldLabel: "评价说明",
       id: "memo",
       name:"desc",
       labelSepartor: "：",
       value:""
       };
       
 var practiceTestListfieldset={
		 id:'practiceTestListfieldset',
		xtype : 'fieldset',
		margin: '7 10 0 30',
		layout : {
			type : 'column'
		},
		collapsed : false,
		collapsible : true,
		title : '评价流程',
		items : [calculate,combo,testDesc]
	   };

function editSampleWindow(){
	var viewwindow = new Ext.Window({
		title:'内控评价通知',
    	layout:'fit',
		modal:true,//是否模态窗口
		collapsible:true,
		width:750,
		height:600,
		maximizable:true,//（是否增加最大化，默认没有）
		items:[Ext.create('Ext.container.Container',{
	 		autoLoad : {
	 			url : 'pages/icm/assess/editSample.jsp?assessResultId='+assessResultId+'&isQualified='+isQualified,
	 			scripts : true
	 		}
	 	})],
	 	buttons: [{ text: '保存' ,handler:function(){
	 		var rows = sampleGrid.store.getModifiedRecords();
	 		var jsonArray=[];
	 		Ext.each(rows,function(item){
	 				jsonArray.push(item.data);
	 		});
	 		var jsonString=Ext.encode(jsonArray);
	 		FHD.ajax({
				url:'icm/assess/mergeAssessSampleBatch.f',
                params: {
                	jsonString:jsonString,
                	assessResultId:assessResultId
                },
                callback: function (data) {
                	if(data){
                		sampleGrid.store.load();
                	}
                }
			});		
	 	}},{ text: '取消' ,handler:function(){
	 		viewwindow.close();
	 	}}]
		
    }).show();
}
function addPracticeTestList(){
	setValueForCalculate();
		testGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		columnWidth : 1,
		autoDestroy:true,
		multiSelect:false,
		//pagable:false,
		checked:false,
		url: 'icm/assess/findAssessResultPageBySome.f?testType=ca_assessment_measure_0&assessorId=${param.assessorId}&processId='+processId,
		cols:[{dataIndex:'assessResultId',hidden:true},
		      {dataIndex:'yCount',hidden:true},
		      {dataIndex:'nCount',hidden:true},
		      {dataIndex:'nanCount',hidden:true},
		      {header:'流程节点', dataIndex: 'processPointName', sortable: false,flex:1},
		      {header:'评价点', dataIndex: 'assessPointDesc',sortable: false,flex:1},
		      {header:'实施证据', dataIndex: 'assessSampleName',sortable: false,flex:1},
		      {header:'是否存在缺陷', dataIndex: 'hasDefect',sortable: false,editor:new Ext.form.ComboBox({
		    	  store :isGoodStore,
		    	  valueField : 'id',
		    	  displayField : 'name',
		    	  selectOnTab: true,
		    	  lazyRender: true,
		    	  typeAhead: true,
		    	  allowBlank : false,
		    	  editable : false
		      }),
		      renderer:function(value){
		    	  var index = isGoodStore.find('id',value);
		    	  var record = isGoodStore.getAt(index);
		    	  if(record!=null){
		    		  return record.data.name;
		    	  }else{
		    		  return value;
		    	  }
		      }
		      },
		      {header:'补充说明', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'缺陷说明', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'评价结果（有效/无效/不适用）', dataIndex: '',sortable: false,flex:1,
		    	  renderer:function(value,p,record){
		    		 return  "<a href='javascript:void(0);'onclick='editSampleWindow()'>样本测试</a>"+"（"+record.data.yCount+"|"+record.data.nCount+"|"+record.data.nanCount+")";
					 }
		      }
		      ]
	});
	var practiceTestListGrid={
			id:'practiceTestListGrid',
	    	xtype:'fieldset',
	    	margin: '7 10 0 30',
	    	layout:{
	    		type:'column'
	    	},
	    	collapsible : true,
	    	title : '评价内容',
	    	items:[testGrid]
	    };
	basePanel.remove("practiceTestListfieldset",false);  
	basePanel.remove("practiceTestListGrid",false);
	basePanel.remove("sampleTestListGrid",false);
	basePanel.remove("testGrid",false);
	basePanel.doLayout();  
	basePanel.add(practiceTestListfieldset);
	basePanel.add(practiceTestListGrid);
	testGrid.store.load();
	testGrid.on('select',function(){
		var selectionDate=testGrid.getSelectionModel().getSelection();
		if(null!=selectionDate[0].get('assessResultId')){
			assessResultId=selectionDate[0].get('assessResultId');
			
		}
	});
}

function setValueForCalculate(){
	FHD.ajax({
		url:__ctxPath + '/icm/assess/findAssessResultBySome.f',
        params: {
        	assessorId:'${param.assessorId}',
        	processId:processId,
			testDesc:basePanel.getForm().getValues().desc
        },
        callback: function (data) {
        	if(data){
        		var object=Ext.getCmp('calculatefield');
				if(data.result=='partDefc'){
					object.setValue('部分符合');
				}else if(data.result=='allDefc'){
					object.setValue('完全符合');
				}else if(data.result=='notDefc'){
					object.setValue('不符合');
				}
        	}	
        }
	});
}




function addAllPracticeTestList(){
		testGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		columnWidth : 1,
		autoDestroy:true,
		multiSelect:false,
		//pagable:false,
		checked:false,
		url: 'icm/assess/findAssessResultPageBySome.f?testType=ca_assessment_measure_0&assessorId=${param.assessorId}&processId='+processId,
		cols:[{dataIndex:'assessResultId',hidden:true},
		      {dataIndex:'yCount',hidden:true},
		      {dataIndex:'nCount',hidden:true},
		      {dataIndex:'nanCount',hidden:true},
		      {header:'流程节点', dataIndex: 'processPointName', sortable: false,flex:1},
		      {header:'评价点', dataIndex: 'assessPointDesc',sortable: false,flex:1},
		      {header:'实施证据', dataIndex: 'assessSampleName',sortable: false,flex:1},
		      {header:'评价结果(Y|N|N/A)', dataIndex: 'assessSampleName',sortable: false,flex:1,renderer:function(value,p,record){
		    		 return  record.data.yCount+"|"+record.data.nCount+"|"+record.data.nanCount;
				 }},
		      {header:'机算(存在缺陷)', dataIndex: 'hasDefect',sortable: false,flex:1,  renderer:function(value){
		    	  var index = isGoodStore.find('id',value);
		    	  var record = isGoodStore.getAt(index);
		    	  if(record!=null){
		    		  return record.data.name;
		    	  }else{
		    		  return value;
		    	  }
		      }},
		      {header:'调整(存在缺陷)', dataIndex: 'hasDefectAdjust',sortable: false,flex:1,editor:new Ext.form.ComboBox({
		    	  store :isGoodStore,
		    	  valueField : 'id',
		    	  displayField : 'name',
		    	  selectOnTab: true,
		    	  lazyRender: true,
		    	  typeAhead: true,
		    	  allowBlank : false,
		    	  editable : false
		      }),  renderer:function(value){
		    	  var index = isGoodStore.find('id',value);
		    	  var record = isGoodStore.getAt(index);
		    	  if(record!=null){
		    		  return record.data.name;
		    	  }else{
		    		  return value;
		    	  }
		      }},
		      {header:'补充说明', dataIndex: 'adjustDesc',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'缺陷描述', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'操作', dataIndex: '',sortable: false,flex:1,
		    	  renderer:function(value,p,record){
		    		 return  "<a href='javascript:void(0);'onclick='editSampleWindow()'>样本测试</a>";
					 }
		      }
		      ]
		
	});
	testGrid.on('select',function(){
		var selectionDate=testGrid.getSelectionModel().getSelection();
		if(null!=selectionDate[0].get('assessResultId')){
			assessResultId=selectionDate[0].get('assessResultId');
		}
	});
	var practiceTestListGrid={
			id:'practiceTestListGrid',
	    	xtype:'fieldset',
	    	margin: '7 10 0 30',
	    	layout:{
	    		type:'column'
	    	},
	    	collapsible : true,
	    	title : '评价内容',
	    	items:[testGrid]
	    };
	basePanel.remove("practiceTestListfieldset",false);  
	basePanel.remove("practiceTestListGrid",false);
	basePanel.remove("sampleTestListGrid",false);
	basePanel.remove("testGrid",false);
	basePanel.add(practiceTestListfieldset);
	basePanel.add(practiceTestListGrid);
	basePanel.doLayout();
	testGrid.store.load();

}
function addSampleTest(){
		testGrid=Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		//renderTo: 'assessResult${param._dc}', 
		columnWidth:1,
		multiSelect:false,
		//pagable:false,
		checked:false,
		url: 'icm/assess/findAssessResultPageBySome.f?testType=ca_assessment_measure_1&assessorId=${param.assessorId}&processId='+processId,
		//height:FHD.getCenterPanelHeight(),
		cols:[{dataIndex:'assessResultId',hidden:true},
		      {header:'控制措施', dataIndex: 'measureName', sortable: false,flex:1},
		      {header:'评价点', dataIndex: 'assessPointDesc',sortable: false,flex:1},
		      {header:'实施证据', dataIndex: 'assessSampleName',sortable: false,flex:1},
		      {header:'抽取样本数', dataIndex: 'extractedAmount',sortable: false},
		      {header:'有效样本数', dataIndex: 'effectiveNumber',sortable: false},
		      {header:'无效样本数', dataIndex: 'defectNumber',sortable: false},
		      {header:'是否存在缺陷', dataIndex: 'hasDefect',sortable: false,editor:new Ext.form.ComboBox({
		    	  store :isGoodStore,
		    	  valueField : 'id',
		    	  displayField : 'name',
		    	  selectOnTab: true,
		    	  lazyRender: true,
		    	  typeAhead: true,
		    	  allowBlank : false,
		    	  editable : false
		      }),
		      renderer:function(value){
		    	  var index = isGoodStore.find('id',value);
		    	  var record = isGoodStore.getAt(index);
		    	  if(record!=null){
		    		  return record.data.name;
		    	  }else{
		    		  return value;
		    	  }
		      }},
		      {header:'说明', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'评价结果（有效/无效/不适用）', dataIndex: '',sortable: false,flex:1,
		    	  renderer:function(value,p,record){
		    		 return  "<a href='javascript:void(0);'onclick='editSampleWindow()'>样本测试</a>"+"（"+record.data.yCount+"|"+record.data.nCount+"|"+record.data.nanCount+")";
					 }
		      }
		      ]
		});
	var sampleTestListGrid={
			id:'practiceTestListGrid',
	    	xtype:'fieldset',
	    	margin: '7 10 0 30',
	    	layout:{
	    		type:'column'
	    	},
	    	collapsible : true,
	    	title : '评价内容',
	    	items:[testGrid]
	    };
	basePanel.remove("practiceTestListfieldset",false);  
	basePanel.remove("practiceTestListGrid",false);
	basePanel.remove("sampleTestListGrid",false);
	basePanel.remove("testGrid",false);
	basePanel.add(sampleTestListGrid);
	basePanel.doLayout();
	testGrid.store.load();
	testGrid.on('select',function(){
		var selectionDate=assessPlanRelaPointGrid.getSelectionModel().getSelection();
		if(null!=selectionDate[0].get('assessResultId')){
			assessResultId=selectionDate[0].get('assessResultId');	
		}
	});
}
function addAllSampleTest(){
		testGrid=Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		//renderTo: 'assessResult${param._dc}', 
		columnWidth:1,
		multiSelect:false,
		//pagable:false,
		checked:false,
		url: 'icm/assess/findAssessResultPageBySome.f?testType=ca_assessment_measure_1&assessorId=${param.assessorId}&processId='+processId,
		//height:FHD.getCenterPanelHeight(),
		cols:[{dataIndex:'assessResultId',hidden:true},
		      {header:'控制措施', dataIndex: 'measureName', sortable: false,flex:1},
		      {header:'评价点', dataIndex: 'assessPointDesc',sortable: false,flex:1},
		      {header:'实施证据', dataIndex: 'assessSampleName',sortable: false,flex:1},
		      {header:'抽取样本数', dataIndex: 'extractedAmount',sortable: false},
		      {header:'有效样本数', dataIndex: 'effectiveNumber',sortable: false},
		      {header:'无效样本数', dataIndex: 'defectNumber',sortable: false},
		      {header:'是否存在缺陷', dataIndex: 'hasDefect',sortable: false,editor:new Ext.form.ComboBox({
		    	  store :isGoodStore,
		    	  valueField : 'id',
		    	  displayField : 'name',
		    	  selectOnTab: true,
		    	  lazyRender: true,
		    	  typeAhead: true,
		    	  allowBlank : false,
		    	  editable : false
		      }),
		      renderer:function(value){
		    	  var index = isGoodStore.find('id',value);
		    	  var record = isGoodStore.getAt(index);
		    	  if(record!=null){
		    		  return record.data.name;
		    	  }else{
		    		  return value;
		    	  }
		      }},
		      {header:'说明', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'评价结果（有效/无效/不适用）', dataIndex: '',sortable: false,flex:1,
		    	  renderer:function(value,p,record){
		    		 return  "<a href='javascript:void(0);'onclick='editSampleWindow()'>编辑样本</a>"+"（"+record.data.yCount+"|"+record.data.nCount+"|"+record.data.nanCount+")";
					 }
		      }
		      ]
		});
	var sampleTestListGrid={
			id:'practiceTestListGrid',
	    	xtype:'fieldset',
	    	margin: '7 10 0 30',
	    	layout:{
	    		type:'column'
	    	},
	    	collapsible : true,
	    	title : '评价内容',
	    	items:[testGrid]
	    };
	basePanel.remove("practiceTestListfieldset",false);  
	basePanel.remove("practiceTestListGrid",false);
	basePanel.remove("sampleTestListGrid",false); 
	basePanel.remove("testGrid",false); 
	basePanel.add(sampleTestListGrid);
	basePanel.doLayout();
	testGrid.store.load();
	testGrid.on('select',function(){
		var selectionDate=assessPlanRelaPointGrid.getSelectionModel().getSelection();
		if(null!=selectionDate[0].get('assessResultId')){
			processId=selectionDate[0].get('assessResultId');	
		}
	});
}
function returnAssessRsultList(){//返回
	 assessResuListView.remove(assessResuListView.gridPanel);
	 assessResuListView.gridPanel=Ext.create('Ext.container.Container',{
		autoLoad : {
			url : 'pages/icm/assess/assessorBasic.jsp?assessPlanId=${param.assessPlanId}',
			scripts : true
		}
	});
	 assessResuListView.add(assessResuListView.gridPanel);
	}

/***function end***/
/***Ext.onReady start***/
Ext.onReady(function(){
	/***assessPlanRelaPointGrid start***/
	assessPlanRelaPointGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		//renderTo: 'assessResult${param._dc}', 
		columnWidth : 1,
		multiSelect:false,
		checked:false,
		pagable:false,
		url: 'icm/assess/findAssessorRelaProcessFormListByAssessorId.f?assessorId=${param.assessorId}',
		//height:FHD.getCenterPanelHeight(),
		cols:[{dataIndex:'id',hidden:true},
		      {dataIndex:'assessorId',hidden:true},
		      {dataIndex:'numByPracticeTest',hidden:true},
		      {dataIndex:'allNumByPracticeTest',hidden:true},
		      {dataIndex:'numBySampleTest',hidden:true},
		      {dataIndex:'allNumBySampleTest',hidden:true},
		      {header:'流程分类', dataIndex: 'firstProcess', sortable: false,flex:1},
		      {header:'末级流程', dataIndex: 'name', sortable: false,flex:1},
		      {header:'部门', dataIndex: 'orgName',sortable: false,flex:1},
		      {header:'操作', dataIndex: 'do',sortable: false,flex:2,
		    	  renderer:function(value,p,record){
		    		 return  "穿行测试(<a href='javascript:void(0);'onclick='addPracticeTestList()'>"+record.data.numByPracticeTest+"</a>"+"  /  <a href='javascript:void(0);'onclick='addAllPracticeTestList()'>"+record.data.allNumByPracticeTest+"</a>)"+" | "+"抽样测试(<a href='javascript:void(0);'onclick='addSampleTest()'>"+record.data.numBySampleTest+"</a>"+"  /  <a href='javascript:void(0);'onclick='addAllSampleTest()'>"+record.data.allNumBySampleTest+"</a>)";
					 }
		      }
		      ]
	});
	var basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation');
	basePanel=new Ext.FormPanel({
		height : FHD.getCenterPanelHeight(),
		autoScroll:true,
		layout : {
		type : 'column'
		},
		defaults : {
		columnWidth : 1 / 1
		},
		collapsed : false,
		renderTo: 'assessResult${param._dc}',
		items:[basicInfo,
		       {
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : true,
			title : '评价流程',
			items : [assessPlanRelaPointGrid]
		       }],
		       tbar:{
						items: [{
							xtype: 'tbtext'
						}, '->',{
							text: '返回',
							iconCls: 'icon-arrow-redo',
							handler: function(){
								if('${param.flag}'==1){//=1:b表示是在执行页面直接跳转过来的=0：表示是在 流程范围列表页面跳转过来的
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
								}else{
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorBasic.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
								}
							}
						},{
							text: '保存',
							iconCls: 'icon-control-stop-blue',
							handler:  function(){
								if(basePanel.getForm().getValues().combo!=undefined){
									if(basePanel.getForm().getValues().desc==''){
										Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'请填写评价说明!');
										return ;
									}
								}
								var rows = testGrid.store.getModifiedRecords();
								var jsonArray=[];
								var flag=false;
								Ext.each(rows,function(item){
									if(item.data.hasDefectAdjust){
										if(item.data.comment==''){
											flag=true;
											return false;
										}
									}
									jsonArray.push(item.data);
								});
								if(flag){
									Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'请填写缺陷描述!');
									return ;
								}
								url=__ctxPath + '/icm/assess/mergeAssessResultBatch.f',
								FHD.ajax({
									url:url,
                                    params: {
                                    	jsonString:Ext.encode(jsonArray),
                                    	assessorId:'${param.assessorId}',
                                    	combo: basePanel.getForm().getValues().combo,
										testDesc:basePanel.getForm().getValues().desc
                                    },
                                    callback: function (data) {
                                    	if(data){
                            				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
                            				testGrid.store.load();
                                    	}	
                                    }
								});
							}
						},{
							text: '提交',
							iconCls: 'icon-control-stop-blue',
							handler:  function(){
								url=__ctxPath + '/icm/assess/mergeAssessorAndDefect.f',
								FHD.ajax({
									url:url,
                                    params: {
                                    	assessorId:'${param.assessorId}'
                                    },
                                    callback: function (data) {
                                    	if(data){
                            				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
                            				if('${param.flag}'==1){//=1:b表示是在执行页面直接跳转过来的=0：表示是在 流程范围列表页面跳转过来的
            									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
            									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
            									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
            								}else{
            									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
            									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorBasic.jsp?assessPlanId=${param.assessPlanId}');
            									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
            								}
                            			}	
                                    }
								});
							}
						}]
				},
				bbar:{
						items: [{
							xtype: 'tbtext'
						}, '->',{
							text: '返回',
							iconCls: 'icon-arrow-redo',
							handler: function(){
								if('${param.flag}'==1){//=1:b表示是在执行页面直接跳转过来的=0：表示是在 流程范围列表页面跳转过来的
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
								}else {
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorBasic.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
								}
								
							}
						},{
							text: '保存',
							iconCls: 'icon-control-stop-blue',
							handler:  function(){
								var rows = testGrid.store.getModifiedRecords();
								var jsonArray=[];
								Ext.each(rows,function(item){
										jsonArray.push(item.data);
								});
								url=__ctxPath + '/icm/assess/mergeAssessResultBatch.f',
								FHD.ajax({
									url:url,
                                  params: {
                                  	jsonString:Ext.encode(jsonArray),
                                  	assessorId:'${param.assessorId}',
                                  	combo: basePanel.getForm().getValues().combo,
										testDesc:basePanel.getForm().getValues().testDesc
                                  },
                                  callback: function (data) {
                                  	if(data){
                          				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
                          				testGrid.store.load();
                                  	}	
                                  }
								});
							}
						},{
							text: '提交',
							iconCls: 'icon-control-stop-blue',
							handler:  function(){
								url=__ctxPath + '/icm/assess/mergeAssessorAndDefect.f',
								FHD.ajax({
									url:url,
                                  params: {
                                  	assessorId:'${param.assessorId}'
                                  },
                                  callback: function (data) {
                                  	if(data){
                          				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
                          				if('${param.flag}'==1){//=1:b表示是在执行页面直接跳转过来的=0：表示是在 流程范围列表页面跳转过来的
        									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
        									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
        									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
        								}else{
        									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
        									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorBasic.jsp?assessPlanId=${param.assessPlanId}');
        									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
        								}
                          			}	
                                  }
								});
							}
						}]
				}
				
	});
	basePanel.getForm().load({
		url:'assess/assessPlan/findAssessPlanForView.f?assessPlanId=${param.assessPlanId}',
				success: function (form, action) {
					return true;
				},
				failure: function (form, action) {
					return true;
				}
	});
	assessPlanRelaPointGrid.on('select',function(){
		var selectionDate=assessPlanRelaPointGrid.getSelectionModel().getSelection();
		if(null!=selectionDate[0].get('id')){
			processId=selectionDate[0].get('id');	
		}
	});
	
});
/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='assessResult${param._dc}'></div>
	<div id='practiceTest${param._dc}'></div>
	
</body>
</html>