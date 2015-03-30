/**
 * 基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author haojing
 */
Ext.define('FHD.view.comm.theme.ThemeBasicForm', {
    extend: 'Ext.form.Panel',
    border: false,
    alias: 'widget.themebasicform',
    

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
            items: []
        });

        //名称
        var name = Ext.widget('textareafield', {
            xtype: 'textareafield',
            name: 'analyname',
            id:'analynameId',
            rows: 3,
            fieldLabel: FHD.locale.get('fhd.kpi.kpi.form.name') + '<font color=red>*</font>', //名称
            value: '',
            maxLength: 255,
            columnWidth: .5,
            allowBlank: false
            /*validator :function(){
            	alert("************");
            },
  			invalidText:"名称已经存在"*/
        });
        basicfieldSet.add(name);
        //说明
        var desc = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 3,
            labelAlign: 'left',
            name: 'analydesc',
            id:'analydescId',
            fieldLabel: FHD.locale.get('fhd.sys.dic.desc'), //说明
            maxLength: 4000,
            columnWidth: .5
        });
        basicfieldSet.add(desc);

        me.add(basicfieldSet);
        
        
        
//        
//       var selectorWindow =Ext.create('FHD.ux.kpi.ScorecardSelectorWindow',{
//							selectedvalues:'',
//							onSubmit:function(values){
//								
//							}
//						}).show();
//
//        basicfieldSet.add(selectorWindow);
        
        
        
        



    },
    
    /**
     * 清除form数据
     */
    clearFormData:function(){
      var me = this;
      me.getForm().reset();
    },
    /**
     * 初始化组件方法
     */
    initComponent: function () {
        var me = this;
        me.analytreeId = 'undefined';//树节点id
        me.save_url = 'ic/theme/saveanalysisinfo.f';//保存url
        
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


    /**
     * 表单加载
     */
    load:function(){
    	var me = this;
    	if(typeof(me.analytreeId) != 'undefined'){
        	//编辑时加载form数据
            me.basicformLoad();
    	}
    },
    /**
     * 加载表单中的数据
     */
    basicformLoad : function(){
    	var me = this;
    	if(typeof(me.analytreeId) != 'undefined') {
    		me.form.load({
    	        url:'ic/theme/findanalysisbyid.f',
    	        params:{id:me.analytreeId},
    	        failure:function(form,action) {
    	            alert("err 155");
    	        },
    	        success:function(form,action){
    	        	var formValue = form.getValues();
    	        }
    	    });
    	}
    },

    /**
     * 保存按钮事件
     */
    saveAnaly:function(){
    	var me = this;
    	var themetree = Ext.getCmp('themetree');
    	var themetab = Ext.getCmp('themetab');
    	var form = me.getForm();
    	if(form.isValid()){
    		if(themetree.isAdd){//新增保存
    			FHD.submit({
    				form:form,
    				url:me.save_url,
    				callback:function(data){
    					themetree.store.load();
    					themetab.setActiveTab(0);
    				}
    				});	
    		}else{
    			FHD.submit({	//修改保存
    				form:form,
    				url:me.save_url+ '?id=' + me.analytreeId,
    				callback:function(data){
    					themetree.store.load();
    					themetab.setActiveTab(0);
    				}
    			});	
    		}
    	}
    }
    

});