package net.eulerform.web.module.authentication.dao;

import java.util.List;

import net.eulerform.web.core.base.dao.IBaseDao;
import net.eulerform.web.core.base.entity.PageResponse;
import net.eulerform.web.core.base.entity.QueryRequest;
import net.eulerform.web.module.authentication.entity.Scope;

public interface IScopeDao extends IBaseDao<Scope> {

    PageResponse<Scope> findScopeByPage(QueryRequest queryRequest, int pageIndex, int pageSize);

    List<Scope> findAllScopesInOrder();
    
}