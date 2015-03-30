Ext.define('pages.icm.assess.assessPlanBasicInformation',{
	extend : 'Ext.form.Panel',
	border:false,
	autoScroll:true,
	items : [],
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1 / 1,
		margin:'7 10 10 30'
	},
	collapsed : false,
	initComponent : function() {
		var me = this;
		idStroFile = {
			xtype : 'textfield',
			disabled : false,
			name : 'id',
			hidden : true
		};
		//所属公司
		assessPlanCompany ={
			labelWidth : 80,
			xtype : 'displayfield',
			disabled : false,
			lblAlign : 'rigth',
			fieldLabel : '公司名称',
			value : '',
			name : 'companyName',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//编号
		assessPlanCode = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'rigth',
			fieldLabel : '编号',
			value : '',
			name : 'code',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//名称
		assessPlanName = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '名称',
			name : 'name',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		/*评价方式 */
		assessPlanMeasure ={
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '评价方式',
			name : 'assessMeasureName',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		/*评价类型  */
		assessPlanType = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '评价类型',
			name : 'type',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		/*范围选择方式 */
		assessPlanScaleSetMeasure = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '范围要求',
			name : 'scopeReq',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		/*评价期间  */
		assessPlanDateAmongStart = {
			xtype: 'displayfield',
			fieldLabel:'评价期始',
			name: 'targetStartDate',
			margin: '7 10 0 30',
			columnWidth : .25,
			labelWidth:80
		};
		assessPlanDateAmongEnd = {
			xtype: 'displayfield',
			fieldLabel:'评价期止',
			name: 'targetEndDate',
			margin: '7 10 0 30',
			columnWidth : .25
		};
		/*计划评价时间  */
		assessPlanTimeStart = {
			xtype: 'displayfield',
			fieldLabel:'计划期始',
			name: 'planStartDate',
			margin: '7 10 0 30',
			columnWidth : .25,
			labelWidth:80
		};
		assessPlanTimeEnd = {
			xtype: 'displayfield',
			fieldLabel:'计划期止',
			name: 'planEndDate',
			margin: '7 10 0 30',
			columnWidth : .25,
			labelWidth:80
		};
		/*组长 */
		assessPlanGroup = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'rigth',
			fieldLabel : '组长',
			value : '',
			name : 'responsibilityEmp',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		/*组成员 */
		assessPlanGroupPers = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'rigth',
			fieldLabel : '组成员',
			value : '',
			name : 'handlerEmp',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		/*评价目标 */
		assessPlanTarget = {
			xtype : 'displayfield',
			fieldLabel : '目标',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'assessTarget',
			columnWidth : .5,
			labelWidth : 80,
			row : 5
		};
		/*基本要求 */
		assessPlanStandard = {
			xtype : 'displayfield',
			fieldLabel : '基本要求',
			labelAlign : 'left',
			margin: '7 10 0 30',
			row : 5,
			columnWidth : .5,
			name : 'assessStandard',
			labelWidth : 80
		};
		/*时间要求*/
		assessPlanRequirement = {
			xtype : 'displayfield',
			fieldLabel : '时间要求',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'requirement',
			columnWidth : .5,
			labelWidth : 80,
			row : 5
		};
		/*考核要求 */
		assessPlanDesception={
			xtype : 'displayfield',
			fieldLabel : '考核要求',
			labelAlign : 'left',
			margin: '7 10 0 30',
			columnWidth : .5,
			name : 'desc',
			labelWidth : 80,
			row : 5
		};
		me.items=[idStroFile,
            		assessPlanCompany,
            		assessPlanCode,
					assessPlanName,
					assessPlanType,
					assessPlanTarget,
					assessPlanScaleSetMeasure,
					assessPlanMeasure,
					assessPlanStandard,
					assessPlanRequirement,
					assessPlanDesception,
					assessPlanTimeStart,
					assessPlanTimeEnd,
					assessPlanDateAmongStart,
					assessPlanDateAmongEnd,
					assessPlanGroup,
					assessPlanGroupPers];
		this.callParent(arguments);
	}
});
