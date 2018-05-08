// functions for JVM:

fun NodeList.toElementList() : List<Element>{
	return (0 until this.length)
			.toList()
			.map { this.item(it) as Element }
}

fun main(args: Array<String>) {
	val infile = if (args.size == 0) {
		System.err.println("Warning: no input file specified. Defaulting to pubmed_result.xml\n")
		"pubmed_result.xml"
	} else
		args[0]
	
	val bufferedReader: BufferedReader = File(infile).bufferedReader()
	val xmlString = bufferedReader.use { it.readText() }
	val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(xmlString.reader()))
	
	val converted = pubmedxmlToBib(xmlDoc)
	
	println(converted)
	
}

