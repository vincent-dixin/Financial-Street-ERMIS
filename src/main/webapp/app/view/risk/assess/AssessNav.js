/**
 * 
 * 风险评估主面版导航
 */
Ext.define('FHD.view.risk.assess.AssessNav', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessNav',
    
    requires: [
               'FHD.view.risk.assess.utils.GridCells'
              ],
            
    frame: false,
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.id = 'assessNavId';
        
        Ext.apply(me, {
        	margin: '5 0 5 0', 
        	autoScroll:true,
        	border : false,
        	html : 
        		
        		'<table width="99%" height="99%" align="center"  bgcolor="#ffffff">' + 
        		'<tr><td colspan="4"><div align="center"  style="height:50px;padding-bottom:15px;background:#DFE8F6 ;" ><img src="images/risk/comment_user_chart_32.png"><br>风险个人工作台<font color="#999999">-（财务部-周小川） &nbsp;&nbsp;</font></div></td></tr>' +
        		'<tr style="border-bottom:#CCC 1px dotted;border-right:#CCC 1px dotted;background:#fff; border:none;  ">' +
        		'<td width="15%">' +
        		'<div align="center"    style="height:305px;padding-bottom:15px;background:#DFE8F6;"  ><br><br><br><br><br><br><br><img  src="images/risk/pie-chart-icon.png"><br>部门现状</font></div>' +
        		'</td>' +
        		'<td>' + 
        			'<div id="bing1" align="center">FusionGadgets</div>' +
        		   '<div id="bing2" align="center">FusionGadgets</div>' +
        		 '</td>' +
        		 '<td>' +
        		   '<div id="bing3" align="center">FusionGadgets</div>' +
        		   '<div id="bing4" align="center">FusionGadgets</div>' +
        		 '</td>' +
        		'</td>' +
        		'<td align="center" style="border-bottom:#CCC 1px dotted;border-right:#CCC 1px dotted;background:#fff; border:none;  "> ' +
        		    '<div  align="left" data-spy="affix" style="height:320px; wheight:42px;background:#fff; border-right:#CCC 1px dotted;border-left:#CCC 1px dotted; vertical-align:middle" > <img src="images/risk/umbrella.png" style="vertical-align:middle; margin-right:5px" />重大风险清单：</font>' +
        		    '<br>    <br><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px" />' +
        		'<img src="images/risk/bookmark-small.png" style="vertical-align:middle; margin-right:5px" /><a href="javascript:void(0);" onclick="openwindow()"><font color="#000000" >市场波动风险</a>' + 
        		'<br /><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px" />' +
        		'<img src="images/risk/bookmark-small.png" style="vertical-align:middle; margin-right:5px" /><a href="javascript:void(0);" onclick="openwindow()"><font color="#000000" >竞争对手恶意竞争风险</a>' + 
        		'<br /><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px" />' +
        		'<img src="images/risk/bookmark-small.png" style="vertical-align:middle; margin-right:5px" /><a href="javascript:void(0);" onclick="openwindow()"><font color="#000000" >大客户付款违约风险</a>' + 
        		'<br /><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px" />' +
        		'<img src="images/risk/bookmark-small.png" style="vertical-align:middle; margin-right:5px" /><a href="javascript:void(0);" onclick="openwindow()"><font color="#000000" >内部员工舞弊风险</a>' + 
        		'</font>' +
        		'<div align="right" style="vertical-align:bottom;">' +
        		'<font size="-1" color="#CCCCCC">重大分析影响分布</font>' +
        		'<a  href="风险分布3.html" ><img src="images/risk/application.png" style="vertical-align:middle; margin-right:5px" /></a>' +
        		'<a href=\'javascript:void(0);\' onclick=\'analyse("风险评估/风险分布3.html","采购计划性差")\'></a>' +
        		'</div>' +
        		'</td>' +
        		'</tr>' +
        		'<tr><td valign="middle" width="20%" >' +
        		'<div align="center"   style="height:180px;padding-bottom:15px;background:#DFE8F6; " ><br><br><br><img  src="images/risk/comment_add_32.png"><br>工作待办</font></div>' +
        		'</td><td  width="40%"  height="150" colspan="2"  style="border-bottom:#CCC 1px dotted;border-top:#CCC 1px dotted;border-right:#CCC 1px dotted;  ">' +
        		'<div align="left"  ><br>' +
        		'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#1960AD" ><img src="images/risk/book-open-text-image.png" style="vertical-align:middle; margin-right:5px">&nbsp;风险评估</font><font color="#999999" >&nbsp;&nbsp;全部</font>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px"><a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.formulatePlan.FormulatePlanCard\',\'计划制定\');">计划制定</a>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px"><a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.kpiSet.SetMainPanel\',\'目标设定\');">任务分配</a><br><br><br><br>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px"><a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.quaAssess.QuaAssessMan\',\'定性评估\');">定性评估</a><br><br><br><br>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px"><a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.riskTidy.RiskTidyMan\',\'风险整理\');">风险整理</a><br><br><br><br>' +
        		'</td>' +
        		'<td width="40%"  height="100"  style="border-top:#CCC 1px dotted;border-right:#CCC 1px dotted; "><br>' +
        		'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF0000" ><img src="images/risk/book-open-list.png" style="vertical-align:middle; margin-right:5px"><a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.report.RiskAssessReportTemplateMain\',\'评估报告模板\');">风险评估报告</a></font>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF0000" ><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px">' +
        		'<a href="贝叶斯网络帮助.html">2013年度第二季度风险评估报告<font color="#FF0000">（新）</font></a></font>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF0000" ><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px">' +
        		'<a href="风险评估-因果分析.html">2013年度第二季度风险评估报告</a></font>' +
        		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF0000" ><img src="images/risk/join.gif" style="vertical-align:middle; margin-right:5px">' +
        		'<img src="images/risk/layer-small.png" style="vertical-align:middle; margin-right:5px">' +
        		'<a href="风险评估-因果分析.html">市场评估报告-（2013-04-21）</a></font>' +
        		'<br><br><br><br>' +
        		'</font>' +
        		'</div>' +
        		'</td></tr>' +


        		'<tr>' +
        		'<td>' +
        		'<div align="center" style="height:55px;margin-top:5px;padding-bottom:5px;background:#DFE8F6;" ><img src="images/risk/tools_32.png"><br/>基础管理</font></div>' +
        		'</td><td colspan="3" height="40" style="border-bottom:#CCC 1px dotted;border-right:#CCC 1px dotted;">' +
        		'<div  align="left" style="margin-top: 5px;background: #DFE8F6;height: 56px;">' +
        		'<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
        		'<img src="images/risk/chart_organisation.png" style="vertical-align:middle; margin-right:5px"><font color="#999999">&nbsp; &nbsp; &nbsp; &nbsp;<a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.formulatePlan.FormulatePlanEdit\',\'计划制定\');">计划制定<a/>&nbsp; &nbsp;&nbsp; &nbsp;</font>' +
        		 '<img src="images/risk/chart_organisation.png" style="vertical-align:middle; margin-right:5px"><font color="#999999">&nbsp; &nbsp; &nbsp; &nbsp;个人设置&nbsp; &nbsp;&nbsp; &nbsp;</font>' + 
        		     '<img src="images/risk/pie_chart.png" style="vertical-align:middle; margin-right:5px"><font color="#999999">统计信息&nbsp; &nbsp; &nbsp; &nbsp;</font>' +
        		     '<img src="images/risk/application.png" style="vertical-align:middle; margin-right:5px"><font color="#999999">部门风险结构&nbsp; &nbsp; &nbsp; &nbsp;</font>' +          
        		     '<img src="images/risk/application_side_expand.png" style="vertical-align:middle; margin-right:5px"><font color="#999999">导出风险&nbsp; &nbsp;&nbsp; &nbsp;</font>' +
        		     '<img src="images/risk/inbox-slide.png" style="vertical-align:middle; margin-right:5px"><font color="#999999"><a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.formulatePlan.FormulateGrid\',\'历史工作\');">历史工作<a/>&nbsp; &nbsp;&nbsp; &nbsp;</font>     ' +
        		     '<img src="images/risk/help.png" style="vertical-align:middle; margin-right:5px"><font color="#999999">帮助&nbsp; &nbsp;&nbsp; &nbsp;</font>     ' +
        			'</div>' +
        		'</td>' +
        		'</tr></table>'
        });
        
        
        me.callParent(arguments);
    }

});