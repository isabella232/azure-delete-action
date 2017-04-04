/*
 * Copyright © 2017 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package co.cask.hydrator.plugin.batch;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.plugin.PluginConfig;
import co.cask.cdap.etl.api.action.Action;
import co.cask.cdap.etl.api.action.ActionContext;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action to delete a container on Azure Storage Blob service
 */

@Plugin(type = Action.PLUGIN_TYPE)
@Name("AzureDelete")
@Description("Action to delete a container on Microsoft Azure Blob service.")
public class AzureDeleteAction extends Action{
  private static final Logger LOG = LoggerFactory.getLogger(AzureDeleteAction.class);

  private AzureDeleteActionConfig config;

  public AzureDeleteAction(AzureDeleteActionConfig config) {
    this.config = config;
  }

  @Override
  public void run(ActionContext context) throws Exception {
    /**
     * Stores the storage connection string.
     */
    final String storageConnectionString = "DefaultEndpointsProtocol=https;"
      + "AccountName=" + config.accountName + ";"
      + "AccountKey=" + config.accountKey;

    // Setup the cloud storage account.
    CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);

    // Create a blob service client
    CloudBlobClient blobClient = account.createCloudBlobClient();

    try {
      CloudBlobContainer deletedContainer = blobClient.getContainerReference(config.container);

      //Delete the container
      if (deletedContainer.deleteIfExists()) {
        LOG.info("The container {} existed and was deleted.", config.container);
      } else {
        LOG.info("The container {} does not exist or can not be deleted.", config.container);
      }

    } catch (Exception e) {
      LOG.error("Error when deleting the container {}", config.container);
      throw e;
    }
  }


  /**
   *  Config for the action to delete a container on Azure Storage Blob service
   */
  public class AzureDeleteActionConfig extends PluginConfig {
    @Description("The Microsoft Azure Storage account name.")
    @Macro
    private String accountName;

    @Description("The account key for the specified Azure Storage account name.")
    @Macro
    private String accountKey;

    @Description("The container to be deleted.")
    @Macro
    private String container;

    public AzureDeleteActionConfig(String accountName, String accountKey, String container) {
      this.accountName = accountName;
      this.accountKey = accountKey;
      this.container = container;
    }
  }

}
