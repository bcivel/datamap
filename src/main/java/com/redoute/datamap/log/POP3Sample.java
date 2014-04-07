/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.log;

// Import the Commons/Net classes
import org.springframework.stereotype.Service;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Service
public class POP3Sample {

  public static void test() throws Exception {

  String host = "mymail.pprgroup.net";
  String user = "bcivel@siege.red";
  String password = "BCjan14!";
  
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.HOUR, -24);
        Date yesterday = cal.getTime();
        System.out.print("Date = " + yesterday);
         // Getting now.
  
  // Get system properties
  Properties properties = System.getProperties();

  // Get the default Session object.
  Session session = Session.getDefaultInstance(properties);

  // Get a Store object that implements the specified protocol.
  Store store = session.getStore("pop3");

  //Connect to the current host using the specified username and password.
  store.connect(host, user, password);

  //Create a Folder object corresponding to the given name.
  Folder folder = store.getFolder("inbox");

  // Open the Folder.
  folder.open(Folder.READ_ONLY);

  // Get the messages from the server
  Message[] messages = folder.getMessages();

  // Display message.
  for (int i = 0; i < messages.length; i++) {
      if (messages[i].getSentDate().after(yesterday)) {
  System.out.println("------------ Message " + (i + 1) + " ------------");
  // Here's the big change...
  String from = InternetAddress.toString(messages[i].getFrom());
  if (from != null) {
  System.out.println("From: " + from);
  }
  String replyTo = InternetAddress.toString(
  messages[i].getReplyTo());
  if (replyTo != null) {
  System.out.println("Reply-to: " + replyTo);
  }
  String to = InternetAddress.toString(
  messages[i].getRecipients(Message.RecipientType.TO));
  if (to != null) {
  System.out.println("To: " + to);
  }
  String cc = InternetAddress.toString(
  messages[i].getRecipients(Message.RecipientType.CC));
  if (cc != null) {
  System.out.println("Cc: " + cc);
  }
  String bcc = InternetAddress.toString(
  messages[i].getRecipients(Message.RecipientType.BCC));
  if (bcc != null) {
  System.out.println("Bcc: " + to);
  }
  String subject = messages[i].getSubject();
  if (subject != null) {
  System.out.println("Subject: " + subject);
  }
  Date sent = messages[i].getSentDate();
  if (sent != null) {
  System.out.println("Sent: " + sent);
  }
  String message = messages[i].getContent().toString();
  if (message != null) {
  System.out.println("Message: " + message);
  }
  Date received = messages[i].getReceivedDate();
  if (received != null) {
  System.out.println("Received: " + received);
  }
  System.out.println();
  }
  }
  folder.close(true);
  store.close();
  
  }
} 
