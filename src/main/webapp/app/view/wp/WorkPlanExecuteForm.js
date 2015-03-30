Ext.define('FHD.view.wp.WorkPlanExecuteForm', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workplanexecuteform',
    
    border : false,
    // 布局
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    
    
    initComponent: function() {
        var me = this;
        
        me.flowImage = Ext.widget('image',{
        	src : __ctxPath + '/images/wp/psteps33.jpg',
        	width:450
        });
        
        me.formPanel = Ext.widget('form',{
        	autoScroll:true,
        	flex:1,
        	border:false,
        	bodyPadding: '0 3 3 3',
			bbar:['->',{
				iconCls : 'icon-control-play-blue',
				text:'保存',
				handler: me.onSave
			},'-',{
				iconCls : 'icon-control-fastforward-blue',
				text:'提交',
				handler: me.onSubmit
			}]
        });
        
        
        
        
        me.form = me.formPanel.getForm();
        
        Ext.applyIf(me, {
            items: [{
            	xtype:'container',
            	height: 50,
            	style:'border-bottom: 1px  #99bce8 solid !important;',
            	layout:{
            		align: 'stretch',
        			type: 'hbox'
            	},
            	items:[{
            		xtype:'image',
            		src : __ctxPath + '/images/wp/zuo.jpg',
            		flex:1
            	},me.flowImage,{
            		xtype:'image',
            		src : __ctxPath + '/images/wp/zuo.jpg',
            		flex:1
            	}]
            },me.formPanel]
        });

        me.callParent(arguments);
        
        var basicFieldSet = Ext.widget('fieldset', {
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105,
            	labelAlign: 'left',
                columnWidth: .5
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.common.baseInfo'),
            items: [me.hiddenId]
        });
        
        me.formPanel.add(basicFieldSet);
        
        var companyLabel = Ext.widget('displayfield',{
        	fieldLabel : '所属公司',
        	name : 'company'
        });
        
        basicFieldSet.add(companyLabel);
        
        var codeLabel = Ext.widget('displayfield',{
        	fieldLabel : '计划编号',
        	name : 'code'
        });
        
        basicFieldSet.add(codeLabel);
        
        var nameLabel = Ext.widget('displayfield',{
        	fieldLabel : '计划名称',
        	name : 'name'
        });
        
        basicFieldSet.add(nameLabel);
        
        var startEndDateLabel = Ext.widget('displayfield',{
        	fieldLabel : '计划起止日期',
        	name : 'startEndDate'
        });
        
        basicFieldSet.add(startEndDateLabel);
        
        var responsilePersionLabel = Ext.widget('displayfield',{
        	fieldLabel : '责任人',
        	name : 'responsilePersion'
        });
        
        basicFieldSet.add(responsilePersionLabel);
        
        var targetLabel = Ext.widget('displayfield',{
        	fieldLabel : '计划目标',
        	name : 'target'
        });
        
        basicFieldSet.add(targetLabel);
        
        var contentLabel = Ext.widget('displayfield',{
        	fieldLabel : '计划内容',
        	name : 'content'
        });
        
        basicFieldSet.add(contentLabel);
        
        var orgLabel = Ext.widget('displayfield',{
        	fieldLabel : '实施单位',
        	name : 'orgs'
        });
        
        basicFieldSet.add(orgLabel);
        
        var assessRequirementLabel = Ext.widget('displayfield',{
        	fieldLabel : '考核要求',
        	name : 'assessRequirement'
        });
        
        basicFieldSet.add(assessRequirementLabel);
        
        var contributeTargetAmountLabel = Ext.widget('displayfield',{
        	fieldLabel : '贡献目标额',
        	name : 'contributeTargetAmount'
        });
        
        basicFieldSet.add(contributeTargetAmountLabel);
        
        
        var milestoneFieldSet = Ext.widget('fieldset', {
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
            title: '里程碑信息'
        });
        
        me.formPanel.add(milestoneFieldSet);
        
        var retaPlanCombo = Ext.widget('combo',{
			store: Ext.create('Ext.data.Store', {
			    fields: ['id', 'name'],
			    proxy: {
			        type: 'ajax',
			        url: __ctxPath + '/wp/findmilestionretaplan.f',
			        reader: {
			            type : 'json',
			            root : 'datas'
			        }
			    },
			    listeners:{
			    	beforeload : function(store){
			    		var selection = me.milestoneGrid.getSelectionModel().getSelection()[0];
			    		store.proxy.extraParams.milestoneType = selection.get('milestoneType');
			    	}
			    }
			}),
			typeAhead: true,
		    displayField: 'name',
		    valueField: 'id'
		});
        
        
        me.milestoneGrid = Ext.widget('fhdeditorgrid',{
			checked:false,
			searchable:false,
			pagable : false,
			height: 140,
			url: __ctxPath + '/wp/findmilestonelistforexecute.f',
			extraParams: {
				workPlanId : me.workPlanId
			},
        	cols:[{
        			dataIndex:'milestoneType',hidden : true
        		},{
        			header: "里程碑名称", width: 150, sortable: false, dataIndex: 'milestoneName'
		       	},{
	            	header: "里程碑描述", width: 490, sortable: false, dataIndex: 'milestoneDesc'
				},{
					header: "完成日期", width: 100, sortable: false, dataIndex: 'milestoneDate',
					renderer: function (value) {
	                    if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
	                    } else {
	                        return value;
	                    }
	                }
				},{
					header: "关联计划", width: 200, sortable: false, dataIndex: 'subPlan',
					/*editor: retaPlanCombo,
				    listeners: {
				        click:  function(){ 
				        	retaPlanCombo.store.load();				        	 
				        }
				    },*/
					renderer: function (value,metaData,record,rowIndex,colIndex,store,view) {
						var name = "";
						if(null != value){
							FHD.ajax({
								url : __ctxPath + '/icm/assess/findAssessPlan.f',
								async : false,
								params : {
									assessPlanId : value
								},
								callback:function(responseText){
									if(responseText){
										name = responseText.data.name;
									}
									
								}
							});
						}
						return '<font color="blue">'+name+'</font>';
						/*var comboStore = retaPlanCombo.store
	                    var index = comboStore.find('id',value);
						var r = comboStore.getAt(index);
						if (r == null) {
					        return value;
					    } else {
					        return r.data.name; // 获取record中的数据集中的display字段的值 
					    }*/
	                }
				},{
					menuDisabled: true,xtype: 'actioncolumn', width: 70, sortable: false, dataIndex: 'subPlanId',
					items:[{
	                    getClass: function(v, meta, rec) { 
	                    	if(rec.data.subPlan == null) {
	                    		this.items[0].tooltip = '添加计划';
                            	return 'icon-add';
	                    	}else {
	                    		this.items[0].tooltip = '已完成';
                            	return 'icon-tick';
	                    	}
                            
	                    },
	                    handler: function(grid, rowIndex, colIndex) {
	                    	if(grid.store.getAt(rowIndex).data.subPlan == null) {
	                    		me.addPlan(grid, rowIndex, colIndex);
	                    		//'' == grid.store.getAt(rowIndex).data.milestoneType
	                    		//FHD.alert('调用添加计划 -- 里程碑id:' + grid.store.getAt(rowIndex).data.id);
	                    		
	                    	}
	                    	
	                    }
	                },'-',{
	                    getClass: function(v, meta, rec) { 
	                    	this.items[2].tooltip = '关联计划';
	                    	console.log(this.items);
                            return 'icon-cog';
	                    },
	                    handler: function(grid, rowIndex, colIndex) {
	                    		var className ;
						    	var title ;
						    	if('assessment_plan'==grid.store.getAt(rowIndex).data.milestoneType){
						    		className = 'FHD.ux.icm.assess.AssessPlanSelectorWindow';
						    		title = '评价计划列表';
						    	}else{
						    		className = 'FHD.ux.icm.rectify.ImproveSelectorWindow';
						    		title = '整改计划列表';
						    	}
                    		var assessPlanSelectorWindow = Ext.create(className,{
                    			modal: true,
                    			multiSelect : false,
                    			onSubmit: function(win){
                    				var assessPlanIdArray = new Array();
					            	win.selectedGrid.store.each(function(r){
					            		assessPlanIdArray.push(r);
					            	});
					            	if(assessPlanIdArray.length > 0){
					            		var r =  assessPlanIdArray.pop();
					            		grid.store.getAt(rowIndex).data.subPlan = r.data.id;
					            		grid.refresh();
					            	}
                    			}
                    		});
                    		assessPlanSelectorWindow.show();
	                    	
	                    }
	                }]
				}]
        });
        
        milestoneFieldSet.add(me.milestoneGrid);
    },
    
    onSave : function() {
    	var me = this.up('workplanexecuteform');
    	var jsonArray = me.buildMilestoneStore();
    	FHD.ajax({
    		url : __ctxPath + '/wp/savemilestoneimprovement.f',
    		params : {
    			milestoneData : Ext.JSON.encode(jsonArray)
    		},
    		callback :function() {
    			Ext.getCmp(me.winId).close();
    		}
    		
    	});
    },
    
    onSubmit : function() {
    	var me = this.up('workplanexecuteform');
    	var jsonArray = me.buildMilestoneStore();
    	var rows = me.milestoneGrid.store.data.items;
    	var flag = false;
    	Ext.Array.each(rows,function(item){
    			if(item.data.subPlan==null){
    				flag = true;
    			}
    	});
    	if(flag){
    		return FHD.alert("里程碑中存在未关联的计划，无法提交！");
    	}
    	FHD.ajax({
    		url : __ctxPath + '/wp/savemilestoneimprovementsubmit.f',
    		params : {
    			milestoneData : Ext.JSON.encode(jsonArray),
    			processInstanceId : me.executionId
    		},
    		callback :function() {
    			Ext.getCmp(me.winId).close();
    		}
    		
    	});
    },
    
    
    buildMilestoneStore: function(){
    	var me = this;
 		var rows = me.milestoneGrid.store.data.items;
 		var jsonArray = new Array();
		Ext.Array.each(rows, function (item) {
		    jsonArray.push(item.data);
		});
    	return jsonArray;
    },
    
    reloadData: function() {
    	var me = this,
    		workPlanId = me.businessId;
    	
    	me.milestoneGrid.store.proxy.extraParams.workPlanId = workPlanId;	
    	me.milestoneGrid.store.load();
    	
	    me.form.waitMsgTarget = true;
    	me.form.load({
            waitMsg: '加载中...',
            url: __ctxPath + '/wp/findworkplanview.f',
            params: {
                workPlanId: workPlanId
            },
            // form加载数据成功后回调函数
            success: function (form, action) {
                return true;
            }
        });
    },
    addPlan:function(grid, rowIndex, colIndex){
    	var me = this;
    	var className ;
    	var title ;
    	if('assessment_plan'==grid.store.getAt(rowIndex).data.milestoneType){
    		className = 'FHD.ux.icm.assess.AssessPlanSelectorPanel';
    		title = '评价计划制定';
    	}else{
    		className = 'FHD.ux.icm.rectify.ImproveSelectorPanel';
    		title = '整改计划制定';
    	}
    
    	me.win = Ext.create('FHD.ux.Window',{
			    		title:title,
			    		collapsible:false,
			    		maximizable:true,
			    		items:Ext.create(className,{
				    		onSubmit:function(paramObj){
				    			grid.store.getAt(rowIndex).data.subPlan = paramObj.businessId;
				    			grid.refresh();
					    		me.win.close();
					    		
				    		}
			    		})
			    		
    		    }).show();
    }
});