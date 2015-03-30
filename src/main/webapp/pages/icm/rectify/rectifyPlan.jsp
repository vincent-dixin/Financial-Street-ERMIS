<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.rectify.rectifyPlan', {
		extend : 'Ext.form.Panel',
		autoScroll:true,
		renderTo:'FHD.rectify.rectifyPlanForm${param._dc}',
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
		//tbarItems:[],
		improveGrid:{},//整改跟踪
		initComponent :function() {
		  var me = this;
		  var id = {
				  xtype : 'textfield',
				  hidden: true,
				  name : 'id'
		  };
		   var name = {
				    xtype:"displayfield",
					fieldLabel : '改进名称',
					allowBlank : false,
					//width : 400,
					margin : '7 5 5 30',
					name : 'name'
		  };
		  
		  var improvementSource = Ext.create('FHD.ux.dict.DictCheckbox',
					{
				margin : '7 5 5 30',
				xtype:"displayfield",
				name:'improvementSource',dictTypeId:'ref_improvement_source',labelAlign:'left',columns:5,fieldLabel : '改进原因'
			});

		  var improvementTarget = {
					xtype:"displayfield",
					margin : '7 5 5 30',
					fieldLabel : '改进目标',
				    //width : 400,
					rows : 3,
					name : 'improvementTarget'
			};
		  var reasonDetail = {
					xtype:"displayfield",
					fieldLabel : '具体原因',
					margin : '7 5 5 30',
					//width : 400,
					rows : 3,
					name : 'reasonDetail'
			};
		  var empERPId = {
				  xtype:"displayfield",
				  fieldLabel : ' 复核人',
                  allowBlank:false,
                  margin : '7 5 5 30',
                  name:'empERPId',
                  value:''
                  };
		  var planStartDate =  {
					xtype:"displayfield",
			        fieldLabel:'计划开始时间',
			        name: 'planStartDate',
			        margin : '7 5 5 30',
			        //columnWidth : .3,
			        format: 'Y-m-d',
			        labelWidth:100,
			};
			
			var planEndDate = {
					xtype:"displayfield",
			        fieldLabel:'计划结束时间',
			        name: 'planEndDate',
			        margin : '7 5 5 30',
			        //columnWidth : .3,
			        format: 'Y-m-d',
			        labelWidth:100,
			};

			me.improveGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
				columnWidth : 1,
				multiSelect:false,
				autoScroll:true,
				//tbarItems:tbar,
				checked:false,
				url: 'icm/improve/findImprovePlanListBypage.f',
				cols:[{header:'id',dataIndex:'id',sortable: false,hidden:true},
				      {header:'责任部门', dataIndex: 'org', sortable: false,flex:1},
				      {header:'缺陷描述', dataIndex: 'desc',sortable: false,flex:1},
				      {header:'方案内容', dataIndex: 'content', sortable: false,flex:1,editor:'textfield'},
				      {header:'实际开始日期', dataIndex: 'actualStartDate', sortable: false,flex:1,editor:'datefield',renderer: Ext.util.Format.dateRenderer('Y-m-d')},
				      {header:'实际结束日期', dataIndex: 'actualEndDate', sortable: false,flex:1,editor:'datefield',renderer: Ext.util.Format.dateRenderer('Y-m-d')},
				      {header:'附件', dataIndex: 'file',sortable: false,
				    	  renderer:function(value,p,record){
				    		  if(value!=''){
				    			  return "<a href='javascript:void(0);'onclick='download()'>"+value+"</a>"+"<a href='javascript:void(0);'onclick='delFile()'><img src='images/icons/del.png'></a>";
				    		  }else{
				    			  return  "<a href='javascript:void(0);'onclick='upload()'>选择文件</a>";
				    		  }
						 }
				      }]
				      
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
				items:[ name,
				        improvementSource,
				        improvementTarget,
				        reasonDetail,
				        empERPId,
				        planStartDate,
				        planEndDate
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
		                		var jsonArray=[];
		                		var rows = me.improveGrid.store.getModifiedRecords();
		                		Ext.each(rows,function(item){
		                			jsonArray.push(item.data);
		                		});
		                		if(jsonArray.length>0){
		                			 FHD.ajax({//ajax调用
		                    		     url : __ctxPath+ '/icm/improvePlan/mergeimprovePlan.f',
		                    		     params : {
		                    		    	 improvePlanId:'111',
		                    		    	 modifiedRecord:Ext.encode(jsonArray)
		                    			 },
		                    			 callback: function (data){		                    			
							                  me.improveGrid.store.load();
						                   }
		                    		});
		                		}
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
	
		});
	

	var improvePlanId;
	var fileId;
	function upload(){
		Ext.create('FHD.ux.fileupload.FileUploadWindow',{
			multiSelect: false,
			callBack:function(value){
				if(value!=null&&value.length>0){
					FHD.ajax({
						url:__ctxPath+'/icm/rectify/mergeimprovePlanFile.f',
						params:{
							fileId:value, 
							improvePlanId:improvePlanId
							},
						callback:function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								FHD_icm_rectify_rectifyEdit.improveGrid.store.load();
							}
						}
					}); 
				}
			}
		}).show();
	};
	function download(){
		downloadFile(fileId);
	};
	function delFile(){
		FHD.ajax({
			url:__ctxPath+'/icm/rectify/removeimprovePlanFile.f',
			params:{
				improvePlanId:improvePlanId 
				},
			callback:function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					FHD_icm_rectify_rectifyEdit.improveGrid.store.load();
				}
			}
		}); 
	}; 
	
	/* function addSample(){
		var selection=me.improveGrid.getSelectionModel().getSelection();
			improvePlanId=selection[0].get('id');
		FHD.ajax({
			//url:__ctxPath+'/icm/assess/mergeSample.f',
			params:{
				improvePlanId:improvePlanId
				},
			callback:function(data){
				if(data){
					//Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					improveGrid.store.load();
				}
			}
		}); 
	};  */
	var FHD_icm_rectify_rectifyEdit;
	Ext.onReady(function(){
		FHD_icm_rectify_rectifyEdit=Ext.create('FHD.icm.rectify.rectifyPlan');
		FHD_icm_rectify_rectifyEdit.getForm().load({
			url:'icm/improve/findImproveAdviceview.f?improveId=12b77491-2638-41a7-bee9-ef38d7b13e7c',
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
	<div id=FHD.rectify.rectifyPlanForm${param._dc}></div>
</body>
</html>