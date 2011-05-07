package com.beecas;

import com.beecas.service.CacheService;
import com.beecas.service.DatabaseService;
import com.beecas.service.FriendService;
import com.beecas.service.ICacheService;
import com.beecas.service.IDatabaseService;
import com.beecas.service.IFriendService;
import com.beecas.service.IPSService;
import com.beecas.service.ISubscriberService;
import com.beecas.service.IUserService;
import com.beecas.service.IVersionService;
import com.beecas.service.PSService;
import com.beecas.service.SubscriberService;
import com.beecas.service.UserService;
import com.beecas.service.VersionService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class BeecasModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ICacheService.class).to(CacheService.class).in(Scopes.SINGLETON);
        bind(IUserService.class).to(UserService.class).in(Scopes.SINGLETON);
        bind(IDatabaseService.class).to(DatabaseService.class).in(Scopes.SINGLETON);
        bind(IFriendService.class).to(FriendService.class).in(Scopes.SINGLETON);
        bind(ISubscriberService.class).to(SubscriberService.class).in(Scopes.SINGLETON);
        bind(IVersionService.class).to(VersionService.class).in(Scopes.SINGLETON);
        bind(IPSService.class).to(PSService.class).in(Scopes.SINGLETON);
    }

}
