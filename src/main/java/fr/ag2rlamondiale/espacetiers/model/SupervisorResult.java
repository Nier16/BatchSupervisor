package fr.ag2rlamondiale.espacetiers.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SupervisorResult {
	OK("Ok"),
	OUT_SLOT_KO("Ko plage"),
	LAUNCH_KO("Ko lancement"),
	PROCEED_KO("Ko traitement");

	private String text;

	public String toString(){
		return this.text;
	}
}
