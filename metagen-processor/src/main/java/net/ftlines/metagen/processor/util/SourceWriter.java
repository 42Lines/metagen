/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ftlines.metagen.processor.util;

import java.io.IOException;
import java.io.OutputStream;

public class SourceWriter {
	private final OutputStream out;
	private int indent = 0;

	public SourceWriter(OutputStream out) {
		this.out = out;
	}

	public SourceWriter line(String line, Object... objects) throws IOException {
		indent();
		out.write(String.format(line, objects).getBytes());
		line();
		return this;
	}

	public SourceWriter line() throws IOException {
		out.write('\r');
		out.write('\n');
		return this;
	}

	public SourceWriter startBlock() throws IOException {
		line("{");
		indent++;
		return this;
	}

	public SourceWriter endBlock() throws IOException {
		indent--;
		line("}");
		return this;
	}

	public SourceWriter flush() throws IOException {
		out.flush();
		return this;
	}

	public void close() throws IOException {
		out.close();
	}

	private void indent() throws IOException {
		for (int i = 0; i < indent; i++) {
			out.write('\t');
		}
	}
}
