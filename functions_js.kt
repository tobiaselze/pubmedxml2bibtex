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

