package net.eulerframework.web.module.authentication.entity;

import net.eulerframework.web.core.base.entity.UUIDEntity;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name = "SYS_USER")
public class User extends UUIDEntity<User> implements UserDetails, CredentialsContainer {
    public final static User ANONYMOUS_USER;
    public final static User ROOT_USER;
    public final static String ANONYMOUS_USERNAME = "anonymousUser";
    public final static String ROOT_USERNAME = "root";
    
    static {
        ANONYMOUS_USER = new User();
        ANONYMOUS_USER.setId(ANONYMOUS_USERNAME);
        ANONYMOUS_USER.setUsername(ANONYMOUS_USERNAME);
        ANONYMOUS_USER.setAuthorities(null);
        ANONYMOUS_USER.setAccountNonExpired(false);
        ANONYMOUS_USER.setAccountNonLocked(false);
        ANONYMOUS_USER.setEnabled(false);
        ANONYMOUS_USER.setCredentialsNonExpired(false);
        
        ROOT_USER = new User();
        ROOT_USER.setId(ROOT_USERNAME);
        ROOT_USER.setUsername(ROOT_USERNAME);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.ROOT_AUTHORITY);
        ROOT_USER.setAuthorities(authorities);
        ROOT_USER.setAccountNonExpired(true);
        ROOT_USER.setAccountNonLocked(true);
        ROOT_USER.setEnabled(true);
        ROOT_USER.setCredentialsNonExpired(true);
    }

    @NotNull
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;
    @NotNull
    @Column(name = "EMAIL", nullable = true, unique = true)
    private String email;
    @Column(name = "MOBILE", nullable = true, unique = true)
    private String mobile;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @NotNull
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Column(name = "ACCOUNT_NON_EXPIRED", nullable = false)
    private Boolean accountNonExpired;
    @Column(name = "ACCOUNT_NON_LOCKED", nullable = false)
    private Boolean accountNonLocked;
    @Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false)
    private Boolean credentialsNonExpired;
    @Column(name = "ROOT")
    private Boolean root;
    @Column(name = "RESET_TOKEN", unique = true)
    private String resetToken;
    @Column(name = "RESET_TOKEN_EXPIRE_TIME")
    private Date resetTokenExpireTime;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "SYS_USER_AUTHORITY", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
    @Transient
    private Set<Authority> authorities;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SYS_USER_GROUP", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID") })
    private Set<Group> groups;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return this.enabled == null ? false : this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired == null ? false : this.accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked == null ? false : this.accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired == null ? false : this.credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    
    @Override
    public Set<Authority> getAuthorities() {
        Set<Authority> result =  new HashSet<>();
        
        if(this.authorities != null && !this.authorities.isEmpty())
            result.addAll(this.authorities);
        
        if(this.groups != null) {
            for(Group group : this.groups) {
                if(group.getAuthorities() != null && !group.getAuthorities().isEmpty())
                    result.addAll(group.getAuthorities());
            }            
        }
        
        if(this.root == true) {
            result.add(Authority.ROOT_AUTHORITY);
        }
        
        return result;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
    
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Date getResetTokenExpireTime() {
        return resetTokenExpireTime;
    }

    public void setResetTokenExpireTime(Date resetTokenExpireTime) {
        this.resetTokenExpireTime = resetTokenExpireTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean isRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public User loadDataFromOtherUserDetails(UserDetails userDetails) {
    	User result = new User();
    	result.setId(userDetails.getUsername());
    	result.setUsername(userDetails.getUsername());
    	Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
    	if(authorities == null){
        	result.setAuthorities(null);    		
    	} else {
        	Set<Authority> tmpAuthorities = new HashSet<>();
    		for(GrantedAuthority authority : authorities){
    			Authority tmpAuthority = new Authority();
    			tmpAuthority.setAuthority(authority.getAuthority());
    			tmpAuthority.setDescription(authority.getAuthority());
    			tmpAuthorities.add(tmpAuthority);
    		}
        	result.setAuthorities(tmpAuthorities); 
    	}
    	result.setAccountNonExpired(userDetails.isAccountNonExpired());
    	result.setAccountNonLocked(userDetails.isAccountNonLocked());
    	result.setEnabled(userDetails.isEnabled());
    	result.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
    	return result;
    }

}
