<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">
var tree,panel;
Ext.define('demopanel',{
	extend:'Ext.panel.Panel',
	border:false,
	autoScroll:false,
	height:FHD.getCenterPanelHeight()-3,
	region: 'center'
});
function initDemoPanel(url){
	var panelDemopanel;
	if(url!='暂无'){
		panelDemopanel=Ext.create('demopanel',{autoLoad :{ url: url,scripts: true}});
	}else{
		panelDemopanel=Ext.create('demopanel',{title:'暂无'});
	}
	panel.demopanel=panelDemopanel;
	panel.add(panelDemopanel);
}
Ext.onReady(function () {
	tree = Ext.create('Ext.tree.Panel', {
	    region: 'west',
	    split: true,
        collapsible : true,
        border:true,
        maxWidth:300,
        height:FHD.getCenterPanelHeight()-3,
	    width:220,
	    root: {
	        text: '开发帮助',
	        iconCls:'icon-help',
	        expanded: true,
	        autoLoad:true,
	        children:[
	        	{
	        		text:'测试',
	        		iconCls:'icon-folder',
	        		expanded: true,
	        		children: [
						{
						    text: '流程图设计器',
						    iconCls:'icon-information',
						    leaf: true
						},
						{
						      text: '查看图',
						      iconCls:'icon-information',
						      leaf: true
						},
	  		            {
	  		                text: '普通列表',
	  		              	iconCls:'icon-information',
	  		                leaf: true
	  		            },
	  		            {
	  		                text: '可编辑列表',
	  		              	iconCls:'icon-information',
	  		                leaf: true
	  		            },
	  		            {
	  		                text: '树',
	  		              	iconCls:'icon-information',
	  		                leaf: true
	  		            },
	  		            {
	  		                text: '树列表',
	  		              	iconCls:'icon-information',
	  		                leaf: true
	  		            },
	  		            {
	  		                text: '测试流程',
	  		              	iconCls:'icon-information',
	  		                leaf: true
	  		            }
	  		        ]
	        	},
	        	{
	        		text:'公共控件',
	        		iconCls:'icon-folder',
	        		expanded: true,
	        		children:[
						{
					        text: '上传文件',
					    	iconCls:'icon-information',
					        leaf: true
						},
	        			{
	        				text:'部门人员',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'字典选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'带radio的公式选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'公式选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'公式弹窗选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			
	        			{
	        				text:'流程树',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'流程选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'内控标准树',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'内控标准选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'内控要求选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'制度树',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'制度选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'树选择布局',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'树列表选择布局',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'列表选择布局',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'时间选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'导航组件',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'风险树',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'风险选择',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'采集频率',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			},
	        			{
	        				text:'步骤导航',
	  		              	iconCls:'icon-information',
	        				leaf: true
	        			}
	        		]
	        	},
	        	{
	        		text:'业务控件',
	        		iconCls:'icon-folder',
	        		expanded: true,
	        		children:[
						{
			        		text:'内控',
			        		iconCls:'icon-folder',
			        		expanded: true,
			        		children:[
								{
							        text: '内控评价计划选择',
							    	iconCls:'icon-information',
							        leaf: true
								},
			        			{
			        				text:'内控评价计划弹窗选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			},
			        			{
			        				text:'内控整改计划选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			},
			        			{
			        				text:'内控整改计划弹窗选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			},
			        			{
			        				text:'内控缺陷选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			},
			        			{
			        				text:'内控缺陷弹窗选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			},
			        			{
			        				text:'流程选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			},
			        			{
			        				text:'制度选择',
			  		              	iconCls:'icon-information',
			        				leaf: true
			        			}
			        		]
			        	},{
			        		text:'指标',
			        		iconCls:'icon-folder',
			        		expanded: true,
			        		children:[
									{
										text:'指标树',
									    	iconCls:'icon-information',
										leaf: true
									},
									{
										text:'指标选择',
									    	iconCls:'icon-information',
										leaf: true
									},
									{
										text:'目标树',
									    	iconCls:'icon-information',
										leaf: true
									},
									{
										text:'目标选择',
									    	iconCls:'icon-information',
										leaf: true
									},
									{
				        				text:'指标类型选择',
				  		              	iconCls:'icon-information',
				        				leaf: true
				        			}
			        		]
			        	}
	        		]
	        	}
	        ]
	        
	    },
	    listeners : {
	    	itemclick:function(view,node){
	    		panel.remove(panel.demopanel);
	    		switch (node.data.text){
		    		case '流程图设计器':
		    			var panelDemopanel;
		    			panelDemopanel=Ext.create('demopanel',{
	    					html : '<iframe width=\'100%\' height=\'100%\' frameborder=\'0\' src=\''+__ctxPath+'/process/flowchart.do'+'\'></iframe>'
	    				});
		    			panel.demopanel=panelDemopanel;
		    			panel.add(panelDemopanel);
		    			break;
	    			case '查看图':
		    			initDemoPanel('pages/icm/assess/assessPlanNotice.jsp?assessPlanId=65456153-00e6-411a-8075-5c8339daea8a');
		    			break;
		    		case '普通列表':
		    			initDemoPanel('pages/demo/grid/gridlist.jsp');
	    				break;
		    		case '可编辑列表':
		    			initDemoPanel('pages/demo/editgrid/editgridlist.jsp');
	    				break;
		    		case '树':
		    			initDemoPanel('pages/demo/tree/tree.jsp');
	    				break;
		    		case '树列表':
		    			initDemoPanel('pages/demo/treegrid/treegridlist.jsp');
	    				break;
		    		case '测试流程':
		    			initDemoPanel('pages/demo/processInstance/processInstanceList.jsp');
	    				break;
		    		case '部门人员':
		    			initDemoPanel('pages/demo/deptAndEmpSelect/deptAndEmpSelect.jsp');
		    			break;
		    		case '上传文件':
		    			initDemoPanel('pages/demo/FileUpload/FileUpload.jsp');
		    			break;
		    		case '部门选择':
		    			initDemoPanel('pages/demo/departmentSelect/departmentSelect.jsp');
		    			break;
		    		case '公司选择':
		    			initDemoPanel('pages/demo/companySelect/companySelect.jsp');
			    		break;
		    		case '字典选择':
		    			initDemoPanel('pages/demo/dict/dictselect.jsp');
			    		break;
		    		case '带radio的公式选择':
		    			initDemoPanel('pages/demo/formula/formulaselector.jsp');
			    		break;
		    		case '公式选择':
		    			initDemoPanel('pages/demo/formula/formulatrigger.jsp');
			    		break;
		    		case '公式弹窗选择':
		    			initDemoPanel('pages/demo/formula/formulaselectorforwindow.jsp');
			    		break;
		    		case '指标树':
		    			initDemoPanel('pages/demo/kpitree/kpitree.jsp');
			    		break; 
		    		case '指标选择':
		    			initDemoPanel('pages/demo/kpitree/kpiselect.jsp');
			    		break;
		    		case '指标类型选择':
		    			initDemoPanel('pages/demo/kpitree/kpitypeselect.jsp');
			    		break;
		    		case '目标树':
		    			initDemoPanel('pages/demo/strategymaptree/strategymaptree.jsp');
			    		break;
		    		case '目标选择':
		    			initDemoPanel('pages/demo/strategymaptree/strategymapselect.jsp');
			    		break;
		    		case '流程树':
		    			initDemoPanel('pages/demo/process/processtree.jsp');
			    		break;
		    		case '流程选择':
		    			initDemoPanel('pages/demo/process/processselect.jsp');
			    		break;
		    		case '内控标准树':
		    			initDemoPanel('pages/demo/standard/standardtree.jsp');
			    		break;
		    		case '内控标准选择':
		    			initDemoPanel('pages/demo/standard/standardselect.jsp');
			    		break;
		    		case '内控要求选择':
		    			initDemoPanel('pages/demo/standard/requireselect.jsp');
			    		break;
		    		case '制度树':
		    			initDemoPanel('pages/demo/rule/ruletree.jsp');
			    		break;
		    		case '制度选择':
		    			initDemoPanel('pages/demo/rule/ruleselect.jsp');
			    		break;
		    		case '树选择布局':
		    			initDemoPanel('pages/demo/treeselector/treeselector.jsp');
	    				break;
		    		case '树列表选择布局':
		    			initDemoPanel('pages/demo/treelistselector/treelistselector.jsp');
	    				break;
		    		case '列表选择布局':
		    			initDemoPanel('pages/demo/listselector/listselector.jsp');
	    				break;
		    		case '时间选择':
		    			initDemoPanel('pages/demo/time/time.jsp');
	    				break;
		    		case '导航组件':
		    			initDemoPanel('pages/demo/navigation/navigation.jsp');
	    				break;
		    		case '风险树':
		    			initDemoPanel('pages/demo/risk/riskTree.jsp');
	    				break;
		    		case '风险选择':
		    			initDemoPanel('pages/demo/risk/riskSelect.jsp');
	    				break;
		    		case '采集频率':
		    			initDemoPanel('pages/demo/collect/collect.jsp');
	    				break;
		    		case '步骤导航':
		    			initDemoPanel('pages/demo/stepnavigator/stepnavigator.jsp');
	    				break;
		    		case '内控评价计划选择':
		    			initDemoPanel('pages/demo/assessplanselector/assessplanselector.jsp');
	    				break;
		    		case '内控评价计划弹窗选择':
		    			initDemoPanel('pages/demo/assessplanselector/assessplanselectorforwindow.jsp');
	    				break;
		    		case '内控整改计划选择':
		    			initDemoPanel('pages/demo/rectifyplanselector/rectifyplanselector.jsp');
	    				break;
		    		case '内控整改计划弹窗选择':
		    			initDemoPanel('pages/demo/rectifyplanselector/rectifyplanselectorforwindow.jsp');
	    				break;
		    		case '内控缺陷选择':
		    			initDemoPanel('pages/demo/defectselector/defectselector.jsp');
	    				break;
		    		case '内控缺陷弹窗选择':
		    			initDemoPanel('pages/demo/defectselector/defectselectorforwindow.jsp');
	    				break;
		    		case '流程选择': 
		    			initDemoPanel('pages/demo/process/processselect.jsp');
	    				break;
		    		case '制度选择':
		    			initDemoPanel('pages/demo/defectselector/defectselectorforwindow.jsp');
	    				break;
	    			default:
	    				initDemoPanel('pages/risk/dimension/dimension.jsp');
	    		}
	    	}
	    }
	});
	var panelDemopanel=Ext.create('demopanel',{title:'暂无'});
	panel = Ext.create('Ext.container.Container',{
	    renderTo: 'demotree', 
	    autoScroll:true,
	    layout: {
	        type: 'border'
	    },
	    defaults: {
            border:true
        },
	    demopanel:panelDemopanel,
        height:FHD.getCenterPanelHeight()-3,
	    items:[tree,panelDemopanel]
	});
	FHD.componentResize(panel,0,0); 
});

</script>


</head>
<body>
	<div id='demotree'></div>
</body>

</html>