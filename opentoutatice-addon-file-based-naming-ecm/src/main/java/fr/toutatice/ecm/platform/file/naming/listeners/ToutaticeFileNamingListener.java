/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *
 * Contributors:
 *   dchevrier
 *    
 */
package fr.toutatice.ecm.platform.file.naming.listeners;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import fr.toutatice.ecm.platform.core.constants.ToutaticeNuxeoStudioConst;
import fr.toutatice.ecm.platform.core.helper.ToutaticeDocumentHelper;
import fr.toutatice.ecm.platform.file.naming.constants.FileNamingConstants;
import fr.toutatice.ecm.platform.file.naming.helper.ToutaticeFileNamingHelper;

/**
 * 
 * @author David Chevrier.
 *
 */
public class ToutaticeFileNamingListener implements EventListener {

    @Override
    public void handleEvent(Event event) throws ClientException {
        if (event.getContext() instanceof DocumentEventContext) {
            DocumentEventContext ctx = (DocumentEventContext) event.getContext();
            CoreSession session = ctx.getCoreSession();
            DocumentModel document = ctx.getSourceDocument();
            String eventName = event.getName();
            
            if(DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)
                    || DocumentEventTypes.DOCUMENT_UPDATED.equals(eventName)){
                
                if(FileNamingConstants.SUPPORTED_DOC_TYPES.contains(document.getType())){
                    
                    String title = (String) document.getPropertyValue(ToutaticeNuxeoStudioConst.CST_DOC_XPATH_NUXEO_DC_TITLE);
                    String fileName = ToutaticeFileNamingHelper.getDocFileName(document);
                    
                    if(StringUtils.isBlank(title) && StringUtils.isNotBlank(fileName)){
                        document.setPropertyValue(ToutaticeNuxeoStudioConst.CST_DOC_XPATH_NUXEO_DC_TITLE, fileName);
                        ToutaticeDocumentHelper.saveDocumentSilently(session, document, true);
                    }
                    
                }
                
            } 
            
        }
        
    }
    
}
