package text.searchSDK.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UtilClassLoader {
	public static String CONFIG_FILENAME = "config.properties";

	public Properties getProperties() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				CONFIG_FILENAME);
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties;
	}

	/**
	 * @功能：根据传入参数的不同，返回相应的配置文件属性值
	 * @param args
	 * @return
	 */
	public String getPropertiesValue(String args) {
		Properties propertie = getProperties();

		String value = "";
		if (propertie.containsKey(args)) {
			value = propertie.getProperty(args);
		}
		return value;
	}

	public static void main(String[] args) {
		PrintConsole.PrintLog(
				"new UtilClassLoader().getPropertiesValue('urltype')",
				new UtilClassLoader().getPropertiesValue("urltype"));
	}
}
