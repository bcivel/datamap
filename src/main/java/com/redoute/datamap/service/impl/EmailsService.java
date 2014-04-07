/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.service.impl;

import com.redoute.datamap.dao.IEmailsDAO;
import com.redoute.datamap.dao.impl.DatamapDAO;
import com.redoute.datamap.entity.Emails;
import com.redoute.datamap.service.IEmailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author bcivel
 */
@Service
public class EmailsService implements IEmailsService {

    @Autowired
    IEmailsDAO emailsDao;
    
    @Override
    public void insertEmails(Emails email) {
        emailsDao.insertEmails(email);
    }
    
}
