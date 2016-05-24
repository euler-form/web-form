package net.eulerform.web.core.util;

import net.eulerform.web.core.security.authentication.entity.User;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

public class UserContext {
    
    private static UserDetailsService userDetailsService;
    
    public static void setUserDetailsService(UserDetailsService userDetailsService) {
        UserContext.userDetailsService = userDetailsService;
    }

    private final static User ANONYMOUS_USER;
    
    static {
        ANONYMOUS_USER = new User();
        ANONYMOUS_USER.setId("anonymousUser");
        ANONYMOUS_USER.setUsername("anonymousUser");
        ANONYMOUS_USER.setAuthorities(null);
        ANONYMOUS_USER.setAccountNonExpired(false);
        ANONYMOUS_USER.setAccountNonLocked(false);
        ANONYMOUS_USER.setEnabled(false);
        ANONYMOUS_USER.setCredentialsNonExpired(false);
    }
    
    public static User getCurrentUser() {
          SecurityContext context = SecurityContextHolder.getContext();
          if(context != null && context.getAuthentication() != null){
              Object principal = context.getAuthentication().getPrincipal();
              
              if(principal.getClass().isAssignableFrom(User.class))
                  return (User)context.getAuthentication().getPrincipal();
              
              if(principal.getClass().isAssignableFrom(String.class)) {
                  User user = (User) userDetailsService.loadUserByUsername((String) principal);
                  user.eraseCredentials();
                  return user;
              }
          }
        return ANONYMOUS_USER;
    }
}
