
Ext.define('FHD.ux.kpi.FormulaWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.formulawindow',

	mixins :{
		formula : 'FHD.ux.kpi.Formula'
	},
	
	
	title : FHD.locale.get('fhd.formula.formulaEditor'),
	height : 500,
    width : 720,
	modal : true,
    maximizable : true,
    border : false,
    
	initComponent : function() {
		var me = this,myItems;

		if('kpiType' == me.showType){
			myItems=[
				{
					text : FHD.locale.get('fhd.formula.kpi'),
					iconCls : 'icon-folder',
					expanded : true,
					children : [
						{
						    text : 'kpiBaseAttributes',
						  	iconCls : 'icon-information',
						    leaf : true
						},
			            {
			                text : 'myself',
			              	iconCls : 'icon-information',
			                leaf : true
			            },
						{
						    text : 'prior',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'isKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'subSumKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'kpiStatusLevel',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
				}
	        ];
		}else if('kcType' == me.showType){
			myItems=[
				{
					text : FHD.locale.get('fhd.formula.kpi'),
					iconCls : 'icon-folder',
					expanded : true,
					children : [
			            {
						    text : 'kpiBaseAttributes',
						  	iconCls : 'icon-information',
						    leaf : true
						},
			            {
			                text : 'myself',
			              	iconCls : 'icon-information',
			                leaf : true
			            },
						{
						    text : 'prior',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'isKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'subSumKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'kpiStatusLevel',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
				}
	        ];
		}else if('riskType' == me.showType){
			myItems=[
				{
	        		text : FHD.locale.get('fhd.formula.risk'),
	        		iconCls : 'icon-folder',
	        		expanded : false,
	        		children : [
			            {
			                text : FHD.locale.get('fhd.formula.impacts'),
			              	iconCls : 'icon-information',
			                leaf : true
			            },
			            {
			                text : FHD.locale.get('fhd.formula.probability'),
			              	iconCls : 'icon-information',
			                leaf : true
			            }
			        ]
				}
	        ];
		}else if('categoryType' == me.showType){
			myItems=[
			    {
	        		text : FHD.locale.get('fhd.formula.kpi'),
	        		iconCls : 'icon-folder',
	        		expanded : true,
	        		children : [
			            {
						    text : 'kpiBaseAttributes',
						  	iconCls : 'icon-information',
						    leaf : true
						},
			            {
			                text : 'myself',
			              	iconCls : 'icon-information',
			                leaf : true
			            },
						{
						    text : 'prior',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'isKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'subSumKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'kpiStatusLevel',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
			    },
			    {
	        		text : '记分卡',
	        		iconCls : 'icon-folder',
	        		expanded : false,
	        		children : [
	        		    /*
						{
						    text : 'SubCategory',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						*/
	        		    {
						    text : 'scBaseAttributes',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'isScStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'subSumScStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
			    }
			];
		}else if('strategyType' == me.showType){
			myItems=[
			    {
	        		text : FHD.locale.get('fhd.formula.kpi'),
	        		iconCls : 'icon-folder',
	        		expanded : true,
	        		children : [
			            {
						    text : 'kpiBaseAttributes',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
			                text : 'myself',
			              	iconCls : 'icon-information',
			                leaf : true
			            },
			            {
						    text : 'prior',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'isKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'subSumKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'kpiStatusLevel',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
			    },
			    {
	        		text : '战略目标',
	        		iconCls : 'icon-folder',
	        		expanded : false,
	        		children : [
						{
						    text : 'SubStrategy',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
			    }
			];
		}else if('all' == me.showType){
			myItems=[
			    {
	        		text : FHD.locale.get('fhd.formula.kpi'),
	        		iconCls : 'icon-folder',
	        		expanded : true,
	        		children : [
			            {
						    text : 'kpiBaseAttributes',
						  	iconCls : 'icon-information',
						    leaf : true
						},
			            {
			                text : 'myself',
			              	iconCls : 'icon-information',
			                leaf : true
			            },
						{
						    text : 'prior',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'isKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : 'subSumKpiStatus',
						  	iconCls : 'icon-information',
						    leaf : true
						},{
						    text : 'kpiStatusLevel',
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
			    },
			    {
	        		text : FHD.locale.get('fhd.formula.risk'),
	        		iconCls : 'icon-folder',
	        		expanded : false,
	        		children : [
						{
						    text : FHD.locale.get('fhd.formula.impacts'),
						  	iconCls : 'icon-information',
						    leaf : true
						},
						{
						    text : FHD.locale.get('fhd.formula.probability'),
						  	iconCls : 'icon-information',
						    leaf : true
						}
			        ]
			    }
			];
		}
		var formulaTreePanel = Ext.create('Ext.tree.Panel',{
			flex:1,
	        border:true,
	        maxWidth:220,
	        title: FHD.locale.get('fhd.formula.operateArea'),
	        autoScroll: false,
	        root : {
	        	text : FHD.locale.get('fhd.formula.formula'),
		        iconCls :'icon-calculator',
		        expanded : true,
		        autoLoad : true,
		        children : [
		        	{
		        		text : FHD.locale.get('fhd.formula.function'),
		        		iconCls : 'icon-folder',
		        		expanded : true,
		        		children : [
		  		            {
		  		                text : FHD.locale.get('fhd.formula.mathematics'),
		  		              	iconCls :'icon-information',
		  		              	expanded : true,
		  		              	children :[
									{leaf : true,text:'ABS'},
									{leaf : true,text:'EXP'},
									{leaf : true,text:'INT'},
									{leaf : true,text:'LOG'},
									{leaf : true,text:'LOG10'},
									{leaf : true,text:'SQRT'} 
		  		    			]
		  		            },
		  		            {
		  		                text : FHD.locale.get('fhd.formula.statistics'),
		  		              	iconCls : 'icon-information',
		  		              	expanded : false,
		  		              	children :[
		  		              	    {leaf : true,text:'SUM'},
		  		              	    {leaf : true,text:'AVERAGE'},
		  		              	    {leaf : true,text:'COUNT'},
				 				    {leaf : true,text:'MAX'},
				 				    {leaf : true,text:'MIN'}
				 				    /*
				 				    {leaf : true,text:'Variance'},
				 				    {leaf : true,text:'StandardDeviation'},
				 				    
				 				    {leaf : true,text:'fai'},
				 				    {leaf : true,text:'skeness'},
				 				    {leaf : true,text:'kurt'},
				 				    {leaf : true,text:'cov'},
				 				    {leaf : true,text:'vol'},
				 				    {leaf : true,text:'chr'},
				 				    {leaf : true,text:'corr'}
				 				    */
		  		    			]
		  		            },
		  		            {
		  		                text : FHD.locale.get('fhd.formula.logic'),
		  		              	iconCls : 'icon-information',
		  		              	expanded : false,
		  		              	children :[
		  		              	    {leaf : true,text:'if'},
		  		              	    {leaf : true,text:'isNoValue'}
		  		    			]
		  		            },
		  		            /*
		  		            {
		  		                text : FHD.locale.get('fhd.formula.time'),
		  		              	iconCls : 'icon-information',
		  		              	expanded : false,
		  		              	children :[
		  		              	    {leaf : true,text:'timeOffSet'},
		  		              	    {leaf : true,text:'valuesToDate'}
		  		    			]
		  		            },
		  		            */
		  		            {
		  		                text : FHD.locale.get('fhd.formula.riverDelta'),
		  		              	iconCls : 'icon-information',
		  		              	expanded : false,
		  		              	children :[
		  		              	    {leaf : true,text:'SIN'},
		  		              	    {leaf : true,text:'COS'},
		  		              	    {leaf : true,text:'TAN'}
		  		    			]
		  		            }
		  		        ]
		        	},
		        	{
		        		text : FHD.locale.get('fhd.formula.applicationData'),
		        		iconCls : 'icon-folder',
		        		expanded : true,
		        		children : myItems
		        	}
	        	]
	        },
            listeners:{    
              	itemclick:function(view,node){
              		var textarearValue = me.editor.html();
              		switch (node.data.text){
	              		case 'ABS':
	              			me.editor.insertHtml("ABS(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'EXP':
	              			me.editor.insertHtml("EXP(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'INT':
	              			me.editor.insertHtml("INT(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'LOG':
	              			me.editor.insertHtml("LOG(<"+FHD.locale.get('fhd.formula.baseNumber')+">,<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'LOG10':
	              			me.editor.insertHtml("LOG10(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'SQRT':
	              			me.editor.insertHtml("SQRT(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'SUM':
	              			me.editor.insertHtml("SUM(<"+FHD.locale.get('fhd.formula.param1')+">"+","+"<"+FHD.locale.get('fhd.formula.param2')+">"+","+"<"+FHD.locale.get('fhd.formula.param3')+">"+"..."+")");
		    				break;
	              		case 'AVERAGE':
	              			me.editor.insertHtml("AVERAGE(<"+FHD.locale.get('fhd.formula.param1')+">"+","+"<"+FHD.locale.get('fhd.formula.param2')+">"+","+"<"+FHD.locale.get('fhd.formula.param3')+">"+"..."+")");
		    				break;
	              		case 'COUNT':
	              			me.editor.insertHtml("COUNT(<"+FHD.locale.get('fhd.formula.param1')+">"+","+"<"+FHD.locale.get('fhd.formula.param2')+">"+","+"<"+FHD.locale.get('fhd.formula.param3')+">"+"..."+")");
		    				break;
	              		case 'MAX':
	              			me.editor.insertHtml("MAX(<"+FHD.locale.get('fhd.formula.param1')+">"+","+"<"+FHD.locale.get('fhd.formula.param2')+">"+","+"<"+FHD.locale.get('fhd.formula.param3')+">"+"..."+")");
		    				break;
	              		case 'MIN':
	              			me.editor.insertHtml("MIN(<"+FHD.locale.get('fhd.formula.param1')+">"+","+"<"+FHD.locale.get('fhd.formula.param2')+">"+","+"<"+FHD.locale.get('fhd.formula.param3')+">"+"..."+")");
		    				break;
	              		case 'Variance':
	              			//me.editor.insertHtml("SQRT(<"+FHD.locale.get('fhd.formula.value')+">)");
	              			alert("未实现");
		    				break;
	              		case 'StandardDeviation':
	              			//me.editor.insertHtml("SQRT(<"+FHD.locale.get('fhd.formula.value')+">)");
	              			alert("未实现");
		    				break;
	              		case 'if':
	              			me.editor.insertHtml("IF(< condition >,< then >,< else >)");
		    				break;
	              		case 'isNoValue':
	              			me.editor.insertHtml("isNoValue(<"+FHD.locale.get('fhd.formula.param')+">)");
		    				break;
	              		case 'timeOffSet':
	              			//me.editor.insertHtml("SQRT(<"+FHD.locale.get('fhd.formula.value')+">)");
	              			alert("未实现");
		    				break;
	              		case 'valuesToDate':
	              			//me.editor.insertHtml("SQRT(<"+FHD.locale.get('fhd.formula.value')+">)");
	              			alert("未实现");
		    				break;
	              		case 'SIN':
	              			me.editor.insertHtml("SIN(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'COS':
	              			me.editor.insertHtml("COS(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'TAN':
	              			me.editor.insertHtml("TAN(<"+FHD.locale.get('fhd.formula.value')+">)");
		    				break;
	              		case 'kpiBaseAttributes':
	              			me.openKpiSelectorWindow(node.data.text,me.showType);
		    				break;
			    		case 'myself':
			    			me.openKpiSelectorWindow(node.data.text,me.showType);
		    				break;
			    		case 'prior':
	              			me.openKpiSelectorWindow(node.data.text,me.showType);
	              			break;
			    		case 'isKpiStatus':
			    			me.editor.insertHtml("isKpiStatus($指标名称,$告警状态颜色)");
		    				break;
			    		case 'subSumKpiStatus':
			    			me.editor.insertHtml("subSumKpiStatus($指标名称,$告警状态颜色)");
		    				break;
			    		case 'kpiStatusLevel':
			    			me.openKpiSelectorWindow(node.data.text,me.showType);
			    			break;
			    		case FHD.locale.get('fhd.formula.impacts'):
			    			me.openRiskSelectorWindow(node.data.text);
		    				break;
			    		case FHD.locale.get('fhd.formula.probability'):
			    			me.openRiskSelectorWindow(node.data.text);
		    				break;
			    		case 'scBaseAttributes':
	              			me.openScSelectorWindow(node.data.text,me.showType);
		    				break;
			    		case 'isScStatus':
			    			me.editor.insertHtml("isScStatus($记分卡名称,$告警状态颜色)");
		    				break;
			    		case 'subSumScStatus':
			    			me.editor.insertHtml("subSumScStatus($记分卡名称,$告警状态颜色)");
		    				break;
			    		case 'SubCategory':
			    			me.editor.insertHtml("SUB(myself,$评估值,$kpi,$A)");
		    				break;
			    		case 'SubStrategy':
			    			me.editor.insertHtml("SUB(myself,$评估值,$kpi,$A)");
		    				break;
		    			default:
		    				break;
		    		}
          		}    
          	}    
		});
		var formulaFormPanel = Ext.create('Ext.form.Panel',{
			border: false,
			defaults : {columnWidth : 1/1,margin : '2 2 2 2'},//每行显示一列，可设置多列
			layout : {
				type : 'vbox',
	        	align:'stretch'
	        },
			flex:1,
			title : FHD.locale.get('fhd.formula.formulaArea'),
			
			listeners:{  
	            render:function(){
	                setTimeout(function(){  
	                	me.editor = KindEditor.create('textarea[name="content"]', {   
	                        width : pa.getWidth(),   
	                        height: pa.getHeight()-5,   
	                        newlineTag : 'br',   
	                        resizeType : 0,   
	                        allowPreviewEmoticons : false,   
	                        allowImageUpload : false,   
	                        items : [
	                        ],   
	                        allowFileManager : false,
	                        afterCreate : function(id) {
	                        	//当修改时，设置公式默认值
	                        	if(me.formulaContent==undefined){
	                        		//me.editor.insertHtml("");
	                        		this.html("");
	                        	}else{
	                        		this.html(me.formulaContent);
	                        	}
	                        },
	                        afterFocus: function(){
	                        	//debugger;
	                        	if(this.html()==undefined || this.html()==''){
	                        		//this.html();
	                        		this.text("");
	                        	}else{
	                        		var orginalValue = this.html().replace(/<[^>].*?>/g,"");
		                        	//this.html("");
	                        		//this.insertHtml("");
		                        	this.html("").appendHtml(orginalValue);
	                        	}
	                        }
	                    });
	                },1000);  
	            }
	        }  
		});
		
		var lab = Ext.create('Ext.form.Label',{
			flex:1,
			id:"labelID",
			text:FHD.locale.get('fhd.formula.targetName')+'：'+me.targetName,
			cls:"cssLabel",//
			autoShow:true,
			autoWidth:true,
			autoHeight:true
		});
		var pa = Ext.create('Ext.container.Container',{
			flex:5,
			margin: '2 2 4 2',
            html:'<textarea id="textarea${param._dc}" name="content" style="width:100%;height:100%;"></textarea>',
            listeners:{
            	resize:function(t,width, height){
            		if(me.editor){
            			me.editor.resize(width,height);
            		}
            	}
            }
        });
		
		if('' != me.targetName && undefined != me.targetName){
			formulaFormPanel.add(lab);
			formulaFormPanel.add(pa);
		}else{
			formulaFormPanel.add(pa);
		}
		
		var formulaButtonPanel = Ext.create('Ext.panel.Panel',{
			border: false,
			autoScroll:true,
			flex:1,
			region : 'south',
			title : FHD.locale.get('fhd.formula.operator'),
			layout:'fit',
			items:[
				 {	 xtype : 'fieldset',
					 margin: '5 5 5 5',
					 title:FHD.locale.get('fhd.formula.commonlyUsed'),
					 defaults:{
						 width:40,margin: '3 5 2 5'
					 },
					 items:[
 				     {xtype : 'label',text:FHD.locale.get('fhd.formula.mathematics')},
 				     {xtype : 'button',text:'+',
 				    	 handler:function(){
 				    		me.editor.insertHtml("+");
 				    	 }
 				     },
 				     {xtype : 'button',text:'-',
 				    	 handler:function(){
 				    		me.editor.insertHtml("-");
 				    	 }
 				     },
 				     {xtype : 'button',text:'*',
 				    	 handler:function(){
 				    		me.editor.insertHtml("*");
 				    	 }
 				     },
 				     {xtype : 'button',text:'/',
 				    	 handler:function(){
 				    		me.editor.insertHtml("/");
 				    	 }
 				     },
 				     /*
 				     {xtype : 'button',text:'^',
 				    	 handler:function(){
 				    		me.editor.insertHtml("^");
 				    	 }
 				     },
 				     {xtype : 'button',text:'%',
 				    	 handler:function(){
 				    		me.editor.insertHtml("%");
 				    	 }
 				     },
 				     */
 				     {xtype: 'panel', html: '<br>', border:false}, //空元素，用于分割
   				     {xtype : 'label',text:FHD.locale.get('fhd.formula.compare')},
   				     {xtype : 'button',text:'<',
 				    	 handler:function(){
 				    		me.editor.insertHtml("&#60;");
 				    	 }
   				     },
   				     {xtype : 'button',text:'<=',
 				    	 handler:function(){
 				    		me.editor.insertHtml("<=");
 				    	 }
   				     },
   				     {xtype : 'button',text:'==',
 				    	 handler:function(){
 				    		me.editor.insertHtml("==");
 				    	 }
   				     },
   				     {xtype : 'button',text:'!=',
 				    	 handler:function(){
 				    		me.editor.insertHtml("!=");
 				    	 }
   				     },
   				     {xtype : 'button',text:'>=',
 				    	 handler:function(){
 				    		me.editor.insertHtml(">=");
 				    	 }
   				     },
   				     {xtype : 'button',text:'>',
 				    	 handler:function(){
 				    		me.editor.insertHtml(">");
 				    	 }
   				     },
   				     {xtype: 'panel', html: '<br>', border:false}, //空元素，用于分割
				     {xtype : 'label',text:FHD.locale.get('fhd.formula.logic')},
				     {xtype : 'button',text:'&&',
 				    	 handler:function(){
 				    		me.editor.insertHtml("&#38;&#38;");
 				    	 }
				     },
				     {xtype : 'button',text:'||',
 				    	 handler:function(){
 				    		me.editor.insertHtml("||");
 				    	 }
				     },
				     {xtype : 'button',text:'!',
 				    	 handler:function(){
 				    		me.editor.insertHtml("!");
 				    	 }
				     }
				 ]}
			]
		});
		
		var formulaGridPanel = Ext.create('Ext.panel.Panel',{
			flex:2,
			layout : {
	        	type : 'vbox',
	        	align:'stretch'
		    },
			items:[
			    formulaFormPanel,formulaButtonPanel
			],
			listeners:{
				/*
				beforeclose: function( p, options ){
					//关闭panel前移除编辑器
					me.editor.remove('textarea[name="content"]');
				}
				*/
			}
		});
		
		Ext.applyIf(me, {
			layout : {
	        	type : 'hbox',
	        	align:'stretch'
		    },
            items : [formulaTreePanel,formulaGridPanel]
        });
		
        me.buttons =[{
        	xtype:'button',
        	text:FHD.locale.get('fhd.formula.validate'),
            handler:function(){
            	//前台验证通过后，计算和保存按钮可用
            	if(me.validateFormula(me.editor.html())){
            		//targetId为空，不进行后台验证，例如：指标类型
            		if('' != me.targetId && undefined != me.targetName){
            			/**
                    	 * 后台验证循环嵌套
                    	 * @param id 要计算的指标或风险id
                    	 * @param type kpi或risk
                    	 * @param formula 公式
                    	 * @return 成功:值;失败:原因
                    	 * @RequestMapping("/formula/validateFormula.f")
                    	 */
            			var formulaContent = me.editor.html().replace(/<[^>].*?>/g,"");
            			formulaContent = me.replaceall(formulaContent,"&lt;","<");
            			formulaContent = me.replaceall(formulaContent,"&gt;",">");
            			//jrj注释
            			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.validateSuccess'));
            			Ext.getCmp('save${param._dc}').setDisabled(false);
            			//jrj注释
                		/*
            			FHD.ajax({
    						url : __ctxPath + '/formula/validateFormula.f',
    						method:'post',
    						params : {
    							id:me.targetId,
    							type:me.type,
    							objectColumn:me.column,
    							formula: formulaContent
    							//me.editor.html().replace(/<[^>].*?>/g,"").replace("&lt;","<").replace("&gt;",">")
    						},
    						callback : function(response){
    					      	if(response){
    					      		//Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
    					      		Ext.getCmp('save${param._dc}').setDisabled(false);
    					      		if('kpiType' == me.showType){
    		                			Ext.getCmp('cal${param._dc}').setDisabled(false);
    		                		}
    			            		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.validateSuccess'));
    						    }else{
    						    	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.vallidateFailure'));
    						    }
    						}
    					});
    					*/
                	}else{
                		Ext.getCmp('save${param._dc}').setDisabled(false);
                		if('kpiType' == me.showType){
                			Ext.getCmp('cal${param._dc}').setDisabled(false);
                		}
	            		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.validateSuccess'));
                	}
            	}else{
            		Ext.getCmp('save${param._dc}').setDisabled(true);
            		Ext.getCmp('cal${param._dc}').setDisabled(true);
            		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.vallidateFailure'));
            	}
            }
        },{
        	id:'cal${param._dc}',
        	xtype:'button',
        	text:FHD.locale.get('fhd.formula.calculate'),
        	disabled: true,
        	style: {
            	marginLeft: '10px'    	
            },
            handler:function(){
            	/**
            	 * 后台计算公式值
            	 * @param id 要计算的指标或风险id
            	 * @param type kpi或risk
            	 * @param formula 公式
            	 * @return 成功:值;失败:原因
            	 * @RequestMapping("/formula/calculateFormula.f")
            	 */
            	var formulaContent = me.editor.html().replace(/<[^>].*?>/g,"");
    			formulaContent = me.replaceall(formulaContent,"&lt;","<");
    			formulaContent = me.replaceall(formulaContent,"&gt;",">");
            	FHD.ajax({
					url : __ctxPath + '/formula/calculateFormula.f',
					method:'post',
					params : {
						id: me.targetId,
						targetName: me.targetName,
						type: me.type,
						formula: formulaContent
						//me.editor.html().replace(/<[^>].*?>/g,"").replace("&lt;","<").replace("&gt;",">")
					},
					callback : function(response){
				      	if(response){
				      		//Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				      		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.calculateResult')+'：'+response);
					    }else{
					    	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.formula.calculate')+FHD.locale.get('fhd.formula.failureReason')+'：'+response);
					    }
					}
				});
            }
        },{
        	id:'save${param._dc}',
        	xtype:'button',
        	text:FHD.locale.get('fhd.common.confirm'),
        	style: {
            	marginLeft: '10px'    	
            },
        	disabled: true,
            handler:function(){
            	var formulaContent = me.editor.html().replace(/<[^>].*?>/g,"");
    			formulaContent = me.replaceall(formulaContent,"&lt;","<");
    			formulaContent = me.replaceall(formulaContent,"&gt;",">");
    			formulaContent = me.replaceall(formulaContent,"&amp;","&");
            	me.onSubmit(formulaContent);
            	me.close();
            }
        },
        /*
        {
        	xtype:'button',
        	text:FHD.locale.get('fhd.common.reset'),
        	style: {
            	marginLeft: '10px'    	
            },
            handler:function(){
            	if(me.formulaContent==undefined){
            		//me.editor.insertHtml("");
            		me.editor.html("");
            	}else{
            		me.editor.html(me.formulaContent);
            	}
            }
        },
        */
        {
        	xtype:'button',
        	text:FHD.locale.get('fhd.common.close'),
        	style: {
            	marginLeft: '10px'    	
            },
            handler:function(){
            	me.close();
            }
        }];
		me.callParent(arguments);
	},
	setTargetId:function(value){
    	var me = this;
		me.targetId = value;
	},
	setTargetName:function(value){
		var me = this;
		me.targetName = value;
	},
	setFormulaContent:function(value){
		var me = this;
		me.formulaContent = value;
	},
	onSubmit : Ext.emptyFn()
});