Ext.define('Ext.scripts.component.NavigationBars', {
	localId : 'navigationBars',
	id : '',//当前节点ID
	type : '',//当前类型树
	array : null,//所有集合
	name : '',//当前指标名称
	
	settings : {
		showSpeed:        'fast',
		hideSpeed:        '',
		collapsible:      false,
		collapsedWidth:   10
	},
	
	
	constructor: function(config) {
        var me = this;
        Ext.apply(me, config);
        me.callParent(arguments);
    },
	
	renderHtml : function(divId, id, name, type){
		var classNavigation = Ext.query("*[class=navigation]")
		var div = document.getElementById(divId);
		
		if(classNavigation.length != 0){
			for(var i = 0; i < classNavigation.length; i++){
				classNavigation[i].innerHTML = '';
			}
		}
		this.id = id;
		this.name = name;
		this.type = type;
		var html = this.getHtmlStr();
		div.innerHTML = html;
		this.loadCss();
		if(this.array.length != 0){
        	this.binding();
        }
		
	},
	
	getHtml : function(id, name, type){
		this.id = id;
		this.name = name;
		this.type = type;
		var html = this.getHtmlStr();
		return html; 
	},
	
	//得到动态HTML
    getHtmlStr : function() {
    	var me = this;
    	var html = me.getStartHtml();
    	if(this.type == 'sc'){//记分卡
    		html += me.getTitleHtml(FHD.locale.get('fhd.navigationBars.category'), 'SC');
    	}else if(this.type == 'kpi'){//指标类型
    		html += me.getTitleHtml(FHD.locale.get('fhd.navigationBars.kpiType'), 'KPI');
    	}else if(this.type == 'sm'){//战略目标
    		html += me.getTitleHtml(FHD.locale.get('fhd.strategymap.strategymapmgr.target'), 'SM');
    	}else if(this.type == 'role'){//角色管理
    		html += me.getTitleHtml('角色管理', 'ROLE');
    	}else if(this.type == 'org'){//机构管理
    		html += me.getTitleHtml('机构管理', 'ORG');
    	}
    	
    	Ext.Ajax.request({
		    url: __ctxPath + '/components/NavigationBars/findNavigationBars.f?id=' + this.id + '&type=' + this.type,
		    async:  false,
		    success: function(response){
		        var text = response.responseText;
		        me.array = new Array();
		        var mapCount = Ext.JSON.decode(text).count;
		        var mapc = mapCount - 1;
		        var countroot = Ext.JSON.decode(text).countroot;
		        var gw = Ext.JSON.decode(text).gw;
		        Ext.each(Ext.JSON.decode(text).result,function(r,i){
		        	me.array.push({id:r.id, name:r.name});
		        });
		        
		        if(countroot != '-1'){
			        for(var i = 0; i < me.array.length; i++){
		        			//存在父级并且不是根
		        			if(me.array[i].id.indexOf('++,,root' + 'my') != -1){
		        				//把自己添加到A标签中
		        				var htmls = me.getMyLiHtml(me.array[i].id, me.array[i].name);
		        				if(!gw){
			        				if(me.name == ''){
				        				if(mapc != 0){
				        				}else{
				        					htmls = htmls.replace("<li", "<li class='current' ");
				        				}
			        				}
		        				}
			        			html += htmls;
		        				break;
		        			}
		        	}
			        
			        for(var i = 0; i < me.array.length; i++){
	        			//存在父级并且不是根
	        			if(me.array[i].id.indexOf('_root') != -1){
	        				//同级别添加到LI中
	        				html += me.getLiHtml(me.array[i].id, me.array[i].name);
	        			}
			        }
		        	html += me.getStartEndHtml();
		        }
		        
		        
		        if(mapc != 0){
		        	var count = 1;
			        while(true){
			        	for(var i = 0; i < me.array.length; i++){
			        			//存在父级并且不是根
			        			if(me.array[i].id.indexOf('++,,' + mapc + 'my') != -1){
			        				//把自己添加到A标签中
			        				var htmls = me.getMyLiHtml(me.array[i].id, me.array[i].name);
			        				
			        				if(me.name == ''){
				        				if(!gw){
				        					if(mapc == 1){
					        					htmls = htmls.replace("<li", "<li class='current' ");
					        				}
				        				}
			        				}
				        			
				        			html += htmls;
			        				break;
			        		}
			        	}
			        	
			        	for(var i = 0; i < me.array.length; i++){
			        		if(me.array[i].id.indexOf('_' + mapc) != -1){
			        			//存在父级并且不是根
		        				if(me.array[i].id.indexOf('_' + mapc) != -1){
			        				//同级别添加到LI中
			        				html += me.getLiHtml(me.array[i].id, me.array[i].name);
			        			}
			        		}
			        	}
			        	
			        	html += me.getStartEndHtml();
			        	count++;
			        	mapc--;
			        	if(count == mapCount){
			        		break;
			        	}
			        }
		        }
		        
		        for(var i = 0; i < me.array.length; i++){
	        		//存在父级并且不是根
	        		if(me.array[i].id.indexOf('++gw') != -1){
	        			//把自己添加到A标签中
	        			var htmls = me.getMyLiHtml(me.array[i].id, me.array[i].name);
	        			if(me.name == ''){
	        				htmls = htmls.replace("<li", "<li class='current' ");
	        			}
	        			
	        			html += htmls;
	        			break;
	        		}
	        	}
		        
		        for(var i = 0; i < me.array.length; i++){
		        	//存在父级并且不是根
	        		if(me.array[i].id.indexOf('__gw') != -1){
	        			//同级别添加到LI中
        				html += me.getLiHtml(me.array[i].id, me.array[i].name);
	        		}
	        	}
		        
		        if(me.name != ''){
		        	var htmls = me.getNameHtml(me.name);
    				html += htmls;
		        	html += me.getStartEndHtml();
		        }
		        
	        	html += me.getEndHtml();
		    }
		});
    	return html;
    },
    
    //渲染HTML样式效果
    loadCss : function(){
    	var me = this;
		me.element = $('#breadcrumbs-3');
		me._build();
	},
	
	
	_build : function(){
		var me = this;
		var element = me.element;
		if(me.settings.collapsible){
			var sz = element.children('LI').length;
			element.children('LI').children('A').css('white-space', 'nowrap').css('float', 'left');
			element.children('LI').children('A').each(function(i, el){
				if(i != sz - 1){
					$(this).css('overflow', 'hidden');
					$(this).attr('init-width', $(this).width());
					$(this).width(me.settings.collapsedWidth);
				}
			});
		}
		
        element.children('LI').mouseenter(function(){
            if($(this).hasClass('hover')){ return; }
        	me._hideAllSubLevels();
        	if(!me._subLevelExists($(this))){ return; }

        	// Show sub-level
        	var subLevel = $(this).children('UL');
        	me._showHideSubLevel(subLevel, true);
        	
        	if(me.settings.collapsible && !$(this).hasClass('current')){
        		var initWidth = $(this).children('A').attr('init-width');
        		$(this).children('A').animate({width: initWidth}, 'normal');
        	}
        });
        
        element.children('LI').mouseleave(function(){
            var subLevel = $(this).children('UL');
            me._showHideSubLevel(subLevel, false);
            
            if(me.settings.collapsible && !$(this).hasClass('current')){
            	$(this).children('A').animate({width: me.settings.collapsedWidth}, 'fast');
            }
        });
	},
	
	_hideAllSubLevels : function(){
		var me = this;
		var element = me.element;
		element.children('LI').children('UL').each(function(){
            $(this).hide();
            $(this).parent().removeClass('hover');
		});
	},
	
	_showHideSubLevel : function(subLevel, isShow){
		var me = this;
		var element = me.element;
		if(isShow){
            subLevel.parent().addClass('hover');
           
            if($.browser.msie){
            	var pos = subLevel.parent().position();
            	subLevel.css('left', parseInt(pos['left']));
            }
			if(me.settings.showSpeed != ''){  subLevel.show(); } 
			else { subLevel.show(); }
		} else {
            subLevel.parent().removeClass('hover');
            if(me.settings.hideSpeed != ''){ subLevel.fadeOut( me.settings.hideSpeed ); } 
            else { subLevel.hide(); }
		}
	},
	
	_subLevelExists : function(obj){
		return obj.children('UL').length > 0;
	},
	
	//绑定HTML(元素ID)事件
	binding : function(){
		var me = this;
		for(var i = 0; i < me.array.length; i++){
			Ext.get(me.array[i].id).addListener("click",function(obj){
				if(me.type == 'sc'){
					me.scorecardFun(obj.target.id, obj.target.text);
				}else if(me.type == 'kpi'){
					me.kpiTypeFun(obj.target.id, obj.target.text);
				}else if(me.type == 'sm'){
					me.smFun(obj.target.id, obj.target.text);
				}else if(me.type == 'role'){
					me.roleFun(obj.target.id, obj.target.text);
				}else if(me.type == 'org'){
					me.orgFun(obj.target, obj.target.text);
				}
			});
		}
	},
	
	//开始DIV
	getStartHtml : function(){
		return "<div><ul class='xbreadcrumbs' id='breadcrumbs-3'>"
	},
	
	//标题LI
	getTitleHtml : function(title, classStyle){
		return "<li><span class='" + classStyle + "'>" + title + "</span></li>";
	},
	
	//MYLI
	getMyLiHtml : function(id, name){
		return "<li><a id='" + id + "'>" + name + "</a><ul>";
	},
	
	//同级别LI
	getLiHtml : function(id, name){
		return "<li><a id='" + id + "'>" + name + "</a></li>";
	},
	
	//当前指标名称
	getNameHtml : function(name){
		return "<li class='current'><a> " + name + "</a></li>";
	},
	
	//同级别结束LI
	getStartEndHtml : function(){
		return "</ul></li>";
	},
	
	//结束DIV
	getEndHtml : function(){
		return "</ul></div>";
	},
	
	//记分卡点击函数
	scorecardFun : function(id, name){
		if(id.indexOf('my') != -1){
			var idstr = id.split('++');
			id = idstr[0];
		}if(id.indexOf('_') != -1){
			var idstr = id.split('_');
			id = idstr[0];
		}
		var scorecardtree = Ext.getCmp('scorecardtree');
		var rootNode = scorecardtree.getRootNode();
		var selectNode = scorecardtree.findNode(rootNode,id);
		scorecardtree.navNode = {};
		if(!selectNode.data){
			selectNode.data = {};
			selectNode.data.id = id;
			selectNode.data.text = name;
			selectNode.parentNode = 'navigator';
		}
		scorecardtree.selectedNodeClick(selectNode);
	},
	
	//指标类型
	kpiTypeFun : function(id, name){
		if(id.indexOf('my') != -1){
			var idstr = id.split('++');
			id = idstr[0];
		}if(id.indexOf('_') != -1){
			var idstr = id.split('_');
			id = idstr[0];
		}
		var kpitypetree = Ext.getCmp('kpitypetree');
		var rootNode = kpitypetree.getRootNode();
		var selectNode = kpitypetree.findNode(rootNode,id);
		kpitypetree.navNode = {};
		if(!selectNode.data){
			selectNode.data = {};
			selectNode.data.id = id;
			selectNode.data.text = name;
			selectNode.parentNode = 'navigator';
		}
		kpitypetree.selectedNodeClick(selectNode);
	},
	
	//战略目标
	smFun : function(id, name){
		if(id.indexOf('my') != -1){
			var idstr = id.split('++');
			id = idstr[0];
		}if(id.indexOf('_') != -1){
			var idstr = id.split('_');
			id = idstr[0];
		}
		var strategyobjectivetree = Ext.getCmp('strategyobjectivetree');
		var rootNode = strategyobjectivetree.getRootNode();
		var selectNode = strategyobjectivetree.findNode(rootNode,id);
		strategyobjectivetree.navNode = {};
		if(!selectNode.data){
			selectNode.data = {};
			selectNode.data.id = id;
			selectNode.data.text = name;
			selectNode.parentNode = 'navigator';
		}
		strategyobjectivetree.selectedNodeClick(selectNode);
	},
	//角色管理
	
	roleFun : function(id, name){
		if(id.indexOf('my') != -1){
			var idstr = id.split('++');
			id = idstr[0];
		}if(id.indexOf('_') != -1){
			var idstr = id.split('_');
			id = idstr[0];
		}
		var roleTreePanel = Ext.getCmp('roleTreePanel');//得到叶签
		var roleTabPanel = Ext.getCmp('roleTabPanel');//得到叶签
		var rolePersonGridPanel = Ext.getCmp('rolePersonGridPanel');//得到grid
		var roleBasicPanel = Ext.getCmp('roleBasicPanel');//得到角色基本信息form
		var authorityTree = Ext.getCmp('authorityTree');//得到角色授权树(左)
		var authorityShowTree = Ext.getCmp('authorityShowTree');//得到权限树展示树(右)
		var roleRightPanel = Ext.getCmp('roleRightPanel');//导航
    	rolePersonGridPanel.store.proxy.url = rolePersonGridPanel.roleQueryUrl + "?roleId=" + id;//角色对应人员列表url
    	//authorityShowTree.store.proxy.url = roleTreePanel.authorityShowTreeUrl + '?id=' + id;//加载权限树显示树
		//roleTabPanel.setActiveTab(0);
		roleBasicPanel.roleTreeId = id;
		authorityTree.roleTreeId= id;
		authorityShowTree.roleTreeId= id;
		rolePersonGridPanel.roleTreeId = id;
		roleTreePanel.roleTreeId = id;
		authorityTree.extraParams.id = id;
		authorityShowTree.extraParams.id = id;
		roleBasicPanel.load();
		rolePersonGridPanel.store.load();
		authorityTree.store.load();
		authorityShowTree.store.load();
		rolePersonGridPanel.setstatus();//设置按钮可用状态
		roleBasicPanel.isAdd = true;//标志位修改为true添加为false
		roleRightPanel.navigationBar.renderHtml('rolerightPanelDiv',id, '', 'role');
		roleTreePanel.selectedNodeClick(id);//选中树节点
	},
	
	//机构管理
	orgFun : function(id, name){
		realId = id.id;
		if(realId.indexOf('my') != -1){
			var idstr = realId.split('++');
			realId = idstr[0];
		}if(realId.indexOf('_') != -1){
			var idstr = realId.split('_');
			realId = idstr[0];
		}
		var tabPanel = Ext.getCmp('tabPanel');
		var treepanel = Ext.getCmp('treePanel');
		var orgGridPanel = Ext.getCmp('tabpanelorgGridPanel');
		var empGridPanel = Ext.getCmp('tabpanelempGridPanel');
		var orgEditPanel = Ext.getCmp('orgEditPanel');
		var cardPanel = Ext.getCmp('cardPanel');
		var rightPanel = Ext.getCmp('rightPanel');//导航
		var positionTabPanel = Ext.getCmp('positionTabPanel');
		var positionGridPanel = Ext.getCmp('positionGridPanel');
		var positionEditPanel = Ext.getCmp('positionEditPanel');
		var posiOrgGrid = Ext.getCmp('positionorgGridPanel');
		var posiEmpGrid = Ext.getCmp('positionempGridPanel');
		var rootNode = treepanel.getRootNode();//得到根节点
		var selectNode = treepanel.findNode(rootNode,realId);
		treepanel.getSelectionModel().select(selectNode);//选中节点
		treepanel.currentNode = selectNode;//将选中的节点传给树
		if(id.id.indexOf('gw')== -1){
			cardPanel.layout.setActiveItem(tabPanel);
			if(tabPanel.getActiveTab()==tabPanel.items.items[0]){
				orgGridPanel.store.proxy.url = orgGridPanel.queryUrl;//动态赋给机构列表url
	  			orgGridPanel.store.proxy.extraParams.orgIds = realId;
	  			orgGridPanel.store.load();
			}else if(tabPanel.getActiveTab()==tabPanel.items.items[1]){
				positionGridPanel.store.proxy.url = positionGridPanel.queryUrl;
	  			positionGridPanel.store.proxy.extraParams.orgId = realId;
	  			positionGridPanel.store.load();
			}else if(tabPanel.getActiveTab()==tabPanel.items.items[2]){
				empGridPanel.store.proxy.url = empGridPanel.queryUrl;
  				empGridPanel.store.proxy.extraParams.orgIds = realId;
  				empGridPanel.store.load();
			}else if(tabPanel.getActiveTab()==tabPanel.items.items[3]){
				orgEditPanel.orgtreeId = realId;
				orgEditPanel.load();
			}
			rightPanel.navigationBar.renderHtml('organizationNavDiv', realId, '', 'org');
		}else{
			cardPanel.layout.setActiveItem(positionTabPanel);
			if(positionTabPanel.getActiveTab()==positionTabPanel.items.items[0]){
				posiEmpGrid.store.proxy.url = posiEmpGrid.queryUrl;
				posiEmpGrid.store.proxy.extraParams.positionIds = realId;
				posiEmpGrid.store.load();
			}else if(positionTabPanel.getActiveTab()==positionTabPanel.items.items[1]){
				positionEditPanel.orgtreeId = realId;
  				positionEditPanel.load();
			}
			//导航
			rightPanel.navigationBar.renderHtml('organizationNavDiv', realId, '', 'org');
			
		}
	}



});