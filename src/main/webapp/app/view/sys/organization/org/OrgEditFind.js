/**
 * 人员管理编辑FORM处理
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.org.OrgEditFind',{
	url:'sys/organization/findorgbyid.f',
	findOrgById : function(){
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
	        }
	    });
	}
}
});