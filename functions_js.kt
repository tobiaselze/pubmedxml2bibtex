// functions for javascript:

fun getentrylist(doc: Document) : NodeList {
	return doc.getElementsByName("PubmedArticle")
}

fun getnodes(n: Node, s: String) : NodeList {
	return (n as Document).getElementsByName(s)
}

fun safeitem(nl: NodeList, i: Int) : Node {
	val n = nl.item(i)
	if(n == null)
		throw IllegalArgumentException("safeitem: empty")
	else
		return n
}

fun NodeList.forEach(f: (Node) -> Unit) {
	(0 until this.length)
			.asSequence()
			.map { safeitem(this, it) }
			.forEach { f(it) }
}

fun NodeList.map(f: (Node) -> String) : List<String> {
	return (0 until this.length)
			.toList()
			.map { f(safeitem(this, it)) }
}


fun main(args: Array<String>) {
	val button = document.getElementById("convertbutton") as HTMLButtonElement
	val inputarea = document.getElementById("xmlinput") as HTMLTextAreaElement
	val outputarea = document.getElementById("biboutput") as HTMLTextAreaElement
	val parser = DOMParser()
	button.addEventListener("click", {
		val xmlStr = inputarea.value
		val doc = parser.parseFromString(xmlStr, "text/xml")
		val fc = doc.firstChild!!
		//outputarea.value = pubmedxmlToBib(fc)
		//outputarea.value = doc.nodeName 
		val entrylist: NodeList = fc.getElementsByName("PubmedArticle")
		outputarea.value = entrylist.length.toString()
	})
}



