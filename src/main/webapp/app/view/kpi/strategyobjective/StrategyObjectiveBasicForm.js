Ext.define('FHD.view.kpi.strategyobjective.StrategyObjectiveBasicForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.strategyobjectivebasicform',
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
	    if(!form.isValid()){
        	return false;
        }
        FHD.ajax({
            url: __ctxPath + '/kpi/kpistrategymap/validate.f',
            params: {
                name: vobj.name,
                id: me.paramObj.smid,
                code: vobj.code
            },
            callback: function (data) {
                if (data && data.success) {
                    //提交目标信息
                	//取预警公式值
                	var warningFormulaValue = Ext.getCmp('warningFormula').getValue();
                	//评估值公式
                	var assessmentFormulaValue = Ext.getCmp('sm_assessmentFormula').getValue();
                	assessmentFormulaValue = assessmentFormulaValue==null?"":assessmentFormulaValue;
                	
                    var param = Ext.JSON.encode({
                    	currentSmId: me.paramObj.smid,
                    	warningFormula:warningFormulaValue==undefined?"":warningFormulaValue,
                    	assessmentFormula:assessmentFormulaValue});
                    var addUrl = __ctxPath + '/kpi/kpistrategymap/mergestrategymap.f?param='+encodeURIComponent(param);
                    
                    FHD.submit({
                        form: form,
                        url: addUrl,
                        callback: function (data) {
                            if (data) {
                            	var strategyobjectivecardpanel = Ext.getCmp('strategyobjectivecardpanel');
                                me.paramObj.smname = vobj.name;
                                me.paramObj.smid = data.smId;
                                Ext.getCmp('strategyobjectivemainpanel').paramObj.smid = data.smId;//图表使用
                                var editflag =  me.paramObj.editflag ;
                                me.paramObj.editflag = true;
                                if(!finishflag){
                                	cardPanel.lastSetBtnState(cardPanel,cardPanel.getActiveItem());
                                	cardPanel.pageMove("next");//否则移动到下一个面板
                                	strategyobjectivecardpanel.navBtnState(cardPanel);
                                    //同时将衡量指标按钮变为激活状态
                                    Ext.getCmp('sm_kpiset_btn').setDisabled(false);
                                    Ext.getCmp('sm_kpiset_btn_top').setDisabled(false);
                                }else{
                                	strategyobjectivecardpanel.gotopage();
                                }
                                var strategyobjectivetree = Ext.getCmp('strategyobjectivetree');
                                if(!editflag){//添加节点
                                    var node = {iconCls:'icon-status-disable',id:me.paramObj.smid,text:me.paramObj.smname,dbid:me.paramObj.smid,leaf:true,type:'sm'};
                                    if(strategyobjectivetree.currentNode.isLeaf()){
                                    	strategyobjectivetree.currentNode.data.leaf = false;
                                    }
                                    strategyobjectivetree.currentNode.appendChild(node);
                                    strategyobjectivetree.currentNode.expand();
                                    strategyobjectivetree.getSelectionModel().select(strategyobjectivetree.currentNode.lastChild);
                                    strategyobjectivetree.currentNode = strategyobjectivetree.currentNode.lastChild;
                                    //刷新导航
                                    Ext.getCmp('strategyobjectivemainpanel').navigationBar.renderHtml('smtabcontainer', me.paramObj.smid, '', 'sm');
                                  }else{//编辑节点
                                  	//需要替换节点名称
                                	  var currentnode = strategyobjectivetree.currentNode;
                                	  var data = currentnode.data;
                                	  data.text = me.paramObj.smname;
                                	  currentnode.updateInfo(data);
                                	  Ext.getCmp('strategyobjectivemainpanel').navigationBar.renderHtml('smtabcontainer', me.paramObj.smid, '', 'sm');
                                  }
                                
                            }
                        }
                    });
                } else {
                    if (data && data.error == "nameRepeat") {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.nameRepeat'));
                        return false;
                    }
                    if (data && data.error == "codeRepeat") {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.codeRepeat'));
                        return false;
                    }
                    return false;
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
        paraobj.currentSmId = me.paramObj.smid;
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
     * form表单中添加控件
     */
    addComponent: function () {
        var me = this;
        me.basicfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', //基本信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            defaults: {
                margin: '3 30 3 30',
                labelWidth: 100
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.common.baseInfo'),
            items: [{
                xtype: 'hidden',
                hidden: true,
                name: 'currentSmId'
            }, {
                xtype: 'hidden',
                hidden: true,
                name: 'parentId'
            }, {
                xtype: 'hidden',
                hidden: true,
                name: 'charttypehidden'
            }]
        });

        //上级目标
        var parentname = Ext.widget('textfield', {
            id: 'smparentname',
            xtype: 'textfield',
            disabled: true,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.parent') + '<font color=red>*</font>', //上级目标
            //value: me.smparentname,
            name: 'parentname',
            maxLength: 200,
            columnWidth: .5,
            allowBlank: false
        });

        me.basicfieldSet.add(parentname);

        //编码
        var code = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'), //编码
            margin: '3 3 3 30',
            name: 'code',
            maxLength: 255,
            columnWidth: .4,
            allowBlank: true
        });
        me.basicfieldSet.add(code);

        //自动生成编码按钮
        var codebtn = Ext.widget('button', {
            xtype: 'button',
            margin: '3 30 3 3',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'), //自动生成编码按钮
            handler: function () {
                me.createCode();
            },
            columnWidth: .1
        });

        me.basicfieldSet.add(codebtn);

        //名称
        var name = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.name') + '<font color=red>*</font>', //名称
            margin: '7 30 3 30',
            name: 'name',
            maxLength: 255,
            columnWidth: .5,
            allowBlank: false
        });
        me.basicfieldSet.add(name);

        //短名称
        var shortName = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 2,
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'), //短名称
            margin: '7 30 3 30',
            name: 'shortName',
            columnWidth: .5,
            allowBlank: true,
            maxLength: 255
        });
        me.basicfieldSet.add(shortName);

        //主维度
        var mainDim = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            multiSelect: false,
            margin: '7 30 3 30',
            name: 'mainDim',
            labelWidth: 100,
            dictTypeId: 'kpi_dimension',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'), //主维度
            columnWidth: .5,
            labelAlign: 'left',
            columns: 5
        });

        me.basicfieldSet.add(mainDim);

        //战略主题
        var mainTheme = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            multiSelect: false,
            margin: '7 30 3 30',
            name: 'mainTheme',
            labelWidth: 100,
            dictTypeId: 'kpi_theme',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainTheme'), //战略主题
            columnWidth: .5,
            labelAlign: 'left',
            columns: 5
        });
        me.basicfieldSet.add(mainTheme);

        //辅助纬度
        var otherDim = Ext.create('FHD.ux.dict.DictSelect', {
            id: 'sm_otherDimMultiCombo',
            margin: '7 30 3 30',
            name: 'otherDim',
            labelWidth: 100,
            dictTypeId: 'kpi_dimension',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'), //辅助纬度
            columnWidth: .5,
            labelAlign: 'left',
            columns: 5,
            multiSelect: true
        });
        me.basicfieldSet.add(otherDim);
        //辅助战略主题
        var otherTheme = Ext.create('FHD.ux.dict.DictSelect', {
            margin: '7 30 3 30',
            name: 'otherTheme',
            id: 'sm_otherTheme',
            labelWidth: 100,
            dictTypeId: 'kpi_theme',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherTheme'), //辅助战略主题
            columnWidth: .5,
            labelAlign: 'left',
            columns: 5,
            multiSelect: true
        });
        me.basicfieldSet.add(otherTheme);

        //所属部门人员
        me.ownDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept"), //所属部门人员
            labelWidth: 95,
            labelAlign: 'left',
            margin: '0 30 0 30',
            columnWidth: .5,
            name: 'ownDept',
            multiSelect: false,
            type: 'dept_emp'
        });
        me.basicfieldSet.add(me.ownDept);

        //是否启用
        var estatus = Ext.widget('dictradio', {
            xtype: 'dictradio',
            margin: '7 30 3 30',
            name: 'estatus',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: FHD.locale.get('fhd.common.enable'), //是否启用
            defaultValue: '0yn_y',
            labelAlign: 'left',
            allowBlank: false,
            columnWidth: .5,
            id:'sm_estatus'
        });
        me.basicfieldSet.add(estatus);
        //查看人
        me.viewDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.viewEmp"), //查看人
            labelWidth: 95,
            growMax: 74,
            labelAlign: 'left',
            margin: '0 12 0 30',
            columnWidth: .5,
            name: 'viewDept',
            multiSelect: true,
            type: 'dept_emp',
            maxHeight: 68
        });
        me.basicfieldSet.add(me.viewDept);
        //预警公式
        var warningFormula = Ext.create('FHD.ux.kpi.FormulaTrigger', {
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.warningFormula'),
            hideLabel: false,
            id: 'warningFormula',
            emptyText: '',
            labelAlign: 'left',
            flex: 1.5,
            labelWidth: 100,
            cols: 20,
            margin: '7 30 3 30',
            rows: 5,
            name: 'warningFormula',
            type: 'kpi',
            showType: 'all',
            column: 'assessmentValueFormula',
            columnWidth: .5
        });
        me.basicfieldSet.add(warningFormula);
        //报告人
        me.reportDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.reportEmp"), //报告人
            labelWidth: 95,
            growMax: 74,
            labelAlign: 'left',
            margin: '0 12 0 30',
            columnWidth: .5,
            name: 'reportDept',
            multiSelect: true,
            type: 'dept_emp',
            maxHeight: 70
        });
        me.basicfieldSet.add(me.reportDept);

        //评估值公式
        var assessmentFormula = Ext.create('FHD.ux.kpi.FormulaTrigger', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula'), //评估值公式
            hideLabel: false,
            id: 'sm_assessmentFormula',
            emptyText: '',
            labelAlign: 'left',
            flex: 1.5,
            cols: 20,
            rows: 5,
            name: 'assessmentFormula',
            type: 'strategy',
            showType: 'strategyType',
            column: 'assessmentValueFormula',
            labelWidth: 100,
            columnWidth: .5
        });
        me.basicfieldSet.add(assessmentFormula);
        //图表类型
        var chartTypeStr = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
            id: 'sm_charttype',
            editable: false,
            labelWidth: 100,
            margin: '7 30 3 30',
            multiSelect: true,
            name: 'chartTypeStr',
            dictTypeId: 'strategy_map_chart_type',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.category.charttype') + '<font color=red>*</font>' , //图表类型
            columnWidth: .5,
            labelAlign: 'left',
            allowBlank: false,
            maxHeight: 70
        });
        me.basicfieldSet.add(chartTypeStr);
        //说明
        var desc = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 5,
            margin: '7 30 3 30',
            labelAlign: 'left',
            name: 'desc',
            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'), //说明
            maxLength: 4000,
            columnWidth: .5
        });
        me.basicfieldSet.add(desc);

        me.add(me.basicfieldSet);


    },

    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            autoScroll: true,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3"

        });
        me.callParent(arguments);

        //向form表单中添加控件
        me.addComponent();
    },
    /**
     * 清除form数据
     */
    clearFormData:function(){
      var me = this;
      me.getForm().reset();
      me.ownDept.clearValues();
      me.viewDept.clearValues();
      me.reportDept.clearValues();
    },
    initParam: function (paramObj) {
        var me = this;
        me.paramObj = paramObj;
    },
    reLoadData: function () {
        var me = this;
        me.form.setValues({
            currentSmId: me.paramObj.smid,
            parentId: me.paramObj.parentid,
            parentname: me.paramObj.parentname
        });
        if (me.paramObj.editflag) {
            me.form.waitMsgTarget = true;
            me.load({
                waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                url: __ctxPath + '/kpi/kpistrategymap/findsmbyidtojson.f',
                params: {
                    id: me.paramObj.smid
                },
                success: function (form, action) {
                    var multiSelect = action.result.multiSelect;
                    if (multiSelect.otherDim) {
                        //给辅助纬度赋值
                        var arr = Ext.JSON.decode(multiSelect.otherDim);
                        Ext.getCmp('sm_otherDimMultiCombo').setValue(arr);
                    }
                    if (multiSelect.otherTheme) {
                        //给辅助战略主题赋值
                        var arr = Ext.JSON.decode(multiSelect.otherTheme);
                        Ext.getCmp('sm_otherTheme').setValue(arr);
                    }

                    var vobj = form.getValues();
                    if (vobj.charttypehidden != "") {
                        var charttypearr = vobj.charttypehidden.split(',');
                        //给图表类型赋值
                        Ext.getCmp("sm_charttype").setValue(charttypearr);
                    }
                    return true;

                }
            });
        }
      if(!me.paramObj.editflag){
    	//赋默认值
    	Ext.getCmp('sm_estatus').setValue('0yn_y');
     }

    }

});