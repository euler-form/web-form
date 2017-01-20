package net.eulerframework.web.module.file.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import net.eulerframework.common.util.Assert;
import net.eulerframework.web.core.base.service.impl.BaseService;
import net.eulerframework.web.module.file.dao.IAttachmentDao;
import net.eulerframework.web.module.file.entity.AbstractAttachment;
import net.eulerframework.web.module.file.exception.AttachmentNotFoundException;
import net.eulerframework.web.module.file.service.IAttachmentService;

public class AttachmentService extends BaseService implements IAttachmentService {
    
    private Collection<IAttachmentDao<AbstractAttachment>> attachmentDaos;

    public void setAttachmentDaos(Collection<IAttachmentDao<AbstractAttachment>> attachmentDaos) {
        this.attachmentDaos = attachmentDaos;
    }

    @Override
    public void addAttachment(AbstractAttachment attachment) {
        Assert.isNotNull(attachment, "attachment is null");
        Assert.isNotNull(attachment.getOwnerId(), "attachment owner id is null");
        
        
        this.getEntityDao(attachmentDaos, attachment.getClass()).save(attachment);
    }

    @Override
    public void updateAttachment(AbstractAttachment attachment) {
        Assert.isNotNull(attachment, "attachment is null");
        Assert.isNotNull(attachment.getOwnerId(), "attachment owner id is null");
        
        this.getEntityDao(attachmentDaos, attachment.getClass()).saveOrUpdate(attachment);
    }

    @Override
    public void deleteAttachment(AbstractAttachment attachment) {
        Assert.isNotNull(attachment, "attachment is null");
        Assert.isNotNull(attachment.getOwnerId(), "attachment owner id is null");
        
        this.getEntityDao(attachmentDaos, attachment.getClass()).delete(attachment);
    }

    @Override
    public <T extends AbstractAttachment> void deleteAttachment(Serializable ownerId, String attachmentId,
            Class<T> attachmentClass) throws AttachmentNotFoundException {
        Assert.isNotNull(ownerId, "attachment owner id is null");
        Assert.isNotNull(attachmentId, "attachment id is null");
        

        @SuppressWarnings("unchecked")
        IAttachmentDao<T> dao = (IAttachmentDao<T>) this.getEntityDao(attachmentDaos, attachmentClass);
        
        T data = dao.load(attachmentId);
        
        if(data == null || !data.getOwnerId().equals(ownerId))
            throw new AttachmentNotFoundException("Attachment id is "+attachmentId+" and owner id is "+ownerId+" not found");
        
        dao.deleteById(attachmentId);        
    }

    @Override
    public <T extends AbstractAttachment> void deleteAllAttachment(Serializable ownerId, Class<T> attachmentClass) {
        Assert.isNotNull(ownerId, "attachment owner id is null");
        
        List<T> data = this.loadAttachments(ownerId, attachmentClass);
        
        if(data == null || data.isEmpty())
            return;
        
        @SuppressWarnings("unchecked")
        IAttachmentDao<T> dao = (IAttachmentDao<T>) this.getEntityDao(attachmentDaos, attachmentClass);
        dao.deleteAll(data);
        
    }

    @Override
    public <T extends AbstractAttachment> T loadAttachment(Serializable ownerId, Class<T> attachmentClass) {
        Assert.isNotNull(ownerId, "attachment owner id is null");
        
        List<T> data = this.loadAttachments(ownerId, attachmentClass);
        
        if(data == null || data.isEmpty())
            return null;
        else
            return data.get(0);
    }

    @Override
    public <T extends AbstractAttachment> List<T> loadAttachments(Serializable ownerId, Class<T> attachmentClass) {
        Assert.isNotNull(ownerId, "attachment owner id is null");
        
        @SuppressWarnings("unchecked")
        IAttachmentDao<T> dao = (IAttachmentDao<T>) this.getEntityDao(attachmentDaos, attachmentClass);
        
        return dao.loadByOwnerId(ownerId);
    }

}