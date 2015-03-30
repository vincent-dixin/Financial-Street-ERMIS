/**
 * 计划任务模版
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.st.TempMan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.tempMan',

    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.queryUrl = 'sys/st/findDictEntryBySome.f?dictEntryId=st_temp_category';
    	me.tempMan_saveUrl = 'sys/st/saveTemp.f';
    	me.editor;
    	me.dictEntryId='';
    	me.tempGridqueryUrl = 'sys/st/findDictEntryByType.f';
    	me.formUrl = 'sys/st/findTempByCategory.f';
    	
    	Ext.define('demopanel',{
    		extend:'Ext.panel.Panel',
    		border:false,
    		autoScroll:false,
    		height:FHD.getCenterPanelHeight(),
    		region: 'center',
    		width:100
    	});
    	
    	me.formText = [//form表单的列
           	{
               	xtype:'textfield',
               	fieldLabel:FHD.locale.get('fhd.sys.tempMan.name')+'<font color=red>*</font>',
               	name:'name',
               	allowBlank:false
            },			
    		{
            	xtype:'textarea',
    	        fieldLabel:FHD.locale.get('fhd.sys.tempMan.content')+'<font color=red>*</font>',
    	        name:'content',
    	        id:'content'
            },
    		{xtype:'hidden', value:'', name:'state',labelHiden:true},
    		{xtype:'hidden', name:'id',labelHiden:true},
    		{xtype:'hidden', name:'dictEntryId',labelHiden:true}
        ];

    	me.gridColums =[
    	                {header:FHD.locale.get('fhd.sys.tempMan.parameter'),dataIndex:'parameter', flex:1},
	               		{header:FHD.locale.get('fhd.sys.tempMan.describe'),dataIndex:'describe', flex:1}
	               ];
	               
       	me.tempGrid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
       		cols:me.gridColums,//cols:为需要显示的列
       		checked:false,
       		searchable:false,
       		pagable:false,
       		listeners:{
       			itemdblclick:function(){//双击执行修改方法
       				var selection = me.tempGrid.getSelectionModel().getSelection()[0];//得到选中的记录
       				me.editor.html(me.editor.html() + selection.data.parameter);
       			}
       		}
       	});
    	
        me.formGrid = [
        	me.tempGrid
        ];

        me.formButtons = [
        	{
      			text:FHD.locale.get('fhd.common.save'),
      			id:"btnEditContentCn",
      			handler:function(){
      				var form = me.tempFormPanel.getForm();
      				if(form.isValid()){
      					//保存
      					FHD.submit({
      						form:form,
      						url:me.tempMan_saveUrl + '?contentEdit=' + encodeURI(me.editor.html()),
      						callback:function(data){
    	  						if(data){
    	  							me.loadForm(me);
    	  	  					}
      						}
          				});
      						
      				}				
      			}
      		}
         	,
      		{
    			text:FHD.locale.get('fhd.common.cancel'),
    			handler:function(){
    				me.loadForm(me);
    			}
    		}
        ];
    	
        me.tempFormPanel = Ext.create('Ext.form.Panel',{
        	bodyPadding:5,
        	region:'center',
    		autoScroll:true,
    		listeners:{
            	render:function(){
    		        setTimeout(function(){
    		        	me.editor = KindEditor.create('#' + (Ext.getCmp("content").getEl().query('textarea')[0]).id);
    		        	me.editor.resizeType = 1;
    		        });
    	        }  
    		},
        	items: [ 
        		{
            		xtype: 'fieldset',
        			defaults:{columnWidth: 1/1,labelWidth:46,margin: '3 3 3 3'},
        			layout:{type: 'column'},
        			title:FHD.locale.get('fhd.common.baseInfo'),
        			items:me.formText
        		},
        		{
            		xtype: 'fieldset',
            		defaults:{columnWidth: 1/1,labelWidth:46,margin: '3 3 3 3'},
        			layout:{type: 'column'},
        			title:FHD.locale.get('fhd.sys.tempMan.gridTitle'),
        			items:me.formGrid
        		}
        	],
        	buttons:me.formButtons
    	});
    	
    	me.taskTree = Ext.create('FHD.ux.TreePanel',{
            useArrows: true,
            rootVisible: false,
            split: true,
            width:220,
            collapsible : true,
            region: 'west',
            multiSelect: true,
            rowLines:false,
            singleExpand: false,
            checked: false,
    		url: me.queryUrl,//调用后台url
    		height:FHD.getCenterPanelHeight(),
    		listeners : {
     			'itemclick' : function(view,re){
    				var form = me.tempFormPanel.getForm();
    				me.editor.html(form.items[1].value);
    				me.dictEntryId = re.data.id;
    				me.tempGrid.store.proxy.url = me.tempGridqueryUrl + '?dictEntryId=' + me.dictEntryId;
    				me.tempGrid.store.load();
					me.loadForm(me);
      			}
     		}
    	});
    	
    	
       
        me.panelDemopanel = Ext.create('demopanel',{
    		items:[me.tempFormPanel]
    	});
        
        Ext.apply(me, {
        	demopanel:me.panelDemopanel,
            height:FHD.getCenterPanelHeight(),
            border:false,
     		layout: {
     	        type: 'border',
     	        padding: '0 0 5	0'
     	    },
     	    defaults: {
                 border:true
            },
     	    items:[me.taskTree, me.tempFormPanel]
        });

        me.callParent(arguments);
	},
        
 	loadForm : function(me){
 		if(typeof(me.dictEntryId) != 'undefined') {
 			me.tempFormPanel.form.load( {
	        url:me.formUrl,
	        params:{dictEntryId:me.dictEntryId},
	        failure:function(form,action) {
	            alert("err 155");
	        },
	        success:function(form,action){
		        if(form.getValues().content != ''){
		        	me.editor.html(form.getValues().content);
			    }else{
			    	me.editor.html('');
				}
		        
	        }
	    });
 		}
 	}
});