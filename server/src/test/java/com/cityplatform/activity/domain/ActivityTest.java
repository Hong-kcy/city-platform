package com.cityplatform.activity.domain;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Activity Domain 状态机与业务规则测试。
 */
class ActivityTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 9, 10, 18, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 9, 12, 22, 0);

    private Activity newDraftActivity() {
        return Activity.create(101L, "淮河路夜市美食节", "摘要", "描述", null,
                ActivityType.FESTIVAL, START, END, "淮河路步行街全段", null);
    }

    @Test
    void create_shouldBeDraft() {
        Activity activity = newDraftActivity();
        assertEquals(ActivityStatus.DRAFT, activity.getStatus());
        assertFalse(activity.subscribable());
    }

    @Test
    void publish_fromDraft_succeeds() {
        Activity activity = newDraftActivity();
        activity.publish();
        assertEquals(ActivityStatus.PUBLISHED, activity.getStatus());
        assertTrue(activity.subscribable());
    }

    @Test
    void publish_twice_throws() {
        Activity activity = newDraftActivity();
        activity.publish();
        assertThrows(IllegalStatusTransitionException.class, activity::publish);
    }

    @Test
    void publish_fromOffline_throws() {
        Activity activity = newDraftActivity();
        activity.publish();
        activity.takeOffline();
        assertThrows(IllegalStatusTransitionException.class, activity::publish);
    }

    @Test
    void takeOffline_fromPublished_succeeds() {
        Activity activity = newDraftActivity();
        activity.publish();
        activity.takeOffline();
        assertEquals(ActivityStatus.OFFLINE, activity.getStatus());
        assertFalse(activity.subscribable());
    }

    @Test
    void takeOffline_fromDraft_throws() {
        Activity activity = newDraftActivity();
        assertThrows(IllegalStatusTransitionException.class, activity::takeOffline);
    }

    @Test
    void takeOffline_twice_throws() {
        Activity activity = newDraftActivity();
        activity.publish();
        activity.takeOffline();
        assertThrows(IllegalStatusTransitionException.class, activity::takeOffline);
    }

    @Test
    void create_endBeforeStart_throws() {
        assertThrows(BusinessException.class, () ->
                Activity.create(101L, "标题", null, null, null,
                        ActivityType.OTHER, END, START, null, null));
    }

    @Test
    void updateInfo_onOffline_throws() {
        Activity activity = newDraftActivity();
        activity.publish();
        activity.takeOffline();
        assertThrows(IllegalStatusTransitionException.class, () ->
                activity.updateInfo(101L, "新标题", null, null, null,
                        ActivityType.OTHER, START, END, null, null));
    }
}
