package com.example.solace.decode.controllers;

import com.example.solace.decode.Services.ChannelService;
import com.example.solace.decode.messaging.MessagingService;
import com.example.solace.decode.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "channels")
public class ChannelController {

   private final ChannelService channelService;
   private final MessagingService messagingService;

   @Autowired
    public ChannelController(ChannelService channelService, MessagingService messagingService){
       this.channelService = channelService;
       this.messagingService = messagingService;
   }

    @GetMapping
    public List<Channel> getChannels() {
       return channelService.getChannels();
    }

    @PostMapping
    public void createChannel(@RequestBody  Channel channel) throws Exception {
        channelService.createChannel(channel);
        messagingService.publish("channels", channel);
    }
}

