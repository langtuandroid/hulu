package com.beecas;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class BeecasServer {
    private BeecasServiceManager beecasServiceManager;
    
    public BeecasServer() {
        
        Injector injector = Guice.createInjector(new BeecasModule());
        beecasServiceManager = new BeecasServiceManager(injector);
    }
}
