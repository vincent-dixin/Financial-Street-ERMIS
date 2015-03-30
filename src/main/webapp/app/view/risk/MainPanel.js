/**
 * 风险主面板
 * @author zhengjunxiang
 */
Ext.define('FHD.view.risk.MainPanel', {
    extend: 'FHD.ux.layout.treeTabFace.TreeTabFace',
    alias: 'widget.riskMainPanel',
    
    nodeId:'',
    nodeName:'',
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        
        var orgTreeContainer =  Ext.create('Ext.container.Container',{
        	treeTitle:'组织',
        	treeIconCls : 'icon-ibm-new-group-view',
        	layout:'fit',
        	onClick:function(){
        		if(!me.orgTree){
                    me.orgTree = Ext.create('FHD.ux.org.DeptTree',{
                    	subCompany: true,
                    	companyOnly: false,
                    	checkable:false,
                    	border:false,
                    	face:me,
                    	rootVisible: true
            	    });
               		this.add(me.orgTree);
            		this.doLayout();
        		} 
        	}
        });

        var strategyTreeContainer =  Ext.create('Ext.container.Container',{
        	treeTitle:'目标',
        	treeIconCls : 'icon-strategy',
        	layout:'fit',
        	onClick:function(){
        		if(!me.strategyTree){
        	        me.strategyTree = Ext.create('FHD.view.risk.strategy.StrategyTreePanel',{
        	        	border:false,
        	        	face:me
        	        });
               		this.add(me.strategyTree);
            		this.doLayout();
        		} 
        	}
        });

        var processTreeContainer =  Ext.create('Ext.container.Container',{
        	treeTitle:'流程',
        	treeIconCls : 'icon-ibm-icon-metrictypes',
        	layout:'fit',
        	onClick:function(){
        		if(!me.processTree){
        	        me.processTree = Ext.create('FHD.ux.process.processTree', {
        	        	border:false,
        	        	face:me,
        				extraParams : {canChecked : false}
        			});
               		this.add(me.processTree);
            		this.doLayout();
        		} 
        	}
        });

        me.riskTree = Ext.create('FHD.view.risk.risk.RiskTreePanel',{//risktreepanel
        	border:false,
        	treeTitle:'风险',
        	treeIconCls : 'icon-ibm-icon-scorecards',
        	face:me,
        	rbs:true        	
        }); 
        
        //信息查看
        me.riskBasicFormView =  Ext.create('FHD.view.risk.risk.RiskBasicFormView',{
        	face:me,
        	border:false,
        	autoHeight : true,
        	onClick:function(){
        		//根据左侧选中节点，初始化数据
        		if(me.nodeId != ''){
        			FHD.ajax({
               			async:false,
               			params: {
                            riskId: me.nodeId
                        },
                        url: __ctxPath + '/risk/findRiskById.f',
                        callback: function (ret) {
                        	//显示目标详细信息
                        	me.riskBasicFormView.reLoadData(ret);
                        }
                    });
        		}
        	}
        });
        
        //风险事件列表
        me.riskEventGridContainer =  Ext.create('Ext.container.Container',{
        	title:'风险列表',
        	onClick:function(){
        		if(!me.riskEventGrid){
        			me.riskEventGrid =  Ext.create('FHD.view.risk.risk.RiskEventGrid',{
                    	face:me,
                    	border:false,
                    	height:FHD.getCenterPanelHeight()-47
                    });
            		this.add(me.riskEventGrid);
            		this.doLayout();
        		}
        		//根据左侧选中节点，初始化数据
        		if(me.nodeId != ''){
        			me.riskEventGrid.reLoadData(me.nodeId,me.nodeName);
        		}
        	}
        });

        //历史信息
        me.riskHistoryGridContainer =  Ext.create('Ext.container.Container',{
        	title:'历史记录',
        	onClick:function(){
        		if(!me.riskHistoryGrid){
        			me.riskHistoryGrid =  Ext.create('FHD.view.risk.risk.RiskHistoryGrid',{
        	        	face:me,
        	        	border:false,
        	        	height:FHD.getCenterPanelHeight()-47
        	        });
            		this.add(me.riskHistoryGrid);
            		this.doLayout();
        		}
        		//根据左侧选中节点，初始化数据
        		if(me.nodeId != ''){
        			me.riskHistoryGrid.reLoadData(me.nodeId);
        		}
        	}
        });
        
        //步骤导航
        me.stepPanelContainer =  Ext.create('Ext.container.Container',{
        	title:'基本信息',
        	onClick:function(){
        		if(!me.stepPanel){
        			/**
        	         * 基本信息
        	         */
        	        me.riskEditBasicFormView =  Ext.create('FHD.view.risk.risk.RiskEditBasicFormView',{
        	        	navigatorTitle:'基本信息',
        	        	face:me,
        	        	border:false,
        	        	height:FHD.getCenterPanelHeight()-47
//        	        	onClick:function(){
//        	        		alert('tab');
//        	        	}
//        	        	last:function(){
//        	        		var btn = Ext.create("Ext.button.Button",{
//        	        			text:'新添按钮'
//        	        		});
//        	        		me.stepPanel.getBottomToolbar().add(btn);
//        	        	}
        	        });
        	        /**
        	         *  add by 宋佳 
        	         *  给基本信息增加上一步下一步内容
        	         */
        	        me.riskstandardmanage = Ext.create('FHD.view.risk.riskedit.RiskStandardManage',{
        	        	navigatorTitle:'风险上报标准'
//        	        	back:function(){
//        	        		alert('back');
//        	        	},
        	        });
        	        /**
        	         * add by 宋佳
        	         * 风险管控措施 
        	         */
        	        me.riskmeasuremanage = Ext.create('FHD.view.risk.measureedit.RiskMeasureManage',{navigatorTitle:'风险管控措施'});
        	        /**
        	         * 应对预案维护 response plan
        	         */
        			me.riskresponseplanmanage =Ext.create('FHD.view.risk.responseplan.RiskResponsePlanManage',{navigatorTitle:'应对预案维护'});
        			/**
        			 * 有效性标准 
        			*/
        	        /**
        	         * 设定检查点
        	         */
        			me.checkpointmanage = Ext.create('FHD.view.risk.checkpoint.CheckPointManage',{navigatorTitle:'设定检查点'});
        			/**
        			 * 检查点检查
        			 */
        			me.checkpointcheckmanage = Ext.create('FHD.view.risk.checkpoint.CheckPointCheckManage',{navigatorTitle:'检查点检查'});
        	        me.effectivestandardmanage = Ext.create('FHD.view.risk.effective.EffectiveStandardManage',{navigatorTitle:'风险管理有效性标准'});
        			me.stepPanel = Ext.create('FHD.ux.layout.StepNavigator',{
        				height : FHD.getCenterPanelHeight()-47,
        				items : [me.riskEditBasicFormView,me.riskstandardmanage,me.riskmeasuremanage,me.riskresponseplanmanage,me.effectivestandardmanage,me.checkpointmanage,me.checkpointcheckmanage],
        				undo : function(){
        					//tab切换到风险列表标签
        					var tab = me.tabpanel;
        					tab.setActiveTab(1);
        	        	}
        	        });
            		this.add(me.stepPanel);
            		this.doLayout();
        		}
        		//根据左侧选中节点，初始化数据
        		if(me.nodeId != ''){
        			FHD.ajax({
               			async:false,
               			params: {
                            riskId: me.nodeId
                        },
                        url: __ctxPath + '/risk/findRiskEditInfoById.f',
                        callback: function (ret) {
                        	//显示目标详细信息
                        	var riskEditFormView = me.riskEditBasicFormView;	//找到步骤导航中的formpanel
                        	riskEditFormView.reLoadData(ret);
                        	riskEditFormView.isEdit = true;
                        	riskEditFormView.editId = me.nodeId;
                        }
                    });
        		}
        	}
        });
	    
        var tabs = [me.riskBasicFormView,me.riskEventGridContainer,me.riskHistoryGridContainer,me.stepPanelContainer];//riskBasicFormView,riskEventGrid,riskHistoryGrid,stepPanel

        var accordionTree = Ext.create("FHD.ux.layout.AccordionTree",{
        	title: '风险',
            iconCls: 'icon-ibm-icon-scorecards',
            width:250,
        	treeArr:[me.riskTree,orgTreeContainer,strategyTreeContainer,processTreeContainer]
        });
        Ext.apply(me,{
        	tree:accordionTree,
        	tabs:tabs
        });
        
        me.callParent(arguments);
    }
});