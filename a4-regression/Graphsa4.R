setwd(getwd())

library(ggplot2)
library(scales)
dfRawData = read.csv('a4_regression_sa_200.csv', header = F, sep = ',', quote = "")

colnames(dfRawData) = c("Date", "Week", "MediaPrice")
dfRawDataNull = subset(dfRawData, (is.na(dfRawData$MediaPrice)))
dfRawDataNNull = subset(dfRawData, !(is.na(dfRawData$MediaPrice)))
colnames(dfRawDataNull) = c("Flight", "Time")

# Sorting by Date 
dfRawDataNNull = dfRawDataNNull[order(dfRawDataNNull$Date), ]
dfRawDataNNull$Date = as.Date( dfRawDataNNull$Date, '%Y-%m-%d')
plot.title = paste("Cheapest Flight = ", unique(dfRawDataNull$Flight), sep="")
plot.subtitle = paste("For Flight Time = ", unique(dfRawDataNull$Time), sep="")

ggplot(dfRawDataNNull, aes(Date, MediaPrice)) + 
  geom_line(colour="blue") +
  xlab("Period in Year(s)") + 
  ylab("Median Price in Dollar(s)") +
  ggtitle(bquote(atop(.(plot.title), atop(italic(.(plot.subtitle)), "")))) + 
  scale_x_date(labels = date_format("%Y"))


dfRawData = read.csv('a4_regression_sa_1.csv', header = F, sep = ',', quote = "")

colnames(dfRawData) = c("Date", "Week", "MediaPrice")
dfRawDataNull = subset(dfRawData, (is.na(dfRawData$MediaPrice)))
dfRawDataNNull = subset(dfRawData, !(is.na(dfRawData$MediaPrice)))
colnames(dfRawDataNull) = c("Flight", "Time")

# Sorting by Date 
dfRawDataNNull = dfRawDataNNull[order(dfRawDataNNull$Date), ]
dfRawDataNNull$Date = as.Date( dfRawDataNNull$Date, '%Y-%m-%d')
plot.title = paste("Cheapest Flight = ", unique(dfRawDataNull$Flight), sep="")
plot.subtitle = paste("For Flight Time = ", unique(dfRawDataNull$Time), sep="")

ggplot(dfRawDataNNull, aes(Date, MediaPrice)) + 
  geom_line(colour="blue") +
  xlab("Period in Year(s)") + 
  ylab("Median Price in Dollar(s)") +
  ggtitle(bquote(atop(.(plot.title), atop(italic(.(plot.subtitle)), "")))) + 
  scale_x_date(labels = date_format("%Y"))