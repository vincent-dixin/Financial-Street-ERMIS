/**
 * 
 * 计划制定表格
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulateGrid', {
    extend: 'FHD.ux.layout.GridPanel',
    alias: 'widget.formulateGrid',
 	requires: [
 	           'FHD.view.IndexNav',
 	           'FHD.view.risk.assess.formulatePlan.FormulatePlanEdit'
	],
    edit : function(isAdd){
    	var me = this;
    	var prt = me.up('formulatePlanCard');
    	var formulatePlanEdit = Ext.getCmp('formulatePlanEdit');
    	if(isAdd){//添加按钮
    		var responsPerson = Ext.getCmp('responsNameId');//负责人组件
    		var contactPerson = Ext.getCmp('contactNameId');//联系人组件
    		responsPerson.clearValues();//清空组件缓存
    		contactPerson.clearValues();
    		formulatePlanEdit.getForm().reset();
    		prt.showFormulatePlanMainPanel();
    	}else{//修改按钮
    		var selection = me.getSelectionModel().getSelection();
    		var length = selection.length;
    	    	   if (length >= 2) {//判断是否多选
    	    	        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.updateTip'));//提示
    	    	        return;
    	    	   }else{
    					if(typeof(formulatePlanEdit) != 'undefined'){
    						var responsPerson = Ext.getCmp('responsNameId');//负责人组件
    						var contactPerson = Ext.getCmp('contactNameId');//联系人组件
    						responsPerson.clearValues();//清空组件缓存
    						contactPerson.clearValues();
    						formulatePlanEdit.getForm().reset();
    						me.busId = selection[0].get('id');
    						formulatePlanEdit.loadData(me.busId);
    					}
//    					Ext.widget('indexNav').onMenuClick('FHD.view.risk.assess.formulatePlan.FormulatePlanMainPanel','计划制定',selection[0].get('id'));
    					prt.showFormulatePlanMainPanel();
    	    	   	}
    	}
    },
    
     del : function(me){//删除方法
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	Ext.MessageBox.show({
    		title : FHD.locale.get('fhd.common.delete'),
    		width : 260,
    		msg : FHD.locale.get('fhd.common.makeSureDelete'),
    		buttons : Ext.MessageBox.YESNO,
    		icon : Ext.MessageBox.QUESTION,
    		fn : function(btn) {
    			if (btn == 'yes') {//确认删除
    				var ids = [];
    				for(var i=0;i<selection.length;i++){
    					ids.push(selection[i].get('id'));
    				}
    				FHD.ajax({//ajax调用
    					url : me.delUrl,
    					params : {
    						ids:ids.join(',')
    					},
    					callback : function(data){
    						if(data){//删除成功！
    							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
    							me.store.load();
    						}
    					}
    				});
    			}
    		}
    	});
    },
    
    setstatus : function(me){//设置按钮可用状态
    	Ext.getCmp('planEditId').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('planDeleteId').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.id = 'formulateGridId';
        me.busId;
        me.delUrl = 'access/formulateplan/removeriskassessplanbyid.f';
        var cols = [
			{
				dataIndex:'id',
				hidden:true
			},
	        {
	            header: "计划名称",
	            dataIndex: 'planName',
	            sortable: true,
	            width:40,
	            flex:2,
	            renderer:function(value,metaData,record,colIndex,store,view) { 
     				metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
     	    		return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('formulateGridId').edit()\">"+value+"</a>";
     			}
	        },{
	            header: "工作类型",
	            dataIndex: 'workType',
	            sortable: true,
	            width:40,
	            flex:1,
	            renderer:function(dataIndex) { 
    				  if(dataIndex == "AL"){
    					  return "因果分析";
    				  }else if(dataIndex == "AK"){
    					  return "层次分析";
    				  }
    			}
	        },{
	            header: "负责人",
	            dataIndex: 'responsName',
	            sortable: true,
	            width:40,
	            flex:.5
	        },{
	            header: "联系人",
	            dataIndex: 'contactName',
	            sortable: true,
	            width:40,
	            flex:.5
	        },{
	            header: "开始时间",
	            dataIndex: 'beginDataStr',
	            sortable: true,
	            width:40,
	            flex:1
	        },{
	            header: "结束时间",
	            dataIndex: 'endDataStr',
	            sortable: true,
	            width:40,
	            flex:1
	        },{
	            header: "处理状态",
	            dataIndex: 'dealStatus',
	            sortable: true,
	            width:40,
	            flex:1,
	            renderer:function(dataIndex) { 
    				  if(dataIndex == "N"){
    					  return "未开始";
    				  }else if(dataIndex == "H"){
    					  return "处理中";
    				  }else if(dataIndex == "F"){
    					  return "已完成";
    				  }else if(dataIndex == "A"){
    					  return "逾期";
    				  }else if(dataIndex == "E"){
    					  return "已评价";
    				  }else if(dataIndex == "R"){
    					  return "已复核";
    				  }
    			}
	        },{
	            header: "状态",
	            dataIndex: 'status',
	            sortable: true,
	            width:40,
	            flex:1,
	            renderer:function(dataIndex) { 
    				  if(dataIndex == "S"){
    					  return "已保存";
    				  }else if(dataIndex == "P"){
    					  return "已提交";
    				  }else if(dataIndex == "D"){
    					  return "已处理";
    				  }
    			}
	        },{
	            header: "操作",
	            dataIndex: '',
	            sortable: true,
	            width:40,
	            flex:1,
	            renderer:function(){
					return "<a href=\"javascript:void(0);\" onclick=\"alert('评估工作跟踪')\">评估工作跟踪</a>&nbsp;&nbsp;&nbsp;"
				}
	        }
			
        ];
       
        Ext.apply(me,{
        	region:'center',
        	url : __ctxPath + "/access/formulateplan/queryAccessPlansPage.f",//查询列表url
            extraParams:{
            	riskId:1
            },
        	cols:cols,
        	btns:[{
        			btype:'add',
        			handler:function(){
        				me.edit(true);
        			}
    			},{
        			btype:'edit',
        			disabled:true,
        			id : 'planEditId',
        			handler:function(){
        				me.edit(false);
        			}
    			},{
        			btype:'delete',
        			disabled:true,
        			id : 'planDeleteId',
        			handler:function(){
        				me.del(me);
        			}
    			},{
        			tooltip: 'export',
				    text:'导出',
		            iconCls: 'icon-del',
				    handler: function() {
				        
				    }
    			},{
        			tooltip: '操作2',
				    text:'操作2',
		            iconCls: 'icon-del',
				    handler: function() {
				        
				    }
    			}],
		    border: true,
		    checked : true,
		    pagable : true
        });
       
        me.on('selectionchange',function(){me.setstatus(me)});
        me.callParent(arguments);

        
        me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight() - 40);
    	});
    }

});