
jQuery.validator.addMethod("isMobile", function(value, element) {
	var length = value.length;
    return this.optional(element) || length == 11 && /^1[358]\d{9}$/.test(value);
},"请填写正确的手机号码");
//电话号码验证
jQuery.validator.addMethod("isTel", function(value, element) {
var tel = /^\d{3,4}-?\d{7,8}$/; //电话号码格式-
return this.optional(element) || (tel.test(value));
}, "请正确填写您的电话号码");

// 联系电话(手机/电话皆可)验证
jQuery.validator.addMethod("isPhone", function(value,element) {
var length = value.length;
var mobile = /^((([-]{})|([-]{}))+\d{})$/;
var tel = /^\d{3,4}-?\d{7,8}$/;
return this.optional(element) || (tel.test(value) || mobile.test(value));
}, "请正确填写您的联系电话"); 

// 邮政编码验证
jQuery.validator.addMethod("isZipCode", function(value, element) {
var tel = /^[-]{}$/;
return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码");

//部门/员工选择控件验证
jQuery.validator.addMethod("isNeed", function(value, element) {
var tel = /^\s*$/g;
var nextSibling = element.nextSibling;
return this.optional(nextSibling) || (tel.test(value) || value=="");
}, "必选字段");
/**
 * 控件值比较大于
 * param:比较控件name属性
 */
jQuery.validator.addMethod("gt",function(value,element,param){
	var flag=true;
		var element2=jQuery("[name='"+param+"']");
		if(value!=""&&element2.val()!=""&&!(value>element2.val())){
			flag=false;
		}
		return flag;
},jQuery.validator.format("必须大于{0}"));
/**
 * 控件值比较小于等于
 * param:比较控件name属性
 */
jQuery.validator.addMethod("ge",function(value,element,param){
	var flag=true;
	var element2=jQuery("[name='"+param+"']");
	if(value!=""&&element2.val()!=""&&!(value>=element2.val())){
		flag=false;
	}
	return flag;
},jQuery.validator.format("必须大于等于{0}"));
/**
 * 控件值比较小于
 * param:比较控件name属性
 */
jQuery.validator.addMethod("lt",function(value,element,param){
	var flag=true;
	var element2=jQuery("[name='"+param+"']");
	if(value!=""&&element2.val()!=""&&!(value<element2.val())){
		flag=false;
	}
	return flag;
},jQuery.validator.format("必须小于{0}"));
/**
 * 控件值比较小于等于
 * param:比较控件name属性
 */
jQuery.validator.addMethod("le",function(value,element,param){
	var flag=true;
	var element2=jQuery("[name='"+param+"']");
	if(value!=""&&element2.val()!=""&&!(value<=element2.val())){
		flag=false;
	}
	return flag;
},jQuery.validator.format("必须小于等于{0}"));
/**
 * grid非空验证(程序限制必须在grid附着div后添加一个同名name隐藏域)
 * param:grid
 */
jQuery.validator.addMethod("gridRequired",function(value,element,param){
	var flag=true;
	if(param.store.data.length==0){
		flag=false;
	}
	return flag;
},jQuery.validator.format("必填数据"));
/**
 * file类型验证
 * params:允许后缀名数组
 */
jQuery.validator.addMethod("fileType",function(value,element,params){
	var flag=true;
	var message="";
	if(value&&value!=""&&params&&params.length>0){
		flag=false;
		var star=value.lastIndexOf(".");
		var suffix="";
		if(star!=0){
			suffix=value.substr(star+1,value.length);
		}
		for ( var i = 0; i < params.length; i++) {
			if(suffix==params[i]){
				flag=true;
				break;
			}
		}
		
		message=params.join(",");
		//params[0]=message;
	}
	return flag;
},jQuery.validator.format("允许的后缀名为：\"{0}\""));