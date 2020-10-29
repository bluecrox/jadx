package jadx.core.deobf;

import org.junit.jupiter.api.Test;

import jadx.core.dex.instructions.args.ArgType;
import jadx.core.dex.instructions.args.PrimitiveType;

import static jadx.core.dex.instructions.args.ArgType.object;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProGuardUtilTest {

    @Test
    public void testIsClass() {
        assertThat("should be a class", ProGuardUtil.isClassLine("com.hundsun.common.config.PageConfig -> com.hundsun.common.config.PageConfig:"));
		assertThat("should not be a class", !ProGuardUtil.isClassLine("    android.content.Context context -> a"));
    }

    @Test
    public void testIsFunction() {
		assertThat("should be a function", ProGuardUtil.isFunctionLine("    27:33:void <init>(android.content.Context) -> <init>"));
		assertThat("should not be a function", !ProGuardUtil.isFunctionLine("    java.lang.String codeType -> d"));
    }

    @Test
	public void testConvertTypeForPrimitive() {
    	assertThat(ProGuardUtil.convertType("boolean"), is(ArgType.BOOLEAN));
		assertThat(ProGuardUtil.convertType("byte"), is(ArgType.BYTE));
		assertThat(ProGuardUtil.convertType("char"), is(ArgType.CHAR));
		assertThat(ProGuardUtil.convertType("short"), is(ArgType.SHORT));
		assertThat(ProGuardUtil.convertType("int"), is(ArgType.INT));
		assertThat(ProGuardUtil.convertType("long"), is(ArgType.LONG));
		assertThat(ProGuardUtil.convertType("float"), is(ArgType.FLOAT));
		assertThat(ProGuardUtil.convertType("double"), is(ArgType.DOUBLE));
		assertThat(ProGuardUtil.convertType("void"), is(ArgType.VOID));
	}

	@Test
	public void testConvertTypeForObject() {
		assertThat(ProGuardUtil.convertType("java.util.List"), is(object("java.util.List")));
	}

	@Test
	public void testConvertTypeForArray() {
		assertThat(ProGuardUtil.convertType("boolean[]"), is(ArgType.array(ArgType.BOOLEAN)));
		assertThat(ProGuardUtil.convertType("byte[]"), is(ArgType.array(ArgType.BYTE)));
		assertThat(ProGuardUtil.convertType("char[]"), is(ArgType.array(ArgType.CHAR)));
		assertThat(ProGuardUtil.convertType("short[]"), is(ArgType.array(ArgType.SHORT)));
		assertThat(ProGuardUtil.convertType("int[]"), is(ArgType.array(ArgType.INT)));
		assertThat(ProGuardUtil.convertType("long[]"), is(ArgType.array(ArgType.LONG)));
		assertThat(ProGuardUtil.convertType("float[]"), is(ArgType.array(ArgType.FLOAT)));
		assertThat(ProGuardUtil.convertType("double[]"), is(ArgType.array(ArgType.DOUBLE)));
	}
}
