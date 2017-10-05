package com.shravya.mkp.application;

import com.shravya.mkp.Bid;
import com.shravya.mkp.Buyer;
import com.shravya.mkp.Project;
import com.shravya.mkp.Seller;
import com.yahoo.elide.contrib.dropwizard.elide.ElideBundle;
import com.yahoo.elide.resources.JsonApiEndpoint;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MkpElideApplication extends Application<MkpElideConfiguration> {

    private final String name = "mkp-elide-app";

    private final ElideBundle<MkpElideConfiguration> elideBundle;

    public MkpElideApplication() {
        this.elideBundle = new ElideBundle<MkpElideConfiguration>(
                Bid.class,
                Buyer.class,
                Project.class,
                Seller.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(MkpElideConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }

    @Override
    public void initialize(Bootstrap<MkpElideConfiguration> bootstrap) {
        bootstrap.addBundle(elideBundle);
    }

    @Override
    public void run(MkpElideConfiguration config, Environment environment) {
        environment.jersey().register(JsonApiEndpoint.class);
    }

    public static void main(String[] args) throws Exception {
        new MkpElideApplication().run(args);
    }

    @Override
    public String getName() {
        return name;
    }
}
