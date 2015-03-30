Ext.define('FHD.view.comm.theme.AnalysisLayoutForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.analysislayoutform',
    border: false,
    dataType : '',
    paramObj:{id:'',layouttype:'',kpitypeid:'',kpitypename:''},
    
    clearParamObj:function(){
    	var me = this;
    	me.paramObj = {id:'',layouttype:'',kpitypeid:'',kpitypename:''};
    },
    
    clearFormData:function(){
    	var me = this;
    	Ext.getCmp('layoutbtn1').toggle(false);
    	Ext.getCmp('layoutbtn2').toggle(false);
    	Ext.getCmp('layoutbtn3').toggle(false);
    	me.getForm().reset();
    },
    
   
    
    /**
     * 点击下一步提交事件
     * @param {panel} cardPanel cardpanel面板
     * @param {boolean} finishflag 是否完成标示,true代表点击了'完成按钮'
     */
    last: function (cardPanel, finishflag) {
        var me = this;
        var form = me.getForm();
        var vobj = form.getValues();
        var themerecordgrid = Ext.getCmp('themerecordgrid');
    	var layoutInfoId = themerecordgrid.layoutInfoId;
    	var layoutType = me.layoutType;
    	
//    	if(Ext.getCmp('layoutbtn1').toggle){
//        	layoutType='1'
//    	}else if(Ext.getCmp('layoutbtn2').toggle){
//        	layoutType='2'
//    	}else if(Ext.getCmp('layoutbtn3').toggle){
//        	layoutType='3'
//    	}
    	if(layoutType==null){
    		 Ext.MessageBox.show({
				title : "操作信息",
				msg : "请选择一种布局",
				buttons : Ext.MessageBox.OK
			});
    		 return false;
    	}
    	
        themeId = me.treeRecordId;//主题id
        
        if(!form.isValid()){
        	return false;
        }
//        FHD.ajax(
//        	{
//            url: __ctxPath + '/ic/theme/validate.f',//校验信息是否重复
//            params: {
//                id: me.paramObj.id,
//                validateItem: Ext.JSON.encode(paramObj)
//            },
//            callback: function (data) {
//                if (data && data.success ) {
                    //提交信息
                    var addUrl = __ctxPath + '/ic/theme/savelayoutinfo.f';
                    /*
                     * 保存信息
                     */
                    FHD.submit({
                        form: form,
                        url: addUrl,
                         params: {
			                themeId: themeId,
			                layoutId : layoutInfoId,
			                layoutType : layoutType
			            },
                        callback: function (data) {
                            if (data) {
                            	me.layoutInfoId = data.id;
//                            	me.layoutType = layoutType;
                            	var analysiscardpanel =  Ext.getCmp("analysiscardpanel");
                                                               
                                if (!finishflag) {//如果点击的不是完成按钮,需要移动到下一个面板
                                	cardPanel.lastSetBtnState(cardPanel,cardPanel.getActiveItem());
                                    analysiscardpanel.navBtnState(cardPanel);//设置导航按钮状态,如果是首个面板则上一步按钮为置灰状态,如果是最后一个面板则下一步按钮为置灰状态
                                    if(layoutType=='1'){
	                                 	Ext.getCmp('analysiscardpanel').setActiveItem(1);
                                    }else if(layoutType=='2'){
                                 		Ext.getCmp('analysiscardpanel').setActiveItem(2);
                                    }else if(layoutType=='3'){
                                 		Ext.getCmp('analysiscardpanel').setActiveItem(3);
                                    }
                                    Ext.getCmp('theme_move-next_top' ).setDisabled(true);
                                    Ext.getCmp('theme_move-prev_top' ).setDisabled(false);
                                    Ext.getCmp('theme_move-next' ).setDisabled(true);
                                    Ext.getCmp('theme_move-prev' ).setDisabled(false);
                                }else{
                                	analysiscardpanel.gotopage();
                                }
                                
                            }
                        }
                    });
//                } else {
//                    //校验失败信息
//                    if (data && data.error == "nameRepeat") {//名称重复
//                        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.namerepeat"));
//                        return;
//                    }
//                }
//            }
//        });
        
    },
    
    /**
     * form表单中添加控件
     */
    addComponent: function () {
        var me = this;
        var layouttype 
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
 

        //名称
        var layoutName = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            labelAlign: 'left',
            name: 'layoutName',
            fieldLabel: '名称', 
            maxLength: 255,
            columnWidth: .5,
            allowBlank: false,
            value:''
        });
        basicfieldSet.add(layoutName);


        me.add(basicfieldSet);

        var layoutTypeBtn = Ext.widget('fieldset', {
            //xtype: 'fieldset', //相关信息fieldset
            autoHeight: true,
            autoWidth: true,
            collapsible: true,
            layout: {
                type: 'hbox'
            },
            title:"页面布局选择",
            defaults: { margin: '10 30 3 30', labelWidth: 105}, 
            items: [{
             			xtype:'button',
						text: '布局1',
						id : 'layoutbtn1',
						enableToggle:true,
						width:80,
						height:80,
						iconCls: 'icon-theme-layout1',
						scale: 'large',
						iconAlign: 'top' ,
						handler: function () {
		                	me.layoutType = '1';
		                	Ext.getCmp('layoutbtn2').toggle(false);
           				    Ext.getCmp('layoutbtn3').toggle(false);
		                }

					}, {
						xtype:'button',
						text: '布局2',
						id : 'layoutbtn2',
						enableToggle:true,
						width:80,
						height:80,
						iconCls: 'icon-theme-layout2',
						scale: 'large',
						iconAlign: 'top' ,
						handler: function () {
		                	me.layoutType = '2';
		                	Ext.getCmp('layoutbtn1').toggle(false);
           				    Ext.getCmp('layoutbtn3').toggle(false);
		                }
					},  {
						xtype:'button',
						text: '布局3',
						id : 'layoutbtn3',
						enableToggle:true,
						width:80,
						height:80,
						iconCls: 'icon-theme-layout3',
						scale: 'large',
						iconAlign: 'top',
						handler: function () {
		                	me.layoutType = '3';
		                	Ext.getCmp('layoutbtn1').toggle(false);
           				    Ext.getCmp('layoutbtn2').toggle(false);
		                }
					}]

        });
        
        me.add(layoutTypeBtn);

    },
    
    
    

    // 初始化方法
    initComponent: function () {
        var me = this;
		me.layoutInfoId=null;
		me.layoutType=null;
		me.xmldata =null;
        Ext.applyIf(me, {
            autoScroll: true,
            border: me.border,
            autoHeight: true,
            layout: 'column',
            height: FHD.getCenterPanelHeight() - 75,
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
    },
    
    /**
     * 加载form数据
     */
    formLoad: function (id) {
        var me = this;
        //清除数据
        //基本信息面板
        me.clearFormData();
        var vobj = me.getForm().getValues();
    	var id = id;
        me.form.load({
            url: __ctxPath + '/ic/theme/findLayoutInfoByIdToJson.f',
            params: {
                id: id
            },
            /**
             * form加载数据成功后回调函数
             */
            success: function (form, action) {
             //根据布局信息ID加载布局信息数据
  		      var layoutType = action.result.data.layoutType;
     		  me.loadFormById(id,layoutType);
     		  if(layoutType == '1'){
     		  	Ext.getCmp('layoutbtn1').toggle(true);
     		  	me.layoutType='1';
     		  }else if(layoutType == '2'){
     		  	Ext.getCmp('layoutbtn2').toggle(true);
     		  	me.layoutType='2';
     		  }else if(layoutType == '3'){
     		  	Ext.getCmp('layoutbtn3').toggle(true);
     		  	me.layoutType='3';
     		  }
              return true;
            }
        });
       
        
    },
            /**
     * form加载数据方法
     */
    loadFormById: function (id,layoutType) {
        var me = this;
		var paraobj = {};
    	paraobj.eType = '0frequecy_all';
        paraobj.isNewValue = FHD.data.isNewValue
        if(FHD.data.yearId == ''){
        	paraobj.year = me.getYear();
        }else{
        	paraobj.year = FHD.data.yearId;
        }
        paraobj.monthId = FHD.data.monthId;
        paraobj.dataType = this.dataType;
        layoutInfoId = id;
        type = layoutType;
        me.load({
            url: __ctxPath + '/ic/theme/findLayoutDetailedByIdToJson.f',
            params: {
                id: id,
                layoutType:layoutType,
                condItem: Ext.JSON.encode(paraobj)
            },
            success: function (form, action) {
            	me.xmldata = action.result.data;
            	if(action.result.data.length>0){
            		if(layoutType=='1'){
            			Ext.getCmp('analysislayoutpanel').categoryPanel(action.result.data);
            		}else if(layoutType=='2'){
            			Ext.getCmp('analysislayoutpanel2').categoryPanel(action.result.data);
            		}else if(layoutType=='3'){
            			Ext.getCmp('analysislayoutpanel3').categoryPanel(action.result.data);
            		}
            		
            	}
                return true;
            }
        });
    },
            //获取当前年份
    getYear : function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    }  

});