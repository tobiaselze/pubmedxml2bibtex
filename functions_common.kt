// functions that are shared between platforms:

fun pubmedxmlToBib(xmlDoc: Document, keyFromPMID: Boolean = false) : String {
	//xmlDoc.documentElement.normalize()
	val entrylist = xmlDoc.getElementsByTagName("PubmedArticle").toElementList()
	if(entrylist.size<1)
		return("ERROR: Input string does not contain PubmedArticle entries. Nothing to convert.\n")
	else {
		fun processEntry(entry: Element) : String{
			fun getelems(el: Element, s: String) : List<Element> {
				return el.getElementsByTagName(s).toElementList()
			}
			
			try {
				val article = getelems(entry, "Article").first()

				val quoteTeX = { s: String ->
					s.replace("&".toRegex(), "\\\\&")
					.replace("%".toRegex(), "\\\\%")
					.replace("<".toRegex(), "\\$<\\$")
					.replace(">".toRegex(), "\\$>\\$")
					.replace("±".toRegex(), "\\$\\\\pm\\$")
					.replace(" ".toRegex(), " ")
					.replace("≤".toRegex(), "\\$\\\\leq\\$")
					.replace("≥".toRegex(), "\\$\\\\geq\\$")
				}
				fun contOrEmpty(s: String, el: Element = article) : String{
					val elist = getelems(el, s)
					val joinedString = if(elist.size < 1) "" else elist.map{it.textContent}.joinToString(" ")
					return quoteTeX(joinedString)
				}
				
				// DOI, PMID, PMC:
				val aidlist = getelems(entry, "ArticleId") 
				fun getIdent(idlabel: String) : String {
					val index = aidlist.map{it.attributes.item(0)?.nodeValue ?: ""}.indexOf(idlabel)
					return if(index < 0) "" else aidlist.get(index)?.textContent ?: ""
				}
				val doi = getIdent("doi")
				val pmid = getIdent("pubmed")
				val pmc = getIdent("pmc")
				
				// Authors:
				val authorlist = getelems(article, "AuthorList").first()
				val lastnames = getelems(authorlist, "LastName")
					.map{
						val name = it?.textContent ?: ""
						if(name == name.toUpperCase())
							name.toLowerCase().capitalize()
						else
							if(name.drop(1).contains("[A-Z]".toRegex())) 
								"{"+name+"}" 
							else 
								name
					}
					
				val firstnames = getelems(authorlist, "ForeName")
					.map{
						(it?.textContent ?: "")
						.replace("([A-Z])$".toRegex(), "$1.")
						.replace("([A-Z]) ".toRegex(), "$1. ")
					}
				
				
				val authorstring = 
					if(lastnames.size<1) 
						"{" + getelems(authorlist, "CollectiveName").first().textContent + "}"
					else
						(firstnames zip lastnames)
						.map{it.first + " " + it.second}
						.joinToString(" and ")
				
				val journal = getelems(article, "Journal").first()
				val year = contOrEmpty("Year", journal)
				
				val bibtags = listOf("author", "title", "journal", "journalISOAbbrev", "volume", "number", "year", "pages", "pmid", "doi", "pmc", "abstract")
				val bibcontents = listOf(
					authorstring, 
					contOrEmpty("ArticleTitle")
						.replace("([:.] )([A-Z])".toRegex(), "$1{$2}")
						.replace("([A-Z][A-Z]+)".toRegex(), "{$1}"),
					contOrEmpty("Title", journal),
					contOrEmpty("ISOAbbreviation", journal),
					contOrEmpty("Volume", journal),
					contOrEmpty("Issue", journal),
					year,
					contOrEmpty("MedlinePgn").replace("\\-".toRegex(), "--"),
					pmid,
					doi,
					pmc,
					contOrEmpty("AbstractText"))
				val bibentries = bibtags zip bibcontents
				// exclude empty entries (or should we keep them?):
				val bibentriesNonEmpty = bibentries.filter{it.second != ""}
				val bibTeXListString = bibentriesNonEmpty.map{ it.first + " = {" + it.second + "}"}.joinToString(",\n  ")
				
				// BibTeX Key:
				val key = if(keyFromPMID || lastnames.size < 1) 
						"pmid$pmid"
					else {
						val lnames = lastnames.map{
							it.replace("\\{".toRegex(), "")
							.replace("\\}".toRegex(), "")
							.replace(" ".toRegex(), "")
						}
						//val lnames = lastnames
						val authstring = if(lnames.size < 3) 
								lnames.joinToString("")
							else
								lnames.first() + "EtAl"
						authstring + year
					}
				
				return "@article{$key,\n  $bibTeXListString\n}"
			}
			catch (e: NoSuchElementException) {
				return "Invalid XML"
			}
		}
		//entrylist.forEach{processEntry(it)}	// this works as well
		return entrylist.map(::processEntry).joinToString("\n\n")
	}
}

