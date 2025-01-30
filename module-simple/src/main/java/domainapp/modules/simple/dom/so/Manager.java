package domainapp.modules.simple.dom.so;

import java.util.Set;

import javax.inject.Named;
import javax.jdo.annotations.Persistent;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.Collection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import domainapp.modules.simple.SimpleModule;

@Named(SimpleModule.NAMESPACE + ".Manager")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(onlyExplicitlyIncluded = true)
public class Manager extends Person {

    @Collection
    @Persistent(mappedBy = "manager", dependentElement = "true")
    @Getter @Setter
    private Set<Person> manages;

    @Action
    public void addManages(Person managesPerson) {
        if (managesPerson != null) {
            manages.add(managesPerson);
            managesPerson.setManager(this);
        }
    }
}
