package com.example.catalog.config;

import com.example.catalog.services.JSONDataSourceService;
import com.example.catalog.services.SpotifyAPIDataSourceService;
import com.example.catalog.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceSelector {

    @Value("${datasource.type}")
    private String dataSourceType;

    @Autowired
    private JSONDataSourceService jsonDataSourceService;

    @Autowired
    private SpotifyAPIDataSourceService spotifyAPIDataSourceService;

    @Bean
    public DataSourceService dataSourceService() {
        try {
            System.out.println("Selecting data source: " + dataSourceType);

            if ("json".equalsIgnoreCase(dataSourceType)) {
                System.out.println("Using JSON data source.");
                return jsonDataSourceService;
            } else if ("spotify_api".equalsIgnoreCase(dataSourceType)) {
                System.out.println("Using Spotify API data source.");
                return spotifyAPIDataSourceService;
            } else {
                throw new IllegalArgumentException("Unsupported data source type: " + dataSourceType);
            }
        } catch (Exception e) {
            System.out.println("Error initializing data source: " + e.getMessage());
            throw e;
        }
    }
}
