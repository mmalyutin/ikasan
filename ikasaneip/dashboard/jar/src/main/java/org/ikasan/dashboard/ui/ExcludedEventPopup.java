/*
 * $Id$  
 * $URL$
 * 
 * ====================================================================
 * Ikasan Enterprise Integration Platform
 * 
 * Distributed under the Modified BSD License.
 * Copyright notice: The copyright for this software and a full listing 
 * of individual contributors are as shown in the packaged copyright.txt 
 * file. 
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 *  - Neither the name of the ORGANIZATION nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
package org.ikasan.dashboard.ui;

import org.ikasan.dashboard.ui.topology.panel.ExclusionEventViewPanel;
import org.ikasan.spec.error.reporting.ErrorOccurrence;
import org.ikasan.spec.exclusion.ExclusionEvent;
import org.ikasan.hospital.model.ExclusionEventAction;
import org.ikasan.hospital.model.ModuleActionedExclusionCount;
import org.ikasan.spec.hospital.service.HospitalManagementService;
import org.ikasan.spec.hospital.service.HospitalService;
import org.ikasan.spec.error.reporting.ErrorReportingManagementService;
import org.ikasan.topology.service.TopologyService;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

/**
 * 
 * @author Ikasan Development Team
 *
 */
@Theme("dashboard")
public class ExcludedEventPopup extends UI
{
	/* (non-Javadoc)
	 * @see com.vaadin.ui.UI#initCloud(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request)
	{		
		ExclusionEvent exclusionEvent 
		 	= (ExclusionEvent)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("exclusionEvent");
		ErrorOccurrence errorOccurrence
		 	= (ErrorOccurrence)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("errorOccurrence");
		ExclusionEventAction exclusionEventAction
	 		= (ExclusionEventAction)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("exclusionEventAction");
		
		HospitalManagementService<ExclusionEventAction, ModuleActionedExclusionCount> hospitalManagementService
 			= (HospitalManagementService<ExclusionEventAction, ModuleActionedExclusionCount>)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("hospitalManagementService");
 		
		TopologyService topologyService
 			= (TopologyService)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("topologyService");
		
		ErrorReportingManagementService errorReportingManagementService = (ErrorReportingManagementService)VaadinService.getCurrentRequest().getWrappedSession()
				.getAttribute("errorReportingManagementService");
		
		
		HospitalService hospitalService = (HospitalService)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("hospitalService");
	        
		 ExclusionEventViewPanel panel = new ExclusionEventViewPanel(exclusionEvent, errorOccurrence, exclusionEventAction,
					hospitalManagementService,  topologyService, errorReportingManagementService, hospitalService);
		
		this.setContent(panel);
	}

}
