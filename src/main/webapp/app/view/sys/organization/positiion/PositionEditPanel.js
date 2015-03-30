/**
 * 岗位基本信息FORM面板
 * 
 * @author 
 */
Ext.define('FHD.view.sys.organization.positiion.PositionEditPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.positionEditPanel',
    title:'岗位信息',
    
    /**
     * 加载form数据
     */
    formLoad : function(){
    	var me = this;
    	if(typeof(me.orgtreeId) != 'undefined') {
    		me.form.load({
    	        url:'sys/organization/findpositionbyid.f',
    	        params:{id:me.orgtreeId},
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
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        me.id = 'positionEditPanel';
        
        var posiEdit_saveUrl = 'sys/organization/saveposition.f';//保存
        var queryposiStateUrl = 'sys/organization/findpositionstates.f';//岗位状态下拉菜单
       
    	var statusStore = Ext.create('Ext.data.Store', {//机构状态store
    	    fields: ['id', 'text'],
    	    proxy: {
    	         type: 'ajax',
    	         url: queryposiStateUrl,
    	         reader: {
    	             type: 'json',
    	             root: 'datas'
    	         }
    	     }, 
    	    autoLoad:true
    	});
       
    	//岗位编号
    	var positionCode=Ext.create('Ext.form.TextField', {
    	    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.posi.posicode')+'<font color=red>*</font>',
    	    allowBlank:false,//不允许为空
    	    margin: '7 30 3 30',
    	    name:'posicode',
    	    id:'posicodeId',
    	    columnWidth:.5
    	});
    	//岗位名称
    	var positionName=Ext.create('Ext.form.TextField', {
    	    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.posi.posiname')+'<font color=red>*</font>',
    	    allowBlank:false,//不允许为空
    	    margin: '7 30 3 30',
    	    name:'posiname',
    	    id:'posinameId',
    	    columnWidth:.5
    	});
    	//岗位状态
    	 var positionStatus = Ext.create('Ext.form.field.ComboBox', {
    		    store: statusStore,
    		    fieldLabel: '岗位状态',
    		    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示
    		    editable:false,
    		    margin: '7 30 3 30',
    		    queryMode: 'local',
    		    name:'posiStatus',
    		    displayField: 'text',
    		    valueField: 'id',
    		    triggerAction :'all',
    		    columnWidth:.5
    		});
    	//开始时间
    	var posistartTime=Ext.create('Ext.form.field.Date', {
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.startTime'),
    	    margin: '7 30 3 30',
    	    name:'posiStartDateStr',
    	    format:"Y-m-d",
    	    columnWidth:.25
    	});
    	//结束时间
    	var posiendTime=Ext.create('Ext.form.field.Date', {
    	    fieldLabel: FHD.locale.get('fhd.sys.planEdit.entTime'),
    	    margin: '7 30 3 30',
    	    name:'posiEndDateStr',
    	    format:"Y-m-d",
    	    columnWidth:.25
    	});
    	//排列顺序
    	var posiSn=Ext.create('Ext.form.field.Number', {
    			 fieldLabel: '排列顺序',
    			 //allowBlank:false,//不允许为空
    			 margin: '7 30 3 30',
    			 name:'posiSnStr',
    			 id:'psnId',
    			 columnWidth:.5
    		});
    	//备注
    	var posiRemark=Ext.create('Ext.form.field.TextArea', {
    	    fieldLabel: FHD.locale.get('fhd.sys.duty.dutyremark'),
    	    margin: '7 30 3 30',
    	    name:'remark',
    	    id:'premarkId',
    	    columnWidth:.5
    	});
    	
    	var tbar =[//菜单项
    	           '->',
    	           {text : "保存",iconCls: 'icon-save',id:'posisave1${param._dc}', handler:save, scope : this}];
    	var bbar =[//菜单项
    	           '->',
    	           {text : "保存",iconCls: 'icon-save',id:'posisave2${param._dc}', handler:save, scope : this}];
        //保存
    	function save(){
    		var positionGridPanel = Ext.getCmp('positionGridPanel');
    		var treePanel = Ext.getCmp('treePanel');
    		var form = me.getForm();
    		
    		if(form.isValid()){
	    		if(positionGridPanel.isAdd){//新增保存
	    			if("jg" == treePanel.currentNode.data.type){
	    				FHD.submit({
	    				form:form,
	    				url:posiEdit_saveUrl+'?orgId=' + treePanel.currentNode.data.id,//orgId:岗位所属机构id
	    				callback:function(data){
	    					positionGridPanel.store.load();
	    					treePanel.store.load();
	    				}
	    				});	
	    			}else{
	    				FHD.submit({
	    				form:form,
	    				url:posiEdit_saveUrl,
	    				callback:function(data){
	    					positionGridPanel.store.load();
	    					treePanel.store.load();
	    				}
	    				});	
	    			}
	    			
	    		}else{
	    			FHD.submit({//修改岗位
	    				form:form,
	    				url:posiEdit_saveUrl + '?id=' + me.orgtreeId,//岗位节点id
	    				callback:function(data){
	    					positionGridPanel.store.load();
	    					treePanel.store.load();
	    				}
	    			});	
	    		}
    		}
    	}
    	
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "5 5 5 5",
            tbar:tbar,
    		bbar:bbar,
            items: [{
                xtype: 'fieldset',//基本信息fieldset
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
                title: "基本信息",
                items:[
                       //positionName, positionCode, positionStatus, posiSn, startTime, endTime, posiRemark
                       positionName, positionCode, positionStatus, posiSn, posistartTime, posiendTime, posiRemark
					]
            }]
            
        });
        
        me.callParent(arguments);
        
    },
    load:function(){
    	var me = this;
    	if(typeof(me.orgtreeId) != 'undefined'){
        	//编辑时加载form数据
            me.formLoad();
    	}
    }
});