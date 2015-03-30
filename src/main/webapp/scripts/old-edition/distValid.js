/*验证风险分布参数的有效性*/
function checkDistParam(){
	alert('***');
   var distId=$id("distribution").getValue();
   Ext.Ajax.request({
	url:'${ctx}/kpi/target/getDictByParam.do',
    method:'post',
    params:{
		distribution: distId//参数
    },
    success:function(response){
  
	    var str = new String(response.responseText);
	    if(response.responseText==''){
			Ext.MessageBox.alert('提示', '此分布下缺少参数信息，请联系管理员!');
			return false;
		}else{
               var paramArray = new Array();
			   paramArray = ret.split(","); 
			   var str="";
			   if(paramArray.length>0){
			     for(var i=0;i<paramArray.length;i++){
			          var param=document.getElementById("dictparam"+i).value; 
			          var numReg=/^(-?\d+)(\.\d+)?$/;
			          if(param==""){
			            alert("参数不能为空");
			            document.getElementById("dictparam"+i).focus();
			            return false;
			          }
			          else if(!numReg.test(param)){
			            alert("参数必须为数字类型");
			            document.getElementById("dictparam"+i).focus();
			            return false;
			          }
			          else{        
			          	str+=paramArray[i]+"="+param+","; 
			          }      
			    
			     }
			   } 
			    
			    //为字段distParam赋值
			   //var docForm=document.forms["riskinput_form"];
			   //var quantity=docForm.elements["risk/quantityparams"];
			   var distParam = $id('distParam');
			   var params= str.substring(0,str.length-1);
			   quantity.value=params;
			   //获得输入的参数数组num
			   var num = new Array();
			   var array = new Array();
			   array = params.split(",");
			   if(array.length>0){
			     for(var i=0;i<array.length;i++){
			        num[i]=array[i].substring(array[i].indexOf('=')+1);
			     }
			   }
			   //综合验证分布参数和风险
			   if(validateInput(distId,num)){
			   	   return true;//验证成功
			      }else{
			      	return false;
			      }
 			}
		}
		});
}

//综合验证
function validateInput(distName,params){

   switch(distName){
     case "Uniform":
         return checkUniform(params);
         break;
     case "Weibull":
         return  checkWeibull(params);
         break;
     case "Lognormal":
         return  checkLognormal(params);
         break;
     case "Logistic":
         return  checkLogistic(params);
         break;
     case "Normal":
         return checkNormal(params);
         break;
   }

}
//风险验证
function checkRisk(){
  var oForm=document.forms["riskinput_form"];
  var oRiskId=oForm.elements["risk/riskid"];
  if(oRiskId.value==""){
		alert("请选择风险!");
		return false;
	}
	
	return true;
}

//正态分布：两个参数（μ , σ）其中σ>0
function checkNormal(params){
      if(params[1]<=0){
        alert("第一个参数必须大于0");
        return false;
      }else{     
       return true;
       } 
}
//均匀分布验证：两个参数（a,b）
//其中 参数必须符合0<a<1,0<b<1,a<b
function checkUniform(params){
       if(params[0]<=0||params[0]>=1){
        alert("第一个参数必须在0到1之间");
        return false;
       }else if(params[1]<=0||params[1]>=1){
         alert("第二个参数必须在0到1之间");
         return false;
       }else if(params[0]>=params[1]){
          alert("第一个参数必须小于第二个参数");
          return false;
       }else{
        return true;
       }
 
}
//韦伯分布验证，三个参数(alpha,lambda,delta)
//其中lambda>0,其余可以任意
function checkWeibull(params){
 if(params[1]<=0){
        alert("第二个参数必须大于0");
        return false;
 }else{     
       return true;
 } 
}
//对数正态分布：两个参数 μ， σ
//其中参数  σ >0
function checkLognormal(params){

       if(params[1]<=0){
         alert("第二个参数必须大于0");
         return false;
      }else{     
       return true;
       }
}
//罗杰斯分布验证:两个参数α，λ
//其中参数必须符合：λ>0
function checkLogistic(params){
      if(params[1]<=0){
        alert("第二个参数必须大于0");
        return false;
      }else{     
       return true;
       }
}