package routines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/** Sysinflame
 * 
 * @author Benjamin Baum
 * @author Thomas Richter */
public class SysInflame {

	private static final String SPECIAL_ALL_SOURCE_TOKEN = "";

	private static final String FILE_NAME_PART_SEPARATOR = "_";

	private static Map<String, Trait> codeBookTraits;

	private static Set<String> discreteTraitIds;

	private static Set<String> discreteTraitNames;

	private static Map<Integer, Trait> headerTraits;

	private static Map<String, List<Trait>> sourceTokenTraitMap;

	private static Map<String, List<Trait>> headerSourceTokenTraitMap;

	private static Map<String, List<Integer>> headerColumnNumbersPerSourceToken;

	public static String[] changeAndAddCol(String[] row, int changeColumn, IModifyCell modifierOriginalColumn,
			IModifyCell modifierAddColumn) {
		String[] newRow = new String[row.length + 1];
		newRow[changeColumn] = modifierOriginalColumn.modify(row[changeColumn]);
		newRow[changeColumn + 1] = modifierAddColumn.modify(row[changeColumn]);
		if (changeColumn == 0) {
			System.arraycopy(row, 1, newRow, 2, row.length - 1);
		} else {
			System.arraycopy(row, 0, newRow, 0, changeColumn);
			System.arraycopy(row, changeColumn + 1, newRow, changeColumn + 2, row.length - changeColumn - 1);
		}
		return newRow;
	}

	public static void prepareFilesInDir(String basePath, List<String> files, File codebook,
			String codeBookHeaderRegExp, char quoteChar, char inputDelim, char inputDelimCodeBook,
			boolean replaceHeader, boolean splitFiles, String inFolder, String outFolder) throws IOException {
		parseCodeBook(codebook, quoteChar, inputDelimCodeBook, splitFiles);
		System.out.println(files);
		for (String file : files) {
			File input = new File(basePath + File.separator + inFolder + File.separator + file);
			File output = new File(basePath + File.separator + outFolder + File.separator
					+ insertStringInFilename(file, ""));
			SysInflame.processFile(input, output, codebook, codeBookHeaderRegExp, quoteChar, inputDelim,
					inputDelimCodeBook, replaceHeader, splitFiles);
		}
	}

	private static void addValue(DiscreteTrait trait, String[] traitArray) {
		TraidValue tv = new TraidValue();
		tv.value = traitArray[7];
		tv.description = traitArray[8];
		trait.addValue(tv);
	}

	private static String calcCellValue(String[] actualLine, int colCount, Trait trait) {
		String value;
		String originalValue;
		if (actualLine == null) {
			originalValue = null;
		} else if (actualLine.length == 0) {
			originalValue = null;
		} else if (actualLine.length - 1 < colCount) {
			originalValue = "";
		} else {
			originalValue = actualLine[colCount];
		}
		if (null != trait && null != originalValue && discreteTraitIds.contains(trait.id)) {
			DiscreteTrait discreteTrait = (DiscreteTrait) trait;
			Map<String, TraidValue> traidValues = discreteTrait.traidValues;
			String key = originalValue;
			TraidValue traidValue = traidValues.get(key);
			if (null == traidValue) {
				if (key.isEmpty()) {
					value = originalValue;
				} else {
					throw new RuntimeException("in col=" + discreteTrait.description + " cell with value="
							+ originalValue + " no TraidValue for key=" + key + " found.");
				}
			} else {
				value = traidValue.description;
			}
		} else {
			value = originalValue;
		}
		return value;
	}

	private static void createPath(String dirName) throws IOException {
		File dir = new File(dirName);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
	}

	private static Map<String, CSVWriter> createWriters(char outputSeparator, File sourceFile,
			Map<String, List<Trait>> headerSourceTokenTraitMap) throws IOException {
		Map<String, CSVWriter> writers = new LinkedHashMap<String, CSVWriter>();
		Set<String> keySet = headerSourceTokenTraitMap.keySet();
		for (String sourceName : keySet) {
			String name = sourceFile.getName();
			String parent = sourceFile.getParent();
			createPath(parent);
			String fileName;
			if (sourceName.isEmpty()) {
				fileName = name;
			} else {
				List<Trait> list = headerSourceTokenTraitMap.get(sourceName);
				Trait trait = list.get(0);
				String sourceDescription = trait.sourceDescription;
				fileName = "POPGEN" + FILE_NAME_PART_SEPARATOR + "BSPSPC" + FILE_NAME_PART_SEPARATOR
						+ getXXFromFilename(sourceFile) + FILE_NAME_PART_SEPARATOR + sourceDescription + ".csv";
			}
			String newFileName;
			newFileName = parent + File.separator + fileName;
			CSVWriter csvWriter = new CSVWriter(new FileWriter(newFileName), outputSeparator,
					CSVWriter.NO_QUOTE_CHARACTER);
			writers.put(sourceName, csvWriter);
		}
		return writers;
	}

	private static CSVReader getCSVReader(Reader reader, CSVReaderConfig readerConfig) {
		char separator = readerConfig.separator;
		char quotechar = readerConfig.quotechar;
		return new CSVReader(reader, separator, quotechar);
	}

	private static String getXXFromFilename(File sourceFile) {
		String rt = "XX";
		Pattern p = Pattern.compile(".*_(BL|F1)_.*");
		Matcher m = p.matcher(sourceFile.getName());
		if (m.matches()) {
			rt = m.group(1);
		}
		return rt;
	}

	private static String insertStringInFilename(String fileName, String insert) {
		String newFileName;
		if (insert != null && insert.isEmpty()) {
			newFileName = fileName;
		} else {
			String regex = "(.*)\\.([a-z]+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fileName);
			if (matcher.matches()) {
				newFileName = matcher.group(1) + "_" + insert + "." + matcher.group(2);
			} else {
				newFileName = fileName + "_" + insert;
			}
		}
		return newFileName;
	}

	private static List<String[]> parseCodeBook(File codebook, char quoteChar, char inputDelimCodeBook,
			boolean splitFiles) throws FileNotFoundException, IOException {
		CSVReaderConfig readerConfig = new CSVReaderConfig(codebook);
		FileReader reader = new FileReader(codebook);
		CSVReader codebookInput = new CSVReader(reader, inputDelimCodeBook, quoteChar);
		codebookInput = getCSVReader(reader, readerConfig);
		List<String[]> codebookList = codebookInput.readAll();
		codeBookTraits = new LinkedHashMap<String, Trait>(codebookList.size() / 7);
		sourceTokenTraitMap = new LinkedHashMap<String, List<Trait>>();
		discreteTraitIds = new HashSet<String>();
		discreteTraitNames = new HashSet<String>();
		codebookList.remove(0);// remove header
		Trait trait;
		for (String[] traitArray : codebookList) {
			String type = traitArray[6];
			if ("discrete".equals(type)) {
				trait = putDiscreteTrait(traitArray);
				discreteTraitIds.add(trait.id);
				discreteTraitNames.add(trait.name);
			} else if ("continuous".equals(type)) {
				trait = putContinuousTrait(traitArray);
			} else {
				throw new RuntimeException("type (" + type + ") is unknown");
			}
			putTrait2SourceTokenMap(sourceTokenTraitMap, trait);
		}
		if (codebookInput != null) {
			codebookInput.close();
		}
		if (reader != null) {
			reader.close();
		}
		return codebookList;
	}

	private static List<String> parseHeader(boolean replaceHeader, String[] header, boolean splitFiles, File output,
			char outputSeparator, String codeBookHeaderRegExp) throws IOException {
		headerTraits = new LinkedHashMap<Integer, Trait>();
		headerColumnNumbersPerSourceToken = new LinkedHashMap<String, List<Integer>>();
		List<String> headerNice = new LinkedList<String>();
		Pattern p = Pattern.compile(codeBookHeaderRegExp);
		String newColName = null;
		for (int colPos = 0; colPos < header.length; colPos++) {
			String head = header[colPos];
			Matcher matcher = p.matcher(head);
			boolean isPopGenTrait = matcher.matches();
			if (isPopGenTrait) {
				String key = matcher.group(1);
				if (codeBookTraits.containsKey(key)) {
					Trait trait = codeBookTraits.get(key);
					newColName = trait.name;
					headerTraits.put(colPos, trait);
					String sourceName = trait.sourceToken;
					putColNumber2SourceNameMap(colPos, sourceName, headerColumnNumbersPerSourceToken);
				} else {
					throw new RuntimeException("traits doesn't contain key=" + key + ".");
				}
			} else {
				newColName = head;
				if (splitFiles) {
					Set<String> sourceNames = sourceTokenTraitMap.keySet();
					for (String sourceName : sourceNames) {
						putColNumber2SourceNameMap(colPos, sourceName, headerColumnNumbersPerSourceToken);
					}
				}
			}
			headerNice.add(newColName);
		}
		if (headerTraits.isEmpty()) {
			List<Integer> colNumbers = headerColumnNumbersPerSourceToken.values().iterator().next();
			headerColumnNumbersPerSourceToken = new LinkedHashMap<String, List<Integer>>(1);
			headerColumnNumbersPerSourceToken.put(SPECIAL_ALL_SOURCE_TOKEN, colNumbers);
		}
		return headerNice;
	}

	private static void processFile(File input, File output, File codebook, String codeBookHeaderRegExp,
			char quoteChar, char inputDelim, char inputDelimCodeBook, boolean replaceHeader, boolean splitFiles)
			throws IOException {
		CSVReader reader = new CSVReader(new FileReader(input), inputDelim, quoteChar);
		char outputSeparator = inputDelim;
		String[] header = reader.readNext();
		List<String> headerNice = parseHeader(replaceHeader, header, splitFiles, output, outputSeparator,
				codeBookHeaderRegExp);
		headerSourceTokenTraitMap = new LinkedHashMap<String, List<Trait>>(1);
		if (headerTraits.isEmpty()) {
			List<Trait> traits = sourceTokenTraitMap.values().iterator().next();
			headerSourceTokenTraitMap.put(SPECIAL_ALL_SOURCE_TOKEN, traits);
		} else {
			headerSourceTokenTraitMap.putAll(sourceTokenTraitMap);
		}
		Map<String, CSVWriter> writers = null;
		writers = createWriters(outputSeparator, output, headerSourceTokenTraitMap);
		writeHeader(replaceHeader, header, splitFiles, writers, output, outputSeparator, headerNice,
				headerColumnNumbersPerSourceToken);
		String[] nextOutputLine;
		String[] actualLine;
		while ((actualLine = reader.readNext()) != null) { // go through all
															// lines
			nextOutputLine = new String[header.length];
			for (int colCount = 0; colCount < header.length; colCount++) {
				Trait trait = headerTraits.get(Integer.valueOf(colCount));
				String value;
				value = calcCellValue(actualLine, colCount, trait);
				nextOutputLine[colCount] = value;
			}
			writeRow2Files(nextOutputLine, writers, headerColumnNumbersPerSourceToken);
		}
		for (CSVWriter writer : writers.values()) {
			writer.close();
		}
		if (reader != null) {
			reader.close();
		}
	}

	private static void putColNumber2SourceNameMap(int colPos, String sourceName,
			Map<String, List<Integer>> headerColumnNumbersPerSourceToken) {
		List<Integer> list;
		if (headerColumnNumbersPerSourceToken.containsKey(sourceName)) {
			list = headerColumnNumbersPerSourceToken.get(sourceName);
		} else {
			list = new ArrayList<Integer>();
			headerColumnNumbersPerSourceToken.put(sourceName, list);
		}
		list.add(colPos);
	}

	private static Trait putContinuousTrait(String[] traitArray) {
		Trait trait = new ContinuousTrait(traitArray);
		putTraitFields(traitArray, trait);
		codeBookTraits.put(trait.id, trait);
		return trait;
	}

	private static Trait putDiscreteTrait(String[] traitArray) {
		Trait trait = new DiscreteTrait(traitArray);
		if (codeBookTraits.containsKey(trait.id)) {
			trait = codeBookTraits.get(trait.id);
			addValue((DiscreteTrait) trait, traitArray);
		} else {
			putTraitFields(traitArray, trait);
			addValue((DiscreteTrait) trait, traitArray);
			codeBookTraits.put(trait.id, trait);
		}
		return trait;
	}

	private static void putTrait2SourceTokenMap(Map<String, List<Trait>> sourceTokenMap, Trait trait) {
		String sourceToken = trait.sourceToken;
		List<Trait> list;
		if (sourceTokenMap.containsKey(sourceToken)) {
			list = sourceTokenMap.get(sourceToken);
		} else {
			list = new LinkedList<Trait>();
			sourceTokenMap.put(sourceToken, list);
		}
		list.add(trait);
	}

	private static void putTraitFields(String[] traitArray, Trait trait) {
		trait.name = traitArray[1];
		trait.description = traitArray[2];
		trait.sourceId = traitArray[3];
		trait.sourceToken = traitArray[4];
		trait.sourceDescription = traitArray[5];
	}

	private static void writeHeader(boolean replaceHeader, String[] header, boolean splitFiles,
			Map<String, CSVWriter> writers, File output, char outputSeparator, List<String> headerNice,
			Map<String, List<Integer>> headerColumnNumbersPerSourceToken) throws IOException {
		if (replaceHeader) {
			header = headerNice.toArray(new String[0]);
		}
		writeRow2Files(header, writers, headerColumnNumbersPerSourceToken);
	}

	private static void writeRow2Files(String[] row, Map<String, CSVWriter> writers,
			Map<String, List<Integer>> columnNumbersPerSourceName) throws IOException {
		Set<String> sourceNames = writers.keySet();
		for (String sourceName : sourceNames) {
			CSVWriter writer = writers.get(sourceName);
			List<Integer> columnNumbers = columnNumbersPerSourceName.get(sourceName);
			List<String> nextLine = new ArrayList<String>();
			for (Integer columnNumber : columnNumbers) {
				nextLine.add(row[columnNumber]);
			}
			writer.writeNext(nextLine.toArray(new String[0]));
			writer.flush();
		}
	}
}

class ContinuousTrait extends Trait {
	public ContinuousTrait(String[] traitArray) {
		super(traitArray);
		type = "continuous";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContinuousTrait [sourceDescription=");
		builder.append(sourceDescription);
		builder.append(", sourceToken=");
		builder.append(sourceToken);
		builder.append(", sourceId=");
		builder.append(sourceId);
		builder.append(", description=");
		builder.append(description);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}

class CSVReaderConfig {

	public char separator;
	public char quotechar;

	public CSVReaderConfig(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String firstLine = bufferedReader.readLine();
		int countTab = firstLine.split("\t").length - 1;
		int countHochkomma = firstLine.split("\"").length - 1;
		int countSemicolon = firstLine.split(";").length - 1;
		if (countSemicolon > countTab * 2) {
			separator = ';';
		} else {
			separator = '\t';
		}
		if (countHochkomma > 0) {
			quotechar = '"';
		} else {
			// quotechar = (Character) null;
		}
		bufferedReader.close();
	}

}

class DiscreteTrait extends Trait {
	Map<String, TraidValue> traidValues;

	public DiscreteTrait(String[] traitArray) {
		super(traitArray);
		traidValues = new LinkedHashMap<String, TraidValue>();
		type = "discrete";
	}

	public void addValue(TraidValue tv) {
		traidValues.put(tv.value, tv);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiscreteTrait [traidValues=");
		builder.append(traidValues);
		builder.append(", sourceDescription=");
		builder.append(sourceDescription);
		builder.append(", sourceToken=");
		builder.append(sourceToken);
		builder.append(", sourceId=");
		builder.append(sourceId);
		builder.append(", description=");
		builder.append(description);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}

interface IModifyCell {
	String modify(String cell);
}

class TraidValue {
	public String description;
	public String value;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TraidValue [description=");
		builder.append(description);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
}

abstract class Trait {
	public String sourceDescription;
	public String sourceToken;
	public String sourceId;
	public String description;
	public String name;
	public String type;
	public String id;

	public Trait(String[] traitArray) {
		id = traitArray[0];
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Trait [sourceDescription=");
		builder.append(sourceDescription);
		builder.append(", sourceToken=");
		builder.append(sourceToken);
		builder.append(", sourceId=");
		builder.append(sourceId);
		builder.append(", description=");
		builder.append(description);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
