/**
 * 计划任务编辑模版方法支持
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.st.PlanEditFind',{
	alias: 'widget.planEditFind',
	
	findPlanById : function(formPanel, 
								planId, 
								queryUrl, 
								triggerTypeComboBox, 
								triggerDataFieldDate, 
								appointDictRadio,
								planSelect,
								lookTypeDictRadio,
								tempStore,
								tempComboBox,
								dt, 
								re, 
								reText,
								dayByMonthStore,
								severalStore,
								weekStore,
								weekCheckboxGroup,
								monthStore,
								dayStore){
		if(typeof(planId) != 'undefined') {
			formPanel.form.load({
		        url:queryUrl,
		        params:{id:planId},
		        failure:function(form,action) {
		            alert("err 155");
		        },
		        success:function(form,action){
			        var formValue = form.getValues();
			        var triggerDataSet = formValue.triggerDataSet;
			        //开始任务
			        for(var i = 0; i < triggerTypeComboBox.store.totalCount; i++){
			        	var record = triggerTypeComboBox.store.getAt(i).get('id'); 
			        	if(record == formValue.triggerType){
			        		triggerTypeComboBox.setValue(record);
			        		//是否为时间触发
			        		if(record == "st_trigger_type_time"){
			        			Ext.getCmp('sjxx').show();
			        			//是否为周期
				        		if(formValue.isRecycle == "true"){
				        			Ext.getCmp('more').checked = true;
			        				Ext.getCmp('more').setValue(true);
			        				triggerDataFieldDate.hide();
					            	Ext.getCmp('sf1').show();
					            	if(formValue.timeType == '1'){
					            		Ext.getCmp('sf2').show();
					            	}else{
					            		Ext.getCmp('sf2').hide();
										//开始日期
						            	Ext.getCmp('startTimeid').allowBlank = true;
						            	Ext.getCmp('startTimeid').setValue('');
						            	//结束日期
						            	Ext.getCmp('endTimeid').allowBlank = true;
						            	Ext.getCmp('endTimeid').setValue('');
						            }
					            	
				        			var type = triggerDataSet.split(';');
				        			if(type[0] == "st_cycle_mode_day"){
				        				//按天
				        				for(var i = 0; i < appointDictRadio.items.length; i++){
											if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_day'){
												Ext.getCmp('ms1').hide();
								            	Ext.getCmp('ms2').show();
								            	Ext.getCmp('ms3').hide();
								            	Ext.getCmp('ms4').hide();
								            	Ext.getCmp('ms5').hide();
								            	appointDictRadio.items.items[i].checked = true;
								            	appointDictRadio.items.items[i].setValue(true);
								            	if(type[1] == "1"){
		            			        			//选项1
		            			        			Ext.getCmp('everyDayRadio1').checked = true;
	            			        				Ext.getCmp('everyDayRadio1').setValue(true);
	            			        				Ext.getCmp('everyDay').setValue(type[2]);
	            			        				planSelect.daySelect1();
		            			        		}else{
		            			        			//选项2
		            			        			Ext.getCmp('everyDayRadio2').checked = true;
	            			        				Ext.getCmp('everyDayRadio2').setValue(true);
	            			        				planSelect.daySelect2();
			            			        	}
								            	break;
											}
										}
	    			        		}else if(type[0] == "st_cycle_mode_hour"){
	        			        		//按小时
						            	for(var i = 0; i < appointDictRadio.items.length; i++){
											if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_hour'){
												Ext.getCmp('ms1').show();
								            	Ext.getCmp('ms2').hide();
								            	Ext.getCmp('ms3').hide();
								            	Ext.getCmp('ms4').hide();
								            	Ext.getCmp('ms5').hide();
								            	appointDictRadio.items.items[i].checked = true;
								            	appointDictRadio.items.items[i].setValue(true);
								            	Ext.getCmp('everyHourId').setValue(type[1]);
								            	planSelect.hourSelect(re, reText, dt);
								            	break;
											}
										}
	    			        		}else if(type[0] == "st_cycle_mode_week"){
	        			        		//按周
	    			        			for(var i = 0; i < appointDictRadio.items.length; i++){
											if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_week'){
												Ext.getCmp('ms1').hide();
								            	Ext.getCmp('ms2').hide();
								            	Ext.getCmp('ms3').show();
								            	Ext.getCmp('ms4').hide();
								            	Ext.getCmp('ms5').hide();
								            	appointDictRadio.items.items[i].checked = true;
								            	appointDictRadio.items.items[i].setValue(true);
								            	
								            	Ext.getCmp('everyWeeksId').setValue(type[1]);
												var weekCheck = type[2].split(",");
								            	for(var i = 0; i < weekCheckboxGroup.items.length; i++){
									            	for(var g = 0; g < weekCheck.length; g++){
														if(weekCheck[g] == weekCheckboxGroup.items.items[i].inputValue){
															weekCheckboxGroup.items.items[i].checked = true;
															weekCheckboxGroup.items.items[i].setValue(true);
															break;
														}
	        								        }
								            	}
								            	break;
											}
										}
	    			        			planSelect.weekSelect();
	    			        		}else if(type[0] == "st_cycle_mode_month"){
	        			        		//按月
	    			        			for(var i = 0; i < appointDictRadio.items.length; i++){
											if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_month'){
												Ext.getCmp('ms1').hide();
								            	Ext.getCmp('ms2').hide();
								            	Ext.getCmp('ms3').hide();
								            	Ext.getCmp('ms4').show();
								            	Ext.getCmp('ms5').hide();
								            	appointDictRadio.items.items[i].checked = true;
								            	appointDictRadio.items.items[i].setValue(true);
								            	if(type[1] == "1"){
		            			        			//选项1
		            			        			Ext.getCmp('monthId1').checked = true;
	            			        				Ext.getCmp('monthId1').setValue(true);
	            			        				Ext.getCmp('everyMonth1Id').setValue(type[2]);
	            			        				for(var i = 0; i < dayByMonthStore.totalCount; i++){
	            	            			        	var record = dayByMonthStore.getAt(i).get('id'); 
	            	            			        	if(record == type[3]){
	            	            			        		Ext.getCmp('everyDay2Id').setValue(dayByMonthStore.getAt(i).get('text'));
	            		            			        }else if(type[3] == '最后一'){
	            		            			        	Ext.getCmp('everyDay2Id').setValue(dayByMonthStore.getAt(i).get('text'));
	                		            			    }
	            		            			    }
	            			        				planSelect.monthSelect1();
		            			        		}else{
		            			        			//选项2
		            			        			Ext.getCmp('monthId2').checked = true;
	            			        				Ext.getCmp('monthId2').setValue(true);
	            			        				Ext.getCmp('everyMonth2Id').setValue(type[2]);
	            			        				
	            			        				for(var i = 0; i < severalStore.totalCount; i++){
	            	            			        	var record = severalStore.getAt(i).get('id'); 
	            	            			        	if(record == type[3]){
	            	            			        		Ext.getCmp('everyNumId').setValue(severalStore.getAt(i).get('text'));
	            		            			        }if(type[3] == '最后一周'){
	            		            			        	Ext.getCmp('everyNumId').setValue(severalStore.getAt(i).get('text'));
	            		            			        }
	            		            			    }

	            			        				for(var i = 0; i < weekStore.totalCount; i++){
	            	            			        	var record = weekStore.getAt(i).get('id'); 
	            	            			        	if(record == type[4]){
	            	            			        		Ext.getCmp('everyWeeksNumId').setValue(weekStore.getAt(i).get('text'));
	            		            			        }
	            		            			    }
	            			        				planSelect.monthSelect2();
			            			        	}
								            	break;
											}
										}
	    			        		}else if(type[0] == "st_cycle_mode_year"){
	        			        		//按年
	    			        			for(var i = 0; i < appointDictRadio.items.length; i++){
											if(appointDictRadio.items.items[i].inputValue == 'st_cycle_mode_year'){
												Ext.getCmp('ms1').hide();
								            	Ext.getCmp('ms2').hide();
								            	Ext.getCmp('ms3').hide();
								            	Ext.getCmp('ms4').hide();
								            	Ext.getCmp('ms5').show();
								            	appointDictRadio.items.items[i].checked = true;
								            	appointDictRadio.items.items[i].setValue(true);
								            	if(type[1] == "1"){
		            			        			//选项1
		            			        			Ext.getCmp('yearId1').checked = true;
	            			        				Ext.getCmp('yearId1').setValue(true);
	            			        				
	            			        				for(var i = 0; i < monthStore.totalCount; i++){
	            	            			        	var record = monthStore.getAt(i).get('id'); 
	            	            			        	if(record == type[2]){
	            	            			        		Ext.getCmp('monthNumId').setValue(monthStore.getAt(i).get('text'));
	            		            			        }
	            		            			    }

	            			        				for(var i = 0; i < dayStore.totalCount; i++){
	            	            			        	var record = dayStore.getAt(i).get('id'); 
	            	            			        	if(record == type[3]){
	            	            			        		Ext.getCmp('dayNum').setValue(dayStore.getAt(i).get('text'));
	            		            			        }else if(type[3] == '最后一'){
	            		            			        	Ext.getCmp('dayNum').setValue(dayStore.getAt(i).get('text'));
	                		            			    }
	            		            			    }
	            			        				planSelect.yearSelect1();
		            			        		}else{
		            			        			//选项2
		            			        			Ext.getCmp('yearId2').checked = true;
	            			        				Ext.getCmp('yearId2').setValue(true);
	            			        				
	            			        				for(var i = 0; i < monthStore.totalCount; i++){
	            	            			        	var record = monthStore.getAt(i).get('id'); 
	            	            			        	if(record == type[2]){
	            	            			        		Ext.getCmp('monthNum2Id').setValue(monthStore.getAt(i).get('text'));
	            		            			        }
	            		            			    }

	            			        				for(var i = 0; i < severalStore.totalCount; i++){
	            	            			        	var record = severalStore.getAt(i).get('id'); 
	            	            			        	if(record == type[3]){
	            	            			        		Ext.getCmp('everyNum2Id').setValue(severalStore.getAt(i).get('text'));
	            		            			        }if(type[3] == '最后一周'){
	            	            			        		Ext.getCmp('everyNum2Id').setValue(severalStore.getAt(i).get('text'));
	            		            			        }
	            		            			    }

	            			        				for(var i = 0; i < weekStore.totalCount; i++){
	            	            			        	var record = weekStore.getAt(i).get('id'); 
	            	            			        	if(record == type[4]){
	            	            			        		Ext.getCmp('everyWeeksNum2Id').setValue(weekStore.getAt(i).get('text'));
	            		            			        }
	            		            			    }
	            			        				planSelect.yearSelect2(); 
			            			        	}
								            	break;
											}
	    			        			}
	    			        		}
	        			        }else{
									Ext.getCmp('sjxx').show();
	        			        	triggerDataFieldDate.value = formValue.triggerData;
	        			        	dt.value = formValue.triggetTime;
	            			    }
				        	}	
			        		break;
				        }		        
				    }
				    
		            //发送方式
					for(var i = 0; i < lookTypeDictRadio.items.length; i++){
						if(lookTypeDictRadio.items.items[i].id == formValue.rb){
							//短信
							lookTypeDictRadio.items.items[i].checked = true;
							lookTypeDictRadio.items.items[i].setValue(true);
				        }
					}
					
		            //选择模版
			        for(var i = 0; i < tempStore.totalCount; i++){
			        	var record = tempStore.getAt(i).get('id'); 
			        	if(record == formValue.createBy){
			        		tempComboBox.setValue(record);
				        }
				    }
		        }
		    });
		}
	}
});