# AzureBlobContainerDelete Action

Description
-----------

Delete a container on Azure Storage Blob service.

Use Case
--------

This action may be used to delete a container on Azure Blob service.

Properties
----------

**accountName:** The Microsoft Azure Storage account name. (Macro-enabled)

**accountKey:** The account key for the specified Azure Storage account name. (Macro-enabled)

**container:** The container to be deleted. (Macro-enabled)

Example
-------

This example moves container 'myContainer'.

    {
        "name": "AzureDeleteAction",
        "type": "action",
        "properties": {
            "accountName": "myStorageAccountNameInAzure",
            "accountKey": "myStorageAccountKey+b2EN6SVpcg==",
            "container": "myContainer"
        }
    }
