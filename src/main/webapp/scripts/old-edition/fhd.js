FHD={};FHD.win={};FHD.util={};FHD.ext={};
FHD.util.StringUtil={};FHD.util.UI={};FHD.util.Object={};
//Ext.BLANK_IMAGE_URL=contextPath+'/scripts/ext-3.3.0/resources/images/default/s.gif';
function changeSkin(value,css) {
		window.top.theme=value;
		//window.top.css=css;
		Ext.util.CSS.swapStyleSheet('window',contextPath+'/scripts/ext-3.3.0/resources/css/' + value + '.css');
		//Ext.util.CSS.swapStyleSheet('fhd',contextPath+'/css/' + css + '.css');
		var oframe = document.getElementsByTagName('iframe');
		for(var i=0;i<oframe.length;i++)changeSkinFrame(oframe[i]);
	};
function changeSkinFrame(frame){
		try{
		frame.contentWindow.Ext.util.CSS.swapStyleSheet('window',contextPath+'/scripts/ext-3.3.0/resources/css/'+window.top.theme+'.css');
		//frame.contentWindow.Ext.util.CSS.swapStyleSheet('fhd',contextPath+'/css/'+window.top.css+'.css');
		}catch(e){}
		var oframe = frame.contentWindow.document.getElementsByTagName('iframe');
		for(var i=0;i<oframe.length;i++)changeSkinFrame(oframe[i]);
}
Ext.onReady(function(){
	try{if(window.top.theme && window.top.css)changeSkin(window.top.theme,window.top.css)}catch(e){}
});
FHD.win.PopWindow=function(){};
FHD.win.PopWindow.prototype.pop = function(title,width,height,url){this.win = new window.top.Ext.Window({renderTo:window.top.Ext.getBody(),layout: 'fit',title: title,closeAction: 'close',modal:true,width:width,height:height,html:"<iframe src='" + url + "' style='height:100%;width:100%'></iframe>"});this.win.show();};
FHD.win.PopWindow.prototype.close = function(callback,obj){callback(obj);this.win.close();};
FHD.win.PopWindow.prototype.closeMsg = function(isSuccess){this.win.close();if(isSuccess)Ext.MessageBox.alert('成功', '操作成功');elseExt.MessageBox.alert('失败', '操作失败');};
FHD.win.PopWindow.showHtml = function(shtml){new Ext.Window({renderTo:Ext.getBody(),layout: 'fit',title: '',manager:getTopTips(),closeAction: 'close',modal:true,width:400,height:300,autoScroll:true,html:shtml}).show();}
FHD.util.Object.props=function(o,isAlert,isAll){if(isAll){var msg = '';for(var p in o){try{msg += p + ':' + ((o[p]+'').indexOf('function')>-1?'function':(o[p]+''));}catch(e){msg += '<span style="color:red">' + p + '[ERROR]' + '</span>'}msg += (isAlert?'\n':'<br>');}if(isAlert){alert(msg);}else{FHD.win.PopWindow.showHtml(msg)};}else{for(var p in o)alert(p + '\t\t' + o[p]);}}
FHD.util.Object.searchProperty=function(o,propName){for(var p in o)if(p.indexOf(propName)>-1)alert(p + '\t\t' + o[p]);}
FHD.util.StringUtil.shortString=function(str,len){if(str.length>len)return str.substr(0,len)+'...';else  return str;}
Array.prototype.remove=function(dx){if(isNaN(dx)||dx>this.length){return false;}for(var i=0,n=0;i<this.length;i++){if(this[i]!=this[dx]){this[n++]=this[i]}}this.length-=1 } 
FHD.util.UI.expand = function(id){if(document.getElementById(id)!=null)document.getElementById(id).style.height = document.body.offsetHeight - 6 + 'px';}
FHD.util.UI.expandByTag = function(tagName){var els = document.getElementsByTagName(tagName);for(var i=0;i<els.length;i++)els[i].style.height = document.body.offsetHeight + 'px';}
/***  全屏弹出窗口 相关函数  ****/
function parentWindow(){var windowId=getWindowId();if(window.top.modals){for(var i=0;i < window.top.modals.length;i++){if(window.top.modals[i].key == windowId){return window.top.modals[i].value;}}}return window.parent;}
function getWindowId(){if(window.windowId&&window.windowId.indexOf('win')>-1)return window.windowId;var s=window.location.href.substring(window.location.href.indexOf('windowId=')+9);return s.substring(0,s.indexOf('$ewin')+5);}
function compactWindowVar(){var wins=window.top.modals;for(var i=0;i<wins.length;i++){if(!wins[i].value){wins.remove(i);}}}
function openWindow(w_title, w_width, w_height, w_url,scrolling) {
	var scroll = "auto";
	var pid = 'win' + Math.random()+'$ewin';
	if(w_url.indexOf('?')>0)w_url+='&windowId='+pid;
	if(w_url.indexOf('?')<0)w_url+='?windowId='+pid;
	if(!window.top.modals)window.top.modals = new Array();
	if(scrolling)scroll=scrolling;
	window.top.modals[window.top.modals.length] = {key:pid,value:window};
	var win = new window.top.Ext.Window({id : 'o_window' + pid,manager:getTopTips(),draggable:false,maximizable:true,constrainHeader:true,resizable:false,iconCls:'windowIcon',
		title :w_title,width : w_width,height : w_height,layout : 'fit',buttonAlign : 'center',modal : true,closeAction : 'close',alwaysOnTop: false,
		html : "<iframe src='" + w_url + "' style='width:100%;height:100%;margin:0px 0px' scrolling='"+scroll+"' frameBorder='0'></iframe>"});
	win.show();return win;
}
function openWindowSimple(w_title,w_url,w_scrolling){openWindow(w_title,window.top.document.body.offsetWidth*0.8,window.top.document.body.offsetHeight*0.8,w_url,w_scrolling);}
function closeWindow(){var owindow=window.top.Ext.getCmp('o_window'+getWindowId());if(owindow){owindow.close();}else{window.close();}}
function getTopTips(){if(!window.top.zIndex)window.top.zIndex=100;window.top.zIndex+=5;var tips=new window.top.Ext.WindowGroup();tips.zseed=window.top.zIndex;return tips;}
/*****************文件上传组件******************
 * width:打开的文件上传页面的宽度；
 * height:打开的文件上传页面的高度；
 * callBack：回调函数，参数：id(上传后的实体的ID)
 * templateName:模板的文件名(必须放在系统的模板文件夹里；)
 * 
 * 
 * */
function openFileUploadWindow(width,height,callBack,templateName){
	if(templateName)
	{
		openWindow('文件上传',width,height,contextPath+"/components/sys/showUploadPage.do?callBack="+callBack+"&templateName="+templateName);
	}
	else
	{
		openWindow('文件上传',width,height,contextPath+"/components/sys/showUploadPage.do?callBack="+callBack);
	}
}
/*****************业务组件-风险列*******************/
FHD.RiskGrid=function(containerId,params){
	dataurl=contextPath+'/components/risk/list.do';
	var cm = [{header:'风险编号',dataIndex:'id',width:20},{header:'风险名称',dataIndex:'riskName',width:20},{header:'风险级别',dataIndex:'eLevel',width:10},{header:'上级风险',dataIndex:'parentRiskName',width:20}];
    var tempaltes= '<hr><span style="padding-left:40px"><b>风险描述:</b> {riskDesc}</span><br><br><span style="padding-left:40px"><b>风险影响:</b> {riskEffect}</span><br>';
	this.extGrid=new FHD.ext.Grid(containerId,params,cm,tempaltes,'风险列表',700,300,dataurl);
}
FHD.RiskGrid.prototype.getStore=function(){return this.extGrid.store;}
FHD.RiskGrid.prototype.getGrid=function(){return this.extGrid.grid;}
FHD.RiskGrid.prototype.query=function(obj){this.extGrid.store.baseParams=obj;this.extGrid.store.load({params:{start:0, limit:20}})}
/*****************列表组件*******************/
FHD.ext.Grid=function(cId,params,cols,exTemp,title,width,height,dataurl,nopage,ischeck,toolbar,buttombar,sortMode,sort,dir){
	if(sortMode!=false&&sortMode!=true){
		sortMode=true;
	}
	this.width=width;this.height=height;this.title=title;this.cols=cols;this.exTemplate=exTemp;this.nopage=nopage;this.ischeck=ischeck;this.toolbar=toolbar;this.buttombar=buttombar;this.sortMode=sortMode;
	var colNames=new Array();for(var i=0;i<cols.length;i++){colNames[i]=cols[i].dataIndex;}
	var ncols=new Array();for(var i=0;i<cols.length;i++){var sortable = true;if(cols[i].sortable==false){sortable=false;}if(cols[i].width!=0){cols[i].sortable=sortable,ncols[ncols.length]=cols[i]}}this.cols=ncols;
	if(sortMode){
		if(!sort){
			sort="id";
		}
		if(!dir){
			dir="ASC";
		}
		this.store = new Ext.data.JsonStore({root: 'datas',totalProperty:'totalCount',remoteSort:sortMode,sortInfo:{field:sort,direction:dir},proxy: new Ext.data.HttpProxy({url:dataurl}),fields:colNames});
	}else{
		this.store = new Ext.data.JsonStore({root: 'datas',totalProperty:'totalCount',proxy: new Ext.data.HttpProxy({url:dataurl}),fields:colNames});	
	}
    this.store.baseParams=params;this.createGrid();
    var g = this.grid;g.render(cId);g.store.load({params:{start:0, limit:20}});
}
FHD.ext.Grid.prototype.createGrid=function(){
	var store = this.store;var width=this.width;var height=this.height;var title=this.title;var plugins=[];
	var pnstore = new Ext.data.ArrayStore({
        fields: ['psize'],
        data : [[10],[20],[30],[50],[100],[200]]
    });
    var pncombo = new Ext.form.ComboBox({
        store: pnstore,
        displayField:'psize',
        typeAhead: true,
        mode: 'local',
        width:100,
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'每页条数',
        selectOnFocus:true
    });
    /*
    var restore = new Ext.data.ArrayStore({
        fields: ['psize'],
        data : [['不刷新'],[30],[60],[100],[200]]
    });
    var recombo = new Ext.form.ComboBox({
        store: restore,
        displayField:'psize',
        typeAhead: true,
        mode: 'local',
        width:90,
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'刷新间隔',
        selectOnFocus:true
    });
    */
	var cols=new Array();
	var sm=new Ext.grid.CheckboxSelectionModel({});
	cols[cols.length]=new Ext.grid.RowNumberer({header:'',width:30, renderer:function(value,metadata,record,rowIndex){	return record.store.lastOptions.params.start + 1 + rowIndex;}});
	if(this.ischeck)cols[cols.length]=sm;
	if(this.exTemplate!=null){var t=this.exTemplate;cols[1]=new Ext.ux.grid.RowExpander({tpl : new Ext.Template(t)});plugins[plugins.length]=cols[1];}
	var cols=cols.concat(this.cols);
	var bbars=null;var disInfo=true;var toolbar=null;var btns=[];var buttombar = [];
	if(this.nopage=='disbar')disInfo=false;
	if(disInfo){
		if(this.buttombar)
			buttombar = this.buttombar;
		for(i=0;i<buttombar.length;i++){btns.push(buttombar[i]);}btns.push(pncombo);//btns.push(recombo);
	}
	if(this.nopage!=true){
		bbars=new Ext.PagingToolbar({buttons:btns,pageSize:20,store:store,displayInfo:disInfo,displayMsg:'共{2}条数据，显示 {0} - {1}条',emptyMsg:'没有数据',prependButons:true,plugins:new Ext.ux.ProgressBarPager()});
	}
	if(this.toolbar)toolbar=this.toolbar;
	this.grid = new Ext.grid.GridPanel({sm:sm,
        width:width,height:height,title:title,store:store,loadMask:true,stripeRows:true,trackMouseOver:true,plugins:plugins,columns:cols,
        viewConfig: {forceFit:true},bbar:bbars,tbar:toolbar
    });
    pncombo.grid=this.grid;
	pncombo.on('select',function(c,r){
		bbars.pageSize=r.get('psize');
		this.grid.store.reload({params:{start:0,limit:bbars.pageSize}});
	});
	/*
	recombo.grid=this.grid;
	recombo.on('select',function(c,r){
			try{clearInterval(this.interval)}catch(e){}
			if(r.get('psize')!='不刷新'){
				var grid = this.grid;
				this.interval = setInterval(function(){grid.store.reload()},r.get('psize')*1000);
			}
	});
	*/
};
FHD.ext.Grid.prototype.getStore=function(){return this.store;};
FHD.ext.Grid.prototype.getGrid=function(){return this.grid;};
/*****************Editor列表组件*******************/
FHD.ext.EditGrid=function(cId,params,cols,exTemp,title,width,height,dataurl,nopage,ischeck,toolbar,buttombar){
	this.width=width;this.height=height;this.title=title;this.cols=cols;this.exTemplate=exTemp;this.nopage=nopage;this.ischeck=ischeck;this.toolbar=toolbar;this.buttombar=buttombar;
	var colNames=new Array();for(var i=0;i<cols.length;i++){colNames[i]=cols[i].dataIndex;}
	var ncols=new Array();for(var i=0;i<cols.length;i++){if(cols[i].width!=0){ncols[ncols.length]=cols[i]}}this.cols=ncols;
	this.store = new Ext.data.JsonStore({root: 'datas',totalProperty:'totalCount',remoteSort: false,proxy: new Ext.data.HttpProxy({url:dataurl}),fields:colNames});
    this.store.baseParams=params;this.createGrid();
    var g = this.grid;g.render(cId);g.store.load({params:{start:0, limit:20}});
};
FHD.ext.EditGrid.prototype.createGrid=function(){
	var store = this.store;var width=this.width;var height=this.height;var title=this.title;var plugins=[];
	var cols=new Array();
	var sm=new Ext.grid.CheckboxSelectionModel({});
	cols[cols.length]=new Ext.grid.RowNumberer({header:'序',width:30,renderer:function(value,metadata,record,rowIndex){return record.store.lastOptions.params.start + 1 + rowIndex;}});
	if(this.ischeck)cols[cols.length]=sm;
	if(this.exTemplate!=null){var t=this.exTemplate;cols[1]=new Ext.ux.grid.RowExpander({tpl : new Ext.Template(t)});plugins[plugins.length]=cols[1];}
	var cols=cols.concat(this.cols);
	var bbars=null;var disInfo=true;var toolbar=null;var buttombar = [];
	if(this.nopage=='disbar')disInfo=false;
	if(this.nopage!=true){
		if(this.buttombar)
			buttombar = this.buttombar;
		bbars=new Ext.PagingToolbar({pageSize:20,store:store,displayInfo:disInfo,displayMsg:'共{2}条数据，显示 {0} - {1}条',emptyMsg:'没有数据',prependButons:true,plugins:[new Ext.ux.ProgressBarPager()],items:buttombar});
	}
	if(this.toolbar)toolbar=this.toolbar;
	this.grid = new Ext.grid.EditorGridPanel({sm:sm,
        width:width,height:height,title:title,store:store,loadMask:true,stripeRows:true,trackMouseOver:true,plugins:plugins,columns:cols,
        viewConfig: {forceFit:true},bbar:bbars,tbar:toolbar
    });
};
FHD.ext.EditGrid.prototype.getStore=function(){return this.store;};
FHD.ext.EditGrid.prototype.getGrid=function(){return this.grid;};
/*************************树列表组件************************************/
FHD.ext.TreeGrid=function(cId,params,title,headers,dataurl,toolbar){
	var dataLoader=new Ext.tree.TreeLoader({dataUrl:dataurl});
	dataLoader.baseParams=params;
	var rootId='rootId';
	if(params.rootId)rootId=params.rootId;
	dataLoader.on('beforeload', function(dataLoader, node) {
		if(!dataLoader.baseParams.id){dataLoader.baseParams.id=rootId;}else{dataLoader.baseParams.id=node.attributes.id;}
		}, dataLoader);
	var root = new Ext.tree.AsyncTreeNode({rootId:'rootId'});
	this.tree = new Ext.ux.tree.TreeGrid({
		title: title,
		enableDD: true,
		autoScroll:true,
		loader : dataLoader,
		renderTo:cId,
		height:document.getElementById(cId).offsetHeight,
		width:Ext.get(cId).getWidth(),
		checkbox: true, 
		checkMode: 'multiple',
		stripeRows: true,
		obarCfg:{column: {width:3}},
		animate:true, 
		enableDD:true,
		rootVisible:false,
		columns:headers,
		tbar:toolbar
	});
	this.tree.on('load',function(node){
		node.eachChild(function(n){
			for(var i=0;i<headers.length;i++){
				if(n.attributes[headers[i].dataIndex]==null||n.attributes[headers[i].dataIndex]=='')n.attributes[headers[i].dataIndex]='&nbsp;';}
			n.attributes[headers[0].dataIndex]='<span>'+n.attributes[headers[0].dataIndex]+'</span>';
		});
	});
};
FHD.ext.TreeGrid.prototype.rowClick=function(handler){
	this.tree.on('click',function(node){
		handler(node);
	});
};
FHD.ext.TreeGrid.prototype.nodeClick=function(handler){
	var tree=this.tree;
	this.tree.on('click',function(node){
		if(event.srcElement.outerHTML.toLowerCase()==node.attributes[tree.columns[0].dataIndex].toLowerCase()
			||node.attributes[tree.columns[0].dataIndex].toLowerCase().indexOf(event.srcElement.outerHTML.toLowerCase())>-1)
			handler(node);
	});
};
/*************************树列表组件************************************/
FHD.ext.Tree=function(cId,rootText,rId,dataurl,disroot,checkModel){
	var ba={}
	if(checkModel && checkModel!='none')ba={uiProvider:Ext.ux.TreeCheckNodeUI}
	var dataLoader=new Ext.tree.TreeLoader({dataUrl:dataurl,
		baseAttrs:ba});
	var rootId='rootId';
	if(rId)rootId=rId;
	dataLoader.on('beforeload', function(dataLoader, node) {
		if(!dataLoader.baseParams.id){dataLoader.baseParams.id=rootId;}else{dataLoader.baseParams.id=node.attributes.id;}
		}, dataLoader);
	var root = new Ext.tree.AsyncTreeNode({id:rootId,text:rootText});
	this.tree = new Ext.tree.TreePanel({
		enableDD: true,
		checkModel:checkModel,
		loader : dataLoader,
		renderTo:cId,
		autoScroll:true,
		height:Ext.get(cId).getHeight(),
		width:Ext.get(cId).getWidth(),
		//checkbox: true, 
		enableDD:true,
		root:root,
		rootVisible:disroot
	});
	this.tree.expand();
};
/*************************************************************/
function selectRiskWindow(callback,params){
	if(params && params.indexOf('history=true') > -1){
		openWindow('风险案例选择',1000,600,contextPath+"/components/risk/jstreepage.do?callback="+callback+'&'+params);
	}else if(params && params.indexOf('riskClass=true')>-1){
		openWindow('风险选择',1000,600,contextPath+"/components/risk/jstreepage.do?callback="+callback+'&'+params);
	}else{
		openWindow('风险选择',1000,600,contextPath+"/components/risk/jstreepage.do?callback="+callback+'&'+params);
	}
}
function selectOrgWindow(callback,params){
	openWindow('组织选择',500,400,contextPath+"/components/org/jstreepage.do?callback="+callback+'&'+params);
}
function selectAssetWindow(callback,params){
	openWindow('资产选择',500,400,contextPath+"/components/asset/jstreepage.do?callback="+callback+'&'+params);
}
function selectEmpWindow(callback,params){
	openWindow('人员选择',606,400,contextPath+"/components/emp/jstreepage.do?callback="+callback+'&'+params);
}
function selectEmpWindowEx(callback,params){
	openWindow('人员选择',1000,500,contextPath+"/components/emp/jstreepageEx.do?callback="+callback+'&'+params);
	//openWindow('风险选择',1000,600,contextPath+"/components/risk/jstreepage.do?callback="+callback+'&'+params);
}
FHD.selectRoleWindow=function(callback,params){
	openWindow('角色选择',500,400,contextPath+"/components/role/jstreepage.do?callback="+callback+'&'+params);
};
FHD.selectViWindow=function(callback,params){
	openWindow('指标选择',500,400,contextPath+"/components/vi/vitreepage.do?callback="+callback+'&'+params);
};
FHD.selectFlowWindow=function(callback,params){
	openWindow('流程选择',500,400,contextPath+"/components/flow/flowtreepage.do?callback="+callback+'&'+params);
};
FHD.selectWarningWindow=function(callback,params){
	openWindow('预警方案选择',1000,600,contextPath+"/kpi/target/warningSelectorTree.do?callback="+callback+'&'+params);
};
function deleteRecorder(tag){
			if(document.getElementById(tag) == null){
				list.innerHTML="";
			}else{
				document.getElementsByTagName(tag).innerHTML = "";
			}
			
}
function NTip(tip,window){
	this.tip=tip;
	this.window=window;
}
NTip.prototype.hide=function(){
	this.tip.hide();
	if(this.window)this.window.getEl().unmask();
};
function opWait(msg,msgCls){
	var m='操作进行中,请稍候......';
	var mc='x-mask-loading';
	if(msg)m=msg;
	if(msgCls)mc=msgCls;
	var owindow = window.top.Ext.getCmp('o_window'+getWindowId());
	if(owindow)owindow.getEl().mask();
	var tip = new window.top.Ext.LoadMask(window.top.Ext.getBody(), {msg:m,removeMask:true,msgCls:mc});
	tip.show();
	return new NTip(tip,owindow);
}
function ajaxReq(url,params,callback,msg,msgCls){
		var tip = opWait(msg,msgCls);
		Ext.Ajax.request({
		    url:url,
		    timeout:3600000,
		    method:'post',
		    params:params,
		    success:function(req){tip.hide();callback(req.responseText);},
	    	failure:function(req){tip.hide();callback(req.responseText);}
		  });
	}

FHD.disabledInput=function(id){
	var el = document.getElementById(id).getElementsByTagName('input');
	for(var i=0;i<el.length;i++)
		el[i].disabled=true;
};

//查询风险事件详细信息
function showRisk(id){
	openWindowSimple('查看风险事件', contextPath + '/risk/identification/risk/showrisktabs.do?id='+id);
}


FHD.selectRiskWindow=selectRiskWindow;
FHD.selectOrgWindow=selectOrgWindow;
FHD.selectAssetWindow=selectAssetWindow;
FHD.selectEmpWindow=selectEmpWindow;
FHD.opWait=opWait;
FHD.ajaxReq=ajaxReq;
FHD.parentWindow=parentWindow;
FHD.openWindow=openWindow;
FHD.openWindowSimple=openWindowSimple;
FHD.closeWindow=closeWindow;

FHD.exportExcel=function(grid){
	var arr = "[";
	grid.store.each(function(r){
		var o = "{";
		grid.getColumnModel().getColumnsBy(function(c){
			if(c.dataIndex != ""){
				var v = r.get(c.dataIndex);
				if(c.type == "alert"){
					v = getStatusMsg(v);
				}
				o += (c.header + ":\"" + v + "\",");
			}
		});
		o = o.substring(0,o.length-1);
		o += "}";
		arr += (o + ",");
	});
	arr = arr.substring(0,arr.length-1);
	arr += "]";
	Ext.Ajax.request({  
		method:'post',   
        url: contextPath + "/gridExportXsl.do",  
        params:{datas:arr},  
        success:function(req){  
              window.location.href = contextPath + "/downloadXsl.do";
        }  
    });  
}

var LittleUrl = {
// public method for url encoding
encode : function (string) {
    return escape(this._utf8_encode(string));
},
// public method for url decoding
decode : function (string) {
    return this._utf8_decode(unescape(string));
},
// private method for UTF-8 encoding
_utf8_encode : function (string) {
    string = string.replace(/\r\n/g,"\n");
    var utftext = "";
        for (var n = 0; n < string.length; n++) {
 
            var c = string.charCodeAt(n);
 
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
 
        }
 
        return utftext;
    },
 
// private method for UTF-8 decoding
_utf8_decode : function (utftext) {
    var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
 
        while ( i < utftext.length ) {
 
            c = utftext.charCodeAt(i);
 
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            }
            else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            }
            else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}

