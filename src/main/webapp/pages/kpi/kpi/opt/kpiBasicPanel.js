
/**
 * 添加指标时,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */

Ext.define('Ext.kpi.kpi.opt.kpiBasicPanel', {
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
    last: function (cardPanel, finishflag) {
        var me = this;
        var form = me.getForm();
        Ext.getCmp('kpiname').setDisabled(false);
       
        var vobj = form.getValues();
        var paramObj = {};
        paramObj.name = vobj.name;
        paramObj.code = vobj.code;
        paramObj.categoryname = categoryname;
        paramObj.type = "KPI";
        
        parentKpiId = Ext.getCmp("kpi_parentKpiID"+kpi_kpi_paramdc).getFieldValue();
        
        FHD.ajax({
            url: __ctxPath + '/kpi/kpi/validate.f',//校验信息,名称和编码是否重复
            params: {
                id: me.kpiId,
                validateItem: Ext.JSON.encode(paramObj)
            },
            callback: function (data) {
                if (data && data.success && form.isValid()) {
                    //提交指标信息
                    var addUrl = __ctxPath + '/kpi/kpi/mergekpi.f?id=' + me.kpiId+"&parentid="+parentKpiId;
                    /**
                     * 保存指标信息
                     */
                    FHD.submit({
                        form: form,
                        url: addUrl,
                        callback: function (data) {
                            if (data) {
                                me.kpiId = data.id;
                                if (!finishflag) {//如果点击的不是完成按钮,需要移动到下一个面板
                                    cardPanel.pageMove("next");
                                    kpiMainPanel._navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                    /*同时将采集频率按钮为可用状态*/
                                    Ext.getCmp('kpi_kpi_caculate_btn' + kpi_kpi_paramdc).setDisabled(false);
                                    Ext.getCmp('kpi_kpi_caculate_btn_top' + kpi_kpi_paramdc).setDisabled(false);
                                    
                                    /*给公式赋值,公式编辑器需要设置它的targetid和targetnametargetid为指标ID,targetname为指标名称*/
                                    kpiValueToFormula(me.kpiId,vobj.name);
                                }else{
                                	kpiMainPanel._gotopage();
                                }
                                
                            }
                        }
                    });
                } else {
                    //校验失败信息
                    if (data && data.error == "nameRepeat") {//名称重复
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                        var defaultDict = Ext.getCmp('kpinameselector'+kpi_kpi_paramdc).isDefaultDictRadio;
                        defaultDict.setValue("0yn_n");
                        return;
                    }
                    if (data && data.error == "codeRepeat") {//编码重复
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.coderepeat"));
                        return;
                    }
                }
            }
        });
    },
    
    /**
     * 选择指标类型的按钮事件
     */
    selectKpiType: function () {
        var me = this;
        me.formwindow = new Ext.Window({
            constrain: true,
            layout: 'fit',
            iconCls: 'icon-edit', //标题前的图片
            modal: true, //是否模态窗口
            collapsible: true,
            scroll: 'auto',
            width: 400,
            height: 630,
            maximizable: true, //是否增加最大化，默认没有
            autoLoad: {
                url: 'pages/kpi/kpi/opt/kpitypeselect.jsp',
                nocache: true,
                scripts: true
            }
        });
        me.formwindow.show();

    },
    
    /**
     * 编码自动生产按钮事件
     */
    createCode: function () {
    	var me = this;
        var vform = me.getForm();
        var vobj = vform.getValues();
        var paraobj = {};
        paraobj.id = me.kpiId;
        paraobj.parentid = parentKpiId;
        FHD.ajax({
            url: __ctxPath + '/kpi/kpi/findcodebyparentid.f',
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
                collapsible: true,
                autoHeight:true,
                autoWidth:true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 105
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
                items: [

                {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'kpitypeid'
                }, {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'opflag',
                    value: kpi_kpi_opflag
                }, {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'id',
                    id: 'kpi_id'+kpi_kpi_paramdc
                }, {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'categoryId',
                    value: categoryid
                }, {
                    xtype: 'textfield',
                    disabled: true,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'),//指标类型
                    margin: '7 3 3 30',
                    name: 'kpitypename',
                    maxLength: 255,
                    columnWidth: .4
                }, {
                    xtype: 'button',
                    margin: '7 30 3 3',
                    text: FHD.locale.get('fhd.common.select'),//指标类型选择按钮
                    handler: function () {
                        me.selectKpiType();//指标按钮事件
                    },
                    columnWidth: .1
                }, 
                {
                	id: 'isherit'+kpi_kpi_paramdc,
                    xtype: 'dictradio',
                    labelWidth: 105,
                    name: 'isInheritStr',
                    columns: 4,
                    dictTypeId: '0yn',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.herit'),//是否继承
                    defaultValue: '0yn_y',
                    labelAlign: 'left',
                    columnWidth: .5,
                    listeners: {
                        click: {
                            element: 'el',
                            fn: function () {
                                var isherit = Ext.getCmp('isherit'+kpi_kpi_paramdc);

                                for (var i = 0; i < isherit.items.length; i++) {
                                    if (isherit.items.items[i].checked) {
                                        if (isherit.items.items[i].inputValue == '0yn_n') {

                                        } else if (isherit.items.items[i].inputValue == '0yn_y') {

                                        }
                                    }
                                }

                            }
                        }
                    }
                },
                
                 {
					xtype:'kpinameselector',
					fieldLabel:FHD.locale.get('fhd.strategymap.strategymapmgr.form.defaultname') + '<font color=red>*</font>',//默认名称
					textfieldname:'name',
					is_default:'namedefault',
					columnWidth: .5,
					id:'kpinameselector'+kpi_kpi_paramdc
        	     },
                
                 {
                    xtype: 'textareafield',
                    rows: 3,
                    labelAlign: 'left',
                    name: 'desc',
                    fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),//描述
                    maxLength: 2000,
                    columnWidth: .5
                }, 
                
                {
                    xtype: 'textfield',
                    name: 'shortName',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'),//短名称
                    value: '',
                    maxLength: 255,
                    columnWidth: .5
                }, {
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
                    handler: function () {
                        me.createCode();//自动生成按钮事件
                    },
                    columnWidth: .1
                },
                
                {
                    id: 'kpi_parentKpiID'+kpi_kpi_paramdc,
                    xtype: 'kpioptselector',
                    name: 'parentKpiId',
                    multiSelect:false,
                    columnWidth:.5,
				 	gridHeight:25,
				 	btnHeight:25,
                    labelText: FHD.locale.get('fhd.kpi.kpi.form.parentKpi'),//上级指标
                    labelAlign: 'left',
                    columnWidth: .5
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
                }), {
                    xtype: 'textfield',
                    name: 'null',
                    value: '',
                    maxLength: 100,
                    columnWidth: .5,
                    hideMode: "visibility",
                    hidden: true
                },
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.gatherdept'),//采集部门
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
                })

                ]
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
               
                Ext.create('widget.dictselectforeditgrid', {
                    editable: false,
                    multiSelect: false,
                    name: 'unitsStr',
                    dictTypeId: '0units',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.unit'),//单位
                    columnWidth: .5,
                    labelAlign: 'left',
                    labelWidth: 105
                }), {
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    name: 'startDateStr',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.startdate') + '<font color=red>*</font>',//开始日期
                    columnWidth: .5,
                    allowBlank: false
                },
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
                    maxHeight: 60,
                    labelWidth: 105,
                    name: 'otherDim',
                    dictTypeId: 'kpi_dimension',
                    fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'),//辅助纬度
                    columnWidth: .5,
                    labelAlign: 'left',
                    multiSelect: true,
                    id: 'otherDimMultiCombo'+kpi_kpi_paramdc,
                }), {
                    xtype: 'textfield',
                    name: 'targetValueAlias',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias'),//目标值别名
                    maxLength: 255,
                    columnWidth: .5,
                    value:FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias')
                }, {
                    xtype: 'textfield',
                    name: 'resultValueAlias',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias'),//实际值别名
                    maxLength: 255,
                    columnWidth: .5,
                    value:FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias')
                }

                ]
            }

            ],

            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "true") {
                    	//编辑时加载form数据
                        me.formLoad();
                    } else {
                        //如果为添加指标,则初始化根指标为根节点
                    	Ext.getCmp("kpi_parentKpiID"+kpi_kpi_paramdc).initGridStore("kpi_root");
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
        var id = me.kpiId;
        if (me.selecttypeflag) {
            //说明是选择指标类型后,的form加载
            id = me.kpitypeid; //指标类型ID
            var vform = me.getForm();
            vform.setValues({
                kpitypename: me.kpitypename,//指标类型名称
                kpitypeid: id
            }); 
        }
        me.form.waitMsgTarget = true;//设置等待图标
        me.form.load({
            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/kpi/Kpi/findKpiByIdToJson.f',
            params: {
                id: id
            },
            /**
             * form加载数据成功后回调函数
             */
            success: function (form, action) {
                parentKpiId = action.result.data.parentKpiId;
                //如果指标的parentId为空时,说明是第一级节点,赋值为默认根节点
                if (parentKpiId == undefined) {
                    parentKpiId = "kpi_root";
                }
                //给赋值纬度赋值
                var otherDimMultiCombo = Ext.getCmp('otherDimMultiCombo'+kpi_kpi_paramdc);
                var otherDimArray = action.result.data.otherDimArray;
                if (otherDimArray) {
                    var arr = Ext.JSON.decode(otherDimArray);
                    otherDimMultiCombo.setValue(arr);
                }
                //给指标选择控件赋值
                if (parentKpiId && me.editflag == "true") {
                	Ext.getCmp("kpi_parentKpiID"+kpi_kpi_paramdc).initGridStore(parentKpiId);
                }
                return true;
            },
            failure: function (form, action) {
            }
        });
        //加载采集结果form
        kpiGatherPanel.kpiId = id;
        kpiGatherPanel.formLoad();
        //加载告警列表数据
        kpiWarningPanel.grid.store.proxy.extraParams.id = id;
        kpiWarningPanel.grid.store.proxy.extraParams.editflag = true;
        kpiWarningPanel.grid.store.load();
    }





});