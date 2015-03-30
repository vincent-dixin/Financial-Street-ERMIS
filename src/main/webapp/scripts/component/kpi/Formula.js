
Ext.define('FHD.ux.kpi.Formula',{
	/**
	 * 解析Tokanizer需要变量
	 */
	lstAlpha: "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,$kpi,$category,$strategy,$A,$S,$评估值",
	lstDigits: "0,1,2,3,4,5,6,7,8,9",
	lstOperates: "^,*,/,%,+,-",
	lstLogicOps: "!,&&,||",
	lstCompaOps: "<,<=,>,>=,<>,==",
	lstFuncOps: ["AVERAGE","ABS","COS","COUNT","EXP","IF","INT","ISKPISTATUS","ISNOVALUE","ISSCSTATUS","KPISTATUSLEVEL","LOG","LOG10",
	             "MAX","MIN","MOD","PRIOR","SIN","SQRT","STANDARDDEVIATION","SUB","SUBSUMKPISTATUS",
	             "SUBSUMSCSTATUS","SUM","TAN","VARIANCE"],
	/**
	 * 验证公式
	 * @params formula 公式字符串
	 */
	replaceall:function(oldstring,oldsign,newsign){
		while(oldstring.indexOf(oldsign)!=-1){
			oldstring=oldstring.replace(oldsign,newsign);
		}
		return oldstring;
	},
	validateFormula:function (formula){
		
		var me = this;
		/*
		 * 1.替换所有标签和特殊字符
		 */
		if(formula=="<p>\n\t&nbsp;\n</p>"){
			me.editor.html("");
			return true;
		}
		formula = this.replaceall(formula,"&nbsp;","");
		//去掉所有的html标签
		formula = formula.replace(/<[^>].*?>/g,"");
		//把"&gt;"替换成">"、"&lt;"替换成"<"
		formula = this.replaceall(formula,"&lt;","<");
		formula = this.replaceall(formula,"&gt;",">");
		formula = this.replaceall(formula,"&amp;","&");
		var flag = false;
		if(formula != ''){
			//2.验证括号匹配
			if(this.isBalanced(formula)){
				//3.拆分公式成Tokanizer
				var intCntr;
			    var arrTokens = this.Tokanize(formula);
			    var strTemp = "";
			    var strTmp = "";
			    if (arrTokens.length > 0) {
			    	//4.验证函数参数个数验证函数参数个数
					if(!this.validateFunctionParams(arrTokens)){
						alert("验证函数参数个数不正确");
						return false;
					}
			        for (intCntr = 0; intCntr < arrTokens.length; intCntr++){
			            strTemp += arrTokens[intCntr];
			            //alert("Token " + intCntr + " : " + arrTokens[intCntr]);
			            /*
			             * 5.循环遍历验证每个Token是否符合规则正确
			             * 每次循环取当前token和下一个token进行验证，最后一个token单独验证
			             */
			            if(intCntr != arrTokens.length-1){
			            	if(!this.validateToken(arrTokens[intCntr],arrTokens[intCntr+1])){
			            		flag = true;
			            		//标红当前Token到kindeditor中进行错误提示
			            		me.editor.html(this.showErrorFormula(arrTokens,intCntr));
			            		return false;
			            		//break;
			            	}
			            }else{
			            	//验证最后一个token
			            	if(!this.validateToken(arrTokens[intCntr],"last")){
			            		flag = true;
			            		//标红当前Token到kindeditor中进行错误提示
			            		me.editor.html(this.showErrorFormula(arrTokens,intCntr));
			            		return false;
			            		//break;
			            	}
			            }
			        }
			        /*
			         * 6.替换计算值：替换所有变量为随机数 且保证除数后面的变量结果值不能为0，替换数学函数为可计算函数，替换自定义函数的值
			         * 若返回值，则验证成功，否则验证失败
			         */
			        
			    }
			    me.editor.html('<span style="background-color:#A2EEA2;">'+strTemp+'</span>');
				return true;
			}else{
				alert('公式括号不平衡!');
				return false;
			}
		}else{
			me.editor.html("");
		}
		return true;
	},
	/**
	 * 验证函数参数个数是否正确
	 * @param arrTokens 
	 */
	validateFunctionParams:function (arrTokens){
		var me = this;
		//alert('验证函数参数个数是否正确='+arrTokens);
		for(var intCntr = 0; intCntr < arrTokens.length; intCntr++){
			//alert('arrTokens[intCntr]='+arrTokens[intCntr]+"\t"+"arrTokens[intCntr+1]="+arrTokens[intCntr+1]);
			if(this.IsFunction(arrTokens[intCntr]) && arrTokens[intCntr+1]=="("){
				//如果当前tokens是函数，那么把arrTokens中该函数的内容取出来重新拼成字符串进行拆分
				var str=this.stitchingFunctionContent(arrTokens,intCntr);
				//alert('str='+str);
				var paramArray = new Array();
				paramArray = this.spliteFun(str, ",");
				var length = paramArray.length;
				if("if"==arrTokens[intCntr] && 3!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
	        		me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("sub"==arrTokens[intCntr] && 4!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
	        		me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("mod"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("int"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("isNoValue"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("sin"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("cos"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("tan"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("abs"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("exp"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("log"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("log10"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("sqrt"==arrTokens[intCntr] && 1!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("max"==arrTokens[intCntr] && paramArray.length>0){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("min"==arrTokens[intCntr] && paramArray.length>0){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("count"==arrTokens[intCntr] && paramArray.length>0){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("sum"==arrTokens[intCntr] && paramArray.length>0){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("average"==arrTokens[intCntr] && paramArray.length>0){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("prior"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("kpiStatusLevel"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("iskpistatus"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("isscstatus"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("subsumkpistatus"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}else if("subsumscstatus"==arrTokens[intCntr] && 2!=paramArray.length){
					//标红当前Token到kindeditor中进行错误提示
					me.editor.html(this.showErrorFormula(arrTokens,intCntr));
					return false;
				}
			}
	    }
		
		return true;
	},
	/**
	 * 拼接kinkeditor公式中函数内容
	 * @param arrTokens
	 * @param position
	 */
	stitchingFunctionContent:function (arrTokens,position){
		var strTemp = "";
		var brack=0;
		var intCntr=position+1;
	    if (arrTokens.length > 0) {
	        for (;intCntr < arrTokens.length; intCntr++){
	        	//alert('arrTokens[intCntr]='+arrTokens[intCntr]);
	        	if(arrTokens[intCntr] == "("){
	        		if(brack>0){
	        			strTemp += arrTokens[intCntr];
	        		}
	        		brack++;
	        	}else if(arrTokens[intCntr] == ")"){
	        		if(brack>0){
	        			strTemp += arrTokens[intCntr];
	        		}
	        		brack--;
	        	}else if(brack==-1){
	        		break;
	        	}else{
	        		strTemp += arrTokens[intCntr];
	        	}
	        }
	    }
		return strTemp;
	},
	/**
	 * 根据函数名把函数括号内的参数拆分
	 * @params str 函数括号内的参数
	 * @params bs 函数名
	 * @return array
	 */
	spliteFun:function (str, bs) {
		var paramArray = new Array();
		var intIndex=0;
		var s;
		var bds = "";
		var bracket = 0;
		var len = str.length;
		for (var i = 0; i < len; i++) {
			s = str.charAt(i)
			if ("("==s) {
				bracket++;
			} else if (")"==s) {
				bracket--;
			}

			if (bracket==0 && bs==s) {
				paramArray[intIndex] = bds;
				intIndex++;
				bds = "";
				continue;
			}
			
			bds += s;
		}
		paramArray[intIndex] = bds;
		
		return paramArray;
	},
	/**
	 * 解析Tokanizer
	 * @params pstrExpression 
	 * @return array
	 */
	Tokanize:function (pstrExpression){
	   var intCntr, intBraces;
	   var arrTokens;
	   var intIndex, intPos;
	   var chrChar, chrNext;
	   var strToken, prevToken;

	   intCntr   = 0;
	   intBraces = 0;
	   intIndex  = 0;
	   strToken  = "";
	   arrTokens = new Array();
	   pstrExpression = this.Trim(pstrExpression);
	   while (intCntr < pstrExpression.length){
	       prevToken = "";
	       chrChar = pstrExpression.substr(intCntr, 1);
	       if (window)
	           if (window.status)
	               window.status = "Processing " + chrChar;
	       switch (chrChar){
	           case " " :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               break;
	           case "(":
	               intBraces++;
	               if (strToken.length > 0){
	            	   if(strToken.indexOf("#[") == -1 || this.isVariable(strToken)){
	            		   arrTokens[intIndex] = strToken;
		                   intIndex++;
		                   strToken = "";
		                   
		                   arrTokens[intIndex] = chrChar;
			               intIndex++;
	            	   }else{
	            		   if('' == chrChar){
			        		   strToken += " ";
			        	   }else{
			        		   strToken += chrChar;
			        	   }
	            	   }
	               }else{
	            	   arrTokens[intIndex] = chrChar;
		               intIndex++;
	               }
	               
	               break;
	           case ")" :
	               intBraces--;
	               if (strToken.length > 0){
	            	   if(strToken.indexOf("#[") == -1 || this.isVariable(strToken)){
	            		   arrTokens[intIndex] = strToken;
		                   intIndex++;
		                   strToken = "";
		                   
		                   arrTokens[intIndex] = chrChar;
			               intIndex++;
	            	   }else{
	            		   if('' == chrChar){
			        		   strToken += " ";
			        	   }else{
			        		   strToken += chrChar;
			        	   }
	            	   }
	               }else{
	            	   arrTokens[intIndex] = chrChar;
		               intIndex++;
	               }
	               
	               break;
	           case "^" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               arrTokens[intIndex] = chrChar;
	               intIndex++;
	               break;
	           case "*" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               arrTokens[intIndex] = chrChar;
	               intIndex++;
	               break;
	           case "/" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               arrTokens[intIndex] = chrChar;
	               intIndex++;
	               break;
	           case "%" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               arrTokens[intIndex] = chrChar;
	               intIndex++;
	               break;
	           case "&" :
	               chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               if (chrNext == "&"){
	                   arrTokens[intIndex] = chrChar + "&";
	                   intIndex++;
	                   intCntr++;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	               }
	               break;
	           case "|" :
	               chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               if (chrNext == "|"){
	                   arrTokens[intIndex] = chrChar + "|";
	                   intIndex++;
	                   intCntr++;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	               }
	               break;
	           case "," :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               arrTokens[intIndex] = chrChar;
	               intIndex++;
	               break;
	           case "-" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (arrTokens.length > 0)
	                   prevToken = arrTokens[intIndex - 1];
	               if (intCntr == 0 || ((this.IsOperator(prevToken) ||
	                   prevToken == "(" || prevToken == ",") && 
	                   (this.IsDigit(chrNext) || chrNext == "("))){
	                   // Negative Number
	                   strToken += chrChar;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	                   strToken = "";
	               }
	               break;
	           case "+" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (arrTokens.length > 0)
	                   prevToken = arrTokens[intIndex - 1];
	               if (intCntr == 0 || ((this.IsOperator(prevToken) ||
	                   prevToken == "(" || prevToken == ",") && 
	                   (this.IsDigit(chrNext) || chrNext == "("))){
	                   // positive Number
	                   strToken += chrChar;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	                   strToken = "";
	               }
	               break;
	           case "<" :
	               chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               if (chrNext == "="){
	                   arrTokens[intIndex] = chrChar + "=";
	                   intIndex++;
	                   intCntr++;
	               }else if (chrNext == ">"){
	                   arrTokens[intIndex] = chrChar + ">";
	                   intIndex++;
	                   intCntr++;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	               }
	               break;
	           case ">" :
	               chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               if (chrNext == "="){
	                   arrTokens[intIndex] = chrChar + "=";
	                   intIndex++;
	                   intCntr++;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	               }
	               break;
	           case "=" :
	        	   chrNext = pstrExpression.substr(intCntr + 1, 1);
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }
	               if (chrNext == "="){
	                   arrTokens[intIndex] = chrChar + "=";
	                   intIndex++;
	                   intCntr++;
	               }else{
	                   arrTokens[intIndex] = chrChar;
	                   intIndex++;
	               }
	               break;
	           case "'" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }

	               intPos = pstrExpression.indexOf(chrChar, intCntr + 1);
	               if (intPos < 0) 
	                   throw "Unterminated string constant";
	               else{
	                   strToken += pstrExpression.substring(intCntr + 1, intPos);
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	                   intCntr = intPos;
	               }
	               break;
	           case "\"" :
	               if (strToken.length > 0){
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	               }

	               intPos = pstrExpression.indexOf(chrChar, intCntr + 1);
	               if (intPos < 0){
	                   throw "Unterminated string constant";
	               }else{
	                   strToken += pstrExpression.substring(intCntr + 1, intPos);
	                   arrTokens[intIndex] = strToken;
	                   intIndex++;
	                   strToken = "";
	                   intCntr = intPos;
	               }
	               break;
	           default :
	        	   if('' == chrChar){
	        		   strToken += " ";
	        	   }else{
	        		   strToken += chrChar;
	        	   }
	               break;
	       }
	       intCntr++;
	   }
	   if (intBraces > 0)
	       throw "Unbalanced parenthesis!";

	   if (strToken.length > 0)
	       arrTokens[intIndex] = strToken;
	   return arrTokens;
	},
	/**
	 * 验证是否是数字
	 * @params chrArg
	 * @return boolean 
	 */
	IsDigit:function (chrArg){
		var me = this;
	    if (me.lstDigits.indexOf(chrArg) >= 0)
	        return true;
	    return false;
	},
	/**
	 * 验证是否是字符
	 * @params chrArg
	 * @return boolean 
	 */
	IsAlpha:function (chrArg){
	    if (me.lstAlpha.indexOf(chrArg) >= 0 || me.lstAlpha.toUpperCase().indexOf(chrArg) >= 0)
	        return true;
	    return false;
	},
	/**
	 * 验证是否是运算符
	 * @params chrArg
	 * @return boolean 
	 */
	IsOperator:function (strArg){
		var me = this;
	    if (me.lstOperates.indexOf(strArg) >= 0 || me.lstCompaOps.indexOf(strArg) >= 0 || me.lstLogicOps.indexOf(strArg) >= 0)
	        return true;
	    return false;
	},
	/**
	 * 去掉空格
	 * @params pstrVal
	 * @return pstrVal
	 */
	Trim:function (pstrVal){
	    if (pstrVal.length < 1) 
	    	return "";

	    pstrVal = this.RTrim(pstrVal);
	    pstrVal = this.LTrim(pstrVal);
	    if (pstrVal == "")
			return "";
	    else
	        return pstrVal;
	},
	/**
	 * 去掉右侧空格
	 * @params pstrValue
	 * @return pstrValue
	 */
	RTrim:function (pstrValue){
	    var w_space = String.fromCharCode(32);
	    var v_length = pstrValue.length;
	    var strTemp = "";
	    if(v_length < 0){
	        return"";
	    }
	    var iTemp = v_length - 1;

	    while(iTemp > -1){
	        if(pstrValue.charAt(iTemp) == w_space){
	        	
	        }else{
	            strTemp = pstrValue.substring(0, iTemp + 1);
	            break;
	        }
	        iTemp = iTemp - 1;
	    }
	    return strTemp;
	},
	/**
	 * 去掉左侧空格
	 * @params pstrValue
	 * @return pstrValue
	 */
	LTrim:function (pstrValue){
	    var w_space = String.fromCharCode(32);
	    if(v_length < 1){
	        return "";
	    }
	    var v_length = pstrValue.length;
	    var strTemp = "";
	    var iTemp = 0;

	    while(iTemp < v_length){
	        if(pstrValue.charAt(iTemp) == w_space){
	        	
	        }else{
	            strTemp = pstrValue.substring(iTemp, v_length);
	            break;
	        }
	        iTemp = iTemp + 1;
	    }
	    return strTemp;
	},
	/**
	 * 验证每个token是否符合规则正确
	 * @param token2
	 * @param token3
	 * @return boolean
	 */
	validateToken:function (token2,token3){
		if(token3 == "last"){
			if(this.isVariable(token2) || token2==")"){
				return true;
			}else{
				return false;
			}
		}else{
			//alert('验证='+token2+'\t'+token3);
			//1:变量验证
			if(this.isVariable(token2)){
				//alert("变量验证.....");
				if(!this.validateVariable(token2,token3)){
					alert("变量验证未通过.....");
					return false;
				}
			}else if(this.IsFunction(token2)){
				//alert("函数验证.....");
				//2:函数验证(包括数学函数[SIN,COS,TAN,ABS,EXP,INT,LOG,LOG10,SQRT]、自定义函数[IF,MOD,SUB]和统计函数)
				if(!this.validateFunction(token2,token3)){
					alert("函数验证未通过.....");
					return false;
				}
			}else if(token2=="(" || token2==")"){
				//alert("括号验证.....");
				//3:括号验证
				if(!this.validateParenthesis(token2,token3)){
					alert("括号验证未通过.....");
					return false;
				}
			}else{
				//alert("运算符验证.....");
				//4:运算符验证(包括+、-、*、/、^、%、>、>=、<、<=、==、<>)
				if(!this.validateOperator(token2,token3)){
					alert("运算符验证未通过.....");
					return false;
				}
			}
		}
		return true;
	},
	/**
	 * 验证变量
	 * @param token2
	 * @param token3
	 * @return boolean
	 */
	validateVariable:function (token2,token3){
		if(this.isVariable(token2)){
			if(token3=="&&" || token3=="||" || token3==")" || this.IsOperator(token3) || token3==","){
				return true;
			}
		}
		return false;
	},
	/**
	 * 验证是否是函数
	 * @params strArg
	 * @return boolean 
	 */
	IsFunction:function (strArg){
		var me = this;
		var idx = 0;
		strArg = strArg.toUpperCase();
		for (idx = 0; idx < me.lstFuncOps.length; idx++){
		    if (strArg == me.lstFuncOps[idx]){
		        return true;
		    }
		}
		return false;
	},
	/**
	 * 验证操作符
	 * @param token2
	 * @param token3
	 * @return boolean
	 */
	validateOperator:function (token2,token3){
		if(token2 == "+" || token2 == "-" || token2 == "*" || token2 == "^"){
			if(this.isVariable(token3) || token3 == "(" || this.IsFunction(token3)){
				return true;
			}
		}else if(token2 == "%"){
			if(token3 == "+" || token3 == "-" || token3 == "*" || token3 == "^" || token3 == "/"){
				return true;
			}
		}else if(token2 == '/'){
			//除法"/",除数不能为0
			if(token3 == '0'){
				return false;
			}
			if(this.isVariable(token3) || token3 == "(" || this.IsFunction(token3)){
				return true;
			}
		}else if(token2 == ","){
			if(this.isVariable(token3) || token3 == "(" || this.IsFunction(token3)){
				return true;
			}
		}else if(token2 == ">" || token2 == "<" || token2 == ">=" || token2 == "<=" || token2 == "==" || token2 == "!=" || token2 == "<>" || token2 == "&&" || token2 == "||"){
			if(this.isVariable(token3) || token3 == "(" || this.IsFunction(token3)){
				return true;
			}
		}
		return false;
	},
	/**
	 * 验证函数
	 * @param token2
	 * @param token3
	 * @return Boolean
	 */
	validateFunction:function (token2,token3){
		if(this.IsFunction(token2)){
			if(token3 != "("){
				return false;
			}
		}
		return true;
	},
	/**
	 * 验证括号
	 * @param token2
	 * @param token3
	 * @return boolean
	 */
	validateParenthesis:function (token2,token3){
		/*
		 * 由于公式中只允许出现"("和")"小括号做运算规则，只验证小括号"("和")"
		 * "["和"]"用于变量
		 * "{"和"}"未使用
		 */
		//alert('token2='+token2+"\t"+'token3='+token3);
		if("(" == token2){
			if(token3=="(" || this.isVariable(token3) || this.IsFunction(token3)){
				return true;
			}
		}else if(")" == token2){
			if(token3==")" || this.IsOperator(token3)){
				return true;
			}
		}
		return false;
	},
	/**
	 * 验证一个token是否是一个变量
	 * @param token
	 * @return boolean
	 */
	isVariable:function (token){
		//变量：#[对象名称~指标值类型]
		//if(token.startsWith("#[") && token.endsWith("]")){
		//常量
		if('myself'==token || '$kpi'==token || '$category'==token || '$strategy'==token || '$A'==token || '$S'==token || '$评估值'==token || '$记分卡名称'==token || '$指标名称'==token || '$告警状态颜色'==token || '$red'==token || '$green'==token || '$yellow'==token || '$orange'==token){
			return true;
		}
		//指标变量
		if(/^#\[[^~\]]+~[^~\]]+\]$/.test(token)){
			return true;
		}
		if(/^#\[[^~\]]+\]$/.test(token)){
			return true;
		}
		//记分卡变量
		if(/^@\[[^~\]]+~[^~\]]+\]$/.test(token)){
			return true;
		}
		if(/^@\[[^~\]]+\]$/.test(token)){
			return true;
		}
		//正整数和double数值看作是特殊的常量
		//if(str.matches("\\d+\\.\\d+") || str.matches("-?\\d+")){
		if(/^-?\d+$/.test(token)){
	        return true;
	   	}
		if(/^-?\d+.\d+$/.test(token)){
	        return true;
	   	}
		return false;
	},
	/**
	 * 拼接kinkeditor公式错误提示，错误处标红
	 * @param arrTokens
	 * @param token
	 * @param htmlStr
	 */
	showErrorFormula:function (arrTokens,position){
		var strTemp = "";
	    if (arrTokens.length > 0) {
	        for (intCntr = 0; intCntr < arrTokens.length; intCntr++){
	        	if(position != intCntr){
	        		strTemp += arrTokens[intCntr];
	        	}else{
	        		strTemp += '<span style="background-color:red;">'+arrTokens[intCntr]+'</span>';
	        	}
	        }
	    }
		return strTemp;
	},
	/**
	 * 匹配括号
	 * @params obj 公式
	 * @return boolean
	 */
	isBalanced:function (obj){
		var me = this;
	    var leftNormal = '(';   
	    var rightNormal = ')';
	    var leftSquare = '[';   
	    var rightSquare = ']';
	    var p;
	    var data = []; 
	    var flag = false;
	    var text = obj;//obj.innerText || obj.innerHTML || obj.value;
	    for(var i = 0 ; !flag && i < text.length ; i++){
	        switch(text.charAt(i)){   
	            case leftNormal:
	            	p = i;
	            	data.push(text.charAt(i));
	                break;
	            case leftSquare:
	            	p = i;
	                data.push(text.charAt(i));
	                break;
	            case rightNormal:
	                if(data.length == 0 || data.pop() != leftNormal){
	                	me.editor.html(obj.substr(0,i)+'<span style="background-color:#ff8484;">'+text.charAt(i)+'</span>'+obj.substr(i+1,text.length-1));
	                    flag = true;
					}
	                break;
	            case rightSquare:   
	                if(data.length == 0 || data.pop() != leftSquare){
	                	me.editor.html(obj.substr(0,i)+'<span style="background-color:#ff8484;">'+text.charAt(i)+'</span>'+obj.substr(i+1,text.length-1));
	                    flag = true;
					}
	                break;
	        }   
	    }
	    if(!flag && data.length>0){
	    	me.editor.html(obj.substr(0,p)+'<span style="background-color:#ff8484;">'+text.charAt(p)+'</span>'+obj.substr(p+1,text.length-1));
	    }
	    return data.length == 0 && !flag;
	},
	/**
	 * 打开风险选择控件
	 * @params type 影响程度/发生可能性
	 */
	openRiskSelectorWindow:function (type){
		var me = this;
		alert("弹出风险选择控件");
		//me.editor.insertHtml("#[<font color='#3366ff'>"+"risk"+'</font>%<font color="#969696">'+type+"</font>]");
	},
	/**
	 * 打开指标选择控件
	 * @param funcName 函数名称
	 * @param showType 指标/指标类型
	 */
	openKpiSelectorWindow:function (funcName,showType){
		var me = this;
		if('kcType' != me.showType){
			//kpi选择控件
			var kpiSelectorWindow = Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow', {
	            multiSelect: false,
	            closeAction:'hide',
	            selectedvalues: [],
	            onSubmit: function (store) {
	            	 var kpiName = '';
	            	 var type = '';
	            	 var items = store.data.items;
	                 Ext.Array.each(items, function (obj) {
	                     var item = obj.data;
	                     kpiName = item.name;
	                     type = item.valueType;
	                 });
	                 
	                 if('prior' == funcName){
	                	 if(''==type || undefined == type){
	                		 me.editor.insertHtml("prior(#[<font color='#3366ff'>"+kpiName+"</font>],0)");
	                	 }else{
	                		 me.editor.insertHtml("prior(#[<font color='#3366ff'>"+kpiName+'</font>~<font color="#969696">'+type+"</font>],0)");
	                	 }
	                 }else if('kpiStatusLevel' == funcName){
	                	 if(''==type || undefined == type){
	                		 me.editor.insertHtml("kpiStatusLevel(#[<font color='#3366ff'>"+kpiName+"</font>],0)");
	                	 }else{
	                		 me.editor.insertHtml("kpiStatusLevel(#[<font color='#3366ff'>"+kpiName+'</font>~<font color="#969696">'+type+"</font>],0)");
	                	 }
	                 }else if('myself' == funcName){
	                	 if(''==type || undefined == type){
	                		 me.editor.insertHtml("#[<font color='#3366ff'>myself</font>]");
	                	 }else{
	                		 me.editor.insertHtml("#[<font color='#3366ff'>myself</font>~<font color='#969696'>"+type+"</font>]");
	                	 }
	                 }else{
	                	 if(''==type || undefined == type){
	                		 me.editor.insertHtml("#[<font color='#3366ff'>"+kpiName+"</font>]");
	                	 }else{
	                		 me.editor.insertHtml("#[<font color='#3366ff'>"+kpiName+'</font>~<font color="#969696">'+type+"</font>]");
	                	 }
	                 }
	            }
	        }).show();
		}else {
			//kc选择控件
			alert("弹出kc选择控件");
		}
	},
	/**
	 * 打开记分卡选择控件
	 * @param funcName 函数名称
	 */
	openScSelectorWindow:function (funcName){
		var me = this;
		
		//记分卡选择控件
		var scSelectorWindow = Ext.create('FHD.ux.kpi.ScorecardSelectorWindow',{
			multiSelect: false,
            closeAction:'hide',
            selectedvalues: [],
            onSubmit: function (store) {
            	var scName = '';
            	var type = '';
            	var items = store.data.items;
                Ext.Array.each(items, function (obj) {
                	var item = obj.data;
                    scName = item.text;
                    type = item.valueType;
                });
                if(''==type || undefined == type){
            		me.editor.insertHtml("@[<font color='#3366ff'>"+scName+"</font>]");
            	}else{
            		me.editor.insertHtml("@[<font color='#3366ff'>"+scName+'</font>~<font color="#969696">'+type+"</font>]");
            	}
            }
		}).show();
	},
	/**
	 * 验证四则混合运算
	 * @param str
	 * @return boolean
	 */
	isExpression:function (str){
		try{
			eval("var v="+str);
			//alert(str+"="+v);
			return true;
		}catch(e){
			//alert("错误的表达式:"+str);
			return false;
		}
	}
});