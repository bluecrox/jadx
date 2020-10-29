package jadx.core.deobf;

import java.util.regex.Pattern;

import jadx.core.dex.instructions.args.ArgType;

public class ProGuardUtil {

	// class非空字符打头, 冒号结尾
	private static Pattern classLinePattern = Pattern.compile("\\S+.*:");

	// 函数是前缀4个空格，且包含一对()
	private static Pattern functionLinePattern = Pattern.compile("\\s+.*\\(.*\\).*");

	private static boolean isEmptyLine(String line) {
		return line == null || line.length() == 0;
	}

	public static String[] splitAndTrim(String str, String regex) {
		String[] v = str.split(regex);
		for (int i = 0; i < v.length; i++) {
			v[i] = v[i].trim();
		}
		return v;
	}

	// 判断是否是Class
	public static boolean isClassLine(String line) {
		// 如果是class，则没有前导空格，方法和变量前有4个空格
		return classLinePattern.matcher(line).matches();
	}

	// 判断是否是函数
	public static boolean isFunctionLine(String line) {
		return functionLinePattern.matcher(line).matches();
	}

    public static ArgType convertType(String type) {

		if(type.contains("[]")) {
			String singleType = type.replaceAll("\\[\\]", "");
			return ArgType.array(convertSingleType(singleType));
		}

		return convertSingleType(type);
    }

    private static ArgType convertSingleType(String singleType) {

			switch(singleType) {
				case "boolean":
					return ArgType.BOOLEAN;
				case "byte":
					return ArgType.BYTE;
				case "char":
					return ArgType.CHAR;
				case "short":
					return ArgType.SHORT;
				case "int":
					return ArgType.INT;
				case "long":
					return ArgType.LONG;
				case "float":
					return ArgType.FLOAT;
				case "double":
					return ArgType.DOUBLE;
				case "void":
					return ArgType.VOID;
				default:
					return ArgType.object(singleType);
			}
	}
}
