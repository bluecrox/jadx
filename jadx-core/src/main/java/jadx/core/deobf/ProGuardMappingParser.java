package jadx.core.deobf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jadx.core.dex.nodes.RootNode;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ProGuardMappingParser {

	private static final Logger LOG = LoggerFactory.getLogger(ProGuardMappingParser.class);

	private static final Charset MAP_FILE_CHARSET = UTF_8;

	private final Path proGuardMapFile;

    public ProGuardMappingParser(Path proGuardMapFile ) {
    	this.proGuardMapFile = proGuardMapFile;
	}

    public void load(RootNode root, Map<String, String> fldPresetMap) {
		if (!Files.exists(proGuardMapFile)) {
			return;
		}
		LOG.info("Loading ProGuard mapping from: {}", proGuardMapFile.toAbsolutePath());
		try {
			List<String> lines = Files.readAllLines(proGuardMapFile, MAP_FILE_CHARSET);
			for (int i = 0; i < lines.size(); i++) {
				String l = lines.get(i);
				if (l.isEmpty()) {
					continue;
				}

				if (ProGuardUtil.isClassLine(l)) {
					ProGuardClassNode classData = new ProGuardClassNode();
					classData.setClassLine(l);

					for(int j = i + 1; j < lines.size(); j++) {
						String cl = lines.get(j);
						if (cl.isEmpty()) {
							continue;
						}

						if(!ProGuardUtil.isClassLine(cl)) {
							classData.addMemberLine(cl);
						} else { // 已经到下一个class了，索引要退回一个位置
							classData.parseData(root, fldPresetMap);
							i = j - 1;
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			LOG.error("Failed to load deobfuscation map file '{}'", proGuardMapFile.toAbsolutePath(), e);
		}
	}
}
