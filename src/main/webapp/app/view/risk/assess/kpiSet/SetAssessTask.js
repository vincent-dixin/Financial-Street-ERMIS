Ext.define('FHD.view.risk.assess.kpiSet.SetAssessTask', {
    extend: 'FHD.ux.layout.EditorGridPanel',
    alias: 'widget.setassesstask',
    requires: [
 	           'FHD.view.risk.assess.kpiSet.KpiSetRiskTaskGrid'
	],
    
    flex:1,
    border:false,
    //按风险分配窗口
    riskTaskWindow: function(){
    	var me = this;
    	var riskIdArray=[];
    	var items = me.store.data.items;
    	Ext.each(items,function(item){//循环取出每一列的风险id
				riskIdArray.push(item.data.riskId);
			});
    	var kpiSetRiskTaskGrid = Ext.widget('kpisetrisktaskgrid');
    	kpiSetRiskTaskGrid.store.load({params:{riskIds: riskIdArray.join(',')}});
    	me.riskWin = Ext.create('FHD.ux.Window', {
			title:'按风险分配',
   		 	height: 300,
    		width: 600,
   			layout: 'fit',
   			buttonAlign: 'center',
    		items: [kpiSetRiskTaskGrid],
   			fbar: [
   					{ xtype: 'button', text: '保存', handler:function(){me.winConfirm(kpiSetRiskTaskGrid);}},
   					{ xtype: 'button', text: '取消', handler:function(){me.riskWin.hide();}}
				  ]
		}).show();
    },
    
    winConfirm: function(grid){
    	var me = this;
		var empIds = [];
		var items = grid.store.data.items;
		for(var k in items){
				if(!items[k].data.empId){
					 empIds = [];
					 break;
				}else{
					empIds.push(items[k].data.empId);
				}
		}
		if(!empIds.length){
			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '评估人不能为空！');
			return ;
		}
		var rows = grid.store.getModifiedRecords();//取出列表每一行数据
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
		
		FHD.ajax({//ajax调用
    				url : __ctxPath + '/access/kpiSet/saveObjdeptempbysome.f',//保存对象，人员，部门综合表
    				params : {
    					modifyRecords:Ext.encode(jsonArray),
    					planId:'68fce32a-106f-4492-b979-263091aaf134'//***************************************//
    				},
    				callback : function(data){
    					
    				}
    			});
    	me.riskWin.hide();
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        //目标名称 目标责任人 衡量指标 权重  指标说明  指标责任人
        me.id = "setAssessTaskId";
        me.empStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
						proxy : {
							type : 'ajax',
							url : __ctxPath + '/access/kpiSet/findempsbyuserdeptId.f'
						}
		});
        var cols = [ 
		{header : "riskId",dataIndex:'riskId', hidden: true},
		{header : "planId",dataIndex:'planId', hidden: true},
		{
			header : "上级风险",
			dataIndex : 'parentRiskName',
			sortable : true,
			flex : 1
		}
		, {
			header : "风险名称",
			dataIndex : 'riskName',
			sortable : true,
			flex : 2
		},{
			header : "责任部门",
			dataIndex : 'mainOrgName',
			sortable : true,
			flex : 1
		}
		, {
			header : "相关部门",
			dataIndex : 'relaOrgName',
			sortable : true,
			flex : 1
		},
		{header:'评估人<font color=red>*</font>',dataIndex:'empId',flex:1,emptyCellText:'<font color="#808080">请选择</font>',
				editor:Ext.create('Ext.form.field.ComboBox',{
					store :me.empStore,
					//multiSelect: true,//多选
					valueField : 'id',
					displayField : 'name',
					allowBlank : false,
					editable : false,
					listeners:{
							select:function(){//监听下拉框值改变,保存
								debugger;
								var items = me.getSelectionModel().getSelection();
								var empId = this.getValue();
								var jsonArray=[];
								jsonArray.push(items[0].data);
								FHD.ajax({//ajax调用
    								url : __ctxPath + '/access/kpiSet/saveobjdeptempgridbysome.f',//保存对象，人员，部门综合表
    								params : {
    									modifyRecords: Ext.encode(jsonArray),
    									empId: empId
    								},
    								callback : function(data){
    					
    								}
    							});
							}
					}
					}),
					renderer:function(value,metaData,record,rowIndex ,colIndex,store,view){
						metaData.tdAttr = 'style="background-color:#FFFBE6"';
						var index = me.empStore.find('id',value);
						var record = me.empStore.getAt(index);
						if(record){
							return record.data.name;
						}else{
							return '';
						}
					}
			}
		];
        
        Ext.apply(me, {
        	region:'center',
        	url : __ctxPath + "/access/kpiSet/queryassesstaskspage.f",//列表查询url
            extraParams:{
            },
            cols:cols,
            btns:[ 
                  {
            		btype:'custom',
            		text:'按风险分配',
            		iconCls : 'icon-menu',
            		handler:function(){
            			me.riskTaskWindow();
            		}
            	  },{
            		btype:'custom',
            		text:'工作说明',
            		iconCls : 'icon-emp',
            		handler:function(){
            			//alert('save');
            		}
                  }],
		    border: false,
		    checked: false,
		    pagable : false,
		    searchable : true
        });

        me.callParent(arguments);
        
    }

});