package com.gym.gymmanagementsystem.services.specifications;


import java.time.LocalDateTime;

import com.gym.gymmanagementsystem.entities.UserSubscription;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.EntryHistory;

/**
 * Utility třída obsahující JPA Specifications pro entitu {@link User}.
 * Slouží k dynamickému sestavování podmínek dotazů na uživatele podle různých kritérií.
 */
public class UserSpecifications {

    /**
     * Specifikace pro filtrování uživatelů, kteří mají alespoň jeden záznam vstupu v zadaném intervalu.
     *
     * @param start počáteční datum a čas vstupu
     * @param end   konečné datum a čas vstupu
     * @return Specification pro filtrování podle záznamů vstupů
     */
    public static Specification<User> hasEntryHistoryInRange(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            Join<User, EntryHistory> entryJoin = root.join("entryHistories");
            query.distinct(true);
            return cb.between(entryJoin.get("entryDate"), start, end);
        };
    }

    /**
     * Specifikace pro filtrování uživatelů, kteří mají alespoň daný počet záznamů vstupů.
     *
     * @param minCount minimální počet záznamů vstupů
     * @return Specification pro filtrování podle počtu vstupů
     */
    public static Specification<User> hasMinimumEntryCount(Integer minCount) {
        return (root, query, cb) -> {
            Join<User, EntryHistory> entryJoin = root.join("entryHistories", JoinType.LEFT);
            query.groupBy(root.get("userID"));
            return cb.ge(cb.count(entryJoin.get("entryID")), minCount);
        };
    }

    /**
     * Specifikace pro filtrování uživatelů, kteří mají aktivní předplatné.
     *
     * @return Specification pro filtrování podle aktivního předplatného
     */
    public static Specification<User> hasActiveSubscription() {
        return (root, query, cb) -> {
            Join<User, ?> subJoin = root.join("subscriptions", JoinType.LEFT);
            return cb.isTrue(subJoin.get("isActive"));
        };
    }

    /**
     * Specifikace pro filtrování uživatelů, kteří nemají žádné aktivní předplatné.
     *
     * @return Specification pro filtrování uživatelů bez aktivního předplatného
     */
    public static Specification<User> hasNoActiveSubscription() {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            var subRoot = subquery.from(UserSubscription.class);
            subquery.select(cb.count(subRoot));
            subquery.where(cb.and(
                    cb.equal(subRoot.get("user"), root),
                    cb.isTrue(subRoot.get("isActive"))
            ));
            return cb.equal(subquery, 0);
        };
    }

    /**
     * Specifikace pro filtrování uživatelů, jejichž předplatné končí v zadaném intervalu.
     *
     * @param now  počáteční datum intervalu
     * @param upTo konečné datum intervalu
     * @return Specification pro filtrování uživatelů s předplatným končícím v zadaném intervalu
     */
    public static Specification<User> hasExpiringSubscription(LocalDateTime now, LocalDateTime upTo) {
        return (root, query, cb) -> {
            Join<User, ?> subJoin = root.join("subscriptions", JoinType.LEFT);
            return cb.between(subJoin.get("endDate"), now, upTo);
        };
    }
}
