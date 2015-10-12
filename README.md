nuxeo-custom-thumbnail-factories
===

## About this module

TODO

### Contribution to `thumbnailFactory`

Consider `DocType1` being the document type for which you want to render in the `thumbnail` view:

```xml
<extension target="org.nuxeo.ecm.core.api.thumbnail.ThumbnailService"
  point="thumbnailFactory">
  <thumbnailFactory name="DocType1_ThumbnailFactory"
    docType="DocType1"
    factoryClass="org.nuxeo.ecm.platform.thumbnail.factories.CustomThumbnailFactory" />
</extension>
```

### Configuration variable in `nuxeo.conf`

Consider `DocType1` being the document type and `schema1:image` being the `Blob` metadata for which you want to render in the `thumbnail` view:

```
nuxeo.thumbnail.DocType1.metadata=schema1:image
```

## Building

```
mvn clean install
```

## Using

All you have to do is:

 - copy the bundle in `nxserver/plugins` or `nxserver/bundles`
 - contribute a `ThumbnailFactory` for your custom document type using the factory class `org.nuxeo.ecm.platform.thumbnail.factories.CustomThumbnailFactory`
 - add a configuration variable in `nuxeo.conf` to define the metadata for your custom type containing the blob to render in the thumbnail view
 - restart the server
