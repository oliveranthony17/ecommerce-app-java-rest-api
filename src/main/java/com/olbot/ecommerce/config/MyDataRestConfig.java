package com.olbot.ecommerce.config;

import com.olbot.ecommerce.entity.Product;
import com.olbot.ecommerce.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration // makes Spring pick this up
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) { // effectively injecting JPA entity manager
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        // want to disable for Product: PUT, POST, DELETE

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                // apply to Product repository
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                // apply to Product repository
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

        // RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        // call an internal helper method to expose ids
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // get a list of all entity classes from entity manager
        Set<EntityType<?>> entites = entityManager.getMetamodel().getEntities();
        // create an empty array to fill with entity types
        List<Class> entityClasses = new ArrayList<>();
        // get entity types for the entities
        for (EntityType tempEntityType : entites) {
            entityClasses.add(tempEntityType.getJavaType());
        }
        // expose entity ids for the array of entity types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
