package io.github.ldhai99.easyOrm.Dialect;



import io.github.ldhai99.easyOrm.PageModel;
import io.github.ldhai99.easyOrm.SQL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Dialect {
    public Dialect setSql(SQL sql);
    public Dialect setPage(PageModel page);
    public int getRowCount() throws SQLException;
    public List<Map<String, Object>> getPageData() throws SQLException;
}