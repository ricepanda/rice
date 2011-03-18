package org.kuali.rice.krms.framework.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kuali.rice.krms.api.Agenda;
import org.kuali.rice.krms.api.AssetResolver;
import org.kuali.rice.krms.api.Context;
import org.kuali.rice.krms.api.ExecutionEnvironment;

public class BasicContext implements Context {
	
	private Map<String, String> qualifiers;
	private List<Agenda> agendas;
	private List<AssetResolver<?>> assetResolvers;
	
	public BasicContext(Map<String, String> qualifiers, List<Agenda> agendas, List<AssetResolver<?>> assetResolvers) {
		this.qualifiers = qualifiers;
		this.agendas = agendas;
		this.assetResolvers = assetResolvers;
	}
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		if (assetResolvers != null) for (AssetResolver<?> assetResolver : assetResolvers) {
			environment.addAssetResolver(assetResolver);
		}
		List<Agenda> matchingAgendas = findMatchingAgendas(environment);
		for (Agenda matchingAgenda : matchingAgendas) {
			matchingAgenda.execute(environment);
		}
	}
	
	protected List<Agenda> findMatchingAgendas(ExecutionEnvironment environment) {
		List<Agenda> matchingAgendas = new ArrayList<Agenda>();
		for (Agenda agenda : agendas) {
			if (agenda.appliesTo(environment)) {
				matchingAgendas.add(agenda);
			}
		}
		return matchingAgendas;
	}

	@Override
	public boolean appliesTo(ExecutionEnvironment environment) {
		for (String contextQualifierName : qualifiers.keySet()) {
			String qualifierValue = qualifiers.get(contextQualifierName);
			String environmentQualifierValue = environment.getSelectionCriteria().getContextQualifiers().get(qualifierValue);
			if (!qualifierValue.equals(environmentQualifierValue)) {
				return false;
			}
		}
		return true;
	}
	
	public List<AssetResolver<?>> getAssetResolvers() {
		return Collections.unmodifiableList(assetResolvers);
	}

}
