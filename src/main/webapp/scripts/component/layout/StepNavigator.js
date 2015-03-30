/**
 * 左侧树，右侧待选列表和已选列表布局
 * @author 郑军祥
 * 上一步，下一步如果没有先后顺序，可以不显示上一步，下一步工具条，并且前进图标也可以换一个平级图标
 * 多条记录点击返回，还停留在第二步
 * 支持上面显示导航，下面工具条只显示按钮
 * 可以自定义添加工具条按钮
 * 步骤导航点击支持回调函数
 * 支持暴露toolbar工具条，支持使用者将页面按钮添加到工具条中，这些按钮以及状态有开发人员维护
 */

Ext.define('FHD.ux.layout.StepNavigator', {
	extend : 'FHD.ux.CardPanel',
	alias : 'widget.stepnavigator',
    activeItem: 0,	//用于标识当前激活的状态
    hiddenTop:false,	//是否隐藏头部
    hiddenTopBtns:true,//是否隐藏头部按钮
    hiddenBottom:false, //是否隐藏底部
    hiddenBottomNavigators:true,	//是否隐藏下部的导航
    hiddenUndo:false,	//是否有返回按钮
    btns:[],
    topToolbar:null,	//上部工具条
    bottomToolbar:null,	//底部工具条
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	
    	//top工具条
    	if(!me.hiddenTop){
    		if(!me.hiddenTopBtns){	//显示按钮
    			me.preTop.setDisabled(!layout.getPrev());
            	me.nextTop.setDisabled(!layout.getNext());
            	if(me.items.length==1){	//解决只有一个元素的导航，一个元素!layout.getNext()为false
            		me.nextTop.setDisabled(true);
            	}
    		}
    	}

    	//bottom工具条
    	if(!me.hiddenBottom){
    		me.preBottom.setDisabled(!layout.getPrev());
        	me.nextBottom.setDisabled(!layout.getNext());
        	if(me.items.length==1){
        		me.nextBottom.setDisabled(true);
        	}
    	}
    },
    
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    navPanelState: function (index) {
    	var me = this;
 
    	var k = 0;
    	
    	//top工具条
    	if(!me.hiddenTop){
    		k=0;
    		for(var i=0;i<me.topPanelArr.length;i++){
	    	 	var item = me.topPanelArr[i];
	            if (item.pressed != undefined) {
		    		if(k==index){
		    			item.toggle(true);
		    		}else{
		    			item.toggle(false);
		    		}
		    		k++;
	            }
	    	}
    	}

    	//bottom工具条
    	if(!me.hiddenBottom){
    		if(!me.hiddenBottomNavigators){	//显示导航条
    			k=0;
        		for(var i=0;i<me.bottomPanelArr.length;i++){
    	    		var item = me.bottomPanelArr[i];
    	            if (item.pressed != undefined) {
    		    		if(k==index){
    		    			item.toggle(true);
    		    		}else{
    		    			item.toggle(false);
    		    		}
    		    		k++;
    	            }
    	    	}
    		}
    		
    	}

    },
    
    /**
     * 返回按钮事件
     */
    undo:Ext.emptyFn(),
    
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
    	
    	var isBack = true;	//是否通过验证，没通过就不跳转到下一步
        var activePanel = me.getActiveItem();
        if (activePanel.back) {
        	isBack = activePanel.back(me);
        }
        
        if(isBack==false){
	    	
        }else{
            me.pageMove("prev");
            me.navBtnState();
            
            me.activeItem = me.activeItem-1;
            me.navPanelState(me.activeItem);
        }

    },
    
    /**
     * 下一步按钮事件
     */
    last:function(){
    	var me = this;
		
    	var isLast = true;	//是否通过验证，没通过就不跳转到下一步
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
        	isLast = activePanel.last(me);
        }
        
        if(isLast==false){
	    	
        }else{
        	me.pageMove("next");
	        me.navBtnState();
	        
	        me.activeItem = me.activeItem+1;
	        me.navPanelState(me.activeItem);
        }
    },
    
    /**
     * 完成按钮事件
     */
    finish:function(){
    	var me = this;
    	
    	var isLast = true;	//是否通过验证，没通过就不跳转到下一步
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            isLast = activePanel.last(me);
        }
        
        if(isLast==false){
	    	
        }else{
        	//是否显示undo
	        if(!me.hiddenUndo){
	        	 me.undo();
	        }
        }        
    },
    
    /**
     * 导航到第一个Panel
     */
    navToFirst:function(){
    	var me = this;
    	
    	var index = 0;
    	me.activeItem = index;
        me.navPanelState(index);
        me.getLayout().setActiveItem(index);
        me.navBtnState();
    },
    
    /**
     * 获取上部工具条
     */
    getTopToolbar:function(){
    	var me = this;
    	return me.getDockedItems('toolbar[dock="top"]')[0];
    },
    
    /**
     * 获取下部工具条
     */
    getBottomToolbar:function(){
    	var me = this;
    	return me.getDockedItems('toolbar[dock="bottom"]')[0];
    },
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
    	var me = this;

    	me.topPanelArr = [];		//上部导航panel数组
    	me.bottomPanelArr = [];		//下部导航panel数组
    	
    	//设置内部panel的高度
    	for(var i=0;i<me.items.length;i++){
    		//指定宽度就不设置了
    		if(!me.items[i].height){
    			me.items[i].height = FHD.getCenterPanelHeight()-61;//减去导航高度和上面高度
    		}
    		//指定border就不设置了
    		if(!me.items[i].border){
    			me.items[i].border = false;
    		}
    	}
    	
        //top工具条
    	if(!me.hiddenTop){
	    	var tbarArr = [];
	    	for(var i=0;i<me.items.length;i++){
	    		var panel = Ext.create('Ext.button.Button',{
		            text: me.items[i].navigatorTitle || i,
		            cls:i,	//用于传值
		            iconCls: 'icon-00'+(i+1),
		            handler: function () {
		            	var index = this.cls;
		            	me.activeItem = index;
		                me.navPanelState(index);
		                me.getLayout().setActiveItem(index);
		                me.navBtnState();
		                //导航条点击的回调函数
		                var newCard = me.items.items[index];
		                if(newCard.onClick){
		                	newCard.onClick();
		                }
		            }
		        });
		        if(i!=0){
		        	tbarArr.push('<img src="'+__ctxPath+'/images/icons/show_right.gif">');
		        }
		        tbarArr.push(panel);
		        //保存导航panel
		        me.topPanelArr.push(panel);
	    	}

	    	if(!me.hiddenTopBtns){
		        me.undoTop = Ext.create('Ext.button.Button',{
		            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
		            iconCls: 'icon-arrow-undo',
		            handler: function () {
		            	me.undo();
		            }
		        });
		        me.preTop = Ext.create('Ext.button.Button',{
		            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
		            iconCls: 'icon-control-rewind-blue',
		            handler: function () {
		            	me.back();
		            }
		        });
		        me.nextTop = Ext.create('Ext.button.Button',{
		            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
		            iconCls: 'icon-control-fastforward-blue',
		            handler: function () {
		            	me.last();
		            }
		        });
		        me.finishTop = Ext.create('Ext.button.Button',{
		            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
		            iconCls: 'icon-control-stop-blue',
		            handler: function () {
		            	me.finish();
		            }
		        });
		        tbarArr.push('->');
		        if(!me.hiddenUndo){
		        	tbarArr.push(me.undoTop);
		        }
		        tbarArr.push(me.preTop);
		        tbarArr.push(me.nextTop);
		        tbarArr.push(me.finishTop);
		        //自定义button
		        if(me.btns.length){
		        	for(var k in me.btns){
		        		var temp = Ext.create('Ext.button.Button',{
				            text: me.btns[k].text||'按钮',
				            iconCls: me.btns[k].iconCls||'icon-save',
				            handler: me.btns[k].handler
		        		})
		        		tbarArr.push(temp);
		        	}
		        }
	    	}

	        //items由数据传入
	        Ext.apply(me, {
	        	tbar: {
	        		items:tbarArr
	        	}
	        });
    	}
        
        //bottom工具条
    	if(!me.hiddenBottom){
    		var bbarArr = [];
    		if(!me.hiddenBottomNavigators){
    	    	for(var i=0;i<me.items.length;i++){
    	    		var panel = Ext.create('Ext.button.Button',{
    		            text: me.items[i].navigatorTitle || i,
    		            cls:i,	//用于传值
    		            iconCls: 'icon-00'+(i+1),
    		            handler: function () {
    		            	var index = this.cls;
    		            	me.activeItem = index;
    		                me.navPanelState(index);
    		                me.getLayout().setActiveItem(index);
    		                me.navBtnState();
    		            }
    		        });
    		        if(i!=0){
    		        	bbarArr.push('<img src="'+__ctxPath+'/images/icons/show_right.gif">');
    		        }
    		        bbarArr.push(panel);
    		        //保存导航panel
    		        me.bottomPanelArr.push(panel);
    	    	}
    		}

	        me.undoBottom = Ext.create('Ext.button.Button',{
	            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
	            iconCls: 'icon-arrow-undo',
	            handler: function () {
	            	me.undo();
	            }
	        });
	        me.preBottom = Ext.create('Ext.button.Button',{
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
	            iconCls: 'icon-control-rewind-blue',
	            handler: function () {
	            	me.back();
	            }
	        });
	        me.nextBottom = Ext.create('Ext.button.Button',{
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
	            iconCls: 'icon-control-fastforward-blue',
	            handler: function () {
	            	me.last();
	            }
	        });
	        me.finishBottom = Ext.create('Ext.button.Button',{
	            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
	            iconCls: 'icon-control-stop-blue',
	            handler: function () {
	            	me.finish();
	            }
	        });
	        bbarArr.push('->');
	        if(!me.hiddenUndo){
	        	bbarArr.push(me.undoBottom);
	        }
	        bbarArr.push(me.preBottom);
	        bbarArr.push(me.nextBottom);
	        bbarArr.push(me.finishBottom);
	        //自定义button
	         if(me.btns.length){
	        	for(var k in me.btns){
	        		var temp = Ext.create('Ext.button.Button',{
			            text: me.btns[k].text||'按钮',
			            iconCls: me.btns[k].iconCls||'icon-save',
			            handler: me.btns[k].handler
	        		})
	        		bbarArr.push(temp);
	        	}
	        }
	        //items由数据传入
	        Ext.apply(me, {
	        	bbar: {
	        		items:bbarArr
	        	}
	        });
    	}

        me.callParent(arguments);
      
        me.navBtnState();
        me.navPanelState(me.activeItem);//0
    }
});