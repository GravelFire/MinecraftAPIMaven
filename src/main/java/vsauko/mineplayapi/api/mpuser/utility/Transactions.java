package vsauko.mineplayapi.api.mpuser.utility;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Transactions {
  Transaction data;

  @AllArgsConstructor
  public static class Transaction {
    String uniqueId;
    int value;
  }
}
