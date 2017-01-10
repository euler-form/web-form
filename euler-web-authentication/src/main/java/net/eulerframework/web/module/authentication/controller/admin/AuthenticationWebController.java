package net.eulerframework.web.module.authentication.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.eulerframework.web.core.base.controller.AbstractWebController;
import net.eulerframework.web.core.base.request.QueryRequest;
import net.eulerframework.web.core.base.response.PageResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.eulerframework.common.util.BeanTool;
import net.eulerframework.web.core.annotation.AdminWebController;
import net.eulerframework.web.core.exception.ResourceExistException;
import net.eulerframework.web.module.authentication.entity.Authority;
import net.eulerframework.web.module.authentication.entity.Group;
import net.eulerframework.web.module.authentication.entity.User;
import net.eulerframework.web.module.authentication.service.IAuthorityService;
import net.eulerframework.web.module.authentication.service.IUserService;

@AdminWebController
@Scope("prototype")
@RequestMapping("")
@Deprecated
public class AuthenticationWebController extends AbstractWebController {
    

	@Resource
	private IUserService userService;
	@Resource
	private UserDetailsService userDetailsService;
	@Resource
	private IAuthorityService authorityService;

    @RequestMapping(value ="/user",method=RequestMethod.GET)
    public String user(){
        return "/manage/authentication/user";
    }
    
    @RequestMapping(value ="/group",method=RequestMethod.GET)
    public String group(){
        return this.display("group");
    }
    
    @RequestMapping(value ="/authority",method=RequestMethod.GET)
    public String authority(){
        return this.display("authority");
    }
    
    @RequestMapping(value ="/authorize",method=RequestMethod.GET)
    public String authorize(){
        return "/manage/authentication/authorize";
    }
    
    
    @ResponseBody
    @RequestMapping(value ="/findGroupByPage")
    public PageResponse<Group> findGroupByPage(HttpServletRequest request, String page, String rows) {
        QueryRequest queryRequest = new QueryRequest(request);
        
        int pageIndex = Integer.parseInt(page);
        int pageSize = Integer.parseInt(rows);
        return this.authorityService.findGroupByPage(queryRequest, pageIndex, pageSize);
    }

    @ResponseBody
    @RequestMapping(value ="/findAuthorityByPage")
    public PageResponse<Authority> findAuthorityByPage(HttpServletRequest request, String page, String rows) {
        QueryRequest queryRequest = new QueryRequest(request);
        
        int pageIndex = Integer.parseInt(page);
        int pageSize = Integer.parseInt(rows);
        return this.authorityService.findAuthorityByPage(queryRequest, pageIndex, pageSize);
    }



    @ResponseBody
    @RequestMapping(value = { "/saveGroup" }, method = RequestMethod.POST)
    public void saveGroup(Group group) {
        this.authorityService.saveGroup(group);
    }

    @ResponseBody
    @RequestMapping(value = { "/saveAuthority" }, method = RequestMethod.POST)
    public void saveAuthority(@Valid Authority authority) {
        this.authorityService.saveAuthority(authority);
    }

    
    @ResponseBody
    @RequestMapping(value ="/saveGroupAuthorities", method = RequestMethod.POST)
    public void saveGroupAuthorities(@RequestParam String groupId, @RequestParam String athorityIds) {
        String[] idArray = athorityIds.trim().replace(" ", "").split(";");
        List<Authority> authorities = this.authorityService.findAuthorityByIds(idArray);
        this.authorityService.saveGroupAuthorities(groupId, authorities);
    }

    
    @ResponseBody
    @RequestMapping(value ="/deleteGroups", method = RequestMethod.POST)
    public void deleteGroups(@RequestParam String ids) {
        String[] idArray = ids.trim().replace(" ", "").split(";");
        this.authorityService.deleteGroups(idArray);
    }
    
    @ResponseBody
    @RequestMapping(value ="/deleteAuthorities", method = RequestMethod.POST)
    public void deleteAuthorities(@RequestParam String ids) {
        String[] idArray = ids.trim().replace(" ", "").split(";");
        this.authorityService.deleteAuthorities(idArray);
    }
    
    @ResponseBody
    @RequestMapping(value="/findAllGroups")
    public List<Group> findAllGroups() {
        return this.authorityService.findAllGroups();
    }
    
    @ResponseBody
    @RequestMapping(value="/findAllAuthorities")
    public List<Authority> findAllAuthorities() {
        return this.authorityService.findAllAuthorities();
    }
}