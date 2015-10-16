package org.nuxeo.ecm.platform.blobholder.factories;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.BlobHolderFactory;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;
import org.nuxeo.runtime.api.Framework;

public class CustomBlobHolderFactory implements BlobHolderFactory {

    @Override
    public BlobHolder getBlobHolder(DocumentModel doc) {
        String propertyName = Framework.getProperty("nuxeo.blobholder." + doc.getType() + ".metadata", "file:content");
        DocumentBlobHolder blobHolder = new DocumentBlobHolder(doc, propertyName);
        return blobHolder;
    }

}
