// functions for JVM:

fun NodeList.toElementList() : List<Element>{
	return (0 until this.length)
			.toList()
			.map { this.item(it) as Element }
}

fun stripAccents(s: String): String 
{
	var string = Normalizer.normalize(s, Normalizer.Form.NFD)
	return Regex("\\p{InCombiningDiacriticalMarks}+").replace(string, "")
}


fun main(args: Array<String>) {
	
	if(args.size > 0 && (args[0]=="-h" || args[0]=="-help" || args[0]=="--help")) {
		println(
"""Usage: pubmedxml2bib [OPTION] [FILE]
convert PubMed XML stored in FILE to BibTeX (command line output)

If FILE is not specified, a warning is shown, and file pubmed_result.xml is used.

Options:
  -h, -help, --help	show this help and exit
  -keyFromPMID		generate BibTeX key from pubmed ID;
  			without this option, key is generated
  			from author list
""")
	} else {
		val keyFromPMID = (args.size > 0 && args[0]=="-keyFromPMID") 
		
		val infileIndex = if(keyFromPMID) 1 else 0
		
		val infile = if (args.size == infileIndex) {
			System.err.println("Warning: no input file specified. Defaulting to pubmed_result.xml\n")
			"pubmed_result.xml"
		} else 
			args[infileIndex]
		
		val bufferedReader: BufferedReader = File(infile).bufferedReader()
		val xmlString = bufferedReader.use { it.readText() }
		val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(xmlString.reader()))
		
		val converted = pubmedxmlToBib(xmlDoc, keyFromPMID)
		
		println(converted)
	}
	
}

