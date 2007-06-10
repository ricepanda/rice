/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.kuali.core.bo.Attachment;
import org.kuali.core.bo.PersistableBusinessObject;

/**
 * Defines the methods common to all AttachmentService implementations
 *
 *
 */
public interface AttachmentService {
    /**
     * Stores the given fileContents and returns referring Attachment object whieh acts as a momento to the archived object.
     *
     * @param document TODO
     * @param foo
     *
     * @return Attachment
     * @throws IOException
     */
    public Attachment createAttachment(PersistableBusinessObject parent, String uploadedFileName, String mimeType, int fileSize, InputStream fileContents, String attachmentType) throws IOException;

    /**
     * Retrieves a given Attachments contents from the corresponding Attachment object
     *
     * @param documentAttachment
     *
     * @return OutputStream
     * @throws IOException
     */
    public InputStream retrieveAttachmentContents(Attachment attachment) throws IOException;

    /**
     * Deletes a given DocumentAttachment contents from the corresponding Attachment object
     *
     * @param documentAttachment
     */
    public void deleteAttachmentContents(Attachment attachment);
    
    /**
     * 
     * Moves attachments on notes from the pending directory to the real one
     * @param notes
     * @param objectId
     */
    public void moveAttachmentsWherePending(List notes, String objectId);
    
    /**
     * Deletes pending attachments that were last modified before the given time.  Java does not have easy access to a file's creation
     * time, so we use modification time instead.
     * 
     * @param modificationTime the number of milliseconds since "the epoch" (i.e.January 1, 1970, 00:00:00 GMT).  java.util.Date and java.util.Calendar's
     *  methods return time in this format.  If a pending attachment was modified before this time, then it will be deleted (unless an error occurs)
     */
    public void deletePendingAttachmentsModifiedBefore(long modificationTime);
}
