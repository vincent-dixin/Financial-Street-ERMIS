/**
 * 添加目标时,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */

Ext.define('Ext.kpi.strategyMap.smBasicPanel', {
    extend: 'Ext.form.Panel',
    /**
	 * @cfg {boolean} 是否显示边框
	 */
    border: false,
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last:function(cardPanel,finishflag){
		var me = this;
		var form = me.getForm();
	    var vobj = form.getValues();
        FHD.ajax({
            url: __ctxPath + '/kpi/kpistrategymap/validate.f',
            params: {
                name: vobj.name,
                id: me.smid,
                code: vobj.code
            },
            callback: function (data) {
                if (data && data.success) {
                    //提交目标信息
                	//取预警公式值
                	var warningFormulaValue = Ext.getCmp('warningFormula'+sm_paramdc).getValue();
                	//评估值公式
                	var assessmentFormulaValue = Ext.getCmp('assessmentFormula'+sm_paramdc).getValue();
                	assessmentFormulaValue = assessmentFormulaValue==null?"":assessmentFormulaValue;
                	
                    var param = Ext.JSON.encode({
                    	currentSmId:me.smid,
                    	warningFormula:warningFormulaValue==undefined?"":warningFormulaValue,
                    	assessmentFormula:assessmentFormulaValue});
                    var addUrl = __ctxPath + '/kpi/kpistrategymap/mergestrategymap.f?param='+param;
                    FHD.submit({
                        form: form,
                        url: addUrl,
                        callback: function (data) {
                            if (data) {
                                smname = vobj.name;
                                smid = data.smId;
                                //fhd_kpi_sm_tree_view.refreshTree();
                                var layout = cardPanel.getLayout();
                                if(!finishflag){
                                	cardPanel.pageMove("next");//否则移动到下一个面板
                               	 	smcontainer._navBtnState(cardPanel);
                                    //同时将衡量指标按钮变为激活状态
                                    Ext.getCmp('sm_kpiset_btn'+sm_paramdc).setDisabled(false);
                                    Ext.getCmp('sm_kpiset_btn_top'+sm_paramdc).setDisabled(false);
                                }else{
                                	smcontainer._gotopage();
                                }
                                
                                if(me.editflag=='false'){//添加节点
                                    var node = {id:smid,text:smname,dbid:smid,leaf:true,type:'sm'};
                                    if(fhd_kpi_sm_tree_view.currentNode.isLeaf()){
                                    	fhd_kpi_sm_tree_view.currentNode.data.leaf = false;
                                    }
                                    fhd_kpi_sm_tree_view.currentNode.appendChild(node);
                                    fhd_kpi_sm_tree_view.currentNode.expand();
                                    fhd_kpi_sm_tree_view.tree.getSelectionModel().select(fhd_kpi_sm_tree_view.currentNode.lastChild);
                                  }else{//编辑节点
                                  	//需要替换节点名称
                                  }
                                
                            }
                        }
                    });
                } else {
                    if (data && data.error == "nameRepeat") {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.nameRepeat'));
                        return;
                    }
                    if (data && data.error == "codeRepeat") {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.codeRepeat'));
                        return;
                    }
                }
            }
        });
    	
    },
    /**
     * 生成编码函数
     */
    createCode:function(){
    	var me = this;
    	var vform = me.getForm();
        var vobj = vform.getValues();
        var paraobj = {};
        paraobj.currentSmId = me.smid;
        if (vobj.parentId != "") {
            paraobj.parentid = vobj.parentId;
            FHD.ajax({
                url: __ctxPath + '/kpi/kpistrategymap/findcodebyparentid.f',
                params: {
                    param: Ext.JSON.encode(paraobj)
                },
                callback: function (data) {
                    if (data && data.success) {
                        vform.setValues({
                            code: data.code
                        });
                    }
                }
            });
        }
    },
    
    
    /**
     * 获得图表类型值
     */
    getChartTypeValue:function(){
    	var me = this;
    	return me.form.getValues().charttypehidden;
    },
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;


        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3",
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                autoHeight: true,
                autoWidth: true,
                collapsible: true,
                defaults: {
                    margin: '3 30 3 30',
                    labelWidth: 85
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.common.baseInfo'),
                items: [{
                    xtype: 'hidden',
                    hidden: true,
                    name: 'currentSmId',
                    value: me.smid
                }, {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'parentId',
                    value: me.smparentid
                }, 
                {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'charttypehidden'
                },
                {
                    id: 'smparentname'+sm_paramdc,
                    xtype: 'textfield',
                    disabled: true,
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.parent') + '<font color=red>*</font>',//上级目标
                    value: me.smparentname,
                    name: 'parentname',
                    maxLength: 200,
                    columnWidth: .5,
                    allowBlank: false
                }, {
                    xtype: 'textfield',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'),//编码
                    margin: '3 3 3 30',
                    name: 'code',
                    maxLength: 255,
                    columnWidth: .4,
                    allowBlank: true
                }, {
                    xtype: 'button',
                    margin: '3 30 3 3',
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),//自动生成编码按钮
                    handler: function(){
                    	me.createCode();
                    },
                    columnWidth: .1
                }, {
                    id: 'smname'+sm_paramdc,
                    xtype: 'textareafield',
                    rows: 2,
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.name') + '<font color=red>*</font>',//名称
                    margin: '7 30 3 30',
                    name: 'name',
                    maxLength: 255,
                    columnWidth: .5,
                    allowBlank: false
                }, {
                    xtype: 'textareafield',
                    rows: 2,
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'),//短名称
                    margin: '7 30 3 30',
                    name: 'shortName',
                    columnWidth: .5,
                    allowBlank: true,
                    maxLength: 255
                },
                Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    multiSelect: false,
                    margin: '7 30 3 30',
                    name: 'mainDim',
                    labelWidth: 85,
                    dictTypeId: 'kpi_dimension',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'),//主维度
                    columnWidth: .5,
                    labelAlign: 'left',
                    columns: 5
                }),
                Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    multiSelect: false,
                    margin: '7 30 3 30',
                    name: 'mainTheme',
                    labelWidth: 85,
                    dictTypeId: 'kpi_theme',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainTheme'),//战略主题
                    columnWidth: .5,
                    labelAlign: 'left',
                    columns: 5
                }),
                Ext.create('FHD.ux.dict.DictSelect', {
                	id:'sm_otherDimMultiCombo'+sm_paramdc,
                    margin: '7 30 3 30',
                    name: 'otherDim',
                    labelWidth: 85,
                    dictTypeId: 'kpi_dimension',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'),//辅助纬度
                    columnWidth: .5,
                    labelAlign: 'left',
                    columns: 5,
                    multiSelect: true
                }),
                Ext.create('FHD.ux.dict.DictSelect', {
                        margin: '7 30 3 30',
                        name: 'otherTheme',
                        id:'sm_otherTheme'+sm_paramdc,
                        labelWidth: 85,
                        dictTypeId: 'kpi_theme',
                        fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherTheme'),//辅助战略主题
                        columnWidth: .5,
                        labelAlign: 'left',
                        columns: 5,
                        multiSelect: true
                }),
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept"),//所属部门人员
                    labelWidth: 85,
                    growMax: 74,
                    labelAlign: 'left',
                    margin: '7 30 3 30',
                    columnWidth: .5,
                    name: 'ownDept',
                    multiSelect: false,
                    type: 'dept_emp'
                }), {
                    xtype: 'dictradio',
                    margin: '7 30 3 30',
                    name: 'estatus',
                    columns: 4,
                    dictTypeId: '0yn',
                    fieldLabel: FHD.locale.get('fhd.common.enable'),//是否启用
                    defaultValue: '0yn_y',
                    labelAlign: 'left',
                    allowBlank: false,
                    columnWidth: .5
                },
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.viewEmp"),//查看人
                    labelWidth: 85,
                    growMax: 74,
                    labelAlign: 'left',
                    margin: '7 30 3 30',
                    columnWidth: .5,
                    name: 'viewDept',
                    multiSelect: true,
                    type: 'dept_emp',
                    maxHeight: 68
                }), 
                
                Ext.create('FHD.ux.kpi.FormulaTrigger',{
                	fieldLabel:FHD.locale.get('fhd.strategymap.strategymapmgr.form.warningFormula'),
        	        hideLabel:false,
        	        id:'warningFormula'+sm_paramdc,
        	        emptyText: '',
        	        labelAlign: 'left',
        	        flex:1.5,
        	        labelWidth: 85,
        	        cols: 20,
        	        margin: '7 30 3 30',
            		rows: 5,
        	        name:'warningFormula',
        	        type:'kpi',
        	        showType:'all',
        	        column:'assessmentValueFormula',
        	        columnWidth: .5
        	        //,targetId:me.targetId,
        	        //targetName:me.targetName
        	    }),
        	    
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.reportEmp"),//报告人
                    labelWidth: 85,
                    growMax: 74,
                    labelAlign: 'left',
                    margin: '7 30 3 30',
                    columnWidth: .5,
                    name: 'reportDept',
                    multiSelect: true,
                    type: 'dept_emp',
                    maxHeight: 70
                }), 
               
                Ext.create('FHD.ux.kpi.FormulaTrigger',{
                	fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula'),//评估值公式
        	        hideLabel:false,
        	        id:'assessmentFormula'+sm_paramdc,
        	        emptyText: '',
        	        labelAlign: 'left',
        	        flex:1.5,
        	        cols: 20,
            		rows: 5,
        	        name:'assessmentFormula',
        	        type:'kpi',
        	        showType:'kpiType',
        	        column:'assessmentValueFormula',
        	        labelWidth: 85,
        	        columnWidth: .5
        	        //,targetId:me.targetId,
        	        //targetName:me.targetName
        	    }),
        	    
        	    Ext.create('FHD.ux.dict.DictSelect', {
                    id: 'sm_charttype'+sm_paramdc,
                    editable: false,
                    labelWidth: 85,
                    margin: '7 30 3 30',
                    multiSelect: true,
                    name: 'chartTypeStr',
                    dictTypeId: 'strategy_map_chart_type',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.category.charttype'),//图表类型
                    columnWidth: .5,
                    labelAlign: 'left',
                    maxHeight: 70
                }),
                
                {
                    xtype: 'textareafield',
                    rows: 5,
                    margin: '7 30 3 30',
                    labelAlign: 'left',
                    name: 'desc',
                    fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),//说明
                    maxLength: 4000,
                    columnWidth: .5
                }
                
                ]
            }],

            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "true") {
                    	//编辑时加载form数据
                        me.formLoad();
                    }

                }
            }

        });

        me.callParent(arguments);

    },
    /**
     * 加载form数据
     */
    formLoad: function () {
        var me = this;
        me.form.waitMsgTarget = true;
        me.form.load({
            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/kpi/kpistrategymap/findsmbyidtojson.f',
            params: {
                id: me.smid
            },
            success: function (form, action) {
                var multiSelect = action.result.multiSelect;
                if (multiSelect.otherDim) {
                	//给辅助纬度赋值
                    var arr = Ext.JSON.decode(multiSelect.otherDim);
                    Ext.getCmp('sm_otherDimMultiCombo'+sm_paramdc).setValue(arr);
                }
                if (multiSelect.otherTheme) {
                	//给辅助战略主题赋值
                    var arr = Ext.JSON.decode(multiSelect.otherTheme);
                    Ext.getCmp('sm_otherTheme'+sm_paramdc).setValue(arr);
                }
                
                var vobj = form.getValues();
                if (vobj.charttypehidden != "") {
                    var charttypearr = vobj.charttypehidden.split(',');
                    //给图表类型赋值
                    Ext.getCmp("sm_charttype"+sm_paramdc).setValue(charttypearr);
                }
                return true;
                
            },
            failure: function (form, action) {}
        });

    }





});