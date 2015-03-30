/**
 * 添加指标类型时,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.kpi.opt.kpiTypeBasicPanel', {
    extend: 'Ext.form.Panel',
    /**
	 * @cfg {boolean} 是否显示边框
	 */
    border: false,
    /**
     * 指标类型编码生成
     */
    createKpiTypeCode: function () {
        var me = this;
        var paraobj = {
            id: me.currentId
        };
        var vform = me.getForm();
        FHD.ajax({
            url: __ctxPath + '/kpi/kpi/findkpitypecode.f',
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
    },
    /**
     * 指标类型基本信息提交方法点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel,finishflag) {
        var me = this;
        var form = me.getForm();
        var vobj = form.getValues();
        var paramObj = {};
        paramObj.name = vobj.name;
        paramObj.code = vobj.code;
        paramObj.type = "KC";

        if (form.isValid()) {
            FHD.ajax({
                url: __ctxPath + '/kpi/kpi/validate.f',
                params: {
                    id: currentId,
                    validateItem: Ext.JSON.encode(paramObj)
                },
                callback: function (data) {
                    if (data && data.success) {
                        //提交指标信息
                        var addUrl = __ctxPath + '/kpi/kpi/mergekpitype.f?id=' + currentId;
                        FHD.submit({
                            form: form,
                            url: addUrl,
                            callback: function (data) {
                                if (data) {
                                    currentId = data.id;
                                    //fhd_kpi_kpitypetree_view.refreshTree();
                                    if(!finishflag){//如果点击的不是完成按钮,需要移动到下一个面板
                                    	cardPanel.pageMove("next");
                                   	 	kpiTypeMainPanel._navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                   	 	/*同时将采集频率按钮为可用状态*/
                                        Ext.getCmp('caculate_btn'+param_dc).setDisabled(false);
                                        Ext.getCmp('caculate_btn_top'+param_dc).setDisabled(false);
                                        /*给公式赋值,公式编辑器需要设置它的targetid和targetnametargetid为指标ID,targetname为指标名称*/
                                        kpitypeValueToFormula(data.id,vobj.name);
                                    }else{
                                    	kpiTypeMainPanel._gotopage();
                                    }
                                    if(me.editflag=='false'){//添加节点
                                        var node = {id:currentId,text:vobj.name,dbid:currentId,leaf:true,type:'kpitype'};
                                        if(fhd_kpi_kpitypetree_view.currentNode.isLeaf()){
                                        	fhd_kpi_kpitypetree_view.currentNode.data.leaf = false;
                                        }
                                        fhd_kpi_kpitypetree_view.currentNode.appendChild(node);
                                        fhd_kpi_kpitypetree_view.currentNode.expand();
                                        fhd_kpi_kpitypetree_view.tree.getSelectionModel().select(fhd_kpi_kpitypetree_view.currentNode.lastChild);
                                      }else{//编辑节点
                                      	//需要替换节点名称
                                      }
                                }
                            }
                        });
                    } else {
                        //校验失败信息
                        if (data && data.error == "nameRepeat") {
                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                            return;
                        }
                        if (data && data.error == "codeRepeat") {
                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.coderepeat"));
                            return;
                        }
                    }
                }
            });
        }
    },
    /**
     * 初始化组件方法
     */
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            autoScroll: true,
            border: me.border,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3",
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                autoHeight:true,
                autoWidth:true,
                collapsible: true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 105
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
                items: [{
                    xtype: 'hidden',
                    hidden: true,
                    name: 'id',
                    value: me.currentId,
                    id: 'kpitype_id'
                }, {
                    xtype: 'textareafield',
                    labelAlign: 'left',
                    rows: 3,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>',//名称
                    name: 'name',
                    maxLength: 255,
                    columnWidth: .5,
                    allowBlank: false
                }, {
                    xtype: 'textareafield',
                    rows: 3,
                    labelAlign: 'left',
                    name: 'desc',
                    fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),//说明
                    maxLength: 2000,
                    columnWidth: .5
                }, {
                    xtype: 'textfield',
                    name: 'shortName',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'),//短名称
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                },
                
                {
                	xtype: 'numberfield',
                    step: 1,
                    name: 'sort',
                    minValue:0,
                    fieldLabel: FHD.locale.get('fhd.sys.icon.order'),//序号
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                },
                
                {
                    xtype: 'textfield',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'),//编码
                    margin: '7 3 3 30',
                    name: 'code',
                    maxLength: 255,
                    columnWidth: .4
                }, {
                    xtype: 'button',
                    margin: '7 30 3 3',
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),//自动生成按钮
                    columnWidth: .1,
                    handler: function () {
                        me.createKpiTypeCode();
                    }
                },
              
                
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>',//所属部门
                    labelAlign: 'left',
                    columnWidth: .5,
                    name: 'ownDept',
                    multiSelect: false,
                    type: 'dept_emp',
                    labelWidth: 105,
                    allowBlank: false
                }),
               
                
                /*{
                    xtype: 'textfield',
                    name: 'null',
                    value: '',
                    maxLength: 100,
                    columnWidth: .5,
                    hideMode: "visibility",
                    hidden: true
                },*/
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.gatherdept') ,//采集部门
                    labelAlign: 'left',
                    columnWidth: .5,
                    name: 'gatherDept',
                    multiSelect: false,
                    type: 'dept_emp',
                    labelWidth: 105
                }), Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetdept'),//目标部门
                    labelAlign: 'left',
                    columnWidth: .5,
                    name: 'targetDept',
                    multiSelect: false,
                    type: 'dept_emp',
                    labelWidth: 105
                }), Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.reportdept'),//报告部门
                    growMax: 60,
                    labelAlign: 'left',
                    columnWidth: .5,
                    name: 'reportDept',
                    multiSelect: true,
                    type: 'dept_emp',
                    maxHeight: 60,
                    labelWidth: 105
                }), Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.viewdept'),//查看部门
                    growMax: 60,
                    labelAlign: 'left',
                    columnWidth: .5,
                    name: 'viewDept',
                    multiSelect: true,
                    type: 'dept_emp',
                    maxHeight: 60,
                    labelWidth: 105
                })]
            }, {
                xtype: 'fieldset',//相关信息fieldset
                autoHeight:true,
                autoWidth:true,
                collapsible: true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 105
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.kpi.kpi.form.assinfo'),//相关信息
                items: [{
                    xtype: 'dictradio',
                    labelWidth: 105,
                    name: 'statusStr',
                    columns: 4,
                    dictTypeId: '0yn',
                    fieldLabel: FHD.locale.get('fhd.common.enable'),//是否启用
                    defaultValue: '0yn_y',
                    labelAlign: 'left',
                    allowBlank: false,
                    columnWidth: .5
                }, 
                {
                    xtype: 'dictradio',
                    labelWidth: 105,
                    name: 'monitorStr',
                    columns: 4,
                    dictTypeId: '0yn',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.ismonitor'),//是否监控
                    defaultValue: '0yn_y',
                    labelAlign: 'left',
                    columnWidth: .5
                },
                
                {
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    name: 'startDateStr',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.startdate') + '<font color=red>*</font>',//开始日期
                    columnWidth: .5,
                    allowBlank: false
                },
                Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    multiSelect: false,
                    name: 'unitsStr',
                    dictTypeId: '0units',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.unit'),//单位
                    columnWidth: .5,
                    labelAlign: 'left',
                    labelWidth: 105
                }), 
                Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    labelWidth: 105,
                    multiSelect: false,
                    name: 'typeStr',
                    dictTypeId: 'kpi_etype',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'),//指标类型
                    columnWidth: .5,
                    labelAlign: 'left',
                    defaultValue:'kpi_etype_positive'
                }), Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    labelWidth: 105,
                    multiSelect: false,
                    name: 'kpiTypeStr',
                    dictTypeId: 'kpi_kpi_type',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.type'),//指标性质
                    columnWidth: .5,
                    labelAlign: 'left',
                    defaultValue:'kpi_kpi_type_assessment'
                }), Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    labelWidth: 105,
                    multiSelect: false,
                    name: 'alarmMeasureStr',
                    dictTypeId: 'kpi_alarm_measure',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmMeasure'),//亮灯依据
                    columnWidth: .5,
                    labelAlign: 'left',
                    defaultValue:'kpi_alarm_measure_score'
                }), Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    labelWidth: 105,
                    multiSelect: false,
                    name: 'alarmBasisStr',
                    dictTypeId: 'kpi_alarm_basis',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmBasis'),//预警依据
                    columnWidth: .5,
                    labelAlign: 'left',
                    defaultValue:'kpi_alarm_basis_forecast'
                }), Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    labelWidth: 105,
                    multiSelect: false,
                    name: 'mainDim',
                    dictTypeId: 'kpi_dimension',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'),//主纬度
                    columnWidth: .5,
                    labelAlign: 'left'
                }),
                Ext.create('FHD.ux.dict.DictSelect', {
                    maxHeight: 70,
                    labelWidth: 105,
                    name: 'otherDim',
                    dictTypeId: 'kpi_dimension',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'),//辅助纬度
                    columnWidth: .5,
                    labelAlign: 'left',
                    multiSelect: true,
                    id: 'otherDimSelect'+param_dc
                }), {
                    xtype: 'textfield',
                    name: 'targetValueAlias',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias'),//目标值别名
                    value: '',
                    maxLength: 255,
                    columnWidth: .5,
                    value:FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias')
                }, {
                    xtype: 'textfield',
                    name: 'resultValueAlias',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias'),//实际值别名
                    value: '',
                    maxLength: 255,
                    columnWidth: .5,
                    value:FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias')
                }]
            }],

            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag=="true") {
                    	/**
                    	 * 编辑时加载form数据
                    	 */
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
            url: __ctxPath + '/kpi/Kpi/findKpiByIdToJson.f',
            params: {
                id: me.currentId
            },
            success: function (form, action) {
                var otherDimArray = action.result.data.otherDimArray;
                if (otherDimArray) {
                    var arr = Ext.JSON.decode(otherDimArray);
                    //给辅助纬度赋值
                    Ext.getCmp("otherDimSelect"+param_dc).setValue(arr);
                }
                return true;
            },
            failure: function (form, action) {
            }
        });
    }



});