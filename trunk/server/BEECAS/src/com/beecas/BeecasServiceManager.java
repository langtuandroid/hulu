package com.beecas;

import com.google.inject.Injector;


public class BeecasServiceManager {
    
    private static Injector injector;
    
    public BeecasServiceManager(Injector injector) {
        BeecasServiceManager.injector = injector;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<?> interfaceClass) {
        return (T) injector.getInstance(interfaceClass);
    }
}
