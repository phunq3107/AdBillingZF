package com.phunq.adbilling.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Account {

    @Id
    private Long uid;
    private Long balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return uid != null && Objects.equals(uid, account.uid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
