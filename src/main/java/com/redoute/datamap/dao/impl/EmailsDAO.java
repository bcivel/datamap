/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.dao.impl;

import com.redoute.datamap.dao.IEmailsDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.entity.Emails;
import com.redoute.datamap.log.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bcivel
 */
@Repository
public class EmailsDAO implements IEmailsDAO {

    @Autowired
    private DatabaseSpring databaseSpring;
    
    @Override
    public void insertEmails(Emails emails) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO emails (`id`,`from`,`replyto`,`to`,`cc`, `bcc`, `subject`, `sendDate`, `message`, `receiveddate`) ");
        query.append("VALUES (0,?,?,?,?,?,?,?,?,?)");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, emails.getFrom());
                preStat.setString(2, emails.getReplyTo());
                preStat.setString(3, emails.getTo());
                preStat.setString(4, emails.getCc());
                preStat.setString(5, emails.getBcc());
                preStat.setString(6, emails.getSubject());
                preStat.setString(7, emails.getSendDate());
                preStat.setString(8, emails.getMessage());
                preStat.setString(9, emails.getReceivedDate());

                preStat.executeUpdate();

            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }}
    
}
