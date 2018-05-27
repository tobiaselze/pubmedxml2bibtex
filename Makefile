KOTLINSTDLIBJS = /snap/kotlin/current/lib/kotlin-stdlib-js.jar

TARGETSCRIPT = pubmedxml2bib.kt
TARGETJVM = $(TARGETSCRIPT:.kt=.jar)
TARGETJS = $(TARGETSCRIPT:.kt=.js)

KOTLINC = kotlinc
KOTLINCJS = kotlinc-js
CAT = cat 
UNZIP = unzip

VERSION :=	$(shell cat pubmedxml2bib_version)

all: $(TARGETSCRIPT) $(TARGETJVM) $(TARGETJS) kotlin.js


$(TARGETSCRIPT): import_common.kt import_jvm.kt functions_common.kt pubmedxml2bib_version.kt functions_jvm.kt
	#$(file >$@,#!/usr/bin/env kscript)
	echo \#\!/usr/bin/env kscript > $@
	$(CAT) $^ >> $@
	chmod 755 $@

$(TARGETJVM): $(TARGETSCRIPT)
	$(KOTLINC) $^ -include-runtime -d $@

$(TARGETJS): import_common.kt import_js.kt functions_common.kt functions_js.kt
	$(CAT) $^ > $(TARGETSCRIPT:.kt=_js.kt)
	$(KOTLINCJS) -output $(TARGETJS) $(TARGETSCRIPT:.kt=_js.kt) 

kotlin.js: | $(KOTLINSTDLIBJS)
	$(UNZIP) $| $@

pubmedxml2bib_version.kt: pubmedxml2bib_version
	echo "// inserted by Makefile from pubmedxml2bib_version:" > $@
	echo "val version = \"$(VERSION)\"" >> $@
	echo >> $@


