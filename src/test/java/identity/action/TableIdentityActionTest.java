package identity.action;

import org.junit.jupiter.api.Test;

class TableIdentityActionTest {

  String TEXT_TABLE_1 = "1 2 3\n";

  @Test
  public void shouldParseTheTablesCorrectly() {
    TableIdentityAction action = new TableIdentityAction(' ', '\n');

    action.append(TEXT_TABLE_1);

    System.out.println(action.process());

  }

}