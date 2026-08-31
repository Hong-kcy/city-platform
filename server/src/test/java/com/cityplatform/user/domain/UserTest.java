package com.cityplatform.user.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * User Domain 规则测试。
 */
class UserTest {

    @Test
    void register_defaults() {
        User user = User.register("openid-1", null);
        assertEquals("openid-1", user.getOpenid());
        assertNull(user.getUnionid());
        assertNull(user.getNickname());
        assertNull(user.getAvatarFileId());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(0, user.getPoints());
        assertTrue(user.isActivityReminderEnabled());
        assertTrue(user.isSystemNotificationEnabled());
    }

    @Test
    void fillUnionid_onlyWhenAbsent() {
        User user = User.register("openid-1", null);
        user.fillUnionid("unionid-1");
        assertEquals("unionid-1", user.getUnionid());
        // 已有 unionid 时不被覆盖
        user.fillUnionid("unionid-2");
        assertEquals("unionid-1", user.getUnionid());
    }

    @Test
    void updateProfile_partialSemantics() {
        User user = User.register("openid-1", null);
        user.updateProfile("昵称", null, "FESTIVAL,PERFORMANCE", null);
        assertEquals("昵称", user.getNickname());
        assertNull(user.getAvatarFileId());
        assertEquals("FESTIVAL,PERFORMANCE", user.getPreferredActivityTypes());
        assertNull(user.getPreferredExperienceTypes());
    }

    @Test
    void updateMessagePreference_partialSemantics() {
        User user = User.register("openid-1", null);
        user.updateMessagePreference(false, null);
        assertEquals(false, user.isActivityReminderEnabled());
        assertTrue(user.isSystemNotificationEnabled());
    }
}
