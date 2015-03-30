Ext.define('FHD.ux.layout.AccordionTree',{
	extend: 'Ext.panel.Panel',
	alias: 'widget.fhdaccordiontree',
	requires: [
               'FHD.ux.layout.multilayout.MultiLeftCardPanel'
    ],
	/**
	 * public
	 * 接口属性
	 */
	treeArr:[],		//传入的tree
	btnArr:[],		//构建的btn
	accordionCard:null,  //构建的左侧card
	width:200,	//宽度
	/**
	 * public
	 * 接口方法
	 */
	
	/**
	 * private
	 * 自定义的属性和方法
	 */
	
	/**
	 * public
	 * ext属性
	 */
	autoScroll: true,
	
	initComponent:function(){
		var me = this;
        
		//创建Card
        me.accordionCard = Ext.widget('multileftcardpanel',{
        	itemArr:me.treeArr,
        	border : false,
            flex: 1	//否则高度0
        });
        
        //创建树的按钮组
		me.btnArr = [];
		for(var i=0;i<me.treeArr.length;i++){
        	var tree = me.treeArr[i];
        	//创建左侧button数组
        	var btn = Ext.create("Ext.button.Button",{
        		id: 'treeBtn'+i,
        		height: 30,
	            textAlign: 'left',
	            style:'border-top: 1px  #f3f7fb solid !important; border-bottom: 1px  #99bce8 solid !important;',
	          	cls:'aaa-btn',
	          	text: tree.treeTitle,
	            iconCls: tree.treeIconCls,	//图标
				handler:function(){// 点击事件，添加了选中的样式，移除了其他按钮选中样式，并切换riskTreeCardPanel、riskCenterCardPanel
					var index = this.id.replace('treeBtn','');
					this.addCls('aaa-selected-btn');
					for(var j=0;j<me.btnArr.length;j++){
						if(j!=index){
							me.btnArr[j].removeCls('aaa-selected-btn');
						}
					}
					var newCard = me.treeArr[index];
					me.accordionCard.setActiveItem(newCard);
					me.setTitle(this.text);
					me.setIconCls(this.iconCls);
					//按钮回调函数
					if(newCard.onClick){
						newCard.onClick();
					}
				}
        	});
        	if(i==0){
        		btn.addCls('aaa-selected-btn');
        	}
        	me.btnArr.push(btn);
        }
        
        //创建左侧手风琴布局
	    var leftpanelArr = [me.accordionCard];	//左侧leftpanel数组
	    for(var i=0;i<me.btnArr.length;i++){
	    	leftpanelArr.push(me.btnArr[i]);
	    }
	    
	    var outterPanel = Ext.create("Ext.panel.Panel",{
	    	// 右边框样式
        	//style:'border-right: 1px  #99bce8 solid !important;',
        	border : false,
            xtype: 'panel',
            region: 'west',
            split:true,
            width: me.width,
            defaults: {
                height: 30,
                textAlign: 'left',
                style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
              	cls:'aaa-btn'
            },
            layout: {
                align: 'stretch',
                type: 'vbox'
            },
            collapsible: true,
            items: leftpanelArr////[me.leftcard,btn1,btn2]
	    });
		Ext.apply(me,{
//			title:'aa',
//			layout: {
//		        type: 'border'
//		    },
////			frame: false,
////			collapsible: false,
//            items: [outterPanel]
			// 右边框样式
        	style:'border-top: 1px;border-right: 1px  #99bce8 solid !important;',
        	border : false,
            xtype: 'panel',
            region: 'west',
            split:true,
            width: me.width,
            defaults: {
                height: 30,
                textAlign: 'left',
                style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
              	cls:'aaa-btn'
            },
            layout: {
                align: 'stretch',
                type: 'vbox'
            },
            collapsible: true,
            items: leftpanelArr////[me.leftcard,btn1,btn2]
		});
		me.callParent(arguments);
	}
});