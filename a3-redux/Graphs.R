
pdf("Rplots.pdf", width=5.0, height=4.5)

dfMedianData = read.csv("median_results.csv", header =FALSE)  # read from first sheet
dfMeanData = read.csv("mean_results.csv", header =FALSE)  # read from second sheet

colnames(dfMedianData) = c("Type", "Run", "Time")
colnames(dfMeanData) = c("Type", "Run", "Time")

c2 = toupper(c("seq_mean","multi_mean","pseudo_mean","cloud_mean"))
dfMeanData$Type = ordered(toupper(dfMeanData$Type), levels=c2)
boxplot(Time~Type, data = dfMeanData,
        horizontal=F,
        range = 0,
        las = 2,
        xaxt = "n",
        yaxt = "n",
        # xlab = "Run Environment", 
        ylab = "Time in Seconds", 
        main = "Mean : Run Config vs Time",
        cex.main = 1.5,   font.main= 1, col.main= "blue")
text(1:length(levels(dfMeanData$Type)), 
     srt = 45, offset = 0.5, adj = 0.5,
     labels = levels(dfMeanData$Type), xpd = TRUE,cex = 0.5)
axis(2, round(seq(0, max(dfMeanData$Time, na.rm= T), length.out = 15)), las = 2)

c1 = toupper(c("seq_median","multi_median","pseudo_median","pseudo_fast","cloud_median","cloud_fast"))
dfMedianData$Type = ordered(toupper(dfMedianData$Type), levels=c1)
boxplot(Time~Type, data = dfMedianData,
        horizontal=F,
        range = 0,
        las = 2,
        xaxt = "n",
        yaxt = "n",
        # xlab = "Run Environment", 
        ylab = "Time in Seconds", 
        main = "Median : Run Config vs Time",
        cex.main = 1.5,   font.main= 1, col.main= "blue")
text(1:length(levels(dfMedianData$Type)), 
     srt = 45, offset = 0.5, adj = 1,
     labels = levels(dfMedianData$Type), xpd = TRUE,cex = 0.5)
axis(2, round(seq(0, max(dfMedianData$Time, na.rm= T), length.out = 15)), las = 2)

c = toupper(c("seq_median","seq_mean","multi_mean","multi_median","pseudo_mean","pseudo_median","pseudo_fast","cloud_mean","cloud_median","cloud_fast"))
dfConsolidatedData = rbind(dfMedianData, dfMeanData)
dfConsolidatedData$Type = ordered(toupper(dfConsolidatedData$Type), levels=c)

boxplot(Time~Type, data = dfConsolidatedData,
        horizontal=F,
        range = 0,
        las = 2,
        xaxt = "n",
        yaxt = "n",
        # xlab = "Run Environment", 
        ylab = "Time in Seconds", 
        main = "Mean & Median : Run Config vs Time",
        cex.main = 1.5,   font.main= 1, col.main= "blue")
text(1:length(levels(dfConsolidatedData$Type)), 
     srt = 45, offset = 0.5, adj = 1,
     labels = levels(dfConsolidatedData$Type), xpd = TRUE,cex = 0.5)
axis(2, round(seq(0, max(dfConsolidatedData$Time, na.rm= T), length.out = 15)), las = 2)
