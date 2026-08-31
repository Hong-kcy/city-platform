package com.cityplatform.activity.domain;

import com.cityplatform.platform.exception.IllegalStatusTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ActivitySubscription Domain 规则测试。
 */
class ActivitySubscriptionTest {

    @Test
    void subscribe_createsActive() {
        ActivitySubscription subscription = ActivitySubscription.subscribe(101L, 1L);
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertEquals(101L, subscription.getActivityId());
        assertEquals(1L, subscription.getUserId());
    }

    @Test
    void cancel_thenReactivate() {
        ActivitySubscription subscription = ActivitySubscription.subscribe(101L, 1L);
        subscription.cancel();
        assertEquals(SubscriptionStatus.CANCELLED, subscription.getStatus());
        subscription.reactivate();
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
    }

    @Test
    void cancel_twice_throws() {
        ActivitySubscription subscription = ActivitySubscription.subscribe(101L, 1L);
        subscription.cancel();
        assertThrows(IllegalStatusTransitionException.class, subscription::cancel);
    }

    @Test
    void reactivate_whenActive_throws() {
        ActivitySubscription subscription = ActivitySubscription.subscribe(101L, 1L);
        assertThrows(IllegalStatusTransitionException.class, subscription::reactivate);
    }
}
