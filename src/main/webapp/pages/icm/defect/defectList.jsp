<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
	/***attribute start***/

 Ext.define('defect_defectPanel',{
    	extend:'FHD.ux.GridPanel',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	defectIds:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    		var me=this;
    	me.tbarItems=[{iconCls : 'icon-add',id : 'add${param._dc}',handler :me.addDefect,scope : this}, 
    	    	     // {iconCls : 'icon-edit',id : 'upd',handler :me.editDefect,scope : this}, 
    	    	      {iconCls : 'icon-del',id : 'del${param._dc}',handler :me.delDefect,scope : this}
    	    	     ];
    	me.cols=[ {header : 'ID',dataIndex : 'id',hidden:true,sortable : true},
                  {header : '编号',dataIndex : 'code',sortable : true,flex : 1}, 
                  {header : '缺陷描述',dataIndex : 'desc',sortable : true,flex : 1},
                  {header : '所属公司',dataIndex : 'companyId',sortable : true,flex : 1}, 
                  {header : '缺陷等级',dataIndex :'controlRequirement',sortable : true,}, 
                  {header : '缺陷类型',dataIndex : 'type',sortable : true}, 
                  {header : '缺陷状态',dataIndex : 'dealstatus',sortable : true},
                  {header : '提交状态',dataIndex : 'status',sortable : true},
                  {
  		            xtype:'actioncolumn',
  		            dataIndex : 'id',
  		            text:'操作',
  		            items: [{
  		                icon:__ctxPath+'/images/icons/edit.gif',
  		               // dataIndex : 'id',
  		                tooltip: '编辑',
  		                handler: function(grid, rowIndex, colIndex) {
              	     	var row=grid.store.getAt(rowIndex)
				    	var defectId=row.get('id');
              	  	defect_ManagerView.remove(defect_ManagerView.gridPanel);
              	    defect_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
              					autoLoad : {
              						url : 'pages/icm/defect/defectEdit.jsp?defectId='+defectId+'&type=yes',

              						scripts : true
              					}
              				});
              		defect_ManagerView.add(defect_ManagerView.gridPanel);
  		                }
  		            },{
  		                icon:__ctxPath+'/images/icons/edit.gif',
   		               // dataIndex : 'id',
   		                tooltip: '缺陷跟踪',
   		                handler: function(grid, rowIndex, colIndex) {
               	     	var row=grid.store.getAt(rowIndex)
 				    	var defectId=row.get('id');
               	  	defect_ManagerView.remove(defect_ManagerView.gridPanel);
               	    defect_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
               					autoLoad : {
               						url : 'pages/icm/defect/defectFollowManage.jsp?defectId='+defectId,

               						scripts : true
               					}
               				});
               		defect_ManagerView.add(defect_ManagerView.gridPanel);
   		                }
   		            }]
                  }
                  /* {xtype : 'templatecolumn',text : $locale('fhd.common.edit'),align : 'center',dataIndex : 'id',
                	  tpl : '<font class="icon-edit" ">&nbsp&nbsp&nbsp</font>',
                	  listeners : {
                	     click :function(){
                	    	 //选择编辑评价计划，首先初始化form               	  
                	     	 var selection = me.getSelectionModel().getSelection();//得到选中的记录
                	     	 var defectId=selection[0].get('id');
                	     	defect_ManagerView.remove(defect_ManagerView.gridPanel);
                    		defect_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
                				autoLoad : {
                					url : 'pages/icm/defect/defectEdit.jsp?defectId='+defectId,

                					scripts : true
                				}
                			});
                    		defect_ManagerView.add(defect_ManagerView.gridPanel);
                	     	
                	     
                	          }
                	      } 
                	 } */
                  ];
    	
    	me.callParent(arguments);
    	},
    	
    	
    	//添加 的
    	addDefect:function(){
    		defect_ManagerView.remove(defect_ManagerView.gridPanel);
    		defect_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
				autoLoad : {
					url : 'pages/icm/defect/defectEdit.jsp',

					scripts : true
				}
			});
    		defect_ManagerView.add(defect_ManagerView.gridPanel);
    	},
    	//编辑
    /*     editDefect:function(){

        	var me=this;
    	    var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	    defectID=selection[0].get('id');
        	defect_ManagerView.remove(defect_ManagerView.gridPanel);
    	    defect_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
            autoLoad : {
            url : 'pages/icm/defect/defectEdit.jsp?defectID='+defectID,
            scripts : true	
                }
             });
    	
    	 defect_ManagerView.add(defect_ManagerView.gridPanel);
        	
        }, */
        //删除
        delDefect:function(){
        	var me=this;
        	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    		Ext.MessageBox.show({
    			title : FHD.locale.get('fhd.common.delete'),
    			width : 260,
    			msg : FHD.locale.get('fhd.common.makeSureDelete'),
    			buttons : Ext.MessageBox.YESNO,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn) {
    		     if (btn == 'yes') {//确认删除
    			 var ids = [];
    			 for ( var i = 0; i < selection.length; i++) {
    			  ids.push(selection[i].get('id'));
    			 }
    			 FHD.ajax({//ajax调用
    		     url : __ctxPath+ '/icm/defect/removeDefectByIdBatch.f',
    		     params : {
    		     defectIds : ids
    			 },
    			 callback : function(data) {
    			 if (data) {//删除成功！
    			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
    			 me.store.load();
    		   }
    		   }
    		});
    		}
    			}
    		});
        	
        	
        },
    });
    
     var defect_defectPanel=null;
	Ext.onReady(function() {
	Ext.QuickTips.init();
	defect_defectPanel= Ext.create('defect_defectPanel',{
			renderTo: 'FHD.icm.defect.defectList${param._dc}',
		    height : FHD.getCenterPanelHeight() - 3,//高度为：获取center-panel的高度
			listeners : {
			}
		});
	defect_defectPanel.store.proxy.url=__ctxPath+ '/icm/defect/findDefectListBypage.f?assessPlanId=${param.assessPlanId}',//调用后台url
	defect_defectPanel.store.load();
	defect_defectPanel.store.on("load",function(store){
	  //  Ext.getCmp('upd').setDisabled(true);//编辑不可用
  	    Ext.getCmp('del${param._dc}').setDisabled(true);
	 });
	defect_defectPanel.on('selectionchange',function(m) {
	var len=defect_defectPanel.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	  // Ext.getCmp('upd').setDisabled(true);//编辑不可用
	   Ext.getCmp('del${param._dc}').setDisabled(true);
	   } else if (len == 1) {
	  // Ext.getCmp('upd').setDisabled(false);//编辑可用
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   } else if (len > 1) {
	  // Ext.getCmp('upd').setDisabled(true);//编辑不可用
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   }
	 });	 
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.defect.defectList${param._dc}'></div>
</body>
</html>