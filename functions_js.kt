// functions for javascript:

fun HTMLCollection.toElementList() : List<Element>{
	return (0 until this.length)
		.toList()
		.map { this.item(it)!! }
}


fun main(args: Array<String>) {
	val button = document.getElementById("convertbutton") as HTMLButtonElement
	val inputarea = document.getElementById("xmlinput") as HTMLTextAreaElement
	val outputarea = document.getElementById("biboutput") as HTMLTextAreaElement
	val parser = DOMParser()
	button.addEventListener("click", {
		val xmlStr = inputarea.value
		val doc = parser.parseFromString(xmlStr, "text/xml")
		outputarea.value = pubmedxmlToBib(doc)
	})
}



