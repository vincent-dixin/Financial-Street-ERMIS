Ext.define('FHD.ux.process.processRelaOrgSelectorWindow', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.processrelaorgselectorwindow',
	
	requires : ['FHD.ux.org.DeptTree'],
	constrain : true,
	height : 500,
	width : 720,
	modal : true,
	collapsible : false,
	maximizable : true,
	layout : {
		type : 'border'
	},
	// 单选部门
	single : true,
	checkModel : 'multiple',

	values : new Array(),
	// 设置目标树图标
	smTreeIcon : 'icon-flag-red',

	// 赋值给grid,参数类型为store
	setValue : function(selecteds) {
		var me = this;
		var value = new Array();
		if (me.gridSelect) {
			if (Ext.typeOf(selecteds) == 'array') {
				Ext.Array.each(selecteds, function(selected) {
					me.gridSelect.store.insert(me.gridSelect.store.count(),	selected);
					value.push(selected.dbid);
				});
			}
			if (Ext.typeOf(selecteds) == 'object') {
				selecteds.each(function(selected) {
					me.gridSelect.store.insert(me.gridSelect.store.count(),	selected);
					value.push(selected.data.dbid);
				});
			}
		}
		me.values = value;
		this.setTreeValue(value);
	},
	//获得grid中的值
	getValue:function(){
		var me = this;
		var resultValue = new Array();
		me.gridSelect.store.each(function(value) {
			resultValue.push(value.data.id);
		});
		return resultValue;
	},
	setTreeValue : function(values) {
		var me = this;
		if (me.tree) {
			me.tree.values = values;
			me.setTreeValues(true);
		}
	},
	initComponent : function() {
		var me = this;
		if (me.single) {
			me.checkModel = 'single';
		}
		
		me.tree = Ext.create('FHD.ux.org.DeptTree',{
			split : true,
			region : 'west',
    		flex: 0.4,
    		title:'',
    		//chooseId:me.choose,
        	checkModel:me.checkModel,
	    	check:function(tree,node,checked){
	    		FHD.ajax({
	            	url: __ctxPath + '/process/process/findProcessByOrgIds.f',
	            	params:{
	            		orgIds:node.data.id
	            	},
	            	callback:function(response){
	            		if(checked){
	            			//me.grid.store.removeAll();
	            			var ids=new Array();
		            		Ext.Array.each(response.datas,function(item){
		            			var file=new Array();
		            			file.push({
		    		    			id:item.id,
		    		    			parentName:item.parentName,
		    		    			text:item.text,
		    		    			riskCount:item.riskCount,
		    		    			riskStatus:item.riskStatus,
		    		    			defectCount:item.defectCount,
		    		    			defectStatus:item.defectStatus
		    		    		});
		    	        		me.grid.store.insert(me.grid.store.count(),file);
		            		})
		    	    	}else{
		    	    		var ids=new Array();
		            		Ext.Array.each(response.datas,function(item){
		            			var file=new Array();
		            			file.push({
		            				id:item.id,
		    		    			parentName:item.parentName,
		    		    			text:item.text,
		    		    			riskCount:item.riskCount,
		    		    			riskStatus:item.riskStatus,
		    		    			defectCount:item.defectCount,
		    		    			defectStatus:item.defectStatus
		    		    		});
		            			me.grid.store.remove(me.grid.store.getById(item.id));
		            		})	
		    	    	}
	    				/*
	    				me.value = value;
	    				me.field.setValue(value);
	    				*/
	            	}
	            });
	    	}
	    });
		me.grid = Ext.create('Ext.grid.Panel', {
			flex:1,
			region : 'center',
			tbar : ['可选列表','-'],
			store : Ext.create('Ext.data.Store', {
				proxy : {
					type : 'ajax',
					reader : {
						type : 'json',
						root : 'users'
					}
				},
				idProperty : 'id',
				fields : ['id', 'parentName', 'text', 'riskCount', 'riskStatus', 'defectCount', 'defectStatus']
			}),
			columns : [
			    {
					//xtype : 'gridcolumn',
					hidden : true,
					dataIndex : 'id'
				},
				{
					header:'流程分类',
					dataIndex: 'parentName',
					flex : 2
				},
				{
					xtype: 'gridcolumn',
					dataIndex: 'text',
					flex : 3,
					text :'流程名称',
					renderer : function(value, metaData, record, rowIndex, colIndex, store) {
						return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
					}
				},
				/*
				{
					header:'风险数量',
					dataIndex: 'riskCount',
					flex: 1
				},
				{
					header:'风险状态',
					dataIndex: 'riskStatus',
					flex: 1,
					renderer: function (v) {
				        var color = "";
				        var display = "";
				        if (v == "0alarm_startus_h") {
				            color = "icon-ibm-symbol-4-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.low");
				        } else if (v == "0alarm_startus_l") {
				            color = "icon-ibm-symbol-6-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.hight");
				        } else if (v == "0alarm_startus_m") {
				            color = "icon-ibm-symbol-5-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.min");
				        } else {
				            v = "icon-ibm-underconstruction-small";
				            display = FHD.locale.get('fhd.common.none');
				        }
				        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
				            "background-position: center top;' data-qtitle='' " +
				            "class='" + color + "'  data-qtip='" + display + "'>&nbsp</div>";
				    }
				},
				*/
				{
					header:'缺陷数量',
					dataIndex: 'defectCount',
					flex: 1
				},
				{
					header:'缺陷状态',
					dataIndex: 'defectStatus',
					flex: 1,
					renderer: function (v) {
				        var color = "";
				        var display = "";
				        if (v == "ca_defect_level_0") {
				            color = "icon-ibm-symbol-4-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.low");
				        } else if (v == "ca_defect_level_2") {
				            color = "icon-ibm-symbol-6-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.hight");
				        } else if (v == "ca_defect_level_1") {
				            color = "icon-ibm-symbol-5-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.min");
				        } else {
				            v = "icon-ibm-underconstruction-small";
				            display = FHD.locale.get('fhd.common.none');
				        }
				        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
				            "background-position: center top;' data-qtitle='' " +
				            "class='" + color + "'  data-qtip='" + display + "'>&nbsp</div>";
				    }
				}
			]
		});
		
		me.grid.on('select',function(t,record,index,e){
			var gridStore=me.gridSelect.store;
			var thisId=record.data.id;
			var flag=true;
			gridStore.each(function(n){
				if(thisId==n.data.id){
					flag=false;
				}
			});
			if(flag){
				gridStore.add(record);   
			}
		});
		
		me.gridSelect = Ext.widget('grid',{
			flex:1,
			tbar : new Ext.Toolbar({
				height : 25,
				items : [
				    {
						xtype : "tbtext",
						text : $locale('standardselectorwindow.selectgrid.title')
					}
				]
			}),
			store : Ext.create('Ext.data.Store', {
				proxy : {
					type : 'ajax',
					reader : {
						type : 'json',
						root : 'users'
					}
				},
				idProperty : 'id',
				fields : ['id', 'parentName', 'text', 'riskCount', 'riskStatus', 'defectCount', 'defectStatus']
			}),
			columns : [
				{
					//xtype : 'gridcolumn',
					hidden : true,
					dataIndex : 'id'
				},
				{
					header:'流程分类',
					dataIndex: 'parentName',
					flex : 2
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'text',
					flex : 3,
					text : '流程名称',
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				},
				/*
				{
					header:'风险数量',
					dataIndex: 'riskCount',
					flex: 1
				},
				{
					header:'风险状态',
					dataIndex: 'riskStatus',
					flex: 1,
					renderer: function (v) {
				        var color = "";
				        var display = "";
				        if (v == "0alarm_startus_h") {
				            color = "icon-ibm-symbol-4-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.low");
				        } else if (v == "0alarm_startus_l") {
				            color = "icon-ibm-symbol-6-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.hight");
				        } else if (v == "0alarm_startus_m") {
				            color = "icon-ibm-symbol-5-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.min");
				        } else {
				            v = "icon-ibm-underconstruction-small";
				            display = FHD.locale.get('fhd.common.none');
				        }
				        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
				            "background-position: center top;' data-qtitle='' " +
				            "class='" + color + "'  data-qtip='" + display + "'>&nbsp</div>";
				    }
				},
				*/
				{
					header:'缺陷数量',
					dataIndex: 'defectCount',
					flex: 1
				},
				{
					header:'缺陷状态',
					dataIndex: 'defectStatus',
					flex: 1,
					renderer: function (v) {
				        var color = "";
				        var display = "";
				        if (v == "ca_defect_level_0") {
				            color = "icon-ibm-symbol-4-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.low");
				        } else if (v == "ca_defect_level_2") {
				            color = "icon-ibm-symbol-6-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.hight");
				        } else if (v == "ca_defect_level_1") {
				            color = "icon-ibm-symbol-5-sm";
				            display = FHD.locale.get("fhd.alarmplan.form.min");
				        } else {
				            v = "icon-ibm-underconstruction-small";
				            display = FHD.locale.get('fhd.common.none');
				        }
				        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
				            "background-position: center top;' data-qtitle='' " +
				            "class='" + color + "'  data-qtip='" + display + "'>&nbsp</div>";
				    }
				},
				{
					xtype : 'templatecolumn',
					text : $locale('fhd.common.delete'),
					align : 'center',
					tpl : '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
					listeners : {
						click : {
							fn : function(grid,d, i) {
								/*		
								var nodes = me.stadardtree.standardTree.getChecked();
								if(me.mytype==0){
								  	Ext.each(nodes,function(n) {
								     	if (n.data.id == grid.store.getAt(i).data.id) {
								         	n.set("checked",false);
							            }
						         	});
								}
								*/
								grid.store.removeAt(i);
							}
						}
					}
				}
			]
		});
		/*	
		me.buttons = [{
			xtype : 'button',
			text : $locale('fhd.common.confirm'),
			handler : function() {
				me.onSubmit(me.gridSelect.store);
				me.close();
			}
		}, {
			xtype : 'button',
			text : $locale('fhd.common.close'),
			style : {
				marginLeft : '10px'
			},
			handler : function() {
				me.close();
			}
		}];
		*/

		Ext.applyIf(me, {
			items : [me.tree,{
                xtype: 'container',
                layout: {
                    align: 'stretch',
                    type: 'vbox'
                },
                region: 'center',
                items: [me.grid,me.gridSelect]
            }]
		});

		me.callParent(arguments);
		me.setValue(me.values);
	},
    setTreeValues:function(checked){
    	if (this.tree) {
    		this.tree.values = this.values;
    		this.tree.setChecked(this.tree.getRootNode(), checked);
    	}
    }
});