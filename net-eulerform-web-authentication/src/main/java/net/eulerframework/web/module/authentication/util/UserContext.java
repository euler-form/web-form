package net.eulerframework.web.module.authentication.util;

import net.eulerframework.common.util.GlobalProperties;
import net.eulerframework.common.util.GlobalPropertyReadException;
import net.eulerframework.web.core.cache.DefaultObjectCache;
import net.eulerframework.web.module.authentication.entity.User;
import net.eulerframework.web.module.authentication.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserContext {

    private static UserDetailsService userDetailsService;

    private static IUserService userService;

    public static void setUserDetailsService(UserDetailsService userDetailsService) {
        UserContext.userDetailsService = userDetailsService;
    }

    public static void setUserService(IUserService userService) {
        UserContext.userService = userService;
    }

    private final static String USER_CONTEXT_CACHE_SECOND = "userContext.cacheSeconds";

    private final static DefaultObjectCache<String, User> USER_CACHE = new DefaultObjectCache<>(24 * 60 * 60 * 1000);

    private final static DefaultObjectCache<Serializable, User> USER_CACHE_ID = new DefaultObjectCache<>(24 * 60 * 60 * 1000);

    static {
        try {
            long cacheSecond = Long.parseLong(GlobalProperties.get(USER_CONTEXT_CACHE_SECOND));
            USER_CACHE.setDataLife(cacheSecond * 1000);
            USER_CACHE_ID.setDataLife(cacheSecond * 1000);
        } catch (GlobalPropertyReadException e) {
            // DO NOTHING
            LogManager.getLogger().info("Couldn't load " + USER_CONTEXT_CACHE_SECOND + " , use 86,400 for default.");
        }
    }
    
    public static List<User> getUserByNameOrCode(String nameOrCode) {
        return userService.findUserByNameOrCode(nameOrCode);
    }
    
    public static List<Serializable> getUserIdByNameOrCode(String nameOrCode) {
        List<User> users = getUserByNameOrCode(nameOrCode);
        List<Serializable> result = new ArrayList<>();
        for(User user : users){
            result.add(user.getId());
        }
        return result;
    }
    
    public static User getUserById(Serializable id){
        User user = USER_CACHE_ID.get(id);
        
        if(user == null) {
            user = userService.findUserById(id);
            
            if(user == null)
                return null;
            
            USER_CACHE_ID.put(user.getId(), user);
        }
        
        return user;        
    }

    public static String getUserNameAndCodeById(Serializable id) {
        User user = getUserById(id);
        if(user == null)
            return "UNKOWN-"+id;
        return user.getFullName()+"("+user.getUsername()+")";
    }

    public static User getCurrentUser() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context != null && context.getAuthentication() != null) {
                Object principal = context.getAuthentication().getPrincipal();

                if (User.class.isAssignableFrom(principal.getClass())) {
                    User user = (User) principal;
                    USER_CACHE.put(user.getUsername(), user);
                    return user;
                }
                
                if (UserDetails.class.isAssignableFrom(principal.getClass())) {
                    UserDetails userDetails = (UserDetails)principal;
                    principal = userDetails.getUsername();
                }

                if (String.class.isAssignableFrom(principal.getClass())
                        && (!User.ANONYMOUS_USERNAME.equalsIgnoreCase((String) principal))) {
                    String username = (String) principal;
                    User user = USER_CACHE.get(username);

                    if (user != null) {
                        return user;
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userDetails.getClass().isAssignableFrom(CredentialsContainer.class)) {
                        ((CredentialsContainer) userDetails).eraseCredentials();
                    }

                    if (User.class.isAssignableFrom(userDetails.getClass())) {
                        user = (User) userDetails;
                    } else {
                        user = new User();
                        user.loadDataFromOtherUserDetails(userDetails);
                    }

                    USER_CACHE.put(user.getUsername(), user);

                    return user;
                }
            }
        } catch (Exception e) {
            // DO NOTHING
        }
        return User.ANONYMOUS_USER;
    }
    
    public static void addSpecialSystemSecurityContext() {
        UsernamePasswordAuthenticationToken systemToken = new UsernamePasswordAuthenticationToken(
                User.SYSTEM_USER, null,
                User.SYSTEM_USER.getAuthorities());
        systemToken.setDetails(User.SYSTEM_USER);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(systemToken);
    }
}
