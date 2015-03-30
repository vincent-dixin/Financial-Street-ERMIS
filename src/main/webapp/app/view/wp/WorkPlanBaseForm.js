Ext.define('FHD.view.wp.WorkPlanBaseForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.workplanbaseform',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	
    	me.callParent(arguments);
        
        me.hiddenId = Ext.widget('hidden',{
        	hidden: true,
			name: 'id'
        })
        
        me.basicFieldSet = Ext.widget('fieldset', {
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
        
        me.add(me.basicFieldSet);
        
        
        Ext.apply(Ext.form.VTypes, {
		    codeField: function(val, field) {
		    	var flag = false;
		    	FHD.ajax({
		    		async:false,
	                url: __ctxPath + '/wp/validatecode.f',
	                params: {
	                	workPlanId : me.hiddenId.getValue(),
	                    code:val
	                },
	                callback: function (data) {
	                	flag = data;
	                }
	            });
		    	
		        return flag;
		    },
		    codeFieldText: '编号重复'
		});
        
        // 计划编号
        me.codeField = Ext.widget('textfield',{
        	fieldLabel : '计划编号<font color=red>*</font>',
        	validateOnChange: false,
        	allowBlank: false,
			vtype : 'codeField',
        	name : 'code'
        })
        
        me.basicFieldSet.add(me.codeField);
        
        // 计划名称
        me.nameField = Ext.widget('textfield',{
        	fieldLabel : '计划名称<font color=red>*</font>',
			allowBlank: false,
        	name : 'name'
        })
        
        me.basicFieldSet.add(me.nameField);
        
        // 计划起止日期
        me.startEndDateField = Ext.widget('fieldcontainer',{
        	fieldLabel : '计划起止日期',
        	layout: 'hbox',
	        items: [{
	            xtype: 'datefield',
	            name: 'startDateStr',
	            format:'Y-m-d',
	            flex: 1
	        },{
	            xtype: 'displayfield',
	            value: '至'
	        },{
	            xtype: 'datefield',
	            name: 'endDateStr',
	            format:'Y-m-d',
	            flex: 1
	        }]
        })
        
        me.basicFieldSet.add(me.startEndDateField);
        
        // 责任人
        me.responsilePersionField = Ext.create('FHD.ux.org.CommonSelector',{
        	type:'emp',
        	multiSelect:false,
        	fieldLabel : '责任人',
        	name : 'responsilePersionId'
        })
        
        me.basicFieldSet.add(me.responsilePersionField);
        
        // 计划目标
        me.targetField = Ext.widget('textarea',{
        	fieldLabel : '计划目标',
        	name : 'target'
        })
        
        me.basicFieldSet.add(me.targetField);
        
        // 计划内容
        me.contentField = Ext.widget('textarea',{
        	fieldLabel : '计划内容',
        	name : 'content'
        })
        
        me.basicFieldSet.add(me.contentField);
        
        // 实施部门
        me.orgsField = Ext.create('FHD.ux.org.CompSelect',{
        	fieldLabel : '实施单位',
        	name : 'orgs'
        })
        
        me.basicFieldSet.add(me.orgsField);
        
        // 考核要求
        me.assessRequirementField = Ext.widget('textarea',{
        	fieldLabel : '考核要求',
        	name : 'assessRequirement'
        })
        
        me.basicFieldSet.add(me.assessRequirementField);
               
        // 贡献目标额
        me.contributeTargetAmountField = Ext.widget('numberfield',{
        	fieldLabel : '贡献目标额',
        	name : 'contributeTargetAmount'
        })
        
        me.basicFieldSet.add(me.contributeTargetAmountField);
        
        
        me.milestoneFieldSet = Ext.widget('fieldset', {
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
            title: '里程碑设置'
        });
        
        me.add(me.milestoneFieldSet);
        
        Ext.define('MilestoneModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'milestoneName', 'milestoneDesc', 'milestoneDate']
        });
        
        var milestoneType = Ext.widget('dictselectforeditgrid',{
			typeahead: true,
			triggerAction:'all',
			selectOnTab: true,
			dictTypeId:'work_plan_milestone_type',
			hideLabel : true,
			allowBlank: false,
			name:'milestoneName'
		});
		milestoneType.store.load();
        
        me.milestoneGrid = Ext.widget('fhdeditorgrid',{
			checked:true,
			searchable:false,
			pagable : false,
			height: 140,
			url: __ctxPath + '/wp/findMilestoneList.f',
			tbarItems:[{
				iconCls: 'icon-add',
				handler:function(){
					var r = Ext.create('MilestoneModel');
			        me.milestoneGrid.store.add(r);
			        me.milestoneGrid.editingPlugin.startEditByPosition({
			            row: 0,
			            column: 0
			        });
			        me.milestoneGrid.doComponentLayout();
				}
			},'-',{
				iconCls: 'icon-del',
				handler:function(){
			        var selection = me.milestoneGrid.getSelectionModel().getSelection();
			        Ext.MessageBox.show({
			            title: FHD.locale.get('fhd.common.delete'),
			            width: 260,
			            msg: FHD.locale.get('fhd.common.makeSureDelete'),
			            buttons: Ext.MessageBox.YESNO,
			            icon: Ext.MessageBox.QUESTION,
			            fn: function (btn) {
			                if (btn == 'yes') {
			                    var ids = [];
			                    for (var i = 0; i < selection.length; i++) {
			                        me.milestoneGrid.store.remove(selection[i]);
			                    }
			                }
			            }
			        });
				}
			}],
        	cols:[{
        			dataIndex:'milestoneType',hidden : true
        		},{
        			header: "里程碑名称", width: 150, sortable: false,dataIndex: 'milestoneName',flex:1,
					editor: milestoneType,
					renderer: function (value,metaData,record,rowIndex,colIndex,store,view) {
						record.data.milestoneType = value;
						var comboStore = milestoneType.store;
	                    var index = comboStore.find('id',value);
						var r = comboStore.getAt(index);
						if (r == null) {
					        return value;
					    } else {
					        return r.data.name; // 获取record中的数据集中的display字段的值 
					    }
	                }
		       	},{
	            	header: "里程碑描述", width: 500, sortable: false, dataIndex: 'milestoneDesc',flex:2,
					editor: Ext.widget('textarea',{
						allowBlank: false,
						name:'milestoneDesc'
					})
				},{
					header: "完成日期", width: 100, sortable: false, dataIndex: 'milestoneDate',flex:1,
					renderer: function (value) {
	                    if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
	                    } else {
	                        return value;
	                    }
	                },
					editor: Ext.widget('datefield',{
						allowBlank: false,
						format:'Y-m-d',
						name:'milestoneDate'
					})
				}]
        });
        
        me.milestoneFieldSet.add(me.milestoneGrid);
    },
    /*
     * 整理里程碑列表数据
     */
    buildMilestoneStore: function(){
    	var me = this;
 		var rows = me.milestoneGrid.store.data.items;
 		var jsonArray = new Array();
		Ext.Array.each(rows, function (item) {
		    jsonArray.push(item.data);
		});
    	return jsonArray;
    },
    /**
     * 
     * @param {String} json
     * @return {Boolean}
     */
    validateMilestone : function() {
    	 var me = this;
    	 var json = me.buildMilestoneStore();
    	 if(json.length < 1) {
    	 	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '<font color="red">至少有添加里程碑</font>');
            return false;
    	 }
    	
    	 for (var i = 0; i < json.length; i++) {
            if (json[i].milestoneName == "") {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '<font color="red">里程碑名称不能为空</font>');
                return false;
            }
            if (json[i].milestoneDesc == "") {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '<font color="red">里程碑描述不能为空</font>');
                return false;
            }
            if (json[i].milestoneDate == "") {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '<font color="red">完成日期不能为空</font>');
                return false;
            }
        }
        return true;
    }
    
});