package lol.cicco.admin.common.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lol.cicco.admin.common.util.GsonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonArrayHandler extends BaseTypeHandler<JsonArray> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonArray parameter, JdbcType jdbcType)
            throws SQLException {
        PGobject uuidObj = new PGobject();
        uuidObj.setType("jsonb");
        uuidObj.setValue(GsonUtils.gson().toJson(parameter));
        ps.setObject(i, uuidObj);
    }

    @Override
    public JsonArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonParser.parseString(rs.getString(columnName)).getAsJsonArray();
    }

    @Override
    public JsonArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonParser.parseString(rs.getString(columnIndex)).getAsJsonArray();
    }

    @Override
    public JsonArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonParser.parseString(cs.getString(columnIndex)).getAsJsonArray();
    }

}
