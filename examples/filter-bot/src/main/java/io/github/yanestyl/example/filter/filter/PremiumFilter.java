package io.github.yanestyl.example.filter.filter;

import io.github.yanestyl.jgram.filter.Filter;
import io.github.yanestyl.jgram.model.UpdateContext;

import java.util.Set;

/**
 * Example custom filter - simulates premium users check.
 * In real project this would check a database.
 */
public class PremiumFilter implements Filter {

    private static final Set<Long> PREMIUM_USERS = Set.of(
            123456789L
    );

    @Override
    public boolean test(UpdateContext ctx) {
        return PREMIUM_USERS.contains(ctx.userId());
    }

    @Override
    public String onFail() {
        return "⭐ Эта команда доступна только премиум пользователям!";
    }
}
