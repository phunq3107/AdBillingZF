package com.phunq.adbilling;

import com.phunq.rpc.adbilling.AdBillingService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("rpc")
@Component
public class AdBillingRpcServer implements CommandLineRunner, Runnable {

    private TServer server;
    @Value("${server.port}")
    private Integer serverPort;

    private final AdBillingService.Iface adBillingService;

    public AdBillingRpcServer(AdBillingService.Iface adBillingService) {
        this.adBillingService = adBillingService;
    }

    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Thread serverThread = new Thread(this);
        serverThread.setPriority(Thread.MAX_PRIORITY);
        serverThread.start();
    }

    public void start() throws TTransportException {
        TServerTransport serverTransport = new TServerSocket(serverPort);
        server = new TSimpleServer(new TServer.Args(serverTransport)
                .processor(new AdBillingService.Processor<>(adBillingService)));
        System.out.println("Starting the Ad billing service server... ");
        server.serve();
        System.out.println("Done");
    }

}
