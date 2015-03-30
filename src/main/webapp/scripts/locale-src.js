/**
 * FHD.data.Locale
 * 
 * @author 胡迪新
 * @version 1.0
 */

FHD.data.Locale = function(M) {
   this.map = M || {}
   
};

Ext.extend(FHD.data.Locale, Ext.util.Observable, {
   get : function(key) {
   	  
      var value = this.map[key] || ('! Not Found: '+key + ' - '+this.map['LOCALE']); 
      if(arguments.length > 1 && value.indexOf('{') >= 0) {
         value = new Ext.Template(value).apply(Array.prototype.slice.call(arguments, 1))
      }
      return value;
   }
});

// 服务器组装国际化json数据 GajaxLocale
FHD.locale = new FHD.data.Locale(GajaxLocale);

$locale = Ext.Function.bind(FHD.locale.get,FHD.locale);
