package com.driver.repository;

import com.driver.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {
    @Query(value = "select * from service_provider where name=?1;",nativeQuery = true)
    ServiceProvider findproviderbyname(String name);
}
