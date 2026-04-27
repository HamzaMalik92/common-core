package com.pixel.common.util;

import lombok.experimental.UtilityClass;

// Lombok: marks as utility class — makes it final, adds private constructor, and makes all methods static
@UtilityClass
public class MaskingUtil {

    // Fully hides any sensitive value — returns [PROTECTED]
    public String hide(final String value) {
        System.out.println("MaskingUtil.hide");
        if (value == null || value.isEmpty()) return "";
        return "[PROTECTED]";
    }

    // Masks email — shows first 3 chars and domain: ham****@gmail.com
    public String maskEmail(String email) {
        System.out.println("MaskingUtil.maskEmail");
        if (email == null || !email.contains("@")) return "[INVALID EMAIL]";
        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];
        if (name.length() <= 3) return "***@" + domain;
        return name.substring(0, 3) + "****@" + domain;
    }
}
