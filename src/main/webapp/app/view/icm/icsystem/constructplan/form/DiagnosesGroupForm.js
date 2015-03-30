/**
 * 缺陷反馈主页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.constructplan.form.DiagnosesGroupForm', {
	extend: 'Ext.form.Panel',
	alias: 'widget.diagnosesgroupform',
	requires: [
		'FHD.view.icm.icsystem.constructplan.form.DiagnosesDefectForm'
	],
	frame: false,	
	border : false,
	autoHeight: true,
	paramObj : {
	},
	diagnosesdefectform : [],
	autoScroll : false,
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	reloadData: function() {
		var me = this;
		me.getInitData();
	},
   
	// 初始化方法
	initComponent: function() {
		var me = this;
		// 建设计划id隐藏域
		me.constructPlanId = Ext.widget('textfield', {name: 'constructPlanId',hidden : true });
		me.defectFormstr = Ext.widget('textfield', {name: 'defectFormstr',hidden : true });
   		me.callParent(arguments);
   		me.add(me.constructPlanId,me.defectFormstr);
   	   //向form表单中添加控件
   	},
	save: function() {
		var me = this;
	    var jsonArray = [];
	    for(var i = 0;i<me.diagnosesdefectform.length;i++){
	    	var validateFlag = false;
    		if(me.diagnosesdefectform[i].isAgree.lastValue.isAgree == null){
 				reason = '是否认同必填!';
 				validateFlag = true;
 				break;
 			}else if(me.diagnosesdefectform[i].isAgree.lastValue.isAgree == '0yn_n' && me.diagnosesdefectform[i].feedbackoptions.value == ""){
 				reason = '反馈意见必填!';
 				validateFlag = true;
 				break;
 			}
	    	}
	    	if(validateFlag){
	 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), reason);
	 			return false;
	 		}
	    for(var i = 0;i<me.diagnosesdefectform.length;i++){
	    	jsonArray.push(me.diagnosesdefectform[i].getForm().getValues(false,false,true));
	    	//添加控制评价点信息 
		    var jsonStr = Ext.encode(jsonArray);
		    var groupForm = me.getForm();
		   	groupForm.setValues({//paramObj
	            constructPlanId : me.businessId,
	            defectFormstr : jsonStr
	            }); //editGridJson
    		FHD.submit({
				form : groupForm,
				url : __ctxPath + '/icm/icsystem/savediagnosesdefect.f',
				callback: function (data) {
				}
			});
	    }
	    return true;
	},
	getInitData : function(){
   	    var me = this;
   		me.diagnosesdefectform = [];
   		FHD.ajax({
			url:__ctxPath+'/icm/icsystem/finddefectidbyconstructplanid.f',
			params: {
			constructPlanId : me.businessId,
			executionId : me.executionId
		},
		callback: function (data) {
			me.paramObj.defectId = data.data;
			for(var i = 0;i<me.paramObj.defectId.length;i++){
				me.editform = Ext.widget('diagnosesdefectform',{autoHeight:true});
				me.diagnosesdefectform.push(me.editform);
				me.editform.initParam({
					defectId : me.paramObj.defectId[i]
				});
			me.editform.reloadData();
			me.add(me.diagnosesdefectform[i]);
			}
		}
		});
	}
});