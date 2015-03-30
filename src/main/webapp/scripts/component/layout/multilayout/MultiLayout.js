/**
 * 
 * 多表布局，左面treeCard，右侧主面板card
 * @author zhengjunxiang
 */
Ext.define('FHD.ux.layout.multilayout.MultiLayout', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.multilayout',
    requires: [
               'FHD.ux.layout.multilayout.MultiLeftCardPanel',
               'FHD.ux.layout.multilayout.MultiRightCardPanel',
               'FHD.ux.layout.multilayout.MultiMainPanel',
               'FHD.ux.layout.multilayout.MultiTabPanel'
    ],
	
    /**
	 * public
	 * 接口属性
	 */

    pages:[],	//一共几个页面,[{tree:null,treeBtn:null,tabs:[]},{}] 说明： tree左侧树，treeBtn树按钮点击处理事件，tabs每个树对应的右侧tab页
    
    /**
	 * public
	 * ext属性
	 */
    frame: false,    
    // 布局
    layout: {
        type: 'border'
    },
    
    /**
	 * private
	 * 自定义属性
	 */
    leftcard:null,	//左侧treecard
    rightcard:null, //右侧card public
    righttabArr:[], //右侧tab数组
    leftcardArr:[], //左侧树数组
    leftbtnArr:[],	//左侧按钮数组
    rightcardArr:[],//右侧mainpanel数组 public
    righttabArray:[], //右侧mainpanel下tab数组
    
	/**
	 * public
	 * 接口方法
	 */
    
    /**
	 * private
	 * 自定义方法
	 */


    // 初始化方法
    initComponent: function() {
        var me = this;
         
        //数组处理
        me.leftcardArr = [];
        me.leftbtnArr = [];
        me.righttabArr = [];
        for(var i=0;i<me.pages.length;i++){
        	var page = me.pages[i];
        	me.leftcardArr.push(page.tree);
        	//创建左侧button数组
        	var btn = Ext.create("Ext.button.Button",{
        		id: 'treeBtn'+i,
        		height: 30,
	            textAlign: 'left',
	            style:'border-bottom: 1px  #99bce8 solid !important;',
	          	cls:'aaa-btn',
	          	text: page.treeTitle,
	            iconCls: page.treeIconCls,	//图标
				handler:function(){// 点击事件，添加了选中的样式，移除了其他按钮选中样式，并切换riskTreeCardPanel、riskCenterCardPanel
					var index = this.id.replace('treeBtn','');
					this.addCls('aaa-selected-btn');
					for(var j=0;j<me.leftbtnArr.length;j++){
						if(j!=index){
							me.leftbtnArr[j].removeCls('aaa-selected-btn');
						}
					}
					me.leftcard.setActiveItem(me.leftcardArr[index]);
					me.leftpanel.setTitle(this.text);
					me.leftpanel.setIconCls(this.iconCls);
					me.rightcard.setActiveItem(me.rightcardArr[index]);
					//默认选择如果没有选中节点,默认选中首节点
					//me.pages[index].tree.currentNodeClick();
				}
        	});
        	if(i==0){
        		btn.addCls('aaa-selected-btn');
        	}
        	me.leftbtnArr.push(btn);
        	me.righttabArray.push(page.tabs);
        }
         
        //创建左侧Card
        me.leftcard = Ext.widget('multileftcardpanel',{
        	itemArr:me.leftcardArr,
        	border : false,
            flex: 1	//否则高度0
        });
        
        //创建右侧Card
        me.rightcardArr = [];
        for(var i=0;i<me.righttabArray.length;i++){
         	//创建tab
        	var tabpanelInstance = Ext.widget("multitabpanel",{
	        	tabs:me.righttabArray[i]
	        });
	        me.righttabArr.push(tabpanelInstance);	//添加右侧tab数组
	        var mainpanelInstance = Ext.widget("multimainpanel",{
	         	tabpanel:tabpanelInstance,
	        	flex:1
	        });
	        me.rightcardArr.push(mainpanelInstance);
        }
        me.rightcard = Ext.widget('multirightcardpanel',{
        	itemArr:me.rightcardArr,
        	region:'center',
        	border : false
        });
        
        //创建左侧手风琴布局
	    var leftpanelArr = [me.leftcard];	//左侧leftpanel数组
	    for(var i=0;i<me.leftbtnArr.length;i++){
	    	leftpanelArr.push(me.leftbtnArr[i]);
	    }
        me.leftpanel = Ext.widget('panel',{		// 使用的是vbox布局，一个是riskTreeCardPanel,其他是按钮，更改了按钮的样式，使它看见起来想手风琴布局
        	// 右边框样式
        	style:'border-right: 1px  #99bce8 solid !important;',
        	border : false,
            xtype: 'panel',
            region: 'west',
            split:true,
            width: 210,
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
            title: '风险',
            iconCls: 'icon-ibm-icon-scorecards',
            items: leftpanelArr//[me.leftcard,btn1,btn2]
        });
       
        Ext.apply(me, {
        	border : false,
            items: [me.leftpanel,me.rightcard]//
		});
        me.callParent(arguments);
        
    }

});