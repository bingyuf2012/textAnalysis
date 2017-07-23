/**
 * 
 */
package text.analyse.model;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Alex
 * 
 */
public abstract class BaseModelObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6059716602297475831L;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String dump = getFieldsDump(this.getClass());
		dump = dump.substring(0, dump.length() - 1);

		return this.getClass().getName() + ":[" + dump + "]";
	}

	@SuppressWarnings("unchecked")
	private String getFieldsDump(Class c) {
		Field[] fields = c.getDeclaredFields();
		if (fields == null || fields.length == 0)
			return "";
		StringBuilder buf = new StringBuilder();
		for (Field f : fields) {
			f.setAccessible(true);
			if ("serialVersionUID".equals(f.getName()))
				continue;
			try {
				buf = buf.append(f.getName()).append("=").append(f.get(this))
						.append(',');
			} catch (Exception e) {
				continue;
			}
		}
		if (c.getSuperclass() != null) {
			buf = buf.insert(0, getFieldsDump(c.getSuperclass()));
		}
		return buf.toString();
	}
}
