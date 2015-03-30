<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构/人员选择</title>
<script type="text/javascript">
	Ext.onReady(function(){
		var emppanel = Ext.create('Ext.form.Panel',{
			bodyPadding: '0 3 3 3',
			renderTo:'empselectdemo',
			useArrows: true,
			height:FHD.getCenterPanelHeight()-5,
			items: [/* {xtype: 'fieldset',
						defaults: {columnWidth: 1/2,labelWidth: 100,margin:'5 50 5 50'},//每行显示一列，可设置多列
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: '公司选择',
						items:[
							Ext.create('FHD.ux.org.CompSelect',{
	                        	fieldLabel : '公司多选',
	                            multiSelect : true,
	                            name : '1',
	                            labelAlign : 'right'
							}),
							Ext.create('FHD.ux.org.CompSelect',{
	                        	fieldLabel : '公司单选',
	                        	name : '2',
	                            multiSelect : false,
	                            labelAlign : 'right'
							})
						]
					}, */
					{xtype: 'fieldset',
						defaults: {columnWidth: 1/2,labelWidth: 100,margin : '7 10 0 30'},//每行显示一列，可设置多列
						layout: {type: 'column'},
						collapsed: false,
						collapsible: false,
						title: '机构选择',
						items:[
							/* Ext.create('FHD.ux.org.CompanySelectList',{
		                    	id:'company',
		                    	
		                    	labelAlign: 'right',
		                    	maxHeight:300,
		                        fieldLabel: '先选公司'
		                    }),
		                    Ext.create('FHD.ux.org.DeptSelectList',{
		                    	cascadeCompany:'company',
		                    	
		                    	labelAlign: 'right',
		                        fieldLabel: '后选机构'
		                    }), */
		                    Ext.create('FHD.ux.org.CommonSelector',{
		                    	fieldLabel : '机构多选',
		                    	labelAlign: 'right',
		                    	type : 'dept',
		                    	subCompany: true,//显示子公司
		                    	companyOnly: false,//显示公司和部门
		                    	rootVisible: true,//显示根机构
		                    	value : '[{"id":"13c2667cdfe444d99c0625cbec215375"},{"id":"0e5254f249e74d63be576c8b8076c4ca"}]',
		                        multiSelect : true
		                    }),
		                    Ext.create('FHD.ux.org.CommonSelector',{
		                    	fieldLabel : '机构单选',
		                    	labelAlign: 'right',
		                    	type : 'dept',
		                    	value : '[{"id":"13c2667cdfe444d99c0625cbec215375"}]',
		                        multiSelect : false
		                    })
						]
					},
					{xtype: 'fieldset',
						defaults: {columnWidth: 1/2,labelWidth: 100,margin : '7 10 0 30'},//每行显示一列，可设置多列
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: '员工选择',
						items:[
	                        Ext.create('FHD.ux.org.CommonSelector',{
	                        	fieldLabel: '员工多选',
	                        	labelAlign: 'right',
	                        	subCompany:true,
	                        	rootVisible: true,//显示根机构
	                            type:'emp',
	                            multiSelect:true
	                        }),
	                        Ext.create('FHD.ux.org.CommonSelector',{
	                        	fieldLabel: '员工单选',
	                        	labelAlign: 'right',
	                            type:'emp',
	                            multiSelect:false
	                        })
						]
					},
					{xtype: 'fieldset',
						defaults: {columnWidth: 1/2,labelWidth: 100,margin : '7 10 0 30'},//每行显示一列，可设置多列
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: '员工选择',
						items:[
	                        Ext.create('FHD.ux.org.CommonSelector',{
	                        	fieldLabel: '机构员工多选',
	                        	labelAlign: 'right',
	                            type:'dept_emp',
	                            subCompany: true,
	                            multiSelect:true
	                        }),
	                        Ext.create('FHD.ux.org.CommonSelector',{
	                        	fieldLabel: '机构员工单选',
	                        	labelAlign: 'right',
	                            type:'dept_emp',
	                            multiSelect:false
	                        })
						]
					}
			      ]
		});
		tree.on('collapse',function(p){
			emppanel.setWidth(panel.getWidth()-26-5);
		});
		tree.on('expand',function(p){
			emppanel.setWidth(panel.getWidth()-p.getWidth()-5);
		});
		panel.on('resize',function(p){
			emppanel.setHeight(p.getHeight()-5);
			if(tree.collapsed){
				emppanel.setWidth(p.getWidth()-26-5);
			}else{
				emppanel.setWidth(p.getWidth()-tree.getWidth()-5);
			}
		});
		tree.on('resize',function(p){
			if(p.collapsed){
				emppanel.setWidth(panel.getWidth()-26-5);
			}else{
				emppanel.setWidth(panel.getWidth()-p.getWidth()-5);
			}
		});
});
	
</script>
</head>
<body>
	<div id='empselectdemo'></div>
</body>
</html>