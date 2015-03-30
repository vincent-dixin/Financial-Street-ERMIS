<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>指标类型选择</title>
            <script type="text/javascript">
                var kpitypeselect_view = (function () {
                    var url = __ctxPath + "/kpi/kpi/findkpitypeall.f";

                    function save() {
                        var selections = kpitypeGrid.getSelectionModel().getSelection();
                        var length = selections.length;
                        if (length > 1) {
                            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get("fhd.kpi.kpi.prompt.kpitypeone"));
                            return;
                        }
                        var selection = selections[0]; //得到选中的记录
                        kpiBasicPanel.selecttypeflag = true;
                        kpiBasicPanel.kpitypeid = selection.get('id');
                        kpiBasicPanel.kpitypename = selection.get('name');
                        kpiBasicPanel.formwindow.close();
                        kpiBasicPanel.formLoad();
                    }

                    var formButtons = [ // 表单按钮
                    {
                        text: '选择',
                        handler: function () {
                            save();
                        }
                    }, {
                        text: FHD.locale.get('fhd.common.cancel'),
                        handler: function () {
                        	kpiBasicPanel.formwindow.close();
                        }
                    }];
                    var kpitypeGrid = Ext.create('FHD.ux.EditorGridPanel', {
                        pagable: false,
                        border: false,
                        url: url,
                        height: 560,
                        cols: [{
                            dataIndex: 'id',
                            id: 'id',
                            width: 0
                        }, {
                            header: FHD.locale.get('fhd.kpi.kpi.form.name'),
                            dataIndex: 'name',
                            sortable: true,
                            flex: 1
                        }]
                    });

                    Ext.define('kpitypeselect.view', {
                        kpitypePanel: null,
                        init: function () {
                            this.kpitypePanel = Ext.create('Ext.form.Panel', {
                                renderTo: 'kpitypeselect${param._dc}',
                                border: false,
                                items: [
                                kpitypeGrid],
                                buttons: formButtons
                            });

                        }

                    });

                    var kpitypeselect_view = Ext.create('kpitypeselect.view');
                    return kpitypeselect_view;

                })();

                Ext.onReady(function () {
                    kpitypeselect_view.init();
                    kpiBasicPanel.formwindow.on('resize', function (me) {
                        kpitypeselect_view.kpitypePanel.setWidth(me.getWidth() - 10);
                        kpitypeselect_view.kpitypePanel.setHeight(me.getHeight() - 40);
                    })
                })
            </script>
        </head>
        
        <body>
            <div id='kpitypeselect${param._dc}'></div>
        </body>
    
    </html>