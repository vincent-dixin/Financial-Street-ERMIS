<%@ page language="java" pageEncoding="utf-8"%> 
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<html>
<head>
<base href="${req.scheme}://${req.serverName}:${req.serverPort}${ctx}/">
		<title>第一会达风险管理平台</title>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		
		<link rel="icon" href="${req.scheme}://${req.serverName}:${req.serverPort}${ctx}/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="${req.scheme}://${req.serverName}:${req.serverPort}${ctx}/favicon.ico" type="image/x-icon" />  
		
		
		<!-- 主界面CSS -->
		<link rel="stylesheet" type="text/css" href="<c:url value='/scripts/ext-4.1/resources/css/ext-all-debug.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/FHDstyle.css'/>"/>
		<!-- Shared -->
		<!-- ext4默认样式CSS-->
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/icon.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/w.css'/>" />
		
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/xbreadcrumbs.css'/>" />
		
		 
		<link rel="shortcut icon" href="<c:url value='/favicon.ico'/>" type="image/x-icon" />
		<link rel="icon" href="<c:url value='/favicon.ico'/>" type="image/x-icon" />
		
		<script type="text/javascript" src="<c:url value='/scripts/jquery-1.7.2.min.js'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/scripts/xbreadcrumbs.js'/>"></script>
		
		<!-- ext4核心JS -->
		<script type="text/javascript" src="<c:url value='/scripts/ext-4.1/ext-all.js.gzjs'/>"></script>
		<!-- ext4中文支持JS -->
		<script type="text/javascript" src="<c:url value='/scripts/ext-4.1/locale/ext-lang-zh_CN.js'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/scripts/kindeditor-4.1.1/kindeditor.js'/>"></script>
		
		<!-- 公用JS -->
		<script type="text/javascript" src="<c:url value='/scripts/fhd.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/app/view/kpi/result/ResultParam.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/App2.js'/>" defer="defer"></script>
		<script type="text/javascript" src="<c:url value='/scripts/commons/dynamic.jsp'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/commons/selectable.js'/>"></script>
		<!-- 本地 国际化资源 -->
		<script type="text/javascript" src="<c:url value='/i18n'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/locale.js'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/UserAuth'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/authority.js'/>"></script>
		
		
		

                <style type="text/css">
	                .x-grid-row-over .x-grid-cell-inner {
			            font-weight: bold;
			        }
					.x-action-col-cell img {
			            height: 16px;
			            width: 16px;
			            cursor: pointer;
			        }
				    /*  Custom styles for breadcrums (#breadcrumbs-3)  */
				    .xbreadcrumbs {
				        background: none;
				    } 
				    .xbreadcrumbs LI A {
				        text-decoration: underline;
        				color: #2c4674;
				    }
				    .xbreadcrumbs LI A:HOVER, .xbreadcrumbs#breadcrumbs-3 LI.hover A { color:#ff9900; text-decoration: none; }
				    .xbreadcrumbs LI.current A {
				        color: #333333;
				        text-decoration: none;
				    }
				    .xbreadcrumbs LI {
				    	height:25px;
				    	cursor:hand;
				        border-right: none;
				        background: url('images/separator-arrow.gif') no-repeat right center;
				        padding-right: 15px;
				        padding-left: 10px;
				    }
				    .xbreadcrumbs LI.current { background: none; }
				    .xbreadcrumbs LI UL LI { background: none; padding: 0;  }
				    
				
				.aaa-btn{
					background-image:none !important;
					background-attachment:scroll !important;
					background-repeat:repeat !important;
					background-position-x:0% !important;
					background-position-y:0% !important;
					background-color:rgb(217, 231, 248) !important;
				}
				
				.aaa-selected-btn{
					background-color:rgb(192,210, 230) !important;
				}
				
				.menu-btn{
					background-image:none !important;
					background-attachment:scroll !important;
					background-repeat:repeat !important;
					background-position-x:0% !important;
					background-position-y:0% !important;
					background-color:#FFF !important; 
				}
				.menu-selected-btn{background-color:rgb(223, 232, 246) !important; border-left:25px rgb(0,117,170) solid  !important;}
				 #main-panel-mydatas td {
            			padding:5px;
       		 }
				</style>
		
		
		<style type="text/css">
		
			#loading {
				position: absolute;
				left: 42%;
				float:right;
				top: 45%;
				padding: 2px;
				z-index: 20001;
				height: 50px;
				border: 2px solid #ccc;
			}
			.x-panel-ghost {
			    z-index: 1;
			}
			.x-border-layout-ct {
			    background: #DFE8F6;
			}
			.x-portal-body {
			    padding: 0 0 0 8px;
			}
			.x-portal .x-portal-column {
			    padding: 8px 8px 0 0;
			}
			.x-portal .x-panel-dd-spacer {
			    border: 2px dashed #99bbe8;
			    background: #f6f6f6;
			    border-radius: 4px;
			    -moz-border-radius: 4px;
			    margin-bottom: 10px;
			}
			.x-portlet {
			    margin-bottom:10px;
			    padding: 1px;
			}
			.x-portlet .x-panel-body {
			    background: #fff;
			}
			.portlet-content {
			    padding: 10px;
			    font-size: 12px;
			}
			.x-tab-default-top .x-tab-inner {
			height: 14px !important;
			line-height: 14px !important;
			}
		</style>
		<script type="text/javascript">
		//alert(FHD.Authority.ifNotGranted('ROLE_ALARMMGR,ROLE_AUTHORITY2'));
		Ext.Error.ignore = true;
		
		window.onerror = function(msg,url,l){
			var txt="";
			txt+="Error: " + msg + "<br />";

			txt+="URL: " + url + "<br />";

			txt+="Line: " + l + "<br /><br />";
			Ext.create('Ext.window.Window',{
				title:'错误',
				height:400,
				frame:false,
				width:500,
				html:txt
			}).show();
			return true;
		}
		//throw new Error("sss");
		
			String.prototype.endWith = function(s) {
				if (s == null || s == "" || this.length == 0 || s.length > this.length)
					return false;
				if (this.substring(this.length - s.length) == s)
					return true;
				else
					return false;
				return true;
			}
			String.prototype.startWith = function(s) {
				if (s == null || s == "" || this.length == 0 || s.length > this.length)
					return false;
				if (this.substr(0, s.length) == s)
					return true;
				else
					return false;
				return true;
			}
			/*
			 * 
			 * string:原始字符串 substr:子字符串 isIgnoreCase:忽略大小写
			 */

			function contains(string, substr, isIgnoreCase) {
				if (isIgnoreCase) {
					string = string.toLowerCase();
					substr = substr.toLowerCase();
				}
				var startChar = substr.substring(0, 1);
				var strLen = substr.length;
				for (var j = 0; j < string.length - strLen + 1; j++) {
					if (string.charAt(j) == startChar)// 如果匹配起始字符,开始查找
					{
						if (string.substring(j, j + strLen) == substr)// 如果从j开始的字符与str匹配，那ok
						{
							return true;
						}
					}
				}
				return false;
			}
			
			
			
			/**
			 * 修复Grid的loadMask的BUG
			 */
			Ext.override(Ext.view.AbstractView, {
				loadingText: "读取中...",
			    onRender: function(){ 
			        var me = this;
			        me.callOverridden();
			        if (me.mask && Ext.isObject(me.store)) {
			            me.setMaskBind(me.store);
			        }
			    }
			});
			
			
			Ext.override(Ext.AbstractComponent,{
				destroy : function() {
			        var me = this,
			            selectors = me.renderSelectors,
			            selector,
			            el;
					
			        
			        if (!me.isDestroyed) {
			            if (!me.hasListeners.beforedestroy || me.fireEvent('beforedestroy', me) !== false) {
			                me.destroying = true;
			                me.beforeDestroy();

			                if (me.floating) {
			                    delete me.floatParent;
			                    // A zIndexManager is stamped into a *floating* Component when it is added to a Container.
			                    // If it has no zIndexManager at render time, it is assigned to the global Ext.WindowManager instance.
			                    if (me.zIndexManager) {
			                        me.zIndexManager.unregister(me);
			                    }
			                } else if (me.ownerCt && me.ownerCt.remove) {
			                    me.ownerCt.remove(me, false);
			                }

			                me.onDestroy();

			                // Attempt to destroy all plugins
			                Ext.destroy(me.plugins);

			                if (me.hasListeners.destroy) {
			                    me.fireEvent('destroy', me);
			                }
			                Ext.ComponentManager.unregister(me);

			                me.mixins.state.destroy.call(me);

			                me.clearListeners();
			                // make sure we clean up the element references after removing all events
			                if (me.rendered) {
			                    // In case we are queued for a layout.
			                    Ext.AbstractComponent.cancelLayout(me);

			                    if (!me.preserveElOnDestroy) {
			                        me.el.remove();
			                    }
			                    me.mixins.elementCt.destroy.call(me); // removes childEls
			                    if (selectors) {
			                        for (selector in selectors) {
			                            if (selectors.hasOwnProperty(selector)) {
			                                el = me[selector];
			                                if (el) { // in case any other code may have already removed it
			                                    delete me[selector];
			                                    el.remove();
			                                }
			                            }
			                        }
			                    }
			                    delete me.el;
			                    delete me.frameBody;
			                    delete me.rendered;
			                }

			                me.destroying = false;
			                me.isDestroyed = true;
			            }
			        }
			    }
			});
			
			
			/*重写treeStore的load方法,向后台发送除了id以外的node属性*/
			
			Ext.override(Ext.data.TreeStore,{
				load: function(options) {
			        options = options || {};
			        options.params = options.params || {};
	
			        var me = this,
			            node = options.node || me.tree.getRootNode(),
			            root;
	
			        // If there is not a node it means the user hasnt defined a rootnode yet. In this case lets just
			        // create one for them.
			        if (!node) {
			            node = me.setRootNode({
			                expanded: true
			            }, true);
			        }
	
			        if (me.clearOnLoad) {
			            if(me.clearRemovedOnLoad) {
			                // clear from the removed array any nodes that were descendants of the node being reloaded so that they do not get saved on next sync.
			                me.clearRemoved(node);
			            }
			            // temporarily remove the onNodeRemove event listener so that when removeAll is called, the removed nodes do not get added to the removed array
			            me.tree.un('remove', me.onNodeRemove, me);
			            // remove all the nodes
			            node.removeAll(false);
			            // reattach the onNodeRemove listener
			            me.tree.on('remove', me.onNodeRemove, me);
			        }
	
			        Ext.applyIf(options, {
			            node: node
			        });
			        options.params[me.nodeParam] = node ? node.getId() : 'root';
			        options.params['dbid'] = node ? node.data.dbid : '';
			        options.params['code'] = node ? node.data.code : '';
			        options.params['leaf'] = node ? node.data.leaf : '';
			        options.params['iconCls'] = node ? node.data.iconCls : '';
			        options.params['cls'] = node ? node.data.cls : '';
			        options.params['type'] = node ? node.data.type : '';
	
			        if (node) {
			            node.set('loading', true);
			        }
	
			        return me.callParent([options]);
			    }

			});
			
		   	Ext.onReady(function(){
		   		Ext.tip.QuickTipManager.init();
		   		Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
		   		    showDelay: 500      // Show 50ms after entering target
		   		});  
	
		   		
		   		
		   		/***************切换皮肤开始**********************/
				/*var storeTheme=FHD.getCookie('theme');
				if(storeTheme==null || storeTheme==''){
				 	storeTheme='ext-all-debug';
				}
				Ext.util.CSS.swapStyleSheet("theme", __ctxPath+"/scripts/ext-4.1/resources/css/"+storeTheme+".css");
				 var combox = Ext.create('Ext.form.ComboBox', {
					width:60,
				    store : [['ext-all-debug', FHD.locale.get('fhd.common.skinDefault')]
					,['ext-all-gray', FHD.locale.get('fhd.common.skinGray')]
				    ,['ext-all-access', FHD.locale.get('fhd.common.skinHighlight')]
				    ,['ext-neptune', '新视觉']],
				    triggerAction : 'all',
				    queryMode: 'local',
				    labelHiden:true,
				    value : FHD.locale.get('fhd.common.skinChange'),
				    editable : false,
				    renderTo : 'skinchange',
				    listeners : {
						scope : this,
						'select' : function(d, b, c) {
							if (d.value != '') {
								var a = new Date();
								a.setDate(a.getDate() + 300);
								FHD.setCookie('theme', d.value, a, __ctxPath);
								Ext.util.CSS.swapStyleSheet('theme', __ctxPath
												+ '/scripts/ext-4.1/resources/css/'
												+ d.value + '.css');
							}
						}
					}
				}); */
				/***************切换皮肤结束**********************/
		    });
		   	//在线帮助
		   function helponline(){
				var btn = new Object();
				btn.url = "/sys/helponline/helpOnlineView.do";
				btn.text = "在线帮助";
				onButtonClick(btn);
				return false;
			}
		   //点击按钮事件处理方法
		   function onButtonClick(btn){
				var url = '${ctx}' + btn.url;
				var centerPanel = Ext.getCmp('center-panel');
		    	var tab = centerPanel.getComponent(url+'${param._dc}');
		    	if(tab){
		    		centerPanel.setActiveTab(tab);
		    	}else{
		    		if(centerPanel.items.length >= 6)
		    			centerPanel.remove(1);
		        	    var p = centerPanel.add({
		        	    	id:url+'${param._dc}',
		        	        title: btn.text,
		                	tabTip:btn.text,
		        	        layout:'fit',
		        	        autoWidth:true,
		        	        //iconCls: 'tabs',
		        	        html:"<iframe width='100%' height='100%' scrolling='auto' stype='overflow-y:hidden;' noresize='noresize' src='" + url + "' frameborder='0'></iframe>",
		        	        closable:true
		        	    });
		        	    centerPanel.setActiveTab(p);
		    	}
		    } 
		   //点击菜单添加tab
		   function onMenuClick(menu){
				var url = menu.url;
				var centerPanel = Ext.getCmp('center-panel');
				var tab = centerPanel.getComponent(url+'${param._dc}');
				if(tab){
					centerPanel.setActiveTab(tab);
				}else{
					if(url.startWith('FHD')){
						var p = centerPanel.add(Ext.create(url,{
							id:url+'${param._dc}',
							title: menu.text,
							tabTip:menu.text,
							closable:true
						}));
						centerPanel.setActiveTab(p);
					} else if (url.startWith('/pages')){
						var p = centerPanel.add({
							id:url+'${param._dc}',
							title: menu.text,
							tabTip:menu.text,
							layout:'fit',
							autoWidth:true,
							border:false,
							//iconCls: 'tabs',
							closable:true,
							autoLoad :{ url: __ctxPath+url,scripts: true}
						});
						centerPanel.setActiveTab(p);
					}else{
						var p = centerPanel.add({
							id:url+'${param._dc}',
							title: menu.text,
							tabTip:menu.text,
							layout:'fit',
							autoWidth:true,
							border:false,
							//iconCls: 'tabs',
							closable:true,
							html : '<iframe width=\'100%\' height=\'100%\' frameborder=\'0\' src=\''+__ctxPath+url+'\'></iframe>'
							//autoLoad :{ url: 'pages/icon.jsp',scripts: true}
							//items:[{xtype:'dictTypelist'}]
						});
						centerPanel.setActiveTab(p);
					}
				}
			}
		   //添加到收藏
		   var addRemark = function (obj, url, title) {
			    var e = window.event || arguments.callee.caller.arguments[0];
			    var B = {
			        IE : /MSIE/.test(window.navigator.userAgent) && !window.opera
			        , FF : /Firefox/.test(window.navigator.userAgent)
			        , OP : !!window.opera
			    };
			    obj.onmousedown = null;
			    if (B.IE) {
			        obj.attachEvent("onmouseup", function () {
			            try {
			                window.external.AddFavorite(url, title);
			                window.event.returnValue = false;
			            } catch (exp) {}
			        });
			    } else {
			        if (B.FF || obj.nodeName.toLowerCase() == "a") {
			            obj.setAttribute("rel", "sidebar"), obj.title = title, obj.href = url;
			        } else if (B.OP) {
			            var a = document.createElement("a");
			            a.rel = "sidebar", a.title = title, a.href = url;
			            obj.parentNode.insertBefore(a, obj);
			            a.appendChild(obj);
			            a = null;
			        }
			    }
			};
			
			//去除数组中重复的元素，参数：数组，返回值：去除重复元素后的数组
			var uniq = function (arr) {
				var a = [], o = {}, i, v, len = arr.length;
				if (len < 2) {
				return arr;
				}
				for (i = 0; i < len; i++) {
				v = arr[i];
				if (o[v] !== 1) {
				a.push(v);
				o[v] = 1;
				}
				}
				return a;
				}
	    </script>
	</head>
	<body>
	<script type="text/javascript" src="<c:url value='/scripts/commons/Vtypes.js'/>"></script>
	<div id="loading">
             <div class="loading-indicator">
             	 <img
					src="${ctx}/images/extanim32.gif"
					width="32" height="32"
					style="margin-right: 8px; float: left; vertical-align: top;" />
				<span id="loading-msg">验证用户身份...</span>
             </div>
        </div> 
        <div id="loading-mask"></div> 
		<div id="app-header2" >
			<div style="float: left;height:40px;padding-left:26px;padding-top:8px;font-size: 20px;color: white;font-weight: bold;" >
				<img src="${ctx}/images/logo-white.png"
					width="26" height="26"
					style="margin-right: 8px; float: left; vertical-align: top;" />风险管理与内部控制系统
			</div>
			<div align="right" style="padding:8px;padding-right:10px;color: white;">
			欢迎您，${user.realname}(${user.username}) 
					&nbsp;<a href="<c:url value="/j_spring_security_logout"/>" onclick = ""><img src="${ctx}/images/icons/door_out.png" align="bottom" title="退出" style="margin-top: 4px;"/></a>
				    &nbsp;<a href="javascript:void(0)" onclick="helponline()"><img src="${ctx}/images/icons/help.png " align="bottom" title="帮助" style="margin-top: 4px;"/></a>
					&nbsp;<a href="javascript:void(0)" onclick="addRemark(this,location.href,'第一会达风险管理与内部控制系统')"><img src="${ctx}/images/icons/star.png" align="bottom" title="收藏" style="margin-top: 4px;"/></a>
					<div style="display:none" id="skinchange"></div>
					</div>
			<div id="header-nav2" style="position: absolute;left: 0px;"></div>		
		</div>
	</body>
	<script type="text/javascript">
	setTimeout(function(){
		document.getElementById('loading-msg').innerHTML = '正在装载页面...';
	}, 100);
	</script>
		<!-- 图表JS -->
		<!--[if IE 6]>
		        <script src="<c:url value='/scripts/chart/DD_belatedPNG_0.0.8a-min.js'/>"></script>        
		<![endif]-->
		
	<script type="text/javascript" src="<c:url value='/scripts/chart/highcharts.js'/>" ></script>
	<script type="text/javascript" src="<c:url value='/scripts/chart/FusionCharts.js'/>" ></script>
	<script type="text/javascript" src="${ctx}/scripts/component/meshStructureChart/meshStructureChart.js"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/scripts/component/meshStructureChart/meshStructureChart.css'/>" />
	<script type="text/javascript" src="<c:url value='/scripts/mxgraph-1.10/js/mxgraph.js'/>"></script>
</html>
