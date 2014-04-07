/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.log;

// Import the Commons/Net classes
import com.redoute.datamap.entity.Emails;
import com.redoute.datamap.service.IEmailsService;
import com.sun.mail.imap.IMAPFolder;
import org.springframework.stereotype.Service;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class IMAP {

   @Autowired
   IEmailsService emailService;
   
  public void test() throws Exception {

  String host = "mymail.pprgroup.net";
  String user = "preveclient@siege.red";
  String password = "Laredoute2014";
  
  Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.HOUR, -24);
        Date yesterday = cal.getTime();
        System.out.print("Date = " + yesterday);
         // Getting now.
        
  
        IMAPFolder folder = null;
        Store store = null;
        Flag flag = null;
  
      // Get system properties
  Properties props = System.getProperties();
  props.setProperty("mail.store.protocol", "imaps");

  Session session = Session.getDefaultInstance(props, null);

  // Get a Store object that implements the specified protocol.
  store = session.getStore("imaps");

  //Connect to the current host using the specified username and password.
  store.connect(host, user, password);

  //Create a Folder object corresponding to the given name.
  folder = (IMAPFolder) store.getFolder("Inbox/Commande");

  // Open the Folder.
  folder.open(Folder.READ_ONLY);
    int messageCount = folder.getMessageCount();

   System.out.println("Total Messages:- " + messageCount);

  // Get the messages from the server
  Message[] messages = folder.getMessages();

    Emails email = new Emails();
  // Display message.
  for (int i = messages.length-1 ; i > messages.length-100; i--) {
      //if (messages[i].getContent().toString().contains("20140850747407")) {
  //System.out.println("------------ Message " + (i + 1) + " ------------");
  String from = InternetAddress.toString(messages[i].getFrom());
  if (from != null) {
  email.setFrom(from);
  }
  String replyTo = InternetAddress.toString(messages[i].getReplyTo());
  if (replyTo != null) {
  email.setReplyTo(replyTo);
  }
  String to = InternetAddress.toString(messages[i].getRecipients(Message.RecipientType.TO));
  if (to != null) {
  email.setTo(to);
  }
  String cc = InternetAddress.toString(messages[i].getRecipients(Message.RecipientType.CC));
  if (cc != null) {
  email.setCc(cc);
  }
  String bcc = InternetAddress.toString(messages[i].getRecipients(Message.RecipientType.BCC));
  if (bcc != null) {
  email.setBcc(bcc);
  }
  String subject = messages[i].getSubject();
  if (subject != null) {
  email.setSubject(subject);
  }
  Date sent = messages[i].getSentDate();
  if (sent != null) {
  email.setSendDate(sent.toString());
  }
  String message = messages[i].getContent().toString();
  if (message != null) {
  email.setMessage(message);
  }
  Date received = messages[i].getReceivedDate();
  if (received != null) {
  email.setReceivedDate(received.toString());
  }
  
  emailService.insertEmails(email);
  
  }
  //}
  folder.close(true);
  store.close();
  
  
  }
} 
