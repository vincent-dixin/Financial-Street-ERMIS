<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">

/***Ext.onReady start***/
Ext.onReady(function(){
		FHD.ajax({//ajax调用
			url : 'icm/improve/findImproveAdviceForForm.f',
			params : {
				improveId:'4abd794c-b60f-4892-bc48-cf47250e66b5'
			},
			callback : function(data){
				viewwindow = Ext.create('Ext.container.Container',{
					title:'整改计划通知',
			    	layout:'fit',
			    	renderTo : "FHD.rectify.rectifyAdvice${param._dc}",
					modal:true,//是否模态窗口
					collapsible:true,
					width:650,
					height:500,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[Ext.create('Ext.panel.Panel',{
	            		width: 300,
	                    bodyStyle: "padding:5px;font-size:12px;"
	            	})],
					listeners: {
		                afterlayout: function() {
		                	var panel = this.down('panel');
	                        tpl = Ext.create('Ext.Template',
	                        		'<p><div style="text-align: center;"><h1>'+data.data.name+'</h1></div></p>',
	                        		'<p><div style="text-align: left;color: red"><h2>'+data.data.company+'</h2></div></p>',
	                        		'<p><div style="text-align: left;"><h3>&nbsp'+'&nbsp'+'&nbsp'+data.data.improvementSource+'的通知'+'</h3></div></p>',
	                        		'<p><div style="text-align: left;">'+data.data.reasonDetail+'</div></p>'   
	                        		
	                        		);

		                    tpl.overwrite(panel.body, data);
		                }
		            }
			    });
			}
		});
	
});
/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='FHD.rectify.rectifyAdvice${param._dc}' style=""></div>
</body>
</html>