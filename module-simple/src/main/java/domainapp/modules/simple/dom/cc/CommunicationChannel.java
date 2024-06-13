package domainapp.modules.simple.dom.cc;

import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.title.TitleService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import domainapp.modules.simple.SimpleModule;

@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE
        , schema = "dbo"
)
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@javax.jdo.annotations.Discriminator(
        strategy = DiscriminatorStrategy.VALUE_MAP,
        column = "discriminator")
@javax.jdo.annotations.DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Named(SimpleModule.NAMESPACE + ".CommunicationChannel") // because of security (when shown in collections)
@DomainObject(nature = Nature.ENTITY)
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_CHILD)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@NoArgsConstructor
public abstract class CommunicationChannel
        implements Comparable<CommunicationChannel> {


    //endregion> constructors and builders

    @UtilityClass
    public static class EmailType {
//        /**
//         * @deprecated use {@link Meta#REGEX} instead
//         */
//        @Deprecated
//        public static final String REGEX = Meta.REGEX;
//        /**
//         * @deprecated use {@link Meta#REGEX_DESC} instead
//         */
//        @Deprecated
//        public static final String REGEX_DESC = Meta.REGEX_DESC;
//        /**
//         * @deprecated use {@link Meta#MAX_LEN} instead
//         */
//        @Deprecated
//        public final static int MAX_LEN = Meta.MAX_LEN;

        @UtilityClass
        public static class Meta {
            //
            // as per http://emailregex.com/
            //
            // better would probably be:
            // (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
            //
            public static final String REGEX = "[^@ ]*@{1}[^@ ]*[.]+[^@ ]*";

            public static final String REGEX_DESC = "Only one \"@\" symbol is allowed, followed by a domain e.g. test@example.com";

            //
            //http://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
            //
            public final static int MAX_LEN = 254;
        }
    }

    public abstract String title();


    @Property
    @PropertyLayout(hidden = Where.OBJECT_FORMS)
    @lombok.ToString.Include
    public String getName() {
        return titleService.titleOf(this);
    }


    @Column(length = 2000)
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(multiLine = 3, hidden = Where.ALL_TABLES)
    @Getter @Setter @lombok.ToString.Include
    private String description;


    @Override
    public int compareTo(final CommunicationChannel other) {
        return Comparator
                .comparing(     CommunicationChannel::title, defaultOrder())
                .compare(this, other);
    }

    public static <T extends Comparable<? super T>> Comparator<T> defaultOrder() {
        return Comparator.nullsFirst(Comparator.naturalOrder());
    }

    @Inject @NotPersistent TitleService titleService;

}