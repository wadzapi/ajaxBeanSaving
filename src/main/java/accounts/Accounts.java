package accounts;

import javax.annotation.Resource;
import javax.faces.component.UICommand;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Named
@ViewScoped
public class Accounts implements Serializable {
    @Resource(name = "jdbc/DefaultDB")
    private DataSource ds;

    // Data
    private List<AccountsObj> dataList;
    // Rows
    private int totalRows;
    // Paging
    private int firstRow;
    private int rowsPerPage;
    private int totalPages;
    private int pageRange;
    private Integer[] pages;
    private int currentPage;
    // Sorting
    private String sortField;
    private boolean sortAscending;
    // Select all checkbox
    private boolean mainSelectAll = false;
    // Get the selected rows
    private HashMap<Integer, Boolean> selectedIds = new HashMap<>();
    private boolean bSelectAll = mainSelectAll;

    public Accounts() {
        // Set default properties
        rowsPerPage = 10; // Default rows per page (max amount of rows to be displayed at once)
        pageRange = 10; // Default page range (max amount of page links to be displayed at once)
        sortField = "ID"; // Default sort field
        sortAscending = true; // Default sort direction
    }

    private String searchString;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void searchByTitle() {

    }

    public class AccountsObj {
        private int accountNumber;
        private String userName;
        private String passwd;
        private String firstName;
        private String lastName;
        private String lastLogin;
        private String dateRegistered;
        private String role;
        private String canLogin;

        public AccountsObj() {
        }

        public AccountsObj(int accountNumber, String userName, String passwd, String firstName, String lastName, String lastLogin, String dateRegistered, String role, String canLogin) {
            this.accountNumber = accountNumber;
            this.userName = userName;
            this.passwd = passwd;
            this.firstName = firstName;
            this.lastName = lastName;
            this.lastLogin = lastLogin;
            this.dateRegistered = dateRegistered;
            this.role = role;
            this.canLogin = canLogin;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(int accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getDateRegistered() {
            return dateRegistered;
        }

        public void setDateRegistered(String dateRegistered) {
            this.dateRegistered = dateRegistered;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCanLogin() {
            return canLogin;
        }

        public void setCanLogin(String canLogin) {
            this.canLogin = canLogin;
        }

    }

    // Paging actions -----------------------------------------------------------------------------
    public void pageFirst() {
        page(0);
    }

    public void pageNext() {
        page(firstRow + rowsPerPage);
    }

    public void pagePrevious() {
        page(firstRow - rowsPerPage);
    }

    public void pageLast() {
        page(totalRows - ((totalRows % rowsPerPage != 0) ? totalRows % rowsPerPage : rowsPerPage));
    }

    public void page(ActionEvent event) {
        page(((Integer) ((UICommand) event.getComponent()).getValue() - 1) * rowsPerPage);
    }

    private void page(int firstRow) {
        this.firstRow = firstRow;
        loadDataList(); // Load requested page
    }

    // Sorting actions ----------------------------------------------------------------------------
    public void sort(ActionEvent event) {
        String sortFieldAttribute = (String) event.getComponent().getAttributes().get("sortField");

        // If the same field is sorted, then reverse order, else sort the new field ascending
        if (sortField.equals(sortFieldAttribute)) {
            sortAscending = !sortAscending;
        } else {
            sortField = sortFieldAttribute;
            sortAscending = true;
        }

        pageFirst(); // Go to first page and load requested page
    }

    // Loaders ------------------------------------------------------------------------------------
    public void loadDataList() {

        // Load list and totalCount
        try {
            dataList = list(firstRow, rowsPerPage, sortField, sortAscending);
            totalRows = countDBRowNum(); // Count the tablerows
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Set currentPage, totalPages and pages
        currentPage = (totalRows / rowsPerPage) - ((totalRows - firstRow) / rowsPerPage) + 1;
        totalPages = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRange, totalPages);
        pages = new Integer[pagesLength];

        // FirstPage must be greater than 0 and lesser than totalPages-pageLength
        int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)), totalPages - pagesLength);

        // Create pages (page numbers for page links)
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
    }

    // Getters ------------------------------------------------------------------------------------
    public List<AccountsObj> getDataList() {
        if (dataList == null) {
            loadDataList(); // Preload page for the 1st view
        }
        return dataList;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public Integer[] getPages() {
        return pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    // Setters ------------------------------------------------------------------------------------
    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public boolean isMainSelectAll() {
        return mainSelectAll;
    }

    public void setMainSelectAll(boolean mainSelectAll) {
        this.mainSelectAll = mainSelectAll;
    }

    public List<AccountsObj> list(int firstRow, int rowCount, String sortField, boolean sortAscending) throws SQLException {
        String SqlStatement = null;

        if (ds == null) {
            throw new SQLException();
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException();
        }

        String sortDirection = sortAscending ? "ASC" : "DESC";

        SqlStatement = "SELECT * FROM ACCOUNT "
                + " WHERE (trim(?) = '') IS NOT FALSE"
                + " OR to_tsvector('english', USER_NAME || ' ' || FIRST_NAME || ' ' || LAST_NAME ) @@  to_tsquery(?)"
                + " ORDER BY %S %S offset ? limit ? ";

        String sql = String.format(SqlStatement, sortField, sortDirection);

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<AccountsObj> resultList = new ArrayList<>();

        try {
            conn.setAutoCommit(false);
            boolean committed = false;

            ps = conn.prepareStatement(sql);
//            ps.setString(1, searchString);
//            ps.setString(2, searchString);
//            ps.setInt(3, firstRow);
//            ps.setInt(4, rowCount);

            ps.setString(1, searchString);
            ps.setString(2, searchString);
            ps.setInt(3, firstRow);
            ps.setInt(4, rowCount);

            resultSet = ps.executeQuery();
            resultList = AccountArrayList(resultSet);

            conn.commit();
            committed = true;

        } finally {
            ps.close();
            conn.close();
        }

        return resultList;
    }

    //    public List<AccountsObj> list(int firstRow, int rowCount, String sortField, boolean sortAscending) throws SQLException
//    {
//
//        String SqlStatement = null;
//
//        if (ds == null)
//        {
//            throw new SQLException();
//        }
//
//        Connection conn = ds.getConnection();
//        if (conn == null)
//        {
//            throw new SQLException();
//        }
//
//        String sortDirection = sortAscending ? "ASC" : "DESC";
//
//        SqlStatement = "SELECT * FROM ACCOUNT ORDER BY %S %S offset ? limit ? ";
//
//        String sql = String.format(SqlStatement, sortField, sortDirection);
//
//        PreparedStatement ps = null;
//        ResultSet resultSet = null;
//        List<AccountsObj> resultList = new ArrayList<>();
//
//        try
//        {
//            conn.setAutoCommit(false);
//            boolean committed = false;
//
//            ps = conn.prepareStatement(sql);
//
////            ps.setInt(1, countrow);
////            ps.setInt(2, firstRow);
//            ps.setInt(1, firstRow);
//            ps.setInt(2, rowCount);
//
//            resultSet = ps.executeQuery();
//            /*
//             * Take the result from the SQL query and insert it into Array List
//             * collection
//             */
//            resultList = ProcessorArrayList(resultSet);
//
//            conn.commit();
//            committed = true;
//
//        }
//        finally
//        {
//            ps.close();
//            conn.close();
//        }
//
//        return resultList;
//    }
    public int countDBRowNum() throws Exception {

        String SqlStatement = null;

        if (ds == null) {
            throw new SQLException();
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException();
        }

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            conn.setAutoCommit(false);
            boolean committed = false;
            try {
                SqlStatement = "SELECT COUNT(ID) FROM ACCOUNT "
                        + " WHERE (trim(?) = '') IS NOT FALSE"
                        + " OR to_tsvector('english', USER_NAME || ' ' || FIRST_NAME || ' ' || LAST_NAME ) @@  to_tsquery(?)";

                ps = conn.prepareStatement(SqlStatement);
                ps.setString(1, searchString);
                ps.setString(2, searchString);
                resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }

                conn.commit();
                committed = true;
            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        } finally {
            ps.close();
            conn.close();
        }
        // Returns total amount of rmainSelectAllows in table
        return count;
    }

    /**
     * Map the current row of the given ResultSet to AccountsObj
     *
     * @param rs The ResultSet of which the current row is to be mapped to AccountsObj
     * @return The mapped AccountsObj from the current row of the given ResultSet
     */
    private ArrayList<AccountsObj> AccountArrayList(ResultSet rs) throws SQLException {
        ArrayList<AccountsObj> list = new ArrayList<>();

        try {
            while (rs.next()) {
                list.add(new AccountsObj(
                        rs.getInt("ID"),
                        rs.getString("USER_NAME"),
                        rs.getString("PASSWD"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("LAST_LOGIN"),
                        rs.getString("DATE_REGISTERED"),
                        rs.getString("ROLE"),
                        rs.getString("CAN_LOGIN")));

                // Generate selected ids
                if (!selectedIds.containsKey(rs.getInt("ID"))) {
                    selectedIds.put(rs.getInt("ID"), bSelectAll);
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        return list;
    }

    // Get the list from the JSF page
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    // Delete all selected checkboxes
    public void deleteSelectedIDs() throws SQLException {

        if (ds == null) {
            throw new SQLException();
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException();
        }

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String sqlDeleteQuery;
        Iterator<Integer> iter;
        try {
            conn.setAutoCommit(false);
            boolean committed = false;

            try {
                try {
                    if (mainSelectAll) {

                        sqlDeleteQuery = "DELETE FROM ACCOUNT";
                        Statement st = conn.createStatement();
                        st.executeUpdate(sqlDeleteQuery);
                        st.close();
                        st = null;

                    } else if (bSelectAll) {
                        sqlDeleteQuery = "DELETE FROM ACCOUNT WHERE ID NOT IN (%s)";

                        iter = selectedIds.keySet().iterator();

                        StringBuilder sb = new StringBuilder();

                        int cnt = 0;
                        while (iter.hasNext()) {
                            Integer key = iter.next();
                            if (!selectedIds.get(key).booleanValue()) {
                                sb.append("'");
                                sb.append(key);
                                sb.append("',");
                                cnt++;
                            }
                        }

                        if (cnt > 0) {

                            try (Statement st = conn.createStatement()) {
                                // this is the bugfix...
                                sqlDeleteQuery = String.format(sqlDeleteQuery, sb.substring(0, sb.length() - 1));
                                st.executeUpdate(sqlDeleteQuery);
                                // FirstRow must be set to zero, otherwhise  sometimes an empty table is being rendered
                                // even if there is data loaded in the paginator
                                firstRow = 0;

                            } catch (SQLException x) {
                                throw x;
                            }

                        } else {
                            try (Statement st = conn.createStatement()) {
                                st.executeUpdate("DELETE FROM ACCOUNT");
                            } catch (SQLException x) {
                                throw x;
                            }

                        }

                    } else {

                        sqlDeleteQuery = "DELETE FROM ACCOUMT WHERE ID = ?";
                        ps = conn.prepareStatement(sqlDeleteQuery);
                        int batCnt = 0;

                        iter = selectedIds.keySet().iterator();

                        while (iter.hasNext()) {
                            Integer key = iter.next();
                            if (selectedIds.get(key).booleanValue()) {
                                if (batCnt++ < 100) {
                                    ps.setInt(1, key);
                                    ps.addBatch();
                                    ps.clearParameters();
                                } else {
                                    batCnt = 0;
                                    ps.executeBatch();
                                    ps.clearBatch();
                                }

                            }
                        }

                        // When iter.hasNext() = false before batCnt reaches 100
                        if (batCnt > 0) {
                            ps.executeBatch();
                            ps.clearBatch();
                        }
                    }

                } catch (NoSuchElementException nsee) {
                    System.out.println(nsee.getMessage());
                }

                conn.commit();
                committed = true;
                selectedIds.clear();

            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            conn.close();
        }
        // Reset the main checkbox
        mainSelectAll = false;
        bSelectAll = false;
        // Reload the table
        loadDataList();
    }

    // Select all - get all keys
    public void selectAll() {
        bSelectAll = mainSelectAll;
        for (Map.Entry<Integer, Boolean> entry : selectedIds.entrySet()) {
            entry.setValue(mainSelectAll);
        }
    }

    /*
     * If select all checkbox is selected but one row checkbox is deselected
     * deselect the main checkbox
     */
    public void deselectMain() {
        mainSelectAll = false;
    }

    private static Date timestampToDate(java.sql.Timestamp ts) {
        Date d = null;
        try {
            d = new Date(ts.getTime());
        } catch (Exception e) {
        }

        return d;
    }

    public static ExternalContext getExtContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            return context.getExternalContext();
        }
        return null;
    }

    public static Map<String, Object> getSessionMap() {
        ExternalContext extContext = getExtContext();
        if (extContext != null) {
            return extContext.getSessionMap();
        }
        return null;
    }

    public void setSessionValue(Object value) {

        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        String sessionId = session.getId();

        getSessionMap().put(sessionId, value);

    }

    public Object getSessionValue() {

        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        String sessionId = session.getId();

        return getSessionMap().get(sessionId);
    }

    public String pageRedirect() {

        return "/Processor/ProcessorProfile.xhtml?faces-redirect=true";
    }
}