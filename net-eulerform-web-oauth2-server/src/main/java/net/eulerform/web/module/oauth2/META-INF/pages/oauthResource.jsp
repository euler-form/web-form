<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <link rel="stylesheet" href="${contextPath}/resources/css/lib/easyui/themes/metro/easyui.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/lib/easyui/themes/icon.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/lib/global.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/lib/icon.css">

    <title></title>

</head>

<body class="easyui-layout">

    <div data-options="region:'north'" style="overflow:hidden;">
        <form id="search-form">
            <table class="search-table">
                <tr>
                    <td>${euler:i18n('resource.resourceId')}</td>
                    <td><input class="easyui-textbox search-input" id="query_resourceId" name="query.resourceId" /></td>
                    <td>${euler:i18n('resource.name')}</td>
                    <td><input class="easyui-textbox search-input" id="query_name" name="query.name" /></td>
                    <td>${euler:i18n('resource.description')}</td>
                    <td><input class="easyui-textbox search-input" id="query_description" name="query.description" /></td>
                </tr>
            </table>
            <table class="search-btn-table">
                <tr><td>
                <a class="easyui-linkbutton search-btn" data-options="iconCls:'icon-search'" id="search-btn" onclick="onSearch()">${euler:i18n('global.search')}</a>
                <a class="easyui-linkbutton search-btn" data-options="iconCls:'icon-reload'" id="reset-btn" onclick="onReset()">${euler:i18n('global.reset')}</a>
                </td></tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center'" style="background:#eee;">
        
        <div id="toolbar">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">${euler:i18n('global.add')}</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" id="editBtn" iconCls="icon-edit" plain="true" onclick="onEdit()">${euler:i18n('global.edit')}</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" id="editBtn" iconCls="icon-remove" plain="true" onclick="onDelete()">${euler:i18n('global.delete')}</a>
        </div>
        <table id="dg" class="easyui-datagrid" 
            data-options="
                fit:true,
                url:'findOauthResourceByPage',
                toolbar:'#toolbar',
                fitColumns:false,
                rownumbers:true,
                remoteSort:false,
                pagination:true,
                singleSelect:false,
                onDblClickRow:onDblClickRow">
            <thead>
                <tr>
                    <th data-options="field:'ck', checkbox:true"></th>
                    <th data-options="field:'id',hidden:true">ID</th>
                    <th data-options="field:'resourceId',align:'center',width:'200px'">${euler:i18n('resource.resourceId')}</th>
                    <th data-options="field:'name',align:'center',width:'200px'">${euler:i18n('resource.name')}</th>
                    <th data-options="field:'description',align:'center',width:'200px'">${euler:i18n('resource.description')}</th>
                </tr>
            </thead>
        </table>
        <div id="dlg" class="easyui-dialog dlg-window" style="width:400px;"
                data-options="
                    zIndex:99999999,
                    closed:true,
                    iconCls:'icon-save',
                    resizable:false,
                    modal:true,
                    onClose:clearDlg,
                    buttons:[{text:'${euler:i18n('global.save')}', iconCls:'icon-ok', handler:onSave},{text:'${euler:i18n('global.cancel')}', iconCls:'icon-cancel', handler:onCancel}]">
            <form id="fm" class="dlg-form" method="post">
                <input type="hidden" id="dlg_id" name="id">
                <div class="dlg-line"><label class="dlg-label">${euler:i18n('resource.resourceId')}</label><span class="dlg-span"><input class="easyui-textbox dlg-input" data-options="required:true" id="dlg_resourceId" name="resourceId"></span></div>
                <div class="dlg-line"><label class="dlg-label">${euler:i18n('resource.name')}</label><span class="dlg-span"><input class="easyui-textbox dlg-input" data-options="required:true" id="dlg_name" name="name"></span></div>
                <div class="dlg-line"><label class="dlg-label">${euler:i18n('resource.description')}</label><span class="dlg-span"><input class="easyui-textbox dlg-input" data-options="" id="dlg_description" name="description"></span></div>
            </form>
        </div>        
    </div>
    <script src="${contextPath}/resources/scripts/lib/easyui/jquery.min.js"></script>
    <script src="${contextPath}/resources/scripts/lib/easyui/jquery.easyui.min.js"></script>
    <script src="${contextPath}/resources/scripts/lib/easyui/easyui-lang-zh_CN.js"></script>
    <script src="${contextPath}/resources/scripts/lib/common-dict.js"></script>
    <script src="${contextPath}/resources/scripts/lib/util.js"></script>

    <script>    
        $(function(){
        });
        
        function refreshDatagrid(){
            var jsonParam = $('#search-form').serializeJson();
            $('#dg').datagrid('reload', jsonParam);
        }
        
        function onSearch() {
            var jsonParam = $('#search-form').serializeJson();
            $('#dg').datagrid('load', jsonParam);            
        }
        
        function onReset() {
            $('#search-form').form('clear');
        }
        
        function onAdd() {
            $('#fm').form('clear');
            $('#dlg').dialog('open').dialog('setTitle', "${euler:i18n('jsp.resource.addResource')}");
        }
        
        function onEdit() {
            var row = $('#dg').datagrid('getSelections');
            
            if(row == null || row.length < 1){
                $.messager.alert("${euler:i18n('global.remind')}", "${euler:i18n('global.pleaseSelectRowsToEdit')}");
            } else if(row){
                $('#fm').form('load', row[0]);
                $('#dlg').dialog('open').dialog('setTitle', "${euler:i18n('jsp.resource.editResource')}");
                
            }
        }
        
        function onSave() {
            $('#fm').form('submit', {
                url:'saveOauthResource',
                onSumit:function(){
                    return $(this).form('validate');
                },
                success:function(data) {
                    if(data) {
                        $.messager.alert("${euler:i18n('global.error')}", data);
                        return;
                    }
                    clearDlg();
                    onClose();
                    refreshDatagrid();
                }
            });
        }
        
        function onDelete() {
            var row = $('#dg').datagrid('getSelections');
            
            if(row == null || row.length < 1){
                $.messager.alert("${euler:i18n('global.remind')}", "${euler:i18n('global.pleaseSelectRowsToDelete')}");
            } else if(row){
                $.messager.confirm("${euler:i18n('global.warn')}", "${euler:i18n('global.sureToDelete')}", function(r) {
                    if(r) {
                        var ids = "";
                        for(var i = 0; i < row.length; i++){
                            ids += row[i].id + ';';
                        }
                        $.ajax({
                            url:'deleteOauthResources',
                            type:'POST',
                            async:true,
                            data: "ids=" + ids,
                            error:function(XMLHttpRequest, textStatus, errorThrown) {
                                $.messager.alert("${euler:i18n('global.error')}", XMLHttpRequest.responseText);
                            },
                            success:function(data, textStatus) {
                                refreshDatagrid();                                    
                            }
                        });
                    }
                });
            }
        }
        
        function onCancel() {
            clearDlg();
            onClose();
        }
        
        function clearDlg(){
            $('#fm').form('clear');            
        }
        
        function onClose(){
            $('#dlg').dialog('close');
        }
        
        function onDblClickRow(rowIndex, rowData){
            var row = $('#dg').datagrid('clearSelections');
            var row = $('#dg').datagrid('selectRow', rowIndex);
            onEdit();
        }
    </script>
</body>

</html>