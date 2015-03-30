/**
 * 
 * 定性评估FChart
 */

Ext.define('FHD.view.risk.assess.utils.AssessFChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessFChart',
    
    Map : function() {  
        /** 存放键的数组(遍历用到) */  
        this.keys = new Array();  
        /** 存放数据 */  
        this.data = new Object();  
          
        /** 
         * 放入一个键值对 
         * @param {String} key 
         * @param {Object} value 
         */  
        this.put = function(key, value) {  
            if(this.data[key] == null){  
                this.keys.push(key);  
            }  
            this.data[key] = value;  
        };  
          
        /** 
         * 获取某键对应的值 
         * @param {String} key 
         * @return {Object} value 
         */  
        this.get = function(key) {  
            return this.data[key];  
        };  
          
        /** 
         * 删除一个键值对 
         * @param {String} key 
         */  
        this.remove = function(key) {  
            this.keys.remove(key);  
            this.data[key] = null;  
        };  
          
        /** 
         * 遍历Map,执行处理函数 
         *  
         * @param {Function} 回调函数 function(key,value,index){..} 
         */  
        this.each = function(fn){  
            if(typeof fn != 'function'){  
                return;  
            }  
            var len = this.keys.length;  
            for(var i=0;i<len;i++){  
                var k = this.keys[i];  
                fn(k,this.data[k],i);  
            }  
        };  
          
        /** 
         * 获取键值数组(类似Java的entrySet())
         * @return 键值对象{key,value}的数组 
         */  
        this.entrys = function() {  
            var len = this.keys.length;  
            var entrys = new Array(len);  
            for (var i = 0; i < len; i++) {  
                entrys[i] = {  
                    key : this.keys[i],  
                    value : this.data[i]  
                };  
            }  
            return entrys;  
        };  
          
        /** 
         * 判断Map是否为空 
         */  
        this.isEmpty = function() {  
            return this.keys.length == 0;  
        };  
          
        /** 
         * 获取键值对数量 
         */  
        this.size = function(){  
            return this.keys.length;  
        };  
          
        /** 
         * 重写toString  
         */  
        this.toString = function(){  
            var s = "{";  
            for(var i=0;i<this.keys.length;i++,s+=','){  
                var k = this.keys[i];  
                s += k+"="+this.data[k];  
            }  
            s+="}";  
            return s;  
        };  
    } ,
    
    /** 
     * 单点按钮进行维度对应分值保存
     * dimId 维度
     * idS 以--分割(综合ID,维度分值、求分类型、权重、上级维度ID)
     */ 
    saveFun : function(dimId, idS){
		var me = this;
		var value = {};
		var parry = [];
		var values = idS.split('--');
		
		value['dimId'] = dimId;//维度ID
		value['rangObjectDeptEmpId'] = values[1];//综合ID
		value['dimValue'] = values[2];//维度分值
		value['dictEntry'] = values[3];//维度求分类型
		value['weight'] = values[4];//权重
		value['parentDimId'] = values[5];//上级维度ID
		parry.push(value);
		
//		var dicValues = '';
//		for(var i = 0; i < me.assessFChart.map.size(); i++){
//			dicValues += me.assessFChart.map.get(me.assessFChart.map.keys[i]) + ',';
//		}
		
		//me.body.mask("存储中...","x-mask-loading");
		FHD.ajax({
            url: 'saveDicValue.f',
            params: {
            	params : Ext.JSON.encode(parry)
            },
            callback: function (data) {
            	if (data && data.success) {
            		//me.body.unmask();
            	}
            	
            }
        });
		
	},
    
    load : function(result){
    	if(result == null){
    		return;
    	}
    	
    	var me = this;;
    	me.panel = null;
    	me.panel2 = null;
    	me.array = new Array();
    	me.map = new me.Map();
    	
    	for(var i = 0; i < result.length; i++){
    		if(result[i][0][0][2] == result[i][0][0][3]){
    			//没有子级
    			me.label = {
    	    			xtype: 'label',
    	    			text : result[i][0][0][2] + ':　'
    	    	    };
    			
    			
    			me.panel = Ext.create('Ext.panel.Panel', {
        	    		margin : '5 5 0 20',
        	    		border : false
    	    		});
    			
    			me.panel.add(me.label);
    			
    			for(var g = 0; g < result[i].length; g++){
    				
    				for(var h = 0; h < result[i][g].length; h++){
    					var math = Math.random();
    					var button = {xtype:"button", 
    							id:result[i][g][h][1] + ',' + result[i][g][h][0] + ',' + result[i][g][h][5] + ',math' + math,
    							text:result[i][g][h][4], 
    							inputValue:'', 
    							dixsabled:false, 
    							scale: 'medium',
    							
    							listeners: {
    								beforerender : function () {
    									var strs = this.id.split(',');
                		    			for(var i = 0; i < result.length; i++){
                		    				for(var g = 0; g < result[i].length; g++){
                		    					for(var h = 0; h < result[i][g].length; h++){
                		    						if(result[i][g][h][1] == strs[0]){
            	            	    					if(result[i][g][h][0] == strs[1]){
            	            	    						if(result[i][g][h][6] == 'true'){
            	            	    							Ext.getCmp(this.id).disable();
            	            	    						}
            	            	    					}
            	            	    				}
                		    					}
                		    				}
                		    			}
    								}
    							},
    							
            		    		handler: function(){
            		    			var strs = this.id.split(',');
            		    			debugger;
            		    			for(var i = 0; i < result.length; i++){
            		    				for(var g = 0; g < result[i].length; g++){
            		    					for(var h = 0; h < result[i][g].length; h++){
            		    						if(result[i][g][h][1] == strs[0]){
        	            	    					if(result[i][g][h][0] == strs[1]){
        	            	    						me.saveFun(result[i][g][h][1], strs[0] + '--' + strs[2]);
        	            	    						Ext.getCmp(this.id).disable();
        	            	    					}
        	            	    				}
            		    					}
            		    				}
            		    			}
            		    			
            		    			for(var n = 0; n < me.array.length; n++){
    	    							if(me.array[n].id.indexOf('math') != -1){
    	    								if(me.array[n].id != this.id){
    	    									if(me.array[n].id.split(',')[0] == strs[0]){
    	    										Ext.getCmp(me.array[n].id).enable();
    	    									}
    	    								}
    	    							}
    	    						}
            		    		}
            	    		};
            			
    					me.labelnbsp = {
            	    			xtype: 'label',
            	    			text : '　'
            	    	    };
            			
    					me.array.push({id:button.id});
    					me.panel.add(button);
    					me.panel.add(me.labelnbsp);
    				}
    			}
    		}else{
    			//有子级
    			
    			me.label = {
    	    			xtype: 'label',
    	    			text : result[i][0][0][2] + ':　　'
    	    	    };
    			
    			
    			
    			me.panel = Ext.create('Ext.panel.Panel', {
        	    		margin : '5 5 0 20',
        	    		border : false
    	    		});
    			
    			me.panel.add(me.label);
    			
    			for(var g = 0; g < result[i].length; g++){
    				    				
    				me.label2 = {
        	    			xtype: 'label',
        	    			text : '　　　' + result[i][g][0][3] + ':　　'
        	    	    };
    				
    				me.panel2 = Ext.create('Ext.panel.Panel', {
        	    		margin : '5 5 0 20',
        	    		border : false
    	    		});
    				
    				me.panel2.add(me.label2);
    				
    				for(var h = 0; h < result[i][g].length; h++){
    					var math = Math.random();
    					var button = {xtype:"button", 
    							id:result[i][g][h][1] + ',' + result[i][g][h][0] + ',' + result[i][g][h][5] + ',math' + math,
    							text:result[i][g][h][4], 
    							inputValue:'',
    							scale: 'medium',
    							
    							listeners: {
    								beforerender : function () {
    									var strs = this.id.split(',');
                		    			for(var i = 0; i < result.length; i++){
                		    				for(var g = 0; g < result[i].length; g++){
                		    					for(var h = 0; h < result[i][g].length; h++){
                		    						if(result[i][g][h][1] == strs[0]){
            	            	    					if(result[i][g][h][0] == strs[1]){
            	            	    						if(result[i][g][h][6] == 'true'){
            	            	    							Ext.getCmp(this.id).disable();
            	            	    						}
            	            	    					}
            	            	    				}
                		    					}
                		    				}
                		    			}
    								}
    							},
    							
            		    		handler: function(){
            		    			var strs = this.id.split(',');
            		    			for(var i = 0; i < result.length; i++){
            		    				for(var g = 0; g < result[i].length; g++){
            		    					for(var h = 0; h < result[i][g].length; h++){
            		    						if(result[i][g][h][1] == strs[0]){
        	            	    					if(result[i][g][h][0] == strs[1]){        	            	    						
        	            	    						me.saveFun(result[i][g][h][1], strs[0] + '--' + strs[2]);
        	            	    						Ext.getCmp(this.id).disable();
        	            	    					}
        	            	    				}
            		    					}
            		    				}
            		    			}
            		    			
            		    			
            		    			for(var n = 0; n < me.array.length; n++){
    	    							if(me.array[n].id.indexOf('math') != -1){
    	    								if(me.array[n].id != this.id){
    	    									if(me.array[n].id.split(',')[0] == strs[0]){
    	    										Ext.getCmp(me.array[n].id).enable();
    	    									}
    	    								}
    	    							}
    	    						}
            		    		}
            	    		};
            			
    					me.labelnbsp = {
            	    			xtype: 'label',
            	    			text : '　'
            	    	    };
    					me.array.push({id:button.id});
    					me.panel2.add(button);
    					me.panel2.add(me.labelnbsp);
    				}
    				
    				me.panel.add(me.panel2)
    			}
    		}
    		
    		me.add(me.panel);
    	}
    },
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
        Ext.apply(me, {
			border:false
        });

        me.callParent(arguments);
    }

});