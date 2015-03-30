/*
 * 流程的测试的汇总列表
 * 通过参数选择是显示全部的还是某一个参评人的流程测试
 * */
 Ext.define('FHD.view.icm.assess.component.SampleTestDraftGrid',{
	extend:'FHD.ux.GridPanel',
	alias:'widget.sampletestdraftgrid',
	
	url: __ctxPath + '/icm/assess/findpracticeAnalysis.f',
	extraParams:{
		assessPlanId:'',
		executionId:''
	},
	checked:false,
	
	initComponent:function(){
    	var me=this;
    
    	me.extraParams.assessPlanId=me.businessId;
    	me.extraParams.executionId=me.executionId;
    	
    	me.cols=[
    	    {header:'末级流程ID',dataIndex:'id',hidden:true},
      		//{header:'参评人ID',dataIndex:'assessorId',hidden:true},
      		{header:'流程分类', dataIndex: 'parentProcess', sortable: false,flex:1,
      			renderer:function(value,metaData,record,colIndex,store,view) {
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'"';
	    				return value;
	    			}
					return value;
				}
    	    },
      		{header:'末级流程', dataIndex: 'name', sortable: false,flex:3,
      			renderer:function(value,metaData,record,colIndex,store,view) {
					if(value){
						metaData.tdAttr = 'data-qtip="'+value+'"';
	    				return value;
	    			}
					return value;
				}
    	    },
      		{header:'控制措施数', dataIndex: 'assessMeasureNO',sortable: false,flex:1},
      		{header:'评价人', dataIndex: 'executeEmpName',sortable: false,flex:2},
      		{header:'评价日期', dataIndex: 'assessDate',sortable: false,flex:2,hidden:true},
      		{header:'评价点数', dataIndex: 'allNumBySampleTest',sortable: false,flex:1},
      		{header:'评价点通过率', dataIndex: 'qualifiedRateBySampleTest',sortable: false,flex:1,
    	  		renderer:function(value){
    		  		var qualifiedRateByPracticeTest=value*100+'%';
    		  		return qualifiedRateByPracticeTest;
    	  		}
      		},
      		{header:'样本数', dataIndex: 'sampleTestSampleNum',sortable: false,flex:1,
      			renderer:function(value,metaData,record,colIndex,store,view) { 
					return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showSamepleViewList('" + record.data.assessPlanId + "','"+ record.data.id +"')\" >" + value + "</a>"; 
				}
      		},
      		{header:'样本合格率', dataIndex: 'qualifiedSampleTestSample',sortable: false,flex:1,
    	  		renderer:function(value){
    		  		var qualifiedPracticeTestSample=value*100+'%';
    		  		return qualifiedPracticeTestSample;
    	  		}  
      		},
      		{header:'缺陷数', dataIndex: 'sampleHasDefectNum',sortable: false,flex:1},
	      	{header:'计划id', dataIndex: 'assessPlanId',hidden:true}
	    ];
	    
    	me.callParent(arguments);
    	
    	if(me.showAssessDate){
    		me.down('[dataIndex=assessDate]').show();
    	}
    },
    showSamepleViewList:function(assessPlanId, processId){
    	var me=this;
    	
    	me.samplePanel=Ext.create('FHD.view.icm.assess.component.SampleViewGrid',{
    		assessPlanId:assessPlanId,
    		type:'sampling',
    		processId:processId
		});
		
		var win = Ext.create('FHD.ux.Window',{
			title:'流程样本信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.samplePanel]
    	}).show();
    },
    reloadData:function(){
    	var me=this;
    	me.store.load();
    }
});