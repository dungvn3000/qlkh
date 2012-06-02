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

package com.qlvt.server.transaction;

import com.qlvt.core.client.exception.ExceptionHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;

/**
 * The Class TransactionInterceptor.
 *
 * @author Nguyen Duc Dung
 * @since 1/29/12, 11:23 AM
 */
public class TransactionInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //Open Session and begin transaction.
        Session session = null;
        session.beginTransaction();
        Object result = null;

        //Commit and close session.
        try {
            result = invocation.proceed();
            session.flush();
        } catch (Exception ex) {
            ExceptionHandler handler = invocation.getMethod().getAnnotation(ExceptionHandler.class);
            if (handler != null) {
                Class<? extends Exception> expectException = handler.expectException();
                Class<? extends Exception> wrapperException = handler.wrapperException();
                if (ex.getClass() == expectException) {
                    throw wrapperException.newInstance();
                }
            } else {
                //Throw original exception, if exception handler wasn't define.
                throw ex;
            }
        } finally {
            session.close();
        }
        return result;
    }
}
