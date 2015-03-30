Ext.define('FHD.view.risk.risk.RiskEventGrid', {
    extend: 'FHD.ux.layout.GridPanel',
    alias: 'widget.riskeventgrid',
    
    riskId:'root',		//左侧树的风险id
    riskName:'',	//左侧树的风险名称
    
    //title:'风险列表',
    border: true,		// 默认不显示border
    checked: true,		// 是否可以选中
    initComponent: function() {
        var me = this;

        me.cols = [
		{
			dataIndex:'id',
			hidden:true
		},
        {
            header: "状态",
            dataIndex: 'assessementStatus',
            sortable: true,
            width: 40,
            renderer: function(v) {
                var color = "";
                var display = "";
                if (v == "icon-ibm-symbol-4-sm") {
                    color = "symbol_4_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.hight");
                } else if (v == "icon-ibm-symbol-6-sm") {
                    color = "symbol_6_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.low");
                } else if (v == "icon-ibm-symbol-5-sm") {
                    color = "symbol_5_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.min");
                } else {
                    v = "icon-ibm-underconstruction-small";
                    display = "无";
                }
                return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" + "background-position: center top;' data-qtitle='' " + "class='" + v + "'  data-qtip='" + display + "'>&nbsp</div>";
            }
        },
        {
            header: "趋势",
            dataIndex: 'etrend',
            sortable: true,
            width:40,
            renderer: function (v) {
                var color = "";
                var display = "";
                if (v == "icon-ibm-icon-trend-rising-positive") {
                    color = "icon_trend_rising_positive";
                    display = FHD.locale.get("fhd.kpi.kpi.prompt.positiv");
                } else if (v == "icon-ibm-icon-trend-neutral-null") {
                    color = "icon_trend_neutral_null";
                    display = FHD.locale.get("fhd.kpi.kpi.prompt.flat");
                } else if (v == "icon-ibm-icon-trend-falling-negative") {
                    color = "icon_trend_falling_negative";
                    display = FHD.locale.get("fhd.kpi.kpi.prompt.negative");
                }
                return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
                    "background-position: center top;' data-qtitle='' " +
                    "class='" + v + "'  data-qtip='" + display + "'></div>";
            }
        },
        {
            header: '名称',
            dataIndex: 'name',
            sortable: true,
            flex:1,
            align: 'right',
            renderer:function(value,metaData,record){
            	var id = record.data.id;
            	return "<a onclick='Ext.getCmp(\""+me.id+"\").showDetail(\""+id+"\");return false;' >"+value+"</a>";
            }
        },
        {
            header: '责任部门',
            dataIndex: 'respDeptName',
            sortable: true,
            align: 'right',
            flex: 1
        },
        {
            header: '评估时间',
            dataIndex: 'adjustTime',
            sortable: true,
            align: 'right',
            flex: 1
        }
        ];
        
        me.btnAdd = Ext.create('Ext.Button', {
		    tooltip: '添加',
		    text: '添加',
            iconCls: 'icon-add',
		    handler: function() {
		        me.addFun();
		    }
		});
		me.btnEdit = Ext.create('Ext.Button', {
		    tooltip: '修改',
		    text: '修改',
            iconCls: 'icon-edit',
            disabled: true,
		    handler: function() {
		        me.editFun();
		    }
		});
		me.btnDel = Ext.create('Ext.Button', {
		    tooltip: '删除',
		    text: '删除',
            iconCls: 'icon-del',
            disabled: true,
		    handler: function() {
		        me.delFun();
		    }
		});
		me.btnStart = Ext.create('Ext.Button', {
		    tooltip: '启用',
		    text: '启用',
            iconCls: 'icon-plan-start',
            disabled: true,
		    handler: function() {
		        me.enablesFn("0yn_y");
		    }
		});
		me.btnEnd = Ext.create('Ext.Button', {
		    tooltip: '停用',
		    text: '停用',
            iconCls: 'icon-plan-stop',
            disabled: true,
		    handler: function() {
		        me.enablesFn("0yn_n");
		    }
		});
        //me.tbarItems = [me.btnAdd,'-', me.btnEdit,'-', me.btnDel,'-', me.btnStart,'-', me.btnEnd];
        
        Ext.apply(me, {
        	url : __ctxPath + "/risk/findEventById.f",//"/risk/findRiskAdjustHistoryById.f",
            extraParams:{
            	riskId:me.riskId
            },
            btns:[{
        			btype:'add',
        			handler:function(){
        				 me.addFun();
        			}
    			},{
        			btype:'edit',
        			handler:function(){
        				me.editFun();
        			}
    			},{
        			btype:'delete',
        			handler:function(){
        				me.delFun();
        			}
    			},{
        			tooltip: '启用',
				    text:'启用',
		            iconCls: 'icon-plan-start',
				    handler: function() {
				        me.enablesFn("0yn_y");
				    }
    			},{
        			tooltip: '停用',
				    text:'停用',
		            iconCls: 'icon-plan-stop',
				    handler: function() {
				        me.enablesFn("0yn_n");
				    }
    			}]
        });
        me.callParent(arguments);
        
        //记录发生改变时改变按钮可用状态
        me.on('selectionchange', function () {
            me.btnEdit.setDisabled(me.getSelectionModel().getSelection().length === 0);
            me.btnDel.setDisabled(me.getSelectionModel().getSelection().length === 0);
            me.btnStart.setDisabled(me.getSelectionModel().getSelection().length === 0);
            me.btnEnd.setDisabled(me.getSelectionModel().getSelection().length === 0);
        }); 
    },
    reLoadData:function(riskId,riskName){
    	var me = this;
    	me.riskId = riskId;
    	me.riskName = riskName;
    	me.store.proxy.extraParams.riskId = riskId;
		me.store.load();
    },
    /**
     * 查看详细风险页面
     */
    showDetail:function(id){
    	var me = this;
    	/**
         * 将左侧树id,name保持到face的全局变量中，共tab页面点击时加载使用
         */
        me.face.nodeId = id;
        
    	//跳转到查看页面
    	var tab = me.face.tabpanel;
        tab.setActiveTab(me.face.riskBasicFormView);
    	
    	//1 请求风险详细信息
    	FHD.ajax({
   			async:false,
   			params: {
                riskId: id
            },
            url: __ctxPath + '/risk/findRiskById.f',
            callback: function (ret) {
            	//显示目标详细信息
            	me.face.riskBasicFormView.reLoadData(ret);
            }
        });
    },
    /**
     * 添加
     */
    addFun:function(){
    	var me = this;
    	
    	//切换tab标签
        var tab = me.face.tabpanel;
        tab.setActiveTab(me.face.stepPanelContainer);	//？？步骤导航内部还有不同页面
    	var riskEditFormView = me.face.riskEditBasicFormView;
    	
        if(me.riskId=="root"){
        	var json = {parentId:''};
    		//继承父亲的值
    		json.isInherit = '0yn_y';
    		json.templeteId = '';
    		json.relativeTo = '';
    		json.formulaDefine = '';
    		json.alarmPlanId = '';
        	riskEditFormView.reSetData(json);
        	riskEditFormView.isEdit = false;
        	riskEditFormView.isRiskClass = 're';
        }else{
            //请求风险编辑信息,为新节点赋值
        	FHD.ajax({
       			async:false,
       			params: {
                    riskId: me.riskId
                },
                url: __ctxPath + '/risk/findRiskEditInfoById.f',
                callback: function (ret) {
                 	//清空数据，并设置上级id和初始值
                	var parentId = [];
                	var obj = {};
                	obj["id"] = me.riskId;
                	parentId.push(obj);
            		var json = {parentId:Ext.encode(parentId)};
            		//继承父亲的值
            		json.isInherit = '0yn_y';
            		json.templateId = ret.templateId;
            		json.relativeTo = ret.relativeTo||'';
            		json.formulaDefine = ret.formulaDefine;
            		json.alarmPlanId = ret.alarmPlanId;
                	riskEditFormView.reSetData(json);
                	riskEditFormView.isEdit = false;
                	riskEditFormView.isRiskClass = 're';
                }
            });
        }
    },
    /**
     * 编辑指标
     */
    editFun:function(){
    	var me = this;
        var selections = me.getSelectionModel().getSelection();
        var length = selections.length;
        if (length > 0) {
            if (length >= 2) {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.kpi.kpi.prompt.editone'));
                return;
            } else {
                var selection = me.getSelectionModel().getSelection()[0];
	            var id = selection.data.id;
	        	
	            //切换tab标签
	            var tab = me.face.tabpanel;
	            tab.setActiveTab(me.face.stepPanelContainer);	//？？步骤导航内部还有不同页面
	        	var riskEditFormView = me.face.riskEditBasicFormView;
	
	        	//请求风险编辑信息
	        	FHD.ajax({
	       			async:false,
	       			params: {
	                    riskId: id
	                },
	                url: __ctxPath + '/risk/findRiskEditInfoById.f',
	                callback: function (ret) {
	                	//显示目标详细信息
	                	riskEditFormView.reLoadData(ret);
	                	riskEditFormView.isEdit = true;
	                	riskEditFormView.editId = id;
	                }
	            });
            }
        } else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
    },
    /**
     * 删除
     */
    delFun: function () {
        var me = this;
        var delUrl = '/risk/risk/removeRiskById.f';
	    var selections = me.getSelectionModel().getSelection();
        if (selections.length > 0) {
        	Ext.MessageBox.show({
	            title: FHD.locale.get('fhd.common.delete'),
	            width: 260,
	            msg: FHD.locale.get('fhd.common.makeSureDelete'),
	            buttons: Ext.MessageBox.YESNO,
	            icon: Ext.MessageBox.QUESTION,
	            fn: function (btn) {
	                if (btn == 'yes') { //确认删除
	            		var ids = [];
				        Ext.Array.each(selections,
				        	function(item) {
				            	ids.push(item.get("id"));
				        });
	                    FHD.ajax({//ajax调用
							url : __ctxPath + delUrl + "?ids=" + ids.join(','),
							callback : function(data) {
								if (data) {//删除成功！
									 Ext.ux.Toast.msg(FHD.locale
											.get('fhd.common.prompt'), FHD.locale
											.get('fhd.common.operateSuccess'));
									 me.store.load();
								}
							}
						});
	                }
	            }
	        });
        }else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
    },
    /**
     * 启动和关闭
     */
    enablesFn: function(enable) {
        var me = this;

        var selections = me.getSelectionModel().getSelection();
        if (selections.length > 0) {
        	var ids = [];
	        Ext.Array.each(selections,
	        	function(item) {
	            	ids.push(item.get("id"));
	        });
	        FHD.ajax({
	            url: __ctxPath + '/risk/enableRisk',
	            params: {
	                ids: ids.join(','),
	                isUsed:enable
	            },
	            callback: function(data) {
	                if (data) {
	                    me.store.load();
	                    if(enable=='0yn_y'){
	                    	Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '启用成功!');
	                    }else{
	                    	Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '停用成功!');
	                    }
	                    
	                }
	            }
	        });
        }else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
		
    }

});