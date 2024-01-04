package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
       User user=new User();
       user.setUsername(username);
       user.setPassword(password);
       String code= CountryName.valueOf(countryName).toCode();
       String ip=code+"."+user.getId().toString();
       user.setMaskedIp(null);
       user.setOriginalIp(ip);
        Country country=countryRepository3.findBycountryName(CountryName.valueOf(countryName));
        user.setCountry(country);
        user.setConnected(false);
       return    userRepository3.save(user);
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user=userRepository3.findById(userId).get();
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();
        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUserList().add(user);

        serviceProviderRepository3.save(serviceProvider);
        return userRepository3.save(user);
    }
}
