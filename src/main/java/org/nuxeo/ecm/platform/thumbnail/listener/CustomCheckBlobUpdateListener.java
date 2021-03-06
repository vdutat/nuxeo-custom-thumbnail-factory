/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     vdutat
 */
package org.nuxeo.ecm.platform.thumbnail.listener;

import static org.nuxeo.ecm.core.api.event.DocumentEventTypes.BEFORE_DOC_UPDATE;
import static org.nuxeo.ecm.core.api.event.DocumentEventTypes.DOCUMENT_CREATED;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.thumbnail.ThumbnailConstants;
import org.nuxeo.runtime.api.Framework;

public class CustomCheckBlobUpdateListener implements EventListener {

    @Override
    public void handleEvent(Event event) {
        EventContext ec = event.getContext();
        if (!(ec instanceof DocumentEventContext)) {
            return;
        }
        DocumentEventContext context = (DocumentEventContext) ec;
        DocumentModel doc = context.getSourceDocument();
        String[] docTypes = StringUtils.split(Framework.getProperty("nuxeo.thumbnail.update.listener.doctypes"), ",");
        if (!Arrays.asList(docTypes).contains(doc.getType())) {
            return;
        }
        Property content = doc.getProperty(Framework.getProperty("nuxeo.blobholder." + doc.getType() + ".metadata", "file:content"));
        if (DOCUMENT_CREATED.equals(event.getName()) || content.isDirty()) {
            if (BEFORE_DOC_UPDATE.equals(event.getName()) && doc.hasFacet(ThumbnailConstants.THUMBNAIL_FACET)) {
                doc.setPropertyValue(ThumbnailConstants.THUMBNAIL_PROPERTY_NAME, null);
            }
            if (content.getValue() != null) {
                doc.addFacet(ThumbnailConstants.THUMBNAIL_FACET);
                Framework.getLocalService(EventService.class).fireEvent(
                        ThumbnailConstants.EventNames.scheduleThumbnailUpdate.name(), context);
            }
        }
    }
}
