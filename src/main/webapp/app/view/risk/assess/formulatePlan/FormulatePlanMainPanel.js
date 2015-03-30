/**
 * 
 * 计划制定入口
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulatePlanMainPanel',{
 	extend: 'Ext.form.Panel',
 	alias : 'widget.formulateplanmainpanel',
 	border:false,
 	requires: [
 		'FHD.view.risk.assess.formulatePlan.FormulateGrid',
 		'FHD.view.risk.assess.formulatePlan.FormulatePlanEdit',
 		'FHD.view.risk.assess.formulatePlan.FormulatePlanRang',
 		'FHD.view.risk.assess.formulatePlan.FormulateSubmitMainPanel'
	],
 	//提交方法
	submitWindow:function(me,deptIds){
		me.formulateSubmitMainPanel = Ext.widget('formulatesubmitmainPanel');
		var formulatePlanSubWindowGrid = me.formulateSubmitMainPanel.formulatesubmitgrid;
		formulatePlanSubWindowGrid.store.load({params:{deptIds:deptIds}});
		var formulateApproverEdit = me.formulateSubmitMainPanel.formulateapproveredit;
		me.subWin = Ext.create('FHD.ux.Window', {
			title:'提交',
   		 	height: 300,
    		width: 600,
   			layout: 'fit',
   			buttonAlign: 'center',
    		items: [me.formulateSubmitMainPanel],
   			fbar: [
   					{ xtype: 'button', text: '确定', handler:function(){me.btnConfirm(formulatePlanSubWindowGrid,formulateApproverEdit);}},
   					{ xtype: 'button', text: '取消', handler:function(){me.subWin.hide();}}
				  ]
		}).show();
	},
	//窗口确认按钮事件
	btnConfirm: function(grid, form){
		var me = this;
		var empIds = [];
		var approverId = form.items.items[0].value;
		var items = grid.store.data.items;
		for(var k in items){
				if(!items[k].data.empId){
					 empIds = [];
					 break;
				}else{
					empIds.push(grid.store.data.items[k].data.empId);
				}
		}
	
		if(!empIds.length){
			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '承办人不能为空！');
			return ;
		}
		if(!approverId){
			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '审批人不能为空！');
			return ;
		}
		FHD.ajax({//ajax调用
    				url : __ctxPath + '/access/formulateplan/submitassessriskplan.f',
    				params : {
    					empIds: empIds.join(','),
    					approverId: approverId,
    					planId: me.planId
    				},
    				callback : function(data){
    					
    				}
    			});
    	me.subWin.hide();
    	form.approver.clearValues();
	},
   
    initComponent: function () {
    	var me = this;
    	me.id = 'formulatePlanMainPanel';
    	var planEdit_saveUrl = 'access/formulateplan/saveriskplan.f';//保存计划制定表单url
    	 me.p1 = Ext.create('FHD.view.risk.assess.formulatePlan.FormulatePlanEdit',{
    	 		businessId:me.businessId,
    		 	navigatorTitle:'计划制定',
    		 	border: false,
    		 	last:function(){//计划制定保存按钮事件
    		 		var formulateGrid = Ext.getCmp('formulateGridId');
    		 		var form = me.p1.getForm();
    		 		var rangeType;
    		 		if(form.isValid()){
    		 			if(typeof(formulateGrid.busId) == 'undefined'){
    		 				FHD.submit({//添加评估计划
	    						form:form,
	    						url:planEdit_saveUrl,
	    						callback:function(data){
	    							formulateGrid.store.load();
	    							me.planId = data.data.planId;
	    							//me.rangeType = data.data.rangeType;
	    							//为风险列表url动态赋值，重新加载数据
				    		 		var formulatePlanPreviewGrid = me.down('formulateplanpreviewgrid');
				    		 		formulatePlanPreviewGrid.store.proxy.url = 'access/formulateplan/queryriskscoreobjspage.f';
				    		 		formulatePlanPreviewGrid.store.proxy.extraParams.planId = me.planId;
				    		 		formulatePlanPreviewGrid.store.load();
	    						}
	    					});
    		 			}else{//修改
    		 				FHD.submit({
	    						form:form,
	    						url:planEdit_saveUrl + '?id=' + formulateGrid.busId,
	    						callback:function(data){
	    							formulateGrid.store.load();
	    							me.planId = data.data.planId;
	    							//为风险列表url动态赋值，重新加载数据
				    		 		var formulatePlanPreviewGrid = me.down('formulateplanpreviewgrid');
				    		 		formulatePlanPreviewGrid.store.proxy.url = 'access/formulateplan/queryriskscoreobjspage.f';
				    		 		formulatePlanPreviewGrid.store.proxy.extraParams.planId = me.planId;
				    		 		formulatePlanPreviewGrid.store.load();
	    							//me.rangeType = data.data.rangeType;
	    							//me.p2.leftgrid.store.load({params:{planId:me.planId,rangeType:me.rangeType}});
	    						}
	    					});
    		 			}
    		 			var formulatePlanEdit = Ext.getCmp('formulatePlanEdit');
    		 			var formulatePlanRang = Ext.getCmp('formulatePlanRangId');
    		 			//范围选择--计划名称赋值
    		 			formulatePlanRang.items.items[0].items.items[0].setValue(formulatePlanEdit.items.items[0].items.items[0].value);
    		 			//范围选择--起止日期赋值
    		 				//日期控件格式转换
    		 			var beginDate = Ext.util.Format.date(formulatePlanEdit.items.items[0].items.items[5].items.items[1].getValue(), 'Y-m-d');
    		 			var endDate = Ext.util.Format.date(formulatePlanEdit.items.items[0].items.items[5].items.items[3].getValue(), 'Y-m-d');
    		 			formulatePlanRang.items.items[0].items.items[1].setValue(beginDate + ' 至 ' + endDate);
    		 			//范围选择--联系人赋值(组件)
    		 			if(formulatePlanEdit.items.items[0].items.items[3].valueStore.data.items[0]){
    		 				formulatePlanRang.items.items[0].items.items[2].setValue(formulatePlanEdit.items.items[0].items.items[3].valueStore.data.items[0].data.empname);
    		 			}
    		 			//范围选择--负责人赋值
    		 			if(formulatePlanEdit.items.items[0].items.items[4].valueStore.data.items[0]){
    		 				formulatePlanRang.items.items[0].items.items[3].setValue(formulatePlanEdit.items.items[0].items.items[4].valueStore.data.items[0].data.empname);
    		 			}
    		 		}else{
    		 			return false;
    		 		}
    		 	}
    		 });
    		 me.p2 = Ext.create('FHD.view.risk.assess.formulatePlan.FormulatePlanRang',{
    		 	navigatorTitle:'范围选择',
    		 	border: true,
    		 	last:function(){
    		 		var prt = me.up('formulatePlanCard');
    		 		prt.formulateGrid.store.load();
    		 		prt.showFormulateGrid();
    		 	}
    		 });
    		me.btnSubmit = Ext.create('Ext.button.Button',{
 	            text: '提交',//提交按钮
 	            disabled:true,
 	            iconCls: 'icon-operator-submit',
 	            handler: function () {
 	            	FHD.ajax({//查询所有打分部门id
	    						url:__ctxPath + '/access/formulateplan/findscoredeptids.f',
	    						params : {
    								planId:me.planId
    							},
	    						callback:function(data){
	    							me.deptIds = data.deptIds;
	    							me.submitWindow(me,me.deptIds);
	    						}
	    					});
 	            }
 	        });
    		 me.basicPanel = Ext.create('FHD.ux.layout.StepNavigator',{
 			    hiddenTop:true,	//是否隐藏头部
			    hiddenBottom:false, //是否隐藏底部
			    hiddenUndo:false,	//是否有返回按钮
    			btns:[me.btnSubmit],
    		 	items:[me.p1,me.p2],
    		 	undo : function(){
    		 		var prt = me.up('formulatePlanCard');
    		 		prt.formulateGrid.store.load();
    		 		prt.showFormulateGrid();
    		 	}
    		 });
    	me.callParent(arguments);
    	me.add(me.basicPanel);
    	
    	me.on('resize',function(p){
    			me.p1.setHeight(FHD.getCenterPanelHeight()-30);
    			me.p2.setHeight(FHD.getCenterPanelHeight()-30);
    	});
    }

});