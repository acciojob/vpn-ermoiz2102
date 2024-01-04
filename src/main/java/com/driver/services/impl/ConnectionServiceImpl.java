package com.driver.services.impl;

import com.driver.alreadyConnected;
import com.driver.cannotEstablish;
import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import com.driver.unableToConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Autowired
    CountryRepository countryRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
      User user=userRepository2.findById(userId).get();
      if(user.getConnected()==true)
          throw new alreadyConnected("Already connected");
      CountryName countryName1=null;
      try{
          countryName1=CountryName.valueOf(countryName.toUpperCase());
      }catch (Exception e){
          throw new unableToConnect("Unable to connect");
      }

      Country country=countryRepository2.findBycountryName(countryName1);
      if(user.getCountry().equals(country))
          return user;
      if(user.getServiceProviderList().size()==0)
          throw new unableToConnect("Unable to connect");

      ServiceProvider serviceProvider1=null;
        if(user.getServiceProviderList().size()==1&&!user.getServiceProviderList().get(0).getCountryList().contains(country))
            throw new unableToConnect("Unable to connect");
        List<ServiceProvider>ServiceProviderList=user.getServiceProviderList();
        int id=Integer.MAX_VALUE;

        for(ServiceProvider serviceProvider:ServiceProviderList){
            if(serviceProvider.getCountryList().contains(country)&&serviceProvider.getId()<id)
                serviceProvider1=serviceProvider;
            id=serviceProvider.getId();
        }
        if(serviceProvider1==null)
            throw new unableToConnect("Unable to connect");

        String Ip=CountryName.valueOf(countryName).toCode()+"."+serviceProvider1.getId().toString()+"."+user.getId().toString();
        user.setMaskedIp(Ip);
        user.setConnected(true);
        Connection connection= new Connection();
        connection.setUser(user);
        connection.setServiceProvider(serviceProvider1);

        user.getConnectionList().add(connection);
        serviceProvider1.getConnectionList().add(connection);

        connectionRepository2.save(connection);

        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user=userRepository2.findById(userId).get();
        if(user.getConnected()==false)
            throw new unableToConnect("Already disconnected");
        user.setConnected(false);
        user.setMaskedIp(null);
        return userRepository2.save(user);

    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
   User user1=userRepository2.findById(senderId).get();
   User user2=userRepository2.findById(receiverId).get();
   Country country1=null;
        if(user1.getConnected()==true){
            String ip=user1.getMaskedIp().substring(0,3);
            CountryName countryName2=CountryName.valueOf(ip);
            country1=countryRepository2.findBycountryName(countryName2);

        }
        else{
            country1=user2.getCountry();
        }
   if(user1.getCountry().equals(country1))
       return user1;
   if(user1.getConnected()==true){
       String ip=user1.getMaskedIp().substring(0,3);
       CountryName countryName1=CountryName.valueOf(ip);
       Country country=countryRepository2.findBycountryName(countryName1);
       if(!country.equals(country1))
           throw new cannotEstablish("Cannot establish communication");
       else
           return user1;
   }
   else{
       ServiceProvider serviceProvider1=null;
       List<ServiceProvider>ServiceProviderList=user1.getServiceProviderList();
       int id=Integer.MAX_VALUE;

       for(ServiceProvider serviceProvider:ServiceProviderList){
           if(serviceProvider.getCountryList().contains(country1)&&serviceProvider.getId()<id)
               serviceProvider1=serviceProvider;
           id=serviceProvider.getId();
       }
       if(serviceProvider1==null)
           throw new cannotEstablish("Cannot establish communication");

       String Ip=country1.getCode()+"."+serviceProvider1.getId().toString()+"."+user1.getId().toString();
       user1.setMaskedIp(Ip);
       user1.setConnected(true);
       Connection connection= new Connection();
       connection.setUser(user1);
       connection.setServiceProvider(serviceProvider1);

       user1.getConnectionList().add(connection);
       serviceProvider1.getConnectionList().add(connection);

       connectionRepository2.save(connection);

       return user1;

   }

    }
}
