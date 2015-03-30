<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
/*定义容器类  */
Ext.define('FHD_icm_assess_assessPlanPanelMagager',{
					extend : 'Ext.panel.Panel',
					layout : 'border',
					defaults : {
						collapsible : true,
						split : true,
						animFloat : false,
						useSplitTips : true,
						collapseMode : 'mini'
					},
					height : FHD.getCenterPanelHeight(),
					//width : FHD.getCenterPanelWidth(),
					autoWidth:true,
					showIndex:1,
					bbar:new Array(),
					renderTo : 'FHD.icm.assess.assessPlanPanelManager${param._dc}',
					items : new Array(),
					leftContainer:null,
					centerContainer: null,//右侧面板
					initComponent : function() {
					    var me = this;
					    var arr = '<img src="'+__ctxPath+'/images/resultset_next.png">';
					    me.tbar=[
			              {
		                    text: FHD.locale.get('fhd.icm.assess.assessPlanInfo'),//评价信息
		                    iconCls: 'icon-001',
		                    id: 'assessPlanInfoId${param._dc}',
		                    handler: function () {
		                    //this.toggle(true);
		                    //me.showIndex=1;
		                    }
		                  },arr,
		                  {
		                    text: FHD.locale.get('fhd.icm.assess.assessPlanRelaProcess'),//流程范围
		                    iconCls: 'icon-002',
		                    id: 'assessPlanRelaProcessId${param._dc}',
		                    handler: function () {
		                    	//this.toggle(true);
		                    	//me.showIndex=2;
		                    }
		                  },arr,
		                  {
			                    text: '范围明细',//流程范围明细
			                    iconCls: 'icon-003',
			                    id: 'assessPlanRelaProcessIdDetail${param._dc}',
			                    handler: function () {
			                    	//this.toggle(true);
			                    	//me.showIndex=3;
			                    }
			               }
		                  ,
		                  '->',{//返回到列表
                              text: FHD.locale.get("fhd.kpi.kpi.form.back"),
                              iconCls: 'icon-arrow-redo',
                              handler: function () {
                         		fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
                         		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanList.jsp');
                         		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);}
                          },
                          {//上一步
                              text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),
                              iconCls: 'icon-control-rewind-blue',
                              disabled:true,
                              id:'assessPlanBackId${param._dc}',
                              handler: function () {
                                    //操作上一步的时候可以是流程维护页面和流程范围明细页面，需要判断当前打开的页面是那一个
                               	if(me.showIndex==2){//流程维护
                               		//调用编辑页面的上一步方法
                               		fhd_icm_assess_assessPlanListEdit.upSetp(true);
                               		me.showIndex=1;
                               		me.initBtnStatus(true,false,false);
                               		Ext.getCmp('assessPlanBackId${param._dc}').setDisabled(true);
                               		Ext.getCmp('bassessPlanBackId${param._dc}').setDisabled(true);
                               		Ext.getCmp('assessPlanSaveId${param._dc}').setDisabled(false);//保存按钮不可用
                               	}else if(me.showIndex==3){//流程范围明细
                               		fhd_icm_assess_assessPlanShow.upStep();
                               		me.showIndex=2;
                               		me.initBtnStatus(false,true,false);
                               		me.initBBtnStatus(false,true,false);
                               	    //调用维护流程明细页面的上一步方法
                               		Ext.getCmp('assessPlanSubmitId${param._dc}').setDisabled(true);
                             		Ext.getCmp('bassessPlanSubmitId${param._dc}').setDisabled(true);
                               	}
                               	Ext.getCmp('assessPlanLastId${param._dc}').setDisabled(false);
                               	Ext.getCmp('bassessPlanLastId${param._dc}').setDisabled(false);
                              }
                          }
                          ,
                          {//下一步
                              text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
                              iconCls: 'icon-control-fastforward-blue',
                              id:'assessPlanLastId${param._dc}',
                              handler: function () {
                             	//操作下一步的时候可以是，编辑页面和流程范围页面，需要判断当前打开的页面是那一个
                             	if(me.showIndex==1){//编辑页面
                             		//调用编辑页面的保存方法
                             	   fhd_icm_assess_assessPlanEdit.nextSetp(true)
                           		   if(fhd_icm_assess_assessPlanEdit.fhd_icm_assess_assessPlanEditSubMitFlag=='yes'){
                           			me.showIndex=2;
                             		me.initBtnStatus(false,true,false);//设置导航按钮状态
                             		me.initBBtnStatus(false,true,false);//设置导航按钮状态
                             		Ext.getCmp('assessPlanSaveId${param._dc}').setDisabled(true);//保存按钮不可用
                             		Ext.getCmp('bassessPlanSaveId${param._dc}').setDisabled(true);//保存按钮不可用
                           		   }
                             	}else if(me.showIndex==2){//打开的是流程范围页面
                             		//调用维护流程范围的页面的保存方法
                             		fhd_icm_assess_assessPlanListEdit.nextStep();
                             		me.showIndex=3;
                             		me.initBtnStatus(false,false,true);//设置导航按钮状态
                             		me.initBBtnStatus(false,false,true);//设置导航按钮状态
                             		Ext.getCmp('assessPlanSubmitId${param._dc}').setDisabled(false);
                             		Ext.getCmp('bassessPlanSubmitId${param._dc}').setDisabled(false);
                             		Ext.getCmp('assessPlanLastId${param._dc}').setDisabled(true);
                             		Ext.getCmp('bassessPlanLastId${param._dc}').setDisabled(true);
                             	}
                             	Ext.getCmp('assessPlanBackId${param._dc}').setDisabled(false);
                             	Ext.getCmp('bassessPlanBackId${param._dc}').setDisabled(false);
                              }
                          },
                          {//保存，操作后 执行返回功能 
                          	   text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),
                               iconCls: 'icon-control-stop-blue',
                               id:'assessPlanSaveId${param._dc}',
                               handler: function () {
                            	   fhd_icm_assess_assessPlanEdit.nextSetp(false);
                                 } 
                          },
                          {//提交，操作后 执行返回功能 
                         	   text: FHD.locale.get("fhd.common.submit"),
                              iconCls: 'icon-control-eject-blue',
                              id:'assessPlanSubmitId${param._dc}',
                              handler: function () {
                           	   if(me.showIndex==3){
                           		   fhd_icm_assess_assessPlanShow.subMit();
                           	   }
                                } 
                         }
                          ];
					
					    
					me.bbar=[
				              {
				                    text: FHD.locale.get('fhd.icm.assess.assessPlanInfo'),//评价信息
				                    iconCls: 'icon-001',
				                    id: 'bassessPlanInfoId${param._dc}',
				                    handler: function () {
				                   // this.toggle(true);
				                    //me.showIndex=1;
				                    }
				                  },arr,
				                  {
				                    text: FHD.locale.get('fhd.icm.assess.assessPlanRelaProcess'),//流程范围
				                    iconCls: 'icon-002',
				                    id: 'bassessPlanRelaProcessId${param._dc}',
				                    handler: function () {
				                    	//this.toggle(true);
				                    	//me.showIndex=2;
				                    }
				                  },arr,
				                  {
					                    text: '范围明细',//流程范围明细
					                    iconCls: 'icon-003',
					                    id: 'bassessPlanRelaProcessIdDetail${param._dc}',
					                    handler: function () {
					                    	//this.toggle(true);
					                    	//me.showIndex=3;
					                    }
					               }
				                  ,
				                  '->',{//返回到列表
		                              text: FHD.locale.get("fhd.kpi.kpi.form.back"),
		                              iconCls: 'icon-arrow-redo',
		                              handler: function () {
		                         		fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
		                         		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanList.jsp');
		                         		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);}
		                          },
		                          {//上一步
		                              text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),
		                              iconCls: 'icon-control-rewind-blue',
		                              disabled:true,
		                              id:'bassessPlanBackId${param._dc}',
		                              handler: function () {
		                                    //操作上一步的时候可以是流程维护页面和流程范围明细页面，需要判断当前打开的页面是那一个
		                               	if(me.showIndex==2){//流程维护
		                               		//调用编辑页面的上一步方法
		                               		fhd_icm_assess_assessPlanListEdit.upSetp(true);
		                               		me.showIndex=1;
		                               		this.setDisabled(true);
		                               		me.initBtnStatus(true,false,false);
		                               		me.initBBtnStatus(true,false,false);
		                               		Ext.getCmp('bassessPlanBackId${param._dc}').setDisabled(true);
		                               		Ext.getCmp('assessPlanBackId${param._dc}').setDisabled(true);
		                               		Ext.getCmp('bassessPlanSaveId${param._dc}').setDisabled(false);
		                               		Ext.getCmp('assessPlanSaveId${param._dc}').setDisabled(false);
		                               	}else if(me.showIndex==3){//流程范围明细
		                               		//调用维护流程明细页面的上一步方法
		                               		fhd_icm_assess_assessPlanShow.upStep();
		                               		me.showIndex=2;
		                               		me.initBBtnStatus(false,true,false);
		                               		me.initBtnStatus(false,true,false);
		                               		Ext.getCmp('assessPlanSubmitId${param._dc}').setDisabled(true);
		                             		Ext.getCmp('bassessPlanSubmitId${param._dc}').setDisabled(true);
		                               	}
		                               	Ext.getCmp('bassessPlanLastId${param._dc}').setDisabled(false);
		                               	Ext.getCmp('assessPlanLastId${param._dc}').setDisabled(false);
		                              }
		                          }
		                          ,
		                          {//下一步
		                              text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),
		                              iconCls: 'icon-control-fastforward-blue',
		                              id:'bassessPlanLastId${param._dc}',
		                              handler: function () {
		                             	//操作下一步的时候可以是，编辑页面和流程范围页面，需要判断当前打开的页面是那一个
		                             	if(me.showIndex==1){//编辑页面
		                             		//调用编辑页面的保存方法
		                             		   fhd_icm_assess_assessPlanEdit.nextSetp(true)
		                             		   if(fhd_icm_assess_assessPlanEdit.fhd_icm_assess_assessPlanEditSubMitFlag=='yes'){
		                             			    me.showIndex=2;
				                             		me.initBtnStatus(false,true,false);//设置导航按钮状态
				                             		me.initBBtnStatus(false,true,false);//设置导航按钮状态
				                             		Ext.getCmp('bassessPlanSaveId${param._dc}').setDisabled(true);//保存按钮不可用
				                             		Ext.getCmp('assessPlanSaveId${param._dc}').setDisabled(true);//保存按钮不可用
		                             		   }
		                             	}else if(me.showIndex==2){//打开的是流程范围页面
		                             		//调用维护流程范围的页面的保存方法
		                             		fhd_icm_assess_assessPlanListEdit.nextStep();
		                             		me.showIndex=3;
		                             		me.initBBtnStatus(false,false,true);//设置导航按钮状态
		                             		me.initBtnStatus(false,false,true);//设置导航按钮状态
		                             		Ext.getCmp('assessPlanSubmitId${param._dc}').setDisabled(false);
		                             		Ext.getCmp('bassessPlanSubmitId${param._dc}').setDisabled(false);
		                             		Ext.getCmp('bassessPlanLastId${param._dc}').setDisabled(true);
		                             		Ext.getCmp('assessPlanLastId${param._dc}').setDisabled(true);
		                             	}
		                             	Ext.getCmp('assessPlanBackId${param._dc}').setDisabled(false);
		                             	Ext.getCmp('bassessPlanBackId${param._dc}').setDisabled(false);
		                              }
		                          },
		                          {//保存，操作后 执行返回功能 
		                          	   text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),
		                               iconCls: 'icon-control-stop-blue',
		                               id:'bassessPlanSaveId${param._dc}',
		                               handler: function () {
		                            	   fhd_icm_assess_assessPlanEdit.nextSetp(false);
		                                 } 
		                          },{//提交，操作后 执行返回功能 
		                          	   text: FHD.locale.get("fhd.common.submit"),
		                               iconCls: 'icon-control-eject-blue',
		                               id:'bassessPlanSubmitId${param._dc}',
		                               handler: function () {
		                            	   if(me.showIndex==3){
		                            		   fhd_icm_assess_assessPlanShow.subMit();
		                            	   }
		                                 } 
		                          }
					         ];
				     me.setCenterContainer('pages/icm/assess/assessPlanEdit.jsp?initForm=${param.initForm}&assessPlanId=${param.assessPlanId}');
					 me.items = [me.centerContainer];
					 me.callParent(arguments);
					 me.initBtnStatus(true,false,false);
					 me.initBBtnStatus(true,false,false);
					 Ext.getCmp('assessPlanSubmitId${param._dc}').setDisabled(true);
					 Ext.getCmp('bassessPlanSubmitId${param._dc}').setDisabled(true);
					},
					setCenterContainer:function(url){
						var me=this;
						me.centerContainer=Ext.create('Ext.container.Container',{
							region:'center',
							autoLoad:{
								url:url,
								scripts:true
							}
						});
					},
					initBtnStatus:function(b1,b2,b3){
						Ext.getCmp('assessPlanInfoId${param._dc}').toggle(b1);
						Ext.getCmp('assessPlanRelaProcessId${param._dc}').toggle(b2);
						Ext.getCmp('assessPlanRelaProcessIdDetail${param._dc}').toggle(b3);
					},
					initBBtnStatus:function(b1,b2,b3){
						Ext.getCmp('bassessPlanInfoId${param._dc}').toggle(b1);
						Ext.getCmp('bassessPlanRelaProcessId${param._dc}').toggle(b2);
						Ext.getCmp('bassessPlanRelaProcessIdDetail${param._dc}').toggle(b3);
					},
					setBtnSelected:function(btnId){
					
					}
				});
	var fhd_icm_assess_assessPlanPanelManager=null;
    Ext.onReady(function() {
    	fhd_icm_assess_assessPlanPanelManager = Ext.create('FHD_icm_assess_assessPlanPanelMagager');
	    FHD.componentResize(fhd_icm_assess_assessPlanPanelManager,0,0);
});
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessPlanPanelManager${param._dc}'></div>
</body>
</html>