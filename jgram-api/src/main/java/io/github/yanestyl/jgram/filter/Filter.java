package io.github.yanestyl.jgram.filter;

import io.github.yanestyl.jgram.model.UpdateContext;

/**
 * Custom filter for handler methods.
 * Implement this interface and annotate your handler with @UseFilter.
 * <p>
 * Example:
 * <pre>
 *     public class PremiumFilter implementsFilter {
 *
 *         {@literal @}Override
 *         public boolean test(Update update) {
 *             return PremiumService.isPremium(update.message().from().id());
 *         }
 *
 *         {@literal @}Override
 *         public String onFail() {
 *             return "Эта команда только для премиум пользователей!";
 *         }
 *     }
 * </pre>
 */
public interface Filter {

    /**
     * Returns true if the update passes the filter.
     */
    boolean test(UpdateContext update);

    /**
     * Message to send if filter fails.
     * Return null to ignore silently.
     */
    default String onFail() {
        return null;
    }
}
