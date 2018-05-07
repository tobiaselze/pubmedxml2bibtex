// functions for JVM:

fun NodeList.toElementList() : List<Element>{
	return (0 until this.length)
			.toList()
			.map { this.item(it) as Element }
}

fun main(args: Array<String>) {
	val infile = "/tmp/out.xml" //"/tmp/p.xml" //"/tmp/testfile1.xml" //"/tmp/pubmed_result.xml"
	val xlmFile: File = File(infile)
	val bufferedReader: BufferedReader = File(infile).bufferedReader()
	val xmlString = bufferedReader.use { it.readText() }
	val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(xmlString.reader()))
	
	val converted = pubmedxmlToBib(xmlDoc)
	
	println(converted)
	
}

