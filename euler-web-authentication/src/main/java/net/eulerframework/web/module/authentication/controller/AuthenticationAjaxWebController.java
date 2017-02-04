/**
 * 
 */
package net.eulerframework.web.module.authentication.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.eulerframework.web.core.annotation.WebController;
import net.eulerframework.web.core.base.controller.AjaxSupportWebController;
import net.eulerframework.web.core.base.response.AjaxResponse;
import net.eulerframework.web.core.base.response.EmptySuccessAjaxResponse;
import net.eulerframework.web.module.authentication.service.IAuthenticationService;

/**
 * @author cFrost
 *
 */
@WebController
@RequestMapping("/")
public class AuthenticationAjaxWebController extends AjaxSupportWebController {

    @Resource
    private IAuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping(value = "getPasswordResetSMS_ajax", method = RequestMethod.POST)
    public AjaxResponse<String> getPasswordResetSMS(@RequestParam String mobile) {
        this.authenticationService.passwdResetSMSGen(mobile);
        return new EmptySuccessAjaxResponse();
    }

}
