package text.analysis.utils;

import org.springframework.stereotype.Component;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月10日 下午3:14:14
 */
@Component
public class ConvertUtil {
	public String convertPos(String sourcePos) {
		switch (sourcePos) {
		case "nr":
			return "PER";
		case "nt":
			return "ORG";
		case "ns":
			return "LOC";
		default:
			return sourcePos;
		}
	}
}
