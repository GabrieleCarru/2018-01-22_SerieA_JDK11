package it.polito.tdp.seriea.model;

public class AnnoPunti implements Comparable<AnnoPunti> {

	
	private Integer anno;
	private int punti;
	
	/**
	 * @param anno
	 * @param punti
	 */
	public AnnoPunti(int anno, int punti) {
		super();
		this.anno = anno;
		this.punti = punti;
	}
	
	public Integer getAnno() {
		return anno;
	}
	public int getPunti() {
		return punti;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anno;
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
		AnnoPunti other = (AnnoPunti) obj;
		if (anno != other.anno)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return (anno -1) + "/" + anno + "   | " + punti + " punti.";
	}
	@Override
	public int compareTo(AnnoPunti o) {
		return this.getAnno().compareTo(o.getAnno());
	}
	
	
	
}
