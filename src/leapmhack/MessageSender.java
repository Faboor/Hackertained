/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leapmhack;

import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.MessageResponse;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author blacklight
 */

public class MessageSender {
  BigInteger phoneNum;
  List<BigInteger> phones = new ArrayList<BigInteger>();


  final MessageBirdService apikey = new MessageBirdServiceImpl("live_aN03UsAhambvM9ATA1X05otTc");
  final MessageBirdClient messageBirdClient = new MessageBirdClient(apikey);


  public MessageSender(BigInteger phoneNum) {
    this.phoneNum = phoneNum;
    phones.add(phoneNum);
  }

  public void sendMsg(int likes) throws UnauthorizedException, GeneralException {
    final MessageResponse response = messageBirdClient.sendMessage("MessageBird", "Congratulations! "
        + likes + " people like your stuff", phones);
  }


}
