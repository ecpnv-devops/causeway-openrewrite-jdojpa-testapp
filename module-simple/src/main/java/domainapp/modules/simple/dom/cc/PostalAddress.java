package domainapp.modules.simple.dom.cc;

import java.util.Objects;
import java.util.function.Predicate;

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
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.util.TitleBuffer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import domainapp.modules.simple.SimpleModule;

@PersistenceCapable(
        schema = "dbo"
)
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(PostalAddress.DISCRIMINATOR)
@Indices({
        @Index(
                name = "PostalAddress_main_idx",
                members = { "address1", "postalCode", "city" })
})
@Named(PostalAddress.LOGICAL_TYPE_NAMED)
@DomainObject(nature = Nature.ENTITY)
@DomainObjectLayout
@NoArgsConstructor
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class PostalAddress extends CommunicationChannel {

    public static final String LOGICAL_TYPE_NAMED = SimpleModule.NAMESPACE + ".PostalAddress";
    static final String DISCRIMINATOR = LOGICAL_TYPE_NAMED;

    @UtilityClass
    public static final class AddressLineType {
        /**
         * @deprecated - use {@link Meta#MAX_LEN} instead
         */
        @Deprecated
        public static final int MAX_LEN = Meta.MAX_LEN;

        @UtilityClass
        public static class Meta {
            public static final int MAX_LEN = 100;
        }
    }


    @Programmatic
    public String asAddressLine() {
        final StringBuffer buf = new StringBuffer();
        appendOnLine(buf, getAddress1());
        appendOnLine(buf, getAddress2());
        appendOnLine(buf, getAddress3());
        appendOnLine(buf, getCity());
        appendOnLine(buf, getPostalCode());
        return buf.toString();
    }

    private void appendOnLine(final StringBuffer buf, final String str) {
        if (str == null) {
            return;
        }
        if(buf.length() > 0) {
            buf.append(", ");
        }
        buf.append(str);
    }

    @ObjectSupport
    public String title() {
        return new TitleBuffer()
                .append(getAddress1())
                .append(getAddress2())
                .append(", ", getCity())
                .toString();
    }


    @Column(allowsNull = "false", length = AddressLineType.Meta.MAX_LEN)  // ESTUP2-183 v2: framework throws a metamodel validation exception if inconsistent; it needs to become more sophisticated to handle superclass tables
    @Property(optionality = Optionality.MANDATORY)
    @PropertyLayout(named = "Address line 1")
    @Getter @Setter
    private String address1;


    @Column(allowsNull = "true", length = AddressLineType.Meta.MAX_LEN)
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(named = "Address line 2")
    @Getter @Setter
    private String address2;


    @Column(allowsNull = "true", length = AddressLineType.Meta.MAX_LEN)
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(named = "Address line 3")
    @Getter @Setter
    private String address3;


    @Column(allowsNull = "false", length = PostalCodeType.Meta.MAX_LEN) // ESTUP2-183 v2: framework throws a metamodel validation exception if inconsistent; it needs to become more sophisticated to handle superclass tables
    @Property(optionality = Optionality.MANDATORY)
    @Getter @Setter
    private String postalCode;


    @Column(allowsNull = "false", length = 50) // ESTUP2-183 v2: framework throws a metamodel validation exception if inconsistent; it needs to become more sophisticated to handle superclass tables
    @Property(optionality = Optionality.MANDATORY)
    @Getter @Setter
    private String city;


    @UtilityClass
    public static final class PostalCodeType {
        /**
         * @deprecated - use {@link Meta#MAX_LEN} instead
         */
        @Deprecated
        public static final int MAX_LEN = Meta.MAX_LEN;

        @UtilityClass
        public static class Meta {
            public static final int MAX_LEN = 12;
        }
    }



    public PostalAddress changePostalAddress(
            final String addressLine1,
            final String addressLine2,
            final String addressLine3,
            final String city,
            final String postalCode) {
        setAddress1(addressLine1);
        setAddress2(addressLine2);
        setAddress3(addressLine3);
        setCity(city);
        setPostalCode(postalCode);

        return this;
    }
    public String default0ChangePostalAddress() {
        return getAddress1();
    }
    public String default1ChangePostalAddress() {
        return getAddress2();
    }
    public String default2ChangePostalAddress() {
        return getAddress3();
    }
    public String default3ChangePostalAddress() {
        return getCity();
    }
    public String default4ChangePostalAddress() {
        return getPostalCode();
    }


    public static class Predicates {

        private Predicates() {
        }

        public static Predicate<PostalAddress> equalTo(
                final String address1,
                final String postalCode,
                final String city) {
            return input -> Objects.equals(address1, input.getAddress1()) &&
                    Objects.equals(postalCode, input.getPostalCode()) &&
                    Objects.equals(city, input.getCity());
        }

        public static Predicate<PostalAddress> equalTo(
                final String address1,
                final String address2,
                final String address3,
                final String postalCode,
                final String city) {
            return input -> Objects.equals(address1, input.getAddress1()) &&
                    Objects.equals(address2, input.getAddress2()) &&
                    Objects.equals(address3, input.getAddress3()) &&
                    Objects.equals(postalCode, input.getPostalCode()) &&
                    Objects.equals(city, input.getCity());
        }
    }
}