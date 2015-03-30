/**
 * 菜单基本信息添加面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.sys.menu.MenuManagementFormAddPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.menumanagementformaddpanel',
    requires: [
               'FHD.view.sys.menu.MenuManagementTabGridEditPanel',
               'FHD.view.sys.menu.MenuManagementBtnGridEditPanel'
        ],
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
    	
        var me = this;
        me.menuSaveUrl = 'menu/menuManagement/saveMenuInfo.f';//保存
        me.findByIdUrl = 'menu/menuManagement/findMenuInfoById.f';//根据ID查询菜单实体
        
    	//菜单编号
    	me.menuCode=Ext.create('Ext.form.TextField', {
    	    fieldLabel: '菜单编号'+'<font color=red>*</font>',
    	    allowBlank:false,//不允许为空
    	    name:'authorityCode'
    	});
    	//菜单名称
    	var menuName=Ext.create('Ext.form.TextField', {
    	    fieldLabel: '菜单名称'+'<font color=red>*</font>',
    	    allowBlank:false,//不允许为空
    	    name:'authorityName'
    	});
    	//上级菜单名称
    	 me.parentMenu = Ext.create('Ext.form.TextField', {
    		 	 disabled:true,
    			 fieldLabel: '上级菜单名称',
    			 allowBlank:false,
    		     name:'parentMenu'
    		  });
    	//排列顺序
    	 var sn=Ext.create('Ext.form.field.Number', {
     	    fieldLabel: '排列顺序'+'<font color=red>*</font>',
     	    allowBlank:false,//不允许为空
     	    name:'sn',
     	    value:'1'
     	});
    	// 菜单层次
    	 var rank = Ext.create('Ext.form.field.Number', {
    		    fieldLabel: '菜单层次',
    		    name:'rank',
    		    value :'1'
    		});
    	//是否叶子
    	 
    	 var isLeaf = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
    		 emptyText:FHD.locale.get('fhd.common.pleaseSelect'),
             editable: false,
             multiSelect: false,
             name: 'isLeafs',
             dictTypeId: '0yn',
             fieldLabel: '是否子叶'+ '<font color=red>*</font>', 
             labelAlign: 'left',
             allowBlank: false
         });
    	//链接地址
    	var url=Ext.create('Ext.form.TextField', {
    		    fieldLabel: '链接地址',
    		    name:'url'
    		});
    	//样式
//    	var etype = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
//    		emptyText:FHD.locale.get('fhd.common.pleaseSelect'),
//            editable: false,
//            multiSelect: false,
//            name: 'etype',
//            dictTypeId: '0css',
//            fieldLabel: '样式'+ '<font color=red>*</font>', 
//            labelAlign: 'left',
//            allowBlank: false
//        });
    	//图标地址
    	var icon=Ext.create('Ext.form.TextField', {
    			 fieldLabel: '图标',
    			 name:'icon'
    		});
    	
    	
    	var tbar =[//按钮
    	           '->',
    	           {text : "保存",iconCls: 'icon-save', handler:me.save, scope : this}];
    	           
    	 me.tadEditGrid = Ext.widget('menumanagementtabgrideditpanel');

    	 me.btnEditGrid = Ext.widget('menumanagementbtngrideditpanel');
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            tbar:tbar,
            height:500,
            autoScroll:true,
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                title: "菜单信息",
                defaults: {
                    columnWidth : 1 / 2,
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
                layout: {
                    type: 'column'
                },
                items:[
                       me.menuCode,menuName,me.parentMenu,sn , isLeaf, rank, icon,url
					]
            },{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                title: "页签信息",
                defaults: {
                    columnWidth : 1,
                    labelWidth: 95
                },
                layout: {
                    type: 'column'
                },
                items:[
                       me.tadEditGrid
					]
               
            
            },{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                title: "按钮信息",
                defaults: {
                    columnWidth : 1,
                    margin: '5 5 5 5',
                    labelWidth: 95
                },
                layout: {
                    type: 'column'
                },
                items:[
                       me.btnEditGrid
					]
               
            
            }]
            
        });
        me.callParent(arguments);
        me.currentId = 'root';	//加载根节点
        me.loadData();
    },
    loadData:function(){	//初始化右侧面板方法 默认根节点
    	var me = this;
		me.load({
    	        	url:me.findByIdUrl,
	    	        params:{
	    	        	id:me.currentId
	    	        },
	    	        failure:function(form,action) {
	    	            alert("err 155");
	    	        },
	    	        success:function(form,action){
	    	        	var tree = me.up('menumanagementmainpanel').treePanel;
	    	        	if( 'root'==me.currentId ){
	    	        		tree.currentNode = tree.getRootNode().firstChild;
	    	        	}
	    	        	me.currentId = action.result.data.currentId;
	    	        }
	    	    });
	},   
		//保存
	 save:function(){
		var me = this;
			FHD.ajax({
				url: __ctxPath + '/menu/menuManagement/findfindMenuInfoByCodeIfExist.f',//判断菜单编号是否存在
				params : {
					menuCodes:me.menuCode.getValue(),
					currentIds:me.currentId
				},
				callback : function(data){
					if(data.success){
						return Ext.Msg.alert('提示','菜单编号不能重复');
					}
					me.parentId = me.parentId ||'root';
			 		var form = me.getForm();
			 		if(form.isValid()){
			 			//保存
			 			FHD.submit({
			 				form:form,
			 				url:me.menuSaveUrl + '?id=' + me.currentId +'&parentId=' + me.parentId ,
			 				callback:function(result){
			 					//me.up('menumanagementmainpanel').treePanel.store.load();
			 					var tree = me.up('menumanagementmainpanel').treePanel;
			 					var node =  {id:result.data.id,text:result.data.text,leaf:result.data.leaf};
			 					if(me.currentId){
			 						var data = tree.currentNode.data;
			 						data.text = node.text;
			 						data.leaf = node.leaf;
			 						tree.currentNode.updateInfo(data);
			 					}else{
				 					me.currentId = result.data.id;
				 					tree.currentNode.appendChild(node);
			 						tree.getSelectionModel().select(tree.currentNode.lastChild);
			 						tree.currentNode = tree.currentNode.lastChild;
			 					}
			 					//me.loadData();
			 					me.tadEditGrid.store.load();
			 				}
			 			});	
			 		}

				}
			})
 		
 	}
});