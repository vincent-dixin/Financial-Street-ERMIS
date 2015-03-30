/**
 * 添加记分卡,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 陈晓哲
 */
Ext.define('Ext.kpi.kpi.opt.kpicategoryBasicPanel', {
    extend: 'Ext.form.Panel',
    border: false,
    /**
     * 编码生成函数
     */
    createCode:function(){
    	var me = this;
    	var vform = me.getForm();
        var vobj = vform.getValues();
        var paraobj = {};
        paraobj.id = me.categoryid;
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
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last:function(cardPanel,finishflag){
		var me = this;
	    var form = me.getForm();
	    var vobj = form.getValues();
        var paramObj = {};
        paramObj.name = vobj.name;
        paramObj.code = vobj.code;

        FHD.ajax({
            url: __ctxPath + '/kpi/category/validate.f',
            params: {
                id: me.categoryid,
                validateItem: Ext.JSON.encode(paramObj)
            },
            callback: function (data) {
                if (data && data.success && form.isValid()) {
                	var forecastFormulaValue = Ext.getCmp('forecastFormula'+kpicategory_paramdc).getValue();
                	var assessmentFormulaValue = Ext.getCmp('assessmentFormula'+kpicategory_paramdc).getValue();
                	forecastFormulaValue = forecastFormulaValue==null?"":forecastFormulaValue;
                	assessmentFormulaValue = assessmentFormulaValue==null?"":assessmentFormulaValue;
                    
                    //提交指标信息
                    var addUrl = __ctxPath + '/kpi/category/mergecategory.f?id='+categoryid+"&forecastFormula="+encodeURIComponent(forecastFormulaValue)+"&assessmentFormula="+encodeURIComponent(assessmentFormulaValue);
                    FHD.submit({
                        form: form,
                        url: addUrl,
                        callback: function (data) {
                            if (data) {
                            	categoryid = data.id;
                            	categoryname = vobj.name;
                            	//kpi_category_tree_view.refreshTree();
                                if(!finishflag){//如果点击的不是完成按钮,需要移动到下一个面板
                                	 cardPanel.pageMove("next");
                                     kpicategoryMainPanel._navBtnState();//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                     //同时将告警设置按钮为可用状态
                                     Ext.getCmp('kpicategory_alarmset_btn'+kpicategory_paramdc).setDisabled(false);
                                     Ext.getCmp('kpicategory_alarmset_btn_top'+kpicategory_paramdc).setDisabled(false);
                                }else{
                                	kpicategoryMainPanel._gotopage();
                                }
                            }
                            
                            
                            if(me.editflag=='false'){//添加节点
                              var node = {id:categoryid,text:categoryname,dbid:categoryid,leaf:true,type:'category'};
                              if(kpi_category_tree_view.currentNode.isLeaf()){
                            	  kpi_category_tree_view.currentNode.data.leaf = false;
                              }
                              kpi_category_tree_view.currentNode.appendChild(node);
                              kpi_category_tree_view.currentNode.expand();
                              kpi_category_tree_view.tree.getSelectionModel().select(kpi_category_tree_view.currentNode.lastChild);
                            }else{//编辑节点
                            	//需要替换节点名称
                            	//var node = {id:categoryid,text:categoryname,dbid:categoryid,leaf:true,type:'category'};
                            	//kpi_category_tree_view.currentNode.replaceChild(node);
                            }
                            
                        }
                    });
                } else {
                    //校验失败信息
                    if (data && data.error == "codeRepeat") {
                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.coderepeat"));
                        return;
                    }
                }
            }
        });
    },
    /**
     * 获得图表类型值
     */
    getChartTypeValue:function(){
    	var me = this;
    	return me.form.getValues().charttypehidden;
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
                xtype: 'hidden',
                name: 'charttypehidden'
            }, {
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                autoHeight:true,
                autoWidth:true,
                defaults: {
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
                layout: {
                    type: 'column'
                },
                title: FHD.locale.get('fhd.common.baseInfo'),
                items: [{
                    xtype: 'hidden',
                    hidden: true,
                    name: 'id',
                    value: me.categoryid
                }, {
                    xtype: 'hidden',
                    hidden: true,
                    name: 'parentStr',
                    value: me.categoryparentid
                }, {
                    xtype: 'textfield',
                    readOnly: true,
                    disabled: true,
                    name: 'parentStr',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.parentCategory'),//上级维度
                    value: '',
                    maxLength: 200,
                    columnWidth: .5,
                    allowBlank: false,
                    value: me.categoryparentname
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
                    text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),//自动生成编码按钮
                    columnWidth: .1,
                    handler: function(){
                    	me.createCode();
                    }
                }, {
                    xtype: 'textareafield',
                    name: 'name',
                    rows: 3,
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>',//名称
                    value: '',
                    maxLength: 255,
                    columnWidth: .5,
                    allowBlank: false
                },
                
                {
                    xtype: 'textareafield',
                    rows: 3,
                    labelAlign: 'left',
                    name: 'desc',
                    fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),//说明
                    maxLength: 4000,
                    columnWidth: .5
                }

                ,
                Ext.create('FHD.ux.org.CommonSelector', {
                    fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>',//所属部门人员
                    growMax: 60,
                    labelAlign: 'left',
                    columnWidth: .5,
                    name: 'ownDept',
                    multiSelect: false,
                    type: 'dept_emp',
                    maxHeight: 70,
                    labelWidth: 95,
                    allowBlank: false

                }), {
                    xtype: 'dictradio',
                    labelWidth: 105,
                    name: 'statusStr',
                    columns: 4,
                    dictTypeId: '0yn',
                    fieldLabel: FHD.locale.get('fhd.common.enable') + '<font color=red>*</font>',//是否启用
                    defaultValue: '0yn_y',
                    labelAlign: 'left',
                    allowBlank: false,
                    columnWidth: .5
                },
                
                Ext.create('FHD.ux.kpi.FormulaTrigger',{
                	fieldLabel:FHD.locale.get('fhd.kpi.kpi.form.assessmentFormula'),//评估值公式
        	        hideLabel:false,
        	        id:'assessmentFormula'+kpicategory_paramdc,
        	        emptyText: '',
        	        labelAlign: 'left',
        	        flex:1.5,
        	        cols: 20,
            		rows: 3,
        	        name:'assessmentFormula',
        	        type:'kpi',
        	        showType:'kpiType',
        	        column:'assessmentValueFormula',
        	        labelWidth: 95,
        	        columnWidth: .5
        	        //,targetId:me.targetId,
        	        //targetName:me.targetName
        	    }),
        	    
                Ext.create('FHD.ux.kpi.FormulaTrigger',{
                	fieldLabel:FHD.locale.get('fhd.strategymap.strategymapmgr.form.warningFormula'),
        	        hideLabel:false,
        	        id:'forecastFormula'+kpicategory_paramdc,
        	        emptyText: '',
        	        labelAlign: 'left',
        	        flex:1.5,
        	        cols: 20,
            		rows: 3,
        	        name:'forecastFormula',
        	        type:'kpi',
        	        showType:'kpiType',
        	        column:'assessmentValueFormula',
        	        labelWidth: 95,
        	        columnWidth: .5
        	        //,targetId:me.targetId,
        	        //targetName:me.targetName
        	    })
                ,
                Ext.create('FHD.ux.dict.DictSelect', {
                    id: 'kpicategory_charttype'+kpicategory_paramdc,
                    editable: false,
                    labelWidth: 95,
                    multiSelect: true,
                    name: 'chartTypeStr',
                    dictTypeId: '0com_catalog_chart_type',
                    fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.category.charttype'),//图表类型
                    columnWidth: .5,
                    labelAlign: 'left',
                    maxHeight: 60
                })
                ,
                 Ext.create('FHD.ux.dict.DictSelect', {
                	id: 'kpicategory_dataType'+kpicategory_paramdc,
                    editable: false,
                    labelWidth: 95,
                    multiSelect: false,
                    name: 'dataTypeStr',
                    dictTypeId: '0category_data_type',
                    fieldLabel: FHD.locale.get('fhd.kpi.category.form.datatype'),//数据类型
                    columnWidth: .5,
                    labelAlign: 'left',
                    maxHeight: 60
                }),
                {
                    xtype: 'dictradio',
                    labelWidth: 105,
                    name: 'createKpiStr',
                    columns: 4,
                    dictTypeId: '0yn',
                    fieldLabel: FHD.locale.get("fhd.kpi.category.from.createkpi") + '<font color=red>*</font>',//是否生成度量指标
                    defaultValue: '0yn_y',
                    labelAlign: 'left',
                    allowBlank: false,
                    columnWidth: .5
                }

                ]
            }

            ],
            listeners: {
                afterrender: function (t, e) {
                    if (me.editflag == "true") {
                    	/**
                    	 * 编辑状态时加载form数据
                    	 */
                        me.formLoad();
                    }else{
                    	me.getInitData();
                    }

                }
            }


        });


        me.callParent(arguments);

    },
    getInitData:function(){
    	 var me = this;
    	 var vform = me.getForm();
    	 FHD.ajax({
             url: __ctxPath + '/kpi/category/findparentcategorydatetypebyid.f',
             params: {
                 parentid:categoryparentid
             },
             callback: function (data) {
                 if (data && data.success) {
                     vform.setValues({
                    	 dataTypeStr: data.dataTypeStr
                     });
                 }
             }
         });
    },
    /**
     * 加载form数据
     */
    formLoad: function () {
        var me = this;
        me.form.waitMsgTarget = true;
        me.form.load({
            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/kpi/category/findcategoryByIdToJson.f',
            params: {
                id: me.categoryid
            },
            success: function (form, action) {
                var vobj = form.getValues();
                if (vobj.charttypehidden != "") {
                    var charttypearr = vobj.charttypehidden.split(',');
                    //给图表类型赋值
                    Ext.getCmp("kpicategory_charttype"+kpicategory_paramdc).setValue(charttypearr);
                }
                return true;
            },
            failure: function (form, action) {}
        });
    }



});