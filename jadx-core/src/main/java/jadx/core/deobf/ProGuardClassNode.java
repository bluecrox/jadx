package jadx.core.deobf;

import com.sun.org.apache.xpath.internal.Arg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jadx.core.dex.info.ClassInfo;
import jadx.core.dex.info.FieldInfo;
import jadx.core.dex.instructions.args.ArgType;
import jadx.core.dex.nodes.RootNode;

public class ProGuardClassNode {

	private String classLine;
	private List<String> memberLines;

	public String getOrigClass() {
		return origClass;
	}

	public String getMappingClass() {
		return mappingClass;
	}

	private String origClass;
	private String mappingClass;

	private ArgType mappingClassArgType;

	private ClassInfo classInfo;

	public ProGuardClassNode() {
		memberLines = new ArrayList<>();
	}

	public void setClassLine(String line) {
		classLine = line;
	}

	/**
	 * 添加成员行，包括成员方法和成员变量
	 * @param line
	 */
	public void addMemberLine(String line) {
		memberLines.add(line);
	}

	private void parseClassLine(RootNode root) {
		String[] parts = ProGuardUtil.splitAndTrim(classLine, "->");
		if(parts.length == 2) {
			origClass = parts[0];
			mappingClass = parts[1].substring(0, parts[1].length() - 1); // 去掉行尾的冒号

			ArgType mappingClassArgType = ArgType.object(mappingClass);
			classInfo = ClassInfo.fromType(root, mappingClassArgType);
		}
	}

	private void parseFieldLine(String line, RootNode root, Map<String, String> fldPresetMap) {
		String[] parts = ProGuardUtil.splitAndTrim(line, "->");
		if(parts.length == 2) {
			//(origName, alias);
			String origField = parts[0];
			String mappingFieldName = parts[1];
			String[] origFieldParts = ProGuardUtil.splitAndTrim(origField, " ");
			if(origFieldParts.length == 2) {
				String origFieldType = origFieldParts[0];

				ArgType fieldArgType = ProGuardUtil.convertType(origFieldType);

				FieldInfo fieldInfo = FieldInfo.from(root, classInfo, mappingFieldName, fieldArgType);

				String origName =  fieldInfo.getRawFullId();
				String alias = origFieldParts[1];

				fldPresetMap.put(origName, alias);
			}
		}
	}

	//.jobf
	//f com.hundsun.main.R$style.ThemeOverlay_MaterialComponents_TextInputEditText_OutlinedBox_Dense:I = f13075xc85a02f4
	//m com.hundsun.armo.quote.AnsStockBaseData.a()Ljava/util/ArrayList; = mo30819a
	//m com.hundsun.armo.quote.AnsStockBaseData.a(Lcom/hundsun/armo/quote/CodeInfo;)Lcom/hundsun/armo/quote/AnsStockBaseData$BaseData; = mo30818a

	//mappint.txt代码示例，其中<init>代表构造函数名
	//com.hundsun.armo.quote.AnsStockBaseData -> com.hundsun.armo.quote.AnsStockBaseData:
	//    com.hundsun.armo.quote.CodeInfo codeInfo -> a
	//    int getLength() -> getLength
	//    2:2:void <init>(byte[]) -> <init>
	//    1:5:com.hundsun.armo.quote.AnsStockBaseData$BaseData getBaseData(com.hundsun.armo.quote.CodeInfo) -> a
	public void parseData(RootNode root, Map<String, String> fldPresetMap) {

		parseClassLine(root);

		for(String l : memberLines) {
			if(ProGuardUtil.isFunctionLine(l)) {

			} else {
				parseFieldLine(l, root, fldPresetMap);
			}
		}
	}
}
