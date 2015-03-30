/**
 * 添加记分卡,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */
Ext.define('FHD.view.kpi.scorecard.ScorecardBasicForm', {
    extend: 'Ext.form.Panel',
    border: false,
    alias: 'widget.scorecardbasicform',
    
    replaceall:function(oldstring,oldsign,newsign){
		while(oldstring.indexOf(oldsign)!=-1){
			oldstring=oldstring.replace(oldsign,newsign);
		}
		return oldstring;
	},

    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last:function(cardPanel,finishflag){
		var me = this;
		var scorecardtab = Ext.getCmp('scorecardtab');
	    var form = me.getForm();
	    var vobj = form.getValues();
        var paramObj = {};
        paramObj.name = vobj.name;
        paramObj.code = vobj.code;
        if(!form.isValid()){
        	return false;
        }
        FHD.ajax({
            url: __ctxPath + '/kpi/category/validate.f',
            params: {
                id: scorecardtab.paramObj.categoryid,
                validateItem: Ext.JSON.encode(paramObj)
            },
            callback: function (data) {
                if (data && data.success ) {
                	var forecastFormulaValue = Ext.getCmp('forecastFormula').getValue();
                	var assessmentFormulaValue = Ext.getCmp('assessmentFormula').getValue();
                	forecastFormulaValue = forecastFormulaValue==null?"":forecastFormulaValue;
                	assessmentFormulaValue = assessmentFormulaValue==null?"":assessmentFormulaValue;
                	var param = Ext.JSON.encode({
                    	id:scorecardtab.paramObj.categoryid,
                    	forecastFormula:forecastFormulaValue,
                    	assessmentFormula:assessmentFormulaValue
                	});
                    
                    //提交指标信息
                    var addUrl = __ctxPath + '/kpi/category/mergecategory.f?param='+encodeURIComponent(param);
                    FHD.submit({
                        form: form,
                        url: addUrl,
                        callback: function (data) {
                            if (data) {
                            	var scorecardtab =  Ext.getCmp("scorecardtab");
                            	var editflag = scorecardtab.paramObj.editflag;
                            	scorecardtab.paramObj.editflag = true;
                            	var scorecardbasicinfopanel =  Ext.getCmp("scorecardbasicinfopanel");
                            	scorecardtab.paramObj.categoryid = data.id;
                            	scorecardtab.paramObj.categoryname = vobj.name;
                            	Ext.getCmp('scorecardmainpanel').paramObj.categoryid = data.id;
                            	Ext.getCmp('scorecardmainpanel').paramObj.chartIds = Ext.getCmp('kpicategory_charttype').getValue().join(',');
                            	
                                if(!finishflag){//如果点击的不是完成按钮,需要移动到下一个面板
                                	 cardPanel.lastSetBtnState(cardPanel,cardPanel.getActiveItem());
                                	 cardPanel.pageMove("next");
                                	 scorecardbasicinfopanel.navBtnState();//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                     //同时将告警设置按钮为可用状态
                                     Ext.getCmp('kpicategory_alarmset_btn').setDisabled(false);
                                     Ext.getCmp('kpicategory_alarmset_btn_top').setDisabled(false);
                                }else{
                                	scorecardbasicinfopanel.gotopage();
                                }
                            }
                            var scorecardtree = Ext.getCmp('scorecardtree');
                            if(!editflag){//添加节点
                              var node = {iconCls:'icon-status-disable',id:scorecardtab.paramObj.categoryid,text:scorecardtab.paramObj.categoryname,dbid:scorecardtab.paramObj.categoryid,leaf:true,type:'category'};
                              if(scorecardtree.currentNode.isLeaf()){
                            	  scorecardtree.currentNode.data.leaf = false;
                              }
                              scorecardtree.currentNode.appendChild(node);
                              scorecardtree.currentNode.expand();
                              scorecardtree.getSelectionModel().select(scorecardtree.currentNode.lastChild);
                              scorecardtree.currentNode = scorecardtree.currentNode.lastChild;
                              //刷新导航
                              Ext.getCmp('scorecardmainpanel').navigationBar.renderHtml('scorecardtabcontainer', scorecardtab.paramObj.categoryid , '', 'sc');
                            }else{//编辑节点
                              var currentnode = scorecardtree.currentNode;
                          	  var data = currentnode.data;
                          	  data.text = scorecardtab.paramObj.categoryname;
                          	  currentnode.updateInfo(data);
                          	  Ext.getCmp('scorecardmainpanel').navigationBar.renderHtml('scorecardtabcontainer', scorecardtab.paramObj.categoryid , '', 'sc');
                            }
                            
                        }
                    });
                } else {
                    //校验失败信息
                    if (data && data.error == "codeRepeat") {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.coderepeat"));
                        return false;
                    }
                    return false;
                }
            }
        });
    },
    
    /**
     * 编码生成函数
     */
    createCode:function(){
    	var me = this;
    	var vform = me.getForm();
        var vobj = vform.getValues();
        var paraobj = {};
        paraobj.id = Ext.getCmp('scorecardtab').paramObj.categoryid;
        if(vobj.parentStr==""){
        	vobj.parentStr = "category_root";
        }
        if (vobj.parentStr != "") {
            paraobj.parentid = vobj.parentStr;
            FHD.ajax({
                url: __ctxPath + '/kpi/category/findcodebyparentid.f',
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
                labelWidth: 95
            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.common.baseInfo'),
            items: [
			
            {
                xtype: 'hidden',
                hidden: true,
                name: 'id',
                id:'category_id'
            }, {
                xtype: 'hidden',
                hidden: true,
                name: 'parentStr',
                id:'category_parentId'
            },
            {
			    xtype: 'hidden',
			    hidden: true,
			    name: 'charttypehidden'
			}
            ]
        });
        //上级维度
        var parentCategory = Ext.widget('textfield', {
            xtype: 'textfield',
            readOnly: true,
            disabled: true,
            name: 'parentStr',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.parentCategory'), //上级维度
            value: '',
            maxLength: 200,
            columnWidth: .5,
            allowBlank: false,
            id: 'category_parent'
        });

        basicfieldSet.add(parentCategory);

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

        //自动生成编码按钮
        var codeBtn = Ext.widget('button', {
            xtype: 'button',
            margin: '7 30 3 3',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'), //自动生成编码按钮
            columnWidth: .1,
            handler: function () {
                me.createCode();
            }
        });

        basicfieldSet.add(codeBtn);

        //名称
        var name = Ext.widget('textareafield', {
            xtype: 'textareafield',
            name: 'name',
            rows: 3,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>', //名称
            value: '',
            maxLength: 255,
            columnWidth: .5,
            allowBlank: false
        });
        basicfieldSet.add(name);
        //说明
        var desc = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 3,
            labelAlign: 'left',
            name: 'desc',
            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'), //说明
            maxLength: 4000,
            columnWidth: .5
        });
        basicfieldSet.add(desc);

        //所属部门人员
        me.owndepts = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>', //所属部门人员
            growMax: 60,
            id:'sc_owndept',
            labelAlign: 'left',
            columnWidth: .5,
            name: 'ownDept',
            multiSelect: false,
            type: 'dept_emp',
            maxHeight: 70,
            labelWidth: 95,
            allowBlank: false
        });
        basicfieldSet.add(me.owndepts);

        //是否启用
        var statu = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'statusStr',
            id:'isstatus',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: FHD.locale.get('fhd.common.enable') + '<font color=red>*</font>', //是否启用
            defaultValue: '0yn_y',
            labelAlign: 'left',
            allowBlank: false,
            columnWidth: .5
        });
        basicfieldSet.add(statu);
        //评估值公式
        var assessmentFormula = Ext.create('FHD.ux.kpi.FormulaTrigger', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula'), //评估值公式
            hideLabel: false,
            id: 'assessmentFormula',
            emptyText: '',
            labelAlign: 'left',
            flex: 1.5,
            cols: 20,
            rows: 3,
            name: 'assessmentFormula',
            type: 'category',
            showType: 'categoryType',
            column: 'assessmentValueFormula',
            labelWidth: 100,
            columnWidth: .5
        });
        basicfieldSet.add(assessmentFormula);

        //预警公式
        var forecastFormula = Ext.create('FHD.ux.kpi.FormulaTrigger', {
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.warningFormula'),
            hideLabel: false,
            id: 'forecastFormula',
            emptyText: '',
            labelAlign: 'left',
            flex: 1.5,
            cols: 20,
            rows: 3,
            name: 'forecastFormula',
            type: 'kpi',
            showType: 'kpiType',
            column: 'assessmentValueFormula',
            labelWidth: 100,
            columnWidth: .5
        });
        basicfieldSet.add(forecastFormula);
        //图表类型
        var chartype = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
            id: 'kpicategory_charttype',
            editable: false,
            labelWidth: 100,
            multiSelect: true,
            name: 'chartTypeStr',
            dictTypeId: '0com_catalog_chart_type',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.category.charttype')+ '<font color=red>*</font>', //图表类型
            columnWidth: .5,
            labelAlign: 'left',
            allowBlank: false,
            maxHeight: 60
        });
        basicfieldSet.add(chartype);
        //数据类型
        var datatype = Ext.create('FHD.ux.dict.DictSelect', {
            id: 'kpicategory_dataType',
            editable: false,
            labelWidth: 100,
            multiSelect: false,
            name: 'dataTypeStr',
            dictTypeId: '0category_data_type',
            fieldLabel: '数据类型' /*FHD.locale.get('fhd.kpi.category.form.datatype')*/
            , //数据类型
            columnWidth: .5,
            labelAlign: 'left',
            maxHeight: 60
        });
        basicfieldSet.add(datatype);
        //是否生成度量指标
        var iscreateKpi = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'createKpiStr',
            id:'iscreateKpi',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: '生成度量指标' /*FHD.locale.get("fhd.kpi.category.from.createkpi") + '<font color=red>*</font>'*/
            , //是否生成度量指标
            defaultValue: '0yn_y',
            labelAlign: 'left',
            allowBlank: false,
            columnWidth: .5
        });
        basicfieldSet.add(iscreateKpi);
        //是否计算
        var iscalcKpi = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'calcStr',
            id:'calc',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: '是否计算' ,
            defaultValue: '0yn_y',
            labelAlign: 'left',
            allowBlank: false,
            columnWidth: .5
        });
        basicfieldSet.add(iscalcKpi);
        me.add(basicfieldSet);



    },
    
    /**
     * 清除form数据
     */
    clearFormData:function(){
      var me = this;
      me.getForm().reset();
      if(Ext.getCmp('sc_owndept')){
    	  Ext.getCmp('sc_owndept').clearValues();
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
            listeners: {
                afterrender: function (t, e) {


                }
            }


        });

        me.callParent(arguments);

        //向form表单中添加控件
        me.addComponent();




    },

    reLoadData: function () {
        var me = this;
        var scorecardtab = Ext.getCmp('scorecardtab');
        Ext.getCmp('category_parent').setValue(scorecardtab.paramObj.categoryparentname);
        Ext.getCmp('category_parentId').setValue(scorecardtab.paramObj.categoryparentid);
        Ext.getCmp('category_id').setValue(scorecardtab.paramObj.categoryid);
        if(scorecardtab.paramObj.editflag){
        	me.form.waitMsgTarget = true;
            me.load({
            	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
                url: __ctxPath + '/kpi/category/findcategoryByIdToJson.f',
                params: {
                    id: scorecardtab.paramObj.categoryid
                },
                success: function (form, action) {
                    var vobj = form.getValues();
                    if (vobj.charttypehidden != "") {
                        var charttypearr = vobj.charttypehidden.split(',');
                        //给图表类型赋值
                        Ext.getCmp("kpicategory_charttype").setValue(charttypearr);
                    }
                    return true;
                }
            });
        }
        if(!scorecardtab.paramObj.editflag){
        	//赋默认值
        	Ext.getCmp('iscreateKpi').setValue('0yn_y');
        	Ext.getCmp('calc').setValue('0yn_y');
        	Ext.getCmp('isstatus').setValue('0yn_y');
        	//赋值数据类型字段
       	 	FHD.ajax({
                url: __ctxPath + '/kpi/category/findparentcategorydatetypebyid.f',
                async:false,
                params: {
                    parentid:scorecardtab.paramObj.categoryparentid
                },
                callback: function (data) {
                    if (data && data.success) {
                    	var vform = me.getForm();
                        vform.setValues({
                       	 dataTypeStr: data.dataTypeStr
                        });
                    }
                }
            });
       }
        
    }



});