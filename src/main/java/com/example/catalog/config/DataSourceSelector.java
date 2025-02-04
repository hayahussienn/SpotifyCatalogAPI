package com.example.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.services.DatabaseDataSourceService;
import com.example.catalog.services.JSONDataSourceService;

@Configuration
public class DataSourceSelector {

    @Value("${datasource.type}")
    private String dataSourceType;

    @Bean
    public DataSourceService dataSourceService(JSONDataSourceService jsonDataSourceService,
                                               DatabaseDataSourceService databaseDataSourceService) {
        if ("json".equalsIgnoreCase(dataSourceType)) {
            return jsonDataSourceService;
        } else if ("database".equalsIgnoreCase(dataSourceType)) {
            return databaseDataSourceService;
        } else {
            throw new IllegalArgumentException("Invalid data source type: " + dataSourceType);
        }
    }
}
