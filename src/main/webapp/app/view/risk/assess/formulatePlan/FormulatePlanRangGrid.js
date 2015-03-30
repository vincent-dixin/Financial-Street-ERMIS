/**
 * @author denggy
 * @describe 评估范围列表
 */
 Ext.define('FHD.view.risk.assess.formulatePlan.FormulatePlanRangGrid',{
    	extend:'FHD.ux.layout.GridPanel',
    	alias: 'widget.setmeasuregrid',
    	requires: [
 			'FHD.view.risk.assess.formulatePlan.FormulatePlanMainPanel'
		],
    	
    	url: "access/formulateplan/queryscorerangespage.f",//查询
    	region:'center',
    	objectType:{},
    	pagable : false,
    	searchable:true,
    	flex:1,
    	layout: 'fit',
        margin: '2 0 0 0',
        setGridValues:function(store){
        	var me = this;
        	if(store){
	        	Ext.define('RiskAssessRangModel', {//定义model
				    extend: 'Ext.data.Model',
				   	fields: [{name: 'deptid', type: 'string'},
					        {name: 'deptname', type:'string'},
					        {name: 'riskSum', type: 'string'},
					        {name: 'contractors', type:'string'}
					        ]
				});
        		store.each(function(r){
//        			console.dir(r);
        			//根据部门ID 或则承办人 信息
                    var valueRecord = Ext.create('RiskAssessRangModel',{
								deptid: r.data.deptid,
								deptname: r.data.deptname,
								riskSum:20
							});
        			
	        		me.store.insert(0,valueRecord);
        		})
        	}
        },
    	addGrid:function(){
    			var me = this;
		 		me.win = Ext.create('FHD.ux.org.DeptSelectorWindow',{//部门选择组件
						    		checkable:true,
						    		subCompany:false,
						    		rootVisible: false,
						           	multiSelect: true,   	
						        	checkModel:false,
						        	companyOnly: false,

						onSubmit:function(win){//组件确定按钮事件
							var formulatePlanMainPanel = Ext.getCmp('formulatePlanMainPanel');
							var planId = formulatePlanMainPanel.planId;
							var rangeType = formulatePlanMainPanel.rangeType;
							var data = win.selectedgrid.store.data;
							var queryRange_DeptUrl = 'access/formulateplan/savescoreranges.f';//按部门选择列表查询URL
							var ids = [];
							for(var i=0;i<data.length;i++){
    							ids.push(data.items[i].data.id);
    						}
    						FHD.ajax({//ajax调用
    							url : queryRange_DeptUrl,
    							params : {
    								ids:ids.join(','),
    								planId:planId,
    								rangeType:rangeType
    							},
    							callback : function(data){
    								me.store.load({params:{planId:planId,rangeType:rangeType}});
    							}
    						});
						}
					}).show();
    	},
    	addGrid2:function(){
    		var me = this;
		 		me.win = Ext.create('FHD.ux.kpi.KpiSelectorWindow',{
						    		
						onSubmit:function(win){
							me.store.removeAll();
							me.setGridValues(win.selectedgrid.store);
						}
					}).show();
    	},
		delGrid:function(){//删除方法
			var me = this;
			var selection = me.getSelectionModel().getSelection();
			Ext.MessageBox.show({
				title : FHD.locale.get('fhd.common.delete'),
				width : 260,
				msg : FHD.locale.get('fhd.common.makeSureDelete'),
				buttons : Ext.MessageBox.YESNO,
				icon : Ext.MessageBox.QUESTION,
				fn : function(btn) {
					if (btn == 'yes') {
						var ids = [];
						for(var i=0;i<selection.length;i++){
							var delId = selection[i].get('id');
							if(delId){
									ids.push(delId)
							}else {
									return Ext.Msg.alert('提示','没有保存的记录无法删除!')
							       }
						}
						FHD.ajax({
							url : '',//删除  
							params : {
								ids : ids.join(',')
							},callback: function (data) {
                            if (data) {
                            	me.reloadData();
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                            }else{
                            	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                            }
                        }
						});
					}
				} 
			});
		},
		setstatus: function(){
	    	var me = this;
	        var length = me.getSelectionModel().getSelection().length;
	    },
    	initComponent:function(){//初始化
    		var me=this;
	    	me.btns = [{
		    				btype:'custom',
		    				handler:me.addGrid,
		    				text:'按部门选择',
		    				iconCls:'icon-add'
	    				},
	    				/*{
	    				   btype:'custom',
		    			   handler:me.addGrid2,
		    			   text:'按指标选择',
		    			   iconCls:'icon-add'
	    			   },*/
	    			   {
		    			   btype:'delete',
		    			   handler:me.delGrid
	    			   }
	    			   
	    			];
	    	
	    	me.cols=[
			 {
				header : "部门名称",
				dataIndex : 'deptName',
				sortable : true,
				flex : 1,
				editor:true
			},
			{
				header:'风险数量',
				dataIndex:'riskSum',
				flex : 1
			}
			];
	    	me.on('resize',function(p){
    			me.setHeight(FHD.getCenterPanelHeight()-185);
    		});
	        me.on('selectionchange', function () {
	            me.setstatus()
	        });
	        me.on('edit', function () {
	            me.setstatus()
	        });
	    	me.callParent(arguments);
	    },
	    reloadData :function(){//刷新grid
			var me = this;
		},
		relative:function(){
		}
});