package net.eulerframework.web.module.authentication.dao;

import java.util.List;

import net.eulerframework.web.core.base.dao.IBaseDao;
import net.eulerframework.web.core.base.request.QueryRequest;
import net.eulerframework.web.core.base.response.PageResponse;
import net.eulerframework.web.module.authentication.entity.Authority;

public interface IAuthorityDao extends IBaseDao<Authority> {

    PageResponse<Authority> findAuthorityByPage(QueryRequest queryRequest, int pageIndex, int pageSize);

    List<Authority> findAllAuthoritiesInOrder();
    
}
