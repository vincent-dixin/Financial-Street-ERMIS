/*
 * 整改涉及的缺陷列表
 * 入参：improveId  整改计划ID
 */
 Ext.define('FHD.view.icm.defect.component.DefectRelaImproveGrid',{
 	extend: 'Ext.container.Container',
    alias: 'widget.defectrelaimprovegrid',
    layout:'fit',
    editable:true,
    width:300,
    border:false,
    addDefectRelaImprove: function(){
    	var me = this;
		var selectorWindow = Ext.create('FHD.ux.icm.defect.DefectSelectorWindow', {
			onSubmit : function(win) {
				var defectIdArray = new Array();
				win.selectedGrid.store.each(function(r){
            		defectIdArray.push(r.data.id);
            	});
				FHD.ajax({
					url : __ctxPath + '/defect/mergedefectrelaimprovebatch.f',
					params : {
						defectIds:defectIdArray.join(','),
						improveId:me.improveId
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							me.defectRelaImproveGrid.store.load();
						}
					}
				});
			}
		}).show();
    },
    addDefectRelaImproveFromAssessPlan: function(){
    	var me = this,
    		selectorWindow = Ext.create('FHD.ux.icm.assess.AssessPlanSelectorWindow', {
			onSubmit : function(win) {
				var assessPlanIdArray = new Array();
            	win.selectedGrid.store.each(function(r){
            		assessPlanIdArray.push(r.data.id);
            	});
            	FHD.ajax({
					url : __ctxPath + '/defect/mergedefectrelaimprovebatch.f',
					params : {
						assessPlanIds:assessPlanIdArray.join(','),
						improveId:me.improveId
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							me.defectRelaImproveGrid.store.load();
						}
					}
				});
			}
		}).show();
    },
    deleteDefectRelaImprove: function(){
    	var me=this;
		var selection = me.defectRelaImproveGrid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {//确认删除
 					var defectRelaImproveIdArray = new Array();
					for ( var i = 0; i < selection.length; i++) {
  						defectRelaImproveIdArray.push(selection[i].get('id'));
 					}
 					FHD.ajax({//ajax调用
 						url : __ctxPath+ '/defect/removedefectrelaimprovebatch.f',
 						params : {
 							defectRelaImproveIds : defectRelaImproveIdArray.join(',')
 						},
 						callback : function(data) {
 							if (data) { //删除成功！
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
	 							me.defectRelaImproveGrid.store.load();
                            } else {
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                            }
   						}
					});
				}
			}
		});
    },
    setstatus: function(){
    	var me = this;
        var length = me.defectRelaImproveGrid.getSelectionModel().getSelection().length;
        if(me.defectRelaImproveGrid.down('#del')){
        	me.defectRelaImproveGrid.down('#del').setDisabled(length === 0);
        }
    },
    initComponent: function(){
    	var me = this,
    		tbarItems = [
				{iconCls : 'icon-add',handler :me.addDefectRelaImprove, tooltip: '直接从缺陷库选择缺陷', text:'选择缺陷', scope : this},
				'-', 
				{iconCls : 'icon-link',handler :me.addDefectRelaImproveFromAssessPlan, tooltip: '从评价计划中选择缺陷', text:'选择评价计划', scope : this},
				'-', 
				{iconCls : 'icon-del',id:'del',handler :me.deleteDefectRelaImprove, tooltip: '从当前选择的缺陷中取消选择', text:'取消选择', disabled: true,scope : this}
			];
    	me.defectRelaImproveGrid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            searchable:false,
            pagable:false,
            border: me.border,
            checked:me.editable,
            //url: __ctxPath + '/defect/findrectifydefectbyimproveid.f?improveId='+me.improveId, //调用后台url
            cols: [
            	{dataIndex:'id',width:0},
            	{header : '责任部门',dataIndex : 'orgName',sortable : true, flex : 1, renderer : function(value, metaData, record, colIndex, store, view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	return value;  
					}
				}, 
	    		{header : '缺陷描述',dataIndex : 'desc',sortable : true, flex : 2, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	//return "<a href=\"javascript:void(0);\" onclick=\"viewDefect('" + record.get('id') + "')\">"+value+"</a>";  
						return value;
					}
				}, 
	    		
	 			{header : '缺陷等级',dataIndex : 'level',sortable : true}, 
	 			{header : '缺陷类型',dataIndex :'type',sortable : true}
			],
            tbarItems:me.editable?tbarItems:[] 
        });
		me.defectRelaImproveGrid.store.on('load', function () {
            me.setstatus()
        });
        me.defectRelaImproveGrid.on('selectionchange', function () {
            me.setstatus()
        });
        me.callParent(arguments);
        me.add(me.defectRelaImproveGrid);
    },
    loadData: function(improveId){
    	var me = this;
    	me.improveId = improveId;
    	me.reloadData();
    },
    reloadData:function(){
		var me=this;
		if(me.improveId){
			me.defectRelaImproveGrid.store.proxy.url=__ctxPath + '/defect/findrectifydefectbyimproveid.f';
			me.defectRelaImproveGrid.store.on('beforeload', function (store, options) {
				var new_params = {improveId: me.improveId };
		        Ext.apply(store.proxy.extraParams, new_params);
	    	});
			me.defectRelaImproveGrid.store.load();
		}
	}
 });