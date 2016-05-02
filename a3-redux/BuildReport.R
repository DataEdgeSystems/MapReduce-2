
setwd(".")


# Load packages
require(knitr)
require(markdown)

# Create .md, .html, and .pdf files
knit("graphs.Rmd")
markdownToHTML('graphs.md', 'graphs.html', options=c("use_xhml"))
system("pandoc -s graphs.html -o graphs.pdf")

