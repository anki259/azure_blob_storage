package com.blob.azurestorage.service;

import com.blob.azurestorage.util.StorageUtil;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.microsoft.rest.v2.RestException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;

@Service
public class StorageService {

    public static final Logger logger = LoggerFactory.getLogger( StorageService.class);

    @Autowired
    private Environment environment;


    public String uploadFile(String containerName, String fileName){


        File sampleFile = new File("/Users/a0c019t/Documents/AzureBLOBStorage/"+fileName);
        ContainerURL containerURL;
        try {
            SharedKeyCredentials creds = new SharedKeyCredentials(environment.getProperty("storage.accountname"), environment.getProperty("storage.key"));
            final ServiceURL serviceURL = new ServiceURL(new URL("https://" + environment.getProperty("storage.accountname") + ".blob.core.windows.net"), StorageURL.createPipeline(creds, new PipelineOptions()));
            containerURL = serviceURL.createContainerURL(containerName);

             try {
                    ContainerCreateResponse response = containerURL.create(null, null, null).blockingGet();
                    System.out.println("Container Create Response is " + response.statusCode());
                    } catch (RestException e){
                        if (e instanceof RestException && ((RestException)e).response().statusCode() != 409) {
                            throw e;
                        } else {
                            System.out.println(containerName + "Container already exists, resuming...");
                        }
                    }
             final BlockBlobURL blobURL = containerURL.createBlockBlobURL(fileName);
             StorageUtil.uploadFile(blobURL,sampleFile);
            } catch (InvalidKeyException e) {
                logger.error("Invalid Storage account name/key provided",e);
            } catch (MalformedURLException e) {
                logger.error("Invalid URI provided",e);
            } catch (RestException e){
                logger.error("Service error returned: ",e);
            } catch (IOException e) {
                logger.error("IOException", e);

            }
        return "Uploaded Successfully";
    }


    public String downloadFile(String path,String containerName,String fileName){

        ContainerURL containerURL;
        try {

            SharedKeyCredentials creds = new SharedKeyCredentials(environment.getProperty("storage.accountname"), environment.getProperty("storage.key"));
            final ServiceURL serviceURL = new ServiceURL(new URL("https://" + environment.getProperty("storage.accountname") + ".blob.core.windows.net"), StorageURL.createPipeline(creds, new PipelineOptions()));
            containerURL = serviceURL.createContainerURL(containerName);
            final BlockBlobURL blobURL = containerURL.createBlockBlobURL(fileName);
            StorageUtil.downloadFile(blobURL,path);

        }catch (InvalidKeyException e) {
            System.out.println("Invalid Storage account name/key provided");
        }catch (MalformedURLException e) {
            System.out.println("Invalid URI provided");
        }catch (IOException e) {
            logger.error("IOException", e);

        }
        return "Downloaded Successfully";

    }


    public String deleteFile(String containerName,String fileName) {

        try {
            SharedKeyCredentials creds = new SharedKeyCredentials(environment.getProperty("storage.accountname"), environment.getProperty("storage.key"));
            final ServiceURL serviceURL = new ServiceURL(new URL("https://" + environment.getProperty("storage.accountname") + ".blob.core.windows.net"), StorageURL.createPipeline(creds, new PipelineOptions()));
            ContainerURL containerURL = serviceURL.createContainerURL(containerName);
            final BlockBlobURL blobURL = containerURL.createBlockBlobURL(fileName);
            StorageUtil.deleteFile(blobURL);

        } catch (InvalidKeyException e) {
            System.out.println("Invalid Storage account name/key provided");
        } catch (MalformedURLException e) {
            System.out.println("Invalid URI provided");
        }
          return "Deleted Successfully";
    }


}
