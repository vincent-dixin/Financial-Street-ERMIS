Ext.define('FHD.view.risk.assess.formulatePlan.FormulatePlanPreviewGrid', {
    extend: 'FHD.ux.layout.EditorGridPanel',
    alias: 'widget.formulateplanpreviewgrid',
    flex:1,
    style: {
        borderColor: '#99bce8',
        borderStyle: 'solid',
        borderWidth:'1px'
    },
    //按部门添加
    deptSelect:function(){
    	var me = this;
		me.win = Ext.create('FHD.ux.org.DeptSelectorWindow',{//部门选择组件
			checkable:true,
			subCompany:false,
			rootVisible: false,
			multiSelect: true,   	
			checkModel:false,
			companyOnly: false,
		    onSubmit:function(win){//组件确定按钮事件
				var formulatePlanMainPanel = me.up('formulateplanmainpanel');
				var planId = formulatePlanMainPanel.planId;
				var data = win.selectedgrid.store.data;
				var queryRisk_DeptUrl = __ctxPath + '/access/formulateplan/queryriskandsavescoreobject.f';//按部门选择列表查询URL
				var ids = [];//所选部门id
				for(var i=0;i<data.length;i++){
    				ids.push(data.items[i].data.id);
    			}
				FHD.ajax({//ajax调用
    				url : queryRisk_DeptUrl,
    				params : {
    					orgIds:ids.join(','),
    					planId:planId
    				},
    				callback : function(data){
    					me.store.load();
    				}
    			});
			}
		}).show();
    },
    
    setstatus : function(me){//设置按钮可用状态
    	//me.btns[8].disabled = false;
    	Ext.getCmp('risk_grid_delete').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    
    delRisk : function(me){//删除方法
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
    					url : "access/formulateplan/removeriskscoresbyIds.f",
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
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.id = 'formulateplanpreviewgrid';
        //目标名称 目标责任人 衡量指标 权重  指标说明  指标责任人
        me.cols = [ {dataIndex : 'id', hidden : true}, 
         			{header : "上级风险", dataIndex : 'parentRiskName', sortable : true, flex : 1},
                    {header : "风险名称", dataIndex : 'riskName', sortable : true, flex : 2},
                    {header : "责任部门", dataIndex : 'mainOrgName', sortable : true, flex : 1}, 
                    {header : "相关部门", dataIndex : 'relaOrgName', sortable : true, flex : 1 },
                    {header : "参与部门", dataIndex : '', sortable : true, flex : 1 }
                  ];
        me.tbar = [{text : '按部门添加', iconCls: 'icon-add', handler:function(){me.deptSelect();}},'-',
                   {text : '按流程添加', iconCls: 'icon-add'},'-',
                   {text : '按指标添加', iconCls: 'icon-add'},'-',
                   {text : '按风险添加', iconCls: 'icon-add'},'-',
                   {btype: 'delete', id:'risk_grid_delete',disabled:true,handler:function(){me.delRisk(me);}}
                   ];
        
        Ext.apply(me, {
        	region:'center',
        	url : '',
            cols:me.cols,
            btns:me.tbar,
		    border: false,
		    checked: true,
		    pagable : false,
		    searchable : true
        });

        me.on('selectionchange',function(){me.setstatus(me)});
        me.callParent(arguments);
        
    	/*me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight()-120);
		});*/
    }

});