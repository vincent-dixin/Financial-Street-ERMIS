package com.fhd.comm.business.formula;

import java.util.Vector;

import com.fhd.comm.interfaces.ICustomFunctionBO;

public class IfFunctionBO extends FunctionCalculateBO implements ICustomFunctionBO{

	/**
	 * if函数计算.
	 * @param formulaArray
	 * @return String
	 */
	@Override
	public String calculateRule(String[] formulaArray) {
		String reval = "0";
		if(formulaArray[0].toLowerCase().indexOf("isnovalue") != -1){
			if("1".equals(isNoValueValid(formulaArray[0]))){
				reval = calculate(formulaArray[1]);
			}else{
				reval = calculate(formulaArray[2]);
			}
		}else{
			if(recursion(formulaArray[0])){
				reval = calculate(formulaArray[1]);
			} else {
				reval = calculate(formulaArray[2]);
			}
		}
		
		return reval;
	}
	/**
	 * 判断表达式
	 * @param expression
	 * @return boolean
	 */
	public String isNoValueValid(String expression){
		String reval = "0";
		
		int bit = 0;
		Vector<String> splitStr = splitStr(expression);
		for(int i=0;i<splitStr.size();i++){
			if(!"&&".equals(splitStr.get(i)) && !"||".equals(splitStr.get(i))){
				if(splitStr.get(i).toLowerCase().indexOf("isnovalue") != -1){
					bit = expressionValid(splitStr.get(i));
				}else{
					if("0".equals(splitStr.get(i))){
						if(splitStr.get(i).indexOf("!")!=-1){
							bit = 1;
						}else{
							bit = 0;
						}
					}else{
						if(splitStr.get(i).indexOf("!")!=-1){
							bit = 0;
						}else{
							bit = 1;
						}
					}
				}
			}else if("&&".equals(splitStr.get(i))){
				if(bit==0){
					bit = 0;
					i++;
				}
				continue;
			}else if("||".equals(splitStr.get(i))){
				if(bit==1){
					bit = 1;
					i++;
				}
				continue;
			}
		}
		reval = String.valueOf(bit);
		return reval;
	}
	/**
	 * 判断表达式
	 * @param expression
	 * @return boolean
	 */
	public static int expressionValid(String expression){
		int bit = 0;
		
		boolean flag = false;
		
		if(expression.indexOf("!")!=-1){
			//表达式中存在!
			if(isNoValueExpressionValid(expression)){
				flag = false;
			}else{
				flag = true;
			}
		}else{
			//表达式中不存在!
			flag = isNoValueExpressionValid(expression);
		}
		if(flag){
			bit = 1;
		}else{
			bit = 0;
		}
		return bit;
	}
	/**
	 * 判断isNoValue函数表达式是否为真.
	 * @param isNoValueExpression
	 * @return boolean
	 */
	public static boolean isNoValueExpressionValid(String isNoValueExpression){
		boolean flag = false;
		String ret = "";
		
		int bracket = 0;// 对应括号个数
		int pos = 0;//记录位置
		int index = 0;
		for(int i=0;i<isNoValueExpression.length();i++){
			pos += 1;
			String s = String.valueOf(isNoValueExpression.charAt(i));
			if("(".equals(s)){
				index = pos;
				bracket++;
			}else if (")".equals(s))
				bracket--;
			if (bracket == 0 && index != 0) {
				ret = isNoValueExpression.substring(index, pos-1);
				if(!"null".equals(ret) && pos == isNoValueExpression.length()){
					flag = true;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 表达式里的参数分割.
	 * String str1 = "!isnovalue(1)&&isnovalue(null)||isnovalue(null)";
	 * 先把字符串拆分成一个数组:[!isnovalue(null),&&,isnovalue(1),||,isnovalue(2)]
	 * @param pstrExpression
	 * @return Vector<String>
	 */
	public static Vector<String> splitStr(String pstrExpression) {
	   int intCntr;
	   char chrChar;
	   String strToken= "";

	   intCntr = 0;
	   
	   Vector<String> arrTokens = new Vector<String>();

	   while (intCntr < pstrExpression.length()){
	       chrChar = pstrExpression.charAt(intCntr);
	       switch (chrChar){
	           case ' ' :
	               if (strToken.length() > 0){
	                   arrTokens.add(strToken);
	                   strToken = "";
	               }
	               break;
	           case '&' :
	               if (strToken.length() > 1){
	            	   arrTokens.add(strToken);
	                   strToken = "&";
	               }else if(strToken.length() > 0 && "&".equals(strToken)){
	                   strToken += "&";
	                   arrTokens.add(strToken);
	                   strToken = "";
	               }
	               break;
	           case '|' :
	               if (strToken.length() > 1){
	            	   arrTokens.add(strToken);
	                   strToken = "|";
	               }else if(strToken.length() > 0 && "|".equals(strToken)){
	                   strToken += "|";
	                   arrTokens.add(strToken);
	                   strToken = "";
	               }
	               break;
	           default :
	               strToken += chrChar;
	               break;
	       }
	       intCntr++;
	   }

	   if (strToken.length() > 0)
		   arrTokens.add(strToken);
	   return arrTokens;
	}
}
