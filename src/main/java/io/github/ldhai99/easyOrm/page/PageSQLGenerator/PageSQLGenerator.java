package io.github.ldhai99.easyOrm.page.PageSQLGenerator;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;

public interface PageSQLGenerator {

    public SQL generatePageSQL(PageModel pageModel, SQL sql);


}
