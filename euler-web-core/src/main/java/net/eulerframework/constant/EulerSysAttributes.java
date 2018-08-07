package net.eulerframework.constant;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cFrost
 *
 */
public enum EulerSysAttributes { 

    WEB_URL("__WEB_URL"),
    
    CONTEXT_PATH("__CONTEXT_PATH"),
    ASSETS_PATH("__ASSETS_PATH"),
    AJAX_PATH("__AJAX_PATH"),
    ADMIN_PATH("__ADMIN_PATH"),
    ADMIN_AJAX_PATH("__ADMIN_AJAX_PATH"),
    
    SIGN_OUT_ACTION("__SIGN_OUT_ACTION"),
    
    DEBUG_MODE("__DEBUG_MODE"),
    
    PROJECT_VERSION("__PROJECT_VERSION"),
    PROJECT_MODE("__PROJECT_MODE"),
    PROJECT_BUILDTIME("__PROJECT_BUILDTIME"),
    
    SITENAME("__SITENAME"),
    COPYRIGHT_HOLDER("__COPYRIGHT_HOLDER"),
    ADMIN_DASHBOARD_BRAND_ICON("__ADMIN_DASHBOARD_BRAND_ICON"),
    ADMIN_DASHBOARD_BRAND_TEXT("__ADMIN_DASHBOARD_BRAND_TEXT"),
    
    FRAMEWORK_VERSION("__FRAMEWORK_VERSION"),
    
    LOCALE_COOKIE_NAME("__LOCALE_COOKIE_NAME"),
    LOCALE("__LOCALE"),
    
    FILE_DOWNLOAD_PATH_ATTR("__FILE_DOWNLOAD_PATH"),
    IMAGE_DOWNLOAD_PATH_ATTR("__IMAGE_DOWNLOAD_PATH"),
    VIDEO_DOWNLOAD_PATH_ATTR("__VIDEO_DOWNLOAD_PATH"),
    FILE_UPLOAD_ACTION_ATTR("__FILE_UPLOAD_ACTION"),
    
    USER_INFO("__USER_INFO");
    
    private String value;
    
    EulerSysAttributes(String value) {
        this.value = value;
    }
    
    public String value() {
        return this.value;
    }
    
    public static Set<String> getEulerSysAttributeNames() {
        Class<EulerSysAttributes> clz = EulerSysAttributes.class;
        EulerSysAttributes[] eulerSysAttributes= clz.getEnumConstants();
        return Arrays.asList(eulerSysAttributes).stream().map(eulerSysAttribute -> eulerSysAttribute.value()).collect(Collectors.toSet());
    }
}
