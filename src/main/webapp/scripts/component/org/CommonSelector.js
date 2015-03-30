/**
 * 部门和人员组件
 * @param type：emp表明为人员选择组件；dept表明为部门选择组件；dept_emp表明为部门人员混合选择组件
 * @param multiSelect：默认为true，支持单选；设置为true，支持多选		
 * @param allowBlank: 默认为false，可以为空；设置为true，不可为空，提示语句需要单独写     
 * */
Ext.define('FHD.ux.org.CommonSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.commonselector',
	layout: {
        type: 'column'
    },
	autoWidth: true,
	multiSelect: true,
	allowBlank: true,
	labelWidth:95,
	disabled:false,
	subCompany:false,
	companyOnly: false,
	rootVisible: false,
	initComponent : function() {
		var me = this;
		if(me.multiSelect){
			if(me.height){
				me.height = me.height;
			}else{
				me.height = 80;				
			}
		}else{
			if(me.height){
				me.height = me.height;
			}else{
				me.height = 23;			
			}
		}
    	me.field=Ext.widget('textfield',{
			hidden:true,
	        name:me.name,
	        value: me.value,
	        allowBlank:me.allowBlank,
	        listeners:{
				change:function (field,newValue,oldValue,eOpts ){
					me.initValue(newValue);
				}
		    }
        });
        me.label=Ext.widget('label',{
    		width: me.labelWidth,
    		html: me.fieldLabel?'<span style="float:'+me.labelAlign+'">'+me.fieldLabel + ':</span>':'',
    		hidden: me.fieldLabel?false:true,
    		height: 22,
    		style: {
    			marginRight: '10px'
    		}
    	});
    	
    	if('emp' == me.type){
        /************************人员选择开始**************************/       
        	me.valueField = 'id';
	        me.displayField = 'empname';
		    me.valueStore = Ext.create('Ext.data.Store',{
        		idProperty: 'id',
        		queryMode: 'local',
			    fields:['id', 'empno', 'empname']
        	})
        /************************人员选择结束**************************/   
        }else if('dept' == me.type){
        /************************部门选择开始**************************/
        	me.valueField = 'id';
	        me.displayField = 'deptname';
		    me.valueStore = Ext.create('Ext.data.Store',{
        		idProperty: 'id',
        		queryMode: 'local',
			    fields:['id', 'deptno', 'deptname']
	        });
		  
	    /************************部门选择结束**************************/   
        }else  if('dept_emp' == me.type){
        /************************部门或人员综合选择开始**************************/  
        	me.valueField = 'deptid';
        	Ext.define('DeptAndEmp', {//定义model
			    extend: 'Ext.data.Model',
			   	fields: [{name: 'deptid', type: 'string'},
				        {name: 'deptno', type: 'string'},
				        {name: 'deptname', type:'string'},
				        {name: 'empid', type:'string'},
				        {name: 'empno',type: 'string'},
				        {name: 'empname',type: 'string'}]
			});
        	me.valueStore = Ext.create('Ext.data.Store',{
        		idProperty: 'deptid',
        		queryMode: 'local',
			    fields:['empid','diplayname','deptid','empname']
        	});
        	me.selectStore = Ext.create('Ext.data.Store',{
        		queryMode: 'local',
        		fields:['deptid', 'deptno', 'deptname','empid','empno','empname']
        	});
        	
        /************************部门或人员综合选择结束**************************/  
        }
    	if(me.type == 'emp'){
    		/************************人员选择开始**************************/       
    		me.grid=Ext.widget('grid',{
	        	hideHeaders: true,
	        	autoScroll: true,
	            height: me.height,
	            columnWidth: 1,
	        	columns:[{
	        		xtype: 'gridcolumn',
	                dataIndex: 'id',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'empno',
	                hidden:true
	        	},{
	        		xtype: 'gridcolumn',
	                dataIndex: 'empname',
	                flex:2,
	                renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value+" ( "+record.get('empno')+" ) " + "'>" + value + " ( "+record.get('empno')+" ) " + "</div>";					
				    }
	        	},{
	        		xtype:'templatecolumn',
	            	tpl:'<font class="icon-close" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
	            	width:35,
	            	align:'center',
	            	listeners:{
	            		click:{
	            			fn:function(grid,d,i){
	            				var select=grid.store.getAt(i);
	            				me.removeFromStore(select);
	            			}
	            		}
	            	}
	        	}],
	        	store:me.valueStore
	        });
	        me.button=Ext.widget('button',{
	            iconCls:'icon-magnifier',
	            height: 22,
	            width: 22,
	            disabled: me.disabled,
	            handler:function(){
	            	var selects = new Array();
	            	me.getGridStore().each(function(r){
	            		selects.push(r);
	            	});
					me.window = Ext.create('FHD.ux.org.EmpSelectorWindow',{
						multiSelect:me.multiSelect,
						subCompany: me.subCompany,
						onSubmit:function(win){
							me.grid.store.removeAll();
							me.setValueFromStore(win.selectedgrid.store);
						}
					}).show();
					me.window.setValue(selects);
			    }
	    	});
	    	/************************人员选择结束**************************/   
    	}else if(me.type == 'dept'){
    		/************************部门选择开始**************************/
    		me.grid=Ext.widget('grid',{
	        	hideHeaders: true,
	        	autoScroll: true,
	            height: me.height,
	            columnWidth: 1,
	        	columns:[{
	        		xtype: 'gridcolumn',
	                dataIndex: 'id',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'deptno',
	                hidden:true
	        	},{
	        		xtype: 'gridcolumn',
	                dataIndex: 'deptname',
	                flex:2,
	                renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<div data-qtitle='' data-qtip='" + value+" ( "+record.get('deptno')+" ) " + "'>" + value + " ( "+record.get('deptno')+" ) " + "</div>";					
				    }
	        	},{
	        		xtype:'templatecolumn',
	            	tpl:'<font class="icon-close" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;&nbsp;</font>',
	            	width:35,
	            	align:'center',
	            	readonly:true,
	            	listeners:{
	            		click:{
	            			fn:function(grid,d,i){
	            				var select=grid.store.getAt(i);
	            				me.removeFromStore(select);
	            			}
	            		}
	            	}
	        	}],
	        	store:me.valueStore
	        });
	        me.button=Ext.widget('button',{
	            iconCls:'icon-magnifier',
	            height: 22,
	            width: 22,
	            disabled: me.disabled,
	            handler:function(){
	            	var choose = new Array();
	            	var selects = new Array();
	            	me.getGridStore().each(function(r){
	            		choose.push(r.data.id);
	            		selects.push(r);
	            	});
			    	me.window = Ext.create('FHD.ux.org.DeptSelectorWindow',{
						multiSelect:me.multiSelect,
						choose:choose.join(','),
        				subCompany: me.subCompany,
        				companyOnly: me.companyOnly,
        				rootVisible: me.rootVisible,
						onSubmit:function(win){
							me.grid.store.removeAll();
							me.setValueFromStore(win.selectedgrid.store);
						}
					}).show();
					me.window.setValue(selects);
			    }
	    	});
	    	/************************部门选择结束**************************/   
    	}else if(me.type == 'dept_emp'){
    		/************************部门或人员综合选择开始**************************/  
    		me.grid=Ext.widget('grid',{
	        	hideHeaders: true,
	        	autoScroll: true,
	            height: me.height,
	            columnWidth: 1,
	        	columns:[{
	        		xtype: 'gridcolumn',
	                dataIndex: 'deptid',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'empid',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'deptno',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'empno',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'deptname',
	                flex:2,
	                renderer:function(value, metaData, record, rowIndex, colIndex, store){
	                	if(record.get('empid') && record.get('empid')!=''){
	                		return "<div data-qtitle='' data-qtip='" + value +" ( "+record.get('deptno')+" ): " + record.get('empname') +" ( "+record.get('empno')+" ) " + "'>" + value +" ( "+record.get('deptno')+" ): " + record.get('empname') +" ( "+record.get('empno')+" ) " + "</div>";					
	                	}else{
	                		return "<div data-qtitle='' data-qtip='" + value +" ( "+record.get('deptno')+" ) " + "'>" + value + " ( "+record.get('deptno')+" ) " + "</div>";					
	                	}
	                	//return "<div data-qtitle='' data-qtip='" + value + "'><font class='icon-orgsub' style='cursor:pointer;'></font>" + value + "</div>";		
				    }
	        	},{
	        		xtype: 'gridcolumn',
	                dataIndex: 'empname',
	                hidden:true
	        	},{
	        		xtype:'templatecolumn',
	            	tpl:'<font class="icon-close" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
	            	width:35,
	            	align:'center',
	            	listeners:{
	            		click:{
	            			fn:function(grid,d,i){
	            				var select=grid.store.getAt(i);
	            				me.removeFromStore(select);
	            				
	            			}
	            		}
	            	}
	        	}],
	        	store:me.valueStore
	        });
	        me.button=Ext.widget('button',{
	            iconCls:'icon-magnifier',
	            height: 22,
	            width: 22,
	            disabled: me.disabled,
	            handler:function(){
	            	var choose = new Array();
	            	var selects = new Array();
					var deptidArray = new Array();
		        	me.getGridStore().each(function(r){
				        choose.push(r.data.deptid);
			        });
			        me.selectStore.each(function(r){
			    		selects.push(r);
			        });
					me.window = Ext.create('FHD.ux.org.DeptAndEmpSelectorWindow',{
						multiSelect:me.multiSelect,
						subCompany: me.subCompany,
        				companyOnly: me.companyOnly,
        				rootVisible: me.rootVisible,
						choose:choose.join(','),
						onSubmit:function(win){
							me.grid.store.removeAll();
							me.selectStore.removeAll();
							me.setValueFromStore(win.selectedgrid.store);
						}
					}).show();
					me.window.setValue(selects);
			    }
	    	});
	    	/************************部门或人员综合选择结束**************************/  
    	}
        Ext.applyIf(me, {
            items: [
            	me.label,
            	me.grid,
            	me.button,
                me.field
            ]
        });
        me.callParent(arguments);
        me.initValue(me.value);
	},
	renderBlankColor : function(obj){
		var value=obj.field.getValue();
		var ids=value.split(",");
		if(!obj.allowBlank){
			if(value && value !='[]'){
				obj.grid.setBodyStyle('background','#FFFFFF');
			}else{
				obj.grid.setBodyStyle('background:#FFEDE9;border-color:red');
//				obj.grid.setBodyStyle('border-color','red');
			}
		}
	},
	initValue: function(value){
		var me=this;
		if(value==null||value==""){
			value=me.field.getValue();
		}
		var ids=value.split(",");
		if(value && value !='[]'){
			FHD.ajax({//ajax调用
				url : 'sys/org/findDeptAndEmpByIds.f',
				params : {
					ids:value,
					type:me.type
				},
				callback : function(data){
					var records = data.data,
						deptidAndDeptnameArray = new Array();
	    			Ext.Array.each(records,function(r,i){
		        		me.grid.store.removeAt(i);
		        		if('dept_emp' == me.type){
		        			/************************部门或人员综合选择开始**************************/  
		                    var valueRecord = Ext.create('DeptAndEmp',{
								deptid: r.deptid,
								deptno: r.deptno,
								deptname: r.deptname,
								empid: r.empid,
								empno: r.empno,
								empname:r.empname
							});
		        			me.grid.store.insert(i, valueRecord);
		        			//初始赋值
		        			deptidAndDeptnameArray.push(r.deptid+':'+r.deptname);
		        			/************************部门或人员综合选择结束**************************/ 
		        		}else{
			                me.grid.store.insert(i, r);
		        		}
		        	});
		        	if('dept_emp' == me.type){
		        		/************************部门或人员综合选择开始**************************/  
		        		me.selectStore.removeAll();
		        		Ext.each(uniq(deptidAndDeptnameArray),function(deptidAndDeptname,index){
		        			var empidArray = new Array(),
		        				empnameArray = new Array(),
		        				deptname,deptid,
		        				selectRecord;
		        			Ext.Array.each(deptidAndDeptname.split(':'),function(dad,i){
		        				if(i==0){
		        					deptid = dad;
		        				}else{
		        					deptname = dad;
		        				}
		        			});
		        			Ext.Array.each(records,function(r,i){
		        				if(r.deptid==deptid){
		        					if(r.empid){
		        						empidArray.push(r.empid);
		        						empnameArray.push(r.empname);
		        					}
								}
		        			});
							selectRecord = Ext.create('DeptAndEmp',{
								deptid: deptid,
								deptname: deptname,
								empid: empidArray,
								empname: empnameArray
								
							});
		        			me.selectStore.insert(0,selectRecord);
			        	});
			        	/************************部门或人员综合选择结束**************************/ 
		        	}
				}
			});
		}
		
	},
	/**
	 * 为隐藏域赋值
	 *
	 */
	setHiddenValue:function(valueArray){
    	var me = this;
    	if(valueArray.length>0){
    		var value = Ext.JSON.encode(valueArray);
    		me.value = value;
			me.field.setValue(value);
    	}else{
    		me.value = null;
			me.field.setValue(null);
    	}
    },
    /**
	 * 为隐藏域和显示的grid赋值
	 *
	 */
    setValueFromStore:function(values){
    	var me = this;
		var hiddenValue=new Array();
    	values.each(function(r){
    		if('dept_emp' == me.type){
    			me.selectStore.insert(0,r);
				if(r.data.empid && r.data.empid !=''){
        			FHD.ajax({//ajax调用
						url : 'sys/org/findEmpByIds.f',
						params : {
							ids:r.data.empid
						},
						callback : function(data){
							Ext.Array.each(data.data,function(empRecord,index){
								var valueRecord = Ext.create('DeptAndEmp',{
									deptid: r.data.deptid,
									deptno: r.data.deptno,
									deptname: r.data.deptname,
									empid: empRecord.id,
									empno: empRecord.empno,
									empname:empRecord.empname
								});
		                       	me.grid.store.insert(0,valueRecord);
							});
							var hiddenValue = me.getHiddenValue();
    						me.setHiddenValue(hiddenValue);
						}
        			});
        		}else{
		    		me.grid.store.insert(0,r);
	    		}
    		}else{
    			me.grid.store.insert(0,r);
    		}
    	});
    	var hiddenValue = me.getHiddenValue();
		me.setHiddenValue(hiddenValue);
    },
    /**
     * 获得当前值
     * @return {当前值}
     */
    getValue:function(){
    	var me = this;
		me.value = me.field.getValue();
    	return me.value;
    },
    /**
     * 获得当前要设置的值的数组
     * @return 隐藏域的值的数组
     */
    getHiddenValue: function(){
    	var me = this;
    	var values=me.getGridStore();
    	var hiddenValue=new Array();
    	values.each(function(value){
    		var ids = {};
    		if('dept_emp' == me.type){
    			ids['deptid'] = value.data.deptid;
           		ids['empid'] = value.data.empid;
    		}else{
    			ids['id'] = value.data.id;
    		}
    		hiddenValue.push(ids);
    	});
    	return hiddenValue;
    },
    /**
     * 获得当前grid的store
     * @return 当前grid的store
     */
	getGridStore:function(){
    	var me = this;
		return me.grid.store;
    },
    /**
     * 从当前grid的store中删除
     */
	removeFromStore:function(value){
    	var me = this;
    	me.grid.store.remove(value);
    	var hiddenValue = me.getHiddenValue();
    	me.setHiddenValue(hiddenValue);
    },
    /**
     * 清空值
     */
    clearValues : function(){
        var me = this;
        me.field.setValue(null);
        me.grid.store.removeAll();
        if('dept_emp' == me.type){
        	me.selectStore.removeAll();
        }
    }
});