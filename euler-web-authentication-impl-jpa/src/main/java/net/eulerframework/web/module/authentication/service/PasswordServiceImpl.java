package net.eulerframework.web.module.authentication.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.eulerframework.common.email.ThreadSimpleMailSender;
import net.eulerframework.common.util.CommonUtils;
import net.eulerframework.common.util.jwt.InvalidJwtException;
import net.eulerframework.common.util.jwt.Jwt;
import net.eulerframework.common.util.jwt.JwtEncryptor;
import net.eulerframework.web.config.WebConfig;
import net.eulerframework.web.module.authentication.entity.EulerUserEntity;
import net.eulerframework.web.module.authentication.exception.InvalidEmailResetTokenException;
import net.eulerframework.web.module.authentication.exception.InvalidSmsResetPinException;
import net.eulerframework.web.module.authentication.exception.UserNotFoundException;
import net.eulerframework.web.module.authentication.vo.UserResetJwtClaims;
import net.eulerframework.web.util.ServletUtils;

/**
 * @author cFrost
 *
 */
@Service("passwordService")
public class PasswordServiceImpl implements PasswordService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private EulerUserEntityService eulerUserEntityService;
    @Resource
    private JwtEncryptor jwtEncryptor;
    @Resource
    private String resetPasswordEmailSubject;
    @Resource
    private String resetPasswordEmailContent;

    @Autowired(required = false)
    private ThreadSimpleMailSender threadSimpleMailSender;

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    @Override
    public EulerUserEntityService getEulerUserEntityService() {
        return this.eulerUserEntityService;
    }

    @Override
    public void passwdResetSMSGen(String mobile) {
        // TODO Auto-generated method stub
    }

    @Override
    public void passwdResetEmailGen(String email) {
        EulerUserEntity user;
        try {
            user = this.eulerUserEntityService.loadUserByEmail(email);
        } catch (UserNotFoundException e) {
            CommonUtils.sleep(1);
            return;
        }

        UserResetJwtClaims claims = new UserResetJwtClaims(user, 30 * 60);
        Jwt jwt = this.jwtEncryptor.encode(claims);
        String token = jwt.getEncoded();
        String resetPasswordUrl = WebConfig.getWebUrl() + ServletUtils.getRequest().getContextPath()
                + "/reset-password?type=EMAIL&token=" + token;
        String content = this.resetPasswordEmailContent.replaceAll("\\$\\{resetPasswordUrl\\}", resetPasswordUrl);
        if (this.threadSimpleMailSender != null) {
            this.threadSimpleMailSender.send(this.resetPasswordEmailSubject, content, email);
        } else {
            System.out.println(content);
        }
    }

    @Override
    public String analyzeUserIdFromSmsResetPin(String pin) throws InvalidSmsResetPinException {
        // TODO Auto-generated method stub
        throw new InvalidSmsResetPinException();
    }

    @Override
    public String analyzeUserIdFromEmailResetToken(String token) throws InvalidEmailResetTokenException {
        UserResetJwtClaims claims;
        try {
            claims = this.jwtEncryptor.decode(token, UserResetJwtClaims.class);
        } catch (InvalidJwtException e) {
            throw new InvalidEmailResetTokenException();
        }
        return claims.getUserId();
    }

}
