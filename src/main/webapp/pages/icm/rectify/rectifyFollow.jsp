<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.rectify.rectifyEdit', {
		extend : 'Ext.form.Panel',
		autoScroll:true,
		renderTo:'FHD.rectify.rectifyEditForm${param._dc}',
		items:[],
		layout : {
			type : 'column'
		},
		defaults : {
			columnWidth : 1/1
		},
		autoWidth:true,
		collapsed : false,
		height : FHD.getCenterPanelHeight(),
		bbar: {},
		improveScenario:{},//改进方案
		file:{},//附件
		//tbarItems:[],
		improveGrid:{},//整改跟踪
		initComponent :function() {
		  var me = this;
		
		  me.improveScenario = {
				  xtype:"textfield",
                  fieldLabel:"改进方案",
                 // allowBlank:false,
                  height:100,
                  name:"improveScenario",
                  value:''
                  };
		  
		  me.file={
				  xtype : 'FileUpload',
					margin : '7 5 5 30',
					labelAlign : 'left',
					//columnWidth : 1 / 1,
					fieldLabel : '附件',
					labelWidth : 80,
					name : 'file',
					showModel : ' '
	        
			};
		  var tbar=[{iconCls : 'icon-add',xtype:'button',id : 'add${param._dc}',handler :me.addRectify,scope : this}];
			me.improveGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
				columnWidth : 1,
				multiSelect:false,
				autoScroll:true,
				tbarItems:tbar,
				checked:false,
				url: 'icm/improve/findImproveTraceListBypage.f',
				cols:[{header:'id',dataIndex:'id',sortable: false,hidden:true},
				      {header:'实际开始日期', dataIndex: 'actualStartDate', sortable: false,flex:1},
				      {header:'实际结束日期', dataIndex: 'actualEndDate',sortable: false,flex:1},
				      {header:'完成比例', dataIndex: 'finishRate', sortable: false,flex:1},
				      {header:'进度情况', dataIndex: 'improveResult', sortable: false,flex:1},
				      {header:'说明', dataIndex: 'comment', sortable: false,flex:1}]
			}); 
		
			
			me.items= [{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/2,
					margin:'7 10 10 30'
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
				//margin: '8 10 0 10',
				collapsible : false,
				title : '基本信息',
				items:[ me.improveScenario,
				        me.file
				      ]
	            },{
					xtype : 'fieldset',
					defaults : {
						columnWidth : 1/2,
						margin:'7 10 10 30'
					},//每行显示一列，可设置多列
					layout : {
						type : 'column'
					},
					collapsed : false,
					//margin: '8 10 0 10',
					collapsible : false,
					title : '基本信息',
					items:[ 
					        me.improveGrid
					      ]
		            }];
			me.bbar={
		               style: 'background-image:url() !important;background-color:rgb(250,250,250);',
		               items: [{
		                   xtype: 'tbtext',
		               }, '->',{
		                   text:'保存',
		                   width:100,
		                   handler: function () {
		                	   FHD.submit({
		                		   form: me.getForm(),
				                   url: __ctxPath+'/icm/defect/saveDefect.f',
				                   callback: function (data){
				                	  	defect_ManagerView.remove(defect_ManagerView.gridPanel);
					                	defect_ManagerView.gridPanel = Ext.create('defect_defectManage_panelAndMenu');
					               		defect_ManagerView.add(defect_ManagerView.gridPanel);
					                  
				                   }
		                		  });
		                   }
		                   },{
		                   text:'提交',
		                   width:100,
		                   handler: function () {}
		                   }, {
		                   text:'返  回',
		                   width:100,
		                   handler: function () {
		                	   rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
			                	rectify_ManagerView.gridPanel = Ext.create('rectify_rectifyManage_panelAndMenu');
			               		rectify_ManagerView.add(rectify_ManagerView.gridPanel);
		                   }
		               }]
		           };
			Ext.applyIf(me,{
				items:me.items
			});
			me.callParent(arguments);
			}, 
			addRectify:function(){
				var me=this;
				 var form = Ext.create('Ext.form.Panel',{
			      		width: 390,
			      		height: 500,
			      		layout: 'column',
			      	    defaults:{
			      	    	columnWidth:1
			      	    },
			            items:[{
							  xtype : 'textfield',
							  hidden: true,
							  name : 'id'
					         },{
			              	xtype : 'datefield',
								fieldLabel : '实际开始日期',
								margin : '7 5 5 30',
								height: 25,
								 format: 'Y-m-d',
								name : 'actualStartDate'
			              },{
			              	xtype : 'datefield',
								fieldLabel : '实际结束日期',
								margin : '7 5 5 30',
								height: 25,
								 format: 'Y-m-d',
								name : 'actualEndDate'
			              },{
			              	xtype : 'textfield',
								fieldLabel : '完成比例',
								margin : '7 5 5 30',
								height: 25,
								name : 'fRate'
			              },{
			              	xtype : 'textfield',
								fieldLabel : '进度情况',
								margin : '7 5 5 30',
								height: 25,
								name : 'improveResult'
			              },{
			              	xtype : 'textfield',
								fieldLabel : '说明',
								margin : '7 5 5 30',
								height: 25,
								name : 'comment'
			              }]
			      	});
			var viewwindow = new Ext.window.Window({
			title:'基本信息',
			layout: 'column',
    	    columnWidth:1,
    	    collapsible:true,
    	    modal : true,
    	    maximizable:true,
			width:400,
			height:500,
			items:[form],					
        	 buttons : [
        	            {
        	             text : "保存",
        	             iconCls : "btn-save",
        	             handler : function() {
        	            	 FHD.submit({
		                		   form: form.getForm(),
				                   url: __ctxPath+'/icm/improve/saveImproveTrace.f?improveId=${param.improveId}',
				                   callback: function (data){
				                	   viewwindow.close();
				                	   me.improveGrid.store.load();				                	
					                  
				                   }
		                		  });
        	             }
        	            },
        	            {
        	             text : "重置",
        	             iconCls : "reset",
        	             handler : function() {
        	              
        	             }
        	            } ]
	   }).show(); 
	
     }
		});
	 
	var FHD_icm_rectify_rectifyEdit;
	Ext.onReady(function(){
		FHD_icm_rectify_rectifyEdit=Ext.create('FHD.icm.rectify.rectifyEdit');
		FHD_icm_rectify_rectifyEdit.getForm().load({
			url:'icm/improve/findImproveAdviceForview.f?improveId=${param.improveId}',
				success: function (form, action) {
                    
             	   return true;
                },
                failure: function (form, action) {
             	  
             	   return true;
                }
			
		});
	});
	
	
</script>
</head>
<body>
	<div id=FHD.rectify.rectifyEditForm${param._dc}></div>
</body>
</html>