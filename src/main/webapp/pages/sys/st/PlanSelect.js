Ext.define('Ext.app.view.sys.PlanSelect',{
	alias: 'widget.planSelect',
	
	
		hourSelect : function(){

		//触发时间
	  	dt.allowBlank = true;
		
		//每几小时不能为空
		Ext.getCmp('everyHourId').allowBlank = false;
		Ext.getCmp('everyHourId').regex = re;
		Ext.getCmp('everyHourId').regexText = reText;
		
		//天可以为空
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');
		
		//月可以为空
		//选项1
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');
		//选项2
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;	
		Ext.getCmp('everyNumId').setValue('');	
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');
		
		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//年可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;
		Ext.getCmp('monthNumId').setValue('');
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
	},

	//天选项1
	daySelect1 : function(){
		//天不能为空					            	
		Ext.getCmp('everyDay').allowBlank = false;
		Ext.getCmp('everyDay').regex = re;
		Ext.getCmp('everyDay').regexText = reText;
		
		//小时可以为空
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');
		
		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//月可以为空
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;
		Ext.getCmp('everyNumId').setValue('');		
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');
		
		//年可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;	
		Ext.getCmp('monthNumId').setValue('');	
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
	},

	//天选项2
	daySelect2 : function(){
		//天可以为空					            	
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');

		//小时可以为空
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');
		
		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//月可以为空
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');	
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;		
		Ext.getCmp('everyNumId').setValue('');	
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');	
		
		//年可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;		
		Ext.getCmp('monthNumId').setValue('');
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
	},

	//月选项1
	monthSelect1 : function(){
		//月不能为空
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = false;
		Ext.getCmp('everyMonth1Id').regex = re;
		Ext.getCmp('everyMonth1Id').regexText = reText;
		
		Ext.getCmp('everyDay2Id').allowBlank = false;	
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;
		Ext.getCmp('everyNumId').setValue('');
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');
		
		//小时可以为空
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');
		
		//天可以为空
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');
		
		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//年	可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;	
		Ext.getCmp('monthNumId').setValue('');	
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
	},

	weekSelect : function(){
		//周不能为空
		Ext.getCmp('everyWeeksId').allowBlank = false;
		Ext.getCmp('everyWeeksId').regex = re;
		Ext.getCmp('everyWeeksId').regexText = reText;
		
		//小时可以为空
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');
		
		//天可以为空
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');
		
		//月可以为空
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');	
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;		
		Ext.getCmp('everyNumId').setValue('');
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');
		
		//年可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;		
		Ext.getCmp('monthNumId').setValue('');
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
	},

	//月选项2
	monthSelect2 : function(){
		//月可以为空
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');
			
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = false;
		Ext.getCmp('everyMonth2Id').regex = re;
		Ext.getCmp('everyMonth2Id').regexText = reText;
		
		Ext.getCmp('everyNumId').allowBlank = false;		
		Ext.getCmp('everyWeeksNumId').allowBlank = false;
		
		//小时可以为空
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');
		
		//天可以为空
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');
		
		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//年	可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;
		Ext.getCmp('monthNumId').setValue('');		
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
	},

	//年选项1
	yearSelect1 : function(){
		//年不能为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = false;		
		Ext.getCmp('dayNum').allowBlank = false;
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = true;
		Ext.getCmp('monthNum2Id').setValue('');
		Ext.getCmp('everyNum2Id').allowBlank = true;
		Ext.getCmp('everyNum2Id').setValue('');
		Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
		Ext.getCmp('everyWeeksNum2Id').setValue('');
		
		//小时
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');

		//天
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');

		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//月
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');	
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;
		Ext.getCmp('everyNumId').setValue('');		
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');
	},

	//年选项2
	yearSelect2 : function(){
		//年可以为空
		//选项1
		Ext.getCmp('monthNumId').allowBlank = true;		
		Ext.getCmp('monthNumId').setValue('');
		Ext.getCmp('dayNum').allowBlank = true;
		Ext.getCmp('dayNum').setValue('');
		//选项2
		Ext.getCmp('monthNum2Id').allowBlank = false;
		Ext.getCmp('everyNum2Id').allowBlank = false;
		Ext.getCmp('everyWeeksNum2Id').allowBlank = false;
		
		//小时
		Ext.getCmp('everyHourId').allowBlank = true;
		Ext.getCmp('everyHourId').regex = null;
		Ext.getCmp('everyHourId').regexText = '';
		Ext.getCmp('everyHourId').setValue('');

		//天
		Ext.getCmp('everyDay').allowBlank = true;
		Ext.getCmp('everyDay').regex = null;
		Ext.getCmp('everyDay').regexText = '';
		Ext.getCmp('everyDay').setValue('');

		//周可以为空
		Ext.getCmp('everyWeeksId').allowBlank = true;
		Ext.getCmp('everyWeeksId').regex = null;
		Ext.getCmp('everyWeeksId').regexText = '';
		Ext.getCmp('everyWeeksId').setValue('');
		
		//月
		//选项1					            	
		Ext.getCmp('everyMonth1Id').allowBlank = true;
		Ext.getCmp('everyMonth1Id').regex = null;
		Ext.getCmp('everyMonth1Id').regexText = '';
		Ext.getCmp('everyMonth1Id').setValue('');
		
		Ext.getCmp('everyDay2Id').allowBlank = true;
		Ext.getCmp('everyDay2Id').setValue('');	
		//选项2					            	
		Ext.getCmp('everyMonth2Id').allowBlank = true;
		Ext.getCmp('everyMonth2Id').regex = null;
		Ext.getCmp('everyMonth2Id').regexText = '';
		Ext.getCmp('everyMonth2Id').setValue('');
		
		Ext.getCmp('everyNumId').allowBlank = true;	
		Ext.getCmp('everyNumId').setValue('');	
		Ext.getCmp('everyWeeksNumId').allowBlank = true;
		Ext.getCmp('everyWeeksNumId').setValue('');
	},
	//发送方式
	lookTypeCheck : function(){
		var count = 0;
		for(var i = 0; i < lookTypeDictRadio.items.length; i++){
			if(lookTypeDictRadio.items.items[i].checked){
				count = 1
				break;
	        }
		}
		if(count == 0){
			Ext.Msg.alert(FHD.locale.get('fhd.sys.planEdit.message'),FHD.locale.get('fhd.sys.planEdit.vLookTypeMessage'));
			return false;
		}else{
			return true;
		}
	},
	
	//按周,星期
	weekCheckboxGroupCheck : function(){
		if(Ext.getCmp('everyWeeksId').value != ''){
			var count = 0;
			for(var i = 0; i < weekCheckboxGroup.items.length; i++){
				if(weekCheckboxGroup.items.items[i].checked){
					count = 1
					break;
		        }
			}
			if(count == 0){
				Ext.Msg.alert(FHD.locale.get('fhd.sys.planEdit.message'),FHD.locale.get('fhd.sys.planEdit.vWeekMessage'));
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},

	//开始、结束时间清空
	setTimeNull : function(){
		Ext.getCmp('startTimeid').setValue('');
		Ext.getCmp('endTimeid').setValue('');
	},
	
	triggerTypeTimeSelect : function(){
		Ext.getCmp('sjxx').show();
			triggerDataFieldDate.show() ;
			radioGroup1.items.items[0].checked = true;
		radioGroup1.items.items[0].setValue(true);
			//触发时间
      	dt.allowBlank = false;
      	//触发日期
      	triggerDataFieldDate.allowBlank = false;
      	planSelect.setTimeNull();
	},
	
	triggerTypeAddSelect : function(){
		Ext.getCmp('sf1').hide();
		Ext.getCmp('sjxx').hide();
		//触发时间
        dt.allowBlank = true;
        //触发日期
        triggerDataFieldDate.allowBlank = true;
        //开始日期
        Ext.getCmp('startTimeid').allowBlank = true;
        //结束日期
        Ext.getCmp('endTimeid').allowBlank = true;

        //每几小时不能为空
    	Ext.getCmp('everyHourId').allowBlank = true;

    	//天可以为空
    	Ext.getCmp('everyDay').allowBlank = true;
    	
    	//月可以为空
    	//选项1					            	
    	Ext.getCmp('everyMonth1Id').allowBlank = true;
    	Ext.getCmp('everyDay2Id').allowBlank = true;	
    	//选项2					            	
    	Ext.getCmp('everyMonth2Id').allowBlank = true;
    	Ext.getCmp('everyNumId').allowBlank = true;		
    	Ext.getCmp('everyWeeksNumId').allowBlank = true;
    	
    	//周可以为空
    	Ext.getCmp('everyWeeksId').allowBlank = true;
    	
    	//年可以为空
    	//选项1
    	Ext.getCmp('monthNumId').allowBlank = true;		
    	Ext.getCmp('dayNum').allowBlank = true;
    	//选项2
    	Ext.getCmp('monthNum2Id').allowBlank = true;
    	Ext.getCmp('everyNum2Id').allowBlank = true;
    	Ext.getCmp('everyWeeksNum2Id').allowBlank = true;
    	planSelect.setTimeNull();
	},
	
	singleSelect : function(){
		triggerDataFieldDate.show() ;
    	Ext.getCmp('sf1').hide();
    	Ext.getCmp('sf2').hide();
    	//触发时间
    	dt.allowBlank = false;
    	//触发日期
    	triggerDataFieldDate.allowBlank = false;
    	planSelect.setTimeNull();
    	dt.allowBlank = true;
	},
	
	periodSelect : function(){
		triggerDataFieldDate.hide();
    	Ext.getCmp('sf1').show();
    	Ext.getCmp('sf2').show();
    	
    	//触发时间
    	dt.allowBlank = false;
    	//触发日期
    	triggerDataFieldDate.allowBlank = true;

    	//开始日期
    	Ext.getCmp('startTimeid').allowBlank = false;
    	//结束日期
    	Ext.getCmp('endTimeid').allowBlank = false;

    	//天不能为空					            	
    	Ext.getCmp('everyDay').allowBlank = false;
	}
});