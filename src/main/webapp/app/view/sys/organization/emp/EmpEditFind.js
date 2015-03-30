/**
 * 人员管理编辑FORM处理
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.emp.EmpEditFind',{
	url:'sys/organization/findempbyid.f',
	findEmpById : function(lockState,enable){
	var me = this;
	if(typeof(param.id) != 'undefined') {
		formPanel.form.load({
	        url:me.url,
	        params:{id:param.id},
	        failure:function(form,action) {
	            alert("err 155");
	        },
	        success:function(form,action){
	        	var formValue = form.getValues();
	        	//用户锁定状态
	        	if(formValue.lockstate == '1'){
	        		lockState.items.items[0].checked = true;
					lockState.items.items[0].setValue(true);
	        	}else{
	        		lockState.items.items[1].checked = true;
					lockState.items.items[1].setValue(true);
	        	}
	        	//是否启用
	        	if(formValue.enable == '1'){
	        		enable.items.items[0].checked = true;
	        		enable.items.items[0].setValue(true);
	        	}else{
	        		enable.items.items[1].checked = true;
	        		enable.items.items[1].setValue(true);
	        	}
				
	        }
	    });
	}
}
});