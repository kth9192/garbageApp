package com.taehoon.garbagealarm;

import android.app.Application;

import com.taehoon.garbagealarm.repository.configrepository.ConfigRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigSingleton {

    private static ConfigRepository configRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ConfigSingleton() {
    }

    private static class SingletonHolder{
        private static final ConfigSingleton INSTANCE = new ConfigSingleton();
    }

    public static ConfigSingleton getInstance(Application application){
        configRepository = new ConfigRepository(application);
        return SingletonHolder.INSTANCE;
    }

    public ConfigRepository getConfigRepository() {
        return configRepository;
    }
}
