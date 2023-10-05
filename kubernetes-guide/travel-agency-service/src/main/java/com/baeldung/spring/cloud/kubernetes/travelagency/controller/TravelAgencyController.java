package com.baeldung.spring.cloud.kubernetes.travelagency.controller;

import com.baeldung.spring.cloud.kubernetes.travelagency.model.TravelDeal;
import com.baeldung.spring.cloud.kubernetes.travelagency.repository.TravelDealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TravelAgencyController {

    @Autowired
    private TravelDealRepository travelDealRepository;

    private static Integer counter = 0 ;
    private static final Log log = LogFactory.getLog(TravelAgencyController.class);

    @RequestMapping(method = GET, path = "/deals")
    public String deals() {
        log.info("Client is requesting new deals!");

        List<TravelDeal> travelDealList = travelDealRepository.findAll();
        log.info("travelDealList : " + travelDealList);

        if (!travelDealList.isEmpty()) {
            int randomDeal = new Random().nextInt(travelDealList.size());
            return travelDealList.get(randomDeal)
                .toString();
        } else {
            return "NO DEALS";
        }
    }

    @RequestMapping(method = GET, path = "/")
    @ResponseBody
    public String get() throws UnknownHostException {

        return getServerInfo();
    }

    @RequestMapping(method = GET, path = "/delay")
    @ResponseBody
    public String getWihDelay(@RequestParam Integer delaySeconds) throws UnknownHostException, InterruptedException {
        System.out.println( " getWihDelay seconds : " + delaySeconds);
        Thread.sleep(1000*delaySeconds);
        return getServerInfo();
    }

    private String getServerInfo() throws UnknownHostException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Host: ")
            .append(InetAddress.getLocalHost()
                .getHostName())
            .append("<br/>");
        stringBuilder.append("IP: ")
            .append(InetAddress.getLocalHost()
                .getHostAddress())
            .append("<br/>");
        stringBuilder.append("Type: ")
            .append("Travel Agency " + ++counter)
            .append("<br/>");
        return stringBuilder.toString();
    }
}
