/**
 * 岗位管理编辑FORM处理
 * 
 * @author 
 */
Ext.define('FHD.view.sys.organization.positiion.PositionEditFind',{
	url:'sys/organization/findpositionbyid.f',
	findPositionById : function(){
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