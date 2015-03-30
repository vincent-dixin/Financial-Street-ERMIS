/*
 * 流程的测试的列表
 * */
 Ext.define('FHD.view.icm.assess.component.ProcessTestGrid',{
	extend:'FHD.ux.GridPanel',
	alias:'widget.processtestgrid',
	
	url: __ctxPath + '/icm/assess/findAssessorRelaProcessFormListByAssessorId.f',
	extraParams:{
		assessPlanId:'',
		executionId:''
	},
	tbarItems:[],
	checked:false,
	
	initComponent:function(){
		var me=this;
		
		me.extraParams.assessPlanId=me.businessId;
		me.extraParams.executionId=me.executionId;
		
		Ext.apply(me,{
    		cols: [
    			{header:'操作(未测试数/总数)', dataIndex: 'do',sortable: false,width:270,
			    	renderer:function(value,metaData,record,colIndex,store,view) { 
			    		metaData.tdAttr = 'data-qtip="请点击此处"';
			    		var retStr = '';
			    		var assessPlanMeasureId = record.data.assessPlanMeasureId;
			    		if('ca_assessment_measure_2' == assessPlanMeasureId){
			    			//全部
			    			if(record.data.isPracticeTest){
			    				retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\">穿行测试</a>&nbsp;(";
					    		if(0 != record.data.numByPracticeTest){
					    			retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('not','" + record.data.processId + "','"+record.data.assessorId+"')\">&nbsp;&nbsp;" + record.data.numByPracticeTest + "&nbsp;&nbsp;</a>";
					    		}else{
					    			retStr += "&nbsp;&nbsp;"+record.data.numByPracticeTest+"&nbsp;&nbsp;";
					    		}
					    		retStr += "/";
					    		if(0 != record.data.allNumByPracticeTest){
					    			retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\">&nbsp;&nbsp;" + record.data.allNumByPracticeTest + "&nbsp;&nbsp;</a>";
					    		}else{
					    			retStr += "&nbsp;&nbsp;"+record.data.allNumByPracticeTest+"&nbsp;&nbsp;";
					    		}
					    		retStr += ")";
			    			}
			    			if(record.data.isPracticeTest && record.data.isSampleTest){
			    				retStr += "&nbsp;|&nbsp;";
			    			}
			    			if(record.data.isSampleTest){
			    				retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\" >抽样测试</a>&nbsp;(";
								if(0 != record.data.numBySampleTest){
									retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('not','" + record.data.processId + "','"+record.data.assessorId+"')\">&nbsp;&nbsp;" + record.data.numBySampleTest + "&nbsp;&nbsp;</a>";
								}else{
									retStr += "&nbsp;&nbsp;"+record.data.numBySampleTest+"&nbsp;&nbsp;";
								}
								retStr += "/";
								if(0 != record.data.allNumBySampleTest){
									retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\" >&nbsp;&nbsp;" + record.data.allNumBySampleTest + "&nbsp;&nbsp;</a>";
								}else{
									retStr += "&nbsp;&nbsp;"+record.data.allNumBySampleTest+"&nbsp;&nbsp;";
								}
								retStr += ")";
			    			}
			    		}else if('ca_assessment_measure_0' == assessPlanMeasureId){
			    			//穿行
			    			if(record.data.isPracticeTest){
			    				retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\">穿行测试</a>&nbsp;(";
					    		if(0 != record.data.numByPracticeTest){
					    			retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('not','" + record.data.processId + "','"+record.data.assessorId+"')\">&nbsp;&nbsp;" + record.data.numByPracticeTest + "&nbsp;&nbsp;</a>";
					    		}else{
					    			retStr += "&nbsp;&nbsp;"+record.data.numByPracticeTest+"&nbsp;&nbsp;";
					    		}
					    		retStr += "/";
					    		if(0 != record.data.allNumByPracticeTest){
					    			retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\">&nbsp;&nbsp;" + record.data.allNumByPracticeTest + "&nbsp;&nbsp;</a>";
					    		}else{
					    			retStr += "&nbsp;&nbsp;"+record.data.allNumByPracticeTest+"&nbsp;&nbsp;";
					    		}
					    		retStr += ")";
			    			}
			    		}else if('ca_assessment_measure_1' == assessPlanMeasureId){
			    			if(record.data.isSampleTest){
			    				retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\" >抽样测试</a>&nbsp;(";
								if(0 != record.data.numBySampleTest){
									retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('not','" + record.data.processId + "','"+record.data.assessorId+"')\">&nbsp;&nbsp;" + record.data.numBySampleTest + "&nbsp;&nbsp;</a>";
								}else{
									retStr += "&nbsp;&nbsp;"+record.data.numBySampleTest+"&nbsp;&nbsp;";
								}
								retStr += "/";
								if(0 != record.data.allNumBySampleTest){
									retStr += "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\" >&nbsp;&nbsp;" + record.data.allNumBySampleTest + "&nbsp;&nbsp;</a>";
								}else{
									retStr += "&nbsp;&nbsp;"+record.data.allNumBySampleTest+"&nbsp;&nbsp;";
								}
								retStr += ")";
			    			}
			    		}
			    		
						return retStr;
						/*
			    		return "穿行测试("+"<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('not','" + record.data.processId + "','"+record.data.assessorId+"')\" >" + record.data.numByPracticeTest + "</a>"
			    			+"/"+"<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addPracticeTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\" >" + record.data.allNumByPracticeTest + "</a>)"
			    			+"|"+"抽样测试("+"<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('not','" + record.data.processId + "','"+record.data.assessorId+"')\" >" + record.data.numBySampleTest + "</a>"
			    			+"/"+"<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.parentId+"').addSampleTestList('all','" + record.data.processId + "','"+record.data.assessorId+"')\" >" + record.data.allNumBySampleTest + "</a>)"
			    			*/
			    	}
			    },
			    {header:'流程分类', dataIndex: 'firstProcess', sortable: false,flex:2,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
			    {header:'末级流程', dataIndex: 'name', sortable: false,flex:2,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
			    /*
			    {header:'风险水平', dataIndex: 'riskLevel', sortable: false,flex:1,
			    	renderer: function (v) {
				        var color = "";
				        var display = "";
				        if (v == "0alarm_startus_h") {
				            color = "icon-ibm-symbol-4-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.low");
				        } else if (v == "0alarm_startus_l") {
				            color = "icon-ibm-symbol-6-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.hight");
				        } else if (v == "0alarm_startus_m") {
				            color = "icon-ibm-symbol-5-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.min");
				        } else {
				            v = "icon-ibm-underconstruction-small";
				            display = FHD.locale.get('fhd.common.none');
				        }
				        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
				            "background-position: center top;' data-qtitle='' " +
				            "class='" + color + "'  data-qtip='" + display + "'>&nbsp</div>";
				    }
			    },
			    */
			    {header:'发生频率', dataIndex: 'frequency', sortable: false,flex:1,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
			    {header:'责任部门', dataIndex: 'orgName',sortable: false,flex:1,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
			    {header:'评价人', dataIndex: 'executeEmpId',sortable: false,flex:2,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
			    {header:'复核人', dataIndex: 'reviewerEmpId',sortable: false,flex:2,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
				{dataIndex:'processId',hidden:true},
			    {dataIndex:'assessorId',hidden:true},
			    {dataIndex:'numByPracticeTest',hidden:true},
			    {dataIndex:'allNumByPracticeTest',hidden:true},
			    {dataIndex:'numBySampleTest',hidden:true},
			    {dataIndex:'allNumBySampleTest',hidden:true},
			    {dataIndex:'assessPlanMeasureId',hidden:true},
			    {dataIndex:'isPracticeTest',hidden:true},
			    {dataIndex:'isSampleTest',hidden:true}
		    ]
    	});
        
    	me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		
		me.store.load();
    }
});