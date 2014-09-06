package com.radcortez.wow.auctions.configuration;

import com.radcortez.wow.auctions.entity.FolderType;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import java.util.*;

import static com.radcortez.wow.auctions.entity.FolderType.*;

/**
 * @author Roberto Cortez
 */
@Singleton
@Named
@SuppressWarnings("unchecked")
public class ConfigurationFactory {
    private Map<String, Object> configurations;

    @PostConstruct
    public void initConfiguration() {
        configurations = new HashMap<>();

        Map<String, List<FolderType>> folders = new HashMap<>();
        folders.put("batch/work/", Arrays.asList(FI_TMP, FO_TMP));
        folders.put("batch/files/", Arrays.asList(FI, FO, FE));

        List<String> fileExtensions = new ArrayList<>();
        fileExtensions.add("dat");

        configurations.put("batchHome", "/Users/radcortez/Documents/Code/personal/wow-auctions/data");
        configurations.put("folders", folders);
        configurations.put("fileExtensions", fileExtensions);
    }

    @Produces
    @Configuration
    public String getString(InjectionPoint injectionPoint) {
        return (String) configurations.get(injectionPoint.getMember().getName());
    }

    @Produces
    @Configuration
    public <E> List<E> getList(InjectionPoint injectionPoint) {
        return (List) configurations.get(injectionPoint.getMember().getName());
    }

    @Produces
    @Configuration
    public <K,V> Map<K,V> getMap(InjectionPoint injectionPoint) {
        return (Map) configurations.get(injectionPoint.getMember().getName());
    }
}
