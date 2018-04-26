TARGETSCRIPT = pubmedxml2bib.kt
TARGETJVM = $(TARGETSCRIPT:.kt=.jar)
TARGETJS = $(TARGETSCRIPT:.kt=.js)

KOTLINC = kotlinc
CAT = cat 

all: $(TARGETSCRIPT) $(TARGETJVM)


$(TARGETSCRIPT): import_common.kt import_jvm.kt functions_common.kt functions_jvm.kt
	$(file >$@,#!/usr/bin/env kscript)
	$(CAT) $^ >> $@

$(TARGETJVM): $(TARGETSCRIPT)
	$(KOTLINC) $^ -include-runtime -d $@

