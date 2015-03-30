Ext.define('FHD.view.comm.report.assess.GroupYearReportBaseForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.groupyearreportbaseform',
	
	requires: [
    	'FHD.ux.icm.assess.AssessPlanSelector'
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
	            autoHeight: true,
	            autoWidth: true,
	            defaults: {
	                margin: '7 30 3 30',
	                labelWidth: 105,
	            	labelAlign: 'left',
	                columnWidth: .5,
	                xtype:'textfield'
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
	            	fieldLabel : '标题',
	            	name : 'name'
	            },{
	            	fieldLabel : '版本号',
	            	name : 'code'
	            },{
	            	xtype:'assessplanselector',
	            	fieldLabel : '评价计划',
	            	name : 'assessplanId',
	            	onChange :function(field,nValue,oValue){
	            		if(!Ext.isEmpty(nValue)) {
	            			me.defectGrid.store.proxy.extraParams.assessplanId = nValue;
	            			me.defectGrid.store.load();
	            			me.rectifyGrid.store.proxy.extraParams.assessplanId = nValue;
	            			me.rectifyGrid.store.load()
	            		}
	            	}
	            }]
			}]
		})
		
		me.callParent(arguments);
		
		Ext.suspendLayouts(); 
		
		me.createReportContent();
		
		me.defectFieldset = Ext.widget('fieldset',{
			collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
                columnWidth: 1
            },
            layout: {
                type: 'column'
            },
            title: '缺陷认定结果表'
    	});
		me.add(me.defectFieldset);
		
    	me.defectGrid = Ext.create('FHD.ux.GridPanel',{
    		height:150,
    		url: __ctxPath + '/comm/report/findReportDefectList.f',
    		extraParams:{
    			assessplanId:''
    		},
    		checked:false,
    		//pagable:false,
    		cols:[{
    			header:'末级流程',dataIndex:'processName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'评价点',dataIndex:'assessPointName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'实施证据',dataIndex:'comment',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'缺陷描述',dataIndex:'defectDesc',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'缺陷类别',dataIndex:'defectType',flex:2
    		},{
    			header:'缺陷级别',dataIndex:'defectLevel',flex:2
    		},{
    			header:'整改责任部门',dataIndex:'responsibilityOrg',flex:2
    		}]
    	});
    	
    	me.defectFieldset.add(me.defectGrid);
    	
    	me.rectifyFieldset = Ext.widget('fieldset',{
			collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
                columnWidth: 1
            },
            layout: {
                type: 'column'
            },
            title: '缺陷的整改情况表'
    	});
		me.add(me.rectifyFieldset);
    	
		me.rectifyGrid = Ext.widget('fhdgrid',{
    		height:150,
    		url: __ctxPath + '/comm/report/findReportDefectImproveInfoList.f',
    		extraParams:{
    			assessplanId:''
    		},
    		checked:false,
    		pagable:false,
    		cols:[{
    			header:'缺陷描述',dataIndex:'defectDesc',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'缺陷整改方案',dataIndex:'improvePlanContent',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'缺陷整改复核测试情况',dataIndex:'compensationControl',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'缺陷级别',dataIndex:'defectLevel',flex:2
    		},{
    			header:'缺陷状态',dataIndex:'defectDealStatus',flex:2
    		}]
    	});
    	
    	me.rectifyFieldset.add(me.rectifyGrid);
		
		
    	Ext.resumeLayouts(true);
    	
    	me.updateLayout();
    	
	},
	
	createReportContent: function(){
    	var me = this;
    	
    	me.fieldset = Ext.widget('fieldset',{
			collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '7 30 3 30',
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
    		name : 'reportDataText',
    		listeners:{
            	afterrender:function(component){
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
							'anchor', 'link', 'unlink', '|', 'about','/',
							'tableprop'
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
			        url : __ctxPath + '/app/view/comm/report/assess/CompanyReportTpl.xml',
			        reader: {
			            type: 'xml',
			            record: 'tpl',
			            root: 'tpls'
			        }
			    }/*,
			    listeners:{
			    	load:function(s) {
			    		me.editor.insertHtml(s.getAt(0).data.tpldata);
			    	}
			    }*/
			});
    		store.load({
				callback:function(records, options, success){
					me.editor.insertHtml(records[0].data.tpldata);
				}
			});
    	}
    	
    	me.fieldset.add(me.reportData);
    	
    }
});