//package com.CollabIDE.bootstrap;
//
//import com.CollabIDE.Services.SessionService;
//import com.CollabIDE.session.SessionState;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestDataLoader implements CommandLineRunner {
//
//    private final SessionService sessionService;
//
//    public TestDataLoader(SessionService sessionService) {
//        this.sessionService = sessionService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        SessionState state=sessionService.createSession("user1");
//
//        System.out.println("=================================");
//        System.out.println("SESSION ID = " + state.getSession().getSessionId());
//        System.out.println("=================================");
//
//    }
//}
