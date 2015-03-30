/*
 * File: app/view/KpiGrid.js
 *
 * This file was generated by Sencha Architect version 2.1.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.1.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.1.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('FHD.view.comm.theme.ThemeRecordGrid', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.themerecordgrid',
	title:"统计列表",
 	requires: [
        'FHD.view.comm.theme.TipPanel'
       ],
    dataType : '',
    /**
     * 删除
     */
    analysisDelFun: function () {
        var me = this;
        var selection = me.getSelectionModel().getSelection();//得到选中的记录
        var themetree = Ext.getCmp('themetree');
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') { //确认删除
                	var ids = [];
    				for(var i=0;i<selection.length;i++){
    					ids.push(selection[i].get('id'));
    				}
                    FHD.ajax({
                        url: me.delUrl,
                        params: {
                            layInfoIds:ids.join(','),
                            themeId:themetree.currentNode.data.id
                        },
                        callback: function (data) {
                            if (data) {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
    							me.store.load();
                            }
                        }
                    });
                }
            }
        });
    },
    
 
    /**
     * 添加
     */
    analysisAddFun:function(){
    	var analysismainpanel = Ext.getCmp('analysismainpanel');
    	var analysiscardpanel = Ext.getCmp('analysiscardpanel');
    	var analysislayoutform = Ext.getCmp('analysislayoutform');
    	var paramObj = {};
    	paramObj.editflag = false;
    	analysismainpanel.initParam(paramObj);
    	analysiscardpanel.setInitBtnState();//设置导航按钮初始化状态,添加时,后两个按钮置灰;
    	Ext.getCmp('themecentercardpanel').setActiveItem(analysismainpanel);
    	//清空analysisgatherform数据
    	analysislayoutform.clearFormData();
    	//初始化analysisgatherform默认值
    	analysislayoutform.initFormData();
    	//恢复到第一个面板
    	analysiscardpanel.setFirstItemFoucs(true);
    	//设置导航条
    	Ext.getCmp('analysismainpanel').reLoadNav();
		//清除列表选中的记录    	
    	var themerecordgrid = Ext.getCmp('themerecordgrid');
    	themerecordgrid.layoutInfoId='';
    	
    	var analysislayoutpanel = Ext.getCmp('analysislayoutpanel');
    	analysislayoutpanel.categoryPanel();
    	 var analysislayoutpanel2 = Ext.getCmp('analysislayoutpanel2');
    	analysislayoutpanel2.categoryPanel();
    	 var analysislayoutpanel3 = Ext.getCmp('analysislayoutpanel3');
    	analysislayoutpanel3.categoryPanel();
    	
    	
    	Ext.getCmp('theme_move-next_top' ).setDisabled(false);
        Ext.getCmp('theme_move-prev_top' ).setDisabled(true);
        Ext.getCmp('theme_move-next' ).setDisabled(false);
        Ext.getCmp('theme_move-prev' ).setDisabled(true);
        Ext.getCmp('theme_caculate_btn_top' ).setDisabled(false);
        Ext.getCmp('theme_caculate_btn' ).setDisabled(false);
    },
    /**
     * 编辑
     */
    analysisEditFun:function(){
    	var me = this;
        var selections = me.getSelectionModel().getSelection();
        var length = selections.length;
        if (length > 0) {
            if (length >= 2) {
                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.kpi.kpi.prompt.editone'));
                return;
            } else {
                var selection = selections[0]; //得到选中的记录
                var id = selection.get('id'); //获得ID
                var name = selection.get('name');
                var analysislayoutform = Ext.getCmp('analysislayoutform');
                var analysismainpanel = Ext.getCmp('analysismainpanel');
                var analysiscardpanel = Ext.getCmp('analysiscardpanel');
                //设置analysislayoutform中的参数
                analysislayoutform.paramObj.id = id;
                analysislayoutform.paramObj.name = name;
                //设置analysismainpanel中的参数
                analysismainpanel.paramObj.editflag = true;
                //设置analysismainpanel为当前激活面板
                Ext.getCmp('themecentercardpanel').setActiveItem(analysismainpanel);
                //恢复到第一个面板
            	analysiscardpanel.setFirstItemFoucs(false);
            	me.layoutInfoId = id;
            	//加载form数据
                analysislayoutform.formLoad(id);
                //设置导航条
            	Ext.getCmp('analysismainpanel').reLoadNav();
            }
        } else {
            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条指标.');
            return;
        }
        
        var analysislayoutpanel = Ext.getCmp('analysislayoutpanel');
        analysislayoutpanel.categoryPanel();
    	var analysislayoutpanel2 = Ext.getCmp('analysislayoutpanel2');
    	analysislayoutpanel2.categoryPanel();
    	var analysislayoutpanel3 = Ext.getCmp('analysislayoutpanel3');
    	analysislayoutpanel3.categoryPanel();
    	
    },
    //列表监听事件
    addListerner: function () {
        var me = this;
        me.on('selectionchange', function () {
            if (me.down('#theme_edit' )) {
                me.down('#theme_edit' ).setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
            if (me.down('#theme_del' )) {
                me.down('#theme_del' ).setDisabled(me.getSelectionModel().getSelection().length === 0);
            }

        }); //选择记录发生改变时改变按钮可用状态

    },
    
    click : function (val){
    	var me = this;
    	var valArray = val.split(",");
    	var type = valArray[1];
    	var id = valArray[0];
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
    	FHD.ajax({
            url: __ctxPath + '/ic/theme/findLayoutDetailedByIdToJson.f',
            params: {
                id: id,
                layoutType:type,
                condItem: Ext.JSON.encode(paraobj)
            },
            callback: function (data)  {
      
	    		if(data.data!=null){
	    		var tippanel = me.tippanel;
	    		tippanel.categoryPanel(data.data,type);
	    		var layoutwindow = Ext.create('Ext.Window',{
			        constrain: true,
			        layout: 'auto',
			        resizable:false,
			        modal: true, //是否模态窗口
			        collapsible: true,
			        scroll: 'auto',
			        closeAction:'distory',
			        width: 1200,
			        height: 680,
			        items: [tippanel]
	    	    });
	    	    layoutwindow.show();
	    	   }
		    	
            }
        });
	},
                //获取当前年份
    getYear : function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    },
    reloadData: function() {
    	var me = this;
    	me.store.load();
    },
    initComponent: function() {
        var me = this;
        var layoutInfoId = null;
        me.queryUrl = 'ic/theme/querylayinfoandlaytypepage.f';//列表查询url
        me.delUrl = 'ic/theme/removelayinfosbyinfoids.f';//列表删除url
    	me.tippanel = Ext.widget('tippanel');
        Ext.apply(me, {
        	checked: true,
        	//url : __ctxPath + "/kpi/category/findcategoryrelakpiresult.f",
            extraParams:{
            	id : me.categoryid,
            	year : FHD.data.yearId,
            	month : FHD.data.monthId,
            	quarter : FHD.data.quarterId,
            	week : FHD.data.weekId,
            	eType : FHD.data.eType,
            	isNewValue : FHD.data.isNewValue
            },
            tbarItems: [{
//                tooltip: FHD.locale.get('fhd.kpi.kpi.op.addkpi'),
            	text:'添加',
                iconCls: 'icon-add',
                id: 'theme_add',
                handler: function () {
                    me.analysisAddFun();
                }
            },  '-', {
//                tooltip: FHD.locale.get("fhd.kpi.kpi.toolbar.editkpi"),
            	text:'修改',
                id: 'theme_edit',
                iconCls: 'icon-edit',
                disabled: true,
                handler: function () {
                   me.analysisEditFun();
                }
            }, '-', {
            	text:'删除',
                id: 'theme_del',
                iconCls: 'icon-del',
                disabled: true,
                handler: function () {
                  me.analysisDelFun();
                }
            }

            ],
            cols:[
       		     {
	            	header: 'id' ,
	            	dataIndex: 'id',
	            	sortable: true,
	            	flex : 1,
	            	hidden : true
            	},{  
       		        header: '布局', 
       		        dataIndex: 'layoutType', 
       		        flex : 0.3,
					xtype:'actioncolumn',
					renderer: function (value) {
			                if(value == '1'){
			                	return "<img src="+__ctxPath+"/images/icons/layout1.png width=24>"
			                }else  if(value == '2'){
			                	return "<img src="+__ctxPath+"/images/icons/layout2.png width=24>"
			                }else  if(value == '3'){
			                	return "<img src="+__ctxPath+"/images/icons/layout3.png width=24>"
			                }
			               
		            	}
				},
    			{
	    			header: '名称',
	    			dataIndex: 'layoutName',
	    			sortable: true,
	    			flex : 0.5,
	    			editor: {allowBlank: false},
	    			tooltip: '预览',
	    			renderer:function(value,meta, record){
	    				var id = record.data.id;
	    				var type = record.data.layoutType;
		            	return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('themerecordgrid').click('"+id+","+type+"')\" >"+value+"</a>";
           			 }
					           
    			},
    			{
	    			header: '类型',
	    			dataIndex: 'layoutType',
	    			sortable: true, 
	    			flex : 1,
	    			editor:{allowBlank: false},
	    			renderer:function(value){
		            	if(value=='1'){
		            		return "布局一";
		            	}else if(value=='2'){
		            		return "布局二";
		            	}else if(value=='3'){
		            		return "布局三";
		            	}
           			 }
	    		}
	    		
    			],
            
            type: 'themerecordgrid'
        });
        
        me.callParent(arguments);
        
        me.addListerner();
    }

});