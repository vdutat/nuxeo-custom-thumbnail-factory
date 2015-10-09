package org.nuxeo.ecm.platform.thumbnail.factories;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailFactory;
import org.nuxeo.ecm.platform.thumbnail.ThumbnailConstants;
import org.nuxeo.runtime.api.Framework;

public class CustomThumbnailFactory implements ThumbnailFactory {

    private static final Log LOG = LogFactory.getLog(CustomThumbnailFactory.class);

    @Override
    public Blob getThumbnail(DocumentModel doc, CoreSession session) throws ClientException {
        String propertyName = Framework.getProperty("nuxeo.thumbnail." + doc.getType() + ".metadata", ThumbnailConstants.THUMBNAIL_PROPERTY_NAME);
//        propertyName = "SUPNXP-14786_sch_imdc_user:photo";
        Blob thumbnailBlob = null;
        try {
            thumbnailBlob = (Blob) doc.getPropertyValue(propertyName);
        } catch (ClientException e) {
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
