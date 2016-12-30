package net.eulerframework.web.module.basedata.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.eulerframework.common.util.FileReader;
import net.eulerframework.web.core.base.request.QueryRequest;
import net.eulerframework.web.core.base.response.PageResponse;
import net.eulerframework.web.core.base.service.impl.BaseService;
import net.eulerframework.web.module.basedata.dao.ICodeTableDao;
import net.eulerframework.web.module.basedata.dao.IModuleDao;
import net.eulerframework.web.module.basedata.dao.IPageDao;
import net.eulerframework.web.module.basedata.entity.CodeTable;
import net.eulerframework.web.module.basedata.entity.Module;
import net.eulerframework.web.module.basedata.entity.Page;
import net.eulerframework.web.module.basedata.service.IBaseDataService;

@Service
public class BaseDataService extends BaseService implements IBaseDataService {
    
    @Resource private ICodeTableDao codeTableDao;
    
    @Resource private IModuleDao moduleDao;
    @Resource private IPageDao pageDao;
    
    @Resource private ObjectMapper objectMapper;
    
    @Value("${project.Mode}")
    private String projectMode;
    @Value("${project.Version}")
    private String projectVersion;
    @Value("${project.Buildtimesamp}")
    private String projectBuildtimesamp;

    @Override
    public void loadBaseData() {
        
        //allModules
        this.refreshModules();
        
        //contextPaht
        ServletContext sc = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        String contextPath = sc.getContextPath();
        sc.setAttribute("contextPath", contextPath);
        
        sc.setAttribute("project.Version", projectVersion);
        sc.setAttribute("project.Mode", projectMode.toUpperCase());
        sc.setAttribute("project.Buildtimesamp", projectBuildtimesamp);
    }
    
    @Override
    public void createCodeDict() throws IOException {
        String webRootRealPath = this.getServletContext().getRealPath("/");
        String codeTableJsFilePath = "resources/scripts/lib/common-dict.js";
        
        String codeTableJsFileRealPath = webRootRealPath+codeTableJsFilePath;
        this.logger.info("createCodeDict:"+codeTableJsFileRealPath);

        List<CodeTable> codes = this.codeTableDao.findAllCodeOrderByName();
        Map<String, List<Dict>> codeTableMap = new HashMap<>();
        List<Dict> dict = null;
        String name = "";
        for(CodeTable code : codes) {
            if(!name.equals(code.getName())){
                if(dict != null && !dict.isEmpty()){
                    codeTableMap.put(name, dict);
                }                
                dict = new ArrayList<>();
                name=code.getName();
            }
            dict.add(new Dict(code));
        }

        if(dict != null && !dict.isEmpty()){
            codeTableMap.put(name, dict);
        }
        FileReader.deleteFile(codeTableJsFileRealPath);
        this.writeCodeTableToJs(codeTableMap, codeTableJsFileRealPath);
    }
    
    private void writeCodeTableToJs(Map<String, List<Dict>> codeTableMap, String path) throws IOException{
        StringBuffer resutlBuffer = new StringBuffer();

        for (Map.Entry<String, List<Dict>> entry : codeTableMap.entrySet()) {
            if(resutlBuffer.length() > 0)
                resutlBuffer.delete(0,resutlBuffer.length());
            resutlBuffer.append(entry.getKey());
            resutlBuffer.append('=');
            resutlBuffer.append(this.objectMapper.writer().writeValueAsString(entry.getValue()));
            resutlBuffer.append(';');
            resutlBuffer.append('\n');
            FileReader.writeFile(path, resutlBuffer.toString(), true);
        }        
    }

    @SuppressWarnings("unused")
    private class Dict{
        private String key;
        private String value;
        private String valuei18n;
        private String style;
                
        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getValuei18n() {
            return valuei18n;
        }

        public String getStyle() {
            return style;
        }

        private Dict(CodeTable codeTable){
            this.key = codeTable.getKey();
            this.value = codeTable.getValue();
            this.valuei18n = codeTable.getValueI18nCode();
            this.style = codeTable.getCssStyle();
        }
    }
    
    //==================================================================================

    @Override
    public PageResponse<CodeTable> findCodeTableByPage(QueryRequest queryRequest, int pageIndex, int pageSize) {
        return this.codeTableDao.findCodeTableByPage(queryRequest, pageIndex, pageSize);
    }

    @Override
    public void saveCodeTable(CodeTable codeTable) {
        this.codeTableDao.saveOrUpdate(codeTable);
    }

    @Override
    public void deleteCodeTables(Serializable[] idArray) {
        this.codeTableDao.deleteByIds(idArray);
        
    }

    @Override
    public List<Module> findAllModuleFromDB() {
        return this.moduleDao.findAllInOrder();
    }

    @Override
    public Module findModuleById(String id) {
        return this.moduleDao.load(id);
    }

    @Override
    public Page findPageById(String id) {
        return this.pageDao.load(id);
    }

    @Override
    public void savePage(Page page) {
        if(page.getModuleId() == null)
            throw new RuntimeException("Module Id 不能为空");
        this.pageDao.saveOrUpdate(page);
        this.refreshModules();
    }

    @Override
    public void deletePage(Serializable id) {
        this.pageDao.deleteById(id);
        this.refreshModules();
    }

    @Override
    public void saveModule(Module module) {
        if(module.getId() != null){
            List<Page> pages = this.moduleDao.load(module.getId()).getPages();
            if(module.getPages() == null || module.getPages().isEmpty()) {
                module.setPages(pages);
            }
        }
        this.moduleDao.saveOrUpdate(module);
        this.refreshModules();
    }

    @Override
    public void deleteModule(Serializable id) {
        List<Page> pages = this.moduleDao.load(id).getPages();
        this.pageDao.deleteAll(pages);
        this.moduleDao.deleteById(id);
        this.refreshModules();
    }
    
    private void refreshModules(){
        this.moduleDao.flushSession();
        List<Module> allModules = this.moduleDao.findAllInOrder();
        ContextLoader.getCurrentWebApplicationContext().getServletContext().setAttribute("menu", allModules);        
    }
}
