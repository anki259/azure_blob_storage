package com.blob.azurestorage.util;

import com.microsoft.azure.storage.blob.*;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class StorageUtil {

    public static String uploadFile(BlockBlobURL blob, File sourceFile) throws IOException {

        Path path = sourceFile.toPath();
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        TransferManager.uploadFileToBlockBlob(fileChannel, blob, 8 * 1024 * 1024, null).subscribe(response -> {
            System.out.println("Completed download request.");
            System.out.println("The blob was uploaded");
        });
        return String.valueOf("uploaded succesfully");
    }

    public static void downloadFile(BlockBlobURL blob, String path) throws IOException {

        Path sourchFilePath = Paths.get(path);
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(sourchFilePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        TransferManager.downloadBlobToFile(fileChannel, blob, null, null)
                .subscribe(response -> {
                    System.out.println("Completed download request.");
                    System.out.println("The blob was downloaded to " + sourchFilePath);
                });
       }

    public static void deleteFile(BlockBlobURL blobURL) {

        blobURL.delete(null, null, null)
                .subscribe(
                        response -> System.out.println(">> Blob deleted: " + blobURL),
                        error -> System.out.println(">> An error encountered during deleteBlob: " + error.getMessage()));
    }


}
