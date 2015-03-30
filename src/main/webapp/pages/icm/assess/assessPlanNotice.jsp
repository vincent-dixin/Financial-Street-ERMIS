<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
/***Ext.onReady start***/
Ext.onReady(function(){
		FHD.ajax({//ajax调用
			url : 'assess/assessPlan/findAssessPlanForView.f',
			params : {
				assessPlanId:'${param.assessPlanId}'
			},
			callback : function(data){
				var viewwindow = new Ext.Window({
					title:'内控评价通知',
			    	layout:'fit',
					modal:true,//是否模态窗口
					collapsible:true,
					width:750,
					height:600,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[Ext.create('Ext.panel.Panel',{
						autoScroll:true,
	            		width: 300,
	                    bodyStyle: "padding:5px;font-size:12px;"
	            	})],
					listeners: {
		                afterlayout: function() {
		                	var panel = this.down('panel');
	                        tpl = Ext.create('Ext.Template',
	                        		'<p><div style="text-align: center;"><h1>'+data.data.companyName+'</h1></div></p>',
	                        		'<p><div style="text-align: center;color: red"><h2>'+data.data.code+'</h2></div></p>',
	                        		'<p><div style="text-align: center;"><h3>'+data.data.name+'的通知'+'</h3></div></p>',
	                        		'<p><div style="text-align: left;">'+'公司各单位:'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'为了提高公司内控管理的整体水平，监督评价公司内部控制系统设计的合理性和运行的有效性，进一步防范企业风险，促进公司转变发展方式，实现全年的各项目标任务，根据财政部等五部委发布的《企业内部控制基本规范》和配套指引的工作要求，公司决定从'+data.data.planStartDate+
	                        		'到'+data.data.planEndDate+'，在公司范围内展开'+data.data.name+'。先将有关事项通知如下：'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'一、基本信息'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（一）目标:'+data.data.assessTarget+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（二）范围:'+data.data.scopeReq+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（三）基本要求:'+data.data.assessStandard+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（四）时间要求:'+data.data.requirement+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（五）考核要求:'+data.data.desc+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'二、工作组'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'组长：'+data.data.responsibilityEmp+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'成员：'+data.data.handlerEmp+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'主要职责：工作组负责组织实施内控评价测试工作，对发现的内部控制缺陷及审计问题，分析其产生的原因，并制定整改措施方案，督促整改落实，编制完成评价报告。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'三、具体范围'+'</div></p>',
	                        		'<p><div id=\'assessMessageGrid${param._dc}\'></div></p>',
	                        		'<p><div style="text-align: left;">'+'四、评价原则'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'按照《企业内部控制评价指引》要求，公司在实施内控自评价测试工作过程中，应遵循全面性、重要性、客观性、成本效益等原则。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（一）	全面性原则。评价工作应当包括内部控制的设计与执行，涵盖企业及所属单位的各项业务和事项。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（二）	重要性原则。评价工作应关注重要业务单位、重大业务事项、重要流程和高风险领域。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（三）	客观性原则。评价工作应准确解释经营管理的风险状况，如实反映内部控制设计和运行的有效性。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（四）	成本效益原则。评价工作应以降低评价成本，提升评价效果为目的，寻求管理成本与效益的最佳平衡点。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'五、有关要求'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'(一) 提高对内控工作重要性的认识。公司各只能部室、各单位的主要负责人要提高对内控测试工作重要性的认识，积极配合工作组做好评价工作。要明确专人，明确任务，责任到人，对工作任务和时限提出要求，关注工作进展，主动进行协调，杜绝推诿、拖拉、扯皮现象发生。在工作组开展调研、访谈、穿行测试及评价工作中，配合提供工作组所需的相关资料，保证资料的真实性，有效性。组织人员认真查找本单位职责范围内的内部控制重点及薄弱环节，及时制定有效的改进措施，并积极组织落实。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（二）加大内控宣传、动员及培训的工作力度。公司各职能部室、各单位要加强对内部控制的学习宣传，增强全员的内控意识，营造良好的内控文化氛围。继续做好内部控制专业知识、测试方法、总结评价等方面的强化提升培训，不断创新工作思路，提升内控自评价测试的工作水平，提高自评价工作的质量和效果。通过网络、电视台、内部报纸、刊物，宣传本单位开展内部控制工作的好经验、好做法和取得的成效，促进内部控制工作的持续改进。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（三）各级管理人员要以此次内控自评价测试为契机，提高管理水平和内控能力。在履行岗位职责和开展业务活动中，明晰内部控制的工作要求和重要意义，认真落实测试要求，将工作抓实、抓好、抓出成效。要加强学习，进一步提高自身的内控业务水平和能力，对工作组提出的内控缺陷及问题认真对待，积极整改，举一反三，加强制度建设和流程优化，不断提升公司内部控制的管理水平，实现 提高管理水平、提高自我纠偏能力、提高工作质量和效率、提高风险防范能力的目标。'+'</div></p>',
	                        		'<p><div style="text-align: left;">'+'（四）工作组要本着科学规范、严谨细致的工作原则，桌游成效的开展内部控制自评价测试工作，与${计划结束日期}前完成评价报告的编制、审核、上报工作。'+'</div></p>',
	                        		'<p><div style="text-align: right;">'+'创建人'+data.data.creatBy+'</div></p>',
	                        		'<p><div style="text-align: right;">'+'创建时间'+data.data.creatTime+'</div></p>'
	                        );
		                    tpl.overwrite(panel.body, data);
		                    var assessPlanRelaProcessGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		                	  	renderTo:'assessMessageGrid${param._dc}',
		                	 	columnWidth : 1,
		                		checked:false,
		                		url: 'icm/assess/findAssessPlanRelaProcessListByPage.f?assessPlanId=${param.assessPlanId}',
		                		cols:[{dataIndex:'id',hidden:true},
		                		      {header:'一级流程', dataIndex: 'firstProcessName', sortable: false,flex:1},
		                		      {header:'二级流程', dataIndex: 'parentProcessName',sortable: false,flex:1},
		                		      {header:'末级流程', dataIndex: 'processName', sortable: false,flex:1},
		                		      {header: '穿行测试',
		                		    	  dataIndex: 'isPracticeTest',
		                		    	  renderer: function(value,p,record){
		                		    		  if(record.data.isPracticeTest)
		                		    			  return '是';
		                		    		  else
		                		    			  return '否';
		                		    	  }
		                		      },
		                		      {header:'穿行次数', dataIndex: 'practiceNum', sortable: false},
		                		      {header: '抽样测试',
		                		    	  dataIndex: 'isSampleTest',
		                		    	  renderer: function(value,p,record){
		                		    		  if(record.data.isSampleTest)
		                		    			  return '是';
		                		    		  else
		                		    			  return '否';
		                		    	  }
		                		      },
		                		      {header:'抽样次数', dataIndex: 'sampleNum', sortable: false},
		                		      {header:'抽样覆盖率', dataIndex: 'coverageRate', sortable: false}]
		                	});
		                }
		            }
			    }).show();
			}
		});
});
/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='demo' style=""></div>
</body>
</html>