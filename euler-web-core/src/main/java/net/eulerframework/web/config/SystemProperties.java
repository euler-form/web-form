/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.eulerframework.web.config;

import net.eulerframework.common.util.property.PropertyNotFoundException;
import net.eulerframework.common.util.property.PropertyReader;

/**
 * 用户获取系统参数
 * @author cFrost
 *
 */
public final class SystemProperties {

    private final static PropertyReader properties = new PropertyReader("/system.properties");
    
    public static String frameworkVersion() {
        try {
            return properties.get("version");
        } catch (PropertyNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
