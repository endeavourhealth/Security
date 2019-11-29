package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CacheManager {
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> future;

    public static void startScheduler() {
        if (future == null || future.isDone()) {
            future = scheduler.scheduleAtFixedRate(flushAllCaches, 1, 1, TimeUnit.MINUTES);
        }
    }

    public static void stopScheduler() {
        if (future != null && !future.isDone()) {
            try {
                future.cancel(true);
                scheduler.shutdownNow();
                scheduler.awaitTermination(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                scheduler.shutdownNow();
            }
        }
    }

    static Runnable flushAllCaches = new Runnable() {
        @Override
        public void run() {
            try {
                ApplicationCache.flushCache();
                ApplicationPolicyCache.flushCache();
                ApplicationProfileCache.flushCache();
                DataProcessingAgreementCache.flushCache();
                DataSharingAgreementCache.flushCache();
                DelegationCache.flushCache();
                OrganisationCache.flushCache();
                ProjectCache.flushCache();
                RegionCache.flushCache();
                UserCache.flushCache();
                DataSetCache.flushCache();
                CohortCache.flushCache();
            } catch (Exception e) {

            }
        }
    };

}
