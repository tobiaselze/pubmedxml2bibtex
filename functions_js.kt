// functions for javascript:

fun HTMLCollection.toElementList() : List<Element>{
	return (0 until this.length)
		.toList()
		.map { this.item(it)!! }
}

// remove the double backslashes and quoted dollars for TeX output:
val unquoteTeX = { s: String ->
	s.replace("""\\\$""".toRegex(), """$""")
	.replace("""\\\\""".toRegex(), """\""")
}

fun stripAccents(s: String): String 
{
	return js("""s.normalize('NFD').replace(/[\u0300-\u036f]/g, "")""")
}

fun main(args: Array<String>) {
	val button = document.getElementById("convertbutton") as HTMLButtonElement
	val inputarea = document.getElementById("xmlinput") as HTMLTextAreaElement
	val outputarea = document.getElementById("biboutput") as HTMLTextAreaElement
	val checkbox = document.getElementById("keyFromPMID") as HTMLInputElement
	val parser = DOMParser()
	button.addEventListener("click", {
		val xmlStr = inputarea.value
		val keyFromPMID = checkbox.checked
		val doc = parser.parseFromString(xmlStr, "text/xml")
		outputarea.value = unquoteTeX(pubmedxmlToBib(doc, keyFromPMID))
	})
}



