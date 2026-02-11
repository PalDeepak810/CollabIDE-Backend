//package com.CollabIDE.bootstrap;
//
//import com.CollabIDE.Services.FileStateService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//public class FileInitializer implements CommandLineRunner {
//    private final FileStateService fileStateService;
//
//    public FileInitializer(FileStateService fileStateService) {
//        this.fileStateService = fileStateService;
//    }
//
//    @Override
//    public void run(String... args){
//        UUID sessionId=UUID.fromString("dc53e606-14ce-48bf-ba3a-2bdf767a1e80");
//
//        fileStateService.createFile(
//                sessionId,
//                "main.txt",
//                "",
//                "user1"
//        );
//        System.out.println("main.txt created");
//    }
//}
