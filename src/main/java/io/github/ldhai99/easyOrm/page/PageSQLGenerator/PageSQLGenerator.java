package io.github.ldhai99.easyOrm.page.PageSQLGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
@JsonIgnoreProperties(ignoreUnknown = true)
public interface PageSQLGenerator {

    public SQL generatePageSQL(PageModel pageModel, SQL sql);


}
