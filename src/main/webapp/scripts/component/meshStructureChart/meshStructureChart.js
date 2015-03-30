var meshStructureChartNum=0;
function meshStructureChart(chartMessage){
	var auto="auto";
	var leftChild="leftChild";
	var rightChild="rightChild";
	var topInterval=1;
	var leftInterval=50;
	var lineWidth=50;
	var chart=new init(chartMessage);
	return chart;
	function init(chartMessage){
		//附着对象
		this.render=null;
		this.model="show";
		if(chartMessage.model){
			this.model=chartMessage.model;
		}
		this.top=null;
		this.left=null;
		this.id=null;
		this.num=null;
		this.width=null;
		this.height=null;
		this.viewTop=0;
		this.viewLeft=0;
		this.data=null;
		try{
			if(chartMessage.lineWidth!=null){
				lineWidth=chartMessage.lineWidth;
			}
			if(chartMessage.topInterval!=null){
				topInterval=chartMessage.topInterval;
			}
		}catch (e) {}
		this.align=chartMessage.align;
		this.nodeProperty=chartMessage.nodeProperty;
		this.addLeftChildFun=chartMessage.addLeftChildFun;
		this.addRightChildFun=chartMessage.addRightChildFun;
		this.delFun=chartMessage.delFun;
		this.canLeftOpen=chartMessage.canLeftOpen;
		this.canRightOpen=chartMessage.canRightOpen;
		this.isLeftOpen=chartMessage.isLeftOpen;
		this.isRightOpen=chartMessage.isRightOpen;
		this.openLeftFun=chartMessage.openLeftFun;
		this.openRightFun=chartMessage.openRightFun;
		this.icon=chartMessage.icon;
		this.nodeClickFun=chartMessage.nodeClickFun;
		this.nodeNum=0;
		this.nodes=[];
		this.viewMessageDiv=jQuery("<div class='viewMessage'></div>");
		jQuery("body").append(this.viewMessageDiv);
		this.nodeCanRightMenu=false;
		this.nodeRightMenu=null;
		this.doNodeRightMenu=function(node){
			if(node.mainChar.nodeRightMenu){
				node.render.mouseup(function(event){
					if(event.button == 2){
						var node=jQuery(this).data("node");
						node.mainChar.nodeRightMenu.show(node,event.pageX-1,event.pageY-1);
					}
				});
				if(node.leftChilds&&node.leftChilds.length>0){
					var n=node.leftChilds.length;
					for (var i = 0; i < n; i++) {
						node.mainChar.doNodeRightMenu(node.leftChilds[i]);
					}
				}
				if(node.rightChilds&&node.rightChilds.length>0){
					var n=node.rightChilds.length;
					for (var i = 0; i < n; i++) {
						node.mainChar.doNodeRightMenu(node.rightChilds[i]);
					}
				}
			}
		}
		//展现宽度
		this.getViewWidth=function(){
			var v=0;
			for ( var i = 0; i < this.nodes.length; i++) {
				if(v<this.nodes[i].getViewWidth()){
					v=this.nodes[i].getViewWidth();
				}
			}
			return v;
		};
		this.getLeftChildsWidth=function(){
			var v=0;
			for ( var i = 0; i < this.nodes.length; i++) {
				if(v<this.nodes[i].getLeftChildsWidth()){
					v=this.nodes[i].getLeftChildsWidth();
				}
			}
			return v;
		};
		//展现高度
		this.getViewHeight=function(){
			var v=0;
			for ( var i = 0; i < this.nodes.length; i++) {
				v+=this.nodes[i].getViewHeight();
			}
			return v;
		};
		//设定展现上侧位置
		this.setViewTop=function (top){
			var offset=top-this.viewTop;
			if(offset!=0){
				this.viewTop=top;
				if(this.nodes){
					for ( var i = 0; i < this.nodes.length; i++) {
						this.nodes[i].setTop(this.nodes[i].top+offset);
					}
				}
			}
		};
		//设定展现左侧位置
		this.setViewLeft=function (left){
			var offset=left-this.viewLeft;
			if(offset!=0){
				this.viewLeft=left;
				if(this.nodes){
					for ( var i = 0; i < this.nodes.length; i++) {
						this.nodes[i].setLeft(this.nodes[i].viewLeft+offset);
					}
				}
			}
		};
		//设定对齐方式
		this.initViewSite=function(){
			if(this.height>this.getViewHeight()){
				this.setViewTop(this.height/2);
			}else{
				this.setViewTop(this.getViewHeight()/2);
			}
			if(this.align=='left'){
				this.setViewLeft(this.getLeftChildsWidth()+leftInterval);
			}else{
				if(this.render.width()/2<this.getLeftChildsWidth()+this.nodes[0].width/2+lineWidth+leftInterval){//多节点
					this.setViewLeft(this.getLeftChildsWidth()+leftInterval);
				}else{
					var viewLeft=this.render.width()/2;
					if(this.nodes&&this.nodes[0]){
						viewLeft-=this.nodes[0].width/2;
					}
					this.setViewLeft(viewLeft+leftInterval);
				}
			}
		};
		//创建节点
		this.cNode=function(data,parentRender){
			if(!parentRender){
				parentRender=this;
			}
			try{
				if(this.nodeProperty.text!=null&&eval("data."+this.nodeProperty.text)!=null){
					data.text=eval("data."+this.nodeProperty.text);
				}
				if(this.nodeProperty.isCanViewMessage!=null){
					data.isCanViewMessage=this.nodeProperty.isCanViewMessage;
				}
				if(this.nodeProperty.leftChilds!=null&&eval("data."+this.nodeProperty.leftChilds)!=null){
					data.leftChilds=eval("data."+this.nodeProperty.leftChilds);
				}
				if(this.nodeProperty.rightChilds!=null&&eval("data."+this.nodeProperty.rightChilds)!=null){
					data.rightChilds=eval("data."+this.nodeProperty.rightChilds);
				}
				if(this.nodeProperty.addLeftChild!=null){
					data.addLeftChild=this.nodeProperty.addLeftChild;
				}
				if(this.nodeProperty.addRightChild!=null){
					data.addRightChild=this.nodeProperty.addRightChild;
				}
				if(this.nodeProperty.canLeftOpen!=null){
					data.canLeftOpen=this.nodeProperty.canLeftOpen;
				}
				if(this.nodeProperty.canRightOpen!=null){
					data.canRightOpen=this.nodeProperty.canRightOpen;
				}
				if(parentRender.isLeftOpen!=null&&data.isLeftOpen==null){
					data.isLeftOpen=parentRender.isLeftOpen;
				}
				if(parentRender.isRightOpen!=null&&data.isRightOpen==null){
					data.isRightOpen=parentRender.isRightOpen;
				}
				if(parentRender.openLeftFun!=null){
					data.openLeftFun=parentRender.openLeftFun;
				}
				if(parentRender.openRightFun!=null){
					data.openRightFun=parentRender.openRightFun;
				}
				if(parentRender.icon!=null&&data.icon==null){
					data.icon=parentRender.icon;
				}
				if(parentRender.nodeClickFun!=null&&data.clickFun==null){
					data.clickFun=parentRender.nodeClickFun;
				}
				if(this.addLeftChildFun!=null){
					data.addLeftChildFun=this.addLeftChildFun;
				}
				if(this.addRightChildFun!=null){
					data.addRightChildFun=this.addRightChildFun;
				}
				if(this.nodeProperty.isCanDel!=null){
					data.isCanDel=this.nodeProperty.isCanDel;
				}
				if(this.delFun!=null){
					data.delFun=this.delFun;
				}
			}catch (e) {}
			var _node=new node(data,parentRender);
			if(_node.parent==null){
				this.nodes.push(_node);
			}else{
				if(_node.childType=="leftChild"){
					_node.parent.leftChilds.push(_node);
				}
				if(_node.childType=="rightChild"){
					_node.parent.rightChilds.push(_node);
				}
			}
			_node.reSetViewHeight();
			if(_node.parent!=null){
				//_node.parent.saveViewHeight();
			}
			try{
				if(data.leftChilds!=null){
					for ( var i = 0; i < data.leftChilds.length; i++) {
						data.leftChilds[i].parent=_node;
						data.leftChilds[i].childType="leftChild";
						var leftChild=this.cNode(data.leftChilds[i],parentRender);
					}
				}
			}catch (e) {}
			try{
				if(data.rightChilds!=null){
					for ( var i = 0; i < data.rightChilds.length; i++) {
						data.rightChilds[i].parent=_node;
						data.rightChilds[i].childType="rightChild";
						var rightChild=this.cNode(data.rightChilds[i],parentRender);
					}
				}
			}catch (e) {}
			_node.render.data("node",_node);
			_node.mainChar.initViewSite();
			_node.mainChar.doNodeRightMenu(_node);
			return _node;
		};
		//展现方法
		this.reLoad=function(data){
			if(data!=null){
				this.data=data;
			}
			//循环展现所有根节点
			if(this.data!=null){
				for ( var i = 0; i < data.length; i++) {
					var _node=this.cNode(data[i],null);
				}
			}
			this.initViewSite();
			this.render.data("chart",this);
		};
		//附着对象初始化
		try{
			var ele=chartMessage.render;
			if(typeof(ele)=="string"){
				ele=jQuery("#"+ele);
			}else if(!ele instanceof jQuery){
					ele=jQuery(ele);
			}
			if(ele[0]){
				this.render=ele;
			}
			this.render.addClass("meshStructureChart");
		}catch(err){}
		if(this.render==null){
			this.render=jQuery("<div class='meshStructureChart'><div>");
			jQuery(document.body).append(this.render);
		}
		this.left=this.render.offset().left;
		this.top=this.render.offset().top;
		//ID初始化
		this.num=meshStructureChartNum++;
		try{
			this.id=chartMessage.render.attr("id");
		}catch(err){}
		if(this.id==null){
			this.id="meshStructureChart"+this.num;
			this.render.attr("id",this.id);
		}
		//宽度初始化
		try{
			this.width=chartMessage.width;
		}catch(err){}
		if(this.width==null){
			this.width=auto;
		}
		if(this.width==auto){
			this.render.width("100%");
		}else{
			this.render.width(this.width);
		}
		this.width=this.render.width();
		//高度初始化
		try{
			this.height=chartMessage.height;
		}catch(err){}
		if(this.height==null){
			this.height=auto;
		}
		if(this.height==auto){
			this.render.height("100%");
		}else{
			this.render.height(this.height);
		}
		this.height=this.render.height();
		//数据初始化
		try{
			this.data=chartMessage.data;
		}catch(err){}
		if(chartMessage.nodeCanRightMenu){
			this.nodeCanRightMenu=chartMessage.nodeCanRightMenu;
		}
		if(this.nodeCanRightMenu){
			if(!chartMessage.nodeRightMenu){
				chartMessage.nodeRightMenu={};
			}
			if(chartMessage.nodeRightMenu.datas&&chartMessage.nodeRightMenu.datas.length>0){
				if(this.model!="show"){
					chartMessage.nodeRightMenu.datas.push({
						text:"添加左结点",
						img:"add",
						fun:function(node){
							node.mainChar.addLeftChildFun(node);
						}
					});
					chartMessage.nodeRightMenu.datas.push({
						text:"添加右结点",
						img:"add",
						fun:function(node){
							node.mainChar.addRightChildFun(node);
						}
					});
					chartMessage.nodeRightMenu.datas.push('-');
					chartMessage.nodeRightMenu.datas.push({
						text:"删除",
						img:"del",
						fun:function(node){
							node.mainChar.delFun(node);
						}
					});
				}
			}else{
				if(this.model!="show"){
					chartMessage.nodeRightMenu.datas=[
						{
							text:"添加左结点",
							type:"addLeftChildItem",
							fun:function(node){
								node.mainChar.addLeftChildFun(node);
							}
						},
						{
							text:"添加右结点",
							type:"addRightChildItem",
							fun:function(node){
								node.mainChar.addRightChildFun(node);
							}
						},
						"-",
						{
							text:"删除",
							type:"delItem",
							fun:function(node){
								node.mainChar.delFun(node);
							}
						}
					];
				}
			}
			if(chartMessage.nodeRightMenu.datas&&chartMessage.nodeRightMenu.datas.length>0){
				jQuery(this.render).bind("contextmenu",function(){    
					return false;
				});
				this.nodeRightMenu=new rightMenu(this,chartMessage.nodeRightMenu);
				this.render.mousedown(function(event){
					var nodeRightMenu=jQuery(this).data("chart").nodeRightMenu;
					if(!nodeRightMenu.mouseIsOver){
						nodeRightMenu.hide();
					}
					nodeRightMenu.mouseIsOver=false;
				});
			}
		}
		//调用展现
		this.reLoad(this.data);
	}
	//展现节点方法
	function node(node,mainChar){
		this.num=mainChar.num+"node"+mainChar.nodeNum++;
		this.data=node;
		this.text="null";
		this.width="200px";
		this.height="35px";
		this.oldViewHeight=0;
		this.viewWidth=this.width;
		this.viewHeight=this.height;
		this.render=jQuery("<div class='node'><table cellspacing='0'><tr class='topTr'><td class='leftTd'></td><td class='midTd'></td><td class='rightTd'></td></tr><tr class='midTr'><td class='leftTd'></td><td class='midTd'><div class='icon'></div><div class='text'><div></td><td class='rightTd'></td></tr><tr class='bottomTr'><td class='leftTd'></td><td class='midTd'></td><td class='rightTd'></tr></table><div>");
		this.iconDiv=this.render.find(".icon");
		this.textDiv=this.render.find(".text");
		this.addChildDivStr="<div class='addDiv'></div>";
		this.delDivStr="<div class='delDiv'></div>";
		this.top=0;
		this.left=leftInterval;
		this.viewTop=this.top-topInterval;
		this.viewLeft=this.left;
		this.mainChar=mainChar;
		this.parent=null;
		this.childType=null;
		this.line=null;
		this.leftChilds=[];
		this.rightChilds=[];
		this.addLeftChildFun=null;
		this.addRightChildFun=null;
		this.isLeftOpen=false;
		this.isRightOpen=false;
		this.openLeftDiv=null;
		this.openRightDiv=null;
		this.canLeftOpen=false;
		this.canRightOpen=false;
		this.iconImg=jQuery("<img class='icon'/>");
		this.icon=null;
		this.clickFun=null;
		this.isCanViewMessage=true
		this.isCanDel=true;
		this.setTop=function(top){
			var topOffset=top-this.top+this.height/2;
			this.top=top;
			this.viewTop=this.top-topInterval;
			this.render.css("top",this.top);
			if(this.line){
				this.line.show();
			}
			if(this.openLeftDiv){
				this.openLeftDiv.show();
			}
			if(this.openRightDiv){
				this.openRightDiv.show();
			}
			for ( var i = 0; i < this.leftChilds.length; i++) {
				this.leftChilds[i].setTop(this.leftChilds[i].top+topOffset-this.leftChilds[i].height/2);
			}
			for ( var i = 0; i < this.rightChilds.length; i++) {
				this.rightChilds[i].setTop(this.rightChilds[i].top+topOffset-this.rightChilds[i].height/2);
			}
		}
		this.setLeft=function(left){
			var leftOffset=left-this.left;
			this.left=left;
			this.viewLeft=this.left;
			this.render.css("left",this.left);
			if(this.line){
				this.line.show();
			}
			if(this.openLeftDiv){
				this.openLeftDiv.show();
			}
			if(this.openRightDiv){
				this.openRightDiv.show();
			}
			for ( var i = 0; i < this.leftChilds.length; i++) {
				this.leftChilds[i].setLeft(this.leftChilds[i].left+leftOffset);
			}
			for ( var i = 0; i < this.rightChilds.length; i++) {
				this.rightChilds[i].setLeft(this.rightChilds[i].left+leftOffset);
			}
		};
		this.getLeftChildsHeight=function(){
			var leftChildsHeight=0;
			for ( var i = 0; i < this.leftChilds.length; i++) {
				leftChildsHeight+=this.leftChilds[i].getViewHeight();
			}
			return leftChildsHeight;
		};
		this.getRightChildsHeight=function(){
			var rightChildsHeight=0;
			for ( var i = 0; i < this.rightChilds.length; i++) {
				rightChildsHeight+=this.rightChilds[i].getViewHeight();
			}
			return rightChildsHeight;
		};
		this.getViewHeight=function(){
			var v=this.viewHeight;
			if(v<this.getLeftChildsHeight()){
				v=this.getLeftChildsHeight();
			}
			if(v<this.getRightChildsHeight()){
				v=this.getRightChildsHeight();
			}
			return v;
		};
		this.getLeftChildsWidth=function(){
			var leftChildsWidth=0;
			for ( var i = 0; i < this.leftChilds.length; i++) {
				if(leftChildsWidth<this.leftChilds[i].getViewWidth()+lineWidth){
					leftChildsWidth=this.leftChilds[i].getViewWidth()+lineWidth;
				}
			}
			return leftChildsWidth;
		};
		this.getRightChildsWidth=function(){
			var rightChildsWidth=0;
			for ( var i = 0; i < this.rightChilds.length; i++) {
				if(rightChildsWidth<this.rightChilds[i].getViewWidth()){
					rightChildsWidth=this.rightChilds[i].getViewWidth()+lineWidth;
				}
			}
			return rightChildsWidth;
		};
		this.getViewWidth=function(){
			return this.width+this.getLeftChildsWidth()+this.getRightChildsWidth();
		};
		this.saveViewHeight=function(){
			this.oldViewHeight=this.getViewHeight();
			if(this.parent!=null){
				this.parent.saveViewHeight();
			}
		};
		this.reSetViewHeight=function(){
			var _node=this;
			var nowViewHeight=_node.getViewHeight();
			var offset=nowViewHeight-_node.oldViewHeight;
			if(offset!=0){
				if(_node.childType=="leftChild"){
					var nodes=_node.parent.leftChilds;
					if(nodes.length>1){
						for ( var i = 0; i < nodes.length; i++) {
							var o=nodes[i].top-offset/2;
							if(nodes[i].num==_node.num){
								if(i-1>=0){
									var top=nodes[i-1].top+nodes[i-1].height/2+nodes[i-1].getViewHeight()/2+(nowViewHeight-nodes[i].height)/2;
									nodes[i].setTop(top);
								}
								offset=-offset;
							}else{
								nodes[i].setTop(o);
							}
						}
					}
				}
				if(_node.childType=="rightChild"){
					var nodes=_node.parent.rightChilds;
					if(nodes.length>1){
						for ( var i = 0; i < nodes.length; i++) {
							var o=nodes[i].top-offset/2;
							if(nodes[i].num==_node.num){
								if(i-1>=0){
									var top=nodes[i-1].top+nodes[i-1].height/2+nodes[i-1].getViewHeight()/2+(nowViewHeight-nodes[i].height)/2;
									nodes[i].setTop(top);
								}
								offset=-offset;
							}else{
								nodes[i].setTop(o);
							}
						}
					}
				}
				if(_node.parent!=null){
					_node.parent.reSetViewHeight();
				}
				_node.oldViewHeight=_node.getViewHeight();
			}
		};
		//删除节点
		this.delNode=function(){
			var _node=this;
			//_node.saveViewHeight();
			try{
				if(_node.leftChilds!=null){
					for ( var i = 0; i < _node.leftChilds.length; i++) {
						_node.leftChilds[i].delNode();
					}
				}
			}catch (e) {}
			try{
				if(_node.rightChilds!=null){
					for ( var i = 0; i < _node.rightChilds.length; i++) {
						_node.rightChilds[i].delNode();
					}
				}
			}catch (e) {}
			_node.leftChilds=[];
			_node.rightChilds=[];
			_node.width=0;
			_node.viewWidth=0;
			_node.viewWidth=0;
			_node.viewHeight=0;
			_node.render.remove();
			_node.line.render.remove();
			_node.reSetViewHeight();
			if(_node.parent!=null){
				if(_node.childType=="leftChild"){
					for ( var i = 0; i < _node.parent.leftChilds.length; i++) {
						if(_node.parent.leftChilds[i].num==_node.num){
							_node.parent.leftChilds.splice(i);
						}
					}
				}else if(_node.childType=="rightChild"){
					for ( var i = 0; i < _node.parent.rightChilds.length; i++) {
						if(_node.parent.rightChilds[i].num==_node.num){
							_node.parent.rightChilds.splice(i);
						}
					}
				}
			}else{
				for ( var i = 0; i < _node.mainChar.nodes.length; i++) {
					if(_node.mainChar.nodes[i].num==_node.num){
						_node.mainChar.nodes.splice(i);
					}
				}
			}
			_node.mainChar.initViewSite();
			return _node;
		};
		try{
			this.mainChar.render.append(this.render);
			if(node.childType){
				this.childType=node.childType;
			}
			if(node.text){
				this.text=node.text;
			}
			if(node.icon){
				this.icon=node.icon;
			}
			if(node.clickFun){
				this.clickFun=node.clickFun;
			}
			if(node.width){
				this.width=node.width;
			}
			if(node.height){
				this.height=node.height;
			}
			this.render.width(this.width);
			this.width=this.render.width();
			this.textDiv.width(this.width-18);
			this.viewWidth=this.width+leftInterval*2;
			this.render.height(this.height);
			this.height=this.render.height();
			this.viewHeight=this.height+topInterval*2;
			if(this.icon){
				this.iconImg.attr("src",this.icon);
				this.iconDiv.append(this.iconImg);
				this.iconDiv.width(this.iconImg.width());
				this.textDiv.width(this.textDiv.width()-this.iconDiv.width()-3);
			}
			this.textDiv.append(this.text);
			if(this.clickFun!=null){
				this.textDiv.css("cursor","pointer");
				this.textDiv.click(function(){
					var _node=jQuery(this).parent().parent().parent().parent().parent().data("node");
					_node.clickFun(_node);
				})
			}
			if(node.isCanViewMessage!=null){
				this.isCanViewMessage=node.isCanViewMessage;
			}
			if(this.isCanViewMessage){
				this.textDiv.hover(function(event){
					var _node=jQuery(this).parent().parent().parent().parent().parent().data("node");
					_node.mainChar.viewMessageDiv.css("left",event.pageX);
					_node.mainChar.viewMessageDiv.css("top",event.pageY-1);
					_node.mainChar.viewMessageDiv.text(_node.text);
					_node.mainChar.viewMessageDiv.show();
				},function(){
					var _node=jQuery(this).parent().parent().parent().parent().parent().data("node");
					_node.mainChar.viewMessageDiv.hide();
				});
			}
			if(node.parent){
				this.parent=node.parent;
			}
			//设定位置高度
			this.top-=this.height/2;
			if(this.childType==leftChild){
				var nodes=this.parent.leftChilds;
				if(this.parent.leftChilds.length>0){
					this.top=this.parent.leftChilds[0].top;
				}else{
					this.top=this.parent.top;
				}
				this.top+=this.parent.getLeftChildsHeight();
				this.left=this.parent.left-lineWidth-this.width;
			}
			if(this.childType==rightChild){
				var nodes=this.parent.rightChilds;
				if(this.parent.rightChilds.length>0){
					this.top=this.parent.rightChilds[0].top;
				}else{
					this.top=this.parent.top;
				}
				this.top+=this.parent.getRightChildsHeight();
				this.left=this.parent.left+this.parent.width+lineWidth;
			}
			if(node.top){
				this.top=node.top;
			}
			this.viewTop=this.top-topInterval;
			this.setTop(this.top);
			if(node.left){
				this.left=node.left;
			}
			this.viewLeft=this.left;
			this.setLeft(this.left);
			//按钮定义
			if(this.childType!=rightChild){
				var isAddLeftChild=true;
				if(typeof(node.addLeftChild)=="function"){
					isAddLeftChild=node.addLeftChild(this);
				}else if(node.addLeftChild==false){
					isAddLeftChild=node.addLeftChild;
				}
				if(isAddLeftChild){
					this.addLeftChildDiv=jQuery(this.addChildDivStr);
					this.render.find("TABLE>TBODY>TR.midTr>TD.leftTd").append(this.addLeftChildDiv);
					this.addLeftChildFun=node.addLeftChildFun;
					this.addLeftChildDiv.click(function(){
						var _node=jQuery(this).parent().parent().parent().parent().parent().data("node");
						if(_node.data.addLeftChildFun){
							_node.data.addLeftChildFun(_node);
						}
					});
				}else if(this.addLeftChildDiv!=null){
					this.addLeftChildDiv.remove();
					this.addLeftChildDiv=null;
				}
			}
			if(this.childType!=leftChild){
				var isAddRightChild=true;
				if(typeof(node.addRightChild)=="function"){
					isAddRightChild=node.addRightChild(this);
				}else if(node.addRightChild==false){
					isAddRightChild=node.addRightChild;
				}
				if(isAddRightChild){
					this.addRightChildDiv=jQuery(this.addChildDivStr);
					this.render.find("TABLE>TBODY>TR.midTr>TD.rightTd").append(this.addRightChildDiv);
					this.addRightChildFun=node.addRightChildFun;
					this.addRightChildDiv.click(function(){
						var _node=jQuery(this).parent().parent().parent().parent().parent().data("node");
						if(_node.data.addRightChildFun){
							_node.data.addRightChildFun(_node);
						}
					});
				}else if(this.addRightChildDiv!=null){
					this.addRightChildDiv.remove();
					this.addRightChildDiv=null;
				}
			}
			if(this.childType!=null){
				var isCanDel=true;
				if(typeof(node.isCanDel)=="function"){
					isCanDel=node.isCanDel();
				}else if(node.isCanDel==false){
					isCanDel=node.isCanDel;
				}
				if(isCanDel){
					
					this.delDiv=jQuery(this.delDivStr);
					this.render.find("TABLE>TBODY>TR.topTr>TD.rightTd").append(this.delDiv);
					this.delFun=node.delFun;
					this.delDiv.click(function(){
						var _node=jQuery(this).parent().parent().parent().parent().parent().data("node");
						if(_node.data.delFun){
							_node.data.delFun(_node);
						}
					});
				}else if(this.delDiv!=null){
					this.delDiv.remove();
					this.delDiv=null;
				}
			}
			if(this.childType!=null){
				this.line=new line(this.parent,this,mainChar);
			}
			if(this.data.canLeftOpen!=null){
				this.canLeftOpen=this.data.canLeftOpen;
			}
			if(this.data.canRightOpen!=null){
				this.canRightOpen=this.data.canRightOpen;
			}
			if(this.data.isLeftOpen!=null){
				this.isLeftOpen=this.data.isLeftOpen;
			}
			if(this.data.isRightOpen!=null){
				this.isRightOpen=this.data.isRightOpen;
			}
			if(this.canLeftOpen&&this.childType!=rightChild){
				this.openLeftDiv=new openDiv(this,"leftOpenDiv");
				this.openLeftDiv.show();
			}
			if(this.canRightOpen&&this.childType!=leftChild){
				this.openRightDiv=new openDiv(this,"rightOpenDiv");
				this.openRightDiv.show();
			}
		}catch(err){}
	}
	//展开按钮
	function openDiv(node,openDivType){
		this.node=node;
		this.render=jQuery("<div></div>");
		this.top=0;
		this.left=0;
		this.openDivType=openDivType;
		this.openFun=function(openDiv){
		};
		this.closeFun=function(openDiv){
		};
		this.render.click(function(){
			var openDiv=jQuery(this).data("openDiv");
			if(openDiv.openDivType=="leftOpenDiv"){
				if(openDiv.node.isLeftOpen){
					openDiv.closeFun(openDiv);
					openDiv.node.isLeftOpen=false;
					openDiv.render.attr("class","openDiv");
					for ( var i = 0; i < openDiv.node.leftChilds.length; i++) {
						openDiv.node.leftChilds[i].delNode();
					}
				}else{
					openDiv.openFun(openDiv);
					openDiv.node.isLeftOpen=true;
					openDiv.render.attr("class","closeDiv");
				}
			}else if(openDiv.openDivType=="rightOpenDiv"){
				if(openDiv.node.isLeftOpen){
					openDiv.closeFun(openDiv);
					openDiv.node.isLeftOpen=false;
					openDiv.render.attr("class","openDiv");
					for ( var i = 0; i < openDiv.node.rightChilds.length; i++) {
						openDiv.node.rightChilds[i].delNode();
					}
				}else{
					openDiv.openFun(openDiv);
					openDiv.node.isLeftOpen=true;
					openDiv.render.attr("class","closeDiv");
				}
			}
			
		});
		this.show=function(){
			if(this.openDivType=="leftOpenDiv"){
				this.top=this.node.render.height()/2-this.render.height()/2;
				this.left=-this.render.width()/2;
			}else if(this.openDivType=="rightOpenDiv"){
				this.top=this.node.render.height()/2-this.render.height()/2;
				this.left=this.node.render.width()-this.render.width()/2;
			}
			this.render.css("top",this.top);
			this.render.css("left",this.left);
		};
		try{
			this.node.render.append(this.render);
			if(this.openDivType=="leftOpenDiv"){
				if(this.node.data.openLeftFun){
					this.openFun=this.node.data.openLeftFun;
				}
				if(this.node.isLeftOpen){
					this.render.attr("class","closeDiv");
				}else{
					this.render.attr("class","openDiv");
				}
			}else if(this.openDivType=="rightOpenDiv"){
				if(this.node.data.closeRightFun){
					this.closeFun=this.node.data.closeRightFun;
				}
				if(this.node.isRightOpen){
					this.render.attr("class","closeDiv");
				}else{
					this.render.attr("class","openDiv");
				}
			}
			this.render.data("openDiv",this);
		}catch(err){}
	}
	//连线
	function line(parentNode,childNode,mainChar){
		this.parentNode=parentNode;
		this.childNode=childNode;
		this.render=jQuery("<div class='line'><div class='leftLine1'></div><div class='rightLine1'></div></div>");
		this.lineType=false;
		this.getTop=function(){
			return this.top;
		};
		this.setTop=function(top){
			var topOffset=top-this.top;
			this.top=top;
			this.render.css("top",top);
		};
		this.getLeft=function(){
			return this.left;
		};
		this.setLeft=function(left){
			var leftOffset=left-this.left;
			this.left=left;
			this.render.css("left",left);
		};
		this.show=function(){
			var parentNodeHigh=true;
			var parentNodeSide=true;
			if(this.parentNode.left>this.childNode.left){
				this.width=this.parentNode.left-this.childNode.left-this.childNode.width;
				this.left=this.childNode.left+this.childNode.width;
			}else{
				this.width=this.childNode.left-this.parentNode.left-this.parentNode.width;
				this.left=this.parentNode.left+this.parentNode.width;
				parentNodeHigh=false;
			}
			if(this.parentNode.top+this.parentNode.height/2>this.childNode.top+this.childNode.height/2){
				this.height=(this.parentNode.top+this.parentNode.height/2)-(this.childNode.top+this.childNode.height/2);
				this.top=this.childNode.top+this.childNode.height/2;
			}else{
				this.height=(this.childNode.top+this.childNode.height/2)-(this.parentNode.top+this.parentNode.height/2);
				this.top=this.parentNode.top+this.parentNode.height/2;
				parentNodeSide=false;
			}
			
			this.height+=1;
			if(this.height==1){
				this.render.find(".leftLine1").removeClass("leftLine1").addClass("leftLine");
				this.render.find(".rightLine1").removeClass("rightLine1").addClass("rightLine");
				this.render.find(".leftLine2").removeClass("leftLine2").addClass("leftLine");
				this.render.find(".rightLine2").removeClass("rightLine2").addClass("rightLine");
			}else if(parentNodeHigh==parentNodeSide){
				this.render.find(".leftLine1").removeClass("leftLine1").addClass("leftLine2");
				this.render.find(".rightLine1").removeClass("rightLine1").addClass("rightLine2");
				this.render.find(".leftLine").removeClass("leftLine").addClass("leftLine2");
				this.render.find(".rightLine").removeClass("rightLine").addClass("rightLine2");
			}else if(parentNodeHigh!=parentNodeSide){
				this.render.find(".leftLine2").removeClass("leftLine2").addClass("leftLine1");
				this.render.find(".rightLine2").removeClass("rightLine2").addClass("rightLine1");
				this.render.find(".leftLine").removeClass("leftLine").addClass("leftLine1");
				this.render.find(".rightLine").removeClass("rightLine").addClass("rightLine1");
			}
			this.setTop(this.top);
			this.setLeft(this.left);
			this.render.width(this.width);
			this.render.height(this.height);
			
		};
		mainChar.render.append(this.render);
		this.show();
	}
	/*右键菜单*/
	function rightMenu(mainChar,config){
		this.mainChar=mainChar;
		this.render=jQuery("<div></div>");
		this.render.addClass("rightMenu");
		this.datas=config.datas;
		this.node=null;
		this.mouseIsOver=false;
		this.delItem=null;
		this.addLeftChildItem=null;
		this.addRightChildItem=null;
		if(this.datas&&this.datas.length>0){
			var n=this.datas.length;
			jQuery(this.render).bind("contextmenu",function(){    
				return false;
			});
			for (var i = 0; i < n; i++) {
				var item=null;
				if(this.datas[i]=="-"){
					item=jQuery("<hr/>");
				}else{
					var img=this.datas[i].img;
					var text=this.datas[i].text;
					var type=this.datas[i].type;
					item=jQuery("<div></div>");
					item.addClass("item");
					item.hover( function() {
						var item = jQuery(this);
						item.addClass("itemHover");
					}, function() {
						var item = jQuery(this);
						item.removeClass("itemHover");
					});
					var imgRender=jQuery("<div></div>");
					imgRender.addClass("img");
					if(type=="addLeftChildItem"){
						imgRender.addClass("add");
						this.addLeftChildItem=item;
					}else if(type=="addRightChildItem"){
						imgRender.addClass("add");
						this.addRightChildItem=item;
					}else if(type=="delItem"){
						imgRender.addClass("del");
						this.delItem=item;
					}else{
						imgRender.css("background-image",img);
					}
					item.append(imgRender);
					var textRender=jQuery("<div>"+text+"</div>");
					textRender.addClass("text");
					item.append(textRender);
					item.data("fun",this.datas[i].fun);
					item.click(function(){
						if(!jQuery(this).hasClass("itemDisabled")){
							jQuery(this).data("fun")(jQuery(this).parent().data("node"));
							jQuery(this).parent().hide();
						}
					});
				}
				this.render.append(item);
			}
		}
		this.show=function(node,x,y){
			this.node=node;
			this.delItem.removeClass("itemDisabled");
			this.addRightChildItem.removeClass("itemDisabled");
			this.addLeftChildItem.removeClass("itemDisabled");
			if(this.node.childType==null){
				this.delItem.addClass("itemDisabled");
			}else if(this.node.childType=="leftChild"){
				this.addRightChildItem.addClass("itemDisabled");
			}else if(this.node.childType=="rightChild"){
				this.addLeftChildItem.addClass("itemDisabled");
			}
			this.render.data("node",node);
			this.render.css("left",x);
			this.render.css("top",y);
			this.render.show("normal");
		}
		this.hide=function(){
			this.render.hide();
		}
		jQuery(document.body).append(this.render);
		this.render.data("rightMenu",this);
		this.render.mousedown( function() {
			jQuery(this).data("rightMenu").mouseIsOver=true;
		});
	}
}