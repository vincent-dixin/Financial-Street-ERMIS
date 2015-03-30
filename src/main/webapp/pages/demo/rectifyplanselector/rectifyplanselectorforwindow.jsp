<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>内控整改计划选择组件</title>
	<script type="text/javascript">
		Ext.onReady(function(){
			var button = Ext.create('Ext.Button', {
                text: '整改计划弹窗',
                renderTo: Ext.getBody(),
                handler: function() {
                	var rectifyplanforwindowselector = Ext.create('FHD.ux.icm.rectify.ImproveSelectorWindow',{
        				multiSelect:true,
        				modal: true,
        				onSubmit:function(win){
        					
        				}
        			}).show();
                }
            });
			
			//表单panel
			var form = Ext.create("Ext.form.Panel",{
				renderTo: 'rectifyplanforwindowdemo',
				autoScroll: true,
		        border: false,
		        bodyPadding: "5 5 5 5",
		        items: [{
		            xtype: 'fieldset',//基本信息fieldset
		            collapsible: false,
		            defaults: {
		            	margin: '7 30 3 30',
		            	columnWidth:.2
		            },
		            layout: {
		                type: 'column'
		            },
		            title: ' 整改计划弹窗选择',
		            items:[button]
		        }]
			});
		});
	</script>
</head>
<body>
	<div id='rectifyplanforwindowdemo'></div>
</body>
</html>