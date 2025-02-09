package domainapp.modules.simple.dom.so;

import java.util.Comparator;
import java.util.Set;

import javax.inject.Named;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import javax.validation.constraints.Email;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.TableDecorator;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import domainapp.modules.simple.SimpleModule;

@PersistenceCapable(schema = SimpleModule.SCHEMA)
@Uniques({
        @Unique(name = "Person__name__UNQ", members = {"firstName", "lastName"}),
        @Unique(name = "Person__email__UNQ", members = {"email"})
})
@Query(
        name = Person.NAMED_QUERY__FIND_BY_EMAIL,
        value = "SELECT " +
                "FROM domainapp.modules.simple.dom.so.Person " +
                "WHERE email == :email"
)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@Named(SimpleModule.NAMESPACE + ".Person")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout(
        tableDecorator = TableDecorator.DatatablesNet.class,
        bookmarking = BookmarkPolicy.AS_ROOT)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Person implements Comparable<Person> {

    static final String NAMED_QUERY__FIND_BY_EMAIL = "Person.findByEmail";

    @Title(sequence = "1.0")
    @PropertyLayout(hidden = Where.NOWHERE)
    @Getter @Setter @ToString.Include
    private String firstName;

    @Title(sequence = "2.0")
    @Column(allowsNull = "true")
    @Property(optionality = Optionality.OPTIONAL)
    @Getter @Setter @ToString.Include
    private String lastName;

    @Email
    @PropertyLayout(hidden = Where.NOWHERE)
    @Getter @Setter
    private String email;

    @Persistent(mappedBy = "owner")
    @Getter @Setter
    private Set<SimpleObject> simpleObjects;

    @Override
    public int compareTo(Person other) {
        return Comparator
                .comparing(Person::getEmail)
                .compare(this, other);
    }

    public void addSimpleObject(SimpleObject simpleObject) {
        if (simpleObjects != null) {
            simpleObjects.add(simpleObject);
            simpleObject.setOwner(this);
        }
    }
}
