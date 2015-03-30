<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程选择</title>
<script type="text/javascript">
	Ext.require([ 'Ext.form.*' ]);

	Ext.onReady(function() {

		/*责任部门  */
	var	processDepart = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '部门单选',
			margin : '7 5 5 30',
			name:'orgId',
			type : 'dept',
		    labelWidth : 100,
			//allowBlank : false,
			multiSelect : false
		});
		/*员工单选 */
	var	processradio = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '员工单选',
			labelAlign : 'left',
			margin : '7 5 5 30',
			name:'empId',
			labelWidth : 100,
			type : 'emp',
			multiSelect : false
		});
		/*重要性*/
		var importance = Ext.create('FHD.ux.dict.DictSelect', {
			name : 'controlLevelId',
			dictTypeId : 'ic_processure_importance',
			margin : '7 5 5 30',
			labelWidth : 100,
			multiSelect : false,
			fieldLabel : '重要性'
		});
		//iyt
		var processId={
                xtype:"textfield",
                fieldLabel:"流程ID",
                name:"id",
                hidden:true,
                };

		var formPanel = Ext.create('Ext.form.Panel', {
			
			height:FHD.getCenterPanelHeight(),
			autoScroll:true,
			bodyPadding: 5,
			items : [ {
				xtype : 'fieldset',
				title : FHD.locale.get('fhd.common.baseInfo'),
				flex:1,
				layout : {
					type : 'column'
				},
				defaults : {
					columnWidth : 1 / 2,
					/* labelWidth:80,
					margin: '3 3 3 3' */
					
					
				},
				items : [   
				{
					xtype : 'textfield',
					fieldLabel : '上级流程' + '<font color=red>*</font>',
					margin : '7 5 5 30',
					allowBlank : false,
					lableWidth:80,
					value : '${param.processtext}',
					name : 'parent'
				}, {
					xtype : 'textfield',
					fieldLabel : '编号' + '<font color=red>*</font>',
					allowBlank : false,
					//width : 400,
					margin : '7 5 5 30',
					
					name : 'code'
				}, {
					xtype : 'textareafield',
					height:60,
					rows : 3,
					fieldLabel : '名称' + '<font color=red>*</font>',
					allowBlank : false,
					//width : 400,
					margin : '7 5 5 30',
					value : '${param.processName}',
					name : 'name'
				}, {
					xtype : 'textareafield',
					margin : '7 5 5 30',
					fieldLabel : '控制目标',
				    //width : 400,
					rows : 3,
					name : 'controlTarget'
				}, processDepart,processradio, importance, {
					xtype : 'numberfield',
					fieldLabel : '排序',
					margin : '7 5 5 30',
					//width : 400,
					name : 'sort'
				}, {
					//columnWidth:1,
					xtype : 'ruleselector',
					extraParams : {
						smIconType : 'display',
						canChecked : true
					},
					name : 'ruleId',
					value:'1026',
					multiSelect:true,
					margin : '7 5 5 30',
					labelText : $locale('fhd.icm.rule.ruleSelectorLabelText'),
					labelAlign : 'left',
					labelWidth : 100
				}, {
					xtype : 'textareafield',
					fieldLabel : '描述',
					margin : '7 5 5 30',
					//width : 400,
					rows : 3,
					name : 'desc'
				},processId/* ,  */ ]
			},{xtype: 'fieldset',
				flex:1,
				defaults: {
					columnWidth: 1/1,
					margin: '3 3 3 3'},//每行显示一列，可设置多列
				layout: {type: 'column'},
				title: FHD.locale.get('fhd.common.baseInfo'),
				items:[
					{
						xtype : 'FileUpload',
						margin : '7 5 5 30',
						labelAlign : 'left',
						columnWidth : 1 / 1,
						fieldLabel : '附件',
						labelWidth : 80,
						name : 'fileId',
						showModel : ' '
					}       
				]}
			],
			
		   	  bbar:{
             	   style: 'background-image:url() !important;background-color:rgb(250,250,250);',
	               items: [{
 	                   xtype: 'tbtext',
 	               }, '->',{
 	                   text: FHD.locale.get("fhd.common.submit"),
 	                  // iconCls: 'icon-control-fastforward-blue',
 	                   width:100,
 	                   handler: save
 	               }]
             	  },listeners:{
             		  onLoad:function(){
             			  alert();
             		  }
             	  }
		/* 	dockedItems : [ {
				xtype : 'toolbar',
				ui : 'footer',
				dock : 'bottom',
				layout : {
					type : 'hbox',
					pack : 'center'
				},
				items : [ {
					text : '保存',
					width : 80,
					handler : save
				// disabled: true  
				} ]
			} ] */

		});
		
		
		//判断如果是修改数据，给from。初始化值
		if('${param.processEdit}'=='yes'){
			 //var form=form.getForm();
			 formPanel.getForm().load({
                  waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                  url: __ctxPath + '/process/process/editProcess.f',
                  params: {
                	  processEditID:'${param.processEditID}'
                  },
                  success: function (form, action) {
                	 // alert();
                      return true;
                  },
                  failure: function (form, action) {
                	 // alert(action);
                  }
              });
		}/* else{
			 formPanel.getForm().load({
                 waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                 url: __ctxPath + '/process/process/ProcessCode.f',
                 params: {
               	  processEditID:'${param.processEditID}'
               		parentId:'&{param.parentId}'
                 },
                 success: function (form, action) {
               	 // alert();
                     return true;
                 },
                 failure: function (form, action) {
               	 // alert(action);
                 }
             });
		} */
		
		
		function save() {
			formPanel.getForm().submit({
				clientValidation : true,
				url : __ctxPath + '/process/process/saveProcess.f?parentId=${param.parentId}',
					
				method : 'POST',
					success: function (formPanel, action) {
						process_ManagerView.processTreeManage.processTree.getStore().load();
						process_ManagerView.remove(process_ManagerView.rightGridPanel);
						process_ManagerView.rightGridPanel=Ext.create('rightGridPanel',{
							autoLoad : {
								url : 'pages/process/processEdit.jsp',

								scripts : true
							}
						});
						process_ManagerView.add(process_ManagerView.rightGridPanel);
	      				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '保存成功');
					}
			});
			process_ManagerView.processTreeManage.processTree.getStore().load();//刷新
		};

		formPanel.render('form');
		
		//自动改变窗口大小 
		process_ManagerView.processTreeManage.on('collapse',function(p){
			formPanel.setWidth(process_ManagerView.getWidth()-5);
		});
		process_ManagerView.processTreeManage.on('expand',function(p){
			formPanel.setWidth(process_ManagerView.getWidth()-p.getWidth()-5);
		});
		process_ManagerView.on('resize',function(p){
			formPanel.setHeight(p.getHeight()-5);
			if(process_ManagerView.processTreeManage.collapsed){
				formPanel.setWidth(p.getWidth()-26-5);
			}else{
				formPanel.setWidth(p.getWidth()-process_ManagerView.processTreeManage.getWidth()-5);
			}
		});
		process_ManagerView.processTreeManage.on('resize',function(p){
			if(p.collapsed){
				formPanel.setWidth(process_ManagerView.getWidth()-5);
			}else{
				formPanel.setWidth(process_ManagerView.getWidth()-process_ManagerView.processTreeManage.getWidth()-5);
			}
		});
	});
</script>
</head>
<body>
	<div id='form'>
		<div id="l1"></div>
	</div>
</body>
</html>