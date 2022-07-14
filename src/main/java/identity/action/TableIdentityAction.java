package identity.action;

public class TableIdentityAction extends JoinIdentityAction {

  private final char rowSeparator;
  private final char columnSeparator;

  public TableIdentityAction(char columnSeparator, char rowSeparator){
    this.columnSeparator = columnSeparator;
    this.rowSeparator = rowSeparator;
  };

  @Override
  public String process(){
    StringBuffer xml = new StringBuffer("<table>");

    boolean rowOpened = false;
    boolean colOpened = false;

    for ( int i = 0; i < sb.length() ; i ++ ){
      if ( sb.charAt(i) == rowSeparator ) {
        if( colOpened ) {
          xml.append("</col>");
          colOpened = false;
        };
        xml.append("</row>");
        rowOpened = false;
      }

      else if( sb.charAt(i) == columnSeparator ){
        xml.append("</col><col>");
        colOpened = true;
      }

      else {
        if ( !colOpened ) {
          xml.append("\n<row><col>");
          colOpened = true;
        };
        xml.append(sb.charAt(i));
      }
    }

    xml.append("\n</table>");
    return xml.toString();
  }
}
