package com.fhd.fdc.commons.binding;
import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;
/**    
*     
* @author sping mvc Integer 类型转换器    
*    
*/     
public class FHDIntEditor extends PropertyEditorSupport  {     
        @Override     
        public void setAsText(String text) throws IllegalArgumentException {     
            if(text == null ||text.equals(""))     
                text = "0";     
            if ( !StringUtils.hasText(text)) {     
                 
                setValue(null);     
            }     
            else {     
                setValue(Integer.parseInt(text));//这句话是最重要的，他的目的是通过传入参数的类型来匹配相应的databind     
            }     
        }     
        @Override     
        public String getAsText() {     
                 
        	return null!=getValue()?getValue().toString():null;     
        }     
}    
