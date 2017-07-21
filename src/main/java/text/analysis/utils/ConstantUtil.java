package text.analysis.utils;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月10日 下午3:21:00
 */

public class ConstantUtil {
	/*词/词性*/
	public static String WORD_FORMAT = "{0}/{1}";
	/*词与词之间的分隔符*/
	public static String WORD_SPLIT = " ";

	public static final String UTF8 = "utf-8";
	public static final String ISO88591 = "ISO-8859-1";
	public static final String GBK = "GBK";
	public static final String GB2312 = "gb2312";
	public static final String BIG5 = "big5";
	public static final String USASCII = "US-ASCII";

	/**
	 * 搜索进度提示
	 */
	public static final String NULLTIPS = "";
	public static final String BFLORE4 = "开始知识库搜索...";
	public static final String AFLORE4 = "知识库搜索结束";
	public static final String BFBAIDUSEARCH = "抓取互联网搜索结果...";
	public static final String AFBAIDUSEARCH = "抓取互联网结果完毕";
	public static final String BFLDA = "智能处理...";
	public static final String AFLDA = "智能处理完毕";

	/**
	 * 任务状态,0未完成，1人工停止，2完成，3未开始，4未找到
	 */
	public static final int TASK_UNFINISHED = 0;
	public static final int TASK_ARTIFICIAL_STOP = 1;
	public static final int TASK_COMPLETE = 2;
	public static final int TASK_NOT_STARTED = 3;
	public static final int TASK_NOT_FOUND = 4;

	/**
	 * 是否是调试模式
	 */
	public static final boolean ISDEBUG = true;

	public static final long BUFFER_SIZE_LONG = 1000000;
	public static final short BUFFER_SIZE_SHORT = 512;

	public static final int MODEL_STATUS_UNKNOWN = 0;
	public static final int MODEL_STATUS_EST = 1;
	public static final int MODEL_STATUS_ESTC = 2;
	public static final int MODEL_STATUS_INF = 3;

	/**
	 * ETM 模型处理生成文件的文件名
	 */
	public static final String ENTITY_MAP = "entitymap.txt";
	public static final String WORD_MAP = "wordmap.txt";
	public static final String SUFFIX_KESAI = ".kesai";
	public static final String SUFFIX_THETA = ".theta";
	public static final String SUFFIX_XEASSIGN = ".xEAssign";
	public static final String SUFFIX_TEASSIGN = ".tEAssign";
	public static final String SUFFIX_TASSIGN = ".tassign";
	public static final String TRAIN_LOG = "trainlog.txt";
	public static final String SUFFIX_PHI = ".phi";
	public static final String SUFFIX_OTHERS = ".others";
	public static final String SUFFIX_TWORDS = ".twords";
	public static final String SUFFIX_TDOCS = ".tdocs";
	public static final String SUFFIX_TENTITIES = ".tentities";
	public static final String TRNDOCS_DAT = "trndocs.dat";
	public static final String MODEL_NAME = "model-final";
	public static final String DIR = "./";
	public static final String SUFFIX_PHIENTITY = ".phientity";
	public static final String TOP_DOCS = "topDocs.txt";

	/**
	 * 模型迭代次数
	 */
	public static final int ITERATOR_NUM = 1000;

	/**
	 * 文档原文输出的文件名
	 */
	public static final String DOC_FILENAME = "TopicToNews.txt";
	/**
	 * 文档分词后输出的文件名
	 */
	public static final String DOC_TOKEN_FILENAME = "TokensToNews.txt";

	/**
	 * 临时文件夹名字被 “-”分割的字符串数组的长度
	 */
	public static final int TEMP_FILENAME_LEN = 5;
}
