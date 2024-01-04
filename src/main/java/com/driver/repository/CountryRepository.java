package com.driver.repository;

import com.driver.model.Country;
import com.driver.model.CountryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query(value="select * from country where country_name=?1;",nativeQuery = true)
    Country findBycountryName(CountryName countryName);
}
