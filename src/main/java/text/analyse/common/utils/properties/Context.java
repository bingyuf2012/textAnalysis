package text.analyse.common.utils.properties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Context {
	private JSONObject config;

	public Context() {
		String conf = System.getProperty("textAnalysis.conf", "classpath:config.properties");
		this.config = FileUtils.propertiesFileToJSON(conf);
	}

	public Context(String fileName) {
		this.config = FileUtils.propertiesFileToJSON(fileName);
	}

	public JSONArray getJSONArray(String key) {
		return JSONArray.parseArray(config.getString(key));
	}

	public Boolean getBoolean(String key) {
		return config.getBoolean(key);
	}

	public byte[] getBytes(String key) {
		return config.getBytes(key);
	}

	public boolean getBooleanValue(String key) {
		return config.getBooleanValue(key);
	}

	public Byte getByte(String key) {
		return config.getByte(key);
	}

	public byte getByteValue(String key) {
		return config.getByteValue(key);
	}

	public Short getShort(String key) {
		return config.getShort(key);
	}

	public short getShortValue(String key) {
		return config.getShortValue(key);
	}

	public Integer getInteger(String key) {
		return config.getInteger(key);
	}

	public int getIntValue(String key) {
		return config.getIntValue(key);
	}

	public Long getLong(String key) {
		return config.getLong(key);
	}

	public long getLongValue(String key) {
		return config.getLongValue(key);
	}

	public Float getFloat(String key) {
		return config.getFloat(key);
	}

	public float getFloatValue(String key) {
		return config.getFloatValue(key);
	}

	public Double getDouble(String key) {
		return config.getDouble(key);
	}

	public double getDoubleValue(String key) {
		return config.getDoubleValue(key);
	}

	public BigDecimal getBigDecimal(String key) {
		return config.getBigDecimal(key);
	}

	public BigInteger getBigInteger(String key) {
		return config.getBigInteger(key);
	}

	public String getString(String key) {
		return config.getString(key);
	}

	public Date getDate(String key) {
		return config.getDate(key);
	}

	public java.sql.Date getSqlDate(String key) {
		return config.getSqlDate(key);
	}

	public java.sql.Timestamp getTimestamp(String key) {
		return config.getTimestamp(key);
	}
}
