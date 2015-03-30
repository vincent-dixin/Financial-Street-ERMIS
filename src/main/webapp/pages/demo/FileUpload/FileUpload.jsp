<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传择</title>
<script type="text/javascript">
	Ext.onReady(function(){
		var filepanel = Ext.create('Ext.form.Panel',{
			bodyPadding: 5,
			renderTo:'fileuploaddemo',
			height:FHD.getCenterPanelHeight(),
			items: [
				{
					xtype: 'fieldset',
					defaults: {columnWidth: 1/2,margin:'5 50 5 50'},//每行显示一列，可设置多列
					layout: {type: 'column'},
					bodyPadding: 5,
					collapsed: false,
					collapsible: false,
					title: '多选',
					items:[
						{
	                        xtype: 'FileUpload',
	                        name:'fileIds1',//名称
	                        showModel:'base',//显示模式
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',//初始化参数
	                        labelText: $locale('fileupdate.labeltext'),//标题名称
	                        labelAlign: 'right',//标题对齐方式
						},{
	                        xtype: 'FileUpload',
	                        name:'fileIds2',
	                        showModel:'base',
	                        windowModel:'selector',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',
	                        labelAlign: 'right'
						},{
	                        xtype: 'FileUpload',
	                        name:'fileIds3',
	                        showModel:'grid',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',
	                        labelAlign: 'right'
						},{
	                        xtype: 'FileUpload',
	                        name:'fileIds4',
	                        showModel:'grid',
	                        windowModel:'selector',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',
	                        labelText: $locale('fileupdate.labeltext'),
	                        labelAlign: 'right'
						}
					]
				}
				/*预留单选模式展示，暂不可用*/
				/* ,{
					xtype: 'fieldset',
					defaults: {columnWidth: 1/2,margin:'5 50 5 50'},//每行显示一列，可设置多列
					layout: {type: 'column'},
					bodyPadding: 5,
					collapsed: false,
					collapsible: false,
					title: '单选',
					items:[
						{
	                        xtype: 'FileUpload',
	                        name:'fileIds5',
	                        showModel:'base',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078',
	                        labelText: $locale('fileupdate.labeltext'),
	                        labelAlign: 'right',
							multiSelect:false
						},{
	                        xtype: 'FileUpload',
	                        name:'fileIds6',
	                        showModel:'base',
	                        windowModel:'selector',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',
	                        labelAlign: 'right',
							multiSelect:false
						},{
	                        xtype: 'FileUpload',
	                        name:'fileIds7',
	                        showModel:'grid',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',
	                        labelText: $locale('fileupdate.labeltext'),
	                        labelAlign: 'right',
							multiSelect:false
						},{
	                        xtype: 'FileUpload',
	                        name:'fileIds8',
	                        showModel:'grid',
	                        windowModel:'selector',
	                        value:'f1545902-6bef-49c3-a15a-751530b7e078,f4b4801e-4081-4f80-a3d2-dbd06d19fa77',
	                        labelText: $locale('fileupdate.labeltext'),
	                        labelAlign: 'right',
							multiSelect:false
						}
					]
				} */
			]
		});
		if(typeof(tree)!='undefined'){
			tree.on('collapse',function(p){
				filepanel.setWidth(panel.getWidth()-26-5);
			});
			tree.on('expand',function(p){
				filepanel.setWidth(panel.getWidth()-p.getWidth()-5);
			});
			panel.on('resize',function(p){
				filepanel.setHeight(p.getHeight()-5);
				if(tree.collapsed){
					filepanel.setWidth(p.getWidth()-26-5);
				}else{
					filepanel.setWidth(p.getWidth()-tree.getWidth()-5);
				}
			});
			tree.on('resize',function(p){
				if(p.collapsed){
					filepanel.setWidth(panel.getWidth()-26-5);
				}else{
					filepanel.setWidth(panel.getWidth()-p.getWidth()-5);
				}
			});
		}
	});
	
</script>
</head>
<body>
	<div id='fileuploaddemo'></div>
</body>
</html>