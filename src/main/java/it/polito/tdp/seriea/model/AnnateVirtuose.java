package it.polito.tdp.seriea.model;

public class AnnateVirtuose {

	private Season season;
	private int consecutivi;
	/**
	 * @param season
	 * @param consecutivi
	 */
	public AnnateVirtuose(Season season, int consecutivi) {
		super();
		this.season = season;
		this.consecutivi = consecutivi;
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public int getConsecutivi() {
		return consecutivi;
	}
	public void setConsecutivi(int consecutivi) {
		this.consecutivi = consecutivi;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + consecutivi;
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnateVirtuose other = (AnnateVirtuose) obj;
		if (consecutivi != other.consecutivi)
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AnnateVirtuose [season=" + season + ", consecutivi=" + consecutivi + "]";
	}
	
	
	
}
