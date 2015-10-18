package donatr.mobile;

import java.math.BigDecimal;

/**
 * Created by vileda on 17.10.15.
 */
public class ProductAccount {
    public final String id;
    public final String name;
    public final BigDecimal fixedAmount;

    public ProductAccount(String id, String name, BigDecimal fixedAmount) {
        this.id = id;
        this.name = name;
        this.fixedAmount = fixedAmount;
    }

    @Override
    public String toString() {
        return name + " for " + fixedAmount;
    }
}
