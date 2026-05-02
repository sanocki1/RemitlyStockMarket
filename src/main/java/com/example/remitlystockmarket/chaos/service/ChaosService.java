package com.example.remitlystockmarket.chaos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChaosService {

    private final String instanceName;

    public ChaosService(@Value("${INSTANCE_NAME:${HOSTNAME:unknown-instance}}") String instanceName) {
        this.instanceName = instanceName;
    }

    public String killCurrentInstance() {
        Thread killer = new Thread(() -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });

        killer.setDaemon(false);
        killer.start();

        return instanceName;
    }
}