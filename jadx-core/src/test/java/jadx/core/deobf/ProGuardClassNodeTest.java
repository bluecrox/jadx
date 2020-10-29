package jadx.core.deobf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import jadx.api.JadxArgs;
import jadx.core.dex.nodes.RootNode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProGuardClassNodeTest {

	private RootNode root;
	private ProGuardClassNode classNode;
	private Map<String, String> fldPresetMap;

	@BeforeEach
	public void setUp() {
		classNode = new ProGuardClassNode();
		fldPresetMap = new HashMap<>();

		JadxArgs jadxArgs = new JadxArgs();
		jadxArgs.setRenameFlags(EnumSet.noneOf(JadxArgs.RenameEnum.class));
		root = new RootNode(jadxArgs);
	}

	@Test
	public void testProcessClassLine() {
		classNode.setClassLine("com.hundsun.common.config.PageConfigSrc -> com.hundsun.common.config.PageConfig:");
		classNode.parseData(root, fldPresetMap);
		assertThat(classNode.getOrigClass(), is("com.hundsun.common.config.PageConfigSrc"));
		assertThat(classNode.getMappingClass(), is("com.hundsun.common.config.PageConfig"));
	}

	@Test
	public void testProcessFieldLine() {
		classNode.setClassLine("com.hundsun.common.config.PageConfigSrc -> com.hundsun.common.config.PageConfig:");
		classNode.addMemberLine("    com.hundsun.armo.quote.CodeInfo codeInfo -> a");
		classNode.parseData(root, fldPresetMap);
		assertThat(fldPresetMap.size(), is(1));
		assertThat(fldPresetMap.get("com.hundsun.common.config.PageConfig.a:Lcom/hundsun/armo/quote/CodeInfo;"), is("codeInfo"));
	}

	@Test
	public void testProcessFieldLineOfPrimitive() {
		classNode.setClassLine("com.hundsun.common.config.PageConfigSrc -> com.hundsun.common.config.PageConfig:");
		classNode.addMemberLine("    int codeInfo -> a");
		classNode.parseData(root, fldPresetMap);
		assertThat(fldPresetMap.size(), is(1));
		assertThat(fldPresetMap.get("com.hundsun.common.config.PageConfig.a:I"), is("codeInfo"));
	}

	@Test
	public void testProcessFieldLineOfComplex() {
		classNode.setClassLine("com.hundsun.trade.general.ipo_v2.calendar.stock.NotListedListView$3$1 -> com.hundsun.trade.general.ipo_v2.calendar.stock.NotListedListView$3$1:");
		classNode.addMemberLine("    java.lang.String val$code -> val$a");
		classNode.addMemberLine("    com.hundsun.trade.general.ipo_v2.calendar.stock.NotListedListView$3 this$1 -> this$1");
		classNode.parseData(root, fldPresetMap);
		assertThat(fldPresetMap.size(), is(2));
		assertThat(fldPresetMap.get("com.hundsun.trade.general.ipo_v2.calendar.stock.NotListedListView$3$1.val$a:Ljava/lang/String;"), is("val$code"));
	}

	//java.util.List val$dataList -> a
}
