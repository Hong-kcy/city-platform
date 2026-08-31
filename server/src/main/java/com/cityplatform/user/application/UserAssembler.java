package com.cityplatform.user.application;

import com.cityplatform.user.application.dto.UpdateMyProfileRequest;
import com.cityplatform.user.domain.User;
import org.springframework.stereotype.Component;

/**
 * 用户 DTO/Entity 转换器。DTO 转换统一放 Assembler，Controller/Domain 不负责。
 */
@Component
public class UserAssembler {

    public void applyUpdate(User user, UpdateMyProfileRequest req) {
        user.updateProfile(
                req.getNickname(),
                req.getAvatarFileId(),
                req.getPreferredActivityTypes(),
                req.getPreferredExperienceTypes()
        );
        user.updateMessagePreference(
                req.getActivityReminderEnabled(),
                req.getSystemNotificationEnabled()
        );
    }
}
