package com.shravya.mkp.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by syakkali on 05/10/17.
 */
public class MkpElideConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private int deadlineThreshold;  //In minutes

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("deadlineThreshold")
    public int getDeadlineThreshold() {
        return deadlineThreshold;
    }

    @JsonProperty("deadlineThreshold")
    public void setDeadlineThreshold(int deadlineThreshold) {
        this.deadlineThreshold = deadlineThreshold;
    }
}
