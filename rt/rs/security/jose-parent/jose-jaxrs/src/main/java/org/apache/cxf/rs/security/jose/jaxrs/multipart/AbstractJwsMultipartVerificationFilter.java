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
package org.apache.cxf.rs.security.jose.jaxrs.multipart;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.MultipartInputFilter;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.jaxrs.utils.multipart.AttachmentUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.jose.jws.JwsSignatureVerifier;

public abstract class AbstractJwsMultipartVerificationFilter {
    
    private JwsSignatureVerifier sigVerifier;
    private boolean supportSinglePartOnly = true;
    
    public void setSigVerifier(JwsSignatureVerifier sigVerifier) {
        this.sigVerifier = sigVerifier;
    }
    public void setSupportSinglePartOnly(boolean supportSinglePartOnly) {
        this.supportSinglePartOnly = supportSinglePartOnly;
    }

    protected void addMultipartFilterIfNeeded(MediaType contentType) {
        if (contentType != null && contentType.getType().equals("multipart")) {
            Message m = JAXRSUtils.getCurrentMessage();
            MultipartInputFilter jwsFilter = sigVerifier == null 
                ? new JwsMultipartSignatureInFilter(m, supportSinglePartOnly) 
                : new JwsMultipartSignatureInFilter(m, sigVerifier, supportSinglePartOnly); 
            AttachmentUtils.addMultipartInFilter(jwsFilter); 
        }
        
    }
}
