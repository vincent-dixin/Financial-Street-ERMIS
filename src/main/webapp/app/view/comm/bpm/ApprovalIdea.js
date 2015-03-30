/**
 * 审批意见组件
 */
Ext.define('FHD.view.comm.bpm.ApprovalIdea',{
	extend : 'Ext.container.Container',
	alias : 'widget.approvalidea',
	
	isPass:'yes',//是否同意审批，默认yes
	layout : {
		type : 'column'
	},
	isShowRadio:'yes',
	//approvalIdeaName:'approvalIdeaName',
	isSuperior: false,
	margin: '7 10 0 30',
	
	initComponent : function() {
		var me = this;
		
		if("yes"==me.isShowRadio){
			//radio单选
			me.select = Ext.create('FHD.ux.dict.DictRadio', {
			    //margin : '7 5 5 0',
				columnWidth : 1,
				columns:5,
				labelWidth:60,
				fieldLabel:'审批',
				dictTypeId:'workflow_approve_status',
				defaultValue :'workflow_approve_status_agree',
				listeners: {
					change: function(t,newValue, oldValue,op){
						if(newValue._dictRadio=='workflow_approve_status_agree'){//同意审批
							me.isPass='yes';
						}else if(newValue._dictRadio=='workflow_approve_status_disagree'){//不同意审批
							me.isPass='no';
						}
					}
				}
			});	
		}
		
		// 审批意见
		me.ideaApproval = Ext.widget('textareafield',{
			margin : '20 5 5 0',
			columnWidth:1,
			labelWidth:60,
			hideLabel: true,
			fieldLabel : '审批意见',
			value: '同意',
			rows : 5
		});
		
		me.grid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			title:'审批意见历史列表',
			margin:'5 3 5 0',
			autoScroll:true,
			collapsible:true,
			collapsed:true,
			columnWidth:1
		});
		
		Ext.applyIf(me,{
			layout : {
		    	type : 'column'
		    }/*,
		    items: [
			    me.select,
			    me.ideaApproval,
			    me.grid
		    ]*/
		});
		me.callParent(arguments);
		
		
		me.superiorRadio = Ext.create('FHD.ux.dict.DictRadio', {
		    margin : '20 5 5 0',
			columnWidth : .5,
			labelWidth:60,
			fieldLabel:'领导审批',
			dictTypeId:'0yn',
			defaultValue :'0yn_n',
			name : 'superior'
		});
		
		if("yes"==me.isShowRadio){
			//radio单选
			me.select = Ext.create('FHD.ux.dict.DictRadio', {
			    margin : '20 5 5 0',
				columnWidth : .5,
				labelWidth:60,
				fieldLabel:'审批',
				dictTypeId:'workflow_approve_status',
				defaultValue :'workflow_approve_status_agree',
				listeners: {
					change: function(t,newValue, oldValue,op){
						if(newValue._dictRadio=='workflow_approve_status_agree'){//同意审批
							me.superiorRadio.show();
							me.isPass='yes';
							me.ideaApproval.setValue('同意');
						}else if(newValue._dictRadio=='workflow_approve_status_disagree'){//不同意审批
							me.superiorRadio.hide();
							me.isPass='no';
							me.ideaApproval.setValue('不同意');
						}
					}
				}
			});	
			
			me.add(me.select);
		}
		
		if(me.isSuperior) {
			me.add(me.superiorRadio);
		}
		
		
		me.add(me.ideaApproval);
		me.add(me.grid);
	},
	getValue:function(){
		var me=this;
		return me.ideaApproval.getValue();
	},
	reloadData:function(){
	    var me = this;
	    
	    if("yes"==me.isShowRadio){
	    	//radio按钮选中
	    	
	    }
	    me.ideaApproval.reset();
	    
	    me.grid.store.load();
	}
});