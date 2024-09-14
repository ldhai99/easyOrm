package io.github.ldhai99.easyOrm.page;



import io.github.ldhai99.easyOrm.SQL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PageData {
    public PageData setSql(SQL sql);
    public PageData setPage(PageModel page);
    public int getRowCount() ;
    public List<Map<String, Object>> getPageData() ;
}