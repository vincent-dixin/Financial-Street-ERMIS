Ext.define('FHD.view.kpi.kpitype.KpiTypeBasicForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.kpitypebasicform',
    
    border: false,
    
    
    createKpiTypeCode:function(){
    	 var me = this;
         var paraobj = {
             id: Ext.getCmp('kpitypetab').paramObj.kpitypeid
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
        var kpitypetab = Ext.getCmp('kpitypetab');
    	var kpitypecardpanel = Ext.getCmp('kpitypecardpanel');
    	var kpitypeid = kpitypetab.paramObj.kpitypeid;
    	if(!form.isValid()){
    		return false;
    	}
        if (form.isValid()) {
            FHD.ajax({
                url: __ctxPath + '/kpi/kpi/validate.f',
                params: {
                    id: kpitypeid,
                    validateItem: Ext.JSON.encode(paramObj)
                },
                callback: function (data) {
                    if (data && data.success) {
                        //提交指标信息
                        var addUrl = __ctxPath + '/kpi/kpi/mergekpitype.f?id=' + kpitypeid;
                        FHD.submit({
                            form: form,
                            url: addUrl,
                            callback: function (data) {
                                if (data) {
                                	 kpitypetab.paramObj.kpitypeid = data.id;
                                	 var editflag =  kpitypetab.paramObj.editflag ;
                                	 kpitypetab.paramObj.editflag = true;
                                	 kpitypetab.paramObj.kpitypename = vobj.name;
                                    //fhd_kpi_kpitypetree_view.refreshTree();
                                    if(!finishflag){//如果点击的不是完成按钮,需要移动到下一个面板
                                    	cardPanel.lastSetBtnState(cardPanel,cardPanel.getActiveItem());
                                    	cardPanel.pageMove("next");
                                    	kpitypecardpanel.navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                   	 	/*同时将采集频率按钮为可用状态*/
                                        Ext.getCmp('kpitype_kpiset_btn').setDisabled(false);
                                        Ext.getCmp('kpitype_kpiset_btn_top').setDisabled(false);
                                        /*给公式赋值,公式编辑器需要设置它的targetid和targetnametargetid为指标ID,targetname为指标名称*/
                                        Ext.getCmp('kpitypegatherform').valueToFormulaName();
                                    }else{
                                    	kpitypecardpanel.gotopage();
                                    }
                                    var kpitypetree = Ext.getCmp('kpitypetree');
                                    if(!editflag){//添加节点
                                        var node = {iconCls:'icon-ibm-icon-metrictypes',id:kpitypetab.paramObj.kpitypeid,text:vobj.name,dbid:kpitypetab.paramObj.kpitypeid,leaf:true,type:'kpitype'};
                                        if(kpitypetree.currentNode.isLeaf()){
                                        	kpitypetree.currentNode.data.leaf = false;
                                        }
                                        kpitypetree.currentNode.appendChild(node);
                                        kpitypetree.currentNode.expand();
                                        kpitypetree.getSelectionModel().select(kpitypetree.currentNode.lastChild);
                                        kpitypetree.currentNode = kpitypetree.currentNode.lastChild;
                                        //刷新导航
                                        Ext.getCmp('kpitypemainpanel').navigationBar.renderHtml('kpitypecontainer', kpitypetab.paramObj.kpitypeid , '', 'kpi');
                                      }else{//编辑节点
                                      	//需要替换节点名称
                                    	  var currentnode = kpitypetree.currentNode;
                                    	  var data = currentnode.data;
                                    	  data.text = kpitypetab.paramObj.kpitypename;
                                    	  currentnode.updateInfo(data);
                                    	  Ext.getCmp('kpitypemainpanel').navigationBar.renderHtml('kpitypecontainer', kpitypetab.paramObj.kpitypeid , '', 'kpi');
                                      }
                                    
                                }
                            }
                        });
                    } else {
                        //校验失败信息
                        if (data && data.error == "nameRepeat") {
                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
                            return false;
                        }
                        if (data && data.error == "codeRepeat") {
                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.coderepeat"));
                            return false;
                        }
                        return false;
                    }
                }
            });
        }
    },
    
    clearFormData:function(){
   	 	var me = this;
        me.getForm().reset();
        me.ownDept.clearValues();
        me.gatherDept.clearValues();
        me.targetDept.clearValues();
        me.reportDept.clearValues();
        me.viewDept.clearValues();
    },
    
    addComponent:function(){
    	var me = this;
    	
    	var fieldSet =  Ext.widget('fieldset', {
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
            title: FHD.locale.get('fhd.common.baseInfo'),
            items: [
						{
						    xtype: 'hidden',
						    hidden: true,
						    name: 'id',
						    //value: me.currentId,//需要动态加载
						    id: 'kpitype_id'
						}
				  ]
        });
    	
    	me.add(fieldSet);
    	//名称
    	var name =  Ext.widget('textareafield',{
            xtype: 'textareafield',
            labelAlign: 'left',
            rows: 3,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>',//名称
            name: 'name',
            maxLength: 255,
            columnWidth: .5,
            allowBlank: false
        });
    	fieldSet.add(name);
    	//说明
    	var desc = Ext.widget('textareafield',{
            xtype: 'textareafield',
            rows: 3,
            labelAlign: 'left',
            name: 'desc',
            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'),//说明
            maxLength: 2000,
            columnWidth: .5
        });
    	fieldSet.add(desc);
    	//短名称
    	var shortName = Ext.widget('textfield',{
                xtype: 'textfield',
                name: 'shortName',
                fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.shortName'),//短名称
                value: '',
                maxLength: 255,
                columnWidth: .5
            });
    	
    	fieldSet.add(shortName);
    	
    	//序号
    	var sort = Ext.widget('numberfield',{
        	xtype: 'numberfield',
            step: 1,
            name: 'sort',
            minValue:0,
            fieldLabel: FHD.locale.get('fhd.sys.icon.order'),//序号
            value: '',
            maxLength: 255,
            columnWidth: .5
        });
    	fieldSet.add(sort);
    	
    	//编码
    	var code = Ext.widget('textfield',{
            xtype: 'textfield',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.code'),//编码
            margin: '7 3 3 30',
            name: 'code',
            maxLength: 255,
            columnWidth: .4
        });
    	fieldSet.add(code);
    	
    	//自动生成按钮
    	var codeBtn = Ext.widget('button',{
            xtype: 'button',
            margin: '7 30 3 3',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),//自动生成按钮
            columnWidth: .1,
            handler: function () {
               me.createKpiTypeCode();
            }
        });
    	fieldSet.add(codeBtn);
    	
    	//所属部门
    	me.ownDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get("fhd.strategymap.strategymapmgr.form.owndept") + '<font color=red>*</font>',//所属部门
            labelAlign: 'left',
            columnWidth: .5,
            name: 'ownDept',
            multiSelect: false,
            type: 'dept_emp',
            labelWidth: 95,
            allowBlank: false
        });
    	fieldSet.add(me.ownDept);
    	//采集部门
    	me.gatherDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.gatherdept') ,//采集部门
            labelAlign: 'left',
            columnWidth: .5,
            name: 'gatherDept',
            multiSelect: false,
            type: 'dept_emp',
            labelWidth: 95
        });
    	fieldSet.add(me.gatherDept);
    	//目标部门
    	me.targetDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetdept'),//目标部门
            labelAlign: 'left',
            columnWidth: .5,
            name: 'targetDept',
            multiSelect: false,
            type: 'dept_emp',
            labelWidth: 95
        });
    	fieldSet.add(me.targetDept);
    	
    	//报告部门
    	me.reportDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.reportdept'),//报告部门
            growMax: 60,
            labelAlign: 'left',
            columnWidth: .5,
            name: 'reportDept',
            multiSelect: true,
            margin: '0 12 0 30',
            type: 'dept_emp',
            maxHeight: 60,
            labelWidth: 95
        });
    	fieldSet.add(me.reportDept);
    	//查看部门
    	me.viewDept = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.viewdept'),//查看部门
            growMax: 60,
            labelAlign: 'left',
            columnWidth: .5,
            name: 'viewDept',
            multiSelect: true,
            margin: '0 12 0 30',
            type: 'dept_emp',
            maxHeight: 60,
            labelWidth: 95
        });
    	fieldSet.add(me.viewDept);
    	
    	//相关信息fieldset
    	var relaFieldSet = Ext.widget('fieldset', {
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
	            title: FHD.locale.get('fhd.kpi.kpi.form.assinfo')//相关信息
            });
    	
    	me.add(relaFieldSet);
    	
    	//是否启用
    	var statusStr = Ext.widget('dictradio',{
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
        });
    	relaFieldSet.add(statusStr);
    	//是否监控
    	var monitorStr = Ext.widget('dictradio',{
            xtype: 'dictradio',
            labelWidth: 105,
            name: 'monitorStr',
            columns: 4,
            dictTypeId: '0yn',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.ismonitor'),//是否监控
            defaultValue: '0yn_y',
            labelAlign: 'left',
            columnWidth: .5
        });
    	relaFieldSet.add(monitorStr);
    	//开始日期
    	var startDateStr = Ext.widget('datefield',{
            xtype: 'datefield',
            format: 'Y-m-d',
            name: 'startDateStr',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.startdate') + '<font color=red>*</font>',//开始日期
            columnWidth: .5,
            allowBlank: false
        });
    	relaFieldSet.add(startDateStr);
    	//单位
    	var unitsStr = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            multiSelect: false,
            name: 'unitsStr',
            dictTypeId: '0units',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.unit'),//单位
            columnWidth: .5,
            labelAlign: 'left',
            labelWidth: 105
        });
    	relaFieldSet.add(unitsStr);
    	//指标类型
    	var typeStr = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 100,
            multiSelect: false,
            name: 'typeStr',
            dictTypeId: 'kpi_etype',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.etype'),//指标类型
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue:'kpi_etype_positive'
        });
    	relaFieldSet.add(typeStr);
    	//指标性质
    	var kpiTypeStr = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 105,
            multiSelect: false,
            name: 'kpiTypeStr',
            dictTypeId: 'kpi_kpi_type',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.type'),//指标性质
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue:'kpi_kpi_type_assessment'
        });
    	relaFieldSet.add(kpiTypeStr);
    	//亮灯依据
    	var alarmMeasureStr = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 100,
            multiSelect: false,
            name: 'alarmMeasureStr',
            dictTypeId: 'kpi_alarm_measure',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmMeasure'),//亮灯依据
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue:'kpi_alarm_measure_score'
        });
    	relaFieldSet.add(alarmMeasureStr);
    	//预警依据
    	var alarmBasisStr = Ext.create('widget.dictselectforeditgrid', {
            editable: false,
            labelWidth: 105,
            multiSelect: false,
            name: 'alarmBasisStr',
            dictTypeId: 'kpi_alarm_basis',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.alarmBasis'),//预警依据
            columnWidth: .5,
            labelAlign: 'left',
            defaultValue:'kpi_alarm_basis_forecast'
        });
    	relaFieldSet.add(alarmBasisStr);
    	//主纬度
    	var mainDim = Ext.create('widget.dictselectforeditgrid', {
             editable: false,
             labelWidth: 100,
             multiSelect: false,
             name: 'mainDim',
             dictTypeId: 'kpi_dimension',
             fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.mainDim'),//主纬度
             columnWidth: .5,
             labelAlign: 'left'
         });
    	relaFieldSet.add(mainDim);
    	//辅助纬度
    	var otherDim = Ext.create('FHD.ux.dict.DictSelect', {
            maxHeight: 70,
            labelWidth: 105,
            name: 'otherDim',
            dictTypeId: 'kpi_dimension',
            fieldLabel: FHD.locale.get('fhd.strategymap.strategymapmgr.form.otherDim'),//辅助纬度
            columnWidth: .5,
            labelAlign: 'left',
            multiSelect: true,
            id: 'kpitype_otherDimSelect'
        });
    	relaFieldSet.add(otherDim);
    	//目标值别名
    	var targetValueAlias = Ext.widget('textfield',{
            xtype: 'textfield',
            name: 'targetValueAlias',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias'),//目标值别名
            value: '',
            maxLength: 255,
            columnWidth: .5,
            value:FHD.locale.get('fhd.kpi.kpi.form.targetValueAlias')
        });
    	relaFieldSet.add(targetValueAlias);
    	//实际值别名
    	var resultValueAlias = Ext.widget('textfield',{
            xtype: 'textfield',
            name: 'resultValueAlias',
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias'),//实际值别名
            value: '',
            maxLength: 255,
            columnWidth: .5,
            labelWidth: 105,
            value:FHD.locale.get('fhd.kpi.kpi.form.resultValueAlias')
        });
    	relaFieldSet.add(resultValueAlias);
    	
    },

	 // 初始化方法
    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            autoScroll: true,
            border: me.border,
            autoHeight: true,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "0 3 3 3"
        });

        me.callParent(arguments);

        //向form表单中添加控件
        me.addComponent();

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
    },
    
    reLoadData:function(){
    	var me = this;
    	var kpitypetab = Ext.getCmp('kpitypetab');
    	var kpitypeid = kpitypetab.paramObj.kpitypeid;
    	me.form.setValues({
    		id:kpitypeid
    	});
    	if (kpitypetab.paramObj.editflag) {
	    	me.form.waitMsgTarget = true;
	        me.form.load({
	            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/kpi/Kpi/findKpiByIdToJson.f',
	            params: {
	                id: kpitypeid
	            },
	            success: function (form, action) {
	                var otherDimArray = action.result.data.otherDimArray;
	                if (otherDimArray) {
	                    var arr = Ext.JSON.decode(otherDimArray);
	                    //给辅助纬度赋值
	                    Ext.getCmp("kpitype_otherDimSelect").setValue(arr);
	                }
	                return true;
	            }
	        });
        }
    }

});