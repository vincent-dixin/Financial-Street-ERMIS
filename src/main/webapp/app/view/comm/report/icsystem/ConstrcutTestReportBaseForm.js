Ext.define('FHD.view.comm.report.icsystem.ConstrcutTestReportBaseForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.constrcuttestreportbaseform',
	
	requires: [
	    'FHD.ux.icm.icsystem.ConstructPlanSelector',
	    'FHD.view.icm.icsystem.constructplan.ConstructPlanRelaStandardViewGrid'
   	],
	       
	border : false,
	
	autoScroll:true,
    bodyPadding: '0 3 3 3',
	
	
	initComponent: function(){
		var me = this;
		Ext.apply(me,{
			items:[{
				xtype : 'fieldset',
				collapsible: true,
	            defaults: {
	                margin: '7 10 0 30',
	                labelWidth: 100,
	            	labelAlign: 'left',
	                columnWidth: .5,
	                xtype:'textfield',
	                allowBlank : false
	            },
	            layout: {
	                type: 'column'
	            },
	            title: FHD.locale.get('fhd.common.baseInfo'),
	            items: [{
	            	xtype:'hidden',
        			hidden: true,
	            	name:'id'
	            },{
	            	fieldLabel : '标题' + '<font color=red>*</font>',
	            	allowBlank : false,
	            	name : 'reportName'
	            },{
	            	fieldLabel : '版本号' + '<font color=red>*</font>',
	            	allowBlank : false,
	            	name : 'reportCode'
	            }
//	注视掉选择计划的内容            ,{  
//	            	xtype:'constructplanselector',
//	            	fieldLabel : '建设计划',
//	            	name : 'constructPlanId',
//	            	onChange :function(field,nValue,oValue){
//	            		if(!Ext.isEmpty(nValue)) {
//	            			me.constructPlanGrid.extraParams.businessId = nValue;
//	            			me.constructPlanGrid.reloadData();
//	            		}
//	            	}
//	            }
	            ]
			}]
		})
		
		me.callParent(arguments);
		
		Ext.suspendLayouts(); 
		
		me.createReportContent();
		//评价计划可编辑列表  注视掉标准显示列表
//		me.constructPlanGrid=Ext.widget('constructplanrelastandardviewgrid',{
//			columnWidth:1/1,
//			checked:false,
//			tbarItems : [],
//			searchable : false
//		});
//		me.constructPlanGridFieldSet={
//			xtype : 'fieldset',
//			//margin: '7 10 0 30',
//			layout : {
//				type : 'column'
//			},
//			collapsed: false,
//			columnWidth:1/1,
//			collapsible : false,
//			title : '建设范围',
//			items : [me.constructPlanGrid]
//		};
//		me.add(me.constructPlanGridFieldSet);
    	
    	Ext.resumeLayouts(true);
    	
    	me.updateLayout();
	},
	
	createReportContent: function(){
    	var me = this;
    	
    	me.fieldset = Ext.widget('fieldset',{
			collapsible: true,
            defaults: {
                margin: '7 10 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
                columnWidth: 1
            },
            layout: {
                type: 'column'
            },
            title: '报告信息'
    	});
    	
    	me.add(me.fieldset);
    	
    	me.reportData = Ext.widget('textarea',{
    		name : 'reportData',
    		listeners:{
            	afterrender:function(component){
            		/*
            		//插入自定义菜单
            		KindEditor.plugin('tableprop', function(K) {
						var self = this, name = 'tableprop';
						function click(value) {
							self.insertHtml('<p>${' + value + '}</p>');
							self.hideMenu();
						}
						self.clickToolbar(name, function() {
							var menu = self.createMenu({
								name : name,
								width : 150
							});
							menu.addItem({
								title : '缺陷认定结果表',
								click : function() {
									click('缺陷认定结果表');
								}
							});
							menu.addItem({
								title : '缺陷整改情况表',
								click : function() {
									click('缺陷整改情况表');
								}
							});
						});
					});
            		*/
		        	me.editor = KindEditor.create('#' + (component.getEl().query('textarea')[0]).id,{
		        		minHeight:200,
		        		items : [
							'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
							'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
							'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
							'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
							'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
							'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
							'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
							'anchor', 'link', 'unlink', '|', 'about'
						]
		        	});
		        	me.editor.resizeType = 1;
		        	
    	        }  
    		}
    	});
    	
    	if('' === me.reportData.getValue()) {
    		Ext.define('Tpl', {
			    extend: 'Ext.data.Model',
			    fields: ['tpldata']
			});
    		var store = Ext.create('Ext.data.Store', {
			    model: 'Tpl',
			    autoLoad : true,
			    autoSync : true,
			    proxy: {
			        type: 'ajax',
			        url : __ctxPath + '/app/view/comm/report/icsystem/ConstructTestReportTpl.xml',
			        reader: {
			            type: 'xml',
			            record: 'tpl',
			            root: 'tpls'
			        }
			    },
			    listeners:{
			    	load:function(s) {
			    		me.editor.insertHtml(s.getAt(0).data.tpldata);
			    	}
			    }
			});
    	}
    	
    	me.fieldset.add(me.reportData);
    }
	
});