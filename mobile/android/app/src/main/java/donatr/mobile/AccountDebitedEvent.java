package donatr.mobile;

import java.math.BigDecimal;

/**
 * Created by vileda on 18.10.15.
 */
public class AccountDebitedEvent {
    public final String id;
    public final BigDecimal amount;

    public AccountDebitedEvent(String id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }
}
