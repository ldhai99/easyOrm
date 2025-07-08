package io.github.ldhai99.easyOrm.dbenum;

import java.util.Collection;
import java.util.Optional;

/**
 * 用户状态枚举 - 只包含常量定义
 */
public final class UserStatus extends DbEnum {
    // 枚举值定义
    public static final UserStatus ACTIVE = new UserStatus(1, "活跃用户");
    public static final UserStatus INACTIVE = new UserStatus(0, "未激活");
    public static final UserStatus FROZEN = new UserStatus(2, "已冻结");

    // 私有构造函数
    private UserStatus(Object dbValue, String description) {
        super(dbValue, description);
    }

    public static void main(String[] args) {
        // 1. 获取枚举值
        UserStatus active = UserStatus.ACTIVE;
        System.out.println("值: " + active.getValue()); // 输出: 1
        System.out.println("描述: " + active.getDescription()); // 输出: 活跃用户

// 2. 解析枚举值
        UserStatus frozen = DbEnum.valueOf(UserStatus.class, 2);
        System.out.println(frozen.getDescription()); // 输出: 已冻结
        UserStatus frozen1 = DbEnum.valueOf(UserStatus.class, "ACTIVE");
        System.out.println(frozen1.getDescription()); // 输出: 已冻结


// 3. 安全解析
        Optional<UserStatus> inactive = DbEnum.safeValueOf(UserStatus.class, 0);
        inactive.ifPresent(status ->
                System.out.println(status.getDescription())); // 输出: 未激活

// 4. 值验证
        System.out.println(DbEnum.isValid(UserStatus.class, 1)); // true
        System.out.println(DbEnum.isValid(UserStatus.class, 3)); // false

// 5. 获取所有值
        Collection<UserStatus> allStatus = DbEnum.values(UserStatus.class);
        allStatus.forEach(status ->
                System.out.println(status.getDescription()));
    }
}
