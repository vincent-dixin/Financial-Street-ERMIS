/**
 * @author : 邓广义
 *  风险评价报告模板管理表单面板
 */
Ext.define('FHD.view.risk.assess.report.RiskAssessReportTemplateForm',{
 	extend: 'Ext.form.Panel',
 	alias : 'widget.riskassessreporttemplateform',
    requires : [
                ],
	reloadData:function(){},
    initComponent: function () {
		var me = this;
		
		
		
		
		
		var bbar =[//按钮
    	           '->',
    	           {text : "保存",iconCls: 'icon-save', handler:me.save, scope : this}];
		Ext.applyIf(me,{
			tbar:bbar,
            border: false,
            bodyPadding: "5 5 5 5",
            flex:1,
		})
		me.callParent(arguments);
		me.createReportContent();
		
		me.processFieldset = Ext.widget('fieldset',{
			collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '0 30 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
                columnWidth: 1
            },
            layout: {
                type: 'column'
            },
            title: '评价范围列表'
    	});
		me.add(me.processFieldset);
    	
    	me.processGrid = Ext.create('FHD.ux.GridPanel',{
    		height:140,
    		url: __ctxPath + '/comm/report/findReportProcessList.f',
    		extraParams:{
    			assessplanId:''
    		},
    		searchable:false,
    		checked:false,
    		pagable:false,
    		cols:[{
    			header:'名称',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'使用名',dataIndex:'processName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'demo',dataIndex:'isPracticeTest',flex:2
    		}]
    	});
    	
    	me.processFieldset.add(me.processGrid);
		
	},
	createReportContent:function(){

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
			        url : __ctxPath + '/app/view/comm/report/assess/TestReportTpl.xml',
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