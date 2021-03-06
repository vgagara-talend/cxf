/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.systest.ws.policy;

import org.apache.cxf.service.model.BindingFaultInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.ws.policy.attachment.external.DomainExpression;

public class JavaFirstUriDomainExpression implements DomainExpression {
    private final String url;

    public JavaFirstUriDomainExpression(final String url) {
        this.url = url;
    }

    @Override
    public boolean appliesTo(BindingFaultInfo paramBindingFaultInfo) {
        return false;
    }

    @Override
    public boolean appliesTo(BindingMessageInfo bmi) {
        String serviceName =
            bmi.getBindingOperation().getBinding().getService().getName().getLocalPart();

        if ("JavaFirstAttachmentPolicyService".equals(serviceName) && "usernamepassword".equals(url)) {
            return ("doInputMessagePolicy".equals(bmi.getBindingOperation().getName().getLocalPart())
                && MessageInfo.Type.INPUT.equals(bmi.getMessageInfo().getType()))
                || ("doOutputMessagePolicy".equals(bmi.getBindingOperation().getName().getLocalPart())
                && MessageInfo.Type.OUTPUT.equals(bmi.getMessageInfo().getType()));
        } else if ("SslUsernamePasswordAttachmentService".equals(serviceName)
            && "sslusernamepassword".equals(url)) {
            return MessageInfo.Type.INPUT.equals(bmi.getMessageInfo().getType());
        } else {
            return false;
        }
    }

    @Override
    public boolean appliesTo(BindingOperationInfo boi) {
        if ("usernamepassword".equals(url)) {
            return "doOperationLevelPolicy".equals(boi.getName().getLocalPart());
        } else {
            return false;
        }
    }

    @Override
    public boolean appliesTo(EndpointInfo paramEndpointInfo) {
        return false;
    }

    @Override
    public boolean appliesTo(ServiceInfo paramServiceInfo) {
        return false;
    }

}
