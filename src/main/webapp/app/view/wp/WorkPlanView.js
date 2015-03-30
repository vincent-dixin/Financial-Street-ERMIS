Ext.define('FHD.view.wp.WorkPlanView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.workplanview',
    
    
    bodyPadding: '0 3 3 3',
    
    initComponent: function() {
        var me = this;
        
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
        
        me.add(basicFieldSet);
        
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
        
        me.add(milestoneFieldSet);
        
        me.milestoneGrid = Ext.widget('fhdgrid',{
			checked:false,
			searchable:false,
			pagable : false,
			height: 140,
			url: __ctxPath + '/wp/findMilestoneList.f',
			extraParams: {
				workPlanId : me.workPlanId
			},
        	cols:[{
        			header: "里程碑名称", width: 150, sortable: false,dataIndex: 'milestoneName'
		       	},{
	            	header: "里程碑描述", width: 500, sortable: false, dataIndex: 'milestoneDesc'
				},{
					header: "完成日期", width: 100, sortable: false, dataIndex: 'milestoneDate',
					renderer: function (value) {
	                    if (value instanceof Date) {
	                        return Ext.Date.format(value, 'Y-m-d');
	                    } else {
	                        return value;
	                    }
	                }
				}]
        });
        
        milestoneFieldSet.add(me.milestoneGrid);
    },
    
    reloadData: function() {
    	var me = this,
    		workPlanId = me.workPlanId;
    	
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
    	
    }
});