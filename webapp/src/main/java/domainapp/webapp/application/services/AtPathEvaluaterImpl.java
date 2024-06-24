package domainapp.webapp.application.services;

import domainapp.modules.simple.dom.so.SimpleObject;

import org.apache.causeway.extensions.secman.applib.tenancy.spi.ApplicationTenancyEvaluator;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;

import org.springframework.stereotype.Service;

@Service
public class AtPathEvaluaterImpl implements ApplicationTenancyEvaluator {

    public boolean handles(Class<?> cls) {
        return cls == SimpleObject.class;
    }

    @Override
    public String hides(Object domainObject, ApplicationUser applicationUser) {
        SimpleObject object = (SimpleObject) domainObject;
        return object.getName().startsWith("F") ? "hidden" : null;
    }

    @Override
    public String disables(Object domainObject, ApplicationUser applicationUser) {
        return "";
    }
}
