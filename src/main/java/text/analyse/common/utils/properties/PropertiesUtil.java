package text.analyse.common.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 属性文件工具类
 * 
 */
public class PropertiesUtil {
	protected static Log logger = LogFactory.getLog(PropertiesUtil.class);

	/**
	 * 读取属性文件
	 * 
	 * @param fileName
	 * @return 返回Properties对象
	 */
	public static Properties loadProperties(String fileName) {
		Properties p = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		try {
			loadFromStream(p, in);
		} catch (IOException e) {
			logger.error("读取 " + fileName + " 属性文件错误!", e);
		}
		return p;
	}

	public static Properties loadPropFromFilePath(String fileName) {
		Properties p = new Properties();
		try {
			loadFromStream(p, new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			logger.error("配置文件" + fileName + " 不存在!", e);
		} catch (IOException e) {
			logger.error("读取 " + fileName + " 属性文件错误!", e);
		}
		return p;
	}

	private static void loadFromStream(Properties p, InputStream in) throws IOException {
		try {
			if (in != null)
				p.load(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static boolean storePropInFilePath(Properties prop, String fileName) {
		try {
			prop.store(new FileWriter(fileName), "");
			return true;
		} catch (IOException e) {
			logger.error("写入配置文件出错！fileName" + fileName, e);
			return false;
		}
	}

	/**
	 * 根据文件名从类路径读取属性文件
	 * 
	 * @param p
	 * @param fileName
	 */
	public static void loadFromClassPathResource(Properties p, String fileName) {
		/*
		 * InputStream in = null; Resource resource = new
		 * ClassPathResource(fileName);
		 */
		try {
			/*
			 * in = resource.getInputStream(); loadFromStream(p, in);
			 */

			Reader reader = new StringReader(new String(toBytes(fileName)));
			p.load(reader);

		} catch (IOException e) {
			logger.error("读取 " + fileName + " 属性文件错误!", e);
		}
	}

	public static byte[] toBytes(String fileName) {
		String classpath = "classpath:";
		InputStream stream = null;
		byte[] bytes = new byte[0];
		try {
			if (fileName.startsWith(classpath)) {
				stream = PropertiesUtil.class.getClassLoader()
						.getResourceAsStream(fileName.substring(classpath.length()));
			} else {
				stream = new FileInputStream(new File(fileName));
			}

			bytes = new byte[stream.available()];
			stream.read(bytes);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("load {} bytes failed! {}", e);
			}
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("load {} bytes failed! {}", e);
				}
			}
		}
		return bytes;
	}

	public static void main(String[] args) {

		// Properties hh =
		// PropertiesUtil.loadProperties("ssoConfig.properties");
		Properties hh = PropertiesUtil.loadProperties("config.properties");
		Object sss = hh.getProperty("db.driverClassName");
		System.out.println(sss);

		Properties properties = null;
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("ssoConfig.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = properties.getProperty("ssoDomain");// 根据name得到对应的value
		System.out.println(result);
	}

}