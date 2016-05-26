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
package org.nuxeo.ecm.platform.thumbnail.factories;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PropertyException;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailFactory;
import org.nuxeo.ecm.platform.thumbnail.ThumbnailConstants;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.services.config.ConfigurationService;

public class CustomThumbnailFactory implements ThumbnailFactory {

    private static final Log LOG = LogFactory.getLog(CustomThumbnailFactory.class);

    @Override
    public Blob getThumbnail(DocumentModel doc, CoreSession session) {
        String propertyName = Framework.getService(ConfigurationService.class).getProperty("nuxeo.thumbnail." + doc.getType() + ".metadata", ThumbnailConstants.THUMBNAIL_PROPERTY_NAME);
//        propertyName = "SUPNXP-14786_sch_imdc_user:photo";
        Blob thumbnailBlob = null;
        try {
            thumbnailBlob = (Blob) doc.getPropertyValue(propertyName);
        } catch (PropertyException e) {
            LOG.warn("Could not fetch the thumbnail blob from property " + propertyName, e);
        }
        if (thumbnailBlob == null) {
            thumbnailBlob = getDefaultThumbnail(doc);
        }
        return thumbnailBlob;
    }

    @Override
    public Blob computeThumbnail(DocumentModel doc, CoreSession session) {
         return null;
    }

    protected Blob getDefaultThumbnail(DocumentModel doc) {
        if (doc == null) {
            return null;
        }
        String iconPath = "/icons/missing_avatar.png";
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) {
            return null;
        }
        try {
            InputStream iconStream = ctx.getExternalContext().getResourceAsStream(iconPath);
            if (iconStream != null) {
                return new FileBlob(iconStream);
            }
        } catch (IOException e) {
            LOG.warn(String.format("Could not fetch the thumbnail blob from icon path '%s'", iconPath), e);
        }
        return null;
    }

}
