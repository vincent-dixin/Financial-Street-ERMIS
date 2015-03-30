Ext.define('FHD.view.kpi.result.RealTimeGrid', {
    extend: 'FHD.ux.EditorGridPanel',
    alias: 'widget.realtimegrid',
    
    border: true,
    
    addEvent: function () {
        var me = this;
        var r = Ext.create('eventModel');
        me.store.insert(0,r);
        me.editingPlugin.startEditByPosition({
            row: 0,
            column: 0
        });
        me.doComponentLayout();

    },
    
    //返回
    goback : function(){
    	var me = this;
    	var scorecardmainpanel = Ext.getCmp('scorecardmainpanel');
    	scorecardmainpanel.navigationBar.renderHtml('scorecardtabcontainer', Ext.getCmp('scorecardmainpanel').paramObj.categoryid , '', 'sc');
    	Ext.getCmp('scorecardtab').reLoadData();
    	Ext.getCmp('metriccentercardpanel').setActiveItem(scorecardmainpanel);
    	
    },
    onchange:function(me){
    	me.down('#historySaveId${param._dc}').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    remove:function(me){
    	var rows = me.getSelectionModel().getSelection();
    	if(rows.length>0){
    		Ext.MessageBox.show({
                title: FHD.locale.get('fhd.common.delete'),
                width: 260,
                msg: FHD.locale.get('fhd.common.makeSureDelete'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                	var jsobj = {kpiName:me.kpiName};
                	jsobj.datas=[];
            		Ext.each(rows,function(item){
            			jsobj.datas.push(item.data);
            		});
            		if(jsobj.datas.length>0){
            			FHD.ajax({
            				url : __ctxPath + "/category/removeriskevent.f",
            				params : {
            					records:Ext.encode(jsobj)
            				},
            				callback : function(data){
            					if(data){
            						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
            						me.store.load();
            					}
            				}
            			});
            		}
            		me.store.commitChanges();
                }
            });
    	}
    	
    	
    },
    save:function(me){
    	var rows = me.store.getModifiedRecords();
    	var jsobj = {kpiName:me.kpiName};
    	jsobj.datas=[];
		Ext.each(rows,function(item){
			 if(item.data.value==""){
				 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), "实际值不能为空.");
				 return ;
			 }
			 if(item.data.date==""){
				 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),"发生时间不能为空.");
				 return ;
			 }
			 if (item.data.date instanceof Date) {
				 item.data.date = Ext.Date.format(new Date(item.data.date), 'Y-m-d H:i:s');
			 }
			jsobj.datas.push(item.data);
		});
		if(jsobj.datas.length>0){
			FHD.ajax({
				url : __ctxPath + "/category/saveriskevent.f",
				params : {
					records:Ext.encode(jsobj)
				},
				callback : function(data){
					if(data){
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
						me.store.load();
					}
				}
			});
		}
		me.store.commitChanges();
    },
    /**
     * 初始化组件
     */
    initComponent: function () {
        var me = this;
        
        
        var states = Ext.create('Ext.data.Store', {
            fields: ['sid', 'name'],
            data : [
                {"sid":"5", "name":"★★★★★"},
                {"sid":"4", "name":"★★★★"},
                {"sid":"3", "name":"★★★"}
            ]
        });
        
        

        var statusbox = Ext.create('Ext.form.ComboBox', {
            store: states,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'sid'
        });
        
        Ext.define('eventModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'status', 'value', 'desc', 'date']
        });
        var ccols = [];
        ccols.push({
	            dataIndex: 'id',
	            width: 0
	        });
        ccols.push({
	            cls: 'grid-icon-column-header grid-statushead-column-header',
	            header: "<span data-qtitle='' data-qtip='" + FHD.locale.get("fhd.sys.planEdit.status") + "'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
	            dataIndex: 'status',
	            sortable: true,
	            width:40,
	            renderer: function (v) {
	                var display = "";
	                if (v == "icon-ibm-symbol-4-sm") {
	                    display = FHD.locale.get("fhd.alarmplan.form.hight");
	                } else if (v == "icon-ibm-symbol-6-sm") {
	                    display = FHD.locale.get("fhd.alarmplan.form.low");
	                } else if (v == "icon-ibm-symbol-5-sm") {
	                    display = FHD.locale.get("fhd.alarmplan.form.min");
	                } else if(v=="icon-ibm-symbol-safe-sm"){
	                	display = "安全";
	                } 
	                else {
	                    v = "icon-ibm-underconstruction-small";
	                    display = "无";
	                }
	                return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
	                    "background-position: center top;' data-qtitle='' " +
	                    "class='" + v + "'  data-qtip='" + display + "'>&nbsp</div>";
	            }
	        });
        if(me.kpiId=="anquan_zhishu"){
        	ccols.push({
	            header: '实际值'+ '<font color=red>*</font>',
	            dataIndex: 'value',
	            sortable: false,
	            flex: 0.5,
	            align: 'right',
	            editor: statusbox,
	            renderer: function (v) {
	            	var msg = "";
	            	if("5"==v||"5.0000"==v){
	            		msg = "★★★★★";
	            	}else if("4"==v||"4.0000"==v){
	            		msg = "★★★★";
	            	}else if("3"==v||"3.0000"==v){
	            		msg = "★★★";
	            	}
	            	return msg;
	            }
	        });
        }else{
        	ccols.push({
	            header: '实际值'+ '<font color=red>*</font>',
	            dataIndex: 'value',
	            sortable: false,
	            flex: 0.3,
	            align: 'right',
	            editor: {
	                xtype: 'numberfield',
	                allowDecimals: true
	            }
	        });
        }
        ccols.push({
	            header: '事件说明',
	            dataIndex: 'desc',
	            sortable: false,
	            align: 'right',
	            flex: 2,
	            editor: {
	                xtype: 'textareafield',
	                height:100
	            },
	            renderer:function(value,metaData,record,colIndex,store,view) { 
	            	return "<div style=' background-repeat: no-repeat;" +
	                "background-position: center top;' data-qtitle='' " +
	                "class='" + "" + "' data-qwidth='100' data-qalign='tl-br' data-qtip='" + value + "'>"+value+"</div>"; 
	        	}
	        });
        ccols.push({
            header: '发生时间'+ '<font color=red>*</font>',
            dataIndex: 'date',
            align: 'right',
            sortable: false,
            flex: 0.4,
            renderer: function (value) {
                if (value instanceof Date) {
                    return Ext.Date.format(value, 'Y-m-d H:i:s');
                } else {
                    return value;
                }
            },
            editor: new Ext.form.DateField({
                //在编辑器里面显示的格式,这里为10/20/09的格式  
                	format:'Y-m-d H:i:s'//默认配置
            })
        });
        
        /*var ccols = 
	        [{
	            dataIndex: 'id',
	            width: 0
	        }, 
	        
	        {
	            header: '实际值'+ '<font color=red>*</font>',
	            dataIndex: 'value',
	            sortable: false,
	            flex: 1,
	            align: 'right',
	            editor: statusbox,
	            renderer: function (v) {
	            	var msg = "";
	            	if("5"==v){
	            		msg = "★★★★★";
	            	}else if("4"==v){
	            		msg = "★★★★";
	            	}else if("3"==v){
	            		msg = "★★★";
	            	}
	            	return msg;
	            }
	        },
	        
	        {
	            cls: 'grid-icon-column-header grid-statushead-column-header',
	            header: "<span data-qtitle='' data-qtip='" + FHD.locale.get("fhd.sys.planEdit.status") + "'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
	            dataIndex: 'status',
	            sortable: true,
	            width:40,
	            renderer: function (v) {
	                var display = "";
	                if (v == "icon-ibm-symbol-4-sm") {
	                    display = FHD.locale.get("fhd.alarmplan.form.hight");
	                } else if (v == "icon-ibm-symbol-6-sm") {
	                    display = FHD.locale.get("fhd.alarmplan.form.low");
	                } else if (v == "icon-ibm-symbol-5-sm") {
	                    display = FHD.locale.get("fhd.alarmplan.form.min");
	                } else if(v=="icon-ibm-symbol-safe-sm"){
	                	display = "安全";
	                } 
	                else {
	                    v = "icon-ibm-underconstruction-small";
	                    display = "无";
	                }
	                return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
	                    "background-position: center top;' data-qtitle='' " +
	                    "class='" + v + "'  data-qtip='" + display + "'>&nbsp</div>";
	            }
	        },
	        {
	            header: '实际值'+ '<font color=red>*</font>',
	            dataIndex: 'value',
	            sortable: false,
	            flex: 0.3,
	            align: 'right',
	            editor: {
	                xtype: 'numberfield',
	                allowDecimals: true
	            }
	        }, {
	            header: '事件说明',
	            dataIndex: 'desc',
	            sortable: false,
	            align: 'right',
	            flex: 2,
	            editor: {
	                xtype: 'textareafield',
	                height:100
	            },
	            renderer:function(value,metaData,record,colIndex,store,view) { 
	            	return "<div style=' background-repeat: no-repeat;" +
	                "background-position: center top;' data-qtitle='' " +
	                "class='" + "" + "' data-qwidth='100' data-qalign='tl-br' data-qtip='" + value + "'>"+value+"</div>"; 
	        	}
	        },
	        {
	            header: '发生时间'+ '<font color=red>*</font>',
	            dataIndex: 'date',
	            align: 'right',
	            sortable: false,
	            flex: 0.4,
	            renderer: function (value) {
	                if (value instanceof Date) {
	                    return Ext.Date.format(value, 'Y-m-d H:i:s');
	                } else {
	                    return value;
	                }
	            },
	            editor: new Ext.form.DateField({
	                //在编辑器里面显示的格式,这里为10/20/09的格式  
	                	format:'Y-m-d H:i:s'//默认配置
	            })
	        }
	];*/
        
        Ext.apply(me, {
        	pagable: false,
			tbarItems : [
						{
						    iconCls: 'icon-add',
						    handler: function () {
						        me.addEvent();//添加记录
						    },
						    scope: this
						}, '-',{
						    iconCls: 'icon-del',
						    handler: function () {
						        me.remove(me);//删除记录
						    },
						    scope: this
						}, '-',
						{text : FHD.locale.get('fhd.common.save'),iconCls: 'icon-save',handler:function(){me.save(me)}, disabled : false, scope : this}, '-',
					    '->',
						{
							text : '返回',
							iconCls : 'icon-arrow-undo',
							handler : function() {
								me.goback();
							}
						} 
			],
            cols: ccols
        });
        

        me.callParent(arguments);
        //me.on('selectionchange',function(){me.onchange(me)});//选择记录发生改变时改变按钮可用状态
        me.store.proxy.url = __ctxPath + "/category/findriskevent.f";
        me.store.proxy.extraParams = {kpiId:me.kpiId};
        me.store.load();
    },

});