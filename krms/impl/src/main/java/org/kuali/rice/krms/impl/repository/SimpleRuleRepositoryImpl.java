package org.kuali.rice.krms.impl.repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kuali.rice.krms.api.repository.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.ContextDefinition;
import org.kuali.rice.krms.api.repository.ContextSelectionCriteria;
import org.kuali.rice.krms.api.repository.RuleRepositoryService;

/**
 * This impl has some concurrency issues to consider 
 * @author gilesp
 *
 */
public class SimpleRuleRepositoryImpl implements RuleRepositoryService {
	
	ConcurrentMap<String, Set<ContextDefinition>> contextDefinitions = new ConcurrentHashMap<String, Set<ContextDefinition>>();
	
	
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krms.api.repository.RuleRepositoryService#selectContext(org.kuali.rice.krms.api.repository.ContextSelectionCriteria)
	 */
	@Override
	public ContextDefinition selectContext(
			ContextSelectionCriteria contextSelectionCriteria) {
		return selectContext(contextSelectionCriteria.getContextQualifiers());
	}
	
	

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krms.api.repository.RuleRepositoryService#getAgendaTree(java.lang.String)
	 */
	@Override
	public AgendaTreeDefinition getAgendaTree(String agendaId) {
		// TODO
		throw new UnsupportedOperationException("TODO!");
	}



	public ContextDefinition selectContext(Map<String, String> contextQualifiers) {
		Set<ContextDefinition> results = null;
		if (contextQualifiers != null) for (Entry<String,String> entry : contextQualifiers.entrySet()) {
			Set<ContextDefinition> qualifierMatches = contextDefinitions.get(entry.getKey()+"="+entry.getValue());
			if (null == results) {
				results = new HashSet<ContextDefinition>(qualifierMatches);
			} else {
				results.retainAll(qualifierMatches);
			}
		}
		if (results != null) {
			if (results.size() == 1) return results.iterator().next();
			else throw new IllegalArgumentException("ambiguous qualifiers");
		}
		return null;
	}
	
	public void addContextDefinitionMapping(Map<String, String> contextQualifiers, ContextDefinition contextDefinition) {
		if (contextQualifiers != null) for (Entry<String,String> entry : contextQualifiers.entrySet()) {
			String key = entry.getKey()+"="+entry.getValue();
			// TODO: synchronize here
			contextDefinitions.putIfAbsent(key, new HashSet<ContextDefinition>());
			contextDefinitions.get(key).add(contextDefinition);
		}
	}

}
