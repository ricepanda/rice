/*
 * Copyright 2007-2010 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.documentlink.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.jpa.criteria.Criteria;
import org.kuali.rice.core.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.documentlink.DocumentLink;
import org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentLinkDAOJpaImpl implements DocumentLinkDAO {

	
    @PersistenceContext(unitName = "kew-unit")
    private EntityManager entityManager;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
	/**
	 * double delete all links from orgn doc
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#deleteDocmentLinksByDocId(java.lang.Long)
	 */
	public void deleteDocmentLinksByDocId(Long docId) {
		List<DocumentLink> links = getLinkedDocumentsByDocId(docId);
		for(DocumentLink link: links){
			deleteDocumentLink(link);
		}
	}

	/**
	 * double delete a link
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#deleteDocumentLink(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public void deleteDocumentLink(DocumentLink link) {
		deleteSingleLinkFromOrgnDoc(link);
		deleteSingleLinkFromOrgnDoc(DocumentLinkDaoUtil.reverseLink(link));
	}

	/**
	 * get a link from orgn doc
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#getLinkedDocument(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public DocumentLink getLinkedDocument(DocumentLink link) {
		Criteria crit = new Criteria(DocumentLink.class.getName());
		crit.eq("orgnDocId", link.getOrgnDocId());
		crit.eq("destDocId", link.getDestDocId());
		return (DocumentLink) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
	}

	/**
	 * get all links from orgn doc
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#getLinkedDocumentsByDocId(java.lang.Long)
	 */
	public List<DocumentLink> getLinkedDocumentsByDocId(Long docId) {
		Criteria crit = new Criteria(DocumentLink.class.getName());
		crit.eq("orgnDocId", docId);
		return (List<DocumentLink>) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
	
	}

	/**
	 * add double link
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#saveDocumentLink(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public void saveDocumentLink(DocumentLink link) {
		if(getLinkedDocument(link) == null)
			entityManager.persist(link);
//		//if we want a 2-way linked pair
		DocumentLink rLink = DocumentLinkDaoUtil.reverseLink(link);
		if(getLinkedDocument(rLink) == null)
			entityManager.persist(link);

	}
	
	private void deleteSingleLinkFromOrgnDoc(DocumentLink link){
		DocumentLink cur = getLinkedDocument(link);
		entityManager.remove(cur) ;
	}

}
