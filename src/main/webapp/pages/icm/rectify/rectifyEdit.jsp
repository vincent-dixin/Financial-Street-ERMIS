<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.rectify.rectifyEdit', {
		extend : 'Ext.form.Panel',
		renderTo:'FHD.rectify.rectifyEditForm${param._dc}',
		items:[],
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
		processId:{},//涉及流程
		defectId:{},//涉及缺陷
		reasonDetail:{},//具体原因
		initComponent :function() {
		  var me = this;
		  
		  me.id = {
				  xtype : 'textfield',
				  hidden: true,
				  name : 'id'
		  }
		  me.code = {
				  xtype:"textfield",
                  fieldLabel:"编号"+'<font color=red>*</font>',
                  allowBlank:false,
                  margin : '7 5 5 30',
                  name:"code",
                  value:''
                  };
		  
		  me.compSelect=Ext.create('FHD.ux.org.CompanySelectList',{
	          	fieldLabel : '所属公司'+'<font color=red>*</font>',
	          	margin : '7 5 5 30',
	        	name : 'company',
	            multiSelect : false
			});
		  me.name = {
					xtype : 'textfield',
					fieldLabel : '改进名称' + '<font color=red>*</font>',
					allowBlank : false,
					//width : 400,
					margin : '7 5 5 30',
					name : 'name'
		  };
		  me.org = Ext.create('FHD.ux.org.CommonSelector', {
				fieldLabel : '负责部门'+ '<font color=red>*</font>',
				margin : '7 5 5 30',
				name:'orgId',
				type : 'dept',
			    labelWidth : 100,
				//allowBlank : false,
				multiSelect : false
			});
		  
		me.empERId = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '实施负责人'+ '<font color=red>*</font>',
			labelAlign : 'left',
			margin : '7 5 5 30',
			name:'empERId',
			labelWidth : 100,
			type : 'emp',
			multiSelect : false
		});
		me.empERPId = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : ' 复核人'+ '<font color=red>*</font>',
			labelAlign : 'left',
			margin : '7 5 5 30',
			name:'empERPId',
			labelWidth : 100,
			type : 'emp',
			multiSelect : false
		});
		
		me.planStartDate =  {
				xtype: 'datefield',
		        fieldLabel:'计划开始时间'+ '<font color=red>*</font>',
		        name: 'planStartDate',
		        margin : '7 5 5 30',
		        //columnWidth : .3,
		        format: 'Y-m-d',
		        labelWidth:100,
		};
		
		me.planEndDate = {
				xtype: 'datefield',
		        fieldLabel:'计划结束时间'+ '<font color=red>*</font>',
		        name: 'planEndDate',
		        margin : '7 5 5 30',
		        //columnWidth : .3,
		        format: 'Y-m-d',
		        labelWidth:100,
		};
		me.improvementSource = Ext.create('FHD.ux.dict.DictCheckbox',
				{
			margin : '7 5 5 30',
			name:'improvementSource',dictTypeId:'ref_improvement_source',labelAlign:'left',columns:5,fieldLabel : '改进原因'+ '<font color=red>*</font>'
		});

		me.improvementTarget = {
				xtype : 'textareafield',
				margin : '7 5 5 30',
				fieldLabel : '改进目标'+ '<font color=red>*</font>',
			    //width : 400,
				rows : 3,
				name : 'improvementTarget'
		};
		me.processId = Ext.create('FHD.ux.process.processSelector',{                   
	        name:'processId',
	        single : false,	 
	        margin : '7 5 5 30',
	        height : 80,
	        labelText: $locale('fhd.process.processselector.labeltext'),
	        labelWidth: 100
		});
		
		/* me.defectId = Ext.create('pages.icm.defect.defectSelector',{	
    		columnWidth:.5,                       
              name:'defectId',
              single : false,	            	
              height :50,
              labelText: '缺陷选择',
              margin : '7 5 5 30',
             // labelAlign: 'left',
              labelWidth: 100                      		                       		                       		
      	}); */
		
		me.defectId = Ext.create('pages.icm.defect.defectLevelSelector',{
    	    columnWidth : 1/2,
			name : 'defectId',
			multiSelect : true,//多选没有复选框
			//value : '10,11',
			mytype:1,
			height : 80,
			labelText : '缺陷选择',
			labelAlign : 'left',
			width:150,
			labelWidth : 80
     });
		
		
		me.reasonDetail = {
				xtype : 'textareafield',
				fieldLabel : '具体原因',
				margin : '7 5 5 30',
				//width : 400,
				rows : 3,
				name : 'reasonDetail'
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
				title : '基本信息',
				items:[ me.id,
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
						me.processId,
						me.defectId,
						me.reasonDetail
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
				                   url: __ctxPath+'/icm/improve/saveImprove.f',
				                   callback: function (data){
				                	  	rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
					                	rectify_ManagerView.gridPanel = Ext.create('rectify_rectifyManage_panelAndMenu');
					               		rectify_ManagerView.add(rectify_ManagerView.gridPanel);
					                  
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

	var FHD_icm_rectify_rectifyEdit;
	Ext.onReady(function(){
		FHD_icm_rectify_rectifyEdit=Ext.create('FHD.icm.rectify.rectifyEdit');
		if(${param.type=='yes'}){
			FHD_icm_rectify_rectifyEdit.getForm().load({
				url:'icm/improve/findImproveAdviceForForm.f?improveId=${param.improveId}',
					success: function (form, action) {
	                    
	             	   return true;
	                },
	                failure: function (form, action) {
	             	  
	             	   return true;
	                }
				
			});
		}
		
	});
	
	
</script>
</head>
<body>
	<div id=FHD.rectify.rectifyEditForm${param._dc}></div>
</body>
</html>