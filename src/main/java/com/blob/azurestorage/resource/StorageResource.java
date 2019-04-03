package com.blob.azurestorage.resource;

import com.blob.azurestorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageResource {


    @Autowired
    StorageService storageService;

    @GetMapping(path ="/upload")
    public ResponseEntity uploadFile(@RequestParam String containerName, @RequestParam String fileName){

        return ResponseEntity.ok(storageService.uploadFile(containerName,fileName));

    }

    @GetMapping(path ="/download")
    public ResponseEntity downloadFile(@RequestParam String path, @RequestParam String containerName ,@RequestParam String fileName){

        return ResponseEntity.ok(storageService.downloadFile(path,containerName,fileName));

    }

    @GetMapping(path ="/delete")
    public ResponseEntity deleteFile(@RequestParam String containerName, @RequestParam String fileName){

        return ResponseEntity.ok(storageService.deleteFile(containerName,fileName));

    }

}
