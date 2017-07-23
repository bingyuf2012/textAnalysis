package text.analyse.etmutility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import text.searchSDK.util.CommonUtil;
import text.searchSDK.util.Constant;

public class Entity {
	private int id;
	private String name;
	private int totalNum;
	private HashMap<Integer, String> id_name = new HashMap<Integer, String>();

	public Entity(int id, int totalNum) {
		this.id = id;
		this.totalNum = totalNum;
	}

	public Entity(String ouputdir) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(ouputdir + Constant.ENTITY_MAP),
					Constant.UTF8));
			String line = br.readLine();
			totalNum = Integer.parseInt(line);
			while ((line = br.readLine()) != null) {
				String[] pairs = line.split(" ");
				id_name.put(Integer.parseInt(pairs[1]), pairs[0]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		id = -1;
		name = null;

	}

	public void setName(String name) {
		this.name = name;

	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;

	}

	public int getId() {
		return this.id;
	}

	public String getName(int id) {
		return id_name.get(id);
	}

	public int getId(String name) {
		Set<Integer> keys = id_name.keySet();
		for (int key : keys) {
			if (id_name.get(key).equals(name)) {
				return key;
			}
		}
		return -1;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public HashMap<Integer, String> getId_name() {
		return id_name;
	}

	public void setId_name(HashMap<Integer, String> idName) {
		id_name = idName;
	}

	public static Comparator<Entity> comparator = new Comparator<Entity>() {
		public int compare(Entity e1, Entity e2) {
			// 比率
			return (int) (e1.id - e2.id);
		}
	};
}
