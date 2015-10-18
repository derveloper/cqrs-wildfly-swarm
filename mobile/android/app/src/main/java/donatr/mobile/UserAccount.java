package donatr.mobile;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by vileda on 17.10.15.
 */
public class UserAccount implements Serializable {
    public final String id;
    public final String name;
    public final String email;
    public final BigDecimal balance;

    public UserAccount(String id, String name, BigDecimal balance, String email) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.email = email;
    }
}
