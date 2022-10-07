package identity.action;

import identity.entity.GeneralContentHandler;

import java.util.LinkedList;

public interface CSVProcessAction {

    void processCSV(LinkedList<LinkedList<String>> csv, GeneralContentHandler handler);

}
