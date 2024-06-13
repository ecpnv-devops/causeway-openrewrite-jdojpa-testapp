package domainapp.modules.simple.dom.cc;

import javax.inject.Named;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.Indices;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import domainapp.modules.simple.SimpleModule;

@PersistenceCapable(
        schema = "dbo"
)
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(EmailAddress.DISCRIMINATOR)
@Indices({
        @Index(
                name = "EmailAddress_emailAddress_IDX", members = { "emailAddress" })
})
@Named(EmailAddress.LOGICAL_TYPE_NAMED)
@DomainObject(nature = Nature.ENTITY)
@DomainObjectLayout
@NoArgsConstructor
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class EmailAddress extends CommunicationChannel {

    public static final String LOGICAL_TYPE_NAMED = SimpleModule.NAMESPACE + ".EmailAddress";
    public final static String DISCRIMINATOR = LOGICAL_TYPE_NAMED;

    @ObjectSupport
    public String title() {
        return getEmailAddress();
    }

    @Column(allowsNull = "false", length = EmailType.Meta.MAX_LEN)  // ESTUP2-183 v2: framework throws a metamodel validation exception if inconsistent; it needs to become more sophisticated to handle superclass tables
    @Property(optionality = Optionality.MANDATORY)
    @Getter @Setter
    private String emailAddress;

}