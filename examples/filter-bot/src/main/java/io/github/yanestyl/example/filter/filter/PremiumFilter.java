package io.github.yanestyl.example.filter.filter;

import com.pengrad.telegrambot.model.Update;
import io.github.yanestyl.jgram.filter.Filter;

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
    public boolean test(Update update) {
        if (update.message() == null) return false;
        long userId = update.message().from().id();
        return PREMIUM_USERS.contains(userId);
    }

    @Override
    public String onFail() {
        return "⭐ Эта команда доступна только премиум пользователям!";
    }
}
