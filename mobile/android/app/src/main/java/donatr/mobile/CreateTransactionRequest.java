package donatr.mobile;

import java.math.BigDecimal;

/**
 * Created by vileda on 18.10.15.
 */
public class CreateTransactionRequest {
    public final String fromAccount;
    public final String toAccount;
    public final BigDecimal amount;

    public CreateTransactionRequest(String fromAccount, String toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }
}
