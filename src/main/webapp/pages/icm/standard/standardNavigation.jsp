<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
//{title:[{},{},{}],items:[[{},{},{}],[{},{},{}],[{},{},{}]]}
	var fhd_icm_standard_standardNavigation=(function(){
		
		var standardId='${param.standardId}';//内控Id
		
		var startHtml="<div><ul class='xbreadcrumbs' id='breadcrumbs-3'>";
		//标题li
		var titleHtml =function(title){
			return "<li><span class='home'>" + title + "</span></li>"
		};
		//同级别li
		var getLiHtml =function(id, name){
			return "<li><a id='" + id + "'>" + name + "</a></li>";
		};
		//当前指标名称
		var getNameHtml=function(name){
			return "<li class='current'> " + name + "</li>";
		};
		//同级别结束LI
		var getStartEndHtml =function(){
			return "</ul></li>";
		};
		//结束DIV
		var getEndHtml =function(){
			return "</ul></div>";
		};
		//创建导航菜单
		var createNavigation=function(){
			var navigationMenu="";
			
			Ext.Ajax.request({
			    url: __ctxPath + '/standard/standardTree/createStandardCode.f?standardId='+this.standardId,
			    async :  false,
			    success: function(response){
			    	
			        var text = response.responseText;
			        me.array = new Array();
			        var mapCount = Ext.JSON.decode(text).count;
			        var mapc = mapCount - 1;
			        var countroot = Ext.JSON.decode(text).countroot;
			        
			        
			        Ext.each(Ext.JSON.decode(text).result,function(r,i){
			        	me.array.push({id:r.id, name:r.name});
			        });
			        
			        if(countroot != '-1'){
				        for(var i = 0; i < me.array.length; i++){
			        			//存在父级并且不是根
			        			if(me.array[i].id.indexOf('++,,root' + 'my') != -1){
			        				//把自己添加到A标签中
			        				html += me.getMyLiHtml(me.array[i].id, me.array[i].name);
			        				break;
			        			}
			        	}
				        
				        for(var i = 0; i < me.array.length; i++){
		        			//存在父级并且不是根
		        			if(me.array[i].id.indexOf('_root') != -1){
		        				//同级别添加到LI中
		        				html += me.getLiHtml(me.array[i].id, me.array[i].name);
		        			}
				        }
			        	html += me.getStartEndHtml();
			        }
			        
			        if(mapc != 0){
			        	var count = 1;
				        while(true){
				        	for(var i = 0; i < me.array.length; i++){
				        			//存在父级并且不是根
				        			if(me.array[i].id.indexOf('++,,' + mapc + 'my') != -1){
				        				//把自己添加到A标签中
				        				html += me.getMyLiHtml(me.array[i].id, me.array[i].name);
				        				break;
				        		}
				        	}
				        	
				        	for(var i = 0; i < me.array.length; i++){
				        		if(me.array[i].id.indexOf('_' + mapc) != -1){
				        			//存在父级并且不是根
				        			if(me.array[i].id.indexOf('_' + mapc) != -1){
				        				//同级别添加到LI中
				        				html += me.getLiHtml(me.array[i].id, me.array[i].name);
				        			}
				        		}
				        	}
				        	
				        	html += me.getStartEndHtml();
				        	count++;
				        	mapc--;
				        	if(count == mapCount){
				        		break;
				        	}
				        }
			        }
			        //html += me.getNameHtml(this.name);
		        	html += me.getEndHtml();
			    }
			});
			
		};
		
		
	})();
	
	
	
	
	
</script>
</head>
<body bgcolor="red">
	<div  id='FHD.icm.standard.standardNavigation${param._dc}'>
	
	</div>
</body>
</html>