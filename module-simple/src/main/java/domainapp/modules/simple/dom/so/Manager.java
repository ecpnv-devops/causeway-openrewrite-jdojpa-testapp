package domainapp.modules.simple.dom.so;

import java.util.Set;

import javax.inject.Named;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.Collection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import domainapp.modules.simple.SimpleModule;

@PersistenceCapable(schema = SimpleModule.SCHEMA)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Named(SimpleModule.NAMESPACE + ".Manager")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(onlyExplicitlyIncluded = true)
public class Manager extends Person {

    @Collection
    @Persistent(mappedBy = "manager", dependentElement = "true")
    @Getter @Setter
    private Set<Person> manages;

    @Action
    public Manager addManages(Person managesPerson) {
        if (managesPerson != null) {
            manages.add(managesPerson);
            managesPerson.setManager(this);
        }
        return this;
    }
}
