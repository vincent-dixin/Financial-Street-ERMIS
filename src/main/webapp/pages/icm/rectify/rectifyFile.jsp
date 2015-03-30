<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.rectify.rectifyFile', {
		extend : 'Ext.form.Panel',
		renderTo:'FHD.rectify.rectifyFileForm${param._dc}',
		items:[],
		autoScroll:true,
		layout : {
			type : 'column'
		},
		defaults : {
			columnWidth : 1/1
		},
		autoWidth:true,
		collapsed : false,
		height : FHD.getCenterPanelHeight(),
		bbar: {},
		
		id:{},
		code:{},//编号
		compSelect:{},//所属公司
		name:{},//改进名称
		org:{},//所属部门
		empERId:{},//实施负责人
		empERPId:{},//复核人
		planStartDate:{},//计划开始时间
		planEndDate:{},//计划完成时间
		improvementSource:{},//改进原因
		improvementTarget:{},//改进目标
		defectId:{},//涉及缺陷
		reasonDetail:{},//具体原因
		
		
		advice:{},//通知书
		improveScenario:{},//整改方案
		file:{},//附件
		improveExamineAndApprove:{},//审批
		initComponent :function() {
		  var me = this;
		  
		  
		  me.id = {
				  xtype : 'textfield',
				  hidden: true,
				  name : 'id'
		  };
		  me.code = {
				  xtype:"displayfield",
                  fieldLabel:"编号",
                  allowBlank:false,
                  margin : '7 5 5 30',
                  name:"code",
                  value:''
                  }; 
		  me.compSelect={
				  xtype:"displayfield",
                  fieldLabel:"所属公司",
                  allowBlank:false,
                  margin : '7 5 5 30',
                  name : 'company',
                  value:''
                  };
		  me.name = {
				    xtype:"displayfield",
					fieldLabel : '改进名称',
					allowBlank : false,
					//width : 400,
					margin : '7 5 5 30',
					name : 'name'
		  };
		  me.org = {
				  xtype:"displayfield",
                  fieldLabel:"负责部门",
                  allowBlank:false,
                  margin : '7 5 5 30',
        		  name:'orgId',
                  value:''
                  };
		me.empERId = {
				  xtype:"displayfield",
				  fieldLabel : '实施负责人',
                  allowBlank:false,
                  margin : '7 5 5 30',
                  name:'empERId',
                  value:''
                  };
		me.empERPId = {
				  xtype:"displayfield",
				  fieldLabel : ' 复核人',
                  allowBlank:false,
                  margin : '7 5 5 30',
                  name:'empERPId',
                  value:''
                  };
		
		me.planStartDate =  {
				xtype:"displayfield",
		        fieldLabel:'计划开始时间',
		        name: 'planStartDate',
		        margin : '7 5 5 30',
		        //columnWidth : .3,
		        format: 'Y-m-d',
		        labelWidth:100,
		};
		
		me.planEndDate = {
				xtype:"displayfield",
		        fieldLabel:'计划开始时间',
		        name: 'planEndDate',
		        margin : '7 5 5 30',
		        //columnWidth : .3,
		        format: 'Y-m-d',
		        labelWidth:100,
		};
		me.improvementSource = Ext.create('FHD.ux.dict.DictCheckbox',
				{
			margin : '7 5 5 30',
			xtype:"displayfield",
			name:'improvementSource',dictTypeId:'ref_improvement_source',labelAlign:'left',columns:5,fieldLabel : '改进原因'
		});

		me.improvementTarget = {
				xtype:"displayfield",
				margin : '7 5 5 30',
				fieldLabel : '改进目标',
			    //width : 400,
				rows : 3,
				name : 'improvementTarget'
		};
		
		me.defectId = {
				xtype:"displayfield",
				margin : '7 5 5 30',
				fieldLabel : '涉及缺陷列表',
				name : 'defectId'
		};
		
		me.reasonDetail = {
				xtype:"displayfield",
				fieldLabel : '具体原因',
				margin : '7 5 5 30',
				//width : 400,
				rows : 3,
				name : 'reasonDetail'
		};
		  
		  me.advice = Ext.create('Ext.form.Panel',{
              id:'advice',
              width:500,
              //split: true,
             height:200,
              //collapsible: true,           
              //anchor:'200%',
            //  border:true,
              //autoScroll:false,
               autoLoad:{
            	   url:'pages/icm/rectify/rectifyAdvice.jsp',
            	   scripts:true
               },
            	   
             // html:'<iframe  id="advice" src="/pages/icm/rectify/rectifyAdvice.jsp"></iframe>',
            //  margins:'0 0 0 0',
             // cmargins:'0 0 0 0'
           });
		  me.improveScenario = {
				  xtype : 'textareafield',
					fieldLabel : '改进方案',
					//allowBlank : false,
					//width : 400,
					rows:9,
					//height:100,
					margin : '7 5 5 30',
					name : 'improveScenario'
                  };
		  
		  me.file={
				  xtype : 'FileUpload',
					margin : '7 5 5 30',
					labelAlign : 'left',
					//columnWidth : 1 / 1,
					fieldLabel : '附件',
					labelWidth : 80,
					name : 'file',
					showModel : ' '
	        
			};
		  me.improveExamineAndApprove = {
				  xtype : 'textareafield',
					fieldLabel : '意见',
					//allowBlank : false,
					//width : 400,
					rows:9,
					//height:100,
					margin : '7 5 5 30',
					name : 'improveExamineAndApprove'
                };
			
			me.items= [{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/2,
					margin:'7 10 10 30'
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
				//margin: '8 10 0 10',
				collapsible : false,
				title : '整改计划预览',
				items:[ 
				         me.id,
		                 me.code,
		                 me.compSelect,
		                 me.name,
		                 me.org,
		                 me.empERId,
		                 me.empERPId,
		                 me.planStartDate,
		                 me.planEndDate,
		                 me.improvementSource,
		                 me.improvementTarget,
		                 me.defectId,
		                 me.reasonDetail
				      ]
	            },{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/2,
					margin:'7 10 10 30'
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
				//margin: '8 10 0 10',
				collapsible : false,
				title : '上报数据',
				items:[ me.improveScenario,
				        me.file
				      ]
	            },{
					xtype : 'fieldset',
					defaults : {
						columnWidth : 1/2,
						margin:'7 10 10 30'
					},//每行显示一列，可设置多列
					layout : {
						type : 'column'
					},
					collapsed : false,
					//margin: '8 10 0 10',
					collapsible : false,
					title : '审批',
					items:[ me.improveExamineAndApprove
					      ]
		            }];
			me.bbar={
		               style: 'background-image:url() !important;background-color:rgb(250,250,250);',
		               items: [{
		                   xtype: 'tbtext',
		               }, '->',{
		                   text:'保存',
		                   width:100,
		                   handler: function () {
		                	   FHD.submit({
		                		   form: me.getForm(),
				                   url: __ctxPath+'/icm/improve/saveImproveFile.f?improveId=${param.improveId}',
				                   callback: function (data){
				                	  /* 	defect_ManagerView.remove(defect_ManagerView.gridPanel);
					                	defect_ManagerView.gridPanel = Ext.create('defect_defectManage_panelAndMenu');
					               		defect_ManagerView.add(defect_ManagerView.gridPanel);
					                   */
				                   }
		                		  });
		                   }
		                   },{
		                   text:'提交',
		                   width:100,
		                   handler: function () {}
		                   }, {
		                   text:'返  回',
		                   width:100,
		                   handler: function () {
		                	   rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
			                	rectify_ManagerView.gridPanel = Ext.create('rectify_rectifyManage_panelAndMenu');
			               		rectify_ManagerView.add(rectify_ManagerView.gridPanel);
		                   }
		               }]
		           };
			Ext.applyIf(me,{
				items:me.items
			});
			me.callParent(arguments);
			}
		});

	var FHD_icm_rectify_rectifyFile;
	Ext.onReady(function(){
		FHD_icm_rectify_rectifyFile=Ext.create('FHD.icm.rectify.rectifyFile');
		FHD_icm_rectify_rectifyFile.getForm().load({
			url:'icm/improve/findImproveAdviceview.f?improveId=${param.improveId}',
				success: function (form, action) {
                    
             	   return true;
                },
                failure: function (form, action) {
             	  
             	   return true;
                }
			
		});
	});
	
	
</script>
</head>
<body>
	<div id='FHD.rectify.rectifyFileForm${param._dc}'></div>
</body>
</html>