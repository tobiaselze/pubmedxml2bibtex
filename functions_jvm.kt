// functions for JVM:

fun getentrylist(doc: Document) : NodeList {
	return doc.getElementsByTagName("PubmedArticle")
}

fun getnodes(n: Node, s: String) : NodeList {
	return (n as Element).getElementsByTagName(s)
}

fun raiseError(msg: String){
	System.err.println(msg)
}

fun NodeList.forEach(f: (Node) -> Unit) {
	(0 until this.length)
			.asSequence()
			.map { this.item(it) }
			.forEach { f(it) }
}

fun NodeList.map(f: (Node) -> String) : List<String> {
	return (0 until this.length)
			.toList()
			.map { f(this.item(it)) }
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

