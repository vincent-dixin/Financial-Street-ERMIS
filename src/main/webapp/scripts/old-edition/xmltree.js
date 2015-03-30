	var HC = "color:#990000;border:1px solid #cccccc";
	var SC = "background-color:#ffffff;border:1px solid #cccccc;color:#000000;";
	var IO = null;
	
	var firstDefaultNode = "";
	
	function initTree(){
		var rootn = document.all.menuXML.documentElement;
		var sd = 0;
		document.onselectstart = function(){return false;}
		document.all.treeBox.appendChild(createTree(rootn,sd));
	}
			
	function createTree(thisn,sd){
		var nodeObj = document.createElement("span");
		var upobj = document.createElement("span");
		
		with(upobj){
			style.marginLeft = sd*10;
			className = thisn.hasChildNodes()?"hasItems":"Items";
			innerHTML = "<img src="+contextPath+"/images/expand.gif class=ec>" + thisn.getAttribute("text") +"";
			
			onmousedown = function(){
				if(event.button != 1) return;
				if(this.getAttribute("cn")){
					this.setAttribute("open",!this.getAttribute("open"));
					this.cn.style.display = this.getAttribute("open")?"inline":"none";
					this.all.tags("img")[0].src = this.getAttribute("open")?(contextPath+"/images/expand.gif"):(contextPath+"/images/contract.gif");
				}
				
				if( this.getAttribute("href") != null && this.getAttribute("href") != "" && this.getAttribute("href") != "null" ){
					if(IO){
						IO.runtimeStyle.cssText = "";
						IO.setAttribute("selected",false);
					}
					IO = this;
					this.setAttribute("selected",true);
					this.runtimeStyle.cssText = SC;
					
				} else {
					return;
				}
			}
			onmouseover = function(){
				if(this.getAttribute("selected"))return;
				this.runtimeStyle.cssText = HC;
			}
			onmouseout = function(){
				if(this.getAttribute("selected"))return;
				this.runtimeStyle.cssText = "";
			}
			oncontextmenu = contextMenuHandle;
			onclick = clickHandle;
		}
		
		if(thisn.getAttribute("treeId") != null){
			upobj.setAttribute("treeId",thisn.getAttribute("treeId"));
		}
		if(thisn.getAttribute("href") != null){
			upobj.setAttribute("href",thisn.getAttribute("href"));
		}
		if(thisn.getAttribute("target") != null){
			upobj.setAttribute("target",thisn.getAttribute("target"));
		}
	
		nodeObj.appendChild(upobj);
		nodeObj.insertAdjacentHTML("beforeEnd","<br>")
	
		if(thisn.hasChildNodes()){
			var i;
			var nodes = thisn.childNodes;
			var cn = document.createElement("span");
			upobj.setAttribute("cn",cn);
			if(thisn.getAttribute("open") != null){
				upobj.setAttribute("open",(thisn.getAttribute("open")=="true"));
				upobj.getAttribute("cn").style.display = upobj.getAttribute("open")?"inline":"none";
				if( !upobj.getAttribute("open"))upobj.all.tags("img")[0].src =contextPath+"/images/contract.gif";
			}
			
			for(i=0;i<nodes.length;cn.appendChild(createTree(nodes[i++],sd+1)));
			nodeObj.appendChild(cn);
		}else{
			//------------->>将缺省打开的节点上色
			if( firstDefaultNode == "" ){
				firstDefaultNode = "yes";
				
				if(upobj.getAttribute("cn")){
					upobj.setAttribute("open",!upobj.getAttribute("open"));
					upobj.cn.style.display = upobj.getAttribute("open")?"inline":"none";
					upobj.all.tags("img")[0].src = upobj.getAttribute("open")?(contextPath+"/images/expand.gif"):(contextPath+"/images/contract.gif");
				}
				if(IO){
					IO.runtimeStyle.cssText = "";
					IO.setAttribute("selected",false);
				}
				IO = upobj;
				upobj.setAttribute("selected",true);
				upobj.runtimeStyle.cssText = SC;
				
				//调用第一个叶子节点的行为
			    document.all.initmenu.target = "bodyFrame";
			    document.all.initmenu.href = padContextpath(upobj.getAttribute("href"));
			    //document.all.initmenu.click();
			}
			//<<--------------------------------
			upobj.all.tags("img")[0].src =contextPath+"/images/endnode.gif";
		}
		return nodeObj;
	}
	
	function clickHandle(){
		if( this.getAttribute("href") != null && this.getAttribute("href") != "" && this.getAttribute("href") != "null" ){
		    self.parent.parent.bodyFrame_all.bodyFrame.window.location.href = padContextpath(this.getAttribute("href"));
		}
	}
	
	function contextMenuHandle(){
		event.returnValue = false;
		var treeId = this.getAttribute("treeId");
		// your code here
	}
	
	function padContextpath(url){
	  var headexpr = new RegExp("^/");
	  var tailexpr = new RegExp("\\.jsp(\\?.*){0,1}$");
	  var root = contextPath;
	  if(root=="/") root="";
	  if(tailexpr.test(url)){
	    if(headexpr.test(url)){
	      return root+url;
	    }else{
	      return root+"/"+url;
	    }
	  }else{
	    return url;
	  }
	}