Ext.define('FHD.view.kpi.kpi.KpiBasicForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.kpibasicform',

    requires: [
        'FHD.view.kpi.kpi.KpiNameSelector',
        'FHD.view.kpi.kpi.KpiTypeSelector'],
    border: false,
    
    paramObj:{kpiId:'',selecttypeflag:'',kpitypeid:'',kpitypename:''},
    
    clearParamObj:function(){
    	var me = this;
    	me.paramObj = {kpiId:'',selecttypeflag:'',kpitypeid:'',kpitypename:''};
    },
    
    clearFormData:function(){
    	var me = this;
    	me.getForm().reset();
    	if(Ext.getCmp('kpi_owndept')){
    		Ext.getCmp('kpi_owndept').clearValues();
    	}
    	if(Ext.getCmp('kpi_gatherdept')){
    		Ext.getCmp('kpi_gatherdept').clearValues();
    	}
    	if(Ext.getCmp('kpi_targetdept')){
    		Ext.getCmp('kpi_targetdept').clearValues();
    	}
    	if(Ext.getCmp('kpi_reportdept')){
    		Ext.getCmp('kpi_reportdept').clearValues();
    	}
    	if(Ext.getCmp('kpi_viewdept')){
    		Ext.getCmp('kpi_viewdept').clearValues();
    	}
    },
    
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel, finishflag) {
        var me = this;
        var form = me.getForm();
        Ext.getCmp('kpiname').setDisabled(false);
        var scorecardtab = Ext.getCmp('scorecardtab');
        var kpimainpanel = Ext.getCmp('kpimainpanel');
        
        
        var vobj = form.getValues();
        var paramObj = {};
        paramObj.name = vobj.name;
        paramObj.code = vobj.code;
        paramObj.namedefault = vobj.namedefault;
        paramObj.categoryname = scorecardtab.paramObj.categoryname;
        paramObj.kpitypename = me.paramObj.kpitypename;
        paramObj.type = "KPI";
        parentKpiId = Ext.getCmp("kpi_parentKpiID").getFieldValue();
        form.setValues({
        	opflag:kpimainpanel.paramObj.editflag==true?'edit':'add',
        	categoryId:scorecardtab.paramObj.categoryid
        });
        if(!form.isValid()){
        	return false;
        }
        FHD.ajax({
            url: __ctxPath + '/kpi/kpi/validate.f',//校验信息,名称和编码是否重复
            params: {
                id: me.paramObj.kpiId,
                validateItem: Ext.JSON.encode(paramObj)
            },
            callback: function (data) {
                if (data && data.success ) {
                    //提交指标信息
                    var addUrl = __ctxPath + '/kpi/kpi/mergekpi.f?id=' + me.paramObj.kpiId+"&parentid="+parentKpiId;
                    /*
                     * 保存指标信息
                     */
                    FHD.submit({
                        form: form,
                        url: addUrl,
                        callback: function (data) {
                            if (data) {
                            	var kpicardpanel =  Ext.getCmp("kpicardpanel");
                                me.paramObj.kpiId = data.id;
                                me.paramObj.kpiname = vobj.name;
                                
                                if (!finishflag) {//如果点击的不是完成按钮,需要移动到下一个面板
                                	cardPanel.lastSetBtnState(cardPanel,cardPanel.getActiveItem());
                                    cardPanel.pageMove("next");
                                    kpicardpanel.navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                    //同时将采集频率按钮为可用状态
                                    Ext.getCmp('kpi_kpi_caculate_btn' ).setDisabled(false);
                                    Ext.getCmp('kpi_kpi_caculate_btn_top' ).setDisabled(false);
                                    
                                    //给公式赋值,公式编辑器需要设置它的targetid和targetnametargetid为指标ID,targetname为指标名称
                                    me.valueToFormulaName();
                                }else{
                                	kpicardpanel.gotopage();
                                }
                                
                            }
                        }
                    });
                } else {
                    //校验失败信息
                    if (data && data.error == "nameRepeat") {//名称重复
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                        var defaultDict = Ext.getCmp('kpinameselector').isDefaultDictRadio;
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
    

    valueToFormulaName:function(){
    	var me = this;
    	var resultformula = Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector' );
        var targetformula = Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' );
        var assessmentformula = Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' );
       // var alarmFormula = Ext.getCmp('kpi_kpi_kpitypealarmFormulaSelector' );
       // var relationFormula = Ext.getCmp('kpi_kpi_kpityperelationFormulaSelector' );
        
        var kpiid = me.paramObj.kpiId;
        var kpiname = me.paramObj.kpiname;
        resultformula.setTargetId(kpiid);
        resultformula.setTargetName(kpiname);
        targetformula.setTargetId(kpiid);
        targetformula.setTargetName(kpiname);
        assessmentformula.setTargetId(kpiid);
        assessmentformula.setTargetName(kpiname);	
        /*alarmFormula.setTargetId(kpiid);
        alarmFormula.setTargetName(kpiname);	
        relationFormula.setTargetId(kpiid);
        relationFormula.setTargetName(kpiname);	*/
    },
    
    /**
     * 选择指标类型的按钮事件
     */
    selectKpiType: function () {
        var me = this;
        me.selectKpiTypeSelector.reload();
        me.formwindow.show();

    },
    /**
     * 编码自动生产按钮事件
     */
    createCode: function () {
    	parentKpiId = "kpi_root";
    	if(parentKpiId){
    		var me = this;
            var vform = me.getForm();
            var vobj = vform.getValues();
            var paraobj = {};
            paraobj.id = me.paramObj.kpiId;
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
    	}
    },
    /**
     * form表单中添加控件
     */
    addComponent: function () {
        var me = this;
        var basicfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
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
                name: 'opflag'
               ,value: ''//提交时需要赋值
            }, {
                xtype: 'hidden',
                hidden: true,
                name: 'id'
                ,id: 'kpi_id'
            }, {
                xtype: 'hidden',
                hidden: true,
                name: 'categoryId'
               ,value: ''//提交时需要赋值
            }]
        });
        //指标类型
        var kpitype = Ext.widget('textfield', {
            xtype: 'textfield',
            disabled: true,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'), //指标类型
            margin: '7 3 3 30',
            name: 'kpitypename',
            maxLength: 255,
            columnWidth: .4
        });
        basicfieldSet.add(kpitype);

        //指标类型选择按钮
        var selectBtn = Ext.widget('button', {
            xtype: 'button',
            margin: '7 30 3 3',
            text: FHD.locale.get('fhd.common.select'), //指标类型选择按钮
            handler: function () {
                me.selectKpiType();//指标按钮事件
            },
            columnWidth: .1
        });
        basicfieldSet.add(selectBtn);

        //是否继承
        var isHerit = Ext.widget('dictradio', {
            id: 'isherit',
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'isInheritStr',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.herit'), //是否继承
            defaultValue: '0yn_y',
            labelAlign: 'left',
            columnWidth: .5,
            listeners: {
                click: {
                    element: 'el',
                    fn: function () {
                        var isherit = Ext.getCmp('isherit');

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
        });

        basicfieldSet.add(isHerit);

        //默认名称
        var defaultname = Ext.widget('kpinameselector', {
            xtype: 'kpinameselector',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.defaultname') + '<font color=red>*</font>', //默认名称
            textfieldname: 'name',
            is_default: 'namedefault',
            columnWidth: .5,
            id: 'kpinameselector'
        });

        basicfieldSet.add(defaultname);

        //描述
        var desc = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 3,
            labelAlign: 'left',
            name: 'desc',
            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'), //描述
            maxLength: 2000,
            columnWidth: .5
        });
        basicfieldSet.add(desc);
        //短名称
        var shortname = Ext.widget('textfield', {
            xtype: 'textfield',
            name: 'shortName',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'), //短名称
            value: '',
            maxLength: 255,
            columnWidth: .5
        });
        basicfieldSet.add(shortname);
        //编码
        var code = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'), //编码
            margin: '7 3 3 30',
            name: 'code',
            maxLength: 255,
            columnWidth: .4
        });
        basicfieldSet.add(code);

        //自动生成按钮
        var codeBtn = Ext.widget('button', {
            xtype: 'button',
            margin: '7 30 3 3',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'), //自动生成按钮
            handler: function () {
                me.createCode();//自动生成按钮事件
            },
            columnWidth: .1
        });
        basicfieldSet.add(codeBtn);
        //上级指标
        var kpioptselector = Ext.create('FHD.ux.kpi.opt.KpiSelector', {
            id: 'kpi_parentKpiID',
            name: 'parentKpiId',
            multiSelect: false,
            columnWidth: .5,
            gridHeight: 25,
            btnHeight: 25,
            labelText: FHD.locale.get('fhd.kpi.kpi.form.parentKpi'), //上级指标
            labelAlign: 'left',
            labelWidth: 101
        });

        basicfieldSet.add(kpioptselector);
        //所属部门
        me.owndept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>', //所属部门
            labelAlign: 'left',
            id:'kpi_owndept',
            columnWidth: .5,
            name: 'ownDept',
            multiSelect: false,
            type: 'dept_emp',
            labelWidth: 95,
            allowBlank: false
        })
        basicfieldSet.add(me.owndept);

       /* var hi = Ext.widget('textfield', {
            xtype: 'textfield',
            name: 'null',
            value: '',
            maxLength: 100,
            columnWidth: .5,
            hideMode: "visibility",
            hidden: true
        });
        basicfieldSet.add(hi);*/
        //采集部门
        me.gatherDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.gatherdept'), //采集部门
            labelAlign: 'left',
            columnWidth: .5,
            name: 'gatherDept',
            id:'kpi_gatherdept',
            multiSelect: false,
            type: 'dept_emp',
            labelWidth: 95
        });
        basicfieldSet.add(me.gatherDept);
        //目标部门
        me.targetdept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetdept'), //目标部门
            labelAlign: 'left',
            columnWidth: .5,
            name: 'targetDept',
            multiSelect: false,
            type: 'dept_emp',
            id:'kpi_targetdept',
            labelWidth: 95
        });
        basicfieldSet.add(me.targetdept);

        //报告部门
        me.reportdept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.reportdept'), //报告部门
            growMax: 60,
            labelAlign: 'left',
            columnWidth: .5,
            name: 'reportDept',
            multiSelect: true,
            type: 'dept_emp',
            id:'kpi_reportdept',
            margin: '0 12 0 30',
            maxHeight: 60,
            labelWidth: 95
        });
        basicfieldSet.add(me.reportdept);
        //查看部门
        me.viewdept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.viewdept'), //查看部门
            growMax: 60,
            labelAlign: 'left',
            columnWidth: .5,
            name: 'viewDept',
            multiSelect: true,
            type: 'dept_emp',
            margin: '0 12 0 30',
            id:'kpi_viewdept',
            maxHeight: 60,
            labelWidth: 95
        });
        basicfieldSet.add(me.viewdept);

        me.add(basicfieldSet);

        var relafieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //相关信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            defaults: {
                margin: '7 30 3 30',
                labelWidth: 105
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.assinfo') //相关信息
        });
        //是否启用
        var status = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'statusStr',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: FHD.locale.get('fhd.common.enable'), //是否启用
            defaultValue: '0yn_y',
            labelAlign: 'left',
            allowBlank: false,
            columnWidth: .5
        });
        relafieldSet.add(status);

        var monitor = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'monitorStr',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.ismonitor'), //是否监控
            defaultValue: '0yn_y',
            labelAlign: 'left',
            columnWidth: .5
        });


        relafieldSet.add(monitor);
        //单位
        var units = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            multiSelect: false,
            name: 'unitsStr',
            dictTypeId: '0units',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.unit'), //单位
            columnWidth: .5,
            labelAlign: 'left',
            labelWidth: 105
        });
        relafieldSet.add(units);
        //开始日期
        var startDate = Ext.widget('datefield', {
            xtype: 'datefield',
            format: 'Y-m-d',
            name: 'startDateStr',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.startdate') + '<font color=red>*</font>', //开始日期
            columnWidth: .5,
            allowBlank: false
        });

        relafieldSet.add(startDate);

        //指标类型
        var type = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 105,
            multiSelect: false,
            name: 'typeStr',
            dictTypeId: 'kpi_etype',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'), //指标类型
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue: 'kpi_etype_positive'
        });
        relafieldSet.add(type);
        //指标性质
        var kpiType = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 100,
            multiSelect: false,
            name: 'kpiTypeStr',
            dictTypeId: 'kpi_kpi_type',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.type'), //指标性质
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue: 'kpi_kpi_type_assessment'
        });
        relafieldSet.add(kpiType);

        //亮灯依据
        var alarmMeasure = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 105,
            multiSelect: false,
            name: 'alarmMeasureStr',
            dictTypeId: 'kpi_alarm_measure',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmMeasure'), //亮灯依据
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue: 'kpi_alarm_measure_score'
        });
        relafieldSet.add(alarmMeasure);

        //预警依据
        var alarmBasis = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 100,
            multiSelect: false,
            name: 'alarmBasisStr',
            dictTypeId: 'kpi_alarm_basis',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmBasis'), //预警依据
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue: 'kpi_alarm_basis_forecast'
        });
        relafieldSet.add(alarmBasis);
        //主纬度
        var mainDim = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 105,
            multiSelect: false,
            name: 'mainDim',
            dictTypeId: 'kpi_dimension',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'), //主纬度
            columnWidth: .5,
            labelAlign: 'left'
        });
        relafieldSet.add(mainDim);
        //辅助纬度
        var otherDim = Ext.create('FHD.ux.dict.DictSelect', {
            maxHeight: 60,
            labelWidth: 100,
            name: 'otherDim',
            dictTypeId: 'kpi_dimension',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'), //辅助纬度
            columnWidth: .5,
            labelAlign: 'left',
            multiSelect: true,
            id: 'otherDimMultiCombo'
        });
        relafieldSet.add(otherDim);

        //目标值别名
        var targetValueAlias = Ext.widget('textfield', {
            xtype: 'textfield',
            name: 'targetValueAlias',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias'), //目标值别名
            maxLength: 255,
            columnWidth: .5,
            value: FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias'),
            labelWidth: 105
        });
        relafieldSet.add(targetValueAlias);
        //实际值别名
        var resultValueAlias = Ext.widget('textfield', {
            xtype: 'textfield',
            name: 'resultValueAlias',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias'), //实际值别名
            maxLength: 255,
            columnWidth: .5,
            value: FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias')
        });
        relafieldSet.add(resultValueAlias);
        me.add(relafieldSet);

    },

    // 初始化方法
    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            autoScroll: true,
            border: me.border,
            autoHeight: true,
            layout: 'column',
            height: FHD.getCenterPanelHeight() - 75,
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3"
        });
        
        me.selectKpiTypeSelector = Ext.widget('kpitypeselector');
        
        me.formwindow = new Ext.Window({
            constrain: true,
            layout: 'fit',
            iconCls: 'icon-edit', //标题前的图片
            modal: true, //是否模态窗口
            collapsible: true,
            scroll: 'auto',
            closeAction:'hide',
            width: 400,
            height: 630,
            maximizable: true, //是否增加最大化，默认没有
            items: [me.selectKpiTypeSelector]
        });
        me.callParent(arguments);

        //向form表单中添加控件
        me.addComponent();
        
        var kpinameselector = Ext.getCmp('kpinameselector');
        
        kpinameselector.isDefaultDictRadio.on("change",function(t, newValue, oldValue, options ){
  			if(!newValue[kpinameselector.is_default]){
  				kpinameselector.textfield.setDisabled(true);
  			}else if(newValue[kpinameselector.is_default]=="0yn_y"){
  				kpinameselector.textfield.setDisabled(true);
  				var name = kpinameselector.textfield.getValue();
  				if(!Ext.getCmp('kpimainpanel').paramObj.editflag){
  					var categoryname = Ext.getCmp('scorecardtab').paramObj.categoryname;
  	  				if(name.indexOf(categoryname)==-1&&name.length!=0){
  	  					kpinameselector.textfield.setValue(categoryname+" "+name);
  	  				}
  				}
  				
  			}else if(newValue[kpinameselector.is_default]=="0yn_n"){
  				kpinameselector.textfield.setDisabled(false);
  			}
        });

    },
    /**
     * 初始化默认值
     */
    initFormData:function(){
    	var me = this;
    	me.getForm().setValues(
    			{
    				monitorStr:'0yn_y',
    				statusStr:'0yn_y',
    				isInheritStr:'0yn_y',
    				typeStr:'kpi_etype_positive',
    				kpiTypeStr:'kpi_kpi_type_assessment',
    				alarmMeasureStr:'kpi_alarm_measure_score',
    				alarmBasisStr:'kpi_alarm_basis_forecast'
    				
    			});
    	Ext.getCmp('kpinameselector').isDefaultDictRadio.setValue('0yn_y');
    	Ext.getCmp("kpi_parentKpiID").initGridStore("kpi_root");
    },
    
    /**
     * 加载form数据
     */
    formLoad: function () {
        var me = this;
        //清除数据
        //基本信息面板
        me.clearFormData();
        //采集结果面板
        Ext.getCmp('kpigatherform').clearFormData();
        var vform = me.getForm();
        var id = me.paramObj.kpiId;
        if (me.paramObj.selecttypeflag) {
            //说明是选择指标类型后,的form加载
            id = me.paramObj.kpitypeid; //指标类型ID
            //Ext.getCmp('kpinameselector').isDefaultDictRadio.setValue('0yn_y');
            vform.setValues({
                kpitypename: me.paramObj.kpitypename,//指标类型名称
                kpitypeid: id,
                isInheritStr:'0yn_y'
            }); 
        }
        me.form.waitMsgTarget = true;//设置等待图标
        //根据指标类型ID加载kpibasicform数据
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
                var otherDimMultiCombo = Ext.getCmp('otherDimMultiCombo');
                var otherDimArray = action.result.data.otherDimArray;
                if (otherDimArray) {
                    var arr = Ext.JSON.decode(otherDimArray);
                    otherDimMultiCombo.setValue(arr);
                }
                //给指标选择控件赋值
                Ext.getCmp("kpi_parentKpiID").initGridStore(parentKpiId);
                if (me.paramObj.selecttypeflag) {
                	var vform = me.getForm();
                    vform.setValues({
                    	code:'',
                    	name: Ext.getCmp('scorecardtab').paramObj.categoryname + " " + me.paramObj.kpitypename//选择指标类型后,名称显示为记分卡名称+' '+指标类型名称
                    }); 
                    Ext.getCmp('kpinameselector').isDefaultDictRadio.setValue('0yn_y');
                }
                
                return true;
            }
        });
        
        //根据指标类型ID加载kpigatherform
        Ext.getCmp('kpigatherform').loadFormById(id);
        //根据指标类型ID加载kiwarningset列表数据
        Ext.getCmp('kpiwarningset').reLoadGridById(id,true);
    }



});