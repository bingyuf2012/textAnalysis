package text.analyse.common.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;

/**
 * 文件工具类，讲文件内容解析成json或者properties
 *
 */
public class FileUtils {
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	private FileUtils() {
	}

	public static <T> List<T> toJSONArray(String fileName, Class<T> clazz) {
		byte[] bytes = toBytes(fileName);

		try {
			return JSONArray.parseArray(new String(bytes), clazz);
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				// LOG.error("{} to List<{}> failed!{}",new
				// String(bytes),clazz.getName(),e);
			}

			throw Throwables.propagate(e);
		}
	}

	public static JSONArray toJSONArray(String fileName) {
		byte[] bytes = toBytes(fileName);

		try {
			return JSONArray.parseArray(new String(bytes));
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("{} to JSONArray failed!{}", new String(bytes), e);
			}

			throw Throwables.propagate(e);
		}
	}

	public static JSONObject toJSON(String fileName) {
		byte[] bytes = toBytes(fileName);

		try {
			return JSONObject.parseObject(new String(bytes));
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("{} to json failed!{}", new String(bytes), e);
			}

			throw Throwables.propagate(e);
		}
	}

	public static JSONObject propertiesFileToJSON(String fileName) {
		Properties properties = toProperties(fileName);
		JSONObject json = new JSONObject();
		for (Object key : properties.keySet()) {
			json.put(String.valueOf(key), properties.get(key));
		}

		return json;
	}

	public static Properties toProperties(String fileName) {
		Properties properties = new Properties();
		Reader reader = null;

		try {
			reader = new StringReader(new String(toBytes(fileName)));
			properties.load(reader);

			return properties;
		} catch (IOException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("parse {} to properties failed!{}", fileName, e);
			}

			throw Throwables.propagate(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static byte[] toBytes(String fileName) {
		InputStream stream = null;
		byte[] bytes;
		try {
			if (fileName.startsWith(Keys.CLASSPATH)) {
				stream = FileUtils.class.getClassLoader()
						.getResourceAsStream(fileName.substring(Keys.CLASSPATH.length()));
			} else {
				stream = new FileInputStream(new File(fileName));
			}

			bytes = new byte[stream.available()];
			stream.read(bytes);

			return bytes;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("load {} bytes failed! {}", fileName, e);
			}

			throw Throwables.propagate(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
