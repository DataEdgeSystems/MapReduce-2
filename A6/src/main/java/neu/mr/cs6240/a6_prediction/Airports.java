package neu.mr.cs6240.a6_prediction;

/**
 * Top 50 airports with most traffic and any other airport which is not in this
 * list is set as OTHER
 * 
 * @author Smitha Bangalore Naresh
 * @author Ajay Subramanya
 *
 */

public enum Airports {

	ATL(1), LAX(2), ORD(3), DFW(4), DEN(5), JFK(6), SFO(7), CLT(8), LAS(9), PHX(10), IAH(11), MIA(12), YYZ(13), SEA(
			14), MCO(15), EWR(16), MSP(17), DTW(18), BOS(19), PHL(20), LGA(21), FLL(22), BWI(23), IAD(24), SLC(25), MDW(
					26), DCA(27), HNL(28), YVR(29), SAN(30), TPA(31), PDX(32), YYC(33), YUL(34), STL(35), HOU(36), BNA(
							37), AUS(38), OAK(39), MCI(40), MSY(41), RDU(42), DAL(43), SNA(44), SJC(45), SMF(46), SAT(
									47), YEG(48), PIT(49), RSW(50), OTHER(51);

	private int value;

	private Airports(int value) {
		this.value = value;
	}

	public int val() {
		return value;
	}
};
