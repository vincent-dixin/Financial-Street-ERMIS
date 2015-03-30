<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
	Ext.define('FHD.icm.standard.standardEdit', {
		extend : 'Ext.form.Panel',
		//renderTo:'FHD.icm.standard.standardEditForm${param._dc}',
		title:'基本信息',
		items:[],
		layout : {
			type : 'column'
		},
		defaults : {
			columnWidth : 1/1
		},
		controlType:'',
		nodeId:'',
		addType:'',
		isLeaf:'',
		idSeq:'',
		flagAddType:'',
		standardId:'',
		upText:'',
		collapsed : false,
		height : FHD.getCenterPanelHeight(),
		bbar: {},
		initComponent :function() {
		  var me = this;
		   var parentIdFile={
					xtype : 'textfield',
					disabled : false,
					name : 'parent.id',
					hidden:true
		    };
		   var levelField={
					xtype : 'textfield',
					disabled : false,
					name : 'level',
					hidden:true
		    };
		   var idSqlField={
					xtype : 'textfield',
					disabled : false,
					name : 'idSeqp',
					hidden:true
		    };
		
		    var idStroFile={
					xtype : 'textfield',
					disabled : false,
					name : 'id',
					hidden:true
		    };
			var standardUpStep={
					id : 'standardUpStepId',
					labelWidth : 80,
					xtype : 'textfield',
					name:'upName',
					readOnly:true,
					lblAlign:'right',
					fieldLabel : '分类'
							+ '<font color=red>*</font>',
					margin: '7 10 10 30',
					allowBlank : true	
			};
			var standardCode = {
				id : 'standardCodeId',
				labelWidth :80,
				xtype : 'textfield',
				disabled : false,
				lblAlign:'rigth',
				fieldLabel : '编号'
						+ '<font color=red>*</font>',
				value : '',
				name : 'code',
				margin: '7 10 10 30',
				maxLength : 200,
				columnWidth:.4,
				allowBlank : false
			};
			var standardCreateCodeButton={
	                   xtype: 'button',
	                   margin: '7 30 3 3',
	                   id:'standardCreateCodeButtonId',
	                   text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
	                   handler: function(){
	               			FHD.ajax({
	                         url:__ctxPath+'/standard/standardTree/createStandardCode.f',
	                         params: {
	                         nodeId: me.nodeId
	                         },
	                         callback: function (data) {
	                         me.getForm().setValues({'code':data.code});//给code表单赋值
	                         }
	                         });
	                   },
	                   columnWidth: .1
	               };
			var standardName ={
					id : 'standardNameId',
					xtype : 'textfield',
					labelWidth : 80,
					disabled : false,
					fieldLabel : '名称'
							+ '<font color=red>*</font>',
					value : '',
					name : 'name',
					margin: '7 10 10 30',
					allowBlank : false
			};
			/*控制层级 */
			var standardControlLevel=
				Ext.create('FHD.ux.dict.DictSelect',
						{
					     name:'controlLevelId',
					     dictTypeId:'ic_control_level',
					     margin: '7 10 10 30',
					     labelWidth : 80,
					     value:'',
					     multiSelect:false,
					     fieldLabel : '控制层级'
				     });
			/*控制要求  */
			var standardControlRequirement={
				         xtype: 'textareafield',
	                     margin: '7 10 10 30',
	                     labelWidth : 80,
	                     name:'controlRequirement',
	                     rows: 5,
	                     maxLength : 200,
	                     fieldLabel:'控制要求'
			             };
			/*内控要素 */
			var standardControlPoint=
				 Ext.create('FHD.ux.dict.DictSelect',
							{
						     name:'controlPoint',
						     dictTypeId:'ic_control_point',
						     margin: '7 10 10 30',
						     labelWidth : 80,
						     multiSelect:false,
						     fieldLabel : '内控要素'
					     });
			/*责任部门  */
			var standardDepart=
				 Ext.create('FHD.ux.org.CommonSelector',{
                 	fieldLabel : '部门单选',
                 	name:'deptId',
                 	margin: '7 10 10 30',
                 	type : 'dept',
                 	labelWidth : 80,
                    multiSelect : false
                 });
						
			/*状态 */
			var standardStatus=
				 Ext.create('FHD.ux.dict.DictSelectForEditGrid',
							{
						     name:'statusId',
						     dictTypeId:'ic_control_standard_estatus',
						     margin: '7 10 10 30',
						     labelWidth : 80,
						     labelAlign:'left',
						     varlue:'请选择',
						     multiSelect:false,
						     fieldLabel : '状态'
			    });
			/*附件  */	 
			var fileUpLoad={
				xtype:'FileUpload',	
				fieldLabel : '附件',
				labelWidth : 80,
				margin: '7 10 10 30',
				name:'fileId',
				value:'',
				showModel:''
			};
			
			me.items= [{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/2
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
				margin: '8 10 0 10',
				collapsible : false,
				title : '基本信息',
				items:[standardUpStep,standardCode,
				       standardCreateCodeButton,
				       standardName,standardControlLevel,
				       standardControlRequirement,
				       standardControlPoint,standardDepart,
				       standardStatus,idStroFile,
				       parentIdFile,levelField,idSqlField]
	            }, {
					xtype : 'fieldset',
					title : '相关附件',
					margin: '10 8 0 10',
					defaults : {
						columnWidth : 1/1
					},//每行显示一列，可设置多列
					layout : {
						type : 'column'
					},
					items:[fileUpLoad]
		       }];
			me.bbar={
		               style: 'background-image:url() !important;background-color:rgb(250,250,250);',
		               items: [{
		                   xtype: 'tbtext',
		                  // text: '<font color="#3980F4"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset') + '</b></font>'
		               }, '->',{
		                    text:'保 存',
		                    // iconCls: 'icon-control-fastforward-blue',
		                    width:100,
		                    handler: function () {
		                    //提交from表单
		                    var form = me.getForm();
		                    var vobj = form.getValues();
		                    var validUrl= __ctxPath + '/standard/standardTree/validateStandard.do';
		                    if(me.controlType=='listEdit'){
		                     validUrl=validUrl+'?isSaveOrEdit=1'
		                    }
		                    if(!form.isValid()){
		                     Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
		                     return;
		                    }
		                    FHD.ajax({
		                     url:validUrl,
		                     params: {
		                     name: vobj.name,
		                     code: vobj.code
		                     },
		                     callback: function (data) {
		                     if (data.flagStr=="nameRepeat") {
		                      Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.nameRepeat'));
		                      return;
		                      }
		                     if (data.flagStr == "codeRepeat") {
		                      Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.codeRepeat'));
		                      return;
		                      }
		                     if (data.flagStr=='notRepeat') {
		                      //提交目标信息
		                      var addUrl='';
		                     if(me.controlType=='listEdit'){
		                      addUrl=__ctxPath+'/standard/standardTree/editStandard.do';
		          	          }else{
		          	          addUrl=__ctxPath + '/standard/standardTree/saveStandard.do?nodeId='+me.nodeId+'&addType='+me.addType+'&isLeaf='+me.isLeaf+'&idSeq='+me.idSeq+'&flagAddType='+me.flagAddType+'&standardId='+me.standardId;
		          	          }
		                      FHD.submit({
		                      form: form,
		                      url: addUrl,
		                      callback: function (data) {
		                      if(data.success){
		                      form.reset();
		                      standardTree_ManagerView.tabEditPanel.setActiveTab(1);
		                     var tree=standardTree_ManagerView.icmStandardTreeManage.standardTree;
									var node = tree.getSelectionModel().getLastSelected();
							        var path = node.getPath('id');
							        tree.getStore().load({
							        	    scope   : this,
							        	    callback: function(records, operation, success) {
							        	    	 tree.expandPath(path);
							        	    }
							       });
		                      standard_standardGridPanel.store.proxy.url=__ctxPath+ '/standard/standardGrid/findStandardByPage.f?clickedNodeId='+me.standardId+'&isLeaf=1',//调用后台url
						      standard_standardGridPanel.store.load();
		                      }
		                      }
		                      });
		                      }
		                      }
		                     });
		                   }
		               }, {
		                   text: FHD.locale.get("fhd.common.cancel"),
		                   // iconCls: 'icon-control-fastforward-blue',
		                   width:100,
		                   handler: function () {
		                	   standardTree_ManagerView.tabEditPanel.setActiveTab(1);
		                   }
		               }]
		           };
			Ext.applyIf(me,{
				items:me.items
			});
			me.callParent(arguments);
			}
		});

	var FHD_icm_standard_standardEdit;
	Ext.onReady(function(){
		FHD_icm_standard_standardEdit=Ext.create('FHD.icm.standard.standardEdit',{
			//heigth:FHD.getCenterPanelHeight()-100
		});
	});
	
	
</script>
</head>
<body>
	<div id='FHD.icm.standard.standardEditForm${param._dc}'></div>
</body>
</html>