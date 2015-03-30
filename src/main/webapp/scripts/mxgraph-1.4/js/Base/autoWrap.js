var autoWrap=function(entityId,length){

 this.divId="div."+entityId;//初始化外层DIVID
 this.resultId="result."+entityId;//初始化同步结果TEXTID
 this.entityId=entityId;//初始化默认TEXTID
    this.length=length;//每行最长长度
    this.split="/r/n";
    
  this._init();
};

autoWrap.prototype.get=function(e){
 return typeof(e)=='string'?document.getElementById(e):e;
};
autoWrap.prototype.addEvent=function(o,e,fn){
 window.attachEvent ?o.attachEvent('on'+e,fn):o.addEventListener(e,fn,false);
};
autoWrap.prototype.removeEvent=function(o,e,fn){
 window.detachEvent ?o.detachEvent('on'+e,fn):o.removeEventListener(e,fn,false);
};
autoWrap.prototype.wrapkeyup=function(e){//按键弹起事件 
//判断是否超长
  var str=e.srcElement.value;
  var num=0;
  var wrap_i=0;
  for(i=0;i<str.length;i++){
    num++;
    if(str.charCodeAt(i)>255)num++;//汉字作为2个字符计算
    if(num>this.length)
    {
    wrap_i=i;
    break;
    }
    }
    
  if(wrap_i>0)//是否超长
    e.srcElement.value=str.substring(0,wrap_i);
    else
    this.oldValue=str;

//数据同步处理
this.setResultValue(this.entityId);
}

autoWrap.prototype.wrap=function(e){
  var id_end=e.srcElement.id.split("_")[1];//ID后缀
////////////////////////////////////////////////////// 
if(e.keyCode==13){//回车事件 
  var srcElement=this.get(this.entityId+"_"+id_end);

  id_end++;//自动增加为下一个ID
  
  var aElement=this.get(this.entityId+"_"+id_end);
  
  if(aElement)//是否存在下一个,存在则使其获得焦点
  {
  aElement.focus();
  }
  else//是否存在下一个,不存在则创建
  {
  aElement=document.createElement("<input type='text' name='"+this.entityId+"'>");
  aElement.id=this.entityId+"_"+id_end; 
  aElement.className="wrap_text";//初始化CSS

  var self=this; 
  this.addEvent(aElement,'keydown',function(e){self.wrap(e)});
  this.addEvent(aElement,'keyup',function(e){self.wrapkeyup(e)});
  this.addEvent(aElement,'focus',function(e){self.wrapfocus(e)});
  this.addEvent(aElement,'blur',function(e){self.wrapblur(e)});
 
  this.div.appendChild(aElement);
  aElement.focus();
  }  
  } 
////////////////////////////////////////////////////// 
 if(e.keyCode==8&&e.srcElement.value.length==0){//回格事件
  
  if(id_end>0)//判断是否最初的一个
  {
  id_end--;//自动减少为上一个ID
  
  var aElement=this.get(this.entityId+"_"+id_end);
  aElement.focus();
  var textRange=aElement.createTextRange();
  textRange.collapse(false);
  textRange.select();
  
  this.div.removeChild(e.srcElement);
  }
 } 
//////////////////////////////////////////////////////   


};

autoWrap.prototype.wrapfocus=function(e){
e.srcElement.className="wrap_focus";
}
autoWrap.prototype.wrapblur=function(e){
e.srcElement.className="wrap_blur";
}

autoWrap.prototype.setResultValue=function(arg){
var str=document.getElementsByName(arg)
var temp_str="";
for(i=0;i<str.length;i++)
{
if(i<str.length-1)
temp_str+=str[i].value+"\r\n";
else
temp_str+=str[i].value;
}
this.result.value=temp_str;
};

//初始化默认值输入框TEXT
autoWrap.prototype._initValue=function(){
    //取得同步结果TEXT初始值，进行初始化
 var guide=this.result.value;
 
 if(guide.length>0)
 {
 var guide_list=guide.split(this.split);
 for(i=0;i<guide_list.length;i++)
 {
 if(i==0)
 this.entity.value=guide_list[0];
 if(i>0){
 aElement=document.createElement("<input type='text' name='"+this.entityId+"'>");
    aElement.id=this.entityId+"_"+i;
    aElement.value=guide_list[i];
    aElement.className="wrap_blur";

  var self=this; //初始化默认TEXT事件

  this.addEvent(aElement,'keydown',function(e){self.wrap(e)});
  this.addEvent(aElement,'keyup',function(e){self.wrapkeyup(e)});
  this.addEvent(aElement,'focus',function(e){self.wrapfocus(e)});
  this.addEvent(aElement,'blur',function(e){self.wrapblur(e)});
 
  this.div.appendChild(aElement);
 }
 }
 
 }
}
autoWrap.prototype._init=function(){
 this.div=this.get(this.divId);//外层DIV
 this.entity=this.get(this.entityId+"_0");//默认TEXT
 this.result=this.get(this.resultId);//同步结果TEXT
 this.entity.className="wrap_blur";//初始化默认TEXT样式
   
  if(typeof this.entity!='object'){
   alert('entity is not object!');
   return;
  }

  var self=this; //初始化默认TEXT事件
  
 this.addEvent(this.entity,'keydown',function(e){self.wrap(e)});
 this.addEvent(this.entity,'keyup',function(e){self.wrapkeyup(e)});
 this.addEvent(this.entity,'focus',function(e){self.wrapfocus(e)});
    this.addEvent(this.entity,'blur',function(e){self.wrapblur(e)});

    this._initValue();
    
};

