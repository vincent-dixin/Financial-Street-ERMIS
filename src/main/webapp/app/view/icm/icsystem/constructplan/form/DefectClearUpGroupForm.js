/**
 * 流程基本信息编辑页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.constructplan.form.DefectClearUpGroupForm', {
	   extend: 'Ext.form.Panel',
	   alias: 'widget.defectclearupgroupform',
       requires: [
	      'FHD.view.icm.icsystem.constructplan.form.DefectClearUpForm'
       ],
       frame: false,
       border : false,
       paramObj : {
       },
       defectclearupform : [],
       autoScroll : true,
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
	       me.defectClearUpFormstr = Ext.widget('textfield', {name: 'defectClearUpFormstr',hidden : true });
           Ext.applyIf(me,{
        	  bodyPadding: "0 3 3 3"
			});
           me.callParent(arguments);
           //向form表单中添加控件
		   me.add(me.constructPlanId,me.defectClearUpFormstr);
       },
	save: function() {
		var me = this;
	    var jsonArray = [];
	    for(var i = 0;i<me.defectclearupform.length;i++){
	    	var validateFlag = false;
    		me.defectclearupform[i].defectDepart.renderBlankColor(me.defectclearupform[i].defectDepart);
    		if(me.defectclearupform[i].defectLevel.value == null){
 				reason = '缺陷等级必填!';
 				validateFlag = true;
 				break;
 			}else if(me.defectclearupform[i].defectType.value == null){
 				reason = '缺陷类型必填!';
 				validateFlag = true;
 				break;
 			}else if(me.defectclearupform[i].description.value == "" || me.defectclearupform[i].description.value == null){
 				reason = '缺陷描述必填!';
 				validateFlag = true;
 				break;
 			}
	    	}
	    	if(validateFlag){
	 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), reason);
	 			return false;
	 		}
	    
	   for(var i = 0;i<me.defectclearupform.length;i++){
	    	jsonArray.push(me.defectclearupform[i].getForm().getValues(false,false,true));
	    	//添加控制评价点信息 
	    }
	    var jsonStr = Ext.encode(jsonArray);
	    var groupForm = me.getForm();
	   	groupForm.setValues({//paramObj
	               constructPlanId : me.businessId,
	               defectClearUpFormstr : jsonStr
	               }); //editGridJson
    	if(groupForm.isValid()) {
    		FHD.submit({
				form : groupForm,
				url : __ctxPath + '/icm/icsystem/savedefectclearup.f',
				callback: function (data) {
					if (data) {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                    }
				}
			});
		}
		return true;
	   },
	   cancel : function(){
	   	  me = this.up('riskmeasuremainpanel');
	   	  me.getLayout().setActiveItem(0);
	   	  me.flowrisklist.reloadData();
	   },
	   getInitData : function(){
	   	    var me = this;
	   		me.defectclearupform = [];
	   		FHD.ajax({
	                 url:__ctxPath+'/icm/icsystem/findDefectRelaDiagnosesIdbyConstructId.f',
	                 params: {
	                 	constructPlanId : me.businessId,
    				    executionId : me.executionId
                 	 },
	                 callback: function (data) {
	                 	me.paramObj.defectId = data.data;
	                 	for(var i = 0;i<me.paramObj.defectId.length;i++){
		                 	me.editform = Ext.widget('defectclearupform');
	       	    			me.defectclearupform.push(me.editform);
	       	    			me.editform.initParam({
	       	    				defectId : me.paramObj.defectId[i]
	       	    			});
   	    			 		me.editform.reloadData();
   	    			 		me.add(me.defectclearupform[i]);
	                 	 }
	                 }
	                 });
       	     
	   }
	   
});