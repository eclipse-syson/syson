/*******************************************************************************
 * Copyright (c) 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.util;

import java.util.List;

/**
 * Tooling around AQL expressions.
 *
 * @author Jerome Gout
 */
public class AQLUtils {

    /**
     * Return an AQL string from the given string.
     *
     * @param string
     *            the string to transform to an AQL string
     * @return the AQL string built upon the given string.
     */
    public static String aqlString(String string) {
        return '\'' + string + '\'';
    }

    /**
     * Returns the AQL expression for calling a service without any parameter using <code>self</code> as the
     * instance.<br>
     * For instance: <code>aql:self.myService()</code>
     *
     * @param serviceName
     *            The name of the service to call.
     * @return An AQL expression (<code>String</code>) expressing the call of the given service.
     */
    public static String getSelfServiceCallExpression(String serviceName) {
        return getSelfServiceCallExpression(serviceName, List.of());
    }

    /**
     * Returns the AQL expression for calling a service with one parameter using <code>self</code> as the instance.<br>
     * For instance: <code>aql:self.myService(sysml::Package)</code>
     *
     * @param serviceName
     *            The name of the service to call.
     * @param parameter
     *            The unique parameter.
     * @return An AQL expression (<code>String</code>) expressing the call of the given service.
     */
    public static String getSelfServiceCallExpression(String serviceName, String parameter) {
        return getSelfServiceCallExpression(serviceName, List.of(parameter));
    }

    /**
     * Returns the AQL expression for calling a service with several parameters using <code>self</code> as the
     * instance.<br>
     * For instance: <code>aql:self.myService(sysml::Package, newValue)</code>
     *
     * @param serviceName
     *            The name of the service to call.
     * @param parameter
     *            The list of parameters.
     * @return An AQL expression (<code>String</code>) expressing the call of the given service.
     */
    public static String getSelfServiceCallExpression(String serviceName, List<String> parameters) {
        return AQLConstants.AQL_SELF + '.' + serviceName + '(' + String.join(",", parameters) + ')';
    }

    /**
     * Returns the AQL expression for calling a service without any parameter.
     *
     * @param var
     *            The part of the expression specifying the variable on which the service should be called.
     * @param serviceName
     *            The name of the service to call.
     * @return An AQL expression (<code>String</code>) expressing the call of the given service.
     */
    public static String getServiceCallExpression(String var, String serviceName) {
        return AQLUtils.getServiceCallExpression(var, serviceName, List.of());
    }

    /**
     * Returns the AQL expression for calling a service with one parameter.
     *
     * @param var
     *            The part of the expression specifying the variable on which the service should be called.
     * @param serviceName
     *            The name of the service to call.
     * @param parameter
     *            The unique parameter.
     * @return An AQL expression (<code>String</code>) expressing the call of the given service.
     */
    public static String getServiceCallExpression(String var, String serviceName, String parameter) {
        return AQLUtils.getServiceCallExpression(var, serviceName, List.of(parameter));
    }

    /**
     * Returns the AQL expression for calling a service with several parameters.
     *
     * @param var
     *            The part of the expression specifying the variable on which the service should be called.
     * @param serviceName
     *            The name of the service to call.
     * @param parameter
     *            The list of parameters.
     * @return An AQL expression (<code>String</code>) expressing the call of the given service.
     */
    public static String getServiceCallExpression(String var, String serviceName, List<String> parameters) {
        return AQLConstants.AQL + var + '.' + serviceName + '(' + String.join(",", parameters) + ')';
    }

}
