package identity.action;

import constants.Constants;
import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import identity.exception.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static constants.Constants.FILLER;

public class TableIdentityAction implements IdentityAction, CSVProcessAction {
    private static final Logger LOG = LoggerFactory.getLogger(TableIdentityAction.class);
    private boolean started = false;
    private boolean readTags = false;
    private Stack<Identity> subElements = new Stack<>();
    private Queue<String> delayWrites = new LinkedList<>();
    private String[][] heads; // h,r,f are Row s of just first tag of wrap; all TD //TODO, will be Tree or Map of Tr -> td order for each
    private String[][] rows;
    private String[][] foots; // needs to consider elements on delay..,
    private int footSize;
    private String[][] currentCycle;    // segmented, 0 is tr + td heads .. broken at the text inserts, END TAG embedded
    private int currentTr = 0;
    private int currentTd = 0;

//TODO: tag handle option

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        if (!started) {
            initial(identity, handler);
        }

        writeNextText(sb, handler);
    }


    @Override
    public void processCSV(LinkedList<LinkedList<String>> csv, GeneralContentHandler handler) {
        LOG.info("Writing Table by CSV");
        LinkedList<String> rowData = csv.remove();
        int count = Math.min(csv.size(), heads.length);
        int row = 0;
        int data = 0;

        //Write header
        while(count > 0) {
            if (heads[row].length != rowData.size()+1) {
                LOG.error("Mismatch row data count in header");
                throw new RuntimeException("Mismatch row data count in header");
            }

            while(data < rowData.size()) {
                handler.write(heads[row][data++]);
                handler.write(rowData.remove());
            }
            handler.write(heads[row][data]);

            row++;
            count--;
            data =0;
            rowData = csv.remove();
        }

        // Write row cycles
        count = csv.size() - foots.length;
        row = 0;
        while (count > 0) {
            if (rows[row].length != rowData.size()+1) {
                LOG.error("Mismatch row data count in rows");
                throw new RuntimeException("Mismatch row data count in rows");
            }

            while(data < rowData.size()) {
                handler.write(rows[row][data++]);
                handler.write(rowData.remove());
            }
            handler.write(rows[row][data]);

            row = (row+1)%rows.length;
            count--;
            data = 0;
            rowData = csv.remove();
        }

        //write footer
        count = csv.size();
        while (count > 0) {
            if (foots[row].length != rowData.size()+1) {
                LOG.error("Mismatch row data count in foots");
                throw new RuntimeException("Mismatch row data count in foots");
            }

            while(data < rowData.size()) {
                handler.write(foots[row][data++]);
                handler.write(rowData.remove());
            }
            handler.write(foots[row][data]);

            row++;
            count--;
            data = 0;
            rowData = csv.remove();
        }
    }

    private void writeNextText(StringBuilder sb, GeneralContentHandler handler) {
        String text = null;
        if (footSize > 0) {
            delayWrites.add(sb.toString());
            if (delayWrites.size() > footSize) {
                text = delayWrites.poll();
            }
        }
        else {
            text = sb.toString();
        }

        if (text != null) {
            cycle(text, handler);
        }
    }

    private void cycle(String text, GeneralContentHandler handler) {
        handler.write(text);
        cycle(handler);
    }

    //TODO: handle for single segment, no rights, could loop infinate if all are no text inerts
    /**
     * writes the currentTag segment and cycles and new cycles to rows
     * @param handler
     */
    private void cycle(GeneralContentHandler handler) {
        handler.write(currentCycle[currentTr][currentTd]);
        currentTd = (currentTd+1)%currentCycle[0].length;
        if (currentTd == 0) {
            currentTr = (currentTr+1)%currentCycle.length;
            if (currentTr == 0) {
                currentCycle = rows;
            }
        }
    }

    private void initial(Identity identity, GeneralContentHandler handler) {
        heads = (String[][]) identity.getArgs(Constants.TABLE_HEADER);
        rows = (String[][]) identity.getArgs(Constants.TABLE_ROW);
        foots = (String[][]) identity.getArgs(Constants.TABLE_FOOTER);

        if (rows == null || rows.length == 0 || rows[0].length == 0) {
            throw new ParameterException("invalid Args, TABLE_ROW value, need at least 1 row");
        }

        handler.write(identity.getTemplateSegments()[0]);   // Table tag wrap start

        if (heads != null && heads.length > 0 && heads[0].length > 0) {
            currentCycle = heads;
        }
        else {
            currentCycle = rows;
        }

        if (foots != null) {
            for(String[] row: foots) {
                footSize += (row.length-1);
            }
        }

        handler.setActiveSubListHandler(this);

        if (handler.getCurrentTag().equalsIgnoreCase("table")) { // TODO: may need to consider tbody or table with bad subs
            readTags = true;
        }

        started = true;
    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        writeNextText(sb, handler);

        // Finish current row only, not full cycle
        while (currentTd != 0 || currentTd < currentCycle[currentTr].length) {
            cycle(FILLER, handler);
        }

        // write footer
        if (footSize > 0) {
            currentCycle = foots;
            currentTd = 0;
            currentTr = 0;
            while (!delayWrites.isEmpty()) {
                cycle(delayWrites.poll(), handler);
            }
        }

        handler.write(identity.getTemplateSegments()[1]);
        handler.setActiveSubListHandler(null);
    }
}
