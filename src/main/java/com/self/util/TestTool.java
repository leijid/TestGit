/**
 * leijid
 *
 * TestTool.java
 *
 * 2014Äê10ÔÂ13ÈÕ
 */
package com.self.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.Tool;

/**
 * @author leijid
 * 
 */
public class TestTool implements Tool {

	public int run(InputStream in, OutputStream out, OutputStream err,
	        String... arguments) {
		InputStream inputStream = System.in;
		PrintStream printStream = System.out;
		return 0;
	}

	public Set<SourceVersion> getSourceVersions() {
		SourceVersion version = SourceVersion.RELEASE_6;
		Set<SourceVersion> set = new HashSet<SourceVersion>();
		set.add(version);
		return set;
	}

}
