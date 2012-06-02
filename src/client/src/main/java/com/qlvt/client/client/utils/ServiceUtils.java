/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.client.client.utils;

import com.qlvt.core.system.SystemUtil;

/**
 * The Class ServiceUtils.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/11, 9:34 AM
 */
public class ServiceUtils {

    private ServiceUtils() {

    }

    public static String getServiceEntryPoint() {
        if (SystemUtil.isProductionMode()) {
            return SystemUtil.getServerBaseUrl()
                    + SystemUtil.getConfiguration().serverServletRootPath();
        }
        //Set proxy servlet for development mode, to split up gwt server and gwt client to 2 projects.
        return SystemUtil.getConfiguration().developmentModeClientBaseUrl()
                + SystemUtil.getConfiguration().developmentModeClientProxyPath();
    }

    public static void configureServiceEntryPoint(Class<?> clazz, Object ourInstance) {
    }
}