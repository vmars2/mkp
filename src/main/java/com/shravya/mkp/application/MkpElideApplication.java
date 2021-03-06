package com.shravya.mkp.application;

import com.shravya.mkp.entities.Bid;
import com.shravya.mkp.entities.Buyer;
import com.shravya.mkp.deadline.processor.DeadlineProcessorOrchestrator;
import com.shravya.mkp.entities.Seller;
import com.shravya.mkp.entities.Project;
import com.yahoo.elide.contrib.dropwizard.elide.ElideBundle;
import com.yahoo.elide.resources.JsonApiEndpoint;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;

/**
 * The root application.
 */
public class MkpElideApplication extends Application<MkpElideConfiguration> {

    private final String name = "mkp-elide-app";

    private final ElideBundle<MkpElideConfiguration> elideBundle;

    public MkpElideApplication() {
        this.elideBundle = new ElideBundle<MkpElideConfiguration>(
                Bid.class,
                Seller.class,
                Project.class,
                Buyer.class
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

        final PooledDataSourceFactory dbConfig = config.getDataSourceFactory();
        SessionFactory sessionFactory = elideBundle.getSessionFactoryFactory().build(elideBundle, environment
                , dbConfig, elideBundle.getEntities());

        DeadlineProcessorOrchestrator deadlineProcessorOrchestrator = new DeadlineProcessorOrchestrator(sessionFactory,
                config.getDeadlineThreshold());

        (new Thread(deadlineProcessorOrchestrator)).start();

    }

    public static void main(String[] args) throws Exception {
        new MkpElideApplication().run(args);
    }

    @Override
    public String getName() {
        return name;
    }
}
