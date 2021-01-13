package fr.ag2rlamondiale.hec.batchsupervisor.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SupervisorResult {
	OK("Ok"),
	OUT_SLOT_KO("KO plage"),
	LAUNCH_KO("KO lancement"),
	PROCEED_KO("KO traitement");

	private String text;

	public String toString(){
		return this.text;
	}
}
