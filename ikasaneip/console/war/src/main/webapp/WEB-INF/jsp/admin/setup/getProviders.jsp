<%-- 

 $Id: home.jsp 2725 2009-12-09 19:30:08Z karianna $
 $URL: https://open.jira.com/svn/IKASAN/branches/console-redesign/ikasaneip/console/src/main/webapp/WEB-INF/jsp/home.jsp $ 

 ====================================================================
 Ikasan Enterprise Integration Platform
 
 Distributed under the Modified BSD License.
 Copyright notice: The copyright for this software and a full listing 
 of individual contributors are as shown in the packaged copyright.txt 
 file. 
 
 All rights reserved.

 Redistribution and use in source and binary forms, with or without 
 modification, are permitted provided that the following conditions are met:

  - Redistributions of source code must retain the above copyright notice, 
    this list of conditions and the following disclaimer.

  - Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.

  - Neither the name of the ORGANIZATION nor the names of its contributors may
    be used to endorse or promote products derived from this software without 
    specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ====================================================================

 Author:  Ikasan Development Team
 
--%>
<%@ include file="/WEB-INF/jsp/top.jsp" %>

        <div id="content">
            <h1><fmt:message key="setup_provider_heading"/></h1>
            <p class="text"><fmt:message key="setup_provider_heading2"/></p>

            <c:url var="providerLink" value="createProviderPersistence.htm" />
            <form action="${providerLink}" method="post">
                Select Persistence Provider :
                <select name="provider">
                    <c:forEach items="${providers}" var="provider">
                        <option value="${provider}">${provider}</option>
                    </c:forEach>
                </select>

                <input type="submit" value="<fmt:message key="setup_provider_next"  />" class="controlButton"/>
            </form>
            <p/>
            <form action="completed.htm">
                <input type="submit" value="<fmt:message key="setup_provider_skip"  />" class="controlButton"/>
            </form>
        </div>
<%@ include file="/WEB-INF/jsp/bottom.jsp"%>
