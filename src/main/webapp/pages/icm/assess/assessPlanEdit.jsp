<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评价计划编辑</title>
<script type="text/javascript">

	Ext.define('FHD_icm_assess_assessPlanEdit',{
						extend : 'Ext.form.Panel',
						renderTo:'FHD.icm.assess.assessPlanEdit${param._dc}',
						height : FHD.getCenterPanelHeight(),
						fhd_icm_assess_assessPlanEditSubMitFlag:'no',
						autoScroll:true,
						items : [],
						bbar : {},
						layout : {
						type : 'column'
						},
						defaults : {
						columnWidth : 1 / 1
						},
						collapsed : false,
						assessPlanId:'',
						initComponent : function() {
							var me = this;
							var idStroFile = {
								xtype : 'textfield',
								disabled : false,
								name : 'id',
								hidden : true
							};
							//所属公司
							var assessPlanCompany =Ext.create('FHD.ux.org.CompanySelectList',{
								fieldLabel : '所属公司'+ '<font color=red>*</font>',
								name : 'company',
								columnWidth : .5,
								labelWidth : 80,
								value:__user.companyId,
								multiSelect : false,
								margin : '7 10 0 30'
					         }); 
							
							//编号
							var assessPlanCode = {
								labelWidth : 80,
								xtype : 'textfield',
								readOnly:true,
								disabled : false,
								lblAlign : 'rigth',
								fieldLabel : '编号'+ '<font color=red>*</font>',
								value : '',
								name : 'code',
								margin : '7 10 0 30',
								maxLength : 200,
								columnWidth : .5,
								allowBlank : false
							};
							//名称
							var assessPlanName = {
								xtype : 'textfield',
								labelWidth : 80,
								disabled : false,
								fieldLabel : '名称'+ '<font color=red>*</font>',
								name : 'name',
								margin : '7 10 0 30',
								columnWidth : .5,
								allowBlank : false
							};
							/*评价方式 */
							var assessPlanMeasure = Ext.create(
									'FHD.ux.dict.DictSelectForEditGrid', {
										dictTypeId : 'ca_assessment_measure',
										margin : '7 10 0 30',
										labelWidth : 80,
										//readOnly:true,
										allowBlank : false,
										labelAlign : 'left',
										name : 'assessMeasure',
										multiSelect : false,
										columnWidth : .5,
										fieldLabel : '评价方式'+ '<font color=red>*</font>'
									});
							/*评价类型  */
							var assessPlanType = Ext.create(
									'FHD.ux.dict.DictSelectForEditGrid', {
										dictTypeId : 'ca_assessment_etype',
										labelAlign : 'left',
										margin : '7 10 0 30',
										labelWidth : 80,
										//readOnly:true,
										value : "",
										name : 'type',
										columnWidth : .5,
										multiSelect : false,
										fieldLabel : '评价类型'+ '<font color=red>*</font>'
									});
							assessPlanType.setValue('ca_assessment_etype_self');
							
							/*选择范围 */
							//1.按部门选择
							var assessPlanDepart ={
									    xtype:'textfield',
										name : 'deptId',
										hidden:true
									};
							
							//2.按流程选择
							var assessPlanRelaProcess = {
								   xtype:'textfield',
								   name : 'processId',
								   hidden:true
								};
                            assessPlanType.on('change',function(oldValue,newValue,ii){
								
								if(newValue=='ca_assessment_etype_self'){//自评
									assessPlanGroup.show(); 
									assessPlanGroupPers.hide();
								}else if(newValue=='ca_assessment_etype_other'){//他评
									assessPlanGroup.show(); 
									assessPlanGroupPers.show();
								}
							});
							/*评价期间  */
							var assessPlanDateAmongStart = {
							        xtype: 'datefield',
							        fieldLabel:'评价期始',
							        name: 'targetStartDate',
							        margin: '7 10 0 30',
							        columnWidth : .25,
							        format: 'Y-m-d',
							        labelWidth:80,
							};
							var assessPlanDateAmongEnd = {
							        xtype: 'datefield',
							        fieldLabel:'评价期止',
							        name: 'targetEndDate',
							        columnWidth : .25,
							        margin: '7 10 0 30',
							        format: 'Y-m-d',
							};
							/*计划评价时间  */
							var assessPlanTimeStart = {
									 xtype: 'datefield',
									 fieldLabel:'计划期始',
								     name: 'planStartDate',
								     margin: '7 10 0 30',
								     columnWidth : .25,
								     format: 'Y-m-d',
								     labelWidth:80,
							};
							var assessPlanTimeEnd = {
									 xtype: 'datefield',
									 fieldLabel:'计划期止',
								     name: 'planEndDate',
								     margin: '7 10 0 30',
								     columnWidth : .25,
								     format: 'Y-m-d',
								     labelWidth:80,
							};
							/*组长 */
							var assessPlanGroup = Ext.create('FHD.ux.org.CommonSelector', {
										fieldLabel : '组长',
										labelAlign : 'left',
										margin: '7 10 0 30',
										name : 'groupLeaderId',
										columnWidth : .5,
										value:'[{"id":"'+__user.empId+'"}]',
										labelWidth : 80,
										type : 'emp',
										multiSelect : false
									});
							
							/*组成员 */
							var assessPlanGroupPers = Ext.create('FHD.ux.org.CommonSelector', {
										fieldLabel : '组成员',
										labelAlign : 'left',
										margin: '7 10 0 30',
										name : 'groupPersId',
										columnWidth : .5,
										//value:'[{"id":"liruiqun"},{"id":"chenjie"},{"id":"hanwei"}]',
										labelWidth : 80,
										hidden:true,
										type : 'emp'
									});
						

							/*评价目标 */
							var assessPlanTarget = {
								xtype : 'textareafield',
								fieldLabel : '目标',
								labelAlign : 'left',
								margin: '7 10 0 30',
								name : 'assessTarget',
								columnWidth : .5,
								labelWidth : 80,
								row : 5
							};
							/*基本要求 */
							var assessPlanStandard = {
								xtype : 'textareafield',
								fieldLabel : '基本要求',
								labelAlign : 'left',
								margin: '7 10 0 30',
								row : 5,
								columnWidth : .5,
								name : 'assessStandard',
								labelWidth : 80
							};
							/*时间要求*/
							var assessPlanRequirement = {
								xtype : 'textareafield',
								fieldLabel : '时间要求',
								labelAlign : 'left',
								margin: '7 10 0 30',
								name : 'requirement',
								columnWidth : .5,
								labelWidth : 80,
								row : 5
							};
							
							/*考核要求 */
							var assessPlanDesception={
									xtype : 'textareafield',
									fieldLabel : '考核要求',
									labelAlign : 'left',
									margin: '7 10 0 30',
									columnWidth : .5,
									name : 'desc',
									labelWidth : 80,
									row : 5
							};
							
							me.items = [{
										xtype : 'fieldset',
										margin: '7 10 0 30',
										layout : {
											type : 'column'
										},
										collapsed : false,
										collapsible : false,
										title : '基本信息',
										items : [idStroFile,assessPlanCompany,assessPlanCode,
										         assessPlanName,assessPlanType,assessPlanTarget,
										         assessPlanMeasure,assessPlanStandard,
										         assessPlanRequirement,assessPlanDesception,assessPlanDepart,
										         assessPlanRelaProcess
										         ]
									}
									,{
										xtype : 'fieldset',
										margin: '7 10 0 30',
										layout : {
											type : 'column'
										},
										collapsed : false,
										collapsible : false,
										title : '时间选择',
										items : [assessPlanTimeStart,assessPlanTimeEnd,
										         assessPlanDateAmongStart,assessPlanDateAmongEnd
										         ]
									},
									{
										xtype : 'fieldset',
										margin: '7 10 0 30',
										layout : {
											type : 'column'
										},
										collapsed : false,
										collapsible : false,
										title : '人员选择',
										items : [assessPlanGroup,assessPlanGroupPers]
									}
							];
							 this.callParent(arguments);
	},
	 validateDate:function(date1,date2){//验证时间
			    var sd1=date1.split("-");
			    var sd2=date2.split("-");
			    var oldDate=new Date(sd1[0],sd1[1],sd1[2]);
			    var newDate=new Date(sd2[0],sd2[1],sd2[2]);
			if(oldDate<=newDate){
				return true;
			}else{
				return false;
			}
	 }
	,nextSetp:function(isNextStep){
		var me=this;
		//提交from表单
        var form = me.getForm();
        var vobj = form.getValues();
        var validUrl= __ctxPath + '/assess/assessPlan/validateAssessPlanForm.f';
        if(!form.isValid()){
        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
        return
        }
        if(vobj.planStartDate!=''&&vobj.planEndDate!=''){
        	 if(!me.validateDate(vobj.planStartDate,vobj.planEndDate)){
             	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'计划开始时间不能大于结束时间!');
             	return;
             }
        }
        if(vobj.targetStartDate!=''&&vobj.targetEndDate!=''){
       	 if(!me.validateDate(vobj.targetStartDate,vobj.targetEndDate)){
            	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'评价开始时间不能大于结束时间!');
            	return;
            }
       }
		FHD.ajax({
        url:validUrl,
        async:false,
        params: {
        assessPlanId:vobj.id,
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
        	me.fhd_icm_assess_assessPlanEditSubMitFlag='yes';
        //提交目标信息
        var url='';
     	url=__ctxPath + '/assess/assessPlan/mergeAssessPlan.f';
        FHD.submit({
        form: form,
        url: url,
        callback: function (data) {
        if(data.success){
        	if(isNextStep){
        		fhd_icm_assess_assessPlanPanelManager.remove(fhd_icm_assess_assessPlanPanelManager.centerContainer);
            	fhd_icm_assess_assessPlanPanelManager.setCenterContainer('pages/icm/assess/assessPlanListEdit.jsp?assessMeasureId='+vobj.assessMeasure+'&assessPlanId='+data.assessPlanId);
            	fhd_icm_assess_assessPlanPanelManager.add(fhd_icm_assess_assessPlanPanelManager.centerContainer);
        	}
        }
        }
        });
        }
        }
        });
	}
 });
	var fhd_icm_assess_assessPlanEdit=null;
	Ext.onReady(function() {
		fhd_icm_assess_assessPlanEdit=Ext.create('FHD_icm_assess_assessPlanEdit',{
			height : FHD.getCenterPanelHeight()
		});
		FHD.componentResize(fhd_icm_assess_assessPlanEdit,0,0);
		   if("${param.initForm}"=='1'){
			     fhd_icm_assess_assessPlanEdit.getForm().load({
	                                   url:__ctxPath + '/assess/assessPlan/findAssessPlan.f?assessPlanId=${param.assessPlanId}',
	                                   success: function (form, action) {
	                                           
	                                	   return true;
	                                   },
	                                   failure: function (form, action) {
	                                	  
	                                	   return true;
	                                   }
	        });
		}
		else{
			 //创建编号
				FHD.ajax({
					url : __ctxPath+ '/assess/assessPlan/createAssessPlanCode.f',
						callback : function(data) {
						fhd_icm_assess_assessPlanEdit.getForm().setValues(
						 {
						'code' : data.code
						 });//给code表单赋值
						
						 }
					});
		   }
	});
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessPlanEdit${param._dc}'></div>
</body>
</html>