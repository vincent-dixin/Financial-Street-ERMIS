
FHD.data.Authority = function(M) {
	var me = this;

	me.array = M || new Array();
	me.map = Ext.Array.toMap(me.array);
};

Ext.extend(FHD.data.Authority, Ext.util.Observable, {
	ifAllGranted : function(authorities) {
		var me = this, flag = true, 
		authorities_split = authorities.split(',');
		Ext.Array.forEach(authorities_split, function(v) {
			if (!me.map[v]) {
				flag = false;
			}
		});
		return flag;
	},
	ifAnyGranted : function(authorities) {
		var me = this, flag = false, 
		authorities_split = authorities.split(',');
		Ext.Array.forEach(authorities_split, function(v) {
			if (me.map[v]) {
				flag = true;
				//break;
			}
		});
		return flag;
	},
	ifNotGranted : function(authorities) {
		var me = this, flag = true, 
		authorities_split = authorities.split(',');
		Ext.Array.forEach(authorities_split, function(v) {
			if (me.map[v]) {
				flag = false;
				//break;
			}
		});
		return flag;
	}
});

FHD.Authority = new FHD.data.Authority(GajaxAuth);

$ifAllGranted = Ext.Function.bind(FHD.Authority.ifAllGranted,FHD.Authority);
$ifAnyGranted = Ext.Function.bind(FHD.Authority.ifAnyGranted,FHD.Authority);
$ifNotGranted = Ext.Function.bind(FHD.Authority.ifNotGranted,FHD.Authority);