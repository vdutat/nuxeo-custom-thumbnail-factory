nuxeo-custom-thumbnail-factories
===

## About this module

TODO

### Option 1: use original blob as thumbnail

Use this option if you consider that the original blob can be used "as it is" to be rendered as thumbnail.

#### Contribution to `thumbnailFactory`

Consider `DocType1` being the document type for which you want to render a thumbnail image in the `thumbnail` view:

```xml
<extension target="org.nuxeo.ecm.core.api.thumbnail.ThumbnailService"
  point="thumbnailFactory">
  <thumbnailFactory name="DocType1_ThumbnailFactory"
    docType="DocType1"
    factoryClass="org.nuxeo.ecm.platform.thumbnail.factories.CustomThumbnailFactory" />
</extension>
```

#### Configuration variable in `nuxeo.conf`

Consider `DocType1` being the document type and `schema1:image` being the `Blob` metadata for which you want to render a thumbnail image in the `thumbnail` view:

```
nuxeo.thumbnail.DocType1.metadata=schema1:image
```

### Option 2: generate and store thumbnail blob in separate metadata

Use this option if you need to transform the original blob and store the resulting blob in a separate metadata.
**ATTENTION** By default, the Nuxeo Platform does not re-generate `thumbnail` blob for document types other than `File`, a custom listener is defined in this plugin to /create/update `thumbnail` blob for custom doument types (see below).

#### Contribution to `BlobHolderFactory`

Contribute a `BlobHolderFactory` for your custom document type as the `thumbnail service` relies on it to retrieve the source blob to generate the `thumbnail` image.

Consider `DocType1` being the document type for which you want to render a thumbnail image in the `thumbnail` view:

```xml
<extension target="org.nuxeo.ecm.core.api.blobholder.BlobHolderAdapterComponent"
  point="BlobHolderFactory">
  <blobHolderFactory name="DocType1_BlobHolderFactory" 
    docType="DocType1"
    class="org.nuxeo.ecm.platform.blobholder.factories.CustomBlobHolderFactory" />
</extension>
```

#### Contribution to `thumbnailFactory`

Consider `DocType1` being the document type for which you want to render a thumbnail image in the `thumbnail` view:

```xml
<extension target="org.nuxeo.ecm.core.api.thumbnail.ThumbnailService"
  point="thumbnailFactory">
  <thumbnailFactory name="DocType1_ThumbnailFactory"
    docType="DocType1"
    factoryClass="org.nuxeo.ecm.platform.thumbnail.factories.ThumbnailDocumentFactory" />
</extension>
```

#### Configuration variable in `nuxeo.conf`

Consider `DocType1` being the document type and `schema1:image` being the `Blob` metadata for which you want to render in the `thumbnail` view:

```
nuxeo.blobholder.DocType1.metadata=schema1:image
nuxeo.thumbnail.update.listener.doctypes=DocType1,
```

`nuxeo.blobholder.DocType1.metadata` defines the metadata for your custom type containing the blob to render in the thumbnail view. 
`nuxeo.thumbnail.update.listener.doctypes` defines document type(s) for which the custom listener will generate the `thumbnail` blob at creation and modification time.
n 
#### Listener to update `thumnail` blob

A listener is included in this plugin to create/update `thumbnail` blob for document types mentioned in value of configuration variable `nuxeo.thumbnail.update.listener.doctypes` (see above).

## Building

```
mvn clean install
```

## Using

### Option 1

- copy the bundle in `nxserver/plugins` or `nxserver/bundles`
- contribute a `ThumbnailFactory` for your custom document type using the factory class `org.nuxeo.ecm.platform.thumbnail.factories.CustomThumbnailFactory` (defined in this plugin)
- add a configuration variable in `nuxeo.conf` to define the metadata for your custom type containing the blob to render in the thumbnail view
- restart the server
 
### Option 2

- copy the bundle in `nxserver/plugins` or `nxserver/bundles`
- contribute 2 factories for your custom document type:
  - a `ThumbnailFactory` using the standard factory class `org.nuxeo.ecm.platform.thumbnail.factories.ThumbnailDocumentFactory`
  - a `blobHolderFactory` using the factory class `org.nuxeo.ecm.platform.blobholder.factories.CustomBlobHolderFactory` (defined in this plugin)
- add configuration variables `nuxeo.blobholder.DocType1.metadata` and `nuxeo.thumbnail.update.listener.doctypes` in `nuxeo.conf`
- restart the server

